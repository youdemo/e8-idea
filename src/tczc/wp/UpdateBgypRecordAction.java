package tczc.wp;

import freemarker.template.SimpleDate;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * createby jianyong.tang
 * createTime 2020/5/5 11:53
 * version v1
 * desc 更新办公用品月末数据到明细表
 */
public class UpdateBgypRecordAction extends BaseCronJob {

    @Override
    public void execute() {
        BaseBean log = new BaseBean();
        log.writeLog("UpdateBgypRecordAction","开始定时更新物品库记录");
        updateData();
        log.writeLog("UpdateBgypRecordAction","定时更新物品库记录结束");
    }

    public void updateData(){
        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = sf.format(new Date());
        String lastenddate = "";
        String tst = "";//库存数量
        String dj = "";//单间
        String sl = "";//税率
        String sql_dt = "";
        String mainid = "";
        String sql = "select convert(varchar(100),DATEADD(ms,-3,DATEADD(mm, DATEDIFF(m,0,'"+nowdate+"'), 0)),23) as lastenddate";
        rs.execute(sql);
        if(rs.next()){
           lastenddate = Util.null2String(rs.getString("lastenddate"));

        }
        sql = "select * from uf_bgypk";
        rs.execute(sql);
        while(rs.next()){
            tst = Util.null2String(rs.getString("tst"));
            dj = Util.null2String(rs.getString("dj"));
            sl = Util.null2String(rs.getString("sl"));
            if("".equals(tst)){
                tst = "0";
            }
            if("".equals(dj)){
                dj = "0";
            }
            if("".equals(sl)){
                sl = "0";
            }
            mainid = Util.null2String(rs.getInt("id"));
            String billid = "";
            sql_dt = "select id from uf_bgypk_dt1 where mainid="+mainid+" and ymrq='"+lastenddate+"'";
            rs_dt.execute(sql_dt);
            if(rs_dt.next()){
                billid = rs_dt.getString("id");
            }
            if(!"".equals(billid)){
                sql_dt =  "update uf_bgypk_dt1 set dj="+dj+",sl="+sl+",kcsl="+tst+" where mainid="+mainid+" and ymrq='"+lastenddate+"'";
                rs_dt.execute(sql_dt);
            }else{
                sql_dt = "insert into uf_bgypk_dt1(mainid,dj,sl,kcsl,ymrq) values("+mainid+","+dj+","+sl+","+tst+",'"+lastenddate+"')";
                rs_dt.execute(sql_dt);
            }
        }
    }
}
