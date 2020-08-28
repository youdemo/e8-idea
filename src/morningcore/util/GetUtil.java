package morningcore.util;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.hash.BloomFilter;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-1-10 上午10:32:18
 * 类说明
 */
public class GetUtil {
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
	
	public String getDepVal(String tableName,String fieldName,String filed1,String filed2){
		String sql = "select * from "+ tableName + " where "+filed1+" in " + filed2;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getNowdateSM(){
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(da);
		
	}
	public String getNowdate(){
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(da);
		
	}
	public String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr.trim(); // 返回文本字符串
	}
	//获取流程
	public String getWorkflowid(String flag){
		String workflowid= Util.null2o(weaver.file.Prop.getPropValue("workflowids", flag));
		return workflowid;
	}
	/**
	 * 获取年度id
	 * @param nf 年份
	 * @param yf 月份
	 * @return
	 */
	public String getNdqj (String nf,String yf){
		RecordSet res = new RecordSet();
		String yfnew = yf.substring(0, yf.length()-1);
		if(yfnew.length()<2){
			yfnew = "0"+yfnew;
		}
		String nyr = nf+"-"+yfnew+"-01";
		String sql = "select id from fnayearsperiods where fnayear = '"+nf+"' and status = '1' and startdate <= '"+nyr+"' and '"+nyr+"' <= enddate";
		res.executeSql(sql);
		if(res.next()){
			return res.getString("id");
		}
		return "0";
		
	}
	/**
	 * 检查确认是否已存在该预算，如果存在返回对应id 
	 * @param deptid
	 * @param fnayear
	 * @return
	 */
	public String checkIfExist(String deptid,String  fnayear){
		RecordSet res = new RecordSet();
		String str = "select max(id) as mid from fnabudgetinfo where budgetorganizationid = '"+deptid+"' and organizationtype='2' and budgetperiods = '"+fnayear+"' and status='1'";
		res.executeSql(str);
		if(res.next()){
			return res.getString("mid");
		}
		return "0";
		
	}
	/**
	 * 获得版本
	 * @param deptid
	 * @param fnayear
	 * @return
	 */
	public String getViso(String deptid,String  fnayear){
		RecordSet res = new RecordSet();
		BaseBean log =new BaseBean();
		String str = "select isnull(max(revision),0)+1 as mr from fnabudgetinfo where budgetorganizationid = '"+deptid+"' and organizationtype='2' and budgetperiods = '"+fnayear+"'";
		log.writeLog("版本获取----"+str);
		res.executeSql(str);
		if(res.next()){
			return res.getString("mr");
		}
		return "0";
		
	}
	/**
     * 加法运算
     * @param m1
     * @param m2
     * @return
     */
    public  double addDouble(String m1, String m2) {
    	if(m1.length()<1){
    		m1 = "0.00";
    	}
    	if(m2.length()<1){
    		m2 = "0.00";
    	}
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        return p1.add(p2).doubleValue();
    }
	public String getBudgetperiodslist(String kmid,String yearid,String nowyear,String startdate,String enddate){
		RecordSet res = new RecordSet();
		String feeperiod = "";//预算周期	
		String Periodsid = "";// 预算期间表   期间id
		String  str = "select feeperiod from fnabudgetfeetype where id = '"+kmid+"'";
		res.executeSql(str);
		if(res.next()){
			feeperiod = Util.null2String(res.getString("feeperiod"));
		}
		str = "select Periodsid from fnayearsperiodslist where fnayearid = '"+yearid+"' and fnayear = '"+nowyear+"' and startdate<= '"+startdate+"' and enddate>= '"+enddate+"' ";
		res.executeSql(str);
		if(res.next()){
			Periodsid = Util.null2String(res.getString("Periodsid"));
		}
		if("1".equals(feeperiod)){
			return Periodsid;
//			mapinfo.put("budgetperiodslist", Periodsid);//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
		}else if("2".equals(feeperiod)){
			if(Util.getIntValue(Periodsid)>=1&&Util.getIntValue(Periodsid)<=3){
				return "1";
//				mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}else if(Util.getIntValue(Periodsid)>=4&&Util.getIntValue(Periodsid)<=6){
				return "2";
//				mapinfo.put("budgetperiodslist", "2");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}else if(Util.getIntValue(Periodsid)>=7&&Util.getIntValue(Periodsid)<=9){
				return "3";
//				mapinfo.put("budgetperiodslist", "3");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}else if(Util.getIntValue(Periodsid)>=10&&Util.getIntValue(Periodsid)<=12){
				return "4";
//				mapinfo.put("budgetperiodslist", "4");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}
		}else if("3".equals(feeperiod)){
			if(Util.getIntValue(Periodsid)>=1&&Util.getIntValue(Periodsid)<=6){
				return "1";
//				mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}else if(Util.getIntValue(Periodsid)>=7&&Util.getIntValue(Periodsid)<=12){
				return "2";
//				mapinfo.put("budgetperiodslist", "2");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
			}
		}else if("4".equals(feeperiod)){
			return "1";
//			mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
		}
		return "1";
		
	}
	/**
	 * 获取对应的系统科目id
	 * @param jmkm 建模科目id
	 * @return
	 */
	public String getXtkm(String jmkm) {
		RecordSet rs = new RecordSet();
		String sql = "select f.id  from uf_yskmsj uf join fnabudgetfeetype  f on f.codeName = uf.bh  where uf.id = '"+jmkm+"'  ";
		rs.executeSql(sql);
		if(rs.next()) {
			String id = rs.getString("id");
			return  id;
		}
		return "0";
		
	}
	
	public static void main(String[] args) {
		GetUtil a= new GetUtil();
		System.out.println(a.getNowdate());
		System.out.println(a.addDouble("-1000", "0"));
	}
	

}
