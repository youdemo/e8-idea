package zj.certificate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.cfimpl.DoFYFTImpl;
import zj.certificate.cfimpl.DoJZPZImpl;

import java.util.Map;
/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-5 上午9:09:26
 * 记账凭证流程 写入凭证
 */
public class CreateJZPZ {
	BaseBean log = new BaseBean();
	public static int interfaceseq=10;
	public void doCreate(String requestids) {
		interfaceseq=10;
		RecordSet rs = new RecordSet();
		GetSeqNum gsb = new GetSeqNum();
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		String now =sf.format(new Date());
		String companyName = "";
		String requestid[] = requestids.split(",");
		for (int i = 0; i < requestid.length; i++) {
			String rqid = requestid[i];
			if ("".equals(rqid) || "0".equals(rqid)) {
				continue;
			}
			String pch = "";
			if ("".equals(pch)) {
				String sql = "select b.subcompanyname  from v_create_certificate_jz a,HrmSubCompany b where a.gs=b.id and a.requestid="
						+ rqid;
				rs.executeSql(sql);
				if (rs.next()) {
					companyName = Util.null2String(rs.getString("subcompanyname"));
				}
				pch = gsb.getNum("", "zj_gl_interface_control", 4);
				insertControl(rqid,pch,companyName);
			}
			doRqid(rqid, pch,companyName,now);
		}
	}
	/**
	 * 插入
	 * @param rqid 流程ID
	 * @param pch 批次号
	 */
	public void insertControl(String rqid, String pch,String companyName) {
		RecordSetDataSource rs = new RecordSetDataSource("MPROD");
		String sql="";
		String bookid = "";
		if ("中晶科技".equals(companyName)) {
			bookid = "2";
		} else if ("中晶医疗".equals(companyName)) {
			bookid = "164";
		} else {
			bookid = "3";
		}
		int maxid = 1;
		sql = "select nvl(MAX(interface_run_id),90000000)+1 as maxid  from zj_gl_interface_control";
		log.writeLog("testaa+sql"+sql);
		rs.executeSql(sql);
		if (rs.next()) {
			maxid = rs.getInt("maxid");
		}
		sql = "insert into zj_gl_interface_control(JE_SOURCE_NAME,STATUS,INTERFACE_RUN_ID,GROUP_ID,SET_OF_BOOKS_ID) " +
				"values('Manual','S','"+maxid+"','"+pch+"','"+bookid+"')";
		rs.executeSql(sql);
		
	}
	public void doRqid(String rqid, String pch,String companyName,String now) {
		RecordSet rs = new RecordSet();
		String tableName = "";
		InsertUtil iu = new InsertUtil();
		String OracleBatchName = "";
		String workflowId = "";
		String bookid="";
		String SEGMENT1="";
		if ("中晶科技".equals(companyName)) {
			bookid = "2";
			SEGMENT1="02";
		} else if ("中晶医疗".equals(companyName)) {
			bookid = "164";
			SEGMENT1="29";
		} else {
			bookid = "3";
			SEGMENT1="19";
		}
		String sql = "select c.tablename ,b.id as workflowid from workflow_requestbase a,workflow_base b,workflow_bill c where a.workflowid=b.id and b.formid = c.id  and a.requestid="
				+ rqid;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
			workflowId = Util.null2String(rs.getString("workflowid"));
		}
		if("76".equals(workflowId)){
			sql = "select  OracleBatchName from "+tableName +" where requestid = '"+rqid+"'";
			rs.executeSql(sql);
			if (rs.next()) {
				OracleBatchName = Util.null2String(rs.getString("OracleBatchName"));
//				workflowId = Util.null2String(rs.getString("workflowid"));
			}
			Map<String, String> result= new DoJZPZImpl().DoJZPZ(tableName,rqid,bookid,pch,now,SEGMENT1);
			updateRecordInfo(rqid, pch);
			Map<String, String> mapStrseq = new HashMap<String, String>();
			mapStrseq.put("BATCH_NAME", OracleBatchName+"-OA"+pch);
			mapStrseq.put("JOURNAL_NAME", "OA"+pch);			
			iu.insertzj(mapStrseq, "ZJ_JOURNAL_SEQ");
		}else if("75".equals(workflowId)){
			Map<String, String> result= new DoFYFTImpl().DoFYFT(tableName,rqid,bookid,pch,now,SEGMENT1);
			updateRecordInfo(rqid,pch);
		}
		
		
		
		
	}
	public void updateRecordInfo(String requestid,String pch){
    	RecordSet rs = new RecordSet();
    	String sql="update request_fk_record set sfscpz='1',pch='"+pch+"' where requestid="+requestid;
    	rs.executeSql(sql);
    	
    }
}
