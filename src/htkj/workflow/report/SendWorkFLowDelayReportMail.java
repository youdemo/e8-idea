package htkj.workflow.report;

import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFCell;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFRow;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFSheet;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFWorkbook;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.system.SystemComInfo;

import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * createby jianyong.tang
 * createTime 2020/7/29 15:44
 * version v1
 * desc 每周发送上周的流程延期邮件
 */
public class SendWorkFLowDelayReportMail extends BaseCronJob {
    public void execute() {
        BaseBean log = new BaseBean();
        log.writeLog(this.getClass().getName(),"开始定时发送流程延期邮件");
        //此处添加需要定时执行的代码
        String startDate = "";//开始日期
        String endDate = "";//结束日期
        RecordSet rs = new RecordSet();
        String emails = "";
        String flag = ",";
        String roleid = Util.null2o(weaver.file.Prop.getPropValue("wfyqemailjs", "jsid"));
        String sql = "select to_char(sysdate-7,'yyyy-MM-dd') as startdate,to_char(sysdate-1,'yyyy-MM-dd') as enddate from dual";
        rs.execute(sql);
        if(rs.next()){
            startDate = Util.null2String(rs.getString("startdate"));
            endDate = Util.null2String(rs.getString("endDate"));
        }
        if("".equals(roleid)){
            log.writeLog(this.getClass().getName(),"配置的角色id没找到");
            return;
        }
        sql = "select distinct b.email from hrmrolemembers a,hrmresource b where a.resourceid=b.id and a.roleid in("+roleid+")";
        rs.execute(sql);
        while(rs.next()){
            String email = Util.null2String(rs.getString("email"));
            if(!"".equals(email)){
                emails = emails+flag+email;
            }
        }
        if("".equals(emails)){
            log.writeLog(this.getClass().getName(),"发件人为空");
            return;
        }
        getSendData(startDate,endDate,emails);
        log.writeLog(this.getClass().getName(),"定时发送流程延期邮件结束");
    }

    public boolean getSendData(String startDate,String endDate,String emails){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        String showdate = startDate+"~"+endDate;
        RecordSet rs = new RecordSet();
        String sql = "select logid,workflowid,'"+showdate+"' as lastoperatedate,departmentid,(select departmentname from hrmdepartment where id=t.departmentid) as departmentname,cssl,( select workflowname from workflow_base where id=t.workflowid) as workflowname "+
                " from (" +
                "   select max(logid) as logid,a.workflowid,c.departmentid,count(1) as cssl" +
                "  from workflow_requestbase a, workflow_requestlog b,hrmresource c" +
                " where a.requestid = b.requestid" +
                "   and a.currentnodetype >= 3  " +
                "    and a.workflowid in (select distinct lcid from uf_workflow_csmt)" +
                "   and b.operator=c.id";
        if(!"".equals(startDate)){
            sql+=" and a.lastoperatedate >='"+startDate+"' ";

        }
        if(!"".equals(endDate)) {
            sql += " and a.lastoperatedate <='" + endDate + "' ";
        }
        sql	+="   and b.nodeid in (select distinct jdid from uf_workflow_csmt)" +
                "   and b.logtype in ('0','2','3')" +
                "   and f_ht_checkiscs(b.requestid,b.nodeid,b.operatedate,b.operatetime,b.logid)='1'" +
                "   group by a.workflowid,c.departmentid ) t order by cssl desc,workflowid asc,departmentid asc";
        rs.execute(sql);
        int xh =1;
        int allcount =0;
        while(rs.next()){
            Map<String,String> map = new HashMap<String, String>();
            allcount = allcount + rs.getInt("cssl");
            map.put("xh",xh+"");
            map.put("workflowname",Util.null2String(rs.getString("workflowname")));
            map.put("lastoperatedate",Util.null2String(rs.getString("lastoperatedate")));
            map.put("departmentname",Util.null2String(rs.getString("departmentname")));
            map.put("cssl",Util.null2String(rs.getString("cssl")));
            list.add(map);
            xh++;
        }
        boolean result = sendmail(emails,list,showdate,allcount);
        return result;



    }

