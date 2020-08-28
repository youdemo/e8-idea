package jsd.financialcontrol.action;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月24日 上午9:18:58 
* @version 1.0  费用报销校验预算是否超标
*/
public class CheckYS  implements Action  {

	@Override
	public String execute(RequestInfo info ) {
		RecordSet rs = new RecordSet();
		String requestid = info.getRequestid();
		BaseBean logs = new BaseBean();
		String tablename = info.getRequestManager().getBillTableName();
//		辅助
//		String flag_overfee1 = "";// 是否超预算  0 未超出预算  
//		String flag_overfee2 = "";// 是否超预算  1 超出预算
//		String flag_overfee3 = "";// 是否超预算  0 未超出预算  1 超出预算
//		String flag_action1 = "";// 0 通过
//		String flag_action2 = "";// 1 警告 
//		String flag_action3 = "";// 2  禁止
		
//		主表
//		String z_overfee = "";//是否超预算  0 未超出预算 1 超出预算
//		String z_action = "";//控制强度 0 通过  1 警告  2  禁止
		
//		明细数据
//		String overfee = "";//是否超预算  0 未超出预算 1 超出预算
//		String action = "";//控制强度 0 通过  1 警告  2  禁止
//		
		int con1 = 0;
		int con2 = 0;
		int con3 = 0;
		int con4 = 0;
		String sql = "select count(dt.id) as con  from "+tablename+" m join "+tablename+"_dt4 dt  on  m.id = dt.mainid where dt.overfee = '1' and (dt.action='2' or dt.action='1') and m.requestid = '"+requestid+"'";
		rs.execute(sql);
		if(rs.next()){
			con1 = rs.getInt("con");
		}
		
		sql = "select count(dt.id) as con  from "+tablename+" m join "+tablename+"_dt4 dt  on  m.id = dt.mainid where dt.action='2' and m.requestid = '"+requestid+"'";
		rs.execute(sql);
		if(rs.next()){
			con2 = rs.getInt("con");
		}
		sql = "select count(dt.id) as con  from "+tablename+" m join "+tablename+"_dt4 dt  on  m.id = dt.mainid where  dt.action='1' and m.requestid = '"+requestid+"'";
		rs.execute(sql);
		if(rs.next()){
			con3 = rs.getInt("con");
		}
		
		
		String  str = "update "+tablename+" set ";
		if(con1>0) {
			str = str + " overfee= '1' ";
		}else {
			str = str + " overfee= '0' ";
		}
		if(con2>0) {
			str = str + " ,action = '2' where requestid = '"+requestid+"'";
		}else if(con3>0) {
			str = str + " ,action = '1' where requestid = '"+requestid+"'";
		}else {
			str = str + "  ,action = '0' where requestid = '"+requestid+"'";
		}
		logs.writeLog("str ====="+str);
		rs.execute(str);
		if(con2>0) {
			info.getRequestManager().setMessagecontent("所用预算的控制强度为禁止，请确认！");
            info.getRequestManager().setMessageid("信息提示");
            return FAILURE_AND_CONTINUE;
		}
		return SUCCESS;
	}

}
