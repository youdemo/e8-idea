package zx.hr.sysn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;

public class PersonInfoConfirm {
	public String infoConfirm(String xm,String gh,String bm,String zjhm,String ryid,String loginip,String xgwd) {
		String result = "";
		String modeid = getModeId("uf_ryxxqr");
		RecordSet rs = new RecordSet();
		SimpleDateFormat sf1  = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowdate = sf1.format(new Date());
		String nowDateTime = sf2.format(new Date());
		Map<String, String> map = new HashMap<String, String>();
		String workcode = "";
		String sql = "select workcode from hrmresource where id="+ryid;
		String wdarr[] = xgwd.split(",");
		rs.execute(sql);
		if(rs.next()){
			workcode = Util.null2String(rs.getString("workcode"));
		}
		InsertUtil iu = new InsertUtil();
		map.put("ryid", ryid);
		map.put("xm", xm);
		map.put("gh", gh);
		map.put("bm", bm);
		map.put("zjhm", zjhm);
		map.put("qrrq", nowdate);
		map.put("ip", loginip);
		map.put("qrsj", nowDateTime);
		map.put("xgwd", wdarr[0]);
		map.put("xgwd2", wdarr[1]);
		map.put("xgwd3", wdarr[2]);
		map.put("txrgh", workcode);
		map.put("modedatacreatedate", nowdate);
		map.put("modedatacreater", "1");
		map.put("modedatacreatertype", "0");
		map.put("formmodeid", modeid);
		iu.insert(map, "uf_ryxxqr");
		sql = "select id from uf_ryxxqr where ryid='"+ ryid + "'";
		String billid = "";
		rs.execute(sql);
		if (rs.next()) {
			billid = Util.null2String(rs.getString("id"));
		}
		if (!"".equals(billid)) {
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(
					Integer.valueOf("1"),
					Util.getIntValue(modeid),
					Integer.valueOf(billid));
		}
		
		return "1";
	}
	
	public String getModeId(String tableName){
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"+tableName+"'";
		rs.execute(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql="select id from modeinfo where  formid="+formid;
		rs.execute(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}

		return modeid;
	}
}
