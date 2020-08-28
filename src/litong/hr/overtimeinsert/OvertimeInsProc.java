package litong.hr.overtimeinsert;

import oracle.jdbc.OracleTypes;
import weaver.general.BaseBean;

import java.sql.*;
import java.util.Map;

public class OvertimeInsProc {

    private Connection conn;
    private CallableStatement stat;
    private ResultSet rs;

    String url = "jdbc:oracle:thin:@192.168.181.24:1521:hcpprod";
    String driverName = "oracle.jdbc.driver.OracleDriver";
    String username = "HCP";
    String password = "hcp";
    String sql = "call P_INSERT_OVERTIME_FLOW_TW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    /**
     * p_serno        in varchar2,    N   --加班單號
     * p_id_no_sz     in  varchar2,      --員工編號
     * p_cday         out  date,          --加班歸屬日期:西元 YYYY/MM/DD
     * p_begintime    in  varchar2,      --加班日期時間(起)格式西元YYYY/MM/DD HH24:MI
     * p_endtime      in  varchar2,      --加班日期時間(迄)格式西元YYYY/MM/DD HH24:MI
     * p_cost_dept    in  varchar2,      --認列部門
     * p_overtime_reason_no   in varchar2, N -- 加班原因ID 可以為null
     * p_hour         out  number,        --加班時數
     * p_reason       in  varchar2,      --B(補休)/A(計費)
     * p_style        out  varchar2,      --加班類別(N(平時)/S(例假日)/H(國定假日))
     * p_segment_no   in varchar2,       --公司代號
     * p_remark       in varchar2,       N --加班備註
     * p_error        out varchar2,      --錯誤原因
     * p_confirm      in  varchar2,      N --'Y':過期已薪資結算或出勒確認  'N':未過期
     * p_period_master  in varchar2,    N --補扣請假費時的計薪期間
     * p_period_detail  in varchar2,    N --補扣請假費時的期間每月
     * p_error_type     in  varchar2  N default 'MSG',   --訊息類別 'MSG'多語訊息 'CODE'代碼 'FULL' 代碼加訊息 預設為多語訊息
     * p_is_mend        in  varchar2  N default 'N',      -- ASUS 使用  是否外加補休 'Y':計費外加補休
     * p_create_by      in  varchar2  N default 'BPM'
     */
    // 调用存储过程
    public String callProcedure(Map map) {
        BaseBean log = new BaseBean();
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
            stat = conn.prepareCall(sql);
            // 一个输入参数和三个输出参数  P_INSERT_OVERTIME_FLOW_TW
            String p_serno = (String) map.get("p_serno");
            String p_id_no_sz = (String) map.get("p_id_no_sz");
            String p_begintime = (String) map.get("p_begintime");
            String p_endtime = (String) map.get("p_endtime");
            String p_cost_dept = (String) map.get("p_cost_dept");
            String p_reason = (String) map.get("p_reason");
            String p_segment_no = (String) map.get("p_segment_no");
            String p_overtime_reason_no = (String) map.get("p_overtime_reason_no");
            String p_remark = (String) map.get("p_remark");
            String p_confirm = (String) map.get("p_confirm");
            String p_period_master = (String) map.get("p_period_master");
            String p_period_detail = (String) map.get("p_period_detail");
            String p_error_type = (String) map.get("p_error_type");
            String p_is_mend = (String) map.get("p_is_mend");
            String p_create_by = (String) map.get("p_create_by");
            p_begintime = p_begintime.replaceAll("-","/");
            p_endtime = p_endtime.replaceAll("-","/");
            log.writeLog("p_serno(表单编号)-->" + p_serno + ",p_id_no_sz(人员编号)-->" + p_id_no_sz + ",p_begintime(开始时间)-->" + p_begintime + p_reason
                    + ",p_segment_no(分部代码)-->" + p_segment_no + ",p_cost_dept(部门代码)-->" + p_cost_dept + ",p_overtime_reason_no" +
                    "(原因代码)-->" + p_overtime_reason_no + ",p_error_type-->" + p_error_type + ",p_is_mend-->" + p_is_mend + "," +
                    "p_create_by-->" + p_create_by);
            stat.setString(1, p_serno);
            stat.setString(2, p_id_no_sz);
            stat.setString(4, p_begintime);
            stat.setString(5, p_endtime);
            stat.setString(6, p_cost_dept);
            stat.setString(7, p_overtime_reason_no);
            stat.setString(9, p_reason);
            stat.setString(11, p_segment_no);
            stat.setString(12, p_remark);
            stat.setString(14, p_confirm);
            stat.setString(15, p_period_master);
            stat.setString(16, p_period_detail);
            stat.setString(17, p_error_type);
            stat.setString(18, p_is_mend);
            stat.setString(19, p_create_by);
            stat.registerOutParameter(3, OracleTypes.DATE);
            stat.registerOutParameter(8, OracleTypes.NUMBER);
            stat.registerOutParameter(10, OracleTypes.VARCHAR);
            stat.registerOutParameter(13, OracleTypes.VARCHAR);
            stat.execute();

            String error = stat.getString(13);
            String jblb = stat.getString(10);
            double sal = stat.getDouble(8);

//            System.out.println("name: " + name + ", sal: " + sal);
            return error + "@@@" + sal + "@@@" + jblb;
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