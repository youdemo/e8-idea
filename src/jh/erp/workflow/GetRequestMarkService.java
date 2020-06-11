package jh.erp.workflow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jh.erp.util.TransUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
/**
 * @Description:  获取流程签字意见接口给erp调用
* @version: 
* @author: jianyong.tang
* @date: 2019年11月7日 下午4:45:37
 */
public class GetRequestMarkService {
	/**
	 * 获取签字意见
	 * @param erpType erp流程类型号
	 * @param ent ent
	 * @param site site
	 * @param djh 单据号
	 * @return 返回值格式 
	 * {"MSG_TYPE":"E","JSONSTR":[],"MSG_CONTENT":"传入的参数存在空值"}
	 * 说明MSG_TYPE 执行结果 E 失败 S成功 JSONSTR 签字意见的jsonarray MSG_CONTENT 执行结果描述
	 * JSONSTR格式
	 * [{"LASTNAME":"牛琳","LOGTYPE":"退回","LC_NO":"125671","TIME":"2019-11-07 16:03:48","REMARK":"","WORKCODE":"00002179","NODENAME":"02.审核"}]
	 * LASTNAME:姓名 LOGTYPE:审批类型 LC_NO:oarequestid TIME:审批时间  REMARK：签字意见 WORKCODE：工号 NODENAME：节点名称
	 */
	public String getRequestMark(String erpType,String ent,String site,String djh){
		BaseBean log = new BaseBean();
		log.writeLog("GetRequestMarkService","获取签字意见 erpType:"+erpType+" ent:"+ent+" site:"+site+" djh:"+djh);
		Map<String, String> retMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		TransUtil tu = new TransUtil();
		RecordSet rs_dt = new RecordSet();
		JSONArray arr = new JSONArray();
		String sql = "";
		String sql_dt = "";
		String requestids = "";
		String flag = "";
		int count =0;
		if("".equals(erpType)||"".equals(ent)||"".equals(site)||"".equals(djh)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "传入的参数存在空值");
			return getJsonStr(retMap,arr);
		}
		sql = "select count(1) as count from uf_wfid_erpwork where erpworkcode='"+erpType+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count<=0){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "无法匹配传入的类型");
			return getJsonStr(retMap,arr);
		}
		String wfid = "";
		String oaent = "";//site字段名
		String oasite = "";//site字段名
		String oadjh = "";//site字段名
		String tableName = "";
		sql = "select * from uf_wfid_erpwork where erpworkcode='"+erpType+"'";
		rs.execute(sql);
		while(rs.next()) {
			wfid = Util.null2String(rs.getString("wfid"));
			oaent = Util.null2String(rs.getString("oaent"));
			oasite = Util.null2String(rs.getString("oasite"));
			oadjh = Util.null2String(rs.getString("oadjh"));
			tableName = tu.getTableName(wfid);
			sql_dt = "select a.requestid from "+tableName+" a ,workflow_requestbase b where a.requestid=b.requestid "
					+ "and a."+oasite+"='"+site+"' and a."+oaent+"='"+ent+"' and a."+oadjh+"='"+djh+"'";
			rs_dt.execute(sql_dt);
			while(rs_dt.next()) {
				requestids = requestids + flag + Util.null2String(rs_dt.getString("requestid"));
				flag=",";
			}
			
		}
		JSONArray jsonstr = null;
		
		log.writeLog("GetRequestMarkService","requestids:"+requestids);
		try {
			jsonstr = getJsonStr(requestids);
		} catch (Exception e) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "获取流程签字意见失败");
			return getJsonStr(retMap,arr);
		}
		retMap.put("MSG_TYPE", "S");
		retMap.put("MSG_CONTENT", "成功");
		log.writeLog("GetRequestMarkService result:"+getJsonStr(retMap,jsonstr));
		return getJsonStr(retMap,jsonstr).replaceAll("<br/>"," ");
	}
	
	/**
	 * 获取流程签字意见json数组
	 * @param requestids 流程号 多个流程号用逗号分隔
	 * @return
	 * @throws Exception
	 */
	private JSONArray getJsonStr(String requestids) throws Exception{
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		String sql = "";
		String nodename="";
		String lastname="";
		String jobtitlename="";
		String time="";
		String logtype="";
		String remark="";
		String workcode="";
		String requestid="";
		if("".equals(requestids)){
			return null;
		}
		
		JSONArray array = new JSONArray();
		sql = "select requestid from workflow_requestbase where requestid in("+requestids+") order by requestid desc";
		rs1.execute(sql);
		while(rs1.next()) {
			String rqid = Util.null2String(rs1.getString("requestid"));
			String sql_remark = "";
			
			int count_remark=0;
			String nodename2="";
			sql_remark=" select count(1) as count  from workflow_requestbase where requestid= "+rqid+" and currentnodetype>=3";
			rs.executeSql(sql_remark);
			if(rs.next()){
				count_remark = rs.getInt("count");
			}
			if(count_remark>0){
				sql_remark=" select  a.currentnodeid,nodename,a.workflowid from workflow_requestbase a, workflow_nodebase b where a.requestid= "+rqid+" and a.currentnodeid=b.id";
				rs.executeSql(sql_remark);
				if(rs.next()){
					JSONObject jo = new JSONObject();
					nodename2 = Util.null2String(rs.getString("nodename"));
					
					//currentnodeid= Util.null2String(rs.getString("currentnodeid"));
					//workflowid = Util.null2String(rs.getString("workflowid"));
					
					jo = new JSONObject();
					jo.put("NODENAME", nodename2);
					jo.put("WORKCODE", "");
					jo.put("LASTNAME", "");
					//jo.put("JOBTITLE", "");
					jo.put("TIME", "");
					jo.put("LOGTYPE", "");
					jo.put("REMARK", "");
					jo.put("LC_NO", rqid);
					
					array.put(jo);
				}
			}else{
				sql_remark="select b.workcode,b.lastname,c.jobtitlename,d.nodename " +
						" from workflow_currentoperator a, hrmresource b, hrmjobtitles c ,workflow_nodebase d " +
						" where a.userid = b.id and b.jobtitle = c.id  and a.nodeid=d.id and a.isremark not in(2,4)  " +
						" and a.requestid = "+rqid;
				rs.executeSql(sql_remark);
				while(rs.next()){
					JSONObject jo = new JSONObject();
					nodename = Util.null2String(rs.getString("nodename"));
					workcode = Util.null2String(rs.getString("workcode"));
					lastname = Util.null2String(rs.getString("lastname"));
					jobtitlename = Util.null2String(rs.getString("jobtitlename"));
					jo.put("NODENAME", nodename);
					jo.put("WORKCODE", workcode);
					jo.put("LASTNAME", lastname);
					//jo.put("JOBTITLE", jobtitlename);
					jo.put("TIME", "");
					jo.put("LOGTYPE", "");
					jo.put("REMARK", "");
					jo.put("LC_NO", rqid);
					
					array.put(jo);
				}
			}
			sql_remark="select a.requestid,d.nodename,  a.operator,b.lastname,b.workcode,c.jobtitlename,a.operatedate+' '+a.operatetime as time, a.logtype, remark "+
					"from workflow_requestlog a,hrmresource b,hrmjobtitles c,workflow_nodebase d where a.operator=b.id and b.jobtitle=c.id "+
                " and a.nodeid=d.id and a.requestid in("+rqid+") and a.logtype not in ('e') "+
					"  order by a.operatedate desc ,a.operatetime desc";
			log.writeLog("testsql:"+sql_remark);
			rs.executeSql(sql_remark);
			int count=1;
			while(rs.next()){
				JSONObject jo = new JSONObject();
				requestid = Util.null2String(rs.getString("requestid"));
				nodename = Util.null2String(rs.getString("nodename"));
				workcode = Util.null2String(rs.getString("workcode"));
				lastname = Util.null2String(rs.getString("lastname"));
				jobtitlename = Util.null2String(rs.getString("jobtitlename"));
				time = Util.null2String(rs.getString("time"));
				logtype = Util.null2String(rs.getString("logtype"));
				logtype = getState(logtype,7);
				remark = removeHtmlTag(Util.null2String(rs.getString("remark"))).replaceAll("<[^<>]*/>","");
	//			if(count == 1){
	//				logtype=getState("new",7);
	//			}
				jo.put("NODENAME", nodename);
				jo.put("WORKCODE", workcode);
				jo.put("LASTNAME", lastname);
				//jo.put("JOBTITLE", jobtitlename);
				jo.put("TIME", time);
				jo.put("LOGTYPE", logtype);
				jo.put("REMARK", remark);
				jo.put("LC_NO", requestid);
				
				array.put(jo);
				count++;
			}
		}
		//log.writeLog("test:"+array.toString());
		return array;
	}
	
	/**
	 * 获取流程操作类型
	 * @param state
	 * @param saplan
	 * @return
	 */
	private String getState(String state,int saplan) {
		String statename = "";
		if(saplan==8){
			if ("0".equals(state)) {
				statename = "Approval";
			} else if ("2".equals(state)) {
				statename = "Submit";
			} else if ("3".equals(state)) {
				statename = "Return";
			} else if ("4".equals(state)) {
				statename = "Reopen";
			} else if ("5".equals(state)) {
				statename = "Delete";
			} else if ("6".equals(state)) {
				statename = "Activation";
			} else if ("7".equals(state)) {
				statename = "Retransmission";
			} else if ("9".equals(state)) {
				statename = "Comment";
			} else if ("a".equals(state)) {
				statename = "Opinion inquiry";
			} else if ("b".equals(state)) {
				statename = "Opinion inquiry reply";
			} else if ("e".equals(state)) {
				statename = "filing";
			} else if ("h".equals(state)) {
				statename = "Transfer";
			} else if ("i".equals(state)) {
				statename = "intervene";
			} else if ("j".equals(state)) {
				statename = "Transfer feedback";	
			} else if ("t".equals(state)) {
				statename = "CC";
			} else if ("s".equals(state)) {
				statename = "Supervise";
			} else if ("i".equals(state)) {
				statename = "Process intervention";
			} else if ("1".equals(state)) {
				statename = "Save";
			} else if ("new".equals(state)) {
				statename = "Create";
			}else {
				statename = "";
			}
		}
		else{
			if ("0".equals(state)) {
				statename = "批准";
			} else if ("2".equals(state)) {
				statename = "提交";
			} else if ("3".equals(state)) {
				statename = "退回";
			} else if ("4".equals(state)) {
				statename = "重新打开";
			} else if ("5".equals(state)) {
				statename = "删除";
			} else if ("6".equals(state)) {
				statename = "激活";
			} else if ("7".equals(state)) {
				statename = "转发";
			} else if ("9".equals(state)) {
				statename = "批注";
			} else if ("a".equals(state)) {
				statename = "意见征询";
			} else if ("b".equals(state)) {
				statename = "意见征询回复";
			} else if ("e".equals(state)) {
				statename = "归档";
			} else if ("h".equals(state)) {
				statename = "转办";
			} else if ("i".equals(state)) {
				statename = "干预";
			} else if ("j".equals(state)) {
				statename = "转办反馈";	
			} else if ("t".equals(state)) {
				statename = "抄送";
			} else if ("s".equals(state)) {
				statename = "督办";
			} else if ("i".equals(state)) {
				statename = "流程干预";
			} else if ("1".equals(state)) {
				statename = "保存";
			}else if ("new".equals(state)) {
				statename = "创建";
			} else {
				statename = "";
			}
		}
		return statename;
	}
	
	/**
	 * 签字意见html格式化
	 * @param content 签字意见remark字段文本
	 * @return
	 */
    private String removeHtmlTag(String content) {
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
		Matcher m = p.matcher(content);
		if (m.find()) {
			content = content
					.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
			content = removeHtmlTag(content);
		}
		return content;
	}
    /**
     * 获取返回值json串
     * @param map 返回结果map
     * @param arr 意见json数组
     * @return
     */
    private String getJsonStr(Map<String, String> map,JSONArray arr) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		try {
			while (it.hasNext()) {
				String key = it.next();
				String value = map.get(key);		
					json.put(key, value);
			}
			json.put("JSONSTR", arr);
	    } catch (JSONException e) {
	    	e.printStackTrace();		
	    }
		

		return json.toString();
	}
}
