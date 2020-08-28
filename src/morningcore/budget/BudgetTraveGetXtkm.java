package morningcore.budget;

import morningcore.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2019年8月19日 下午2:38:31 
* @version 1.0 
*  出差报销  重新赋值明细 系统科目 
*/
public class BudgetTraveGetXtkm implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		GetUtil gu = new GetUtil();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String fykm = "";
		String kmbm = "";
		String kmmc = "";
		String tyfkbs = "";//统一费控id
		String jkkm = "";//建模科目
		String yskzbz = ""; // 2不控制
		String sql = "select dt.id,dt.jkkm,dt.fykm from "+tablename+"_dt1 dt join "+tablename+" m on m.id=dt.mainid where m.requestid = '"+requestid+"' ";
		rs.executeSql(sql);
		while(rs.next()) {
			jkkm = Util.null2String(rs.getString("jkkm"));
			fykm = gu.getXtkm(jkkm);
			if("0".equals(fykm)) {
				kmmc = gu.getFieldVal("uf_yskmsj", "yskmmc", "id", jkkm);
				info.getRequestManager().setMessagecontent(kmmc+" 无对应系统科目！");    
				info.getRequestManager().setMessageid("错误信息提示");
				return SUCCESS ;//返回
			}
			yskzbz = gu.getFieldVal("uf_yskmsj", "edkzbs", "id", jkkm);
			tyfkbs = gu.getFieldVal("FnaBudgetfeeType", "groupCtrlId", "id", fykm);
			kmbm = gu.getFieldVal("FnaBudgetfeeType", "codeName", "id", fykm);
			String dtid = Util.null2String(rs.getString("id"));
			String str = "update "+tablename+"_dt1 set fykm = '"+fykm+"',tyfkbs='"+	tyfkbs+"',fykmbm = '"+kmbm+"',yskzbz='"+yskzbz+"'  where id = '"+dtid+"'";
			res.executeSql(str);
		}
		// TODO Auto-generated method stub
		
		
		
		
		
		return SUCCESS;
	}

}
