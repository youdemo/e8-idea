package knjr.u8djoa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;


public class ScPz {
    static String from_account="alphamab";//调用方id
    static String to_account="alphamab2019";//提供方id
    static String app_key="opa97ea222a0c0f17b4";//应用编码
    static Integer sync=0;//0=异步新增（默认）;1=同步新增
    static String app_secret="1e67b904360746c083539072e64c7514";
    static String token = GetSecurity.getToken(app_secret);
    static String tradeid = GetSecurity.getTradeid(token);
    static String url = "https://api.yonyouup.com/api/voucher/add?from_account="+from_account+"&app_key="+app_key+"&to_account="+to_account+"&token="+token+"&tradeid="+tradeid+"&sync="+sync;

    public static String scpz(String sql){
        BaseBean log = new BaseBean();
        try {
            RecordSet rs = new RecordSet();
            String yggh ="";//员工工号
            String reqdept ="";//部门
            String zdrgh ="";//制单人工号
            String creditsubj ="";//贷方科目
            String debitsubj ="";//借方科目
            String nf ="";//年份
            String paydate ="";//付款日期
            String debitmt ="";//借方金额
            String creditmt ="";//贷方金额
            String zyyt ="";//摘要
            String cCashItem = "";//现金流量
            String Exdepartment = "";//费用部门
            log.writeLog("==========scpz  sql:  "+sql+"===========================");
            rs.execute(sql);
            while(rs.next()){
                yggh = Util.null2String(rs.getString("yggh"));//员工工号
                reqdept = Util.null2String(rs.getString("reqdept"));//部门
                zdrgh = Util.null2String(rs.getString("zdrgh"));//制单人工号
                creditsubj = Util.null2String(rs.getString("creditsubj"));//贷方科目
                debitsubj = Util.null2String(rs.getString("debitsubj"));//借方科目
                nf = Util.null2String(rs.getString("nf"));//年份
                paydate = Util.null2String(rs.getString("paydate"));//付款日期
                debitmt = Util.null2String(rs.getString("debitmt"));//借方金额
                creditmt = Util.null2String(rs.getString("creditmt"));//贷方金额
                zyyt = Util.null2String(rs.getString("zyyt"));//摘要
                cCashItem = Util.null2String(rs.getString("cCashItem"));//现金流量
                Exdepartment = Util.null2String(rs.getString("Exdepartment"));//费用部门
            }
            JSONObject json = new JSONObject();
            JSONObject voucher = new JSONObject();
            voucher.put("voucher_type","记");
            voucher.put("enter",zdrgh);
            voucher.put("date",paydate);
            voucher.put("accounting_period","1");
            JSONObject credit = new JSONObject();
            JSONArray entry = new JSONArray();
            JSONObject two = new JSONObject();
            two.put("abstract",zyyt);
            two.put("account_code",creditsubj);
            JSONObject auxiliary = new JSONObject();
            auxiliary.put("dept_id",reqdept);
            auxiliary.put("personnel_id",yggh);
            two.put("auxiliary",auxiliary);
            JSONArray cash_flow = new JSONArray();
            JSONObject credit_cash = new JSONObject();
            credit_cash.put("iyear",nf);
            credit_cash.put("mc",creditmt);
            cash_flow.add(credit_cash);
            two.put("cash_flow",cash_flow);
            entry.add(two);
            credit.put("entry",entry);

            JSONObject debit = new JSONObject();
            JSONArray debitEntry = new JSONArray();
            JSONObject three = new JSONObject();
            three.put("abstract",zyyt);
            three.put("account_code",debitsubj);
            JSONArray debitcash_flow = new JSONArray();
            JSONObject debit_cash = new JSONObject();
            debit_cash.put("iyear",nf);
            debit_cash.put("md",debitmt);
            debit_cash.put("cash_item",cCashItem);
            if(!"".equals(Exdepartment)){
                debit_cash.put("cdept_id",Exdepartment);
            }
            debitcash_flow.add(debit_cash);
            three.put("cash_flow",debitcash_flow);
            debitEntry.add(three);
            debit.put("entry",debitEntry);
            voucher.put("debit",debit);
            voucher.put("credit",credit);
            json.put("voucher",voucher);
            log.writeLog("============scpz json: "+json.toString()+"===============");
            log.writeLog("==============scpz url: "+url+"===================================");
            String s = doPost(url, json.toJSONString());
            log.writeLog("=================scpz result:  "+s+"================================");
            JSONObject parse = (JSONObject) JSON.parse(s);
            String errmsg = (String) parse.get("errmsg");
            if(errmsg!=null && errmsg.contains("参数token已过期")){
                token = GetSecurity.getToken(app_secret);
                tradeid = GetSecurity.getTradeid(token);
                url = "https://api.yonyouup.com/api/voucher/add?from_account="+from_account+"&app_key="+app_key+"&to_account="+to_account+"&token="+token+"&tradeid="+tradeid+"&sync="+sync;
                s = doPost(url, json.toJSONString());
                log.writeLog("=================scpz result:  "+s+"================================");
                JSONObject parse1 = (JSONObject) JSON.parse(s);
                String url1 = (String) parse1.get("url");
                if(url1 == null){
                    token = GetSecurity.getToken(app_secret);
                    tradeid = GetSecurity.getTradeid(token);
                    url = "https://api.yonyouup.com/api/voucher/add?from_account="+from_account+"&app_key="+app_key+"&to_account="+to_account+"&token="+token+"&tradeid="+tradeid+"&sync="+sync;
                    s = doPost(url, json.toJSONString());
                    log.writeLog("=================scpz result:  "+s+"================================");
                    JSONObject parse2 = (JSONObject) JSON.parse(s);
                    String url2 = (String) parse2.get("url");
                    return url2;
                }
                log.writeLog("=================scpz resultUrl:  "+url1+"================================");
                return url1;
            }else{
                String url1 = (String) parse.get("url");
                if(url1 == null){
                    token = GetSecurity.getToken(app_secret);
                    tradeid = GetSecurity.getTradeid(token);
                    url = "https://api.yonyouup.com/api/voucher/add?from_account="+from_account+"&app_key="+app_key+"&to_account="+to_account+"&token="+token+"&tradeid="+tradeid+"&sync="+sync;
                    s = doPost(url, json.toJSONString());
                    log.writeLog("=================scpz result:  "+s+"================================");
                    JSONObject parse2 = (JSONObject) JSON.parse(s);
                    String url2 = (String) parse2.get("url");
                    return url2;
                }
                log.writeLog("=================scpz resultUrl:  "+url1+"================================");
                return url1;
            }
        }catch (Exception e) {
            log.writeLog(e);
            return null;
        }
    }
    /**
     * 执行请求
     * @param url
     * @param postData
     * @return String
     */
    public static String doPost(String url, String postData) {
        String result = null;
        HttpPost post = null;
        try {
            HttpClient client = new DefaultHttpClient();

            post = new HttpPost(url);

            post.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");
            post.setHeader("Accept", "application/json; charset=UTF-8");

            StringEntity entity = new StringEntity(postData, "UTF-8");
            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            int rspCode = response.getStatusLine().getStatusCode();
            System.out.println("rspCode:" + rspCode);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(post != null) {
                post.releaseConnection();
            }
        }
        return null;
    }
}
