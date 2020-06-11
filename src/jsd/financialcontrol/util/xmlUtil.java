package jsd.financialcontrol.util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;

/** 
* @author 作者  张瑞坤
* @date 创建时间：2020年5月19日 下午6:54:23 
* @version 1.0 
*/
public class xmlUtil {
	public static HashMap<String, String> stringToXmlByDom4j(String content){
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            SAXReader saxReader=new SAXReader();
            org.dom4j.Document docDom4j=saxReader.read(new ByteArrayInputStream(content.getBytes("utf-8")));
            org.dom4j.Element root = docDom4j.getRootElement();
            List<Attribute> rooAttrList = root.attributes();
            for (Attribute rootAttr : rooAttrList) {
//                System.out.println(rootAttr.getName() + ": " + rootAttr.getValue());
                result.put(rootAttr.getName(), rootAttr.getValue());
            }
            List<org.dom4j.Element> childElements = root.elements();
            for (org.dom4j.Element e1 : childElements) {
//                System.out.println("第一层："+e1.getName() + ": " + e1.getText());
                result.put(e1.getName(), e1.getText());
            }
            for (org.dom4j.Element child : childElements) {
                //未知属性名情况下
                List<Attribute> attributeList = child.attributes();
                for (Attribute attr : attributeList) {
//                    System.out.println("第二层："+attr.getName() + ": " + attr.getValue());
                    result.put(attr.getName(), attr.getValue());
                }
                //未知子元素名情况下
                List<org.dom4j.Element> elementList = child.elements();
                for (org.dom4j.Element ele : elementList) {

//                    System.out.println("第二层："+ele.getName() + ": " + ele.getText());
                    result.put(ele.getName(), ele.getText());
                    List<Attribute> kidAttr = ele.attributes();
                    for (Attribute kidattr : kidAttr) {
//                        System.out.println("第三层："+kidattr.getName() + ": " + kidattr.getValue());
                        result.put(kidattr.getName(), kidattr.getValue());
                    }
                    List<org.dom4j.Element> lidList = ele.elements();
                    int size = lidList.size();
                    if(size>0){
                        for (org.dom4j.Element e2 : lidList) {
//                            System.out.println("第三层："+e2.getName() + ": " + e2.getText());
                            result.put(e2.getName(), e2.getText());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public static void main(String[] args) {
	    HashMap<String, String> stringObjectHashMap = stringToXmlByDom4j("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><trans_content>\n" +
	            "  <trans_reqDatas>\n" +
	            "    <trans_reqData>\n" +
	            "      <trans_orderid>19242271</trans_orderid>\n" +
	            "      <trans_batchid>23016137</trans_batchid>\n" +
	            "      <trans_no>XHKS201901231602211127_4</trans_no>\n" +
	            "      <trans_money>200.00</trans_money>\n" +
	            "      <to_acc_name>王宝</to_acc_name>\n" +
	            "      <to_acc_no>622848044445555333323012012</to_acc_no>\n" +
	            "      <trans_fee>1.00</trans_fee>\n" +
	            "      <state>-1</state>\n" +
	            "      <trans_remark>交易失败，请核实信息并联系发卡行确认！</trans_remark>\n" +
	            "      <trans_starttime>20190125101628</trans_starttime>\n" +
	            "      <trans_endtime>20190125101635</trans_endtime>\n" +
	            "    </trans_reqData>\n" +
	            "  </trans_reqDatas>\n" +
	            "</trans_content>");
	    System.out.println(stringObjectHashMap);

	}
}
