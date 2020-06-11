package jsd.financialcontrol.action.fybx;

import jsd.financialcontrol.services.FactDataServiceStub;
import jsd.financialcontrol.util.FinalUtil;
import jsd.financialcontrol.util.GetUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * createby jianyong.tang
 * createTime 2020/5/24 17:21
 * version v1
 * desc
 */
public class FreezeBudgetForBxAction implements Action {
    @Override
    public String execute(RequestInfo info) {
        RecordSet rs = new RecordSet();
        String requestid = info.getRequestid();
        String tablename = info.getRequestManager().getBillTableName();
        new BaseBean().writeLog("aaaaaaa");
        new BaseBean().writeLog(this.getClass().getName(),"requestid:"+requestid);

        String str = "JST007007000000000";//项目地的固定数据
        String mainid = "";
        String xmlb = "";//项目类表别 0制单相关 0制单相关 1项目地相关 2部门相关 3 职能专项相关
        String orgname ="";
        String orgcode = "";
        String projectname ="";
        String projectcode = "";
        String kmname ="";
        String kmcode = "";
        String zhname = "";
        String fyfsrq = "";//费用发生日期
        String year = "";
        String month = "";
        String je = "";
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
            sql = "select * from " + tablename + "_dt3 where mainid=" + mainid;
            rs.execute(sql);
            while (rs.next()) {
                xmlb = Util.null2String(rs.getString("xmlb"));
                fyfsrq = Util.null2String(rs.getString("fyfsrq"));
                je = Util.null2String(rs.getString("hsje"));
                if (!"".equals(fyfsrq)) {
                    year = fyfsrq.substring(0, 4);
                    month = fyfsrq.substring(5, 7);
                    if ("0".equals(month.substring(0, 1))) {
                        month = month.substring(1);
                    }
                    year = year + "年";
                    month = month + "月";
                }
                if ("2".equals(xmlb)) {
                    orgname = Util.null2String(rs.getString("empzzmc"));
                    projectname = Util.null2String(rs.getString("xmmc"));
                    if("".equals(projectname)){
                        projectname = "不分项目";
                    }
                    kmname= gu.getFieldVal("uf_kindeedata", "fname", "fnumber", Util.null2String(rs.getString("fcostid")) + "' and flag = '1");//科目名称
                    zhname = "不分综合";
                }
                JSONArray ja = new JSONArray();
                ja.put(new JSONObject("{\"name\":\""+orgname+"\",\"code\":\"\"}"));//组织
                ja.put(new JSONObject("{\"name\":\""+projectname+"\",\"code\":\"\"}"));//项目
                ja.put(new JSONObject("{\"name\":\""+kmname+"\",\"code\":\"\"}"));//科目
                ja.put(new JSONObject("{\"name\":\""+year+"\",\"code\":\"\"}"));//年
                ja.put(new JSONObject("{\"name\":\""+month+"\",\"code\":\"\"}"));//期间
                ja.put(new JSONObject("{\"name\":\""+zhname+"\",\"code\":\"\"}"));//综合
                ja.put(new JSONObject("{\"name\":\"实际在途\",\"code\":\"\"}"));//场景
                ja.put(je);//
                ja.put(1);//
                datas.put(ja);

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
