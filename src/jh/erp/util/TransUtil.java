package jh.erp.util;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class TransUtil {
	/**
	 * 根据选择框显示值 获取现在框value
	 * @param tableName
	 * @param filedname
	 * @param selectname
	 * @return
	 */
	public String getSelectValue(String tableName, String filedname,
			String selectname) {
		RecordSet rs = new RecordSet();
		String value = "";
		String sql = "select c.selectvalue from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"
				+ tableName
				+ "' and a.fieldname='"
				+ filedname
				+ "' and c.selectname='"
				+ selectname
				+ "'";
		//writeLog("sql:"+sql);
		rs.execute(sql);
		if (rs.next()) {
			value = Util.null2String(rs.getString("selectvalue"));
		}
		return value;
	}
	
	
	public String getTableName(String wfid) {
		RecordSet rs = new RecordSet();
		String tableName = "";
		rs.execute("select tablename from workflow_bill where id=(select formid from workflow_base where id=" + wfid + ")");
        if (rs.next()) {
        	tableName = Util.null2String(rs.getString("tablename"));
        }
        return tableName;
	}
	
	public String getResourceIdByLoginid(String loginid ) {
		RecordSet rs = new RecordSet();
		String ryid = "";
		String sql = "select id from hrmresource where loginid='"+loginid+"'";
		rs.execute(sql);
		if(rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}
		return ryid;
	}
	
	public String getWorkCode(String ryid ) {
		RecordSet rs = new RecordSet();
		String workcode = "";
		String sql = "select workcode from hrmresource where id='"+ryid+"'";
		rs.execute(sql);
		if(rs.next()) {
			workcode = Util.null2String(rs.getString("workcode"));
		}
		return workcode;
	}
	
	public String getDepartmentId(String code ) {
		RecordSet rs = new RecordSet();
		String dpid = "";
		String sql = "select id from hrmdepartment where departmentcode='"+code+"'";
		rs.execute(sql);
		if(rs.next()) {
			dpid = Util.null2String(rs.getString("id"));
		}
		return dpid;
	}
	/**
	 * 获取流程附件目录
	 * @return
	 */
	public String getFLowFileDir(String wfid) {
		RecordSet rs = new RecordSet();
		String doccategory = "";
		String sql="select doccategory from workflow_base where id="+wfid;
		rs.executeSql(sql);
		if(rs.next()){
			doccategory = Util.null2String(rs.getString("doccategory"));
		}
		if("".equals(doccategory)){
			return "-1";
		}
		String dcg[]=doccategory.split(",");
		String seccategory=dcg[dcg.length-1];
		//log.writeLog("seccategory:"+seccategory);
		if("".equals(seccategory)){
			return "-1";
		}
		return seccategory;
	}
	
	public String getModeId(String tableName){
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"+tableName+"'";
		rs.executeSql(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql="select id from modeinfo where  formid="+formid;
		rs.executeSql(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
	
	 public void writeLog(String className,Object obj) {
	        if (true) {
	            new BaseBean().writeLog(className, obj);
	        }
	    }

}
