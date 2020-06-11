package jh.erp.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class test {

	public static void main(String[] args) throws Exception {
		//http://127.0.0.1:8080/test/111.txt
		String fielurl = "http://127.0.0.1:8080/test/111.txt";
		System.out.println(fielurl.substring(0,fielurl.lastIndexOf("/")));
		System.out.println(fielurl.substring(fielurl.lastIndexOf("/")+1,fielurl.length()));
//		String filename2 = fielurl.substring(fielurl.lastIndexOf("/")+1,fielurl.length());
//		System.out.println(filename2);
//		InputStream in;
//		URL url = new java.net.URL("http://127.0.0.1:8080/test/111.txt");
//		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//		connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestProperty("User-Agent","Mozilla/4.0");
//		connection.connect();
//		in = connection.getInputStream();
//
//		FileOutputStream fos = null;
//		byte [] data = new byte[8*1024];
//		File destFile = new File("D:\\test\\test111.txt");
//		fos = new FileOutputStream(destFile);
//		int count = 0;
//		while((count = in.read(data)) != -1){
//			fos.write(data,0,count);
//		}
//		in.close();
//		fos.close();


	}

}
