package com.api.workflow.workflowactiontest;

import weaver.conn.ConnectionPool;
import weaver.conn.RecordSet;
import weaver.conn.WeaverConnection;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WbzActionNew implements Action {


    public String execute(RequestInfo requestInfo) {
        BaseBean bb = new BaseBean();
        String requestid = requestInfo.getRequestid();
        bb.writeLog("=========fybxWorkflowToModefybxWorkflowToMode批单（在用），Requestid:" + requestid + "=========");
        RecordSet rs = new RecordSet();
        ConnectionPool pool = ConnectionPool.getInstance();
        WeaverConnection conn = pool.getConnection();
        PreparedStatement ps = null;
        int count = 0;

        try {
            ps = conn.prepareStatement("update uf_jyb set zt=1,reqid=?,requestId=? where id=?");
            rs.executeSql("select jmsjid from formtable_main_173_dt9 where mainid = (select id from formtable_main_173 where requestid=" + requestid + ")");

            while(rs.next()) {
                String jmsjid = rs.getString("jmsjid");
                ps.setString(1, requestid);
                ps.setString(2, requestid);
                ps.setString(3, jmsjid);
                ps.addBatch();
                ++count;
                if (count == 1000) {
                    ps.executeBatch();
                    conn.commit();
                    ps.clearBatch();
                    count = 0;
                }
            }

            ps.executeBatch();
            conn.commit();
        } catch (SQLException var18) {
            bb.writeLog("fybxWorkflowToMode, 数据库存储异常");
            bb.writeLog(var18);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var17) {
                bb.writeLog("fybxWorkflowToMode, 关闭连接异常");
                bb.writeLog(var17);
            }

        }

        bb.writeLog("=========fybxWorkflowToMode，费用报销归档结束，Requestid:" + requestid + "=========");
        return "1";
    }
}
