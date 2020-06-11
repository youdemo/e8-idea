<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Date"%>
<%@ page import="javax.crypto.Cipher"%>
<%@ page import="javax.crypto.spec.SecretKeySpec"%>
<%@ page import="sun.misc.BASE64Decoder"%>
<%@ page import="sun.misc.BASE64Encoder"%>
<%@ page import="java.net.URLEncoder"%>
	
<%@ include file="/systeminfo/init_wev8.jsp" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
	
<%!
	public String encrypt(String sSrc) throws Exception {
		String sKey = "7227730012772198";  //  加密
		 if (sKey == null) {
			 System.out.print("Key为空null");
			 return null;
		 }
		 // 判断Key是否为16位
		 if (sKey.length() != 16) {
			 System.out.print("Key长度不是16位");
			 return null;
		 }
		 byte[] raw = sKey.getBytes("UTF-8");
		 SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		 Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
		 cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		 byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
		 return (new BASE64Encoder()).encodeBuffer(encrypted);
	}
%>
<%!
	public String decrypt(String sSrc) throws Exception {
		String sKey = "7227730012772198";  //  加密
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			
			byte[] encrypted1 = (new BASE64Decoder()).decodeBuffer(sSrc);  
			
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original,"UTF-8");
			return originalString;
		}catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
%>
	
<% 
	String userid = ""+user.getUID();
	String serverUrl = "http://172.23.9.51:8080";   // 需要跳转服务域名(或IP)及端口的地址
	
	if("1".equals(userid)){
		out.println("系统管理员不支持查看!");
		return;
	}
	
	String userCode = "";
	String sql = "select * from hrmresource where id = '" + userid + "'" ;
	rs.executeSql(sql);
	if(rs.next()){
		userCode = Util.null2String(rs.getString("workcode"));
	}
	// 加密   2次加密
	String userXf = encrypt(userCode + "@" + userid + "#" + new Date().getTime());
	userXf = encrypt(userXf);
	
	//  封装发送的URL 防止特殊字段转义
	String ulXf = "";
	try{
		ulXf = URLEncoder.encode(URLEncoder.encode(userXf,"UTF-8"),"UTF-8");
	}catch(Exception e){
		e.printStackTrace();
	}
	
	// 发送对方接受页面
	String url_str = serverUrl+"/cx/sendfor/acceptOtherSystemInfo.jsp?x_login=" + ulXf;
	
	response.sendRedirect(url_str);
	
%>