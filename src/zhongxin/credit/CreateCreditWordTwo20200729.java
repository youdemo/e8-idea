package zhongxin.credit;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年3月18日 上午11:03:07 
* @version 1.0 
*/
public class CreateCreditWordTwo20200729 implements Action {
	
	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String rid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName ="";
		String sql = " select tablename From Workflow_bill Where id in (Select formid From workflow_base Where id= (select workflowid from workflow_requestbase where requestid = '"+rid+"' ))";
		rs.executeSql(sql);
		if(rs.next()) {
			tableName = rs.getString("tablename");
		}
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		log.writeLog("开始----------------------------");
		// 主表同步字段
		String sqrq = "";//申请日期---主表字段
		String EXPIRYDATE = "";//EXPIRY DATE---主表字段
		String EXPIRYPLACE = "";//EXPIRY PLACE---主表字段
		String SHIPMENTFROM = "";//SHIPMENT FROM---主表字段
		String FORTRANSPORTATIONTO = "";//FOR TRANSPORTATION TO---主表字段
		String LATESTSHIPMENTDATE = "";//LATEST SHIPMENT DATE---主表字段
		String ANYBANK = "";//ANY BANK---主表字段
		String ISSUINGBANK = "";//ISSUING BANK---主表字段
		String NEGOTIATION = "";//NEGOTIATION---主表字段
		String ACCEPTANCE = "";//ACCEPTANCE---主表字段
		String PAYMENT = "";//PAYMENT---主表字段
		String DEFERRED = "";//DEFERRED---主表字段
		String PAYMENT1 = "";//PAYMENT文本---主表字段
		String DAYSAFTER = "";//DAYS AFTER---主表字段
		String SIGHT = "";//SIGHT---主表字段
		String DAYSAFTER1 = "";//DAYS AFTER---主表字段
		String DRAFTSAGAINSTACCEPTANCE = "";//DRAFTS AGAINST ACCEPTANCE---主表字段
		String ISSUINGBANK1 = "";//ISSUING BANK---主表字段
		String FOR_F = "";//FOR_F---主表字段
		String FOR_T = "";//FOR_T---主表字段
		String COMMODITY = "";//COMMODITY---主表字段
		String QUANTITY = "";//QUANTITY:---主表字段
		String UNITPRICE = "";//UNIT PRICE:---主表字段
		String CONTRACT = "";//CONTRACT.:---主表字段
		String TOTALAMOUNT = "";//TOTAL AMOUNT:---主表字段
		String ALLOWED1 = "";//ALLOWED---主表字段
		String NOTALLOWED1 = "";//NOT ALLOWED---主表字段
		String ALLOWED2 = "";//ALLOWED---主表字段
		String NOTALLOWED2 = "";//NOT ALLOWED---主表字段
		String TRANSFERABLE = "";//TRANSFERABLE---主表字段
		String NOTTRANSFERABLE = "";//NOT TRANSFERABLE---主表字段
		String WITHOUT = "";//WITHOUT---主表字段
		String CONFIRM = "";//CONFIRM---主表字段
		String MAYADD = "";//MAY ADD---主表字段
		String FOB = "";//FOB---主表字段
		String CFR = "";//CFR---主表字段
		String CIF = "";//CIF---主表字段
		String FCA = "";//FCA---主表字段
		String OTHERTERM = "";//OTHERTERM---主表字段
		String D1 = "";//D1---主表字段
		String D1_1 = "";//D1_1---主表字段
		String D1_2 = "";//D1_2---主表字段
		String D1_3 = "";//D1_3---主表字段
		String D2 = "";//D2---主表字段
		String D2_1 = "";//D2_1---主表字段
		String D2_2 = "";//D2_2---主表字段
		String D2_3 = "";//D2_3---主表字段
		String D2_4 = "";//D2_4---主表字段
		String D2_5 = "";//D2_5---主表字段
		String D2_6 = "";//D2_6---主表字段
		String D2_7 = "";//D2_7---主表字段
		String D3 = "";//D3---主表字段
		String D3_1 = "";//D3_1---主表字段
		String D3_2 = "";//D3_2---主表字段
		String D3_3 = "";//D3_3---主表字段
		String D3_4 = "";//D3_4---主表字段
		String D4 = "";//D4---主表字段
		String D4_1 = "";//D4_1---主表字段
		String D4_2 = "";//D4_2---主表字段
		String D4_3 = "";//D4_3---主表字段
		String D4_4 = "";//D4_4---主表字段
		String D5 = "";//D5---主表字段
		String D5_1 = "";//D5_1---主表字段
		String D5_2 = "";//D5_2---主表字段
		String D5_3 = "";//D5_3---主表字段
		String D5_4 = "";//D5_4---主表字段
		String D6 = "";//D6---主表字段
		String D6_1 = "";//D6_1---主表字段
		String D6_2 = "";//D6_2---主表字段
		String D6_3 = "";//D6_3---主表字段
		String D6_4 = "";//D6_4---主表字段
		String D7 = "";//D7---主表字段
		String D7_1 = "";//D7_1---主表字段
		String D7_2 = "";//D7_2---主表字段
		String D7_3 = "";//D7_3---主表字段
		String D7_4 = "";//D7_4---主表字段
		String D8 = "";//D8---主表字段
		String D8_1 = "";//D8_1---主表字段
		String D8_2 = "";//D8_2---主表字段
		String D8_3 = "";//D8_3---主表字段
		String D9 = "";//D9---主表字段
		String D9_1 = "";//D9_1---主表字段
		String D9_2 = "";//D9_2---主表字段
		String D10 = "";//D10---主表字段
		String D10_1 = "";//D10_1---主表字段
		String D11 = "";//D11---主表字段
		String D11_1 = "";//D11_1---主表字段
		String A1 = "";//A1---主表字段
		String A2 = "";//A2---主表字段
		String A2_1 = "";//A2_1---主表字段
		String A3 = "";//A3---主表字段
		String A4 = "";//A4---主表字段
		String A5 = "";//A5---主表字段
		String A5_1 = "";//A5_1---主表字段
		String A6 = "";//A6---主表字段
		String A7 = "";//A7---主表字段
		String A8 = "";//A8---主表字段
		String A9 = "";//A9---主表字段
		String A9_1 = "";//A9_1---主表字段
		String FOR_C = "";//FOR_C---主表字段
		String GYSADDRESS = "";
		String YHZH = "";
		String YHMC = "";
		String YHDZ = "";
		String BZ = "";
		String D2_8 = "";
		
