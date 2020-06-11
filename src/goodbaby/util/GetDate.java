package goodbaby.util;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDate {

	public String getNowDateYear(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return year;
	}
	public String getDay(String ks,String js){
		double day =1000*60*60*8;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String days = "";
		if(ks.length()<16||js.length()<16){
			return "0.00";
		}
		try {
			Date date = format1.parse(ks);
			Date date1 = format1.parse(js);
			Double sec = (double) Math.abs(date.getTime()-date1.getTime());
			days = new DecimalFormat("0.00").format(sec/day);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		} 
		return days;
		
	}
	
	public String getHour(String ks,String js){
		double hour = 1000*60*60;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String hours = "";
		if(ks.length()<16||js.length()<16){
			return "0.00";
		}
		try {
			Date date = format1.parse(ks);
			Date date1 = format1.parse(js);
			Double sec = (double) Math.abs(date.getTime()-date1.getTime());
			hours = new DecimalFormat("0.00").format(sec/hour);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return hours; 
		
	}
	
	
	
	
	public static void main(String[] args) {
		GetDate  g=new GetDate(); 
		System.out.println(g.getNowDateYear()+"-12-31");
		String a="2018-01-01";
		String b ="11:20";
		String c ="12:20";
		String ab=a+" "+b;
		String ac=a+" "+c;
		System.out.println(g.getHour("", ""));
		
	}

}
