package txrz.updatbyed;

import txrz.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-2-21 下午4:10:44
 * 类说明
 */
public class DjByyedYRS implements Action {
	GetUtil gu = new GetUtil();
	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("sql-----------范德萨范德萨范德萨------");
		String byjk = "";
		String sfsybyj = "";//
		String djed = "";//表单金额
		String jmdjed = "";//建模表单金额
		RecordSet rs = new RecordSet();
		String tablename = info.getRequestManager().getBillTableName();
		String requestid = info.getRequestid();
		String sql = "select sfsybyj,wssezh,byjk from "+tablename+" where requestid = '"+requestid+"'";
		log.writeLog("sql-----------------"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			byjk = Util.null2String(rs.getString("byjk"));
			djed = Util.null2String(rs.getString("wssezh"));
			if(djed.equals("")){
				djed = "0.00";
			}
			sfsybyj = Util.null2String(rs.getString("sfsybyj"));
		}
		if(sfsybyj.equals("0")){
			
			sql = "select djje from uf_byjedb where id = '"+byjk+"'";
			rs.executeSql(sql);
			if(rs.next()){
				jmdjed = Util.null2String(rs.getString("djje"));
			}
			if(jmdjed.equals("")){
				jmdjed = "0.00";
			}
			Double zdj =  gu.addDouble(jmdjed, djed);
			sql = "update uf_byjedb set djje = '"+zdj+"' where id = '"+byjk+"'";
			log.writeLog("sql------"+sql);
			rs.executeSql(sql);	
		}
		
		
		return SUCCESS;
	}
	
	  
}