    public boolean sendmail(String mails, List<Map<String,String>> list,String showdata,int allcount){
        BaseBean log = new BaseBean();
        log.writeLog(this.getClass().getName(),"发送邮件 mails:"+mails+" listsize:"+list.size()+" showdata:"+showdata+" allcount:"+allcount);
        SystemComInfo sci = new SystemComInfo();
        String mailip = sci.getDefmailserver();
        String mailuser = sci.getDefmailuser();
        String password = sci.getDefmailpassword();
        String needauth = sci.getDefneedauth();// 是否需要发件认证
        String mailfrom = sci.getDefmailfrom();
        SendMail sm = new SendMail();
        sm.setMailServer(mailip);// 邮件服务器IP
        if (needauth.equals("1")) {
            sm.setNeedauthsend(true);
            sm.setUsername(mailuser);// 服务器的账号
            sm.setPassword(password);// 服务器密码
        } else {
            sm.setNeedauthsend(false);
        }
        StringBuffer content = new StringBuffer();
        content.append("<div>&nbsp;&nbsp;&nbsp;&nbsp;各位领导，附件为本期的OA流程超时数据，涉及到的相关部门总监请输出改善对策。");
        content.append("<br/>");
        content.append("&nbsp;&nbsp;&nbsp;&nbsp;快速响应。</div>");
        content.append("<br/>");
        content.append("<div><a href=\"http://oa.ht-tech.com:8090/htkj/workflow/report/ht-workflow-cs-person-Url.jsp\" target=\"_blank\">超时统计报表：个人维度</a>");
        content.append("&nbsp;&nbsp;<a href=\"http://oa.ht-tech.com:8090/htkj/workflow/report/ht-workflow-cs-node-Url.jsp\" target=\"_blank\">超时统计报表：节点维度</a></div>");
        content.append("<br/>");
        content.append("<div><table border=\"2\"  style=\"width: 610px;border-collapse: collapse;font-size:12px;\"> ");
        content.append("<tr>");
        content.append("	<td style=\"background: red;text-align:center;width:30px;\" colspan=\"5\"><strong>OA红榜</strong></td>" );
        content.append("</tr>");
        content.append("<tr>");
        content.append("	<td style=\"background: red;text-align:center;width:50px;\"><strong>序号</strong></td>" );
        content.append("	<td style=\"background: red;text-align:center;width:120px;\"><strong>流程名称</strong></td>" );
        content.append("	<td style=\"background: red;text-align:center;width:200px;\"><strong>归档时间</strong></td>" );
        content.append("	<td style=\"background: red;text-align:center;width:120px;\"><strong>部门</strong></td>");
        content.append("	<td style=\"background: red;text-align:center;width:120px;\"><strong>节点超时次数</strong></td>");
        content.append("</tr>");
        for(Map<String,String> map:list){
            content.append("<tr>");
            content.append("	<td style=\"text-align:center;\">"+map.get("xh")+"</td>" );
            content.append("	<td style=\"text-align:center;\">"+map.get("workflowname")+"</td>" );
            content.append("	<td style=\"text-align:center;\">"+map.get("lastoperatedate")+"</td>" );
            content.append("	<td style=\"text-align:center;\">"+map.get("departmentname")+"</td>");
            content.append("	<td style=\"text-align:center;\">"+map.get("cssl")+"</td>");
            content.append("</tr>");
        }
        content.append("<tr>");
        content.append("	<td style=\"text-align:center;\" colspan=\"4\">本期累计超时次数</td>" );
        content.append("	<td style=\"text-align:center;\">"+allcount+"</td>" );
        content.append("</tr></div>");
        content.append("<div>请在OA系统中<流程超时报表>查看相应具体数据</div>");


        boolean result = sm.sendhtml(mailfrom,mails,"","","OA红榜",content.toString(),3,"3");
        log.writeLog(this.getClass().getName(),"发送邮件结果："+result);
        return result;

    }

