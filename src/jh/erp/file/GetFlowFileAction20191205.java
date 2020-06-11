package jh.erp.file;

import jh.erp.util.TransUtil;
import org.apache.axis.encoding.Base64;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Description: 获取ERP附件
* @version: 
* @author: jianyong.tang
* @date: 2019年11月4日 下午5:50:55
 */
public class GetFlowFileAction20191205 implements Action{

	@Override
	public String execute(RequestInfo info) {
		TransUtil tu = new TransUtil();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = tu.getTableName(workflowID);
		String dir = tu.getFLowFileDir(workflowID);//附件目录
		String urlip = Util.null2o(weaver.file.Prop.getPropValue("erpconfig", "fileurl"));
		String creater = "";//创建人
		String fileurls = "";//附件地址
		String fjids = "";//附件ids
		tu.writeLog("GetFlowFileAction", "开始读取附件");
		String flag = "";
		String sql = "select * from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			fileurls = Util.null2String(rs.getString("pmdiuc063"));
		}
		tu.writeLog("GetFlowFileAction", "附件地址:"+fileurls);
		sql = "select creater from workflow_requestbase where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			creater = Util.null2String(rs.getString("creater"));
		}
		String urls[] = fileurls.split(";");
		for(String url:urls) {
			if("".equals(url)) {
				continue;
			}
			url=urlip+url;
			tu.writeLog("GetFlowFileAction", "url地址:"+url);
			String docid = getFile(url,creater,dir);
			if(!"".equals(docid)) {
				fjids = fjids + flag +docid;
				flag = ",";
			}
		}
		sql = "update "+tableName+" set fj='"+fjids+"' where requestid="+requestid;
		tu.writeLog("GetFlowFileAction", "sql:"+sql);
		rs.execute(sql);

		return SUCCESS;
	}

	public String getFile(String fileUrl,String creater,String dir) {
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		String docid="";
		try {
			URL url = new URL(fileUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent","Mozilla/4.0");
			connection.connect();
			in = connection.getInputStream();
			baos = new ByteArrayOutputStream(); 
			byte[] buffer = new byte[1024]; 
			int count = 0; 
			while((count = in.read(buffer)) >= 0){ 
			     baos.write(buffer, 0, count); 
		    } 
			String filevalue =Base64.encode(baos.toByteArray());
			String filename = fileUrl.substring(fileUrl.lastIndexOf("/")+1,fileUrl.length());
			docid = getDocId(filename,filevalue,creater,dir);
		}catch(Exception e) {
			new BaseBean().writeLog("GetFlowFileAction",e);
			new BaseBean().writeLog("GetFlowFileAction","fileUrl:"+fileUrl);
			try {
				in.close();
				baos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			in.close();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return docid;
	}
	
	private String getDocId(String name, String value,String createrid,String seccategory) throws Exception {
		String docId = "";
		DocInfo di= new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));	
		di.setDocSubject(name.substring(0, name.lastIndexOf(".")));	
		//di.setDoccontent(arg0);
		DocAttachment doca = new DocAttachment();
		doca.setFilename(name);
		//byte[] buffer = new BASE64Decoder().decodeBuffer(value);
		//String encode=Base64.encode(buffer);
		doca.setFilecontent(value);
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


}
