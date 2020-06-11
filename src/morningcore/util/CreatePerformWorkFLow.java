package morningcore.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static morningcore.util.GetModeidUtil.getModeId;

public class CreatePerformWorkFLow{
    /**
     * @param billid  建模数据id
     * @param creater 创建人
     */
    BaseBean log = new BaseBean();

    public String CreatePerformance(String billid,String creater){
        RecordSet rs = new RecordSet();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        //主表
        String khzqnf = "";
        String jxssbm = "";
        String ygkhqz = "";
        String bmkhzer = "";
        String khzq = "";
        String fqrgh = "";
        String gwmc = "";
        String erjm = "";
        String sjbm = "";
        String ssbm = "";
        String manager = "";
        String sqbm = "";
        String sql = "";
        String fqr = "";
        String ygkhqzlj = "";
        //明细表1
        String zblyxm1 = "";
        String mxid1 = "";
        String zbkhzrr1 = "";
        String zbqz1 = "";
        String zpqzs1 = "";
        String zbmc1 = "";
        String zblx1 = "";
        String zbdyjmbx1 = "";
        String pjbzms1 = "";
        String test = "";
        //明细表2
        String zblb2 = "";
        String zbmc2 = "";
        String zbdy2 = "";
        String pjbzms2 = "";
        String zblyxm2 = "";
        String zbkhzrr2 = "";
        String mxid2 = "";
        String test2 = "";
        sql = "select * from uf_ygjxzbzdk where id = " + billid;
        rs.execute(sql);
        log.writeLog("sql1=" + sql);
        if(rs.next()){
            khzqnf = Util.null2String(rs.getString("khzqnf"));
            jxssbm = Util.null2String(rs.getString("jxssbm"));
            ygkhqz = Util.null2String(rs.getString("ygkhqz"));
            bmkhzer = Util.null2String(rs.getString("bmkhzer"));
            khzq = Util.null2String(rs.getString("khzq"));
            ygkhqzlj = Util.null2String(rs.getString("ygkhqzlj"));
            manager = Util.null2String(rs.getString("managerid"));
            erjm = Util.null2String(rs.getString("erjm"));
            sjbm = Util.null2String(rs.getString("sjbm"));
            gwmc = Util.null2String(rs.getString("gwmc"));
            fqrgh = Util.null2String(rs.getString("fqrgh"));
            fqr = Util.null2String(rs.getString("fqr"));
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        JSONArray dt1 = new JSONArray();
        JSONArray dt2 = new JSONArray();
        String result = "";
        String rqid = "-1";
        try{
            header.put("fqr",fqr);
            header.put("erjm",erjm);
            header.put("fqrgh",fqrgh);
            header.put("sjbm",sjbm);
            header.put("fqri",nowDate);
            header.put("ssbm",ssbm);
            header.put("sqrq",nowDate);
            header.put("xzyzdd",billid);
            header.put("manager",manager);
            header.put("gwmc",gwmc);
            header.put("sqbm",sqbm);
            header.put("khzqnf",khzqnf);
            header.put("bmkhzer",bmkhzer);
            header.put("ygkhqz",ygkhqz);
            header.put("ygkhqzlj",ygkhqzlj);

            header.put("khzq",khzq);
            header.put("jxssbm",jxssbm);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sql = "select * from uf_ygjxzbzdk_dt1 where mainid = " + billid;
        rs.execute(sql);
        log.writeLog("sql2=" + sql);
        while(rs.next()){
            JSONObject node = new JSONObject();
            zblyxm1 = Util.null2String(rs.getString("zblyxm"));
            mxid1 = Util.null2String(rs.getString("mxid"));
            zbkhzrr1 = Util.null2String(rs.getString("zbkhzrr"));
            zbqz1 = Util.null2String(rs.getString("zbqz"));
            zpqzs1 = Util.null2String(rs.getString("zpqzs"));
            zbmc1 = Util.null2String(rs.getString("zbmc"));
            zblx1 = Util.null2String(rs.getString("zblx"));
            zbdyjmbx1 = Util.null2String(rs.getString("zbdyjmbx"));
            pjbzms1 = Util.null2String(rs.getString("pjbzms"));
            test = Util.null2String(rs.getString("test"));
            try{
                node.put("zblyxm",zblyxm1);
                node.put("mxid",mxid1);
                node.put("zbkhzrr",zbkhzrr1);
                node.put("zbqz",zbqz1);
                node.put("zpqzs",zpqzs1);
                node.put("zbmc",zbmc1);
                node.put("zblx",zblx1);
                node.put("zbdyjmbx",zbdyjmbx1);
                node.put("pjbzms",pjbzms1);
                node.put("test",test);
                dt1.put(node);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        sql = "select * from uf_ygjxzbzdk_dt2 where mainid = " + billid;
        rs.execute(sql);
        log.writeLog("sql2=" + sql);
        while(rs.next()){
            JSONObject JSONObj = new JSONObject();
            zblb2 = Util.null2String(rs.getString("zblb"));
            zbmc2 = Util.null2String(rs.getString("zbmc"));
            zbdy2 = Util.null2String(rs.getString("zbdy"));
            pjbzms2 = Util.null2String(rs.getString("pjbzms"));
            zblyxm2 = Util.null2String(rs.getString("zblyxm"));
            zbkhzrr2 = Util.null2String(rs.getString("zbkhzrr"));
            mxid2 = Util.null2String(rs.getString("mxid"));
            test2 = Util.null2String(rs.getString("test"));
            try{
                JSONObj.put("zblb",zblb2);
                JSONObj.put("zbmc",zbmc2);
                JSONObj.put("zbdy",zbdy2);
                JSONObj.put("pjbzms",pjbzms2);
                JSONObj.put("zblyxm",zblyxm2);
                JSONObj.put("zbkhzrr",zbkhzrr2);
                JSONObj.put("mxid",mxid2);
                JSONObj.put("test",test2);
                dt2.put(JSONObj);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        try{
            details.put("DT1",dt1);
            details.put("DT2",dt2);
            jsonObject.put("HEADER",header);
            jsonObject.put("DETAILS",details);
        }catch(JSONException e){
            e.printStackTrace();
        }
        log.writeLog("json=" + jsonObject.toString());
        AutoRequestService au = new AutoRequestService();
        result = au.createRequest("1030",jsonObject.toString(),fqr,"0");
        JSONObject jo = null;
        try{
            jo = new JSONObject(result);
            log.writeLog("CreatePerformWorkFLow","流程创建结果：" + jo.toString());
        }catch(JSONException e){
            log.writeLog(e);
        }
        String OA_ID = null;
        try{
            OA_ID = jo.getString("OA_ID");
        }catch(JSONException e){
            e.printStackTrace();
        }
        log.writeLog("CreatePerformWorkFLow","创建记录：建模id:" + billid+" 流程id:"+OA_ID);
        if(Util.getIntValue(OA_ID,0) > 0){
            rqid = OA_ID;
            sql = "update uf_ygjxzbzdk set sfcf=0 where id=" + billid;
            rs.execute(sql);
        }else{
            rqid = "-1";
        }
        return rqid;
    }

    public boolean PerformanceSysn(String billid){
        RecordSet rs = new RecordSet();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String sql = "";
        //主表
        String fqr = "";
        String erjm = "";
        String fqrgh = "";
        String sjbm = "";
        String fqri = "";
        String ssbm = "";
        String sqrq = "";
        String bdbh = "";
        String manager = "";
        String gwmc = "";
        String khzqnf = "";
        String bmkhzer = "";
        String sqbm = "";

        String ygkhqz = "";
        String erjbmmc = "";
        String ygkhqzlj = "";
        String aqjb = "";
        String sjbmmc = "";
        String jxssbm = "";
        String rqid = "";
//        String sfcf = "";
        String khzq = "";
        String mainid = "";
        //明细表1
        String zblyxm1 = "";
        String mxid1 = "";
        String zbkhzrr1 = "";
        String zbqz1 = "";
        String zpqzs1 = "";
        String zbmc1 = "";
        String zblx1 = "";
        String zbdyjmbx1 = "";
        String pjbzms1 = "";
//        String test = "";
        //明细表2
        String zblb2 = "";
        String zbmc2 = "";
        String zbdy2 = "";
        String pjbzms2 = "";
        String zblyxm2 = "";
        String zbkhzrr2 = "";
        String mxid2 = "";
//        String test2 = "";
        sql = "select * from uf_jxzbzdlsk where id = " + billid;
        rs.execute(sql);
        log.writeLog("sql1=" + sql);
        if(rs.next()){
            erjm = Util.null2String(rs.getString("erjm"));
            sjbm = Util.null2String(rs.getString("sjbm"));
            fqrgh = Util.null2String(rs.getString("fqrgh"));
            fqr = Util.null2String(rs.getString("fqr"));
            fqri = Util.null2String(rs.getString("fqri"));
            ssbm = Util.null2String(rs.getString("ssbm"));
            sqrq = Util.null2String(rs.getString("sqrq"));
            bdbh = Util.null2String(rs.getString("bdbh"));
            manager = Util.null2String(rs.getString("manager"));
            gwmc = Util.null2String(rs.getString("gwmc"));
            khzqnf = Util.null2String(rs.getString("khzqnf"));
            bmkhzer = Util.null2String(rs.getString("bmkhzer"));
            sqbm = Util.null2String(rs.getString("sqbm"));
            ygkhqz = Util.null2String(rs.getString("ygkhqz"));
            erjbmmc = Util.null2String(rs.getString("erjbmmc"));
            ygkhqzlj = Util.null2String(rs.getString("ygkhqzlj"));
            aqjb = Util.null2String(rs.getString("aqjb"));
            sjbmmc = Util.null2String(rs.getString("sjbmmc"));
            jxssbm = Util.null2String(rs.getString("jxssbm"));
            rqid = Util.null2String(rs.getString("rqid"));
//            sfcf = Util.null2String(rs.getString("sfcf"));
            khzq = Util.null2String(rs.getString("khzq"));
        }
        String modeid = getModeId("uf_ygjxzbzdk");
        Map<String,String> map = new HashMap<String,String>();
        map.put("erjm",erjm);
        map.put("sjbm",sjbm);
        map.put("fqrgh",fqrgh);
        map.put("fqr",fqr);
        map.put("fqri",fqri);
        map.put("ssbm",ssbm);
        map.put("sqrq",sqrq);
        map.put("bdbh",bdbh);
        map.put("manager",manager);
        map.put("gwmc",gwmc);
        map.put("khzqnf",khzqnf);
        map.put("bmkhzer",bmkhzer);
        map.put("sqbm",sqbm);
        map.put("ygkhqz",ygkhqz);
        map.put("erjbmmc",erjbmmc);
        map.put("ygkhqzlj",ygkhqzlj);
        map.put("aqjb",aqjb);
        map.put("sjbmmc",sjbmmc);
        map.put("jxssbm",jxssbm);
        map.put("rqid",rqid);
        map.put("khzq",khzq);
        map.put("modedatacreatedate", nowDate);
        map.put("modedatacreater", fqr);//
        map.put("modedatacreatertype", "0");
        map.put("formmodeid", modeid);
        log.writeLog("map:" + map);
        InsertUtil tu = new InsertUtil();
        boolean result = tu.insert(map, "uf_ygjxzbzdk");
        sql = "select * from uf_ygjxzbzdk where rqid = " + rqid;
        rs.execute(sql);
        log.writeLog("sql2=" + sql);
        if(rs.next()){
            mainid = Util.null2String(rs.getString("id"));
        }
        sql = "select * from uf_jxzbzdlsk_dt1 where mainid = " + billid;
        rs.execute(sql);
        log.writeLog("sql3=" + sql);
        while(rs.next()){
            zblyxm1 = Util.null2String(rs.getString("zblyxm"));
            mxid1 = Util.null2String(rs.getString("mxid"));
            zbkhzrr1 = Util.null2String(rs.getString("zbkhzrr"));
            zbqz1 = Util.null2String(rs.getString("zbqz"));
            zpqzs1 = Util.null2String(rs.getString("zpqzs"));
            zbmc1 = Util.null2String(rs.getString("zbmc"));
            zblx1 = Util.null2String(rs.getString("zblx"));
            zbdyjmbx1 = Util.null2String(rs.getString("zbdyjmbx"));
            pjbzms1 = Util.null2String(rs.getString("pjbzms"));
            Map map1 = new HashMap<String,String>();
            map1.put("zblyxm",zblyxm1);
            map1.put("mainid",mainid);
            map1.put("mxid",mxid1);
            map1.put("zbkhzrr",zbkhzrr1);
            map1.put("zbqz",zbqz1);
            map1.put("zpqzs",zpqzs1);
            map1.put("zbmc",zbmc1);
            map1.put("zblx",zblx1);
            map1.put("zbdyjmbx",zbdyjmbx1);
            map1.put("pjbzms",pjbzms1);
            log.writeLog("map1:" + map1);
            tu = new InsertUtil();
            result = tu.insert(map1, "uf_ygjxzbzdk_dt1");
        }
        sql = "select * from uf_jxzbzdlsk_dt2 where mainid = " + billid;
        rs.execute(sql);
        log.writeLog("sql4=" + sql);
        while(rs.next()){
            zblb2 = Util.null2String(rs.getString("zblb"));
            zbmc2 = Util.null2String(rs.getString("zbmc"));
            zbdy2 = Util.null2String(rs.getString("zbdy"));
            pjbzms2 = Util.null2String(rs.getString("pjbzms"));
            zblyxm2 = Util.null2String(rs.getString("zblyxm"));
            zbkhzrr2 = Util.null2String(rs.getString("zbkhzrr"));
            mxid2 = Util.null2String(rs.getString("mxid"));
            Map map2 = new HashMap<String,String>();
            map2.put("zblb",zblb2);
            map2.put("zbmc",zbmc2);
            map2.put("zbdy",zbdy2);
            map2.put("pjbzms",pjbzms2);
            map2.put("zblyxm",zblyxm2);
            map2.put("zbkhzrr",zbkhzrr2);
            map2.put("mxid",mxid2);
            map2.put("mainid",mainid);
            log.writeLog("map2:" + map2);
            tu = new InsertUtil();
            result = tu.insert(map2, "uf_ygjxzbzdk_dt2");
        }
        if (!"".equals(mainid)) {
            ModeRightInfo ModeRightInfo = new ModeRightInfo();
            ModeRightInfo.editModeDataShare(Integer.valueOf(fqr), Integer.valueOf(modeid),
                    Integer.valueOf(mainid));
            sql = "update uf_jxzbzdlsk set sfcf=0 where id = " + billid;
            rs.execute(sql);
        }

        return result;
    }
}
