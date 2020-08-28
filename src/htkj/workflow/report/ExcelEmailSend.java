package htkj.workflow.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeUtility;

import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFCell;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFRow;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFSheet;
import com.fr.third.v2.org.apache.poi.xssf.usermodel.XSSFWorkbook;

import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.system.SystemComInfo;

public class ExcelEmailSend {
	private  XSSFWorkbook workBook;
	/**
	 * 	邮件发送
	 * @param addressList  发送邮件地址列表
	 * @param emailTitle	邮件标题
	 * @param emailContent	邮件内容
	 * @param excelName		excel名称
	 * @param templatePath	模板路径地址
	 * @param infoMap		修改信息map
	 * @param infoList		明细信息list
	 */
	public void sendExcelEmail(List<String> addressList,String writer,String emailTitle,String emailContent,String excelName,String templatePath,Map<String,String> infoMap,List<Map<String,String>> infoList) {
		BaseBean log = new BaseBean();
		log.writeLog("send Email Start----");
		log.writeLog("addressList----"+addressList.toString());
		log.writeLog("writer----"+writer);
		log.writeLog("templatePath----"+templatePath);
		log.writeLog("infoMap----"+infoMap.toString());
		log.writeLog("infoList----"+infoList.toString());
		String newFileName = "";
		try {
			newFileName =  MimeUtility.encodeText(excelName,"utf-8",null);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		ArrayList<String> filenameList = new ArrayList<String>();
		filenameList.add(newFileName);
		InputStream inputStream = null;
		try {
			inputStream = doExcelGenerate(templatePath,infoMap,infoList);
			//writeToLocal("d:/tmp/test.xlsx",inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<InputStream> fileList = new ArrayList<InputStream>();
		fileList.add(inputStream);
		//执行邮件发送
		doSendEmail(addressList,writer,emailTitle,emailContent,filenameList,fileList);
	}
	
	/** 
	 * 文件流转文件
	 * @param destination
	 * @param input
	 * @throws IOException
	 */
	public static void writeToLocal(String destination, InputStream input)
        throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        input.close();
        downloadFile.close();
    }
	
	/**
	 * 
	 * @param infoMap
	 * @return
	 * @throws IOException 
	 */
	public InputStream doExcelGenerate(String templatePath,Map<String,String> infoMap,List<Map<String,String>> infoList) throws IOException {
		//系统日期获取
		Date date = new Date();
		SimpleDateFormat datefmt= new SimpleDateFormat("yyyy/MM/dd");
		String nowDate = datefmt.format(date);		//当前日期
		//excel字段信息获取
		String EBELN = Util.null2String(infoMap.get("EBELN").toString());	 //系统编号
		String CREATIONDATE = Util.null2String(infoMap.get("CREATIONDATE").toString());	//创建日期
		String LIFNR = Util.null2String(infoMap.get("LIFNR").toString());		//供应商名称
		String ZTERM = Util.null2String(infoMap.get("ZTERM").toString());		//付款方式
		String VERKF = Util.null2String(infoMap.get("VERKF").toString());		//代表姓名
		//读取excel模板文件
		 FileInputStream fis = new FileInputStream(templatePath);
		 workBook = new XSSFWorkbook(fis);
		 //模板复制
		 //workBook.setSheetName(0, "订单信息");
		 XSSFSheet sheet = workBook.cloneSheet(0);
	     //信息修改
	     XSSFCell EBELN_Cell = sheet.getRow(2).getCell(9);	//原系统编号
	     replaceCellValue(EBELN_Cell, EBELN);
	     XSSFCell CREATIONDATE_Cell = sheet.getRow(4).getCell(9);	//原创建日期
	     replaceCellValue(CREATIONDATE_Cell, CREATIONDATE);
	     XSSFCell LIFNR_Cell = sheet.getRow(3).getCell(2);	//原供应商名称
	     replaceCellValue(LIFNR_Cell, LIFNR);
	     XSSFCell ZTERM_Cell = sheet.getRow(10).getCell(3);	//原付款方式
	     replaceCellValue(ZTERM_Cell, ZTERM);
	     XSSFCell VERKF_Cell = sheet.getRow(16).getCell(2);	//原代表姓名
	     replaceCellValue(VERKF_Cell, VERKF);
	     XSSFCell LIFNR_Cell2 = sheet.getRow(15).getCell(7);	//原乙方
	     replaceCellValue(LIFNR_Cell2, LIFNR);
	     XSSFCell nowDate_Cell = sheet.getRow(17).getCell(2);	//原日期
	     replaceCellValue(nowDate_Cell, nowDate);
	     //总金额
	     double amount = 0.00;
	     for (int index = 0; index < infoList.size(); index++) {
				Map<String,String> dt_map = infoList.get(index);
				double BRTWR = Double.parseDouble(dt_map.get("BRTWR").toString());
				amount += BRTWR;
	     }
	     XSSFCell AMOUNT_Cell = sheet.getRow(8).getCell(8);
	     replaceCellValue(AMOUNT_Cell, ""+amount);
	     //插入行
	     int startindex = 7;
	     int endindex = 17;
	     //起始行（不含） 5 10行就是 16
		 sheet.shiftRows(startindex,endindex+infoList.size(),infoList.size(), true, true);
		 //excel明细信息获取
		 for (int index = 0; index < infoList.size(); index++) {
			Map<String,String> dt_map = infoList.get(index);
			String EBELP = Util.null2String(dt_map.get("EBELP").toString());		//行项目
			String MATNR = Util.null2String(dt_map.get("MATNR").toString());		//物料编号
			String MKTXT = Util.null2String(dt_map.get("MKTXT").toString());		//物料描述
			String MKTNO = Util.null2String(dt_map.get("MKTNO").toString());		//图号
			String MENGE = Util.null2String(dt_map.get("MENGE").toString());		//数量
			String MEINS = Util.null2String(dt_map.get("MEINS").toString());		//单位
			String NETPR = Util.null2String(dt_map.get("NETPR").toString());		//单价
			String BRTWR = Util.null2String(dt_map.get("BRTWR").toString());		//金额
			String WAERS = Util.null2String(dt_map.get("WAERS").toString());		//币种
			String EINDT = Util.null2String(dt_map.get("EINDT").toString());		//交期
			String ITEM_TEXT = Util.null2String(dt_map.get("ITEM_TEXT").toString());		//备注
			//插入行
			XSSFRow creRow = sheet.createRow(startindex + index);
			//设置行样式
			XSSFCell newCell1 = creRow.createCell(1);
			newCell1.setCellValue(EBELP);
			newCell1.setCellStyle(sheet.getRow(startindex-1).getCell(1).getCellStyle());
			XSSFCell newCell2 = creRow.createCell(2);
			newCell2.setCellValue(MATNR);
			newCell2.setCellStyle(sheet.getRow(startindex-1).getCell(2).getCellStyle());
			XSSFCell newCell3 = creRow.createCell(3);
			newCell3.setCellValue(MKTXT);
			newCell3.setCellStyle(sheet.getRow(startindex-1).getCell(3).getCellStyle());
			XSSFCell newCell4 = creRow.createCell(4);
			newCell4.setCellValue(MKTNO);
			newCell4.setCellStyle(sheet.getRow(startindex-1).getCell(4).getCellStyle());
			XSSFCell newCell5 = creRow.createCell(5);
			newCell5.setCellValue(MENGE);
			newCell5.setCellStyle(sheet.getRow(startindex-1).getCell(5).getCellStyle());
			XSSFCell newCell6 = creRow.createCell(6);
			newCell6.setCellValue(MEINS);
			newCell6.setCellStyle(sheet.getRow(startindex-1).getCell(6).getCellStyle());
			XSSFCell newCell7 = creRow.createCell(7);
			newCell7.setCellValue(NETPR);
			newCell7.setCellStyle(sheet.getRow(startindex-1).getCell(7).getCellStyle());
			XSSFCell newCell8 = creRow.createCell(8);
			newCell8.setCellValue(BRTWR);
			newCell8.setCellStyle(sheet.getRow(startindex-1).getCell(8).getCellStyle());
			XSSFCell newCell9 = creRow.createCell(9);
			newCell9.setCellValue(WAERS);
			newCell9.setCellStyle(sheet.getRow(startindex-1).getCell(9).getCellStyle());
			XSSFCell newCell10 = creRow.createCell(10);
			newCell10.setCellValue(EINDT);
			newCell10.setCellStyle(sheet.getRow(startindex-1).getCell(10).getCellStyle());
			XSSFCell newCell11 = creRow.createCell(11);
			newCell11.setCellValue(ITEM_TEXT);
			newCell11.setCellStyle(sheet.getRow(startindex-1).getCell(11).getCellStyle());
		}
        workBook.removeSheetAt(0); // 模板移除
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workBook.write(bos);
        byte[] barray = bos.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(barray);
        fis.close();
        bos.flush();
        bos.close();
		return is;
	}
	
	/**
	 * 修改excel值
	 * @param cell
	 * @param value
	 */
	public static void replaceCellValue(XSSFCell cell, Object value) {
        String val = value != null ? String.valueOf(value) : "";
        cell.setCellValue(val);
    }
	
	/**
	 * 执行邮件发送
	 * @param list	邮件地址list
	 * @param writer 抄送人
	 * @param title	邮件标题
	 * @param content	邮件内容
	 * @param filenames	邮件附件名称
	 * @param filecontents	邮件附件
	 */
	public void doSendEmail(final List<String> list,String writer,final String title,final String content,final ArrayList<String> filenames,final ArrayList<InputStream> filecontents) {
		final BaseBean log = new BaseBean();
		SystemComInfo sci = new SystemComInfo();
		String mailip = sci.getDefmailserver();
		String mailuser = sci.getDefmailuser();
		String password = sci.getDefmailpassword();
		String needauth = sci.getDefneedauth();// 是否需要发件认证
		final String mailfrom = sci.getDefmailfrom();
		final SendMail sm = new SendMail();
		sm.setMailServer(mailip);// 邮件服务器IP
		if (needauth.equals("1")) {
			sm.setNeedauthsend(true);
			sm.setUsername(mailuser);// 服务器的账号
			sm.setPassword(password);// 服务器密码
		} else {
			sm.setNeedauthsend(false);
		}
		log.writeLog("News Send Email Start");
	    for (int index = 0; index < list.size(); index++) {
			String email = list.get(index);	//邮箱地址
			if(!"".equals(email)&&!"".equals(title)) {
				boolean result = sm.sendMiltipartHtml(mailfrom,email,writer,"",title,content,3,filenames,filecontents,"3");
				log.writeLog("Mail Status:"+email+"-"+result);
			}
		}
	    log.writeLog("News Send Email End");
	}

}
