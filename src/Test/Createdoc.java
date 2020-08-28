package Test;

import org.apache.axis.encoding.Base64;
import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;

/**
 * createby jianyong.tang
 * createTime 2020/7/4 8:16
 * version v1
 * desc
 */
public class Createdoc {
    /**
     * 创建文档
     * @param name 文档名称
     * @param value base64位加密的字符串
     * @param createrid 文档创建人
     * @param seccategory 文档存放目录
     * @return
     * @throws Exception
     */
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
        byte[] buffer = new BASE64Decoder().decodeBuffer(value);
        String encode= Base64.encode(buffer);
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

}
