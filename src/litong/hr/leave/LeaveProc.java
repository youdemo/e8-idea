package litong.hr.leave;
import oracle.jdbc.OracleTypes;
import weaver.general.BaseBean;

import java.sql.*;
import java.util.Map;
/**
 * 
* @author 作者  张瑞坤
* @version 创建时间：2019-3-14 上午9:06:34
* 类说明
 */
public class LeaveProc {
	 private Connection conn;
	    private CallableStatement stat;
	    private ResultSet rs;
	    String url = "jdbc:oracle:thin:@192.168.181.23:1521:HCPTEST";
	    String driverName = "oracle.jdbc.driver.OracleDriver";
	    String username = "HCP";
	    String password = "hcp";
	    String sql = "call XX_lt_p_absence_check_flow_tw(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	    // 调用存储过程
	    public String callProcedure(Map<String, String> map) {
	        BaseBean log = new BaseBean();
	        try {
	            Class.forName(driverName);
	            conn = DriverManager.getConnection(url, username, password);
	            stat = conn.prepareCall(sql);
	            // 一个输入参数和三个输出参数  P_CHECK_OVERTIME_FLOW_TW
	            // ('','0001',rq,'2018/10/15 13:00','2018/10/15 13:00',
	            //    'L0301','',xss,'',jblb,'A001',cwyy,sfgq,'MSG','Y',0);
	            String p_id_no_sz = (String) map.get("p_id_no_sz");
	            String p_datetime_b = (String) map.get("p_datetime_b");
	            String p_datetime_e = (String) map.get("p_datetime_e");
	            String p_dept = (String) map.get("p_dept");
	            String p_reason = (String) map.get("p_reason");
	            String p_segment_no = (String) map.get("p_segment_no");
	            log.writeLog("getMap------->:p_datetime_b-->" + p_datetime_b + ",p_datetime_e-->" + p_datetime_e + ",p_id_no_sz-->"
	                    + p_id_no_sz + ",p_dept-->" + p_dept + ",p_reason-->" + p_reason + ",p_segment_no-->"
	                    + p_segment_no);
	            stat.setString(1, "");
	            stat.setString(2, p_id_no_sz);
	            stat.setString(3, p_dept);
	            stat.setString(4, p_reason);
	            stat.setString(5, p_datetime_b);
	            stat.setString(6, p_datetime_e);
	            stat.setString(7, "");
	            stat.setString(8, "");
	            stat.setString(11, p_segment_no);
	            stat.setString(14, "MSG");
	            stat.setString(15, null);
	            stat.setInt(16, 0);
	            stat.setInt(17, 0);
	            stat.registerOutParameter(9, OracleTypes.NUMBER);
	            stat.registerOutParameter(10, OracleTypes.NUMBER);
	            stat.registerOutParameter(12, OracleTypes.VARCHAR);
	            stat.registerOutParameter(13, OracleTypes.VARCHAR);
	            stat.execute();
	            String e_message = stat.getString(12);//错误信息
	            String confirm = stat.getString(13);//'Y':過期已薪資結算或出勒確認  'N':未過期
	            double hours = stat.getDouble(9);//请假小时数
	            double days = stat.getDouble(10);//请假天数

//	            System.out.println("name: " + name + ", sal: " + sal);
	            return hours + "@@@" + days+"@@@"+confirm+"@@@"+e_message;
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