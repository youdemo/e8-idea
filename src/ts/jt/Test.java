package ts.jt;

import weaver.general.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Test {
	public static void main(String args[]) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate =sf.format(new Date());
		String startDate = TimeUtil.dateAdd(endDate.substring(0, 7)+"-01", -1).substring(0, 7)+"-01";
		endDate = TimeUtil.dateAdd(endDate, -1);
		System.out.println(startDate);
		System.out.println(endDate);
		String time = "12:00:01";
		System.out.println(time.substring(0,5));


	}
	
	// TODO Auto-generated method stub

	
 
}
