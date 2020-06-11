package jh.erp.sso;

import jh.erp.service.TIPTOPServiceGateWayStub;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import weaver.general.BaseBean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * createby jianyong.tang
 * createTime 2020/3/23 20:46
 * version v1
 * desc
 */
public class ERPSSO {
    public String getSendToErpParam(String workcode,String system,String CompanyId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String nowTime = sdf.format(new Date());
        String param = "<request type=\"sync\" key=\"76CFB4D975B57C166A5F93C79E19D933\"> \r\n" +
                "  <host prod=\"OA\" ver=\"1.0\" ip=\"10.10.20.10\" lang=\"zh_CN\" timezone=\"8\" timestamp=\""+sdf1.format(new Date())+"\" acct=\"tiptop\"/>  \r\n" +
                "    <service prod=\"T100\" name=\"oa.trust.get\" srvver=\"1.0\" id=\"00000\"/>   \r\n" +
                "  <datakey type=\"FOM\"> \r\n" +
                "    <key name=\"EntId\">88</key> \r\n" +
                "    <key name=\"CompanyId\">"+CompanyId+"</key> \r\n" +
                "  </datakey>  \r\n" +
                "  <payload> \r\n" +
                "    <param key=\"data\" type=\"XML\"><![CDATA[\r\n" +
                "<Request>\r\n" +
                " <RequestContent>\r\n" +
                "  <Parameter>\r\n" +
                " <Record>\r\n" +
                " <Field name=\"ip\" value=\"10.10.20.11\"/> \r\n" +
                " <Field name=\"region\" value=\""+system+"\"/> \r\n" +
                " <Field name=\"prog\" value=\"azzi000\"/>  \r\n" +
                " <Field name=\"startid\" value=\""+workcode+"\"/> \r\n" +
                " <Field name=\"productid\" value=\"BPM\"/>\r\n" +
                "    </Record>\r\n" +
                "    </Parameter>\r\n" +
                "     </RequestContent>\r\n" +
                "    </Request>]]> </param> \r\n" +
                "  </payload> \r\n" +
                "</request>\r\n";
        return param;
    }

    public String erpsso(String workcode,String system,String CompanyId){
        BaseBean log = new BaseBean();
        String url = "";
        String param = getSendToErpParam(workcode,system,CompanyId);
        try {
            log.writeLog("ERPSSO", "param:" + param);
            String result = sendToErp(param);
            log.writeLog("ERPSSO", "result:" + result);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder
                    .parse(new InputSource(new StringReader(result)));
            NodeList list = doc.getElementsByTagName("param");
            String resultxml =list.item(0).getTextContent();
            doc = builder
                    .parse(new InputSource(new StringReader(resultxml)));

            list = doc.getElementsByTagName("Status");
            String code = list.item(0).getAttributes().getNamedItem("code").getTextContent();
            String description = list.item(0).getAttributes().getNamedItem("description").getTextContent();
            list = doc.getElementsByTagName("Field");
            url = list.item(0).getAttributes().getNamedItem("value").getTextContent().replace("&amp;","&");
        }catch(Exception e){
            log.writeLog("ERPSSO", e);
        }
        return url;

    }
    public String  sendToErp(String param) throws Exception {
        TIPTOPServiceGateWayStub tpg = new TIPTOPServiceGateWayStub();
        TIPTOPServiceGateWayStub.InvokeSrv is = new TIPTOPServiceGateWayStub.InvokeSrv();
        is.setRequest(param);
        TIPTOPServiceGateWayStub.InvokeSrvResponse isr =tpg.invokeSrv(is);
        String resultxml = isr.getResponse().toString();

        return resultxml;

    }
}
