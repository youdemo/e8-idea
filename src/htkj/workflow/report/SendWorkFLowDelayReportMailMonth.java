package htkj.workflow.report;

import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFCell;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFRow;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFSheet;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFWorkbook;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.system.SystemComInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * createby jianyong.tang
 * createTime 2020/7/29 15:44
 * version v1
 * desc 每周发送上周的流程延期邮件
 */
public class SendWorkFLowDelayReportMailMonth extends BaseCronJob {
    public void execute() {
        BaseBean log = new BaseBean();
        log.writeLog(this.getClass().getName(),"开始定时发送月度流程延期邮件");
        //此处添加需要定时执行的代码
        String startDate = "";//开始日期
        String endDate = "";//结束日期
        RecordSet rs = new RecordSet();
        String emails = "";
        String flag = ",";
        String roleid = Util.null2o(weaver.file.Prop.getPropValue("wfyqemailjs", "jsid"));
        String sql = "";
        if("".equals(roleid)){
            log.writeLog(this.getClass().getName(),"配置的角色id没找到");
            return;
        }
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String gtimelast = sdf.format(c.getTime()); //上月
        int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
        endDate = sdf.format(c.getTime()); //上月最后一天
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");
        startDate = sdf2.format(c.getTime()); //上月第一天
        sql = "select distinct b.email from hrmrolemembers a,hrmresource b where a.resourceid=b.id and a.roleid in("+roleid+")";
        rs.execute(sql);
        while(rs.next()){
            String email = Util.null2String(rs.getString("email"));
            if(!"".equals(email)){
                emails = emails+flag+email;
            }
        }
        if("".equals(emails)){
            log.writeLog(this.getClass().getName(),"发件人为空");
            return;
        }
        new SendWorkFLowDelayReportMail().getSendData(startDate,endDate,emails);
        log.writeLog(this.getClass().getName(),"定时发送月度流程延期邮件结束");
    }


}
