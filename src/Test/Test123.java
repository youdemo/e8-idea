package Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * createby jianyong.tang
 * createTime 2020/4/10 16:34
 * version v1
 * desc
 */
public class Test123 {
    public static void main(String[] args) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = sf.format(new Date());
        System.out.println(nowdate);
    }
}
