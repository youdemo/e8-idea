package zhongxin.pub.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class GetTestScores implements Action {
    public String execute(RequestInfo Info) {
        BaseBean log = new BaseBean();
        log.writeLog("==================================开始==================================");
        String requestId = Info.getRequestid();
        String workflowID = Info.getWorkflowid();
        String sql = "";
        String tableName = "";

        int sjczcj = 0;
        String qysfwOQA = "";

        RecordSet rs = new RecordSet();
        try {
            ERPWebServiceServiceStub eRPWebServiceServiceStub = new ERPWebServiceServiceStub();
            ERPWebServiceServiceStub.ECertified eCertified = new ERPWebServiceServiceStub.ECertified();


            sql = "Select tablename From Workflow_bill Where id in (Select formid From workflow_base Where id = '" + workflowID + "')";
            rs.execute(sql);
            if (rs.next()) {
                tableName = Util.null2String(rs.getString("tablename"));
            }
            String EqupId = "";
            String ksrgh = "";
            sql = "SELECT (SELECT gh FROM uf_FABKSRYXX WHERE id = a.ksrgh) as ksrgh , sjczcj , qysfwOQA , (SELECT jtbh FROM uf_JTOWNER WHERE id = a.jtbh) AS jtbh , (SELECT jtbh FROM uf_JTOWNER WHERE id = a.jtbh_OQA) AS jtbh_temp FROM " +
                    tableName + " a WHERE requestId = " + requestId;
            log.writeLog(sql);
            rs.execute(sql);
            if (rs.next()) {
                ksrgh = Util.null2String(rs.getString("ksrgh"));
                sjczcj = Util.getIntValue(Util.null2String(rs.getString("sjczcj")),0);
                qysfwOQA = Util.null2String(rs.getString("qysfwOQA"));

                if ("0".equals(qysfwOQA))
                    EqupId = Util.null2String(rs.getString("jtbh_temp"));
                else if ("1".equals(qysfwOQA)) {
                    EqupId = Util.null2String(rs.getString("jtbh"));
                }

            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(1, 1);
            date = calendar.getTime();

            if (sjczcj >= 90) {
                eCertified.setFacilityId("SMEC");
                eCertified.setUserId(ksrgh);
                eCertified.setEngineerGroup("");
                eCertified.setEntityGroup("");
                eCertified.setEqupId(EqupId);
                eCertified.setExpiryDate(sdf.format(date));

                log.writeLog("FacilityId = " + eCertified.getFacilityId());
                log.writeLog("UserId = " + eCertified.getUserId());
                log.writeLog("EngineerGroup = " + eCertified.getEngineerGroup());
                log.writeLog("EntityGroup = " + eCertified.getEntityGroup());
                log.writeLog("EqupId = " + eCertified.getEqupId());
                log.writeLog("ExpiryDate = " + eCertified.getExpiryDate());

                ERPWebServiceServiceStub.ECertifiedResponse eCertifiedResponse = new ERPWebServiceServiceStub.ECertifiedResponse();
                try {
                    eCertifiedResponse = eRPWebServiceServiceStub.eCertified(eCertified);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.writeLog("错误：" + e.getMessage());
                    log.writeLog(e.toString());
                }
                java.lang.String result = eCertifiedResponse.getECertifiedReturn().toString();
                log.writeLog(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.writeLog("错误：" + e.getMessage());
            log.writeLog(e.toString());
        }

        log.writeLog("==================================结束==================================");

        return SUCCESS;
    }
}