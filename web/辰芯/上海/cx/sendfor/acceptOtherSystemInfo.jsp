<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="weaver.hrm.User"%>
<%@ page import="weaver.general.Util" %>
<%@ page import="java.util.Date"%>
<%@ page import="javax.crypto.Cipher"%>
<%@ page import="javax.crypto.spec.SecretKeySpec"%>
<%@ page import="sun.misc.BASE64Decoder"%>
<%@ page import="sun.misc.BASE64Encoder"%>
<%@ page import="java.net.URLDecoder" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
	
<%!
	public String encrypt(String sSrc) throws Exception {
		String sKey = "7227730012772198";   //  加密
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
		String sKey = "7227730012772198";
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
	
	String x_login = URLDecoder.decode(Util.null2String(request.getParameter("x_login")),"UTF-8") ;
	
	String nsString = decrypt(decrypt(x_login));   //  2次解密
	if(nsString == null) nsString = "";
	
	String arr[] = nsString.split("@");
	
	if(arr.length !=2){
		%>
			<script language="javascript">
				alert("系统验证信息失败!");
				window.opener=null;
				window.open('','_self');
				window.close();
			</script>
		<%
		return;
	}
	String f_login = arr[0];
	
	String sql = "" ;
	if( x_login.length() > 0){
		sql = "select * from hrmresource where workcode = '" + f_login + "'";
		String loginid = "";
		rs.executeSql(sql);
		int xxxid = 0;
		if(rs.next()){
			xxxid = rs.getInt("id");
			loginid = Util.null2String(rs.getString("loginid"));
		}
		
		if(xxxid < 1 ) {
		%>
		<script language="javascript">
			alert("原系统无账号，将关闭页面!");
			window.opener=null;
			window.open('','_self');
			window.close();
		</script>
		<%
		return;
	}
		
		User user1 = new User();
		user1 = User.getUser(xxxid, 0);

		request.getSession(true).setMaxInactiveInterval(60 * 60 * 24);
		request.getSession(true).setAttribute("curloginid",loginid);
		request.getSession(true).setAttribute("SESSION_CURRENT_SKIN","default");
		request.getSession(true).setAttribute("SESSION_CURRENT_THEME","ecology8");
		request.getSession(true).setAttribute("SESSION_TEMP_CURRENT_THEME","ecology8");
		request.getSession(true).setAttribute("SESSION_CURRENT_SKIN","default");
		request.getSession(true).setAttribute("weaver_user@bean", user1);
		request.getSession(true).setAttribute("browser_isie", "true");

				String toRedirect = "/wui/main.jsp";
		response.sendRedirect(toRedirect);
	}
%>