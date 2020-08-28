package litong.hr.overtimecheck;

import oracle.jdbc.OracleTypes;
import weaver.general.BaseBean;

import java.sql.*;
import java.util.Map;

public class OvertimeProc {

    private Connection conn;
    private CallableStatement stat;
    private ResultSet rs;

    String url = "jdbc:oracle:thin:@192.168.181.24:1521:hcpprod";
    String driverName = "oracle.jdbc.driver.OracleDriver";
    String username = "HCP";
    String password = "hcp";
    String sql = "call P_CHECK_OVERTIME_FLOW_TW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    // 调用存储过程
    public String callProcedure(Map map) {
        BaseBean log = new BaseBean();
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
            stat = conn.prepareCall(sql);
            // 一个输入参数和三个输出参数  P_CHECK_OVERTIME_FLOW_TW
            // ('','0001',rq,'2018/10/15 13:00','2018/10/15 13:00',
            //    'L0301','',xss,'',jblb,'A001',cwyy,sfgq,'MSG','Y',0);
            String p_id_no_sz = (String) map.get("p_id_no_sz");
            String p_begintime = (String) map.get("p_begintime");
            String p_endtime = (String) map.get("p_endtime");
            String p_cost_dept = (String) map.get("p_cost_dept");
            String p_reason = (String) map.get("p_reason");
            if("0".equals(p_reason)){
                p_reason = "B";
            }else if("1".equals(p_reason)){
                p_reason = "A";
            }
            String p_overtime_reason_no = (String) map.get("p_overtime_reason_no");
            String p_segment_no = (String) map.get("p_segment_no");
            p_begintime = p_begintime.replaceAll("-","/");
            p_endtime = p_endtime.replaceAll("-","/");
            log.writeLog("getMap------->:p_begintime-->" + p_begintime + ",p_endtime-->" + p_endtime + ",p_id_no_sz-->"
                    + p_id_no_sz + ",p_cost_dept-->" + p_cost_dept + ",p_reason-->" + p_reason + ",p_segment_no-->"
                    + p_segment_no);
            stat.setString(1, "");
            stat.setString(2, p_id_no_sz);
            stat.setString(4, p_begintime);
            stat.setString(5, p_endtime);
            stat.setString(6, p_cost_dept);
            stat.setString(7, p_overtime_reason_no);
            stat.setString(9, p_reason);
            stat.setString(11, p_segment_no);
            stat.setString(14, "MSG");
            stat.setString(15, "N");
            stat.setInt(16, 0);
            stat.registerOutParameter(3, OracleTypes.DATE);
            stat.registerOutParameter(8, OracleTypes.NUMBER);
            stat.registerOutParameter(10, OracleTypes.VARCHAR);
            stat.registerOutParameter(12, OracleTypes.VARCHAR);
            stat.registerOutParameter(13, OracleTypes.VARCHAR);
            stat.execute();

            String name = stat.getString(12);
            double sal = stat.getDouble(8);
            String jblb = stat.getString(10);

//            System.out.println("name: " + name + ", sal: " + sal);
            return name + "@@@" + sal + "@@@" + jblb;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, stat, rs);
        }
        return "";
    }

    // 关闭连接
    public void close(Connection conn, CallableStatement stat, ResultSet rs) {
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
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }

}