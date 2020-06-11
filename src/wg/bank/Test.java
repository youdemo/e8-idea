package wg.bank;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.docx4j.model.datastorage.XPathEnhancerParser.nodeTest_return;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import weaver.general.BaseBean;

public class Test {
	public static void main(String args[]) throws Exception {
		//String aa="<<aa<<".replaceAll("<", "&lt");
		SimpleDateFormat sf1  = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2  = new SimpleDateFormat("yyyyMMdd");
		System.out.println(sf1.format(sf2.parse("20190201")));
//		String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>NTQRYSTN</FUNNAM><LGNNAM>学科园科兴学532</LGNNAM><RETCOD>0</RETCOD></INFO><NTSTLLSTZ><ATHFLG>N</ATHFLG><BUSCOD>N02030</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><CRTACC>7777880230001175</CRTACC><CRTBBK>10</CRTBBK><CRTNAM>中国工商银行总行清算中心</CRTNAM><DBTACC>755915711310210</DBTACC><DBTBBK>75</DBTBBK><EPTDAT>20191009</EPTDAT><EPTTIM>000000</EPTTIM><OPRDAT>20190206</OPRDAT><REQNBR>0030430623</REQNBR><REQSTS>NTE</REQSTS><TRSAMT>23.35</TRSAMT><YURREF>EX-201906190023_15</YURREF></NTSTLLSTZ><NTSTLLSTZ><ATHFLG>N</ATHFLG><BUSCOD>N02030</BUSCOD><BUSMOD>00001</BUSMOD><CCYNBR>10</CCYNBR><CRTACC>7777880230001175</CRTACC><CRTBBK>10</CRTBBK><CRTNAM>中国工商银行总行清算中心</CRTNAM><DBTACC>755915711310210</DBTACC><DBTBBK>75</DBTBBK><EPTDAT>20191010</EPTDAT><EPTTIM>000000</EPTTIM><OPRDAT>20190208</OPRDAT><REQNBR>0030431413</REQNBR><REQSTS>NTE</REQSTS><TRSAMT>23.35</TRSAMT><YURREF>EX-201906190023_16</YURREF></NTSTLLSTZ></CMBSDKPGK>";
String result = "<?xml version='1.0' encoding='GBK'?>\r\n" + 
		"                        <CMBSDKPGK>\r\n" + 
		"                        <INFO>\r\n" + 
		"                        <DATTYP>2</DATTYP>           \r\n" + 
		"                        <ERRMSG></ERRMSG>           \r\n" + 
		"                        <FUNNAM>GetTransInfo</FUNNAM>\r\n" + 
		"                        <RETCOD>0</RETCOD>           \r\n" + 
		"                        </INFO>\r\n" + 
		"                        <NTQTSINFZ>                  \r\n" + 
		"                        <ETYDAT>20060915</ETYDAT>  \r\n" + 
		"                        <ETYTIM>150858</ETYTIM>    \r\n" + 
		"                        <VLTDAT></VLTDAT>        \r\n" + 
		"                        <TRSCOD>GATR</TRSCOD> \r\n" + 
		"                        <NARYUR>1700016092000032</NARYUR> \r\n" + 
		"                        <TRSAMTD>450.00</TRSAMTD>  \r\n" + 
		"                        <TRSAMTC></TRSAMTC>   \r\n" + 
		"                        <AMTCDR>D</AMTCDR>     \r\n" + 
		"                        <TRSBLV>968715563.18</TRSBLV> \r\n" + 
		"                        <REFNBR>AP090003</REFNBR>   \r\n" + 
		"                        <REQNBR>0012341529</REQNBR>\r\n" + 
		"                        <BUSNAM>企业银行直接支付</BUSNAM> \r\n" + 
		"                        <NUSAGE></NUSAGE>                 \r\n" + 
		"                        <YURREF>6092000032</YURREF>\r\n" + 
		"                        <BUSNAR></BUSNAR>         \r\n" + 
		"                        <OTRNAR></OTRNAR>         \r\n" + 
		"                        <C_RPYBBK></C_RPYBBK>   \r\n" + 
		"                        <RPYNAM>北京集团公司收方帐号</RPYNAM>   \r\n" + 
		"                        <RPYACC>1280078810099</RPYACC>          \r\n" + 
		"                        <RPYBBN></RPYBBN>                     \r\n" + 
		"                        <RPYBNK>招商银行乌鲁木齐分行</RPYBNK>   \r\n" + 
		"                        <RPYADR>新疆维吾尔自治区乌鲁木齐市</RPYADR> \r\n" + 
		"                        <C_GSBBBK></C_GSBBBK>\r\n" + 
		"                        <GSBACC></GSBACC>     \r\n" + 
		"                        <GSBNAM></GSBNAM>   \r\n" + 
		"                        <INFFLG>1</INFFLG>        \r\n" + 
		"                        <ATHFLG>N</ATHFLG>  \r\n" + 
		"                        <CHKNBR></CHKNBR>     \r\n" + 
		"                        <RSVFLG></RSVFLG>      \r\n" + 
		"                        <NAREXT>1750054987</NAREXT> \r\n" + 
		"                        <TRSANL>APGATR</TRSANL>   \r\n" + 
		"                        </NTQTSINFZ>\r\n" + 
		"                        </CMBSDKPGK>";
List<Map<String, String>> resultList = getjyrqList(result);
System.out.println(resultList.toString());
		//		Map<String,String> resultMap =doxml2(result);
//		System.out.println(resultMap.toString());
//		if("0".equals(resultMap.get("RETCOD"))) {
//			List<Map<String, String>> resultlist = getresultList(result);
//			System.out.println(resultlist.toString());
//		}
	}
	public static List<Map<String, String>> getjyrqList(String result) throws Exception {
		List<Map<String, String>> resultlist = new ArrayList<Map<String, String>>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		
		NodeList list =doc.getElementsByTagName("NTQTSINFZ");
		for(int i=0;i<list.getLength();i++) {
			Node node=list.item(i);
			NodeList childlist = node.getChildNodes();
			Map<String,String> resultMap = new HashMap<String, String>();
			for(int j=0;j<childlist.getLength();j++) {				
				Node childnode=childlist.item(j);
				if("YURREF".equals(childnode.getNodeName())) {
					resultMap.put("YURREF", childnode.getTextContent());
				}
				if("ETYDAT".equals(childnode.getNodeName())) {
					resultMap.put("ETYDAT", childnode.getTextContent());
				}
				
			}
			if(!resultMap.isEmpty()) {
				resultlist.add(resultMap);
			}
		}
		return resultlist;
	}
	public static Map<String,String> doxml2(String result) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		if(doc.getElementsByTagName("RETCOD").getLength()>0) {
			resultMap.put("RETCOD", doc.getElementsByTagName("RETCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRMSG").getLength()>0) {
			resultMap.put("ERRMSG", doc.getElementsByTagName("ERRMSG").item(0).getTextContent());
		}
		
		return resultMap;
		
	}
	public static List<Map<String, String>> getresultList(String result) throws Exception {
		List<Map<String, String>> resultlist = new ArrayList<Map<String, String>>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		
		NodeList list =doc.getElementsByTagName("NTSTLLSTZ");
		for(int i=0;i<list.getLength();i++) {
			Node node=list.item(i);
			NodeList childlist = node.getChildNodes();
			Map<String,String> resultMap = new HashMap<String, String>();
			for(int j=0;j<childlist.getLength();j++) {				
				Node childnode=childlist.item(j);
				if("YURREF".equals(childnode.getNodeName())) {
					resultMap.put("YURREF", childnode.getTextContent());
				}
				if("OPRDAT".equals(childnode.getNodeName())) {
					resultMap.put("OPRDAT", childnode.getTextContent());
				}
				if("EPTDAT".equals(childnode.getNodeName())) {
					resultMap.put("EPTDAT", childnode.getTextContent());
				}
				if("REQSTS".equals(childnode.getNodeName())) {
					resultMap.put("REQSTS", childnode.getTextContent());
				}
				if("RTNFLG".equals(childnode.getNodeName())) {
					resultMap.put("RTNFLG", childnode.getTextContent());
				}
			}
			if(!resultMap.isEmpty()) {
				resultlist.add(resultMap);
			}
		}
		return resultlist;
	}
	public static void doxml(String result) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		if(doc.getElementsByTagName("RETCOD").getLength()>0) {
			resultMap.put("RETCOD", doc.getElementsByTagName("RETCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRCOD").getLength()>0) {
			resultMap.put("ERRCOD", doc.getElementsByTagName("ERRCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("OPRALS").getLength()>0) {
			resultMap.put("OPRALS", doc.getElementsByTagName("OPRALS").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRTXT").getLength()>0) {
			resultMap.put("ERRTXT", doc.getElementsByTagName("ERRTXT").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRMSG").getLength()>0) {
			resultMap.put("ERRMSG", doc.getElementsByTagName("ERRMSG").item(0).getTextContent());
		}
		
	}
	public String test() {
		String result = "";
		String status = "";
		String errcode = "";
		String errtxt = "";
		String zxjg = "";
		String parm = "<?xml   version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK>\r\n" + 
				"                           <INFO>\r\n" + 
				"                              <FUNNAM>DCPAYREQ</FUNNAM>\r\n" + 
				"                              <DATTYP>2</DATTYP>\r\n" + 
				"                              <LGNNAM>学科园科兴学532</LGNNAM>\r\n" + 
				"                           </INFO>\r\n" + 
				"                           <SDKPAYRQX>\r\n" + 
				"                              <BUSCOD>N02030</BUSCOD>\r\n" + //业务类别 N02030:支付 N02040:集团支付
				"                              <BUSMOD>00001</BUSMOD>\r\n" + 
				"                           </SDKPAYRQX>\r\n" + 
				"                           <DCPAYREQX>\r\n" + 
				"                              <YURREF>201905130004</YURREF>\r\n" + //业务号
				"                              <DBTACC>755915711310210</DBTACC>\r\n" + //付款账号
				"                              <DBTBBK>75</DBTBBK>\r\n" + //地区
				"                              <BNKFLG>N</BNKFLG>\r\n" + //Y：招行；N：非招行；
				"                              <STLCHN>N</STLCHN>\r\n" + //结算方式
				"                              <TRSAMT>33.35</TRSAMT>\r\n" + //金额
				"                              <CCYNBR>10</CCYNBR>\r\n" + //人民币
				"                              <NUSAGE>测试002</NUSAGE>\r\n" + //对应对账单中的摘要
				"                              <CRTACC>7777880230001175</CRTACC>\r\n" + //收方账号
                //"                              <BRDNBR>102100099996</BRDNBR>\r\n" + //收方行号
                "                              <CRTBNK>中国工商银行总行清算中心</CRTBNK>\r\n" + //收方开户行名称
				"                              <CRTNAM>中国工商银行总行清算中心</CRTNAM>\r\n" + //帐户名称
				"                              <CRTPVC>北京</CRTPVC>\r\n" + //收方省份
				"                              <CRTCTY>北京</CRTCTY>\r\n" + //收方城市
				//"                              <CRTDTR>石龙区</CRTDTR>\r\n" + 
				"                              <RCVCHK>1</RCVCHK>\r\n" + 
				"                              <BUSNAR>测试支付</BUSNAR>\r\n" + 
				"                           </DCPAYREQX>  \r\n" + 
				"                        </CMBSDKPGK>";
		
		try {
			//result = postConnection("http://192.168.7.36:8080", parm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			BaseBean log = new BaseBean();
			log.writeLog("result aaa:"+result);
			
		
		return result;
	}
}
