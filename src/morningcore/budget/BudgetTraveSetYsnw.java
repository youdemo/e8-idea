package morningcore.budget;

import morningcore.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2019年8月19日 下午3:39:23 
* @version 1.0   出差报销     更新预算结论     无事项
*/
public class BudgetTraveSetYsnw  implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		GetUtil gu = new GetUtil();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String xmmc1 = "";//项目名称
		String sqrr = "";//申请日期
//		String jkkm = "";//建模科目id
		String yskzfs = "";//预算控制方式  2 不控制
		String xmgsbm = "";//公司/部门
//		String xmsyje = "";//项目可用
//		String bmsyje = "";//部门可用
		String ygje = "";//明细预估
		String bxje = "";//报销金额
		String fycdbm = "";//费用承担部门
		String tyfkbs = "";//明细统一费控id
		String xmbm = "";//项目编号
		String ysjl = "0";//预算结 论  0 预算内  1 预算外
		String ysjlms = "";//预算结论描述
		int flagBM = 0;//部门标识
		int flagXM = 0;//项目标识
		int flagBMJL = 0;//部门结论标识
		int flagXMJL = 0;//项目结论标识
//		double sumYg = 0.00;//预估之和
//		doubler sumBx = 0.00;//报销之和
		
		String sql = "select m.sqrr,m.xmmc1,m.xmgsbm,m.fycdbm from "+tablename+" m where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			xmmc1 = Util.null2String(rs.getString("xmmc1"));//项目名称
			xmgsbm = Util.null2String(rs.getString("xmgsbm"));//公司/部门
			fycdbm = Util.null2String(rs.getString("fycdbm"));//费用承担部门
			sqrr = Util.null2String(rs.getString("sqrr"));//申请日期
			xmbm = gu.getFieldVal("uf_nbdd", "internal_order", "id", xmmc1);
		}
