package litong.hr.leave;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 请假
 * @author 张瑞坤
 *
 */
public class BackLeaveProc implements Action {
		private Connection conn;
	    private CallableStatement stat;
	    private ResultSet rs;
	    String url = "jdbc:oracle:thin:@192.168.181.24:1521:hcpprod";
	    String driverName = "oracle.jdbc.driver.OracleDriver";
	    String username = "HCP";
	    String password = "hcp";
	    String sql = "call XX_LT_P_ABSENCE_INSERT_FLOW_TW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    @Override
		public String execute(RequestInfo info) {
	    	RecordSet res = new RecordSet();
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
	    	String  xs = "";
	    	String ts = "";
	    	Map<String,String> map = new HashMap<String,String>();
	    	String str = "select * from "+tablename+" where requestid='"+requestid+"'";
	    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	    	res.executeSql(str);
	    	if(res.next()){
	    		String ks="";String js = "";
				try {
					ks = format.format(sdf.parse(Util.null2String(res.getString("txksrq"))))+" "+Util.null2String(res.getString("txkssj"));
		    		js = format.format(sdf.parse(Util.null2String(res.getString("txjsrq"))))+" "+Util.null2String(res.getString("txjssj"));

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("p_id_no_sz",Util.null2String(res.getString("sqrgh")));
			    map.put("p_datetime_b",ks);
			    map.put("p_datetime_e",js);
			    map.put("p_dept",Util.null2String(res.getString("bmbm")));
			    map.put("p_hours",Util.null2String(res.getString("qjxs")));
			    map.put("p_days",Util.null2String(res.getString("bctxsj")));
			    map.put("p_segment_no",Util.null2String(res.getString("gcbm")));
			    map.put("p_reason",Util.null2String(res.getString("qjlx"))); 
	    	}
	    	String aa = callProcedure(map);
	    	str = "update "+tablename+" set czzt = '"+aa+"' where requestid = '"+requestid+"'";
	    	res.executeSql(str);	    	
			return SUCCESS;
		}
	    // 调用存储过程
	    public String callProcedure(Map<String, String> map) {
	        BaseBean log = new BaseBean();
	        String confirm = "";
	        try {
	            Class.forName(driverName);
	            conn = DriverManager.getConnection(url, username, password);
	            stat = conn.prepareCall(sql);
	            String p_id_no_sz = (String) map.get("p_id_no_sz");
	            String p_datetime_b = (String) map.get("p_datetime_b");
	            String p_datetime_e = (String) map.get("p_datetime_e");
	            String p_dept = (String) map.get("p_dept");
	            String p_reason = (String) map.get("p_reason");
	            String p_hours = (String) map.get("p_hours");
	            String p_days = (String) map.get("p_days");
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
	            stat.setString(12, "");
	            stat.setString(14, "");
	            stat.setString(15, "");
	            stat.setString(16, "");
	            stat.setString(17, "MSG");
	            stat.setString(18, "BPM");
	            stat.registerOutParameter(9, OracleTypes.NUMBER);
	            stat.registerOutParameter(10, OracleTypes.NUMBER);
	            stat.registerOutParameter(13, OracleTypes.VARCHAR);
	            stat.execute();
	            confirm = stat.getString(13);//错误信息    
	            double h = stat.getDouble(9);;//    
	            double d = stat.getDouble(10);//
	            log.writeLog("输出---------"+confirm+"------"+h+"-----"+d);
//	            System.out.println("name: " + name + ", sal: " + sal);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            log.writeLog("异常-----"+e.getMessage());
	        } finally {
	            close(conn, stat, rs);
	        }
	        return confirm;
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