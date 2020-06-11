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
 * @version 创建时间：2019-5-5 上午2:55:15
 * 费用分摊流程
 */
public class DoFYFTImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> DoFYFT(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		Map<String, String> returnMap = new HashMap<String, String>();
		CreateCertificate  ccf=new CreateCertificate();
		String sqr="";
		String sqrq="";
		String bb="";
		String djbh="";//单据编号
		String ftlx = "";//科目编码
		String mainId="";
		String wbje="0";
		String zzje="0";
		String hl="";
		String hxsm="";
		String OracleBatchName = "";
		String 	zwrq ="";//账务日期
		InsertUtil iu = new InsertUtil();
		String sql = "select OracleBatchName,id,(select lastname from hrmresource where id=sqr) as sqr,sqrq,djbh,ftlr,ftlx,zwrq from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql------"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			sqr = Util.null2String(rs.getString("sqr"));
			zwrq =  Util.null2String(rs.getString("zwrq"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			bb = geCurrencyCode("0");
			OracleBatchName = Util.null2String(rs.getString("OracleBatchName"));
			djbh = Util.null2String(rs.getString("djbh"));
			hxsm = Util.null2String(rs.getString("ftlr")).replace("<br/>", "").replace("<br>", "").replace("</br>", "");
			ftlx = Util.null2String(rs.getString("ftlx"));
		}
		if("".equals(hxsm)){
			hxsm = " ";
		}
		if("".equals(sqr)){
			sqr = " ";
		}
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+zwrq+"','yyyy/mm/dd')");
		mapStr.put("CURRENCY_CODE", bb);
		mapStr.put("DATE_CREATED","to_date('"+zwrq+"','yyyy/mm/dd')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("REFERENCE1", OracleBatchName+"-FTOA"+pch);
		mapStr.put("REFERENCE2", " ");
		mapStr.put("REFERENCE4", "OA"+pch);
		mapStr.put("REFERENCE5", " ");
		mapStr.put("REFERENCE10", sqr);
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
		String xmbm_dt1 ="";//项目代码  Project	
		String dqm_dt1 ="";//地区码  dqm
		String bmdm_dt1 ="";//部门代码 	bmm
		String ftje_dt1 = "";//金额 ftjebhs
		String dtid="";
		String money="0";
		sql="select * from "+tableName+"_dt1 where mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			dtid = Util.null2String(rs.getString("id"));
			xmbm_dt1 = Util.null2String(rs.getString("Project"));
			dqm_dt1 = Util.null2String(rs.getString("dqm"));
			bmdm_dt1 = Util.null2String(rs.getString("bmm"));
			ftje_dt1 = Util.null2String(rs.getString("ftjebhs")).replaceAll(",", "");
			//hxnr_dt1 = Util.null2String(rs.getString("hxnr"));
			money= iu.add(money, ftje_dt1);
			if("".equals(xmbm_dt1)){
				xmbm_dt1 = "0000";
			}
			if("".equals(dqm_dt1)){
				dqm_dt1 = "000";
			}
			if("".equals(bmdm_dt1)){
				bmdm_dt1 = "0000";
			}
			mapStr.put("interface_seq", ccf.getInterfaceSeq());
			if("".equals(ftlx)){
				ftlx = " ";
			}
			mapStr.put("SEGMENT5", ftlx);
			mapStr.put("ENTERED_DR", ftje_dt1);
			mapStr.put("ACCOUNTED_DR", ftje_dt1);
			mapStr.put("SEGMENT2", xmbm_dt1);//项目码
			mapStr.put("SEGMENT3", dqm_dt1);//地区码
			mapStr.put("SEGMENT4", bmdm_dt1);//部门码
			mapStr.put("ENTERED_CR", null);
			mapStr.put("ACCOUNTED_CR", null);
			mapStr.put("REFERENCE2", sqr+" "+hxsm);
			mapStr.put("REFERENCE5", sqr+" "+hxsm);
			mapStr.put("REFERENCE10", sqr+" "+hxsm);
			mapStr.put("REFERENCE30", ccf.interfaceseq+"");
			ccf.interfaceseq= ccf.interfaceseq+10;
			iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			
		}
		wbje=money;
		if("USD".equals(bb)){
			zzje= iu.mul(money, hl);
		}else{
			zzje= money;
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
		sql = "update "+tableName +" set yfk='1' where requestid = '"+requestId+"'";
		rs.executeSql(sql);
		returnMap.put("wb", wbje);
		returnMap.put("rmb", zzje);
		Map<String, String> mapStrseq = new HashMap<String, String>();
		mapStrseq.put("BATCH_NAME",  OracleBatchName+"-OA"+pch);
		mapStrseq.put("JOURNAL_NAME", "OA"+pch);			
		iu.insertzj(mapStrseq, "ZJ_JOURNAL_SEQ");
		
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
