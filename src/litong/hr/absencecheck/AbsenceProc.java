package litong.hr.absencecheck;

import oracle.jdbc.OracleTypes;
import weaver.general.BaseBean;

import java.sql.*;
import java.util.Map;

public class AbsenceProc {

    private Connection conn;
    private CallableStatement stat;
    private ResultSet rs;

    String url = "jdbc:oracle:thin:@192.168.181.24:1521:hcpprod";
    String driverName = "oracle.jdbc.driver.OracleDriver";
    String username = "HCP";
    String password = "hcp";
    String sql = "call P_ABSENCE_DELETE_CHECK_TW(?,?,?,?,?,?,?,?)";

    /***
     *  ( p_serno      in  varchar2,     bdbh
     *     p_id_no_sz   in  varchar2,    --員工代號   sqrgh
     *     p_datetime_b in  date,    --請假日期時間(起)格式西元YYYY/MM/DD HH24:MI  xjksrq,xjkssj
     *     p_datetime_e in  date,    --請假日期時間(迄)格式西元YYYY/MM/DD HH24:MI xjjsrq,xjjssj
     *     p_reason     in  varchar2,    --假別(假扣代號設定內設定之假別代號)
     *     p_segment_no   in varchar2,   --公司代號
     *     p_error out varchar2,       --錯誤原因   zt
     *     p_error_type  in  varchar2 default 'MSG'   --訊息類別 'MSG'多語訊息  'CODE'代碼 預設為多語訊息  ####
     *     )
     */
    // 调用存储过程
    public String callProcedure(Map map) {
        BaseBean log = new BaseBean();
        log.writeLog(this.getClass().getName(),"开始调用");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
            stat = conn.prepareCall(sql);
            // 一个输入参数和三个输出参数  P_CHECK_OVERTIME_FLOW_TW
            // ('','0001',rq,'2018/10/15 13:00','2018/10/15 13:00',
            //    'L0301','',xss,'',jblb,'A001',cwyy,sfgq,'MSG','Y',0);
            //String p_serno = (String) map.get("p_serno");
            String p_id_no_sz = (String) map.get("p_id_no_sz");
            String p_datetime_b = (String) map.get("p_datetime_b");
            String p_datetime_e = (String) map.get("p_datetime_e");
            String p_reason = (String) map.get("p_reason");
            String p_segment_no = (String) map.get("p_segment_no");
            p_datetime_b = p_datetime_b.replaceAll("-","/");
            p_datetime_e = p_datetime_e.replaceAll("-","/");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//            java.util.Date date = sdf.parse(p_datetime_b);
//            java.sql.Timestamp st = new java.sql.Timestamp(date.getTime());
//            java.util.Date date1 = sdf.parse(p_datetime_e);
//            java.sql.Timestamp st1 = new java.sql.Timestamp(date1.getTime());
            log.writeLog("getMap------->:p_begintime-->" + p_datetime_b + ",p_endtime-->" + p_datetime_e + ",p_id_no_sz-->"
                    + p_id_no_sz + ",p_reason-->" + p_reason + ",p_segment_no-->"
                    + p_segment_no);
            stat.setString(1, null);
            stat.setString(2, p_id_no_sz);
            stat.setString(3, p_datetime_b);
            stat.setString(4, p_datetime_e);
            stat.setString(5, p_reason);
            stat.setString(6, p_segment_no);
            stat.setString(8, "MSG");
            stat.registerOutParameter(7, OracleTypes.VARCHAR);
            stat.execute();
            String name = stat.getString(7);
            log.writeLog("name------->" + name);
//            System.out.println("name: " + name + ", sal: " + sal);
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            log.writeLog("异常--->" + e.getMessage());
            log.writeLog(this.getClass().getName(),e);
        } finally {
            close(conn, stat, rs);
        }
        return "调用失败";
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