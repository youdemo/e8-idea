package jsd.financialcontrol.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	 * 获取数据 ----单个条件
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
	 * 获取系统日期
	 * @return
	 */
	
	public String getDateNow () {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
		
	}
	
	
 
	public static void main(String[] args) throws UnsupportedEncodingException {
		GetUtil  gu = new GetUtil();

		String str111 = "{\"controlRuleCode\": \"\",\"dimensions\": [{\"name\": \"组织\",\"valueType\": 0}, {\"name\": \"科目\",\"valueType\": 0}, {\"name\": \"年\",\"valueType\": 0}, {\"name\": \"期间\",\"valueType\": 0}, {\"name\": \"版本\",\"valueType\": 0}, {\"name\": \"场景\",\"valueType\": 0}, {\"name\": \"项目\",\"valueType\": 0}, {\"name\": \"综合\",\"valueType\": 0}],\"cells\": [{\"rowNum\": 1,\"members\": [\"杰士德集团董事长办公室\", \"公积金\", \"2020年\", \"5月\", \"编制第一版\", \"预算\", \"不分项目\", \"不分综合\"],\"value\": 6000},{\"rowNum\": 2,\"members\": [\"杰士德集团董事长办公室\", \"公积金\", \"2020年\", \"5月\", \"编制第一版\", \"预算\", \"不分项目\", \"不分综合\"],\"value\": 6000}]}";
		System.out.println(URLEncoder.encode(str111,"utf-8"));
		str111 = "{\"controlRuleCode\": \"\",\"dimensions\": [{\"name\": \"组织\",\"valueType\": 0}, {\"name\": \"科目\",\"valueType\": 0}, {\"name\": \"年\",\"valueType\": 0}, {\"name\": \"期间\",\"valueType\": 0}, {\"name\": \"版本\",\"valueType\": 0}, {\"name\": \"场景\",\"valueType\": 0}, {\"name\": \"项目\",\"valueType\": 0}, {\"name\": \"综合\",\"valueType\": 0}],\"cells\": [{\"rowNum\": 1,\"members\": [\"杰士德集团董事长办公室\", \"公积金\", \"2020年\", \"5月\", \"编制第一版\", \"预算\", \"不分项目\", \"不分综合\"],\"value\": 6000}]}";
		System.out.println(URLEncoder.encode(str111,"utf-8"));
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
