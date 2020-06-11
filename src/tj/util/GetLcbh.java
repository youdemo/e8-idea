package tj.util;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-7-9 下午2:34:13
 *  处理流程编号  去除前两位
 */
public class GetLcbh implements Action {

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		String lcbh = "";
		String sql = "select 流程编号 from "+tablename +" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			lcbh = Util.null2String(rs.getString("流程编号"));
		}
		String lcbhnew = lcbh.substring(2, lcbh.length());
		sql = " update workflow_requestbase  set requestmark = '"+lcbhnew+"' where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		sql = " update "+tablename+"  set 流程编号  = '"+lcbhnew+"' where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		
		return SUCCESS;
	}
	

}
