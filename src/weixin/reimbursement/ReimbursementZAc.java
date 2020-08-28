package weixin.reimbursement;



import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weixin.util.InsertUtil;
import weixin.util.WxUtil;

public class ReimbursementZAc implements Action {

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean(); 
		log.writeLog("Zbegin------");
		RecordSet rs = new RecordSet(); 
		InsertUtil iu = new InsertUtil();
		WxUtil wu = new WxUtil();
		String workflowID = info.getWorkflowid(); 
		String requestid = info.getRequestid();
		User user = info.getRequestManager().getUser();//180525--
		String tableName ="";
		String sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		//String tablename = info.getRequestManager().getBillTableName();
		sql = "select * from "+tableName+" where requestid = '"+requestid+"'";
		rs.execute(sql);
		while(rs.next()){
			Map<String ,String> map =new HashMap<String, String>();
			String BDID = Util.null2String(rs.getString("FormID"));//编号
			String EMPNO = Util.null2String(rs.getString("EmpNo"));//工号
			String EMPName = Util.null2String(rs.getString("name"));//人员id 
			String LOCN = Util.null2String(rs.getString("Site"));//厂区 
			String DEPT = Util.null2String(rs.getString("Dept"));//部门
			String CMCU = Util.null2String(rs.getString("Costcenter"));//成本中心
			String TRDJ = Util.null2String(rs.getString("Expensesduring"));//费用期间
			String PayeeName = Util.null2String(rs.getString("Name2"));//人员id
			String PayeeCode = Util.null2String(rs.getString("EID2"));//工号
			String Email = Util.null2String(rs.getString("Email"));//
			String PAMT = "0";//
			String DAMT = "0";//
			String AMT = Util.null2String(rs.getString("Total"));//合计
			String DGL = wu.getNowDate();//审批时间
			String APPRO = user.getLastname();//Util.null2String(rs.getString("spr"));//审批人id
			String STAT = "10";//
			String CRMJ = Util.null2String(rs.getString("cjrq"));//流程创建日期
			String UPMJ = "";//更新日期
			String AN8 = "0";//工号
			String PYE = "0";//工号
			String AA = Util.null2String(rs.getString("Total"));//合计
			String EDBT = "";//
			String CO = Util.null2String(rs.getString("Site"));//公司代码
			if(CO.equals("3")){
				CO = "00420"; 
			}else{
				CO ="00418";
			}
			String DOC = "0";//
			String EXP = "";//
			String EDUS = "";//
			map.put("BDID", BDID);
			map.put("EMPNO", EMPNO);
			map.put("EMPName", wu.getFieldVal("hrmresource", "lastname", EMPName));
			map.put("LOCN", wu.getSiteName(LOCN));
			map.put("DEPT", wu.getFieldVal("hrmdepartment", "departmentname", DEPT));
			map.put("CMCU", CMCU);
			map.put("TRDJ", TRDJ);
			map.put("PayeeName", wu.getFieldVal("hrmresource", "lastname", PayeeName));
			map.put("PayeeCode", PayeeCode);
			map.put("Email", Email);
			map.put("PAMT", PAMT);
			map.put("DAMT", DAMT);
			map.put("AMT", AMT);
			map.put("DGL", DGL);
			map.put("APPRO", APPRO);
			map.put("STAT", STAT);
			map.put("CRMJ", CRMJ);
			map.put("UPMJ", UPMJ);
			map.put("AN8", AN8);
			map.put("PYE", PYE);
			map.put("AA", AA);
			map.put("EDBT", EDBT);
			map.put("CO", CO);
			map.put("DOC", DOC);
			map.put("EXP", EXP);
			map.put("EDUS", EDUS);
			iu.insert(map, "F550401");
		} 
		return SUCCESS;
	}

}
