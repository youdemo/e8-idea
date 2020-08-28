package jsd.financialcontrol.action.clbx;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import jsd.financialcontrol.services.FactDataServiceStub;
import jsd.financialcontrol.util.FinalUtil;
import jsd.financialcontrol.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月27日 上午10:55:15 
* @version 1.0 差旅报销扣减
*/
public class ReduceBudgeAction implements Action {
    @Override
    public String execute(RequestInfo info) {
        RecordSet rs = new RecordSet();
        String requestid = info.getRequestid();
        String tablename = info.getRequestManager().getBillTableName();
        new BaseBean().writeLog(this.getClass().getName(),"requestid:"+requestid);

        String mainid = "";
        String xmlb = "";//项目类表别 0制单相关 1项目地相关 2部门相关 3 职能专项相关
        String orgname ="";
//        String orgcode = "";
        String projectname ="";
//        String projectcode = "";
        String kmname ="";
//        String kmcode = "";
        String zhname = "";
        String fyfsrq = "";//费用发生日期
        String zxysbm = "";//专项预算编码----寻对应的项目
        String year = "";
        String month = "";
        String je = "";
        String xmbm = "";
        String resultcode = "";
        String resultmessage = "";
        String sql = "select id from "+tablename+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            mainid = Util.null2String(rs.getString("id"));
        }
        String uniqueMembers = "[{\"版本\":{\"name\":\"执行控制版\",\"code\":\"\"}}]";
        JSONObject dataMembers = new JSONObject();
        String dims[] = new String[]{"组织", "项目", "科目", "年", "期间", "综合", "场景"};
        JSONArray datas = new JSONArray();
        GetUtil gu = new GetUtil();
        try {
            sql = "select * from " + tablename + "_dt6 where mainid=" + mainid;
            rs.execute(sql);
            while (rs.next()) {
                xmlb = Util.null2String(rs.getString("xmlb"));
                fyfsrq = Util.null2String(rs.getString("fyfsrq"));
                je = Util.null2String(rs.getString("bxje"));
                if (!"".equals(fyfsrq)) {
                    year = fyfsrq.substring(0, 4);
                    month = fyfsrq.substring(5, 7);
                    if ("0".equals(month.substring(0, 1))) {
                        month = month.substring(1);
                    }
                    year = year + "年";
                    month = month + "月";
                }
                zhname = "不分综合";
                orgname = Util.null2String(rs.getString("empzzmc"));
                kmname= gu.getKMFieldVal("uf_kindeedata", "fname", "fnumber", Util.null2String(rs.getString("fcostid")) + "' and flag = '10");//科目名称
                if ("0".equals(xmlb)) {
                    projectname = Util.null2String(rs.getString("xmmc"));
                    if("".equals(projectname)){
                        projectname = "不分项目";
                    }
                   
                    kmname = "项目"+kmname;
                }else if ("1".equals(xmlb)) {
                	 orgname = FinalUtil.xmdzz;
                     projectname = "不分项目";
                }else if ("2".equals(xmlb)) {
                    projectname = "不分项目";
                }
                if ("3".equals(xmlb)) {
                    xmbm =  Util.null2String(rs.getString("xmbm")); 
                    zxysbm = Util.null2String(rs.getString("zxysbmnew")); 
                    String zxysbmname = gu.getFieldVal("uf_kindeedata", "fname", "fnumber", zxysbm+"' and flag=7 and useing='0");
//                    kmname= zxysbmname;
                    kmname = "专项"+kmname;
                    projectname = zxysbmname;
                    if(zxysbm.startsWith("CEGC")) {
                    	zhname = zxysbmname.substring(0, zxysbmname.lastIndexOf("-"))+".";
//                    	projectname = "不分项目";
                    	kmname = "不分科目";
                    }
                }
                JSONArray ja1 = new JSONArray();
                ja1.put(new JSONObject("{\"name\":\""+orgname+"\",\"code\":\"\"}"));//组织
                ja1.put(new JSONObject("{\"name\":\""+projectname+"\",\"code\":\"\"}"));//项目
                ja1.put(new JSONObject("{\"name\":\""+kmname+"\",\"code\":\"\"}"));//科目
                ja1.put(new JSONObject("{\"name\":\""+year+"\",\"code\":\"\"}"));//年
                ja1.put(new JSONObject("{\"name\":\""+month+"\",\"code\":\"\"}"));//期间
                ja1.put(new JSONObject("{\"name\":\""+zhname+"\",\"code\":\"\"}"));//综合
                ja1.put(new JSONObject("{\"name\":\"实际在途\",\"code\":\"\"}"));//场景
                ja1.put(je);//
                ja1.put(2);//
                JSONArray ja2 = new JSONArray();
                ja2.put(new JSONObject("{\"name\":\""+orgname+"\",\"code\":\"\"}"));//组织
                ja2.put(new JSONObject("{\"name\":\""+projectname+"\",\"code\":\"\"}"));//项目
                ja2.put(new JSONObject("{\"name\":\""+kmname+"\",\"code\":\"\"}"));//科目
                ja2.put(new JSONObject("{\"name\":\""+year+"\",\"code\":\"\"}"));//年
                ja2.put(new JSONObject("{\"name\":\""+month+"\",\"code\":\"\"}"));//期间
                ja2.put(new JSONObject("{\"name\":\""+zhname+"\",\"code\":\"\"}"));//综合
                ja2.put(new JSONObject("{\"name\":\"实际确认\",\"code\":\"\"}"));//场景
                ja2.put(je);//
                ja2.put(1);//
                datas.put(ja1);
                datas.put(ja2);

            }
            dataMembers.put("dims",dims);
            dataMembers.put("datas",datas);
            new BaseBean().writeLog(this.getClass().getName(),"uniqueMembers:"+uniqueMembers);
            new BaseBean().writeLog(this.getClass().getName(),"dataMembers:"+dataMembers);

            try {
                Map<String,String > result = sendData(uniqueMembers, dataMembers.toString());
                resultcode = result.get("resultcode");
                resultmessage = result.get("resultmessage");
                new BaseBean().writeLog(this.getClass().getName(),"resultcode:"+resultcode+" resultmessage:"+resultmessage);
            }catch (Exception e){
                new BaseBean().writeLog(this.getClass().getName(),"接口调用异常");
                new BaseBean().writeLog(this.getClass().getName(),e);
            }

        }catch (Exception e){
            new BaseBean().writeLog(this.getClass().getName(),e);
        }
        if(!"2000".equals(resultcode)){
            info.getRequestManager().setMessagecontent("调用接口失败:"+resultmessage);
            info.getRequestManager().setMessageid("ErrorMsg:");
            return FAILURE_AND_CONTINUE;
        }

        return SUCCESS;
    }

    public Map<String,String > sendData(String uniqueMembers,String dataMembers) throws Exception {
        Map<String,String > result = new HashMap<String, String>();
        FactDataServiceStub fdss = new FactDataServiceStub();
        FactDataServiceStub.Header header = new FactDataServiceStub.Header();
        FactDataServiceStub.SoapHeader soapheader = new FactDataServiceStub.SoapHeader();
        soapheader.setAppcode(FinalUtil.appcode);
        soapheader.setPassword(FinalUtil.password);
        soapheader.setType(0);//待定
        soapheader.setUsername(FinalUtil.username);
        header.setHeader(soapheader);
        FactDataServiceStub.UpdateFactData ufd = new FactDataServiceStub.UpdateFactData();
        ufd.setUniqueMembers(uniqueMembers);
        ufd.setDataMembers(dataMembers);
        ufd.setCubeName("模型一");
        FactDataServiceStub.UpdateFactDataResponse res = fdss.updateFactData(ufd,header);
        result.put("resultcode",res.get_return().getResultCode()+"");
        result.put("resultmessage",res.get_return().getResultMessage());
        return result;

    }
}

