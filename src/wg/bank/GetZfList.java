package wg.bank;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;

public class GetZfList {
	public String postConnection(String url, String param) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;

		StringBuffer responseResult = new StringBuffer();

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(60000);
			httpURLConnection.setReadTimeout(60000);
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			// httpURLConnection.setRequestProperty("Content-Length",
			// String.valueOf(param.length()));
			httpURLConnection.setRequestProperty("Charset", "GBK");
			httpURLConnection.setRequestProperty("Content-type", "application/xml;charset=GBK");
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			// printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
			out.write(param.toString().getBytes("GBK"));
			// 发送请求参数
			// printWriter.write(new String(param.toString().getBytes(), "UTF-8"));
			// flush输出流的缓冲
			out.flush();
			out.close();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的ResponseData
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "GBK"));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
				return new String(responseResult.toString().getBytes("UTF-8"), "UTF-8").toString();
			}
			return null;
		} catch (ConnectException e) {
			throw new Exception(e);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 调用查询接口
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String getList(String startDate, String endDate) {
		String LGNNAM;
		try {
			LGNNAM = Util.null2String(
					new String(weaver.file.Prop.getPropValue("wgwypz", "LGNNAM").getBytes("ISO-8859-1"), "UTF-8"));
		} catch (Exception e1) {
			LGNNAM = "";
		}
		String result = "";
		String parm = "<?xml   version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK>\r\n"
				+ "                           <INFO>\r\n"
				+ "                              <FUNNAM>NTQRYSTN</FUNNAM>\r\n"
				+ "                              <DATTYP>2</DATTYP>\r\n" + "                              <LGNNAM>"
				+ LGNNAM + "</LGNNAM>\r\n" + "                           </INFO>\r\n"
				+ "                           <NTQRYSTNY1>\r\n"
				+ "                              <BUSCOD>N02030</BUSCOD>\r\n" + // 业务类别 N02030:支付 N02040:集团支付
				"                              <BUSMOD>00001</BUSMOD>\r\n" + "                              <BGNDAT>"
				+ startDate.replaceAll("-", "") + "</BGNDAT>\r\n" + // 业务类别 N02030:支付 N02040:集团支付
				"                              <ENDDAT>" + endDate.replaceAll("-", "") + "</ENDDAT>\r\n"
				+ "                           </NTQRYSTNY1>\r\n" + "                        </CMBSDKPGK>";

		try {
			writeLog("result parm=" + parm);
			result = postConnection("http://192.168.7.26:8080", parm);

			writeLog("resultquery aaa: =" + result);
		} catch (Exception e) {
			result = "接口调用失败 ，网络没通，请联系系统管理员";
			writeLog(e);
			return result;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(result)));
			Map<String, String> resultMap = doxml(doc);
			System.out.println(resultMap.toString());
			if ("0".equals(resultMap.get("RETCOD"))) {
				result = "接口调用成功结果如下：<br/>";
				List<Map<String, String>> resultlist = getresultList(doc);
				for (Map<String, String> map : resultlist) {
					String ywh = map.get("YURREF");// 业务号
					String OPRDAT = map.get("OPRDAT");// 经办日
					String EPTDAT = map.get("EPTDAT");// 期望日
					String REQSTS = map.get("REQSTS");// 请求状态
					String RTNFLG = map.get("RTNFLG");// 业务处理结果
					if ("FIN".equals(REQSTS)) {
						if ("S".equals(RTNFLG)) {
							result = result + "业务号：" + ywh + " 经办日：" + OPRDAT + " 期望日：" + EPTDAT
									+ " 状态：已处理 处理结果：支付成功";
							String ywharr[] = ywh.split("_");							
							if (ywharr.length == 2) {
								String jyrq = getJYrq(startDate, endDate, ywh);
								if(!"".equals(jyrq)) {
									result = result +"交易日期："+jyrq+"<br/>";
									dorq(ywh,jyrq);
								}else {
									result = result +"交易日期：获取结果为空 请手工处理<br/>";
								}
								
							}else {
								result = result +"<br/>";
							}
						
						} else if ("B".equals(RTNFLG)) {
							result = result + "业务号：" + ywh + " 经办日：" + OPRDAT + " 期望日：" + EPTDAT
									+ " 状态：已处理 处理结果：退票<br/>";
						} else {
							result = result + "业务号：" + ywh + " 经办日：" + OPRDAT + " 期望日：" + EPTDAT
									+ " 状态：已处理 处理结果：失败<br/>";
						} 
					} else { 
						result = result + "业务号：" + ywh + " 经办日：" + OPRDAT + " 期望日：" + EPTDAT + " 状态：银行处理中<br/>";
					}
					
				}
			} else {
				result = "接口调用失败，原因ERRMSG:" + resultMap.get("ERRMSG");
				return result;
			}
		} catch (Exception e) {
			result = "接口调用失败 ，返回值解析异常，请联系系统管理员";
			writeLog(e);
			return result;
		}
		return result;
	}
	public String getJYrq(String startDate, String endDate,String ywh) {
		String jyrq = "";
		String result = "";
		String LGNNAM;
		try {
			LGNNAM = Util.null2String(
					new String(weaver.file.Prop.getPropValue("wgwypz", "LGNNAM").getBytes("ISO-8859-1"), "UTF-8"));
		} catch (Exception e1) {
			LGNNAM = "";
		}
		String DBTACC = Util.null2String(weaver.file.Prop.getPropValue("wgwypz","DBTACC"));//付方帐号
		String DBTBBK = Util.null2String(weaver.file.Prop.getPropValue("wgwypz","DBTBBK"));//付方开户地区代码
		String parm = "<?xml   version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK>\r\n"
				+ "                           <INFO>\r\n"
				+ "                              <FUNNAM>GetTransInfo</FUNNAM>\r\n"
				+ "                              <DATTYP>2</DATTYP>\r\n" 
				+ "                              <LGNNAM>"+ LGNNAM + "</LGNNAM>\r\n" + "                           </INFO>\r\n"
				+ "                           <SDKTSINFX>\r\n"
				+"<BBKNBR>"+DBTBBK+"</BBKNBR>\r\n"            
		        +"                <C_BBKNBR></C_BBKNBR>\r\n" 
		        +"               <ACCNBR>"+DBTACC+"</ACCNBR>\r\n"  
		        +"               <BGNDAT>"+startDate.replaceAll("-", "")+"</BGNDAT>\r\n"       
		        +"                <ENDDAT>"+endDate.replaceAll("-", "")+"</ENDDAT>\r\n"   
		        +"                <LOWAMT></LOWAMT>\r\n"          
		        +"               <HGHAMT></HGHAMT>\r\n"         
		        +"               <AMTCDR></AMTCDR>\r\n" 
				+"                           </SDKTSINFX>\r\n" 
				+"                        </CMBSDKPGK>";

		try {
			writeLog("resultjyrq parm=" + parm);
			result = postConnection("http://192.168.7.26:8080", parm);

			writeLog("resultjyrq query aaa: =" + result);
			List<Map<String, String>> resultList = getjyrqList(result);
			for(Map<String, String> map:resultList) {
				if(ywh.equals(map.get("YURREF"))) {
					jyrq = map.get("ETYDAT");
					break;
				}
			}
		} catch (Exception e) {
			writeLog(e);
			return jyrq;
		}
		
		return jyrq ;
	}
	/**
	 * 获取交易日期
	 * @param result
	 * @return 返回业务号和交易日期的list
	 * @throws Exception
	 */
	public static List<Map<String, String>> getjyrqList(String result) throws Exception {
		List<Map<String, String>> resultlist = new ArrayList<Map<String, String>>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		
		NodeList list =doc.getElementsByTagName("NTQTSINFZ");
		for(int i=0;i<list.getLength();i++) {
			Node node=list.item(i);
			NodeList childlist = node.getChildNodes();
			Map<String,String> resultMap = new HashMap<String, String>();
			for(int j=0;j<childlist.getLength();j++) {				
				Node childnode=childlist.item(j);
				if("YURREF".equals(childnode.getNodeName())) {
					resultMap.put("YURREF", childnode.getTextContent());
				}
				if("ETYDAT".equals(childnode.getNodeName())) {
					resultMap.put("ETYDAT", childnode.getTextContent());
				}
				
			}
			if(!resultMap.isEmpty()) {
				resultlist.add(resultMap);
			}
		}
		return resultlist;
	}
	
	/**
	 * 处理单条数据
	 * 
	 * @param ywh
	 * @param zfrq
	 */
	public void dorq(String ywh, String zfrq) {
		SimpleDateFormat sf1  = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2  = new SimpleDateFormat("yyyyMMdd");
		RecordSet rs = new RecordSet();
		String ywharr[] = ywh.split("_");
		String lcbh = "";// 流程号
		String billid = "";// 建模id
		if (ywharr.length == 2) {
			lcbh = ywharr[0];
			billid = ywharr[1];
		} else {
			return;
		}
		if ("".equals(lcbh) || "".equals(billid)) {
			return;
		}
		int count = 0;
		String sql = "select count(1) as count from uf_netbank where id="+billid;
		rs.executeSql(sql);
		if (rs.next()) {
			count = rs.getInt("count");
		}
		if(count==0) {
			writeLog("建模数据不存在" + sql);
			return;
		}
		count = 0;
		sql = "select count(1) as count from uf_netbank where flowno = '" + lcbh + "' and sfzf='1'";
		rs.execute(sql);
		if (rs.next()) {
			count = rs.getInt("count");
		}
		sql = "update uf_netbank set sfzf='1',zfrq='" + zfrq + "' where id=" + billid;
		writeLog("sql:" + sql);
		rs.execute(sql);
		if (count == 0) {
			String tablename = "";
			String requestid = "";
			String userid = "";
			sql = "select a.requestid,c.tablename from workflow_requestbase a,workflow_base b,workflow_bill c where a.workflowid=b.id and b.formid=c.id and a.requestmark='"
					+ lcbh + "'";
			rs.execute(sql);
			if (rs.next()) {
				tablename = Util.null2String(rs.getString("tablename"));
				requestid = Util.null2String(rs.getString("requestid"));
			}
			if(!"".equals(zfrq)) {
				try {
					zfrq = sf1.format(sf2.parse(zfrq));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sql = " update " + tablename + " set paydate='" + zfrq + "' where requestid=" + requestid;
			rs.execute(sql);
			writeLog("sql:" + sql);
			sql="select userid from workflow_currentoperator where requestid='"+requestid+"' and isremark=0 and islasttimes=1 order by id desc";
			rs.execute(sql);
			if(rs.next()) {
				userid = Util.null2String(rs.getString("userid"));
			}
			if("".equals(userid)) {
				writeLog("流程操作者不存在:"+sql);
				return;
			}
			String doresult = autoSubmit(requestid, userid);
			writeLog("doresult:"+doresult);
		}
	}

	/**
	 * 解析结果xml
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> doxml(Document doc) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		if (doc.getElementsByTagName("RETCOD").getLength() > 0) {
			resultMap.put("RETCOD", doc.getElementsByTagName("RETCOD").item(0).getTextContent());
		}
		if (doc.getElementsByTagName("ERRMSG").getLength() > 0) {
			resultMap.put("ERRMSG", doc.getElementsByTagName("ERRMSG").item(0).getTextContent());
		}

		return resultMap;

	}

	/**
	 * 解析返回结果list
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getresultList(Document doc) throws Exception {
		List<Map<String, String>> resultlist = new ArrayList<Map<String, String>>();
		NodeList list = doc.getElementsByTagName("NTSTLLSTZ");
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			NodeList childlist = node.getChildNodes();
			Map<String, String> resultMap = new HashMap<String, String>();
			for (int j = 0; j < childlist.getLength(); j++) {
				Node childnode = childlist.item(j);
				if ("YURREF".equals(childnode.getNodeName())) {
					resultMap.put("YURREF", childnode.getTextContent());
				}
				if ("OPRDAT".equals(childnode.getNodeName())) {
					resultMap.put("OPRDAT", childnode.getTextContent());
				}
				if ("EPTDAT".equals(childnode.getNodeName())) {
					resultMap.put("EPTDAT", childnode.getTextContent());
				}
				if ("REQSTS".equals(childnode.getNodeName())) {
					resultMap.put("REQSTS", childnode.getTextContent());
				}
				if ("RTNFLG".equals(childnode.getNodeName())) {
					resultMap.put("RTNFLG", childnode.getTextContent());
				}
			}
			if (!resultMap.isEmpty()) {
				resultlist.add(resultMap);
			}
		}
		return resultlist;
	}

	public String autoSubmit(String requestid, String userid) {
		WorkflowServiceImpl ws = new WorkflowServiceImpl();
		WorkflowRequestInfo wri = new WorkflowRequestInfo();
		String result = ws.submitWorkflowRequest(wri, Integer.valueOf(requestid), Integer.valueOf(userid), "submit",
				"支付完成 自动提交");
		return result;
	}

	private void writeLog(Object obj) {
		if (true) {
			new BaseBean().writeLog(this.getClass().getName(), obj);
		}
	}

}
