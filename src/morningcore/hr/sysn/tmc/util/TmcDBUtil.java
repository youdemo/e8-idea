package morningcore.hr.sysn.tmc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import weaver.conn.ConnStatement;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;

public class TmcDBUtil {

	
	public boolean insert(Map<String,String> mapStr,String table){
		if(mapStr == null) return false;
		if(mapStr.isEmpty()) return false;
		BaseBean log = new BaseBean();
		String sql_0 = "insert into " + table + "(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";

		Iterator<String> it = mapStr.keySet().iterator();
		while (it.hasNext()) {
			String tmp_1 = it.next();
			// String tmp_1_str = Util.null2String(mapStr.get(tmp_1));
			String tmp_1_str = mapStr.get(tmp_1);
			if (tmp_1_str == null)
				tmp_1_str = "";

			if (tmp_1.length() > 0) {
				sql_1.append(tmp_1);
				sql_1.append(",");

				sql_3.append("?");
				sql_3.append(",");

			}
		}

		String now_sql_1 = sql_1.toString();
		if (now_sql_1.lastIndexOf(",") > 0) {
			now_sql_1 = now_sql_1.substring(0, now_sql_1.length() - 1);
		}

		String now_sql_3 = sql_3.toString();
		if (now_sql_3.lastIndexOf(",") > 0) {
			now_sql_3 = now_sql_3.substring(0, now_sql_3.length() - 1);
		}
		String setvalues = "";
		String sql = sql_0 + now_sql_1 + sql_2 + now_sql_3 + sql_4;
		ConnStatement cs = new ConnStatement();
		//log.writeLog("## sql = " + sql);
		try {
			cs.setStatementSql(sql);
			it = mapStr.keySet().iterator();
			int num=1;
			while (it.hasNext()) {
				String tmp_1 = it.next();
				// String tmp_1_str = Util.null2String(mapStr.get(tmp_1));
				String tmp_1_str = mapStr.get(tmp_1);
				if (tmp_1_str == null)
					tmp_1_str = "";
				if(tmp_1.length()<0 ){
					continue;
				}
				setvalues = setvalues+","+tmp_1_str;
				//log.writeLog("## setvalues = " + setvalues+" num:"+num+" tmp_1:"+tmp_1);
				cs.setString(num,tmp_1_str);
				num++;

			}

			cs.executeUpdate();
			cs.close();
		} catch (Exception e) {
			log.writeLog("sysn","update(sql) = " + sql);
			log.writeLog(e);
			cs.close();
			return false;
		}

		// return false;
		return true;
	}
	
	public boolean update(String table,Map<String,String> mapStr,Map<String,String> whereMap){
		if(mapStr == null || mapStr.isEmpty()) return false;
		if(table == null || "".equals(table)) return false;
		if(whereMap == null|| whereMap.isEmpty()) return false;
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
			
		StringBuffer buff = new StringBuffer();
		buff.append("update ");
		buff.append(table);
		buff.append(" set ");

		Iterator<String> it = mapStr.keySet().iterator();
		String flag = "";
		while (it.hasNext()) {
			String tmp_1 = it.next();
			String tmp_1_str = mapStr.get(tmp_1);
			if (tmp_1_str == null)
				tmp_1_str = "";

			buff.append(flag);
			buff.append(tmp_1);
			buff.append("=");
			if ("".equals(tmp_1_str)) {
				buff.append("null");
			} else {
				buff.append("?");
			}


			flag = ",";

		}


		String whereSql = " where 1=1 ";
		Iterator<String> itx = whereMap.keySet().iterator();
		flag = "";
		while(itx.hasNext()){
			String tmp_1 = itx.next();	
			String tmp_1_str = whereMap.get(tmp_1);
			
			whereSql = whereSql + " and " + tmp_1 + " ='" + tmp_1_str + "' ";
		}
		
		String sql = buff.toString() + whereSql;
		ConnStatement cs = new ConnStatement();
		try {
			cs.setStatementSql(sql);
			it = mapStr.keySet().iterator();
			int num=1;
			while (it.hasNext()) {
				String tmp_1 = it.next();
				// String tmp_1_str = Util.null2String(mapStr.get(tmp_1));
				String tmp_1_str = mapStr.get(tmp_1);
				if (tmp_1_str == null)
					tmp_1_str = "";


				if ("".equals(tmp_1_str)) {

				} else {
					cs.setString(num,tmp_1_str);
					num++;
				}
			}

			cs.executeUpdate();
			cs.close();
		} catch (Exception e) {
			log.writeLog("sysn","update(sql) = " + sql);
			log.writeLog(e);
			cs.close();
			return false;
		}


		return true;
	}
	

	public boolean insertGs(int creater,int modeid,int m_billid){
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.editModeDataShare(creater,modeid,m_billid);
		return true;
	}
	


	public String getNowCode(String code,String flag,String yearM){
		RecordSet rs = new RecordSet();
		String sql = "select * from uf_tmcCode where codef='"+code
				+"' and numf='"+flag+"' and yearm='"+yearM+"'";
		rs.executeSql(sql);
		int now = 0;
		if(rs.next()){
			now = rs.getInt("nextN");
		}
		int nexNow = now + 1;
		if(now == 0){
			now = 1;nexNow = 2;
			sql = "insert into uf_tmcCode(codeF,numf,yearm,nextN) values('"
					+code+"','"+flag+"','"+yearM+"',"+nexNow+")";
		}else{
			sql = "update uf_tmcCode set nextN="+nexNow+"where codef='"+code
					+"' and numf='"+flag+"' and yearm='"+yearM+"'";
		}
		rs.executeSql(sql);
		
		String all_code = code+yearM+String.format("%04d", now);
		
		return all_code;
	}
	
	public String getNowCode(String code,String flag){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String yearM = sdf.format(new Date());
		return getNowCode(code,flag,yearM);
	}
}
