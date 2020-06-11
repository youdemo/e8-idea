package goodbaby.workovertime;

import goodbaby.util.GetDate;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class WorkOvertimeEn implements Action{

	@Override
	public String execute(RequestInfo info) {
		
		GetDate gd = new GetDate();
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		String id="";
		String sqr="";
		String yxsj = gd.getNowDateYear()+"-12-31";
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "select id , sqr from  "+tablename+" where requestid = "+requestid;
		log.writeLog("sqlsql---"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
			sqr = Util.null2String(rs.getString("sqr")); 
		}
		sql ="select * from "+tablename+"_dt1 where mainid = '"+id+"' ";
		rs.executeSql(sql);
		while(rs.next()){
			String ksrq = Util.null2String(rs.getString("jbksrq"));
			String jsrq = Util.null2String(rs.getString("jbjsrq"));
			String kssj = Util.null2String(rs.getString("jbkssj"));
			String jssj = Util.null2String(rs.getString("jbjssj"));
			String jbsy = Util.null2String(rs.getString("jbsy"));
			String qjkssj = ksrq + " " + kssj;
			String qjjssj = jsrq + " " + jssj;
			String days = gd.getDay(qjkssj, qjjssj);
			String hours = gd.getHour(qjkssj, qjjssj);
			
			// 删除标识 流程 员工 加班开始日期 加班开始时间 加班结束日期 加班结束时间 加班类型（0-关联调休，1-不关联调休） 加班天数
			// 可用小时数 有效期截止日期 剩余小时数 备注	
			
			String str = "insert into hrm_paid_leave(delflag,field001,field002,field003,field004,field005," +
					"field006,field007,field008,field009,field010,field011,field012) values ('0','71','"+sqr+"'," +
					"'"+ksrq+"','"+kssj+"','"+jsrq+"','"+jssj+"','0','"+days+"','"+hours+"','"+yxsj+"','"+hours+"','"+jbsy+"')";
			res.execute(str);
			log.writeLog("str---"+str);
		}
		
		return SUCCESS;
	}

}
