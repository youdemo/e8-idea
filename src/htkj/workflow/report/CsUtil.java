package htkj.workflow.report;

/**
 * createby jianyong.tang
 * createTime 2020/7/26 17:30
 * version v1
 * desc 超时Util
 */
public class CsUtil {

    public String csTransView(String nodeid,String lastoperatedate){
        String result = "<a href=\"javascript:showrequest('"+nodeid+"','"+lastoperatedate+"')\">查看</a>";
        return result;
    }
}
