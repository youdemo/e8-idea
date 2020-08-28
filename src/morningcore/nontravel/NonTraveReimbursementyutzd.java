package morningcore.nontravel;

import morningcore.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  梁敏
* @date 创建时间：2010年7月8日 下午13:23:34 
* @version 1.0 预算调整单   --公司级更新项目剩余金额和事项剩余金额    
*/
public class NonTraveReimbursementyutzd  implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		GetUtil gu = new GetUtil();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String xmmc = "";//项目名称  --
		String sqrq = "";//申请日期--
		String xmgsbm = "";//公司/部门  --
		String ygje = "";//明细预估
		String bxje = "";//报销金额
		String fycdbm = "";//费用承担部门 --
		String xmbm = "";//项目编号  --
		int flagSX = 0;//事项标识
		int flagBM = 0;//部门标识
		int flagXM = 0;//项目标识
		int flagSXJL = 0;//事项结论标识
		int flagBMJL = 0;//部门结论标识
		int flagXMJL = 0;//项目结论标识
		int flagISSX = 0;//是否存在事项
		String sql = "select m.fqrq,m.zcxm,m.xmgsbm from "+tablename+" m where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			xmmc = Util.null2String(rs.getString("zcxm"));//项目名称
			xmgsbm = Util.null2String(rs.getString("xmgsbm"));//公司/部门
			//fycdbm = Util.null2String(rs.getString("fycdbm"));//费用承担部门
			sqrq = Util.null2String(rs.getString("fqrq"));//申请日期
			xmbm = gu.getFieldVal("uf_nbdd", "internal_order", "id", xmmc);
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
		String Bbmysje = "";//转入科目项目剩余金额
		String Axmsyje = "";//转出科目项目剩余金额
		String dtid = "";//明细id
		
		
		sql = " select dt.id,fykm,fykmbm,zcejbm,zrejbm,[dbo].[getBudgetTravelnew]('"+sqrq+"','"+xmmc+"','"+xmgsbm+"',dt.zrejbm,dt.fykmbm,dt.fykm,'','7') as bmysje,"
				+"[dbo].[getBudgetTravelnew]('"+sqrq+"','"+xmmc+"','"+xmgsbm+"',dt.zcejbm,dt.fykmbm,dt.fykm,'','7') as xmsyje "
				+ "  from "+tablename+"_dt1 dt join "+tablename+" m on m.id=dt.mainid where m.requestid = '"+requestid+"'";
		rs.executeSql(sql);
		log.writeLog("查询预算数据sql -----"+sql);
		while(rs.next()) {
			dtid = rs.getString("id");
			Bbmysje = Util.null2String(rs.getString("bmysje"));
			
			Axmsyje = Util.null2String(rs.getString("xmsyje"));
			String str = "update "+tablename+"_dt1 set zrxmsyje = '"+Bbmysje+"',xmsyje='"+Axmsyje+"' where id  = '"+dtid+"' ";
			log.writeLog("更新明细  预算数据sql -----"+str);
			res.executeSql(str);	
			
		}		
		
	//事项科目预算比较
		//计算剩余事项，可用事项
		String Asxje = "";//转出事项剩余金额
		String Bsxkyje = "";//转入事项剩余金额
		sql = " select dt.id,fykm,fykmbm,zcejbm,zrejbm,[dbo].[getBudgetTravelnew]('"+sqrq+"','"+xmmc+"','"+xmgsbm+"',dt.zcejbm,dt.fykmbm,dt.fykm,dt.yssx,'3') as sxje,"
				+"[dbo].[getBudgetTravelnew]('"+sqrq+"','"+xmmc+"','"+xmgsbm+"',dt.zrejbm,dt.fykmbm,dt.fykm,dt.yssx,'3') as sxkyje "
				+ "  from "+tablename+"_dt1 dt join "+tablename+" m on m.id=dt.mainid where dt.yssx is not null  and dt.yssx <> '' and m.requestid = '"+requestid+"'";
		rs.executeSql(sql);
		
		log.writeLog("事项查询预算数据sql -----"+sql);
		while(rs.next()) {
			dtid = rs.getString("id");
			Asxje = Util.null2String(rs.getString("sxje"));
			
			Bsxkyje = Util.null2String(rs.getString("sxkyje"));//------
			flagISSX = flagISSX + 1;
			String str = "update "+tablename+"_dt1 set zcsxsyje = '"+Asxje+"',zrsxsyje = '"+Bsxkyje+"'  where id  = '"+dtid+"' ";
			log.writeLog("更新事项明细  预算数据sql -----"+str);
			res.executeSql(str);
			
		}		
		
		
		return SUCCESS;
		
	}
	
	
	
}
