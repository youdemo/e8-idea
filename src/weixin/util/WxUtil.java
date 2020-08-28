package weixin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class WxUtil {
	
	
	public String getFieldVal(String tableName,String fieldName,String id){
		String sql = "select * from "+ tableName + " where id = '" + id + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getNowDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println("现在时间是：" + new Date());
		Date date =new Date();
		String dd =form.format(date); 
		//System.out.println(dd);
		return dd;
	}
	public String getSiteName(String value){
		//String sql = "select selectvalue ,selectname from workflow_selectitem where fieldid ='8044' and selectvalue = '"+value+"'";
		
		if(value.equals("0")){ 
			return "MFC-GX";
		}else if(value.equals("1")){
			
			return "MFC-HZ";
		}else if(value.equals("2")){
			
			return "MUTTO";
		}else if(value.equals("3")){
			
			return "MYC";
			
		}else if(value.equals("4")){
			 
			return "EMS";
		}else if(value.equals("6")){
			
			return "MSG";
		}else if(value.equals("7")){ 
			return "MFI";
		}
		 return "";
	}
	public String getSelect(String fieldid,String value){
		String sql = "select selectvalue ,selectname from workflow_selectitem where fieldid ='"+fieldid+"' and selectvalue='"+value+"' ";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("selectname"));
	    	return name;
	    }
		return "";
	}
	public String getObj(String tableName,String fieldName,String depat1 ,String cbzxfour){
		String sql = "select * from "+ tableName + " where depat1 in (select a.depat1 from xiangmu a where a.id='"+depat1+"') and items like '%"+cbzxfour+"%'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getVro1(String VR01){
		String sql = "select a.depat1 as depat from xiangmu a where id='"+VR01+"'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql); 
	    if(rs.next()){
	    	String VR = Util.null2String(rs.getString("depat"));
	    	return VR;
	    }
	    return "";
	}
	public static void main(String[] args) {
		WxUtil ss=new WxUtil();
		ss.getNowDate();
		String aa="0123456";
		System.out.println(aa.substring(0, 4));
	}
	

}
