package weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import weaver.general.BaseBean;

public class GetJson {
	BaseBean log =new BaseBean();
	public  String postConnection(String url) {
		log.writeLog("url----"+url);
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();// 打开和URL之间的连接
           
            // 发送POST请求必须设置如下两行
            conn.setRequestMethod("POST"); // POST方法
            conn.setDoOutput(true);
            conn.setDoInput(true);
           
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0;   Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
           
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");// 获取URLConnection对象对应的输出流
            out.flush();// flush输出流的缓冲
           
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));//定义BufferedReader输入流来读取URL的响应
           
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
             //   System.out.println("OK");
            }
        } catch (Exception e) {
        	log.writeLog("发送 请求出现异常！"+e.getMessage());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
               if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        log.writeLog("result-----"+result);
        return result;
    }
	
	public static String getMethod(String url)  {
	 	BufferedReader br = null;			
		HttpURLConnection conn = null;
		  StringBuffer responseResult = new StringBuffer();
		try {
	    URL restURL = new URL(url);		 
	     conn = (HttpURLConnection) restURL.openConnection();
	 
	    conn.setRequestMethod("GET"); // POST GET PUT DELETE
	    conn.setRequestProperty("Accept", "application/json");
	 
	    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line;
	    while((line = br.readLine()) != null ){
	    	responseResult.append(line);
	    }
	 
	    br.close();
	    conn.disconnect();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 conn.disconnect();
		}
	    return responseResult.toString();
 }
	
	
}
