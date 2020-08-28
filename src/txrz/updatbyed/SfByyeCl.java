package txrz.updatbyed;

import txrz.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-2-21 下午4:03:39
 * 差旅释放
 */
public class SfByyeCl  implements Action {
	GetUtil gu = new GetUtil();
	@Override
	public String execute(RequestInfo info) {
		String byjk = "";
		String sfsybyj = "";//
		String djed = "";//表单金额    建模冻结金额
		String jmdjed = "";//建模表单金额
		RecordSet rs = new RecordSet();
		String tablename = info.getRequestManager().getBillTableName();
		String requestid = info.getRequestid();
		String sql = "select sfsybyj,wssehj,byjk from "+tablename+" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			byjk = Util.null2String(rs.getString("byjk"));
			djed = Util.null2String(rs.getString("wssehj"));
			if(djed.equals("")){
				djed = "0.00";
			}
			sfsybyj = Util.null2String(rs.getString("sfsybyj"));
		}
		if(sfsybyj.equals("0")){
			sql = "select djje,syje,ysyje from uf_byjedb where id = '"+byjk+"'";
			rs.executeSql(sql);
			if(rs.next()){
				jmdjed = Util.null2String(rs.getString("djje"));
			}
			if(jmdjed.equals("")){
				jmdjed = "0.00";
			}
			Double zdj = gu.subDouble(jmdjed, djed);
			sql = "update uf_byjedb set djje = '"+zdj+"' where id = '"+byjk+"'";
			rs.executeSql(sql);	
		}
		
		
		return SUCCESS;
	}
	
	  
}