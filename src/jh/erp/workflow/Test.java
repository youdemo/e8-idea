package jh.erp.workflow;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class Test {
	public static void main(String[] args) {
		String aaa = "<div><a </><img src=\"/weaver/weaver.file.FileDownload?fileid=117806\" title=\"\" alt=\"\"/>申请060-0007-090数量变更，数量增加1150PCS</div>";
		System.out.println(removeHtmlTag(aaa).replaceAll("<[^<>]*/>",""));

	}
	private static String removeHtmlTag(String content) {
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
		Matcher m = p.matcher(content);
		if (m.find()) {
			content = content
					.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
			content = removeHtmlTag(content);
		}

		return content;
	}
//	public static void main(String[] args) throws Exception {
//		String aaa = "123,22,555,43,11";
//		String aaas[] = aaa.split(",");
//		System.out.println(aaas[0]);
//		Arrays.sort(aaas);
//		for(String cc:aaas) {
//			System.out.println(cc);
//		}
//
//		System.out.println();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//		System.out.println(sdf.format(new Date()));
//		String res = "<response>\r\n" +
//				"  <srvver>1.0</srvver>\r\n" +
//				"  <srvcode>000</srvcode>\r\n" +
//				"  <payload>\r\n" +
//				"    <param key=\"data\" type=\"XML\">\r\n" +
//				"      <![CDATA[\r\n" +
//				"<Response>\r\n" +
//				"  <Execution>\r\n" +
//				"    <Status code=\"0\" sql_code=\"0\" description=\"执行成功！\"/>\r\n" +
//				"  </Execution>\r\n" +
//				"  <ResponseContent>\r\n" +
//				"    <Parameter/>\r\n" +
//				"    <Document/>\r\n" +
//				"  </ResponseContent>\r\n" +
//				"</Response>\r\n" +
//				"]]>\r\n" +
//				"    </param>\r\n" +
//				"  </payload>\r\n" +
//				"</response>";
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		Document doc = builder
//				.parse(new InputSource(new StringReader(res)));
//		NodeList list = doc.getElementsByTagName("param");
//		String resultxml =list.item(0).getTextContent();
//		doc = builder
//				.parse(new InputSource(new StringReader(resultxml)));
//
//		list = doc.getElementsByTagName("Status");
//		String code = list.item(0).getAttributes().getNamedItem("code").getTextContent();
//		String description = list.item(0).getAttributes().getNamedItem("description").getTextContent();
//		System.out.println("aaa description:"+description+" code:"+code);
//
//	}

}