//		ALTER function [dbo].[getBudgetTravelnew](
//				@sqrq varchar(100), --申请日期  √
//				@xm varchar(100), --表单建模项目数据   项目 √
//				@gsbm varchar(100),--公司/部门  √
//				@cdbm varchar(100), --费用承担部门	 √
//				@kmbm  varchar(100), --科目编码
//				@jmkm  varchar(100), --建模科目id    
//				@sxkz  varchar(100), --事项ID  null
//				@types varchar(100) -- 执行的业务类型   0 部门总预算   1  部门总已扣减用预算  2  部门冻结预算 3 部门 可用  4 公司级 总预算  5 公司级总已扣减用预算 6 公司级冻结预算 7 公司可用   
//				)
//			returns varchar(100)
		String bmysje = "";//部门预算金额
		String djjr = "";//部门冻结金额
		String yfsje = "";//部门已发生金额
		String bmsyje = "";//部门剩余金额
		String xmysje = "";//项目预算金额
		String xmdjjr = "";//项目冻结金额
		String xmyfsje = "";//项目已发生金额
		String xmsyje = "";//项目剩余金额
		String dtid = "";//明细id
		
		
		sql = " select dt.id,jkkm,fykmbm,[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','0') as bmysje,"
				+ " [dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','1') as yfsje,[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','2') as djjr,"
				+"[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','3') as bmsyje,[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','4') as xmysje,"
				+"[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','5') as xmyfsje,[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','6') as xmdjjr,"
				+"[dbo].[getBudgetTravelnew]('"+sqrr+"','"+xmmc1+"','"+xmgsbm+"','"+fycdbm+"',fykmbm,jkkm,'','7') as xmsyje "
				+ "  from "+tablename+"_dt1 dt join "+tablename+" m on m.id=dt.mainid where m.requestid = '"+requestid+"'";
		rs.executeSql(sql);
		log.writeLog("查询预算数据sql -----"+sql);
		while(rs.next()) {
			dtid = rs.getString("id");
			bmysje = Util.null2String(rs.getString("bmysje"));
			yfsje = Util.null2String(rs.getString("yfsje"));
			djjr = Util.null2String(rs.getString("djjr"));
			bmsyje = Util.null2String(rs.getString("bmsyje"));
			xmysje = Util.null2String(rs.getString("xmysje"));
			xmyfsje = Util.null2String(rs.getString("xmyfsje"));
			xmdjjr = Util.null2String(rs.getString("xmdjjr"));
			xmsyje = Util.null2String(rs.getString("xmsyje"));
			String str = "update "+tablename+"_dt1 set bmysje = '"+bmysje+"',yfsje='"+yfsje+"',djjr='"+djjr+"',bmsyje = '"+bmsyje+"',xmysje='"+xmysje+"',xmyfsje='"+xmyfsje
					+"',xmdjjr='"+xmdjjr+"',xmsyje='"+xmsyje+"' where id  = '"+dtid+"' ";
			log.writeLog("更新明细  预算数据sql -----"+str);
			res.executeSql(str);	
			
		}		
		sql = "select tyfkbs, isnull(sum(isnull(uu.ygje,0.00)),0.00) as ygje, isnull(sum(isnull(uu.bxje,0.00)),0.00) as bxje, xmsyje, bmsyje from " + 
				"(select m.xmmc1,m.xmgsbm,dt.xmsyje,dt.bmsyje,dt.ygje,dt.tyfkbs ,dt.id,dt.jkkm,dt.fykm ,dt.bxje,dt.yskzbz  "
				+ " from "+tablename+"_dt1 dt join "+tablename+" m on m.id=dt.mainid where m.requestid = '"+requestid+"'" + 
				") uu where  uu.yskzbz <> 2  group by  tyfkbs, xmsyje, bmsyje ";
		rs.executeSql(sql);
		log.writeLog("合并计算查询预算数据sql-------"+sql);
		while(rs.next()) {
			
			xmsyje = Util.null2String(rs.getString("xmsyje"));//项目可用
			bmsyje = Util.null2String(rs.getString("bmsyje"));//部门可用
			ygje = Util.null2String(rs.getString("ygje"));//明细预估
			bxje = Util.null2String(rs.getString("bxje"));//报销金额
			tyfkbs = Util.null2String(rs.getString("tyfkbs"));//明细统一费控id
			double sumXmKy = 0.00;//
			double sumBmKY = 0.00;//
			sumXmKy = gu.addDouble(xmsyje, ygje);
			sumBmKY = gu.addDouble(bmsyje, ygje);
			if(sumXmKy<Double.valueOf(bxje)) {
				flagXM = flagXM + 1;
			}
			if(sumBmKY<Double.valueOf(bxje)) {
				flagBM = flagBM + 1;
			}
			if("公司".equals(xmgsbm) && !"000060000000".equals(xmbm)) {//
				if(sumXmKy<Double.valueOf(bxje)) {
					flagXMJL = flagXMJL + 1;
//					setYsjl(Double.valueOf(bxje), sumBmKY, sumXmKy, tablename, requestid, ysjl);
//					return SUCCESS;
				}
			}else {
				
				if(sumBmKY<Double.valueOf(bxje)) {
					flagBMJL = flagBMJL + 1;
//					setYsjl(Double.valueOf(bxje), sumBmKY, sumXmKy, tablename, requestid, ysjl);
//					return SUCCESS;
				}
			}
			
		}
		if(flagBM>0) {
			ysjlms = "部门预算外，";
		}else {
			ysjlms = "部门预算内，";
		}
		if(flagXM>0) {
			ysjlms = ysjlms + "项目预算外";
		}else {
			ysjlms = ysjlms + "项目预算内";
		}
		if(flagBMJL>0 || flagXMJL>0 ) {
			ysjl = "1";
		}else {
			ysjl = "0";
		}
		sql = "update "+tablename+" set ysjl = '"+ysjl+"' ,ysjlms = '"+ysjlms+"'  where requestid = '"+requestid+"'";
		rs.executeSql(sql);	
		return SUCCESS;
		
	}
	
}
