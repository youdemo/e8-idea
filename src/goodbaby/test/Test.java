package goodbaby.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.informix.util.stringUtil;

import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;

public class Test {
	public static void main(String args[]){
		String aa = "201807_1_92";
		aa.split("_");
		System.out.println(aa.split("_")[0]);
		System.out.println(new Date().getTime());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String nowyear = sf.format(new Date());
		System.out.println(nowyear);
		String scids="123,12,";
		String fkids[] = scids.split(",");
		
		for(String fkid:fkids) {
			System.out.println(fkid);
		}
	}
	private String getDocId(String name, String value,String createrid,String seccategory) throws Exception {
		String docId = "";
		DocInfo di= new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));	
		di.setDocSubject(name.substring(0, name.lastIndexOf(".")));	
		DocAttachment doca = new DocAttachment();
		doca.setFilename(name);
		byte[] buffer = new BASE64Decoder().decodeBuffer(value);
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
			e.printStackTrace();
		}
		
		return docId;
	}
}
