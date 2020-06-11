package gvo.cowork;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * @Description: 门户显示值转换
* @version: 
* @author: jianyong.tang
* @date: 2019年9月25日 上午10:00:54
 */
public class PortalTransUtil {
	public String getJhTitle(String topdiscussid) {
		RecordSet rs = new RecordSet();
		String result = "";
		if("".equals(topdiscussid)) {
			return "";
		}
		String name = "";
		String floornum = "";
		String remark = "";
		String sql = "select b.name,'#'||a.floornum as floornum,regexp_replace(a.remark,'</?[^>]*>|nbsp;|&','') as remark from cowork_discuss a,cowork_items b where  a.coworkid=b.id and  a.id="+topdiscussid;
		rs.executeSql(sql);
		if(rs.next()) {
			name = Util.null2String(rs.getString("name"));
			floornum = Util.null2String(rs.getString("floornum"));
			remark = Util.null2String(rs.getString("remark"));
		}
		result = name+"-"+floornum;
		if(!"".equals(remark)) {
			result = result + "-"+remark;
		}
		return result;
	}
	
	public String getCoWorkPersonName(String creater,String otherpara) {
		String otherParas[] = Util.TokenizerString2(otherpara, "+");
		String userid = otherParas[1];
		String discussid = otherParas[0];
		RecordSet rs = new RecordSet();
		String sql = "";
		String isrealname = "";
		String lastname = "";
		String isanonymous = "";//add 20191225兼容之前的匿名
		int count = 0;
		String canseeLink = "0";
		String roleid= Util.null2o(weaver.file.Prop.getPropValue("CoworkNickname", "roleid"));
		sql = "select count(1) as count from hrmrolemembers where roleid in("+roleid+") and resourceid ="+userid;
		rs.execute(sql);
		if(rs.next()) {
			count = rs.getInt("count");
		}
		if(count >0) {
			canseeLink = "1";
		}
		if(!"-1".equals(discussid)) {
			sql = "select isrealname,isadminsee,isanonymous from cowork_discuss where id="+discussid;
			rs.execute(sql);
			if(rs.next()) {
				isrealname = Util.null2String(rs.getString("isrealname"));
				isanonymous = Util.null2String(rs.getString("isanonymous"));
			}
		}
		if("1".equals(isrealname)) {
			sql = "select name from nickname where userid ="+creater;
			rs.execute(sql);
			if(rs.next()) {
				lastname = Util.null2String(rs.getString("name"));
			}
		}else if("1".equals(isanonymous)){
			lastname = "匿名";
			canseeLink = "0";

		}else{
			sql = "select lastname from hrmresource where id ="+creater;
			rs.execute(sql);
			if(rs.next()) {
				lastname = Util.null2String(rs.getString("lastname"));
			}
		}
		if("0".equals(canseeLink)) {
			return lastname;
		}else {
			return "<a href=\"javascript:openhrm("+creater+")\" onclick=\"pointerXY(event);\">"+lastname+"</a>";
		}
		
	}
}
