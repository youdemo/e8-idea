package knjr.u8djoa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScFkd {
    static String from_account="alphamab";//调用方id
    static String to_account="alphamab2019";//提供方id
    static String app_key="opaf9f7e67dc188feee";//应用编码
    static Integer sync=0;//0=异步新增（默认）;1=同步新增
    static String app_secret="67811b6be0774eed8a0f1e86940aac26";
    static String token = GetSecurity.getToken(app_secret);
    static String tradeid = GetSecurity.getTradeid(token);
    static String url = "https://api.yonyouup.com/api/pay/add?from_account="+from_account+"&app_key="+app_key+"&to_account="+to_account+"&token="+token+"&tradeid="+tradeid+"&sync="+sync;

    public static String scfkd (String sql) {

        String vouchcode = "";//单据编号
        String vouchdate = "";//日期
        String customercode = "";//供应商编码
        String balancecode = "";//结算方式编码
        String amount = "";//本币金额
        String originalamount = "";//原币金额
        String balanceitemcode = "";//结算科目编码
        String departmentcode = "";//部门编码
        String operator = "";//录入人
        String personcode = "";//业务员编码
        String digest = "";//摘要
        RecordSet rs = new RecordSet();
        BaseBean log = new BaseBean();
        try{
            log.writeLog("==================scfkd==sql: "+sql+"======================");
            rs.execute(sql);
            if(rs.next()){
                vouchcode = Util.null2String(rs.getString("flowno"));
                vouchdate = Util.null2String(rs.getString("reqdate"));
                customercode = Util.null2String(rs.getString("cCusVen"));
                balancecode = Util.null2String(rs.getString("cSSCode"));
                amount = Util.null2String(rs.getString("bbje"));
                originalamount = Util.null2String(rs.getString("ybje"));
                balanceitemcode = Util.null2String(rs.getString("Jskmcode"));
                departmentcode = Util.null2String(rs.getString("Bmcode"));
                operator = Util.null2String(rs.getString("Lrr"));
                personcode = Util.null2String(rs.getString("Ywycode"));
                digest = Util.null2String(rs.getString("zy"));
            }
            Map<String,Map<String,Object>> params = new HashMap<String, Map<String,Object>>();
            Map<String,Object> pay = new HashMap<String, Object>();
            pay.put("vouchdate",vouchdate);
            pay.put("vouchcode",vouchcode);
            pay.put("customercode",customercode);
            pay.put("balancecode",balancecode);
            pay.put("amount",amount);
            pay.put("originalamount",originalamount);
            pay.put("balanceitemcode",balanceitemcode);
            pay.put("departmentcode",departmentcode);
            pay.put("operator",operator);
            pay.put("personcode",personcode);
            pay.put("digest",digest);
            List<Map<String,String>> entry = new ArrayList<Map<String, String>>();
            Map<String,String> map = new HashMap<String,String>();
            map.put("customercode",customercode);
            map.put("originalamount",originalamount);
            map.put("amount",amount);
            map.put("departmentcode",departmentcode);
            map.put("personcode",personcode);
            map.put("digest",digest);
            entry.add(map);
            pay.put("entry",entry);
            params.put("pay",pay);
            Map<String,String> headers = new HashMap<String, String>();
            headers.put("Content-Type","application/json");
            String string = JSON.toJSONString(params);
            log.writeLog("scfkd json: "+string);
            log.writeLog("=============scfkd url: "+url+"================================");
            Map<String,String> parse = (Map<String, String>) JSONObject.parse(string);
            HttpClientResult httpClientResult = HttpClientUtils.doPost(url, headers, parse);
            String content = httpClientResult.getContent();
            log.writeLog("=====================scfkd content: "+content+"=========================================");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(content);
            return (String) jsonObject.get("url");
        }catch (Exception e) {
            log.writeLog(e);
            return null;
        }
    }

}
