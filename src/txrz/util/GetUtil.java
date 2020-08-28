package txrz.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;



/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-2-20 下午5:16:48
 * 类说明
 */

public class GetUtil {
	
	 public  double addDouble(String m1, String m2) {
		 BigDecimal p1 = new BigDecimal(m1);
		 BigDecimal p2 = new BigDecimal(m2);
		 return p1.add(p2).doubleValue();
	}
	public  double subDouble(String m1, String m2) {
		BigDecimal p1 = new BigDecimal(m1);
		BigDecimal p2 = new BigDecimal(m2);
		return p1.subtract(p2).doubleValue();
	
	}
	public String getNowTime(){
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String a = sf.format(da);
		return a;
		
	}
	public String getNowTime1(){
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String a = sf.format(da);
		return a;
		
	}
	public String getThreeLS(int lsh){//流水
		String lshnew = "";
		if(lsh<10){
			lshnew = "00" + lsh;
		}else if(lsh<100){
			lshnew = "0" + lsh;
		}
		return lshnew;
		
	}
	/**
	 * 
	 * @param tableName  表
	 * @param fieldName  目标字段
	 * @param filed1     条件字段
	 * @param filed2  条件字段值
	 * @return
	 */
	public String getFieldVal(String tableName,String fieldName,String filed1,String filed2){
		String sql = "select * from "+ tableName + " where "+filed1+" = '" + filed2 + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	
	public boolean checkRid(String rid) {
		RecordSet rs = new RecordSet();
		String sql = "select count(*) as cn from uf_xpzdj where rid = '"+rid+"'";
		rs.executeSql(sql);
		int con = 0;
	    if(rs.next()){
	    	con = rs.getInt("cn");
	    }
		if(con>0) {
			return true;
		}
		return false;
	}
	public boolean checkMxRid(String mxid,String type) {
		RecordSet rs = new RecordSet();
		String sql = "select count(*) as cn from uf_xpzdj_dt"+type+" where glmxid = '"+mxid+"'";
		rs.executeSql(sql);
		int con = 0;
	    if(rs.next()){
	    	con = rs.getInt("cn");
	    }
		if(con>0) {
			return true;
		}
		return false;
	}
	public String getSelectValue(String type) {
		String rseult = "";
		if("0".equals(type)) {
			rseult = "电子发票";
		}else if("1".equals(type)) {
			rseult = "增值税专用发票";
		}else if("2".equals(type)) {
			rseult = "其他";
		}else if("3".equals(type)) {
			rseult = "航空运输电子客票行程单";
		}else if("4".equals(type)) {
			rseult = "铁路车票";
		}else if("5".equals(type)) {
			rseult = "公路、水路等客票";
		}else if("6".equals(type)) {
			rseult = "旅客运输类增值税电子普票";
		}
		return rseult;
		
	}
	
	public static void main(String[] args) {
		GetUtil gu = new GetUtil();
		System.out.println(gu.getNowTime1());
	}
	
	
}
