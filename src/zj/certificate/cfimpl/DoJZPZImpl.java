package zj.certificate.cfimpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.CreateCertificate;
import zj.certificate.InsertUtil;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-5 上午4:00:51
 * 记账凭证
 */
public class DoJZPZImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> DoJZPZ(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		 log.writeLog("凭证写入--=-------");
		String sqr="";
		String txrq="";//填写日期
		String djbh="";
		String biz="";
		String jfje="0";
		String dfje = "0";
		String mainId="";
		String wb="";
		String OracleBatchName = "";
		String ACCOUNTED_DR="";
		String yqzzrq = "";//要求转账日期
		String ENTERED_DR="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=sqr) as sqr,txrq,djbh,OracleBatchName,jfje,dfje,yqzzrq from "+tableName+" where requestId="+requestId;
//		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			sqr = Util.null2String(rs.getString("sqr"));
			txrq = Util.null2String(rs.getString("txrq"));
			yqzzrq = Util.null2String(rs.getString("yqzzrq")); 
			djbh = Util.null2String(rs.getString("djbh"));
			OracleBatchName = Util.null2String(rs.getString("OracleBatchName"));
			biz = geCurrencyCode("0");
			jfje =Util.null2String(rs.getString("jfje")).replaceAll(",", "");
			dfje =Util.null2String(rs.getString("dfje")).replaceAll(",", "");
		}
		
		if("".equals(wb)){
			wb="0";
		}
		if("".equals(jfje)){
			jfje="0";
		}
		if("".equals(djbh)){
			djbh = " ";
		}
		if("".equals(sqr)){
			sqr = " ";
		}
		returnMap.put("wb", jfje);
		returnMap.put("rmb", dfje);
		
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+yqzzrq+"','yyyy/mm/dd')");
		mapStr.put("CURRENCY_CODE", biz);
		mapStr.put("DATE_CREATED","to_date('"+yqzzrq+"','yyyy/mm/dd')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("REFERENCE1", OracleBatchName+"-OA"+pch);
		mapStr.put("REFERENCE4", "OA"+pch);
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
		String Project ="";//项目代码  Project	
		String dqm_dt1 ="";//地区码  dqm
		String bmdm_dt1 ="";//部门代码 	bmm
		String pzzy = "";//摘要
		String kjkmdm = "";//会计科目代码
		String jfje_dt = "";//
		String dfje_dt = "";//
		String dtid="";
		String cutmoney="0";
		sql="select  top 1 * from "+tableName+"_dt1 where  mainid="+mainId +" order by id  asc ";
		rs.executeSql(sql);
		String mxpzsm = "";//明细凭证说明
		if(rs.next()){
			mxpzsm =Util.null2String(rs.getString("pzzy"));
		}
		sql="select * from "+tableName+"_dt1 where  mainid="+mainId;
		log.writeLog("sql----"+sql);
		rs.executeSql(sql);
		while(rs.next()){
			dtid = Util.null2String(rs.getString("id"));
			Project = Util.null2String(rs.getString("Project"));
			dqm_dt1 = Util.null2String(rs.getString("dqm"));
			dfje_dt = Util.null2String(rs.getString("dfje")).replaceAll(",", "");
			jfje_dt = Util.null2String(rs.getString("jfje")).replaceAll(",", "");
			bmdm_dt1 = Util.null2String(rs.getString("bmm"));
			pzzy = Util.null2String(rs.getString("pzzy"));
			kjkmdm = Util.null2String(rs.getString("kjkmdm"));
			if("".equals(Project)){
				Project="0000";
			}
			if("".equals(bmdm_dt1)){
				bmdm_dt1="0000";
			}
			if("".equals(dqm_dt1)){
				dqm_dt1="000";
			}
//			if(dfje_dt.length()<1){
//				dfje_dt = "0.00";
//			}
//			if(jfje_dt.length()<1){
//				jfje_dt = "0.00";
//			}
			mapStr.put("interface_seq", ccf.getInterfaceSeq());
			mapStr.put("SEGMENT2", Project);//项目码
			mapStr.put("SEGMENT3", dqm_dt1);//地区码
			mapStr.put("SEGMENT4", bmdm_dt1);//部门码
			mapStr.put("SEGMENT5", kjkmdm);//科目
			//20190803
			if(dfje_dt.length()<1){
				dfje_dt = null;
			}
			if(jfje_dt.length()<1){
				jfje_dt = null;
			}
			mapStr.put("ENTERED_DR", jfje_dt);
			mapStr.put("ACCOUNTED_DR", jfje_dt);
			mapStr.put("ENTERED_CR", dfje_dt);
			mapStr.put("ACCOUNTED_CR", dfje_dt);
			
			//
//			if(Double.valueOf(jfje_dt)>0){
//				mapStr.put("ENTERED_DR", jfje_dt);
//				mapStr.put("ACCOUNTED_DR", jfje_dt);
//				mapStr.put("ENTERED_CR", null);
//				mapStr.put("ACCOUNTED_CR", null);
//			}else{
//				mapStr.put("ENTERED_DR", null);
//				mapStr.put("ACCOUNTED_DR", null);
//				mapStr.put("ENTERED_CR", dfje_dt);
//				mapStr.put("ACCOUNTED_CR", dfje_dt);
//			}
			mapStr.put("REFERENCE2", mxpzsm);
			mapStr.put("REFERENCE5", mxpzsm);
			mapStr.put("REFERENCE10",pzzy);
			mapStr.put("REFERENCE30", ccf.interfaceseq+"");
			 ccf.interfaceseq= ccf.interfaceseq+10;
			iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			
		}
		int nextSeq= 0;
		String str = "select isnull(MAX(fkseq),0)+1 as next from request_fk_record";
		rs.executeSql(str);
		if (rs.next()) {
			nextSeq = rs.getInt("next");
		}
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		sql = "insert into request_fk_record(requestid,seq,fkseq,fkrq,fksj,fklx) values('"
				+ requestId + "','1','" + nextSeq + "','"+nowDate+"','"+nowTime+"','1')";
		rs.executeSql(sql);
//		log.writeLog("sql2----"+sql);
		sql = "update "+tableName +" set flag = '1' where requestid = '"+requestId+"'";
		rs.executeSql(sql);
//		log.writeLog("sql3----"+sql);
		return returnMap;
	}
    
    public String geCurrencyCode(String currency){
    	String code="";
    	if("0".equals(currency)){
    		code="RMB";
    	}else{
    		code="USD";
    	}
    	return code;
    }
    
    public String getMoney(String money){
    	String result="";
    	if("".equals(money)){
    		result = null;
		}else{
			result = money;
		}
    	return result;
    }
    
    
    
}
