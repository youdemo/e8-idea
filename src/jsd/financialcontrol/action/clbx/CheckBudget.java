package jsd.financialcontrol.action.clbx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jsd.financialcontrol.util.FinalUtil;
import jsd.financialcontrol.util.GetUtil;
import jsd.financialcontrol.util.HttpRequest;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月24日 下午5:21:29 
* @version 1.0 
*/
public class CheckBudget implements Action  {

	@Override
	public String execute(RequestInfo info) {
		
		RecordSet rs = new RecordSet();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		BaseBean logs = new BaseBean();
		//存储  对应返回的行数据
		Map<String ,String > map = new HashMap<String, String>();
		String strParameter  = getKYYSParameter(requestid, tablename,map);
		logs.writeLog(this.getClass().getName(),"strParameter-----"+strParameter);
		String result_kyys = getKYYS(FinalUtil.method_kyys, FinalUtil.appToken_kyys, strParameter);
		logs.writeLog(this.getClass().getName(),"result_kyys-----"+result_kyys);
		try {
			JSONObject jso = new JSONObject(result_kyys);
			String status_re = jso.getString("status");
			String msg_re = jso.getString("msg");
			if("1".equals(status_re)) {
				JSONArray arr = jso.getJSONArray("result");
				for(int i =0;i<arr.length();i++) {
					JSONObject  art = arr.getJSONObject(i);
					String rowNum = art.getString("rowNum");
					String action_re = art.getString("action");
					String accumulativeTotal = art.getString("accumulativeTotal");
					String over_re = art.getString("over");
					String strsq = "update "+tablename+"_dt6  set kyys = '"+accumulativeTotal+"',overfee ='"+over_re
							+"',action ='"+action_re+"',msg='"+msg_re+"',status='"+status_re+"' where id = '"+map.get(rowNum)+"'";
					logs.writeLog(this.getClass().getName(),"updatestrsq----"+strsq);
					rs.execute(strsq);
				}
			}else {
				info.getRequestManager().setMessagecontent("预算数据获取失败："+msg_re);
	            info.getRequestManager().setMessageid("信息提示");
	            return FAILURE_AND_CONTINUE;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 获取登录令牌
	 * @return
	 */
	public String getToken() {
		long dat = new Date().getTime();
		String sec = FinalUtil.secret;
		String username = FinalUtil.username;
		GetUtil gg = new GetUtil();
		String secretKey = "";
		String token = "";
		try {
			secretKey = gg.compute(username, sec, dat+"");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		String str = "method=loginWithoutEncrypt&userName="+username+"&secretKey="+secretKey+"&dateTime="+dat+"&appToken=CONTROLLER_1"; 
		HttpRequest hrt = new HttpRequest();
		String rest  = hrt.sendPost(FinalUtil.loginToken_url, str);
		try {
			JSONObject json = new JSONObject(rest);
			String statu = json.getString("ok");
			if("true".equals(statu)){
				token = json.getJSONObject("body").getString("token");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return token;
		
	}
	/**
	 * 参数组装
	 * @param rid
	 * @param tablename
	 * @param map
	 * @return
	 */
	public String getKYYSParameter(String rid,String tablename,Map <String,String> map) {
		JSONArray arry = new JSONArray();
		String xmlb = "";//项目类型  0 制单相关  1 项目地    2 都不是
		String fcostid = "";//费用项目  科目
		String zxysbm = "";//专项预算编码
		String fyfsrq = "";//费用发生日期
		String empzzmc = "";//emp组织名称
		String xmmc = "";//项目名称
		String mxid = "";//明细3id
		String bxje = "";//报销金额
		GetUtil gu = new GetUtil();
		String sqlstr =  "select m.id as mid,dt.* from  "+tablename +" m join "+tablename+"_dt6 dt on  m.id=dt.mainid   where m.requestid = '"+rid+"'";
		RecordSet  rs = new RecordSet();
		
		String xmbm = "";
		int a = 1;
		rs.execute(sqlstr);
		while(rs.next()) {
			JSONObject json = new JSONObject();
			JSONArray arrydt = new JSONArray();
			mxid = Util.null2String(rs.getString("id"));
			fcostid = Util.null2String(rs.getString("fcostid"));
			zxysbm = Util.null2String(rs.getString("zxysbmnew"));
			fyfsrq = Util.null2String(rs.getString("fyfsrq"));
			xmlb = Util.null2String(rs.getString("xmlb"));
			xmbm =  Util.null2String(rs.getString("xmbm")); 
			xmmc = Util.null2String(rs.getString("xmmc"));
			empzzmc = Util.null2String(rs.getString("empzzmc"));
			bxje = Util.null2String(rs.getString("bxje"));
			String  kmmc = gu.getKMFieldVal("uf_kindeedata", "fname", "fnumber", fcostid+"' and flag = '10");//科目名称
			String zxysbm_name = gu.getFieldVal("uf_kindeedata", "fname", "fnumber", zxysbm+"' and flag='7 ");		
			String year = fyfsrq.substring(0,4);//2020-05-04
			String month = fyfsrq.substring(5, 7);
            if ("0".equals(month.substring(0, 1))) {
                month = month.substring(1);
            }
            //对应返回的行数据
			map.put(""+a, mxid);
			a++;
			if(bxje.length()<1) {
				bxje = "0";
			}
			try {
				json.put("rowNum", mxid);
				json.put("value",bxje);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if("0".equals(xmlb)) {
				arrydt.put(empzzmc);
				kmmc = "项目"+kmmc;
				arrydt.put(kmmc);
				arrydt.put(year+"年");
				arrydt.put(month+"月");
				arrydt.put("执行控制版");
				arrydt.put("预算");
				arrydt.put(xmmc);
				arrydt.put("不分综合");
			}else if("1".equals(xmlb)) {
				arrydt.put(FinalUtil.xmdzz);
				arrydt.put(kmmc);
				arrydt.put(year+"年");
				arrydt.put(month+"月");
				arrydt.put("执行控制版");
				arrydt.put("预算");
				arrydt.put("不分项目");
				arrydt.put("不分综合");
				
			}else if("2".equals(xmlb)) {
				arrydt.put(empzzmc);
				arrydt.put(kmmc);
				arrydt.put(year+"年");
				arrydt.put(month+"月");
				arrydt.put("执行控制版");
				arrydt.put("预算");
				arrydt.put("不分项目");
				arrydt.put("不分综合");
			}else if("3".equals(xmlb)) {
				kmmc = "专项"+kmmc;
				arrydt.put(empzzmc);
				if(zxysbm.startsWith("CEGC")) {
                 	arrydt.put("不分科目");
                 }else {
                	 arrydt.put(kmmc);
                 }
				arrydt.put(year+"年");
				arrydt.put(month+"月");
				arrydt.put("执行控制版");
				arrydt.put("预算");
				arrydt.put(zxysbm_name);
				if(zxysbm.startsWith("CEGC")) {
					arrydt.put(zxysbm_name.substring(0, zxysbm_name.lastIndexOf("-"))+".");
                 }else {
                	 arrydt.put("不分综合");
                 }
			}
			try {
				json.put("members",arrydt );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			arry.put(json);
		
		}
		
		return arry.toString(); 	
	}
	
	
	
	/**
	 * 组装 查询可用预算的数据
	 * @param method_kyys  方法名
	 * @param appToken_kyys  数据参数
	 * @param st
	 * @return
	 */
	 
	public String getKYYS(String method_kyys,String appToken_kyys,String st) {
		JSONObject json = new JSONObject();
		JSONArray jsonarr1 = new JSONArray();
		JSONArray jsonarr2 = new JSONArray();
		String  str = "[{\"name\":\"组织\",\"valueType\":0},{\"name\":\"科目\",\"valueType\":0}," + 
				"{\"name\":\"年\",\"valueType\":0},{\"name\":\"期间\",\"valueType\":0},{\"name\":\"版本\",\"valueType\":0},"
				+ "{\"name\":\"场景\",\"valueType\":0},{\"name\":\"项目\",\"valueType\":0},{\"name\":\"综合\",\"valueType\":0}]";
		
		try {
			jsonarr1 = new JSONArray(str);
			jsonarr2 = new JSONArray(st);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		try {
			json.put("controlRuleCode", "");
			json.put("dimensions", jsonarr1);
			json.put("cells", jsonarr2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new BaseBean().writeLog(this.getClass().getName(),"获取可用预算数据-传参---"+json.toString());
		
		String token = getToken();
		HttpRequest hrt = new HttpRequest();
		String str_parm = "";
		try {
			str_parm = "method="+method_kyys+"&token="+token+"&appToken="+appToken_kyys+"&parameters="+URLEncoder.encode(URLEncoder.encode(json.toString(),"utf-8"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new BaseBean().writeLog(this.getClass().getName(),"获取可用预算数据-str_parm---"+str_parm);
		String result_str =  hrt.sendPost(FinalUtil.ys_url, str_parm);
		return result_str;
		
	}

}
