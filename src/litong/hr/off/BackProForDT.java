package litong.hr.off;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

public class BackProForDT implements Action
{
    private Connection conn;
    private CallableStatement stat;
    private ResultSet rs;
    String url = "jdbc:oracle:thin:@192.168.181.24:1521:hcpprod";
    String driverName = "oracle.jdbc.driver.OracleDriver";
    String username = "HCP";
    String password = "hcp";
    String sql ="call XX_LT_P_ABSENCE_INSERT_FLOW_TW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public String execute(RequestInfo info) {
        RecordSet res = new RecordSet();
        RecordSet rs = new RecordSet();
        BaseBean log = new BaseBean();
        String requestid = info.getRequestid();
        String tablename = info.getRequestManager().getBillTableName();
        String gh = "";
        String startDate = "";
        String startTime = "";
        String endDate = "";
        String endTime = "";
        String bmbm = "";
        String gsbm = "";
        String jb = "";
        String xs = "";
        String ts = "";
        String mainid = "";
        Map map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        String str = "select * from " + tablename + " where requestid='" + requestid + "'";
        rs.executeSql(str);
        if (rs.next()) {
            mainid = Util.null2String(rs.getString("id"));
            log.writeLog("mainid==" + mainid);
        }
        String sqldt = "select * from " + tablename + "_dt1 where mainid='" + mainid + "'";
        res.executeSql(sqldt);

        while (res.next()) {
            String id = Util.null2String(res.getString("id"));
            String ks = "";
            String js = "";
            try {
                ks = format.format(sdf.parse(Util.null2String(res.getString("txksrq")))) + " " + Util.null2String(res.getString("txkssj"));
                js = format.format(sdf.parse(Util.null2String(res.getString("txjsrq")))) + " " + Util.null2String(res.getString("txjssj"));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            map.put("p_id_no_sz", Util.null2String(res.getString("txrgh")));
            map.put("p_datetime_b", ks);
            map.put("p_datetime_e", js);
            map.put("p_dept", Util.null2String(res.getString("bmbm")));
            map.put("p_houres", Util.null2String(res.getString("txxs")));
            map.put("p_days", "");
            map.put("p_segment_no", Util.null2String(res.getString("gcbm")));
            map.put("p_reason", Util.null2String(res.getString("qjlx")));

            String aa = callProcedure(map);

            str = "update " + tablename + "_dt1 set czzt = '" + aa + "' where id = '" + id + "'";
            rs.executeSql(str);
            log.writeLog("str==" + str);
        }
        return SUCCESS;
    }

    public String callProcedure(Map<String, String> map)
    {
        BaseBean log = new BaseBean();
        String confirm = "";
        try {
            Class.forName(this.driverName);
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            stat = conn.prepareCall(sql);
            String p_id_no_sz = (String)map.get("p_id_no_sz");
            String p_datetime_b = (String)map.get("p_datetime_b");
            String p_datetime_e = (String)map.get("p_datetime_e");
            String p_dept = (String)map.get("p_dept");
            String p_reason = (String)map.get("p_reason");
            String p_hours = (String)map.get("p_hours");
            String p_days = (String)map.get("p_days");
            String p_segment_no = (String)map.get("p_segment_no");
            log.writeLog("getMap------->:p_datetime_b-->" + p_datetime_b + ",p_datetime_e-->" + p_datetime_e + ",p_id_no_sz-->" + p_id_no_sz + ",p_dept-->" + p_dept + ",p_reason-->" + p_reason + ",p_segment_no-->" + p_segment_no);

            stat.setString(1, "");
            stat.setString(2, p_id_no_sz);
            stat.setString(3, p_dept);
            stat.setString(4, p_reason);
            stat.setString(5, p_datetime_b);
            stat.setString(6, p_datetime_e);
            stat.setString(7, "");
            stat.setString(8, "");

            stat.setString(11, p_segment_no);
            stat.setString(12, "");
            stat.setString(14, "");
            stat.setString(15, "");
            stat.setString(16, "");
            stat.setString(17, "MSG");
            stat.setString(18, "BPM");
            stat.registerOutParameter(9, 2);
            stat.registerOutParameter(10, 2);
            stat.registerOutParameter(13, 12);
            stat.execute();
            confirm = stat.getString(13);
            double h = stat.getDouble(9);
            double d = stat.getDouble(10);
            log.writeLog("输出---------" + confirm + "------" + h + "-----" + d);
        }
        catch (Exception e) {
            e.printStackTrace();
            log.writeLog("异常-----" + e.getMessage());
        } finally {
            close(conn, stat, rs);
        }
        return confirm;
    }

    public void close(Connection conn, CallableStatement stat, ResultSet rs)
    {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                rs = null;
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                stat = null;
            }
        }
        if (conn == null) return;
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }
}