    public InputStream getExcelFile(List<Map<String,String>> list,int allcount) throws Exception {
        String tempfileurl = new String(weaver.file.Prop.getPropValue("wfyqemailjs","tempfile").getBytes("ISO-8859-1"),"UTF-8");
        FileInputStream fis = new FileInputStream(tempfileurl);
        XSSFWorkbook workBook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workBook.cloneSheet(0);
        XSSFCell AMOUNT_Cell = sheet.getRow(6).getCell(5);
        replaceCellValue(AMOUNT_Cell, ""+allcount);
        int datarowindex =5;
        if(list.size()>0){
            int startindex = 6;
            sheet.shiftRows(startindex,sheet.getLastRowNum(),list.size()-1, true, true);
            for (int index = 0; index < list.size(); index++) {
                Map<String, String> dt_map = list.get(index);
                String xh = Util.null2String(dt_map.get("xh"));        //行项目
                String workflowname = Util.null2String(dt_map.get("workflowname"));        //流程名称
                String lastoperatedate = Util.null2String(dt_map.get("lastoperatedate"));  //归档日期
                String departmentname = Util.null2String(dt_map.get("departmentname"));    //部门名称
                String cssl = Util.null2String(dt_map.get("cssl"));        //数量
                //插入行
                if(index==0){
                    AMOUNT_Cell = sheet.getRow(datarowindex).getCell(1);
                    replaceCellValue(AMOUNT_Cell, xh);
                    AMOUNT_Cell = sheet.getRow(datarowindex).getCell(2);
                    replaceCellValue(AMOUNT_Cell, workflowname);
                    AMOUNT_Cell = sheet.getRow(datarowindex).getCell(3);
                    replaceCellValue(AMOUNT_Cell, lastoperatedate);
                    AMOUNT_Cell = sheet.getRow(datarowindex).getCell(4);
                    replaceCellValue(AMOUNT_Cell, departmentname);
                    AMOUNT_Cell = sheet.getRow(datarowindex).getCell(5);
                    replaceCellValue(AMOUNT_Cell, cssl);
                }else {
                    XSSFRow creRow = sheet.createRow(datarowindex+index);
                    XSSFCell newCell1 = creRow.createCell(1);
                    newCell1.setCellValue(xh);
                    newCell1.setCellStyle(sheet.getRow(datarowindex).getCell(1).getCellStyle());
                    XSSFCell newCell2 = creRow.createCell(2);
                    newCell2.setCellValue(workflowname);
                    newCell2.setCellStyle(sheet.getRow(datarowindex).getCell(2).getCellStyle());
                    XSSFCell newCell3 = creRow.createCell(3);
                    newCell3.setCellValue(lastoperatedate);
                    newCell3.setCellStyle(sheet.getRow(datarowindex).getCell(3).getCellStyle());
                    XSSFCell newCell4 = creRow.createCell(4);
                    newCell4.setCellValue(departmentname);
                    newCell4.setCellStyle(sheet.getRow(datarowindex).getCell(4).getCellStyle());
                    XSSFCell newCell5 = creRow.createCell(5);
                    newCell5.setCellValue(cssl);
                    newCell5.setCellStyle(sheet.getRow(datarowindex).getCell(5).getCellStyle());
                }
            }

        }else{
            AMOUNT_Cell = sheet.getRow(datarowindex).getCell(2);
            replaceCellValue(AMOUNT_Cell, "");
            AMOUNT_Cell = sheet.getRow(datarowindex).getCell(3);
            replaceCellValue(AMOUNT_Cell, "");
            AMOUNT_Cell = sheet.getRow(datarowindex).getCell(4);
            replaceCellValue(AMOUNT_Cell, "");
            AMOUNT_Cell = sheet.getRow(datarowindex).getCell(5);
            replaceCellValue(AMOUNT_Cell, "");
        }
        workBook.removeSheetAt(0); // 模板移除
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workBook.write(bos);
        byte[] barray = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(barray);
        fis.close();
        bos.flush();
        bos.close();
        return is;

    }
    /**
     * 修改excel值
     * @param cell
     * @param value
     */
    public void replaceCellValue(XSSFCell cell, Object value) {
        String val = value != null ? String.valueOf(value) : "";
        cell.setCellValue(val);
    }

}
