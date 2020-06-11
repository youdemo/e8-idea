package jsd.financialcontrol.serviceipms;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.axis2.AxisFault;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.media.Log;

import jsd.financialcontrol.services.FactDataServiceStub;
import jsd.financialcontrol.services.FactDataServiceStub.Header;
import jsd.financialcontrol.services.FactDataServiceStub.QueryFactData;
import jsd.financialcontrol.services.FactDataServiceStub.SoapHeader;
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
* @date 创建时间：2020年5月17日 下午3:57:16 
* @version 1.0  预算数据读取  传文本
*/
public class BudgetDataReading   implements Action  {

	@Override
	public String execute(RequestInfo info ) {
		RecordSet rs = new RecordSet();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		String mid = "";//
		String  kyys_url = FinalUtil.ys_url; 
		BaseBean logs = new BaseBean();
//		明细3
		String xmlb = "";//项目类型  0 制单相关  1 项目地    2 都不是
		
		String fcostid = "";//费用项目  科目
		String ejbm = "";//费用二级部门
		String zxysbm = "";//专项预算编码
		String xmdbhm = "";//项目地编码
		
		String fyfsrq = "";//费用发生日期
		String qy = "";//区域编码
		String bmbm = "";//部门编码
		String zxbm = "";//专项编码
		String xmbm = "";//项目编码
		String kyys = "";//可用预算
		String ghys = "";//规划预算
		String jdys = "";//解冻预算
		String sjqrs = "";//实际确认数
		String ztyss = "";//在途预算数
		String msg = "";//响应消息
		String ysqj = "";//预算期间
		String overfee = "";//是否超预算
		String action = "";//控制强度
		String empzzbm = "";//emp组织编码
		String status = "";//成功信息
		String empzzmc = "";//emp组织名称
		String xmmc = "";//项目名称
		String mxid = "";//明细3id
		String bxje = "";//报销金额
		GetUtil gu = new GetUtil();
		String str = "JST007007000000000";//项目地的固定数据
		String sql = "select m.id as mid,dt.* from  "+tablename +" m join "+tablename+"_dt3 dt on  m.id=dt.mainid   where m.requestid = '"+requestid+"'";
		logs.writeLog("sqls-----"+sql);
		rs.execute(sql);
		try {
			while (rs.next()) {
				mid = Util.null2String(rs.getString("mid"));
				mxid = Util.null2String(rs.getString("id"));
				fcostid = Util.null2String(rs.getString("fcostid"));
				ejbm = Util.null2String(rs.getString("ejbm"));
				xmbm = Util.null2String(rs.getString("xmbm"));
				zxysbm = Util.null2String(rs.getString("zxysbm"));
				xmdbhm = Util.null2String(rs.getString("xmdbhm"));
				fyfsrq = Util.null2String(rs.getString("fyfsrq"));
				xmlb = Util.null2String(rs.getString("xmlb"));
				xmmc = Util.null2String(rs.getString("xmmc"));
				empzzmc = Util.null2String(rs.getString("empzzmc"));
				bxje = Util.null2String(rs.getString("bxje"));
				String kmmc = gu.getFieldVal("uf_kindeedata", "fname", "fnumber", fcostid + "' and flag = '1");//科目名称
				String zxysbm_name = gu.getFieldVal("uf_kindeedata", "fname", "fnumber", zxysbm + "' and flag='7 ");

				String year = fyfsrq.substring(0, 4);//2020-05-04
				String month = fyfsrq.substring(6, 7);
				JSONObject jo = new JSONObject();
				if ("0".equals(xmlb)) {
					String jsonstr = "{\"rowNum\":" + mid + ",\"members\":[\"" + empzzmc + "\",\"" + kmmc + "\",\"" + year + "年\",\"" + month + "月\",\"执行控制版\",\"预算\",\"" + xmmc + "\",\"不分综合\"],\"value\":" + bxje + "}";
					jo = new JSONObject(jsonstr);
//				getMultiMembers("规划预算,预算,实际确认,预算在途");
//				getUniqueMembers(xmmc, year, month, empzzmc, "不分综合", kmmc);

					//String res_xml = getInfos("模型一", getMultiMembers("规划预算,预算,实际确认,预算在途"), getUniqueMembers(xmmc, year, month, empzzmc, "不分综合", kmmc));
					//String jsonstr_kyys = getKYYS(FinalUtil.method_kyys, FinalUtil.appToken_kyys, xmmc, year, month, empzzmc, "不分综合", kmmc, mxid, bxje);
					//logs.writeLog("res_xml1----" + res_xml);
					//logs.writeLog("jsonstr_kyys1----" + jsonstr_kyys);
//				<S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
//				   <S:Body>
//				      <ns2:queryFactDataResponse xmlns:ns2="http://service.ws.module.olap.intelcube.com/">
//				         <return><![CDATA[<?xml version="1.0" encoding="utf-8"?>
//				         <result><returnCode>2000</returnCode>
//				         <returnMessage></returnMessage><cells>
//				         <cell member="规划预算" membercode="">335772.68</cell>
//				         <cell member="预算" membercode="">2344</cell>
//				         <cell member="预算" membercode="">2344</cell></cells></result>]]></return>
//				      </ns2:queryFactDataResponse>
//				   </S:Body>
//				</S:Envelope>
				} else if ("1".equals(xmlb)) {
					String jsonstr = "{\"rowNum\":" + mid + ",\"members\":[\"售服中心\",\"" + kmmc + "\",\"" + year + "年\",\"" + month + "月\",\"执行控制版\",\"预算\",\"" + xmmc + "\",\"不分综合\"],\"value\":" + bxje + "}";
					jo = new JSONObject(jsonstr);
//					String res_xml = getInfos("模型一", getMultiMembers("规划预算,预算,实际确认,预算在途"), getUniqueMembers(str, year, month, "售服中心", "不分综合", kmmc));
//					String jsonstr_kyys = getKYYS(FinalUtil.method_kyys, FinalUtil.appToken_kyys, str, year, month, "售服中心", "不分综合", kmmc, mxid, bxje);
//					logs.writeLog("res_xml2----" + res_xml);
//					logs.writeLog("jsonstr_kyys2----" + jsonstr_kyys);
				} else if ("2".equals(xmlb)) {
					String jsonstr = "{\"rowNum\":" + mid + ",\"members\":[\""+empzzmc+"\",\"" + kmmc + "\",\"" + year + "年\",\"" + month + "月\",\"执行控制版\",\"预算\",\"不分项目\",\"不分综合\"],\"value\":" + bxje + "}";
					jo = new JSONObject(jsonstr);
//					String res_xml = getInfos("模型一", getMultiMembers("规划预算,预算,实际确认,预算在途"), getUniqueMembers("不分项目", year, month, empzzmc, "不分综合", kmmc));
//					String jsonstr_kyys = getKYYS(FinalUtil.method_kyys, FinalUtil.appToken_kyys, "不分项目", year, month, empzzmc, "不分综合", kmmc, mxid, bxje);
//					logs.writeLog("res_xml3----" + res_xml);
//					logs.writeLog("jsonstr_kyys3----" + jsonstr_kyys);
				} else if ("3".equals(xmlb)) {
					String jsonstr = "{\"rowNum\":" + mid + ",\"members\":[\""+empzzmc+"\",\"" + kmmc + "\",\"" + year + "年\",\"" + month + "月\",\"执行控制版\",\"预算\",\""+kmmc+"\",\"不分综合\"],\"value\":" + bxje + "}";
					jo = new JSONObject(jsonstr);
//					String res_xml = getInfos("模型一", getMultiMembers("规划预算,预算,实际确认,预算在途"), getUniqueMembers(zxysbm_name, year, month, empzzmc, "不分综合", kmmc));
//					String jsonstr_kyys = getKYYS(FinalUtil.method_kyys, FinalUtil.appToken_kyys, zxysbm_name, year, month, empzzmc, "不分综合", kmmc, mxid, bxje);
//					logs.writeLog("res_xml4----" + res_xml);
//					logs.writeLog("jsonstr_kyys4----" + jsonstr_kyys);
				}



			}
		}catch (Exception e){

		}
		
//		<option value="0">制单相关</option><option value="1">项目地相关</option>
//		<option value="2">部门相关</option><option value="3">职能专项相关</option>

		return SUCCESS;
	}
	/**
	 * 获取返回数据
	 * @param CubeName  模型一
	 * @param MultiMembers  [{"场景":["规划预算"]}]
	 * @param MultiMembers [{"项目":"不分项目"},{"版本":"执行控制版"},{"年":"2020年"},{"期间":"5月"},{"组织":"董事长办公室"},{"综合":"不分综合"},{"科目":"公积金"}]
	 * @return
	 */
	public String getInfos(String CubeName,String MultiMembers,String UniqueMembers) {
		BaseBean log = new BaseBean();
		log.writeLog("CubeName----"+CubeName+"------MultiMembers----"+MultiMembers+"----UniqueMembers---"+UniqueMembers);
		FactDataServiceStub  fds = null;
		try {
			fds = new FactDataServiceStub();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QueryFactData qf = new QueryFactData();
		Header hed = new Header();
		SoapHeader sh = new SoapHeader();
		sh.setAppcode(FinalUtil.appcode);
		sh.setPassword(FinalUtil.password);
		sh.setType(0);//待定
		sh.setUsername(FinalUtil.username);
		hed.setHeader(sh);
		qf.setCubeName(CubeName);
		qf.setMultiMembers(MultiMembers);
		qf.setUniqueMembers(UniqueMembers);
		String response = "";
		try {
			response = fds.queryFactData(qf, hed).get_return().toString();
			log.writeLog("查询返回的数据-----"+response);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	/**
	 * 组装场景
	 * @param str
	 * @return
	 */
	public String getMultiMembers(String str) {
		String sst []= str.split(",");
		StringBuffer sb = new StringBuffer();
		sb.append("[{\"场景\":[");
		String  flag = "";
		for(int i =0 ;i<sst.length;i++) {
			sb.append(flag);
			sb.append("\""+sst[i]+"\"");
		}
		sb.append("]}]");
		return sb.toString();
	}
	/**
	 * 组装数据
	 * @param xm
	 * @param year
	 * @param month
	 * @param org  主体    --- 可能部门，可能项目
	 * @param zh
	 * @param km
	 * @return
	 */
	public String getUniqueMembers(String xm,String year,String month,String org,String zh,String  km) {
//		[{"项目":"不分项目"},{"版本":"执行控制版"},{"年":"2020年"},{"期间":"5月"},{"组织":"董事长办公室"},{"综合":"不分综合"},{"科目":"公积金"}]
		String ust = "[{\"项目\":\""+xm+"\"},{\"版本\":\"执行控制版\"},{\"年\":\""+year+"年\"},{\"期间\":\""+month
				+"月\"},{\"组织\":\""+org+"\"},{\"综合\":\"不分综合\"},{\"科目\":\""+km+"\"}]";
		return ust;
		
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		{"body":{"token":"bjJQS3NrOE41MXdqbFlkMEtxVTEvMDhubk92eHZFKy8rWWZQcC9laVFuWEVCM2FmZWVjQWV5RDVjc0ZndDFFMnZnQWpxVG9mQVA2aw0KWUtia1hMRmQzNHd4NUh4eFA1Q2M5VzIvTHRtTU1OVT0="},"ok":true,"msg":"请求成功"}
		return token;
		
	}
	/**
	 * 组装 查询可用预算的数据
	 * @param tablename  明细表明
	 * @param mainid  主表id
	 * @param kmmc 
	 * @param string 
	 * @param empzzmc 
	 * @param month 
	 * @param year 
	 * @param xmmc 
	 * @param bxje 
	 * @return
	 */
	public String getKYYS(String method_kyys,String appToken_kyys, String xmmc, String year, String month, String empzzmc, String zh, String kmmc,String mid, String bxje) {
		JSONObject json = new JSONObject();
		JSONArray jsonarr1 = new JSONArray();
		JSONArray jsonarr2 = new JSONArray();
		String  str = "[{\"name\":\"组织\",\"valueType\":0},{\"name\":\"科目\",\"valueType\":0}," + 
				"{\"name\":\"年\",\"valueType\":0},{\"name\":\"期间\",\"valueType\":0},{\"name\":\"版本\",\"valueType\":0},"
				+ "{\"name\":\"场景\",\"valueType\":0},{\"name\":\"项目\",\"valueType\":0},{\"name\":\"综合\",\"valueType\":0}]";
		
		String st ="[{\"rowNum\":"+mid+",\"members\":[\""+empzzmc+"\",\""+kmmc+"\",\""+year+"年\",\""+month+"月\",\"执行控制版\",\"预算\",\""+xmmc+"\",\"不分综合\"],\"value\":"+bxje+"}]";
		try {
			jsonarr1 = new JSONArray(str);
			jsonarr2 = new JSONArray(st);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			json.put("controlRuleCode", "");
			json.put("dimensions", jsonarr1);
			json.put("cells", jsonarr2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String token = getToken();
		HttpRequest hrt = new HttpRequest();
		String str_parm = "method="+method_kyys+"&token="+token+"&appToken="+appToken_kyys+"&parameters="+json.toString();
		new BaseBean().writeLog("获取可用预算数据-str_parm---"+str_parm);
		String result_str =  hrt.sendPost(FinalUtil.ys_url, str_parm);
		 
		
//		{"controlRuleCode":"testRule","dimensions":[{"name":"组织","valueType":0},{"name":"科目","valueType":0},
//	     {"name":"年","valueType":0},{"name":"期间","valueType":0},{"name":"版本","valueType":0},{"name":"场景","valueType":0},
//	     {"name":"项目","valueType":0},{"name":"综合","valueType":0}],
//			"cells":}
//		
		return result_str;
		
	}
	
	
	
	

}
