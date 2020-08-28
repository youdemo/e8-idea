package txrz.pz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import txrz.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2019年12月11日 下午5:36:54 
* @version 1.0 
*/
public class CreateBYJPZActionNew implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		GetUtil gu = new GetUtil();
		PzUtil pu = new PzUtil();
		new BaseBean().writeLog("CreateBYJPZActionNew","requestid:"+requestid);

		String table ="K3CLOUD_LINK.k3cloud_middle.[dbo].UF_XPZDJ";
		//String modeid = pu.getModeId(table);
		InsertUtil iu = new InsertUtil();
		String tableName = "";
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		RecordSet rs = new RecordSet();
		String FBillNo = "";//	单据编号
		String FBILLTYPEID = "0";//	单据类型 备用金支用表
		String FPAYDATE = "";//	付款日期
		String FORGID = "";//	公司别
		String FPROPOSERID = "";//	请款人
		String FREQUESTDEPTID = "";//	申请部门
		String FSUPPLY = "";//	供应商
		String Fregion = "";//	国别地区
		String Fcustodian = "";//	保管人
		String FBankAccount = "";//	银行账号
		String FPettyCash = "";//	是否备用金
		String FBackVault = "";//	备用金库
		String FreserveAmount = "";//	备用金额
		String FBorrowMoney = "";//	是否暂借款
		String FBOrrowAmout = "";//	暂借款金额
		String Ftitle = "";//	标题
		String Fapplicant = "";//	申请人
		String FTravelDate = "";//	出差日期
		String FtravelPlace = "";//	出差地点
		//明细1
		String FPAYEE = "";//	收款人
		String FRECEIVEBILLENTRY = "";//	收款金额
		//明细2
		String FCOSTTYPE = "";//	费用类型
		String FINVOICETYPE = "";//	发票类型
		String FUNTAXEDAMOUNT = "";//	未税金额
		String FTAXAMT = "";//	可抵扣金额
		String FAMOUNT = "";//	金额
		//明细3
		String FCOSTTYPE3 = "";//	费用类型   表字段  FCOSTTYPE
		String FDEPARTMENT = "";//	分摊部门
		String FContributionAmount = "";//	分摊金额
		////
		//boolean flag = gu.checkRid(requestid);
		String mid = "";//建模id
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select m.*,w.requestname from "+tableName+" m join workflow_requestbase w on m.requestid=w.requestid where m.requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			FBillNo  = Util.null2String(rs.getString("djbh"));//	单据编号
			FPAYDATE  = Util.null2String(rs.getString("zdfkrq"));//	付款日期
			FORGID  = Util.null2String(rs.getString("gsdm"));//	公司别
			FPROPOSERID  = Util.null2String(rs.getString("qkr"));//	请款人
			FPROPOSERID = gu.getFieldVal("hrmresource", "workcode", "id", FPROPOSERID);
//			FREQUESTDEPTID  = Util.null2String(rs.getString(""));//	申请部门
//			FSUPPLY  = Util.null2String(rs.getString(""));//	供应商
//			Fregion  = Util.null2String(rs.getString(""));//	国别地区
			Fcustodian  = Util.null2String(rs.getString("bgr1"));//	保管人
			Fcustodian = gu.getFieldVal("uf_byjedb", "bh", "id", Fcustodian);
			FBankAccount  = Util.null2String(rs.getString("fkyh"));//	银行账号
			FBankAccount = gu.getFieldVal("uf_fkyhxx", "zh", "id", FBankAccount);
//			FPettyCash  = Util.null2String(rs.getString(""));//	是否备用金
//			FBackVault  = Util.null2String(rs.getString(""));//	备用金库
			FreserveAmount  = Util.null2String(rs.getString("xj"));//	备用金额
//			FBorrowMoney  = Util.null2String(rs.getString(""));//	是否暂借款
//			FBOrrowAmout  = Util.null2String(rs.getString(""));//	暂借款金额
			Ftitle  = Util.null2String(rs.getString("requestname"));//	标题
			Fapplicant  = Util.null2String(rs.getString("sqr"));//	申请人
			FREQUESTDEPTID  = gu.getFieldVal("hrmdepartmentdefined", "kdbmbm", "deptid", rs.getString("qkdw"));//	申请部门
			Fapplicant = gu.getFieldVal("hrmresource", "workcode", "id", Fapplicant);
//			FTravelDate  = Util.null2String(rs.getString(""));//	出差日期
//			FtravelPlace  = Util.null2String(rs.getString(""));//	出差地点		
			///明细1数据
			FPAYEE = Util.null2String(rs.getString("skrzh"));//	收款人
			FPAYEE = gu.getFieldVal("uf_skxxx", "skzh", "id", FPAYEE);
			
			FRECEIVEBILLENTRY = Util.null2String(rs.getString("xj"));//	收款金额
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("FBillNo", FBillNo);
		map.put("FBILLTYPEID", FBILLTYPEID);
		map.put("FPAYDATE", FPAYDATE);
		map.put("FORGID", FORGID);
		map.put("FPROPOSERID", FPROPOSERID);
		map.put("Fcustodian", Fcustodian);
		map.put("FBankAccount", FBankAccount);
		map.put("FreserveAmount", FreserveAmount);
		map.put("Ftitle", Ftitle);
		map.put("Fapplicant", Fapplicant);
		map.put("FREQUESTDEPTID", FREQUESTDEPTID);
		map.put("fstatus","0");
		//map.put("rid", requestid);
		//map.put("modedatacreatedate", nowDate);
		//map.put("modedatacreater", "1");
		//map.put("modedatacreatertype", "0");
		//map.put("formmodeid", modeid);
		//if(flag) {
		//	iu.updatePZ(map, table, "FBillNo",FBillNo);
		//}else {
			iu.insert(map, table);
		//}
		//明细1
//		sql = "select id from uf_xpzdj where rid = '"+requestid+"'";
//		rs.executeSql(sql);
//		if(rs.next()) {
//			mid = rs.getString("id");
//		};
		map = new HashMap<String, String>();
		map.put("FPAYEE", FPAYEE);
		map.put("FRECEIVEBILLENTRY", FRECEIVEBILLENTRY);
		map.put("FBillNo", FBillNo);
		
		//if(flag) {
		//	iu.updatePZ(map, table+"_dt1", "FBillNo",FBillNo);
		//}else {
			iu.insert(map, table+"_dt1");
			//setJurisdiction(mid, modeid);
		//}
		return SUCCESS;
	}
	/**
	 * 设置权限

	 * @param formmodeid
	 */
	public void setJurisdiction(String mid,String formmodeid) {
		ModeRightInfo modeRightInfo = new ModeRightInfo();
	    modeRightInfo.editModeDataShare(1, Integer.parseInt(formmodeid), Integer.parseInt(mid));
	    modeRightInfo.editModeDataShareForModeField(1, Integer.parseInt(formmodeid), Integer.parseInt(mid));
	}

}