		String OTHERTERM_C = "";//OTHERTERM_C---主表字段
		String GYSNAME = "";
		String VendorCode = "";
		String sylcje = "";
		String DXJE = "";
		sql = "select * from "+tableName+" where requestid = '"+rid+"'";
		rs.executeSql(sql);
		if(rs.next()) {

			sqrq = Util.null2String(rs.getString("sqrq"));//申请日期---主表字段
			EXPIRYDATE = Util.null2String(rs.getString("EXPIRYDATE"));//EXPIRY DATE---主表字段
			EXPIRYPLACE = Util.null2String(rs.getString("EXPIRYPLACE"));//EXPIRY PLACE---主表字段
			SHIPMENTFROM = Util.null2String(rs.getString("SHIPMENTFROM"));//SHIPMENT FROM---主表字段
			FORTRANSPORTATIONTO = Util.null2String(rs.getString("FORTRANSPORTATIONTO"));//FOR TRANSPORTATION TO---主表字段
			LATESTSHIPMENTDATE = Util.null2String(rs.getString("LATESTSHIPMENTDATE"));//LATEST SHIPMENT DATE---主表字段
			ANYBANK = Util.null2String(rs.getString("ANYBANK"));//ANY BANK---主表字段
			ISSUINGBANK = Util.null2String(rs.getString("ISSUINGBANK"));//ISSUING BANK---主表字段
			NEGOTIATION = Util.null2String(rs.getString("NEGOTIATION"));//NEGOTIATION---主表字段
			ACCEPTANCE = Util.null2String(rs.getString("ACCEPTANCE"));//ACCEPTANCE---主表字段
			PAYMENT = Util.null2String(rs.getString("PAYMENT"));//PAYMENT---主表字段
			DEFERRED = Util.null2String(rs.getString("DEFERRED"));//DEFERRED---主表字段
			PAYMENT1 = Util.null2String(rs.getString("PAYMENT1"));//PAYMENT文本---主表字段
			DAYSAFTER = Util.null2String(rs.getString("DAYSAFTER"));//DAYS AFTER---主表字段
			SIGHT = Util.null2String(rs.getString("SIGHT"));//SIGHT---主表字段
			DAYSAFTER1 = Util.null2String(rs.getString("DAYSAFTER1"));//DAYS AFTER---主表字段
			DRAFTSAGAINSTACCEPTANCE = Util.null2String(rs.getString("DRAFTSAGAINSTACCEPTANCE"));//DRAFTS AGAINST ACCEPTANCE---主表字段
			ISSUINGBANK1 = Util.null2String(rs.getString("ISSUINGBANK1"));//ISSUING BANK---主表字段
			FOR_F = Util.null2String(rs.getString("FOR_F"));//FOR_F---主表字段
			FOR_T = Util.null2String(rs.getString("FOR_T"));//FOR_T---主表字段
			COMMODITY = Util.null2String(rs.getString("COMMODITY"));//COMMODITY---主表字段
			QUANTITY = Util.null2String(rs.getString("QUANTITY"));//QUANTITY:---主表字段
			UNITPRICE = Util.null2String(rs.getString("UNITPRICE"));//UNIT PRICE:---主表字段
			CONTRACT = Util.null2String(rs.getString("CONTRACT"));//CONTRACT.:---主表字段
			TOTALAMOUNT = Util.null2String(rs.getString("TOTALAMOUNT"));//TOTAL AMOUNT:---主表字段
			ALLOWED1 = Util.null2String(rs.getString("ALLOWED1"));//ALLOWED---主表字段
			NOTALLOWED1 = Util.null2String(rs.getString("NOTALLOWED1"));//NOT ALLOWED---主表字段
			ALLOWED2 = Util.null2String(rs.getString("ALLOWED2"));//ALLOWED---主表字段
			NOTALLOWED2 = Util.null2String(rs.getString("NOTALLOWED2"));//NOT ALLOWED---主表字段
			TRANSFERABLE = Util.null2String(rs.getString("TRANSFERABLE"));//TRANSFERABLE---主表字段
			NOTTRANSFERABLE = Util.null2String(rs.getString("NOTTRANSFERABLE"));//NOT TRANSFERABLE---主表字段
			WITHOUT = Util.null2String(rs.getString("WITHOUT"));//WITHOUT---主表字段
			CONFIRM = Util.null2String(rs.getString("CONFIRM"));//CONFIRM---主表字段
			MAYADD = Util.null2String(rs.getString("MAYADD"));//MAY ADD---主表字段
			FOB = Util.null2String(rs.getString("FOB"));//FOB---主表字段
			CFR = Util.null2String(rs.getString("CFR"));//CFR---主表字段
			CIF = Util.null2String(rs.getString("CIF"));//CIF---主表字段
			FCA = Util.null2String(rs.getString("FCA"));//FCA---主表字段
			OTHERTERM = Util.null2String(rs.getString("OTHERTERM"));//OTHERTERM---主表字段
			D1 = Util.null2String(rs.getString("D1"));//D1---主表字段
			D1_1 = Util.null2String(rs.getString("D1_1"));//D1_1---主表字段
			D1_2 = Util.null2String(rs.getString("D1_2"));//D1_2---主表字段
			D1_3 = Util.null2String(rs.getString("D1_3"));//D1_3---主表字段
			D2 = Util.null2String(rs.getString("D2"));//D2---主表字段
			D2_1 = Util.null2String(rs.getString("D2_1"));//D2_1---主表字段
			D2_2 = Util.null2String(rs.getString("D2_2"));//D2_2---主表字段
			D2_3 = Util.null2String(rs.getString("D2_3"));//D2_3---主表字段
			D2_4 = Util.null2String(rs.getString("D2_4"));//D2_4---主表字段
			D2_5 = Util.null2String(rs.getString("D2_5"));//D2_5---主表字段
			D2_6 = Util.null2String(rs.getString("D2_6"));//D2_6---主表字段
			D2_7 = Util.null2String(rs.getString("D2_7"));//D2_7---主表字段
			D3 = Util.null2String(rs.getString("D3"));//D3---主表字段
			D3_1 = Util.null2String(rs.getString("D3_1"));//D3_1---主表字段
			D3_2 = Util.null2String(rs.getString("D3_2"));//D3_2---主表字段
			D3_3 = Util.null2String(rs.getString("D3_3"));//D3_3---主表字段
			D3_4 = Util.null2String(rs.getString("D3_4"));//D3_4---主表字段
			D4 = Util.null2String(rs.getString("D4"));//D4---主表字段
			D4_1 = Util.null2String(rs.getString("D4_1"));//D4_1---主表字段
			D4_2 = Util.null2String(rs.getString("D4_2"));//D4_2---主表字段
			D4_3 = Util.null2String(rs.getString("D4_3"));//D4_3---主表字段
			D4_4 = Util.null2String(rs.getString("D4_4"));//D4_4---主表字段
			D5 = Util.null2String(rs.getString("D5"));//D5---主表字段
			D5_1 = Util.null2String(rs.getString("D5_1"));//D5_1---主表字段
			D5_2 = Util.null2String(rs.getString("D5_2"));//D5_2---主表字段
			D5_3 = Util.null2String(rs.getString("D5_3"));//D5_3---主表字段
			D5_4 = Util.null2String(rs.getString("D5_4"));//D5_4---主表字段
			D6 = Util.null2String(rs.getString("D6"));//D6---主表字段
			D6_1 = Util.null2String(rs.getString("D6_1"));//D6_1---主表字段
			D6_2 = Util.null2String(rs.getString("D6_2"));//D6_2---主表字段
			D6_3 = Util.null2String(rs.getString("D6_3"));//D6_3---主表字段
			D6_4 = Util.null2String(rs.getString("D6_4"));//D6_4---主表字段
			D7 = Util.null2String(rs.getString("D7"));//D7---主表字段
			D7_1 = Util.null2String(rs.getString("D7_1"));//D7_1---主表字段
			D7_2 = Util.null2String(rs.getString("D7_2"));//D7_2---主表字段
			D7_3 = Util.null2String(rs.getString("D7_3"));//D7_3---主表字段
			D7_4 = Util.null2String(rs.getString("D7_4"));//D7_4---主表字段
			D8 = Util.null2String(rs.getString("D8"));//D8---主表字段
			D8_1 = Util.null2String(rs.getString("D8_1"));//D8_1---主表字段
			D8_2 = Util.null2String(rs.getString("D8_2"));//D8_2---主表字段
			D8_3 = Util.null2String(rs.getString("D8_3"));//D8_3---主表字段
			D9 = Util.null2String(rs.getString("D9"));//D9---主表字段
			D9_1 = Util.null2String(rs.getString("D9_1"));//D9_1---主表字段
			D9_2 = Util.null2String(rs.getString("D9_2"));//D9_2---主表字段
			D10 = Util.null2String(rs.getString("D10"));//D10---主表字段
			D10_1 = Util.null2String(rs.getString("D10_1"));//D10_1---主表字段
			D11 = Util.null2String(rs.getString("D11"));//D11---主表字段
			D11_1 = Util.null2String(rs.getString("D11_1"));//D11_1---主表字段
			A1 = Util.null2String(rs.getString("A1"));//A1---主表字段
			A2 = Util.null2String(rs.getString("A2"));//A2---主表字段
			A2_1 = Util.null2String(rs.getString("A2_1"));//A2_1---主表字段
			A3 = Util.null2String(rs.getString("A3"));//A3---主表字段
			A4 = Util.null2String(rs.getString("A4"));//A4---主表字段
			A5 = Util.null2String(rs.getString("A5"));//A5---主表字段
			A5_1 = Util.null2String(rs.getString("A5_1"));//A5_1---主表字段
			A6 = Util.null2String(rs.getString("A6"));//A6---主表字段
			A7 = Util.null2String(rs.getString("A7"));//A7---主表字段
			A8 = Util.null2String(rs.getString("A8"));//A8---主表字段
			A9 = Util.null2String(rs.getString("A9"));//A9---主表字段
			A9_1 = Util.null2String(rs.getString("A9_1"));//A9_1---主表字段
			D2_8 = Util.null2String(rs.getString("D2_8"));//
			FOR_C = Util.null2String(rs.getString("FOR_C"));//FOR_C---主表字段
			OTHERTERM_C = Util.null2String(rs.getString("OTHERTERM_C"));//OTHERTERM_C---主表字段
			GYSNAME = Util.null2String(rs.getString("GYSNAME"));
			VendorCode = Util.null2String(rs.getString("VendorCode"));
			sylcje = Util.null2String(rs.getString("sylcje"));
			DXJE = Util.null2String(rs.getString("DXJE"));
			GYSADDRESS = Util.null2String(rs.getString("GYSADDRESS"));
			YHZH = Util.null2String(rs.getString("YHZH"));
			YHMC = Util.null2String(rs.getString("YHMC"));
			YHDZ = Util.null2String(rs.getString("YHDZ"));
			BZ = Util.null2String(rs.getString("BZ"));
		}
		// 新建

		
		// 流程数据insert
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("sqrq", sqrq);//申请日期
		mapStr.put("EXPIRYDATE", EXPIRYDATE);//EXPIRY DATE
		mapStr.put("EXPIRYPLACE", EXPIRYPLACE);//EXPIRY PLACE
		mapStr.put("SHIPMENTFROM", SHIPMENTFROM);//SHIPMENT FROM
		mapStr.put("FORTRANSPORTATIONTO", FORTRANSPORTATIONTO);//FOR TRANSPORTATION TO
		mapStr.put("LATESTSHIPMENTDATE", LATESTSHIPMENTDATE);//LATEST SHIPMENT DATE
		mapStr.put("PAYMENT1", PAYMENT1);//PAYMENT文本
		mapStr.put("DAYSAFTER", DAYSAFTER);//DAYS AFTER
		mapStr.put("FOR_F", FOR_F);//FOR_F
		mapStr.put("FOR_T", FOR_T);//FOR_T
		mapStr.put("COMMODITY", COMMODITY);//COMMODITY
		mapStr.put("QUANTITY", QUANTITY);//QUANTITY:
		mapStr.put("UNITPRICE", UNITPRICE);//UNIT PRICE:
		mapStr.put("CONTRACT", CONTRACT);//CONTRACT.:
		mapStr.put("TOTALAMOUNT", TOTALAMOUNT);//TOTAL AMOUNT:
		mapStr.put("TRANSFERABLE", TRANSFERABLE);//TRANSFERABLE
		mapStr.put("NOTTRANSFERABLE", NOTTRANSFERABLE);//NOT TRANSFERABLE
		mapStr.put("WITHOUT", WITHOUT);//WITHOUT
		mapStr.put("CONFIRM", CONFIRM);//CONFIRM
		mapStr.put("MAYADD", MAYADD);//MAY ADD
		mapStr.put("OTHERTERM", OTHERTERM);//OTHERTERM
		mapStr.put("D1_1", D1_1);//D1_1
		mapStr.put("D1_2", D1_2);//D1_2
		mapStr.put("D1_3", D1_3);//D1_3
		mapStr.put("D2_2", D2_2);//D2_2
		mapStr.put("D2_3", D2_3);//D2_3
		mapStr.put("D3_1", D3_1);//D3_1
		mapStr.put("D3_2", D3_2);//D3_2
		mapStr.put("D4_1", D4_1);//D4_1
		mapStr.put("D4_2", D4_2);//D4_2
		mapStr.put("D4_3", D4_3);//D4_3
		mapStr.put("D4_4", D4_4);//D4_4
		mapStr.put("D5_1", D5_1);//D5_1
		mapStr.put("D5_2", D5_2);//D5_2
		mapStr.put("D5_3", D5_3);//D5_3
		mapStr.put("D5_4", D5_4);//D5_4
		mapStr.put("D6_1", D6_1);//D6_1
		mapStr.put("D6_2", D6_2);//D6_2
		mapStr.put("D6_3", D6_3);//D6_3
		mapStr.put("D6_4", D6_4);//D6_4
		mapStr.put("D7_1", D7_1);//D7_1
		mapStr.put("D7_2", D7_2);//D7_2
		mapStr.put("D7_3", D7_3);//D7_3
		mapStr.put("D7_4", D7_4);//D7_4
		mapStr.put("D8_1", D8_1);//D8_1
		mapStr.put("D8_2", D8_2);//D8_2
		mapStr.put("D8_3", D8_3);//D8_3
		mapStr.put("D9_1", D9_1);//D9_1
		mapStr.put("D9_2", D9_2);//D9_2
		mapStr.put("D10_1", D10_1);//D10_1
		mapStr.put("D11_1", D11_1.replace("<br>", "@@@"));//D11_1
		mapStr.put("A2_1", A2_1);//A2_1
		mapStr.put("A5_1", A5_1);//A5_1
		mapStr.put("A9_1", A9_1.replace("<br>", "@@@"));//A9_1
		mapStr.put("GYSNAME", GYSNAME);//GYSNAME
		mapStr.put("VendorCode", VendorCode);//VendorCode
		mapStr.put("sylcje", sylcje);//sylcje
		mapStr.put("DXJE", DXJE);//DXJE
		mapStr.put("GYSADDRESS", GYSADDRESS);// GYSADDRESS
		mapStr.put("D2_8", D2_8);//D2_8
		mapStr.put("YHZH", YHZH);// GYSADDRESS
		mapStr.put("YHMC", YHMC);// GYSADDRESS
		mapStr.put("YHDZ", YHDZ);// GYSADDRESS
		mapStr.put("BZ", BZ);// GYSADDRESS
		if(ANYBANK.length()<1) {
		   mapStr.put("ANYBANK", "□");
		  }else {
		   mapStr.put("ANYBANK", "☑");
		  }//ANY BANK"
		if(ISSUINGBANK.length()<1) {
		   mapStr.put("ISSUINGBANK", "□");
		  }else {
		   mapStr.put("ISSUINGBANK", "☑");
		  }//ISSUING BANK"
		if(NEGOTIATION.length()<1) {
		   mapStr.put("NEGOTIATION", "□");
		  }else {
		   mapStr.put("NEGOTIATION", "☑");
		  }//NEGOTIATION"
		if(ACCEPTANCE.length()<1) {
		   mapStr.put("ACCEPTANCE", "□");
		  }else {
		   mapStr.put("ACCEPTANCE", "☑");
		  }//ACCEPTANCE"
		if(PAYMENT.length()<1) {
		   mapStr.put("PAYMENT", "□");
		  }else {
		   mapStr.put("PAYMENT", "☑");
		  }//PAYMENT"
		if(DEFERRED.length()<1) {
		   mapStr.put("DEFERRED", "□");
		  }else {
		   mapStr.put("DEFERRED", "☑");
		  }//DEFERRED"
		if(SIGHT.length()<1) {
		   mapStr.put("SIGHT", "□");
		  }else {
		   mapStr.put("SIGHT", "☑");
		  }//SIGHT"
		if(DAYSAFTER1.length()<1) {
		   mapStr.put("DAYSAFTER1", "□");
		  }else {
		   mapStr.put("DAYSAFTER1", "☑");
		  }//DAYS AFTER"
		if(DRAFTSAGAINSTACCEPTANCE.length()<1) {
		   mapStr.put("DRAFTSAGAINSTACCEPTANCE", "□");
		  }else {
		   mapStr.put("DRAFTSAGAINSTACCEPTANCE", "☑");
		  }//DRAFTS AGAINST ACCEPTANCE"
		if(ISSUINGBANK1.length()<1) {
		   mapStr.put("ISSUINGBANK1", "□");
		  }else {
		   mapStr.put("ISSUINGBANK1", "☑");
		  }//ISSUING BANK"
		if(ALLOWED1.length()<1) {
		   mapStr.put("ALLOWED1", "□");
		  }else {
		   mapStr.put("ALLOWED1", "☑");
		  }//ALLOWED"
		if(NOTALLOWED1.length()<1) {
		   mapStr.put("NOTALLOWED1", "□");
		  }else {
		   mapStr.put("NOTALLOWED1", "☑");
		  }//NOT ALLOWED"
		if(ALLOWED2.length()<1) {
		   mapStr.put("ALLOWED2", "□");
		  }else {
		   mapStr.put("ALLOWED2", "☑");
		  }//ALLOWED"
		if(NOTALLOWED2.length()<1) {
		   mapStr.put("NOTALLOWED2", "□");
		  }else {
		   mapStr.put("NOTALLOWED2", "☑");
		  }//NOT ALLOWED"
		if(FOB.length()<1) {
		   mapStr.put("FOB", "□");
		  }else {
		   mapStr.put("FOB", "☑");
		  }//FOB"
		if(CFR.length()<1) {
		   mapStr.put("CFR", "□");
		  }else {
		   mapStr.put("CFR", "☑");
		  }//CFR"
		if(CIF.length()<1) {
		   mapStr.put("CIF", "□");
		  }else {
		   mapStr.put("CIF", "☑");
		  }//CIF"
		if(FCA.length()<1) {
		   mapStr.put("FCA", "□");
		  }else {
		   mapStr.put("FCA", "☑");
		  }//FCA"
		if(D1.length()<1) {
		   mapStr.put("D1", "□");
		  }else {
		   mapStr.put("D1", "☑");
		  }//D1"
		if(D2.length()<1) {
		   mapStr.put("D2", "□");
		  }else {
		   mapStr.put("D2", "☑");
		  }//D2"
		if(D2_1.length()<1) {
		   mapStr.put("D2_1", "□");
		  }else {
		   mapStr.put("D2_1", "☑");
		  }//D2_1"
		if(D2_4.length()<1) {
		   mapStr.put("D2_4", "□");
		  }else {
		   mapStr.put("D2_4", "☑");
		  }//D2_4"
		if(D2_5.length()<1) {
		   mapStr.put("D2_5", "□");
		  }else {
		   mapStr.put("D2_5", "☑");
		  }//D2_5"
		if(D2_6.length()<1) {
		   mapStr.put("D2_6", "□");
		  }else {
		   mapStr.put("D2_6", "☑");
		  }//D2_6"
		if(D2_7.length()<1) {
		   mapStr.put("D2_7", "□");
		  }else {
		   mapStr.put("D2_7", "☑");
		  }//D2_7"
		if(D3.length()<1) {
		   mapStr.put("D3", "□");
		  }else {
		   mapStr.put("D3", "☑");
		  }//D3"
		if(D3_3.length()<1) {
		   mapStr.put("D3_3", "□");
		  }else {
		   mapStr.put("D3_3", "☑");
		  }//D3_3"
		if(D3_4.length()<1) {
		   mapStr.put("D3_4", "□");
		  }else {
		   mapStr.put("D3_4", "☑");
		  }//D3_4"
		if(D4.length()<1) {
		   mapStr.put("D4", "□");
		  }else {
		   mapStr.put("D4", "☑");
		  }//D4"
		if(D5.length()<1) {
		   mapStr.put("D5", "□");
		  }else {
		   mapStr.put("D5", "☑");
		  }//D5"
		if(D6.length()<1) {
		   mapStr.put("D6", "□");
		  }else {
		   mapStr.put("D6", "☑");
		  }//D6"
		if(D7.length()<1) {
		   mapStr.put("D7", "□");
		  }else {
		   mapStr.put("D7", "☑");
		  }//D7"
		if(D8.length()<1) {
		   mapStr.put("D8", "□");
		  }else {
		   mapStr.put("D8", "☑");
		  }//D8"
		if(D9.length()<1) {
		   mapStr.put("D9", "□");
		  }else {
		   mapStr.put("D9", "☑");
		  }//D9"
		if(D10.length()<1) {
		   mapStr.put("D10", "□");
		  }else {
		   mapStr.put("D10", "☑");
		  }//D10"
		if(D11.length()<1) {
		   mapStr.put("D11", "□");
		  }else {
		   mapStr.put("D11", "☑");
		  }//D11"
		if(A1.length()<1) {
		   mapStr.put("A1", "□");
		  }else {
		   mapStr.put("A1", "☑");
		  }//A1"
		if(A2.length()<1) {
		   mapStr.put("A2", "□");
		  }else {
		   mapStr.put("A2", "☑");
		  }//A2"
		if(A3.length()<1) {
		   mapStr.put("A3", "□");
		  }else {
		   mapStr.put("A3", "☑");
		  }//A3"
		if(A4.length()<1) {
		   mapStr.put("A4", "□");
		  }else {
		   mapStr.put("A4", "☑");
		  }//A4"
		if(A5.length()<1) {
		   mapStr.put("A5", "□");
		  }else {
		   mapStr.put("A5", "☑");
		  }//A5"
		if(A6.length()<1) {
			   mapStr.put("A6", "□");
		}else {
			mapStr.put("A6", "☑");
		}//A6
		if(A6.length()<1) {
			   mapStr.put("A7", "□");
		}else {
			mapStr.put("A7", "☑");
		}//A7
		if(A8.length()<1) {
		   mapStr.put("A8", "□");
		  }else {
		   mapStr.put("A8", "☑");
		 }//A8"
		if(A9.length()<1){
		   mapStr.put("A9", "□");
		  }else {
		   mapStr.put("A9", "☑");
		  }//A9"
		if(FOR_C.length()<1) {
		   mapStr.put("FOR_C", "□");
		  }else {
		   mapStr.put("FOR_C", "☑");
		  }//FOR_C"
		if(OTHERTERM_C.length()<1) {
		   mapStr.put("OTHERTERM_C", "□");
		  }else {
		   mapStr.put("OTHERTERM_C", "☑");
		  }//OTHERTERM_C"

		CreateWordContent ccf = new CreateWordContent();
		String docid = ccf.CreateFile(mapStr, tableName, rid);
		if(docid.length()<1) {
			info.getRequestManager().setMessagecontent("文档生成失败");    
			info.getRequestManager().setMessageid("错误信息提示"); 
			return FAILURE_AND_CONTINUE ;//
		}
		log.writeLog("文档id-----"+docid);
		sql = "update "+tableName+" set xywd = '"+docid+"' where requestid = '"+rid+"'";
		rs.execute(sql);
		
		return SUCCESS;
	}
}
