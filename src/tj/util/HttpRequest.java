package tj.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import weaver.general.BaseBean;

public class HttpRequest {
	BaseBean log =new BaseBean();
	 public  String sendPost(String url, String param) {
	        OutputStreamWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            //1.获取URLConnection对象对应的输出流
//	            out = new PrintWriter(conn.getOutputStream());
//	            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));//有中文的时候需要设定编码
	            //2.中文有乱码的需要将PrintWriter改为如下
	            out=new OutputStreamWriter(conn.getOutputStream(),"utf-8");
	            // 发送请求参数
//	            out.print(param);
	            out.append(param);//写到body里面
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));//设定输出编码
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
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
	        System.out.println("post推送结果："+result);
	        return result;
	 }
	public static void main(String[] args) {
		 //发送 POST 请求
		HttpRequest hr= new HttpRequest();
//        String sr=hr.sendPost("http://dg.txcallme.com:8088/Api/ThirdPartyApi/FormAdd", "{\"menuID\":\"49aa1c27-79c7-4476-9a16-9b123eed41d7\", \"data\":\"{\\\"BeginTime\\\":\\\"11:00\\\", \\\"EndTime\\\":\\\"12:00\\\", \\\"LeaveRecordNo\\\":null, \\\"BeginDate\\\":\\\"2019-06-06\\\", " +
//        		"\\\"EndDate\\\":\\\"2019-06-06\\\", \\\"LeaveHours\\\":1, \\\"IsFreeTime\\\":true, \\\"Remark\\\":\\\"44\\\", \\\"LeaveType_Key\\\":\\\"e16eb9fb-6cc4-469e-8216-d2b9c91016a1\\\", \\\"Emp_Key\\\":\\\"2c227a5f-ccc3-4e27-8bb9-f3b54a3b2532\\\"}\", \"formtype\":\"请假\" }","LTAINd3g7bQbcsn4pGyhHtqYPR8q0nwgVpIR2AGlc2pYcq");
//        String sr=hr.sendPost("http://127.0.0.1:8080/jsd/txhr/procedure/JSD/trigger/post", "[{\"DETAILS\":{},\"HEADER\":{\"cjr\":\"KSPD001\",\"bz\":\"tetstettttcsdsd\"},\"CREATERCODE\":\"KSPD001\"}]","LTAINd3g7bQbcsn4pGyhHtqYPR8q0nwgVpIR2AGlc2pYcq");
        
//		
		String sr = hr.sendPost(HttpConstant.OATOHR_URL,"{\"head\":[{\"FRequesterID\":\"\",\"FDeptId\":\"\",\"FIsWaiGou\":\"内购\",\"FCaiGouBillNo\":\"TGF-1-ML-19-502\",\"FBillerId\":\"\",\"FDate\":\"2019-10-23\"}],\"typecode\":\"70\",\"body\":[{\"FQty\":\"50\",\"FFetchtime\":\"2019-10-31\",\"FAPurchTime\":\"2019-10-30\",\"FEntryID\":\"1\",\"FUnitId\":\"\",\"FItemID\":\"21648\",\"FPrice\":\"426.5487\",\"FAmount\":\"21327.43\"},{\"FQty\":\"200\",\"FFetchtime\":\"2019-10-31\",\"FAPurchTime\":\"2019-10-30\",\"FEntryID\":\"2\",\"FUnitId\":\"\",\"FItemID\":\"21484\",\"FPrice\":\"6.1947\",\"FAmount\":\"1238.94\"}]}");
		System.out.println(sr);
		
	}
	
}
