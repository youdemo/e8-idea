package jsd.financialcontrol.util;

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
	        PrintWriter out = null;
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
	            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            //1.获取URLConnection对象对应的输出流
//	            out = new PrintWriter(conn.getOutputStream());
	            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));//有中文的时候需要设定编码
	            //2.中文有乱码的需要将PrintWriter改为如下
	            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
	            // 发送请求参数
	            out.print(param);
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
//	        System.out.println("post推送结果："+result);
	        return result;
	 }
//	public static void main(String[] args) {
//		 //发送 POST 请求
//        String sr=HttpRequest.sendPost("http://uatb.paas.qjclouds.com/laserEr/http/post/omns.oa.updateOrderStatus/1.0/00000001/utf-8/ucc", "order_type=1&order_bn=201812130001&status=正在安装&operator_time=2018-12-15 11:18:01&sign=72482448E260BCA6642EF97D5895904D");
//        System.out.println(sr);
//		
//	}
	
}
