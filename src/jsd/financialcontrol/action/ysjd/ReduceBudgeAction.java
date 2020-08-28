package jsd.financialcontrol.action.ysjd;

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
* @version 1.0 职能中心专项预算资金解冻申请   解冻
*/
public class ReduceBudgeAction implements Action {
    @Override
    public String execute(RequestInfo info) {
        RecordSet rs = new RecordSet();
        String requestid = info.getRequestid();
        String tablename = info.getRequestManager().getBillTableName();
        new BaseBean().writeLog(this.getClass().getName(),"requestid:"+requestid);
        GetUtil gu = new GetUtil();
        String orgname ="";
        String projectname ="";
        String kmname ="";
        String zhname = "";
        String ysuanbianma = "";//预算编码----寻对应的项目  uf_zxysbmzjjd
        String bcsqsyysje = "";// 本次申请使用预算金额
        String year = gu.getDateNow().substring(0,4)+'年';
        String resultcode = "";
        String resultmessage = "";
        String sqrbm = "";//申请人部门
        String month = "不分期间";
        String sql = "select ysuanbianma,bcsqsyysje,sqrbm from "+tablename+" where requestid="+requestid;
        rs.execute(sql);
        if(rs.next()){
            ysuanbianma = Util.null2String(rs.getString("ysuanbianma"));
            bcsqsyysje = Util.null2String(rs.getString("bcsqsyysje"));
            sqrbm = Util.null2String(rs.getString("sqrbm"));
            
        }
        String uniqueMembers = "[{\"版本\":{\"name\":\"执行控制版\",\"code\":\"\"}}]";
        JSONObject dataMembers = new JSONObject();
        String dims[] = new String[]{"组织", "项目", "科目", "年", "期间", "综合", "场景"};
        JSONArray datas = new JSONArray();
        orgname = gu.getFieldVal("hrmdepartment", "departmentname", "id", sqrbm);//部门名称
        String xmmc = gu.getFieldVal("uf_zxysbmzjjd", "mingcheng", "id", ysuanbianma);//项目名称
        String xmbm = gu.getFieldVal("uf_zxysbmzjjd", "bianma", "id", ysuanbianma);//项目编码
        zhname = "不分综合";
        kmname = "不分科目";
        projectname = xmmc;
        if(xmbm.startsWith("CEGC")&& projectname.length()>0) {
        	zhname = projectname.substring(0, projectname.lastIndexOf("-"))+".";
        }
        try {
        	JSONArray ja1 = new JSONArray();
            ja1.put(new JSONObject("{\"name\":\""+orgname+"\",\"code\":\"\"}"));//组织
            ja1.put(new JSONObject("{\"name\":\""+projectname+"\",\"code\":\"\"}"));//项目
            ja1.put(new JSONObject("{\"name\":\""+kmname+"\",\"code\":\"\"}"));//科目
            ja1.put(new JSONObject("{\"name\":\""+year+"\",\"code\":\"\"}"));//年
            ja1.put(new JSONObject("{\"name\":\""+month+"\",\"code\":\"\"}"));//期间
            ja1.put(new JSONObject("{\"name\":\""+zhname+"\",\"code\":\"\"}"));//综合
            ja1.put(new JSONObject("{\"name\":\"预算\",\"code\":\"\"}"));//场景
            ja1.put(bcsqsyysje);//
            ja1.put(1);//
            datas.put(ja1);
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

