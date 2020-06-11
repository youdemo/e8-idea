package goodbaby.gns.bb;

import com.caucho.boot.ManagementConfig.User;

import goodbaby.pz.GetGNSTableName;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class BbTransUtil {
	/**
	  * 获取所有部门名称
	 * @param ryid
	 * @return
	 */
	public String getPersonAllDepartmentName(String ryid) {
		String allDepartmentName = "";
		String deptid = "";
		RecordSet rs = new RecordSet();
		if("".equals(ryid)) {
			return "";
		}
		String sql = "select departmentid from hrmresource where id="+ryid;
		rs.executeSql(sql);
		if(rs.next()) {
			deptid = Util.null2String(rs.getString("departmentid"));
		}
		allDepartmentName = getAllDepartmentName(deptid);
		return allDepartmentName;
	}
	/**
	 * 递归获取部门名称
	 * @param deptid
	 * @return
	 */
	public String getAllDepartmentName(String deptid) {
		RecordSet rs = new RecordSet();
		String supdepid = "";
		String departmentname = "";
		String sql = "select departmentname,supdepid from HrmDepartment where id="+deptid;
		rs.executeSql(sql);
		if(rs.next()) {
			departmentname = Util.null2String(rs.getString("departmentname"));
			supdepid = Util.null2String(rs.getString("supdepid"));
		}
		if(departmentname.indexOf("~`~`7")>=0) {
			departmentname = departmentname.substring(departmentname.indexOf("`~`7")+4, departmentname.indexOf("`~`8"));
		}
		if("".equals(supdepid)||"0".equals(supdepid)||"-1".equals(supdepid)) {
			return departmentname;
		}else {
			return getAllDepartmentName(supdepid)+"/"+departmentname;
		}
		
	}
	
	public String getAllThdh(String rkd) {
		RecordSet rs = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String lrktablename = gg.getTableName("THD");
		String thdhall = "";
		String flag = "";
		String thdh = "";
		String sql = "select THDH from "+lrktablename+" t,"+lrktablename+"_dt1 t1 where t.id=t1.mainid and t1.RKD='"+rkd+"'";
		rs.executeSql(sql);
		while(rs.next()) {
			thdh = Util.null2String(rs.getString("THDH"));
			thdhall = thdhall+flag+thdh;
			flag = "<br/>";
		}
		return thdhall;
	}
	
	public String getAttachLink(String attachid) {
		StringBuffer sb = new StringBuffer();
		RecordSet rs = new RecordSet();
		String sql = "";
		String imagefileid = "";
		String imagefilename = "";
		if(!"".equals(attachid)) {
			sql = " select imagefileid,imagefilename from docimagefile where docid in("+attachid+")";
			rs.executeSql(sql);
			while(rs.next()) {
				imagefileid = Util.null2String(rs.getString("imagefileid"));
				imagefilename = Util.null2String(rs.getString("imagefilename"));
				if(sb.length()>0) {
					sb.append("<br/>");
				}
				sb.append("<a href='/weaver/weaver.file.FileDownload?fileid=" + imagefileid + "&download=1' title='下载' >" + imagefilename + "</a>");
			}
		}
		return sb.toString();
	}
	
	public String getFphm(String rkid) {
		String result = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		String flag = "";
		sql = "select fphm from uf_invoice a, uf_invoice_dt1 b where a.id=b.mainid and b.rid="+rkid;
		rs.execute(sql);
		if(rs.next()) {
			result = result + flag + Util.null2String(rs.getString("fphm"));
			flag = "</br>";
		}
		return result;
	}
	
	public String getfkpzh(String rkid) {
		String result = "";
		RecordSet rs = new RecordSet();
		String sql = "";
		String flag = "";
		sql = "select distinct t1.requestmark from formtable_main_273 t,workflow_requestbase t1,formtable_main_273_dt1 t2 where t.requestid=t1.requestid and t.id=t2.mainid and t1.currentnodetype=3 and t2.fphm in(select a.id from uf_invoice a, uf_invoice_dt1 b where a.id=b.mainid and b.rid="+rkid+")";
		rs.execute(sql);
		if(rs.next()) {
			result = result + flag + Util.null2String(rs.getString("requestmark"));
			flag = "</br>";
		}
		return result;
	}
	
}
