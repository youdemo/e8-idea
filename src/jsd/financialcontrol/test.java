package jsd.financialcontrol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月21日 上午10:06:29 
* @version 1.0 
*/
public class test {
public static void main(String[] args) {
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	String nowdate = sf.format(new Date());
	String year = nowdate.substring(0,4);
	String month = nowdate.substring(5,7);
	if("0".equals(month.substring(0,1))){
		month = month.substring(1);
	}
	System.out.println(year);
	System.out.println(month);
	String  str = "[{\"name\":\"组织\",\"valueType\":0},{\"name\":\"科目\",\"valueType\":0}," + 
			"{\"name\":\"年\",\"valueType\":0},{\"name\":\"期间\",\"valueType\":0},{\"name\":\"版本\",\"valueType\":0},"
			+ "{\"name\":\"场景\",\"valueType\":0},{\"name\":\"项目\",\"valueType\":0},{\"name\":\"综合\",\"valueType\":0}]";
	String st ="[{\"rowNum\":1,\"members\":[\"1\",\"1\",\"1年\",\"1月\",\"执行控制版\",\"预算\",\"1\",\"不分综合\"],\"value\":1}]";
	JSONObject json = new JSONObject();
	JSONArray jsonarr1 = new JSONArray();
	JSONArray jsonarr2 = new JSONArray();
	try {
		jsonarr1 = new JSONArray(str);
		jsonarr2 = new JSONArray(st);
	} catch (JSONException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
System.out.println(jsonarr1.toString());
	try {
		json.put("controlRuleCode", "");
		json.put("dimensions", jsonarr1);
		json.put("cells", jsonarr2);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	System.out.println(json.toString());
	
	
	
}
}
