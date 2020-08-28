package txrz.pz;

import jxl.read.biff.Record;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class PzUtil {
	public String getModeId(String tableName) {
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"
				+ tableName + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			formid = Util.null2String(rs.getString("id"));
		}
		sql = "select id from modeinfo where  formid=" + formid;
		rs.executeSql(sql);
		if (rs.next()) {
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
	/**
	 * 获取辅助核算
	 */
	public String getFZHS(String kmdm) {
		RecordSet rs = new RecordSet();
		String hsxm = "";
		String sql = "select hsxm from uf_kmxx where kmdm='"+kmdm+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			hsxm = Util.null2String(rs.getString("hsxm"));
		}
		return hsxm;
	}

	public String getSelectDtValue(String tableName,String detailtable,String filedname,String value){
		String result = "";
		RecordSet rs = new RecordSet();
		String sql = "select selectname from workflow_billfield t1, workflow_bill t2,workflow_selectitem t3 where t1.billid=t2.id and t3.fieldid=t1.id  and t2.tablename='"+tableName+"' and t1.fieldname='"+filedname+"' and t3.selectvalue='"+value+"' and t1.detailtable='"+detailtable+"'";
	    rs.execute(sql);
	    if(rs.next()){
			result = Util.null2String(rs.getString("selectname"));
		}
	    return result;
	}
}
