package tj.util;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
/**
 * 
 * @author 张瑞坤
 */
public class GetUtil {
	

	public String getNowDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
		
	}	
	public String getNowDatet(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
	public String getFieldVal(String tableName,String fieldName,String wherekey,String workcode){
		String sql = "select * from "+ tableName + " where  "+wherekey+" = '" + workcode + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getK3FieldVal(String tableName,String fieldName,String wherekey,String workcode){
		RecordSetDataSource res = new RecordSetDataSource("k3_test");
		BaseBean log = new BaseBean();
		String sql = "select * from "+ tableName + " where  "+wherekey+" = '" + workcode + "'";
		log.writeLog("fieldName--"+fieldName+"---k3sql-----"+sql);
		res.executeSql(sql);
	    if(res.next()){
	    	String name = Util.null2String(res.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getDepCode(String userid){
		String sql = "select d.departmentcode from hrmresource h join hrmdepartment d on d.id =h.departmentid where  h.id = '" + userid + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("departmentcode"));
	    	return name;
	    }
	    return "0";
	}
	public String getDepName(String userid){
		String sql = "select d.departmentname from hrmresource h join hrmdepartment d on d.id =h.departmentid where  h.id = '" + userid + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("departmentname"));
	    	return name;
	    }
	    return "0";
	}	
	public String getSelectVal(String fieldid,String selectvalue){
		String sql = "select * from workflow_selectitem where  fieldid = '" + fieldid + "' and selectvalue='"+selectvalue+"'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("selectname"));
	    	return name;
	    }
	    return "";
	}
	/**
	 * 保留4位小数
	 * @param str
	 * @return
	 */
	public String getFour(String str){
		if(str.length()<1){
			str = "0.0000";
		}
		double std = Double.valueOf(str);
		DecimalFormat df = new DecimalFormat("#.####");
		String output = String.valueOf(df.format(std));
//		DecimalFormat myFormat = new DecimalFormat(str);
//        String output = myFormat.format(2);
        return output;
		
	}
	public String getInt(String str){
		if(str.length()<1){
			str = "0";
		}
		double std = Double.valueOf(str);
		DecimalFormat df = new DecimalFormat("#");
		return String.valueOf(df.format(std));
		
	}
	public static void main(String[] args) {
		GetUtil  g=new GetUtil();
	
//		System.out.println(g.getFour("1.000000000"));
//		System.out.println(g.getFour("1.000000009"));
//		System.out.println(g.getFour("44.247800"));
//		System.out.println(g.getFour("10.247800"));
		System.out.println(g.getFour("0.258800"));
//		System.out.println(g.getFour("1.000999999"));
//		System.out.println(g.getInt("1.999999999"));
//		
	}

}
