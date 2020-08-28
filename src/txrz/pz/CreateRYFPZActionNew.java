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
* @date 创建时间：2019年12月12日 下午2:21:30 
* @version 1.0  用人、固定资产、诉讼费用等申请
*/
public class CreateRYFPZActionNew implements Action{
	
	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		GetUtil gu = new GetUtil();
		PzUtil pu = new PzUtil();
		new BaseBean().writeLog("CreateRYFPZActionNew","requestid:"+requestid);

		String table ="K3CLOUD_LINK.k3cloud_middle.[dbo].UF_XPZDJ";
		//String modeid = pu.getModeId(table);
		InsertUtil iu = new InsertUtil();
		String tableName = "";
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		RecordSet rs = new RecordSet();
		String FBillNo = "";//	单据编号
		String FBILLTYPEID = "4";//	单据类型 用人、固定资产、诉讼费用等申请
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
		String zjkdh_new = "";//判断是否使用暂借款
		//boolean flag = gu.checkRid(requestid);
		String mid = "";//建模id
		String lcmid = "";//流程 主表id
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
			//
			zjkdh_new = Util.null2String(rs.getString("zjkdh_new"));//
			lcmid = Util.null2String(rs.getString("id"));//
			//
			FBillNo  = Util.null2String(rs.getString("djh"));//	单据编号
			FPAYDATE  = Util.null2String(rs.getString("fkrq"));//	付款日期
			FORGID  = Util.null2String(rs.getString("gsdm"));//	公司别
			FPROPOSERID  = Util.null2String(rs.getString("qkr"));//	请款人
			FPROPOSERID = gu.getFieldVal("hrmresource", "workcode", "id", FPROPOSERID);
//			FREQUESTDEPTID  = Util.null2String(rs.getString(""));//	申请部门
			FSUPPLY  = Util.null2String(rs.getString("gys"));//	供应商
			FSUPPLY = gu.getFieldVal("uf_gysjcb", "gysbm", "id", FSUPPLY);
			Fregion  = Util.null2String(rs.getString("gbdq"));//	国别地区
			Fregion = gu.getFieldVal("uf_gbdq", "gbdqbm", "id", Fregion);
//			Fcustodian  = Util.null2String(rs.getString("bgr1"));//	保管人
//			Fcustodian = gu.getFieldVal("uf_byjedb", "bh", "id", Fcustodian);
			FBankAccount  = Util.null2String(rs.getString("fkyh"));//	银行账号
			FBankAccount = gu.getFieldVal("uf_fkyhxx", "zh", "id", FBankAccount);
			FPettyCash  = Util.null2String(rs.getString("sfsybyj"));//	是否备用金
			FBackVault  = Util.null2String(rs.getString("byjk"));//	备用金库
			FBackVault = gu.getFieldVal("uf_byjedb", "bh", "id", FBackVault);
			FreserveAmount  = Util.null2String(rs.getString("byjsyje"));//	备用金额
			if(zjkdh_new.length()>0) {
				FBorrowMoney = "0";
			}else {
				FBorrowMoney = "1";
			}			
			FBOrrowAmout  = Util.null2String(rs.getString("jine"));//	暂借款金额
			Ftitle  = Util.null2String(rs.getString("requestname"));//	标题
			Fapplicant  = Util.null2String(rs.getString("tdr"));//	申请人
			FREQUESTDEPTID  = gu.getFieldVal("hrmdepartmentdefined", "kdbmbm", "deptid", gu.getFieldVal("hrmresource", "departmentid", "id", Fapplicant));//	申请部门
			Fapplicant = gu.getFieldVal("hrmresource", "workcode", "id", Fapplicant);
//			FTravelDate  = Util.null2String(rs.getString(""));//	出差日期
//			FtravelPlace  = Util.null2String(rs.getString(""));//	出差地点
		}
		Map<String,String> map = new HashMap<String, String>();
		map.put("FBillNo", FBillNo);
		map.put("FBILLTYPEID", FBILLTYPEID);
		map.put("FPAYDATE", FPAYDATE);
		map.put("FORGID", FORGID);
		map.put("FPROPOSERID", FPROPOSERID);
		map.put("FSUPPLY", FSUPPLY);
		map.put("Fregion", Fregion);
		map.put("FBankAccount", FBankAccount);
		map.put("FPettyCash", FPettyCash);
		map.put("FBackVault", FBackVault);
		map.put("FreserveAmount", FreserveAmount);
		map.put("FBorrowMoney", FBorrowMoney);
		map.put("FBOrrowAmout", FBOrrowAmout);		
		map.put("Ftitle", Ftitle);
		map.put("Fapplicant", Fapplicant);
		map.put("FREQUESTDEPTID", FREQUESTDEPTID);
		map.put("fstatus","0");
		//map.put("rid", requestid);
		//map.put("modedatacreatedate", nowDate);
		//map.put("modedatacreater", "1");
		//map.put("modedatacreatertype", "0");
		//map.put("formmodeid", modeid);
		//if(flag){
		//	iu.updatePZ(map, table, "rid",requestid);
		//}else {
			iu.insert(map, table);
		//	setJurisdiction(modeid,requestid);
		//}
		//sql = "select id from uf_xpzdj where rid = '"+requestid+"'";
		//rs.executeSql(sql);
		//if(rs.next()) {
		//	mid = rs.getString("id");
		//};
		//凭证表 明细1       流程明细 2   
		sql = "select * from "+tableName+"_dt2 where mainid = '"+lcmid+"'";
		rs.executeSql(sql);
		String mxid1 = "";
		while(rs.next()) {
			map = new HashMap<String, String>();
			mxid1 = rs.getString("id");
			FPAYEE = Util.null2String(rs.getString("zh"));//	收款人  账户
			FPAYEE = gu.getFieldVal("uf_skxxx", "skzh", "id", FPAYEE);
			FRECEIVEBILLENTRY = Util.null2String(rs.getString("je"));//	收款金额
			map.put("FPAYEE", FPAYEE);
			map.put("FRECEIVEBILLENTRY", FRECEIVEBILLENTRY);
			map.put("FBillNo", FBillNo);
			//map.put("glmxid", mxid1);
			//boolean mxflag = gu.checkMxRid(mxid1,"1");
			//if(mxflag) {
			//	iu.updatePZ(map, table+"_dt1", "mainid",mid);
			//}else {
				iu.insert(map, table+"_dt1");
			//}
		}		
		//明细2  流程明细1
		sql = "select * from "+tableName+"_dt1 where mainid = '"+lcmid+"'";
		rs.executeSql(sql);
		String mxid2 = "";
		while(rs.next()) {
			map = new HashMap<String, String>();
			mxid2 = rs.getString("id");
			FCOSTTYPE = Util.null2String(rs.getString("fylx"));//	费用类型
			FCOSTTYPE = gu.getFieldVal("uf_fylx", "fylx", "id", FCOSTTYPE);
			FINVOICETYPE = gu.getSelectValue(Util.null2String(rs.getString("fplx")));//	发票类型
			FUNTAXEDAMOUNT = Util.null2String(rs.getString("fpje"));//	未税金额
			FTAXAMT = Util.null2String(rs.getString("kdkje"));//	可抵扣金额
			FAMOUNT = Util.null2String(rs.getString("jehj"));//	金额
			map.put("FCOSTTYPE", FCOSTTYPE);
			map.put("FINVOICETYPE", FINVOICETYPE);
			map.put("FUNTAXEDAMOUNT", FUNTAXEDAMOUNT);
			map.put("FTAXAMT", FTAXAMT);
			map.put("FAMOUNT", FAMOUNT);
			map.put("FBillNo", FBillNo);
			map.put("TAX", Util.null2String(rs.getString("se")));
			//map.put("glmxid", mxid2);
			//boolean mxflag = gu.checkMxRid(mxid2,"2");
			//if(mxflag) {
			//	iu.updatePZ(map, table+"_dt2", "mainid",mid);
			//}else {
				iu.insert(map, table+"_dt2");
			//}
		}
		//明细3  流程明细3
		sql = "select * from "+tableName+"_dt3 where mainid = '"+lcmid+"'";
		rs.executeSql(sql);
		String mxid3 = "";
		while(rs.next()) {
			map = new HashMap<String, String>();
			mxid3 = rs.getString("id");
			FCOSTTYPE3 = Util.null2String(rs.getString("fylx"));//	费用类型
			FCOSTTYPE3 = gu.getFieldVal("uf_fylx", "fylx", "id", FCOSTTYPE3);
			FDEPARTMENT = Util.null2String(rs.getString("bmmc"));//	分摊部门
			FDEPARTMENT = gu.getFieldVal("hrmdepartmentdefined", "kdbmbm", "deptid",FDEPARTMENT);
			FContributionAmount = Util.null2String(rs.getString("je"));//	分摊金额
			map.put("FCOSTTYPE", FCOSTTYPE3);
			map.put("FDEPARTMENT", FDEPARTMENT);
			map.put("FContributionAmount", FContributionAmount);
			map.put("FBillNo", FBillNo);
			//map.put("glmxid", mxid3);
			//boolean mxflag = gu.checkMxRid(mxid3,"3");
			//if(mxflag) {
			//	iu.updatePZ(map, table+"_dt3", "mainid",mid);
			//}else {
				iu.insert(map, table+"_dt3");
			//}
		}
		return SUCCESS;
	}
	
	/**
	 * 设置权限

	 * @param formmodeid
	 */
	public void setJurisdiction(String formmodeid,String rid) {
		String mid = "";
		RecordSet rs = new RecordSet();
		String sql = "select id from uf_xpzdj where rid = '"+rid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			mid = rs.getString("id");
		};
		ModeRightInfo modeRightInfo = new ModeRightInfo();
	    modeRightInfo.editModeDataShare(1, Integer.parseInt(formmodeid), Integer.parseInt(mid));
	    modeRightInfo.editModeDataShareForModeField(1, Integer.parseInt(formmodeid), Integer.parseInt(mid));
	}
}
