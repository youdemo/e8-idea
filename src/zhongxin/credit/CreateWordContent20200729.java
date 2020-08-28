package zhongxin.credit;

import org.apache.axis.encoding.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年3月18日 下午2:11:37 
* @version 1.0 
*/
public class CreateWordContent20200729 {
	BaseBean log = new BaseBean();
	/**
	 * 根据流程数据放入标签map创建合同模板
	 * @param mapStr 流程数据map
	 * @param tableName 流程表名
	 * @param requestid 流程id
	 * @return
	 */
	public String CreateFile(Map<String, String> mapStr,String tableName,String requestid) {
		
//		RecordSet rs = new RecordSet();
		//文档标签  map
		Map<String, String> bookMarkMap = new HashMap<String, String>();
		log.writeLog("----开始生成文档");
		bookMarkMap.put("sqrq", mapStr.get("sqrq"));//申请日期
		bookMarkMap.put("EXPIRYDATE", mapStr.get("EXPIRYDATE"));//EXPIRY DATE
		bookMarkMap.put("EXPIRYPLACE", mapStr.get("EXPIRYPLACE"));//EXPIRY PLACE
		bookMarkMap.put("SHIPMENTFROM", mapStr.get("SHIPMENTFROM"));//SHIPMENT FROM
		bookMarkMap.put("FORTRANSPORTATIONTO", mapStr.get("FORTRANSPORTATIONTO"));//FOR TRANSPORTATION TO
		bookMarkMap.put("LATESTSHIPMENTDATE", mapStr.get("LATESTSHIPMENTDATE"));//LATEST SHIPMENT DATE
		bookMarkMap.put("ANYBANK", mapStr.get("ANYBANK"));//ANY BANK
		bookMarkMap.put("ISSUINGBANK", mapStr.get("ISSUINGBANK"));//ISSUING BANK
		bookMarkMap.put("NEGOTIATION", mapStr.get("NEGOTIATION"));//NEGOTIATION
		bookMarkMap.put("ACCEPTANCE", mapStr.get("ACCEPTANCE"));//ACCEPTANCE
		bookMarkMap.put("PAYMENT", mapStr.get("PAYMENT"));//PAYMENT
		bookMarkMap.put("DEFERRED", mapStr.get("DEFERRED"));//DEFERRED
		bookMarkMap.put("PAYMENT1", mapStr.get("PAYMENT1"));//PAYMENT文本
		bookMarkMap.put("DAYSAFTER", mapStr.get("DAYSAFTER"));//DAYS AFTER
		bookMarkMap.put("SIGHT", mapStr.get("SIGHT"));//SIGHT
		bookMarkMap.put("DAYSAFTER1", mapStr.get("DAYSAFTER1"));//DAYS AFTER
		bookMarkMap.put("DRAFTSAGAINSTACCEPTANCE", mapStr.get("DRAFTSAGAINSTACCEPTANCE"));//DRAFTS AGAINST ACCEPTANCE
		bookMarkMap.put("ISSUINGBANK1", mapStr.get("ISSUINGBANK1"));//ISSUING BANK
		bookMarkMap.put("FOR_F", mapStr.get("FOR_F"));//FOR_F
		bookMarkMap.put("FOR_T", mapStr.get("FOR_T"));//FOR_T
		bookMarkMap.put("COMMODITY", mapStr.get("COMMODITY"));//COMMODITY
		bookMarkMap.put("QUANTITY", mapStr.get("QUANTITY"));//QUANTITY:
		bookMarkMap.put("UNITPRICE", mapStr.get("UNITPRICE"));//UNIT PRICE:
		bookMarkMap.put("CONTRACT", mapStr.get("CONTRACT"));//CONTRACT.:
		bookMarkMap.put("TOTALAMOUNT", mapStr.get("TOTALAMOUNT"));//TOTAL AMOUNT:
		bookMarkMap.put("ALLOWED1", mapStr.get("ALLOWED1"));//ALLOWED
		bookMarkMap.put("NOTALLOWED1", mapStr.get("NOTALLOWED1"));//NOT ALLOWED
		bookMarkMap.put("ALLOWED2", mapStr.get("ALLOWED2"));//ALLOWED
		bookMarkMap.put("NOTALLOWED2", mapStr.get("NOTALLOWED2"));//NOT ALLOWED
		bookMarkMap.put("TRANSFERABLE", mapStr.get("TRANSFERABLE"));//TRANSFERABLE
		bookMarkMap.put("NOTTRANSFERABLE", mapStr.get("NOTTRANSFERABLE"));//NOT TRANSFERABLE
		bookMarkMap.put("WITHOUT", mapStr.get("WITHOUT"));//WITHOUT
		bookMarkMap.put("CONFIRM", mapStr.get("CONFIRM"));//CONFIRM
		bookMarkMap.put("MAYADD", mapStr.get("MAYADD"));//MAY ADD
		bookMarkMap.put("FOB", mapStr.get("FOB"));//FOB
		bookMarkMap.put("CFR", mapStr.get("CFR"));//CFR
		bookMarkMap.put("CIF", mapStr.get("CIF"));//CIF
		bookMarkMap.put("FCA", mapStr.get("FCA"));//FCA
		bookMarkMap.put("OTHERTERM", mapStr.get("OTHERTERM"));//OTHERTERM
		bookMarkMap.put("D1", mapStr.get("D1"));//D1
		bookMarkMap.put("D1_1", mapStr.get("D1_1"));//D1_1
		bookMarkMap.put("D1_2", mapStr.get("D1_2"));//D1_2
		bookMarkMap.put("D1_3", mapStr.get("D1_3"));//D1_3
		bookMarkMap.put("D2", mapStr.get("D2"));//D2
		bookMarkMap.put("D2_1", mapStr.get("D2_1"));//D2_1
		bookMarkMap.put("D2_2", mapStr.get("D2_2"));//D2_2
		bookMarkMap.put("D2_3", mapStr.get("D2_3"));//D2_3
		bookMarkMap.put("D2_4", mapStr.get("D2_4"));//D2_4
		bookMarkMap.put("D2_5", mapStr.get("D2_5"));//D2_5
		bookMarkMap.put("D2_6", mapStr.get("D2_6"));//D2_6
		bookMarkMap.put("D2_7", mapStr.get("D2_7"));//D2_7
		bookMarkMap.put("D3", mapStr.get("D3"));//D3
		bookMarkMap.put("D3_1", mapStr.get("D3_1"));//D3_1
		bookMarkMap.put("D3_2", mapStr.get("D3_2"));//D3_2
		bookMarkMap.put("D3_3", mapStr.get("D3_3"));//D3_3
		bookMarkMap.put("D3_4", mapStr.get("D3_4"));//D3_4
		bookMarkMap.put("D4", mapStr.get("D4"));//D4
		bookMarkMap.put("D4_1", mapStr.get("D4_1"));//D4_1
		bookMarkMap.put("D4_2", mapStr.get("D4_2"));//D4_2
		bookMarkMap.put("D4_3", mapStr.get("D4_3"));//D4_3
		bookMarkMap.put("D4_4", mapStr.get("D4_4"));//D4_4
		bookMarkMap.put("D5", mapStr.get("D5"));//D5
		bookMarkMap.put("D5_1", mapStr.get("D5_1"));//D5_1
		bookMarkMap.put("D5_2", mapStr.get("D5_2"));//D5_2
		bookMarkMap.put("D5_3", mapStr.get("D5_3"));//D5_3
		bookMarkMap.put("D5_4", mapStr.get("D5_4"));//D5_4
		bookMarkMap.put("D6", mapStr.get("D6"));//D6
		bookMarkMap.put("D6_1", mapStr.get("D6_1"));//D6_1
		bookMarkMap.put("D6_2", mapStr.get("D6_2"));//D6_2
		bookMarkMap.put("D6_3", mapStr.get("D6_3"));//D6_3
		bookMarkMap.put("D6_4", mapStr.get("D6_4"));//D6_4
		bookMarkMap.put("D7", mapStr.get("D7"));//D7
		bookMarkMap.put("D7_1", mapStr.get("D7_1"));//D7_1
		bookMarkMap.put("D7_2", mapStr.get("D7_2"));//D7_2
		bookMarkMap.put("D7_3", mapStr.get("D7_3"));//D7_3
		bookMarkMap.put("D7_4", mapStr.get("D7_4"));//D7_4
		bookMarkMap.put("D8", mapStr.get("D8"));//D8
		bookMarkMap.put("D8_1", mapStr.get("D8_1"));//D8_1
		bookMarkMap.put("D8_2", mapStr.get("D8_2"));//D8_2
		bookMarkMap.put("D8_3", mapStr.get("D8_3"));//D8_3
		bookMarkMap.put("D9", mapStr.get("D9"));//D9
		bookMarkMap.put("D9_1", mapStr.get("D9_1"));//D9_1
		bookMarkMap.put("D9_2", mapStr.get("D9_2"));//D9_2
		bookMarkMap.put("D10", mapStr.get("D10"));//D10
		bookMarkMap.put("D10_1", mapStr.get("D10_1"));//D10_1
		bookMarkMap.put("D11", mapStr.get("D11"));//D11
//		bookMarkMap.put("D11_1", mapStr.get("D11_1"));//D11_1
		bookMarkMap.put("A1", mapStr.get("A1"));//A1
		bookMarkMap.put("A2", mapStr.get("A2"));//A2
		bookMarkMap.put("A2_1", mapStr.get("A2_1"));//A2_1
		bookMarkMap.put("A3", mapStr.get("A3"));//A3
		bookMarkMap.put("A4", mapStr.get("A4"));//A4
		bookMarkMap.put("A5", mapStr.get("A5"));//A5
		bookMarkMap.put("A5_1", mapStr.get("A5_1"));//A5_1
		bookMarkMap.put("A6", mapStr.get("A6"));//A6
		bookMarkMap.put("A7", mapStr.get("A7"));//A7
		bookMarkMap.put("A8", mapStr.get("A8"));//A8
		bookMarkMap.put("A9", mapStr.get("A9"));//A9
//		bookMarkMap.put("A9_1", mapStr.get("A9_1"));//A9_1
		bookMarkMap.put("FOR_C", mapStr.get("FOR_C"));//FOR_C
		bookMarkMap.put("OTHERTERM_C", mapStr.get("OTHERTERM_C"));//OTHERTERM_C
		bookMarkMap.put("GYSNAME", mapStr.get("GYSNAME"));//GYSNAME
		bookMarkMap.put("VendorCode", mapStr.get("VendorCode"));//VendorCode
		bookMarkMap.put("sylcje", mapStr.get("sylcje"));//sylcje
		bookMarkMap.put("DXJE", mapStr.get("DXJE"));//DXJE
		bookMarkMap.put("GYSADDRESS", mapStr.get("GYSADDRESS"));//GYSADDRESS
		bookMarkMap.put("D2_8", mapStr.get("D2_8"));//D2_8
		bookMarkMap.put("YHZH", mapStr.get("YHZH"));//YHZH
		bookMarkMap.put("YHMC", mapStr.get("YHMC"));//YHMC
		bookMarkMap.put("YHDZ", mapStr.get("YHDZ"));//YHDZ
		bookMarkMap.put("BZ", mapStr.get("BZ"));//BZ
		String str1 = mapStr.get("D11_1").replace("&nbsp;", " ");
		String str2 = mapStr.get("A9_1").replace("&nbsp;", " ");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowDate = format.format(new Date());
		String outFileUrl="D:\\WEAVER\\ecology\\copyworldpage\\"+nowDate+"LC流程打印模板.docx";
//		String newoutFileUrl="D:\\WEAVER\\ecology\\copyworldpagenew\\"+nowDate+"LC流程打印模板.docx";
		try {
			modifyDocumentAndSave("D:\\WEAVER\\ecology\\worldpage\\LC流程打印模板.docx",bookMarkMap,outFileUrl,tableName,requestid, str1, str2);
		} catch (Exception e) {
			log.writeLog("生成模板失败 requestid "+requestid);
			log.writeLog(e);
			return "-1";
		}
//		try {
//			InputStream is = new FileInputStream(outFileUrl);
//			try {
//				HWPFDocument doc = new HWPFDocument(is);
//				Range range = doc.getRange();  
//				range.replaceText("${param2}", ""+(char)11);
//				OutputStream os = new FileOutputStream(newoutFileUrl);  
//			    doc.write(os); 
//			    os.close();
//			    is.close();
//				doc.close();
//			} catch (IOException e) {
//				log.writeLog("異常1----"+e.getMessage());
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
//			
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			log.writeLog("異常2----"+e1.getMessage());
//		}
		String docid= "-1";
		try {
			log.writeLog("开始创建OA文档  copyurl:"+outFileUrl+" requestid:"+requestid+" filename:2020LC流程打印模板 ----");
			docid=createDoc(outFileUrl,requestid,nowDate+"LC流程打印模板","3");
		    log.writeLog("创建文档结束 docid:"+docid);
		} catch (Exception e) {
			log.writeLog("生成文档失败 requestid "+requestid+":"+e.getMessage());
			return "-1";
		}
//		return docid;
		return docid;
	}
	/**
	 * 往合同模板填写数据
	 * @param FileUrl 合同模板路径
	 * @param bkMap 标签map
	 * @param outFileUrl 生成文件路径
	 * @param tableName 流程表名
	 * @param requestid 流程id
	 * @throws Exception
	 */
	public  void modifyDocumentAndSave(String FileUrl,Map<String, String> bkMap,String outFileUrl,String tableName,String requestid,String str1,String str2) throws Exception {
		// 使用java.util打开文件
		File file = new File(FileUrl);
		boolean exist = file.exists();
		boolean read = file.canRead();
		boolean write = file.canWrite();
		//D:\\test\\2017年AFH订制品合同模板.docx
		ZipFile docxFile = new ZipFile(file);
		// 返回ZipEntry应用程序接口
		ZipEntry documentXML = docxFile.getEntry("word/document.xml");

		InputStream documentXMLIS = docxFile.getInputStream(documentXML);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document doc = dbf.newDocumentBuilder().parse(documentXMLIS);
		Map<String, String> bookMarkMap = bkMap;


		/**
		 * 书签列表
		 */

		Element child1=null;
		Element child2=null;
		NodeList this_book_list = doc.getElementsByTagName("w:bookmarkStart");
		if (this_book_list.getLength() != 0) {
			for (int j = 0; j < this_book_list.getLength(); j++) {
				// 获取每个书签
				Element oldBookStart = (Element) this_book_list.item(j);
				// 书签名
				String bookMarkName = oldBookStart.getAttribute("w:name");
				//明细（插表）
				if ("A9_1".equals(bookMarkName)) {
                   // child1 = (Element) oldBookStart.getParentNode().getNextSibling();
                    child2 = (Element) oldBookStart.getParentNode();
//                    System.out.println(child1.getElementsByTagName("w:t").item(0).getTextContent());
                }
				if ("D11_1".equals(bookMarkName)) {
	                   // child1 = (Element) oldBookStart.getParentNode().getNextSibling();
	                    child1 = (Element) oldBookStart.getParentNode();
//	                    System.out.println(child1.getElementsByTagName("w:t").item(0).getTextContent());
	                }
				// 书签名，跟需要替换的书签传入的map集合比较
				for (Map.Entry<String, String> entry : bookMarkMap.entrySet()) {
					// 书签处值开始
					Node wr = doc.createElement("w:r");
					Node wt = doc.createElement("w:t");
					Node wt_text = doc.createTextNode(entry.getValue());
					wt.appendChild(wt_text);
					wr.appendChild(wt);
					// 书签处值结束
					if (entry.getKey().equals(bookMarkName)) {
						Element node = (Element) oldBookStart.getNextSibling();// 获取兄弟节点w:r
						// 如果书签处无文字,则在书签处添加需要替换的内容，如果书签处存在描述文字，则替换内容,用w:r
						NodeList wtList = node.getElementsByTagName("w:t");// 获取w:r标签下的显示书签处内容标签w:t
						if (wtList.getLength() == 0) { // 如果不存在，即，书签处本来就无内容，则添加需要替换的内容
							oldBookStart.appendChild(wr);
						} else { // 如果书签处有内容，则直接替换内容
							Element wtNode = (Element) wtList.item(0);
							wtNode.setTextContent(entry.getValue());
						}

					}
				}

			}
			doDetail(child1,child2,str1,str2);
		}

		Transformer t = TransformerFactory.newInstance().newTransformer();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		t.transform(new DOMSource(doc), new StreamResult(baos));
		//D:\\test\\response.docx
		ZipOutputStream docxOutFile = new ZipOutputStream(new FileOutputStream(
				outFileUrl));
		Enumeration entriesIter = docxFile.entries();
		while (entriesIter.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entriesIter.nextElement();
			// 如果是document.xml则修改，别的文件直接拷贝，不改变word的样式
			if (entry.getName().equals("word/document.xml")) {
				byte[] data = baos.toByteArray();
				docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
				docxOutFile.write(data, 0, data.length);
				docxOutFile.closeEntry();
			} else {
				InputStream incoming = docxFile.getInputStream(entry);
				   ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
				    byte[] buffer = new byte[1024];  
				    int len = -1;  
				    while ((len = incoming.read(buffer)) != -1) {  
				        outSteam.write(buffer, 0, len);  
				    }  
				    outSteam.close();  
				    incoming.close();  
			
				docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
				docxOutFile.write(outSteam.toByteArray(), 0,(int) entry.getSize());
				docxOutFile.closeEntry();
			}
		}
		docxOutFile.close();
	}
	/**
	 * 根据url读取文件数据创建系统文档
	 * @param fileUrl
	 * @param requestid
	 * @param filename
	 * @param createrid
	 * @return
	 * @throws Exception
	 */
	public String createDoc(String fileUrl,String requestid,String filename,String createrid) {
		RecordSet rs = new RecordSet();
//		String ml = "";
//		String sql= "select from workflow_base where id = (select workflowid from workflow_requestbase where requestid = '"+requestid+"')";
//		rs.execute(sql);
//		if(rs.next()) ml = rs.getString("");
		String doccategory = "94";//测试机 84  正式机  94 
		String docid="";
//		log.writeLog("fileUrl----"+fileUrl);
		String uploadBuffer="";
		try {
			FileInputStream fi = new FileInputStream(new File(fileUrl));
			  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			    byte[] buffer = new byte[1024]; 
			    int count = 0; 
			    while((count = fi.read(buffer)) >= 0){ 
			        baos.write(buffer, 0, count); 
			    } 
			    uploadBuffer = new String(Base64.encode(baos.toByteArray()));
			    baos.close();
			   log.writeLog("uploadBuffer:"+uploadBuffer.length());
			 docid=getDocId(filename,uploadBuffer,createrid,doccategory);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog("异常1----"+e.getMessage());
		} catch (IOException e) {
			log.writeLog("异常2----"+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog("异常3----"+e.getMessage());
		}   
		return docid;
	}
	/**
	 * 创建文档
	 * @param name 文档名称
	 * @param value base64位加密的字符串
	 * @param createrid 文档创建人
	 * @param seccategory 文档存放目录
	 * @return
	 * @throws Exception
	 */
	private String getDocId(String name, String value,String createrid,String seccategory)  {
		String docId = "";
		DocInfo di= new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));	
		di.setDocSubject(name);	
		//di.setDoccontent(arg0);
		DocAttachment doca = new DocAttachment();
		doca.setFilename(name+".docx");
		byte[] buffer = null;
		try {
			buffer = new BASE64Decoder().decodeBuffer(value);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String encode=Base64.encode(buffer);
		doca.setFilecontent(encode);
		
		DocAttachment[] docs= new DocAttachment[1];
		docs[0]=doca;
		di.setAttachments(docs);
		String departmentId="-1";
		String sql="select departmentid from hrmresource where id="+createrid;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		User user = new User();
		if(rs.next()){
			departmentId = Util.null2String(rs.getString("departmentid"));
		}	
		user.setUid(Integer.parseInt(createrid));
		user.setUserDepartment(Integer.parseInt(departmentId));
		user.setLanguage(7);
		user.setLogintype("1");
		user.setLoginip("127.0.0.1");
		DocServiceImpl ds = new DocServiceImpl();
		try {
			docId=String.valueOf(ds.createDocByUser(di, user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return docId;
	}
	
	public  void doDetail(Element child1,Element child2,String str1,String str2 ){
        int count1=0;
        int count2=0;
        Element root=(Element)child1.getParentNode();
        Element root2=(Element)child2.getParentNode();
        Element child = child1;
        Element child22 = child2;
        String str1s [] = str1.split("@@@");
        String str2s [] = str2.split("@@@");
        for(String nr:str1s){
            if(count1==0){
                NodeList wtList=child1.getElementsByTagName("w:t");
                    Element wtNode = (Element) wtList.item(0);
                    wtNode.setTextContent(nr);
            }
            else{
                root.appendChild(child1.cloneNode(true));
                Element clone=(Element)child.getNextSibling();
                NodeList wtList=clone.getElementsByTagName("w:t");
                Element wtNode = (Element) wtList.item(0);
                wtNode.setTextContent(nr);
                child = clone;
            }
            count1++;
        }
        if(count1==0){
            NodeList wtList=child1.getElementsByTagName("w:t");
            Element wtNode = (Element) wtList.item(0);
            wtNode.setTextContent("");
        }
        for(String nr:str2s){
            if(count2==0){
                NodeList wtList=child2.getElementsByTagName("w:t");

                    Element wtNode = (Element) wtList.item(0);
                    wtNode.setTextContent(nr);
            }
            else{
            	root2.appendChild(child2.cloneNode(true));
                Element clone=(Element)child22.getNextSibling();
                NodeList wtList=clone.getElementsByTagName("w:t");
                Element wtNode = (Element) wtList.item(0);
                wtNode.setTextContent(nr);
                child22 = clone;
            }
            count2++;
        }
        if(count2==0){
            NodeList wtList=child2.getElementsByTagName("w:t");
            Element wtNode = (Element) wtList.item(0);
            wtNode.setTextContent("");
        }

    }
	

}