///*
// *
// * Copyright (c) 2001-2018 泛微软件.
// * 泛微协同商务系统,版权所有.
// *
// */
//package weaver.ofs.interfaces;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.security.MessageDigest;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.lang3.StringUtils;
//import org.jdom.Document;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;
//
//import weaver.conn.RecordSet;
//import weaver.general.AES;
//import weaver.general.BaseBean;
//import weaver.general.GCONST;
//import weaver.general.Util;
//import weaver.hrm.resource.ResourceComInfo;
//import weaver.integration.logging.Logger;
//import weaver.integration.logging.LoggerFactory;
//import weaver.interfaces.HrmTransferDao;
//import weaver.ofs.service.OfsSysInfoService;
//import weaver.workflow.msg.PoppupRemindInfoUtil;
//import weaver.workflow.request.todo.DataObj;
//import weaver.workflow.request.todo.RequestStatusObj;
//import weaver.interfaces.sso.cas.CasUtil;
//
///**
// * 统一待办E8客户端推送类
// * @version 1.0
// */
//public class SendRequestStatusDataImplForE8 implements SendRequestStatusDataInterfaces{
//	//////////////////////////////////////////////////////////////////
//	//系统变量start
//
//	/**
//     * 日志打印对象
//     */
//	private static Logger log = LoggerFactory.getLogger(SendRequestStatusDataImplForE8.class);
//
//	/**
//	 * 配置id
//	 */
//	private String id = "";
//
//	/**
//	 * 系统编号
//	 */
//	private String syscode = "";
//
//	/**
//	 * 服务地址
//	 */
//	private String serverurl = "";
//
//	/**
//	 * 流程白名单
//	 */
//	public ArrayList<String> workflowwhitelist ;
//
//	/**
//	 * 人员白名单
//	 */
//    public ArrayList<String> userwhitelist ;
//
//    /**
//	 * settingid为ofs_sendinfo的id，实现类需要定义String id
//	 * @return
//	 */
//	public String getId() {
//		return this.id;
//	}
//
//	/**
//     * syscode为系统标识，实现类需要定义String syscode ;
//     * @return
//     */
//	public String getSyscode() {
//		return this.syscode;
//	}
//
//	/**
//     * serverurl为服务端访问url，实现类需要定义String serverurl ;
//     * @return
//     */
//	public String getServerurl() {
//		return this.serverurl;
//	}
//
//	/**
//     * 流程白名单 实现类需要定义ArrayList<String> workflowwhitelist
//     * @return
//     */
//	public ArrayList<String> getWorkflowwhitelist() {
//		return this.workflowwhitelist;
//	}
//
//	/**
//     * 人员白名单 实现类需要定义ArrayList<String> userwhitelist
//     * @return
//     */
//	public ArrayList<String> getUserwhitelist() {
//		return this.userwhitelist;
//	}
//
//	//系统变量end
//	//////////////////////////////////////////////////////////////////
//	//接口变量start
//
//	/**
//     * mobile加密串
//     */
//    private String mobilesecret = "";
//    //云桥地址
//    private String yunqiaourl = "";
//
//
//    //接口变量end
//    //////////////////////////////////////////////////////////////////
//    /**
//     * 发送接口数据
//     *
//     * @param datas
//     */
//	public void SendRequestStatusData(ArrayList<DataObj> datas) {
//		log.info("Ecology统一待办数据推送：start");
//		try{
//		for (int i = 0; i < datas.size(); i++) {
//			DataObj obj = datas.get(i);//获取推送数据
//			String receivets = obj.getSendtimestamp();//时间戳
//
//			//已办批量传输异构系统start
//			ArrayList<RequestStatusObj> donedatas = obj.getDonedatas();
//			JSONArray jsonarr_done = new JSONArray();
//			for(int j = 0; j<donedatas.size(); j++){
//				RequestStatusObj requestStatusObj = donedatas.get(j);
//				JSONObject json = getJsonData(requestStatusObj,receivets);
//				String isremark = requestStatusObj.getIsremark();
//				if("4".equals(isremark)){
//					isremark = "4";
//				}else{
//					if("1".equals(requestStatusObj.getIscomplete())){
//						isremark = "4";
//					}else{
//						isremark = "2";
//					}
//				}
//				if(json != null){
//					json.put("isremark", isremark);
//					jsonarr_done.add(json);
//				}
//			}
//			log.error("Ecology统一待办数据推送：已办数据="+jsonarr_done.toString());
//			postData2ofs("/rest/ofs/batchReceiveRequestInfoByJson",jsonarr_done.toString());
//			//已办批量传输异构系统end
//
//			//代办批量传输异构系统start
//			ArrayList<RequestStatusObj> tododatas = obj.getTododatas();
//			JSONArray jsonarr_todo = new JSONArray();
//			for(int j = 0; j<tododatas.size(); j++){
//				RequestStatusObj requestStatusObj = tododatas.get(j);
//				JSONObject json = getJsonData(requestStatusObj,receivets);
//				if(json != null){
//					json.put("isremark", "0");
//					jsonarr_todo.add(json);
//				}
//			}
//			log.error("Ecology统一待办数据推送：代办数据="+jsonarr_todo.toString());
//			postData2ofs("/rest/ofs/batchReceiveRequestInfoByJson",jsonarr_todo.toString());
//			//代办批量传输异构系统end
//
//			//删除批量传输异构系统start
//			ArrayList<RequestStatusObj> deldatas = obj.getDeldatas();
//			RecordSet rsrtx = new RecordSet();
//			JSONArray jsonarr_del = new JSONArray();
//			for(int j = 0; j<deldatas.size(); j++){
//				RequestStatusObj requestStatusObj = deldatas.get(j);
//				JSONObject json = new JSONObject();
//				json.put("syscode", syscode);
//				json.put("flowid", requestStatusObj.getRequestid()+"");
//				//json.put("userid", requestStatusObj.getUser().getLoginid());
//				json.put("userid", getHrmstr( requestStatusObj.getUser().getUID()));
//				jsonarr_del.add(json);
//			}
//			log.error("Ecology统一待办数据推送：删除数据="+jsonarr_del.toString());
//			postData2ofs("/rest/ofs/batchDeleteUserRequestInfoByJson",jsonarr_del.toString());
//			//删除批量传输异构系统end
//
//
//		}
//		}catch (Exception e) {
//			log.error(e.getMessage(),e);
//		}
//		log.info("Ecology统一待办数据推送：end");
//	}
//
//	/**
//	 * 构造流程请求json对象
//	 *
//	 * @param requestStatusObj
//	 * @param receivets
//	 * @return
//	 */
//	private JSONObject getJsonData(RequestStatusObj requestStatusObj, String receivets){
//		String pcurl = getPcUrl(requestStatusObj.getUser().getUID()+"", requestStatusObj.getRequestid()+"");
//		String appurl =  getAppUrl(requestStatusObj);
//
//		//String appurl = yunqiaourl+"?detailid="+requestStatusObj.getRequestid();
//
//
//		JSONObject json = new JSONObject();
//		json.put("syscode", syscode);
//		json.put("flowid", requestStatusObj.getRequestid()+"");
//		json.put("requestname", requestStatusObj.getRequestnamenew());
//		json.put("workflowname", requestStatusObj.getWorkflowname());
//		json.put("nodename", requestStatusObj.getNodename());
//		json.put("pcurl", pcurl);
//		json.put("appurl", appurl);
//		json.put("viewtype", requestStatusObj.getViewtype().equals("-2")?"1":requestStatusObj.getViewtype());
//		//json.put("creator", requestStatusObj.getCreator().getLoginid());
//		json.put("creator", getHrmstr( requestStatusObj.getCreatorid()));
//		json.put("createdatetime", requestStatusObj.getCreatedate()+" "+requestStatusObj.getCreatetime());
//		//json.put("receiver", requestStatusObj.getUser().getLoginid());
//		json.put("receiver", getHrmstr( requestStatusObj.getUser().getUID()));
//		json.put("receivedatetime", requestStatusObj.getReceivedate()+" "+requestStatusObj.getReceivetime());
//		json.put("receivets", receivets);
//
//		String workflowid = requestStatusObj.getWorkflowid()+"";
//		String requestid = requestStatusObj.getRequestid()+"";
//		String resourceid = requestStatusObj.getUser().getUID()+"";
//
//		//判断该条流程是否需要发送异构系统
//		if(workflowwhitelist==null){
//			workflowwhitelist = new ArrayList<String>();
//		}
//		if(workflowwhitelist.size() > 0 && workflowwhitelist.indexOf(workflowid)==-1){//流程白名单
//			log.info("Ecology统一待办数据推送：流程白名单中没有设置该流程，不需要发送，workflowid="+workflowid+"，requestid="+requestid);
//			return null;	//不发送
//		}
//		//判断该条人员是否需要发送异构系统或者
//		if(userwhitelist==null){
//			userwhitelist = new ArrayList<String>();
//		}
//		if(userwhitelist.size() > 0 && userwhitelist.indexOf(resourceid)==-1){//人员白名单
//			log.info("Ecology统一待办数据推送：人员白名单中没有设置该用户，不需要发送，workflowid="+workflowid+", requestid="+requestid+"，userid="+resourceid);
//			return null;	//不发送
//		}
//		RecordSet rs = new RecordSet();
//		rs.executeQuery("select status from hrmresource where id = ?",requestStatusObj.getUser().getUID());
//		if(rs.next()){
//			int status = rs.getInt("status");
//			if(status<4){
//				return json;
//			}else{
//				log.info("当前人员离职，不发送，userid："+requestStatusObj.getUser().getUID());
//				return null;	//不发送
//			}
//		}else{
//			log.info("当前人员信息无效，不发送，userid："+requestStatusObj.getUser().getUID());
//			return null;	//不发送
//		}
//
//	}
//
//	/**
//	 * 构造emobile流程请求地址
//	 *
//	 * @param requestStatusObj
//	 * @return
//	 */
//
//	private String getAppUrl(RequestStatusObj requestStatusObj) {
//		//app流程地址
//		StringBuilder appurl = new StringBuilder();
//		appurl.append("/mobile/plugin/1/view.jsp?detailid=").append(requestStatusObj.getRequestid()).append("&fromofs=1");
//
//		//app单点登录地址
//		StringBuilder appSsoUrl = new StringBuilder();
//		appSsoUrl.append("/verifyLogin.do");
//		//传入单点登录后的app流程地址，需要做urlencode;
//		appSsoUrl.append("?url=").append(URLEncoder.encode(appurl.toString()));
//		//传入app的链接秘钥，如果链接秘钥为空，则使用服务端的链接秘钥（由服务端自动处理）
//		if(!"".equals(this.mobilesecret)){
//			appSsoUrl.append("&secret=").append(this.mobilesecret);
//		}
//		log.info("Ecology统一待办数据推送：getAppUrl(): result="+appSsoUrl.toString());
//		return appSsoUrl.toString();
//	}
//
//	/**
//	 * 构造PC流程请求地址
//	 * @param id
//	 * @param requestid
//	 * @return
//	 */
//	private String getPcUrl(String id, String requestid) {
//		String tempurl = "";
//		String gotoPage = "workflow/request/ViewRequest.jsp";
//
//		String loginid = "";
//		String zzhid = "";
//		//判断是否是主账号
//		RecordSet rs1 = new RecordSet();
//		rs1.executeSql("select accounttype,belongto from hrmresource where id="+id);
//		if(rs1.next()){
//			String accounttype = Util.null2String(rs1.getString("accounttype"));
//			String belongto = Util.null2String(rs1.getString("belongto"));
//			if("1".equals(accounttype)){
//				zzhid = belongto;
//			}else{
//				zzhid = id;
//			}
//		}
//
//		try {
//			loginid = new ResourceComInfo().getLoginID(zzhid);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//
//		if(isUseCas()){
//			tempurl = "/" + gotoPage + "?fromofs=1&requestid=" + requestid +"&f_weaver_belongto_userid="+id+"&f_weaver_belongto_usertype=0";
//		}else{
//			try {
//				String para = "/" + gotoPage + "?fromofs=1&requestid=" + requestid +"&f_weaver_belongto_userid="+id+"&f_weaver_belongto_usertype=0" + "#" + loginid ;
//				String password=new BaseBean().getPropValue("AESpassword", "pwd");
//				if(password.equals("")){
//					password="1";
//				}
//				para = AES.encrypt(para,password);
//				tempurl = "/login/VerifySSoLogin.jsp?para=" + para;
//			} catch (Exception e) {
//				log.error(e);
//			}
//		}
//		log.info("Ecology统一待办数据推送：getPcUrl(id="+id+")"+", requestid="+requestid+"): result="+tempurl);
//		return tempurl;
//	}
//
//	/**
//	 * 将字段串做SHA-1加密处理
//	 * @param value
//	 * @return
//	 */
//	public static String hexSHA1(String value) {
//		try {
//
//			MessageDigest md = MessageDigest.getInstance("SHA-1");
//
//			md.update(value.getBytes("utf-8"));
//
//			byte[] digest = md.digest();
//
//			return byteToHexString(digest);
//
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
//
//	/**
//	 * 将byte转为16进制
//	 * @param bytes
//	 * @return
//	 */
//	public static String byteToHexString(byte[] bytes) {
//		return String.valueOf(Hex.encodeHex(bytes));
//	}
//
//	/**
//	 * 将流程请求数据post出去
//	 * @param posturl
//	 * @param param
//	 * @return
//	 */
//	private void postData2ofs(String posturl, String param) {
//         OutputStreamWriter out = null;
//         BufferedReader in = null;
//         String result = "";
//       try {
//           URL realUrl = new URL(serverurl+posturl);
//           // 打开和URL之间的连接
//           URLConnection conn = realUrl.openConnection();
//           // 设置通用的请求属性
//           conn.setRequestProperty("accept", "*/*");
//           conn.setRequestProperty("connection", "Keep-Alive");
//           conn.setRequestProperty("Content-Type", "application/json");
//           // 发送POST请求必须设置如下两行
//           conn.setDoOutput(true);
//           conn.setDoInput(true);
//           // 获取URLConnection对象对应的输出流
//           out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//           // 发送请求参数
//           out.write(param);
//           // flush输出流的缓冲
//           out.flush();
//           // 定义BufferedReader输入流来读取URL的响应
//           in = new BufferedReader(
//                   new InputStreamReader(conn.getInputStream(),"UTF-8"));
//           String line;
//           while ((line = in.readLine()) != null) {
//               result += line;
//           }
//       } catch (Exception e) {
//    	   log.error("发送 POST 请求出现异常！",e);
//           e.printStackTrace();
//       }
//       //使用finally块来关闭输出流、输入流
//       finally{
//           try{
//               if(out!=null){
//                   out.close();
//               }
//               if(in!=null){
//                   in.close();
//               }
//           }
//           catch(IOException ex){
//               ex.printStackTrace();
//           }
//       }
//    }
//
//	/**
//	 * 是否使用CAS集成
//	 *
//	 * @return
//	 */
//	private boolean isUseCas(){
//		boolean flag= new CasUtil().isUseCAS();
//		return flag;
//	}
//	private String getHrmstr( int userid){
//		RecordSet rsrtx = new RecordSet();
//		//TODO  增加转换规则
//		// 1 页面下拉框  integration/ofssend/OfsSendEdit.jsp(完成)
//		// 2 数据写入数据库 integration/ofssend/OfsSendOperation.jsp（完成） 0OAid 1OA登录账号 2工号 3身份证号 4电子邮件
//		// 3 取出配置规则 weaver.ofs.interfaces.SendRequestStatusDataImplForYY
//		// 4 调用转换方法 weaver.interfaces.HrmTransferDao
//		OfsSysInfoService ofsSysInfoService = new OfsSysInfoService();
//		rsrtx.executeQuery("select hrmtransrule from ofs_sendinfo where syscode = ?",syscode);
//		rsrtx.next();
//		String hrmtransrule = rsrtx.getString("hrmtransrule");
//		if(StringUtils.isBlank(hrmtransrule)){
//			hrmtransrule = "1";
//		}
//		log.error("hrmtransrule:"+hrmtransrule);
//		HrmTransferDao hrmTransferDao = new HrmTransferDao();
//		String hrmstr = hrmTransferDao.getHrmResourceIdByHrmTransRule(hrmtransrule, userid+"");
//		return hrmstr;
//	}
//}
