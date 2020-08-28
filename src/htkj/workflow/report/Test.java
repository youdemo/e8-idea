package htkj.workflow.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * createby jianyong.tang
 * createTime 2020/8/10 19:14
 * version v1
 * desc
 */
public class Test {
    public static void main(String[] args) {
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String gtimelast = sdf.format(c.getTime()); //上月
        int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
        String gtime = sdf.format(c.getTime()); //上月最后一天
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");
        String gtime2 = sdf2.format(c.getTime()); //上月第一天

    }
}
