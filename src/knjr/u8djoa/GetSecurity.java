package knjr.u8djoa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import weaver.general.BaseBean;

import java.util.HashMap;
import java.util.Map;

public class GetSecurity {
    private static final ThreadLocal tl = new ThreadLocal();
    private static final Map<String,String> map = new HashMap<String, String>();
    private static final String from_account="alphamab";
    private static final String to_account="alphamab2019";
    private static final String app_key="opa97ea222a0c0f17b4";

    public static String getToken(String app_secret){
        Object tlMap = tl.get();
        if(tlMap!=null){
            Map<String,String> stringMap = (Map<String, String>) tlMap;
            String token = stringMap.get("token");
            String time = stringMap.get("time");

            if(!isTimeOut(Long.valueOf(time))){
                return token;
            }else{
                String url="https://api.yonyouup.com/system/token?from_account="+ from_account+"&app_key="+app_key+"&app_secret="+app_secret;
                HttpClientResult httpClientResult = null;
                try {
                    httpClientResult = HttpClientUtils.doGet(url);
                } catch (Exception e) {
                    new BaseBean().writeLog(e);
                }
                String content = httpClientResult.getContent();
                JSONObject parse = (JSONObject) JSON.parse(content);
                JSONObject tokendata= (JSONObject) parse.get("token");
                String getToken = (String) tokendata.get("id");
                map.put("token",getToken);
                map.put("time",""+System.currentTimeMillis());
                tl.set(map);
                return getToken;
            }
        }else{
            String url="https://api.yonyouup.com/system/token?from_account="+from_account+"&app_key="+app_key+"&app_secret="+app_secret;
            HttpClientResult httpClientResult = null;
            try {
                httpClientResult = HttpClientUtils.doGet(url);
            } catch (Exception e) {
                new BaseBean().writeLog(e);
            }
            String content = httpClientResult.getContent();
            JSONObject parse = (JSONObject) JSON.parse(content);
            JSONObject tokendata= (JSONObject) parse.get("token");
            String token = (String) tokendata.get("id");
            map.put("token",token);
            map.put("time",""+System.currentTimeMillis());
            tl.set(map);
            return token;
        }
    }
    public static String getTradeid(String token){
        String url="https://api.yonyouup.com/system/tradeid?from_account="+from_account+"&app_key="+app_key+"&token="+token;
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doGet(url);
        } catch (Exception e) {
            new BaseBean().writeLog(e);
        }
        String content = httpClientResult.getContent();
        JSONObject parse = (JSONObject) JSON.parse(content);
        JSONObject trade = (JSONObject) parse.get("trade");
        return (String)trade.get("id");
    }
    private static boolean isTimeOut(long oldTime){
        long currTime = System.currentTimeMillis();
        long difference = currTime - oldTime;
        if(difference<=6600000){
            return false;
        }else{
            return true;
        }
    }
}
