package txrz.updatbyed;

import txrz.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-3-5 下午9:18:36
 * 类说明
 */
public class DeductionEmploys  implements Action {
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
		String sql = "select sfsybyj,wssezh,byjk from "+tablename+" where requestid = '"+requestid+"'";
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
			String syje = "";//剩余
			String ysyje = "";//已用
			sql = "select djje,syje,ysyje from uf_byjedb where id = '"+byjk+"'";
			rs.executeSql(sql);
			if(rs.next()){
				jmdjed = Util.null2String(rs.getString("djje"));
				syje = Util.null2String(rs.getString("syje"));
				ysyje = Util.null2String(rs.getString("ysyje"));
			}
			if(jmdjed.equals("")){
				jmdjed = "0.00";
			}
			if(syje.equals("")){
				syje = "0.00";
			}
			if(ysyje.equals("")){
				ysyje = "0.00";
			}
			Double zyy = gu.addDouble(ysyje, djed);
			Double zdj = gu.subDouble(jmdjed, djed);
			Double zsy = gu.subDouble(syje, djed);
			sql = "update uf_byjedb set djje = '"+zdj+"',syje = '"+zsy+"',ysyje='"+zyy+"' where id = '"+byjk+"'";
			rs.executeSql(sql);	
		}
		
		
		return SUCCESS;
	}
	
	  
}