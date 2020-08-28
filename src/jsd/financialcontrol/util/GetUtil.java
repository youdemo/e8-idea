package jsd.financialcontrol.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.Util;


/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月17日 下午3:02:32 
* @version 1.0 
*/
public class GetUtil {
	
	/**1.	secretKey参数, 其中secret是固定值: B104-F037092FE0DB
	 * MD5加密
	 * @param userName 登录名
	 * @param secret 密钥
	 * @param dataTime 时间戳 1550475139000 毫秒值
	 * @return
	 * @throws Exception
	 */
	public  String compute(String userName, String secret, String dataTime) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		String temp = userName + secret + dataTime;
		byte[] resbyte = md.digest(temp.getBytes("UTF-8"));
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < resbyte.length; i++) {
			int val = ((int) resbyte[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
	/**
	 * 获取数据 
	 * @param tableName
	 * @param fieldName
	 * @param wherekey
	 * @param strparm
	 * @return
	 */
	public String getFieldVal(String tableName,String fieldName,String wherekey,String strparm){
		String sql = "select * from "+ tableName + " where  "+wherekey+" = '" + strparm + "'";
		RecordSet rs = new RecordSet();
		String result = "";
		rs.executeSql(sql);
	    if(rs.next()){
	    	result = Util.null2String(rs.getString(fieldName));
	    }
	    return result;
	}
	/**
	 * 获取科目数数据 
	 * @param tableName
	 * @param fieldName
	 * @param wherekey
	 * @param strparm
	 * @return
	 */
	public String getKMFieldVal(String tableName,String fieldName,String wherekey,String strparm){
		String sql = "select * from "+ tableName + " where  "+wherekey+" = '" + strparm + "'";
		RecordSet rs = new RecordSet();
		String result = "";
		rs.executeSql(sql);
	    if(rs.next()){
	    	result = Util.null2String(rs.getString(fieldName));
	    }
	    if(result.lastIndexOf("-")<0) {
	    	return result;
	    }else {
	    	return result.substring(result.lastIndexOf("-")+1, result.length());
	    }
	    
	    
	    
	    
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
	 * 获取系统日期
	 * @return
	 */
	
	public String getDateNow () {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
		
	}
	
	
 
	public static void main(String[] args) {
		GetUtil  gu = new GetUtil();
		long aa = new Date().getTime();  
		
		
		System.out.println(aa);
		try {
			String str = gu.compute("jstepm", "B104-F037092FE0DB", aa+"");
			System.out.println(str);
			System.out.println(URLEncoder.encode(URLEncoder.encode("jstepm","utf-8"),"utf-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	

}
