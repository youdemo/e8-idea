package morningcore.budget;

import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-7-26 上午11:34:42
 * 出差报销        --退回删除中间表
 */
public class BudgetTravelDelSH  implements Action {

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String sql = "delete uf_xmyszjb where dqrid = '"+requestid+"'";
		rs.executeSql(sql);
		return SUCCESS;
	}
	
	

}
