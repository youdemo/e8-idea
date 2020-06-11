/**
 * Title:        Excel矩阵导入适配器
 * Company:      泛微软件
 * @author:      孔志文
 * @version:     1.0
 * create date : 2015-5-25
 * modify log:
 *
 * Description:  对Excel矩阵导入模板进行验证和数据解析，形成矩阵数据集合
 *               
 */
package weaver.matrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.file.FileManage;
import weaver.file.FileUploadToPath;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.join.hrm.in.HrmResourceVo;
import weaver.join.hrm.in.ImportLog;
import weaver.servicefiles.BrowserXML;
import weaver.systeminfo.SystemEnv;

public class MatrixImportProcess extends BaseBean{

	private String fileName;

	private HSSFSheet sheet;

	private List errorInfo = new ArrayList();
	
	private String matrixid = "";
	
	//自定义选择框标示集合
	private List<String> browserValues = new ArrayList<String>();
	
	
	//标准导入字段数组
	private List<String> voDisplayName = new ArrayList<String>();
	
	
	//标准导入字段数组
	private List<String> voFields = new ArrayList<String>();
	
	//标准导入字段类型数组
	private List<String> voFieldType = new ArrayList<String>();
	
	//标准导入字段经纬类型数组
	private List<String> fieldType = new ArrayList<String>();
	
	//excel匹配数据集合
	private List<List<Object>> dataList = new ArrayList<List<Object>>();
	
	
	//数据库查询对象
	private RecordSet recordSet=new RecordSet();   
	
	StaticObj staticObj=StaticObj.getInstance(); 
	
	private String logFile="";                  //日志全路径
	private String logFileName="";                  //日志文件名
	
	private  String importType;               //导入类型  添加 add |更新 update
	
	private BrowserXML BrowserXML = new BrowserXML();  //获取自定义浏览框配置文件对象
	
	ArrayList pointArrayList = new ArrayList(); 
	
	Hashtable dataHST = new Hashtable();
	
	private String isSystem = "";
	
	private int userlanguage = 7;
	
	
	/**
	 * map集合创建类
	 * @param fu   上传参数
	 * @return  List
	 */
	public List creatImportMap(FileUploadToPath fu) {
		try {
		// 初始化数据
		 initDataSource(fu);
		 
		 if (!errorInfo.isEmpty()){
			    FileManage.DeleteFile(fileName);
				return errorInfo;
			}
		//初始化模板字段 
		 //initTempFields();
		 
		// 模板验证
		valExcelTemp();

		if (!errorInfo.isEmpty()){
			FileManage.DeleteFile(fileName);
			return errorInfo;
		}
		
		// 读取数据并验证
		readExcel();

		if (errorInfo.isEmpty()){
			FileManage.DeleteFile(fileName);
			return errorInfo;
		}
		    return errorInfo;
		}catch (NegativeArraySizeException e) { 
			errorInfo.add("Excel模板有误，请将数据复制到新的模板重新导入");//目前尚不清楚，这种异常的产生原因
			writeLog(e);
			return errorInfo;
		}catch (Exception e) {
			e.printStackTrace();
			errorInfo.add("Excel导入错误，请阅读注意事项并检查模板文件");//Excel导入错误，请阅读注意事项并检查模板文件
			writeLog(e);
			return errorInfo;
		}
		
	}
	
	/**
	 * 获取上传文件，初始化参数
	 * @param fu
	 * @return
	 */
	public List initDataSource(FileUploadToPath fu) {
		
		this.matrixid=fu.getParameter("matrixid");
		this.importType = fu.getParameter("importType");
		this.userlanguage = Util.getIntValue(fu.getParameter("userlanguage"),7);
		
		String sql ="select *  from MatrixFieldInfo where matrixid="+matrixid + "  order by fieldtype asc, priority";
		recordSet.executeSql(sql);
		while(recordSet.next()){
			voFields.add(recordSet.getString("fieldname"));	
			voDisplayName.add(recordSet.getString("displayname"));	
			voFieldType.add(recordSet.getString("browsertypeid"));	
			browserValues.add(recordSet.getString("browservalue"));	
			fieldType.add(recordSet.getString("fieldtype"));	
        }
		
		

		this.fileName = fu.uploadFiles("excelfile");
		
		this.importType=fu.getParameter("importType");
		
		this.isSystem=fu.getParameter("issystem");
		
		pointArrayList = BrowserXML.getPointArrayList();
		dataHST = BrowserXML.getDataHST();
		
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(
					new FileInputStream(fileName)));
			this.sheet = workbook.getSheetAt(1);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			errorInfo.add(SystemEnv.getHtmlLabelName(83618, userlanguage));	//上传文件不是excel文件
			writeLog(e);
		}catch (IndexOutOfBoundsException e) {
			errorInfo.add("请确认第2个sheet是当前矩阵导入模板");
			writeLog(e);
		}
		return errorInfo;
	}

	/**
	 * 验证模板格式
	 * 
	 * @return
	 */
	public List valExcelTemp() {

		HSSFRow row;
		HSSFCell cell;
		String cellValue;
		List fieldList=new ArrayList();
		try {
			row = sheet.getRow(0);
			int first = row.getFirstCellNum();
			int last = row.getLastCellNum();
			String field = "";
			for (int i = 0; i < row.getLastCellNum(); i++)
			{   boolean flag=false;
				cell = row.getCell((short) i);
				if (cell != null) {
					cellValue =getCellValue(cell).trim();
					if("update".equals(importType)&&i==0){
						continue;
					}
					for(int k=0;k<voFields.size();k++){
						
						if(cellValue.equals(voDisplayName.get(k))){
						    flag=true;
						    break;
						}
					}
					if(!flag)
						errorInfo.add(getCellPosition(i, 1)+"["+cellValue+"]"+" 不是模板中字段，请重新下载模板");      //不是模板中字段，请检查是否有误
					if(!cellValue.equals(voDisplayName.get("update".equals(importType)?i-1:i))){   //列顺序判断
						errorInfo.add("模板中字段的顺序不能变动");  
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			errorInfo.add("Excel导入错误，请阅读注意事项并检查模板文件");             //   excel模板有误时
			writeLog(e);
		}
		return errorInfo;
	}

	/**
	 * 读取excel数据
	 * 
	 * @return
	 */
	public List readExcel() {

		boolean flag = true; // 标记验证是否通过
		HSSFRow row;
		HSSFCell cell;
		int rowNum = 0; // 行号
		int cellNum = 0; // 列号
		int firstRow = 1;
		String keyValue = "";
		int lastRow = sheet.getLastRowNum();
		String subCompany = "";
		String department = "";
		String cellValue;
		int fieldIndex=0;
		HrmResourceVo hrmResourceVo = null;

		for (int i = firstRow; i <= lastRow; i++) {
			row = sheet.getRow(i);
			if(row==null){
				errorInfo.add(SystemEnv.getHtmlLabelName(15323, userlanguage)+(i+1)+" "+SystemEnv.getHtmlLabelName(83622, userlanguage));
				continue;
			}
			rowNum = row.getRowNum();
			List<Object> rowList = new ArrayList<Object>();
			for (int cellIndex = 0; cellIndex < voFields.size(); cellIndex++) {
				if("update".equals(importType)){
					if(cellIndex == 0){
						cell = row.getCell((short) 0);
						rowList.add(getCellValue(cell).trim());
					}
					cell = row.getCell((short) (cellIndex+1));
				}else{
					cell = row.getCell((short) cellIndex);
				}
				if(!"".equals(isSystem) && "id".equalsIgnoreCase(voFields.get(cellIndex))){
					rowList.add("");
					continue;
				}
				cellNum =cellIndex;
			    cellValue =getCellValue(cell).trim(); 
			    String dataType = voFieldType.get(cellIndex).trim();
			    String fieldtypeStr = fieldType.get(cellIndex).trim();
			    if(!"".equals(cellValue)){
			    	Object dataId = -1;
			    	String projName ="";
			    	String msg=SystemEnv.getHtmlLabelName(125268, userlanguage);
			    	if("1".equals(dataType)){ //人力资源
			    		dataId =getResourceId(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(125269, userlanguage);
			    	}else if("17".equals(dataType)){ //部门
			    		dataId = getResourceIds(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(125270, userlanguage);
			    	}else if("4".equals(dataType)){ //部门
			    		dataId = getDeptId(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(124, userlanguage);
			    	}else if("164".equals(dataType)){ //分部
			    		dataId = getSubCompanyId(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(141, userlanguage);
			    	}else if("7".equals(dataType)){ //客户
			    		dataId = getCustomerId(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(136, userlanguage);
			    	}else if("8".equals(dataType)){ //项目
			    		dataId = getProjectId(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(22245, userlanguage);
			    	}else if("24".equals(dataType)){ //岗位
			    		dataId = getJobTitles(cellValue);
			    		projName=SystemEnv.getHtmlLabelName(6086, userlanguage);
			    	}else if("161".equals(dataType)){ //自定义单选
			    		dataId = getBrowserId(cellValue,browserValues.get(cellIndex));
			    		projName=SystemEnv.getHtmlLabelName(21002, userlanguage);
			    	}else if("162".equals(dataType)){ //自定义多选
			    		dataId = getBrowserIds(cellValue,browserValues.get(cellIndex));
			    		projName=SystemEnv.getHtmlLabelName(21003, userlanguage);
			    	}
			    	
			    	if(!dataId.equals(-1)&&!dataId.equals("-1")&&!dataId.equals("")){
			    		rowList.add(dataId);
			    	}else{
			    		errorInfo.add(SystemEnv.getHtmlLabelName(15323, userlanguage)+(i+1)+" "+SystemEnv.getHtmlLabelName(18620, userlanguage)+", "+(getCellPosition(cellIndex+("update".equals(importType)?1:0)))+" "+SystemEnv.getHtmlLabelName(18621, userlanguage)+",["+projName+"]"+msg);
			    	}
			    
			    }else{
			    	rowList.add("");
			    }
			    
			}
			dataList.add(rowList);
		}
		return errorInfo;
	}
	
	
	
	/**
	 * 根据分部名称获取分部Id
	 * @param subCompanyName   分部名称 eg：分部1>分部1.1>分部1.1.1
	 * @return
	 */
	public int getSubCompanyId(String subCompanyName) {
		String subCompanyNames[] = subCompanyName.split(">");
		int currentId = 0;
		int parentId = 0;

		String sql = "";
		for (int i = 0; i < subCompanyNames.length; i++) {
			if(subCompanyNames[i] ==null && subCompanyNames[i].equals("")){
				continue;
			}
			sql = "select id from HrmSubCompany where ltrim(rtrim(subcompanyname))='"
					+ subCompanyNames[i].trim() + "' and supsubcomid=" + parentId +" and (canceled  !=1 or canceled is null)";

			currentId = getResultSetId(sql);

			if (currentId == 0) {
				return -1;
			}
			parentId = currentId;
		}
		return currentId;
	}

	/**
	 * 获取部门id
	 * @param deptNames        部门名称  eg:分部1>分部2||部门1>部门1.1>部门1.1.1
	 * @return
	 */
	public int getDeptId(String deptNames) {
		String subName = "";
		String depName = "";
		if(deptNames.indexOf("||") > -1){
			subName = deptNames.substring(0,deptNames.indexOf("||"));
			depName = deptNames.substring(deptNames.indexOf("||")+2,deptNames.length());
		}else{
			return -1;
		}
		int currentId = 0;
		int parentId = 0;
		//查询分部是否存在
		int subCompanyId =getSubCompanyId(subName);
		
		if(subCompanyId == -1 || "".equals(depName)){
			return -1;
		}
		String deptNameList[]= depName.split(">");
		String sql = "";
		for (int i = 0; i < deptNameList.length; i++) {
			if(deptNameList[i] == null || deptNameList[i].equals("")){
				continue;
			}
			String subStr = "";
			if(subCompanyId !=0){
				subStr = "subcompanyid1 in("+ subCompanyId + ") and"; 
			}
			sql = "select id from HrmDepartment where "+subStr+" ltrim(rtrim(departmentname))='"+ deptNameList[i].trim() + "' and supdepid=" + parentId +" and (canceled  !=1 or canceled is null)";
			currentId = getResultSetId(sql);
			if (currentId == 0) {
				return -1;
			}
			parentId = currentId;
		}
		return currentId;
	}

	/**
	 * 获取岗位id
	 * @param jobtitlename      岗位名称 eg:岗位
	 * @return
	 */
	public int getJobTitles(String jobtitlename) {
		/*获取岗位id*/
		String selSql = "select id from HrmJobTitles where  jobtitlename='" + jobtitlename + "'";
		int jobtitle = getResultSetId(selSql);
		if (jobtitle == 0) {
			return -1;
		}
		return jobtitle;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取单人员id
	 * @param names     人员名称 eg:姓名/部门
	 * @return
	 */
	public String getResourceId(String names) {
		String resourceId = "";
			String thisName = names;
			String departmentid = "";
			
			/* 获取部门 id*/
			String name = "";
			if(thisName.indexOf("/") > -1){
				String departmenName =  thisName.substring(thisName.lastIndexOf("/")+1,thisName.length()).trim();
				name = thisName.substring(0,thisName.lastIndexOf("/")).trim();
				if(!"".equals(departmenName)){
					String sql = "select id from HrmDepartment where departmentname='"+ departmenName + "' and (canceled  !=1 or canceled is null)";
					recordSet.execute(sql);
					int isFirst = 0;
					while (recordSet.next()) {
						if(isFirst > 0){
							departmentid += ",";
						}
						departmentid += recordSet.getInt("id");
						isFirst++;
					}
				}
			}else{
				name=thisName;
			}
			
			/*获取人员id*/
			if("".equals(name)){
				return "";
			}
			String selSql = "select * from hrmresource where lastname='"
				+ name+ "'";
			if (!"".equals(departmentid)) {
				selSql+= " and departmentid in (" + departmentid + ")";
			}
			selSql += "and status in(0,1,2,3)";
			int thisId = getResultSetId(selSql);
			if(thisId != 0){
				resourceId = thisId+"";
			}
			
		return resourceId;
	}
	/**
	 * 获取多人力id
	 * @param names     人员名称 eg:姓名/部门
	 * @return
	 */
	public String getResourceIds(String names) {
		String mulNames[] = names.split(";");
		String resourceId = "";
		for (int i = 0; i < mulNames.length; i++) {
			String thisName = mulNames[i];
			String departmentid = "";
			
			/* 获取部门 id*/
			String name = "";
			if(thisName.indexOf("/") > -1){
				String departmenName =  thisName.substring(thisName.lastIndexOf("/")+1,thisName.length()).trim();
				name = thisName.substring(0,thisName.lastIndexOf("/")).trim();
				if(!"".equals(departmenName)){
					String sql = "select id from HrmDepartment where departmentname='"+ departmenName + "' and (canceled  !=1 or canceled is null)";
					recordSet.execute(sql);
					int isFirst = 0;
					while (recordSet.next()) {
						if(isFirst > 0){
							departmentid += ",";
						}
						departmentid += recordSet.getInt("id");
						isFirst++;
					}
				}
			}else{
				name=thisName;
			}
			
			/*获取人员id*/
			if("".equals(name)){
				return "";
			}
			String selSql = "select * from hrmresource where lastname='"
				+ name+ "'";
			if (!"".equals(departmentid)) {
				selSql+= " and (" + Util.getSubINClause(departmentid, "departmentid", "in") + ")";
			}
			selSql += "and status in(0,1,2,3)";
			int thisId = getResultSetId(selSql);
			if(i>0 && !"".equals(resourceId)){
				resourceId += ",";
			}
			if(thisId != 0){
				resourceId += thisId;
			}
			
		}
		return resourceId;
	}
	
	
	/**
	 * 获取客户id
	 * @param name     客户名称 eg:客户1
	 * @return
	 */
	public int getCustomerId(String name) {
		int customerId = 0;
			/*获取客户id*/
		String selSql = "select * from CRM_CustomerInfo where  deleted<>1 and name='"
				+ name+ "'";
		customerId = getResultSetId(selSql);
			
		if (customerId == 0) {
			return -1;
		}
		return customerId;
	}
	
	/**
	 * 获取项目id
	 * @param name     项目名称 eg:项目1
	 * @return
	 */
	public int getProjectId(String name) {
		int customerId = 0;
		/*获取客户id*/
		String selSql = "select * from Prj_ProjectInfo where  name='"
			+ name+ "'";
		customerId = getResultSetId(selSql);
		
		if (customerId == 0) {
			return -1;
		}
		return customerId;
	}
	
	/**
	 * 获取自定义选择框 选择项id
	 * @param name    选择项名称 eg:选择项1
	 * @return
	 */
	public int getBrowserId(String name,String browserid) {
		int customerId = 0;
		browserid=browserid.substring(browserid.indexOf(".")+1);
		/*获取客户id*/
		String selSql = "";
		String searchSql = "";
		
		for(int i=0;i<pointArrayList.size();i++){
		    String pointid = (String)pointArrayList.get(i);
		    if(browserid.equals(pointid))
		    {
			    Hashtable thisDetailHST = (Hashtable)dataHST.get(pointid);
			    if(thisDetailHST!=null){
			    	searchSql = (String)thisDetailHST.get("searchByName");
			    	selSql = (String)thisDetailHST.get("search");
			    }
		    }
		}
		
		if("".equals(selSql)){
			return -1;
		}
		selSql += " where 1=1";
		RecordSet rs=new RecordSet(); 
		String keyfield = "";
		rs.executeSql("select p.searchname,d.keyfield from datashowset d inner join datashowparam p on d.id=p.mainid where d.showname='"+browserid+"'");
		int num = 0;
		while(rs.next()){
			if(num == 0){
				selSql +=" and ( ";
			}else{
				selSql +=" or ";
			}
			selSql += rs.getString("searchname")+" = '"+name+"'";
			keyfield = rs.getString("keyfield");
			num++;
		}
		if(num > 0){
			selSql +=" )";
		}
		if("".equals(keyfield)){
			rs.executeSql("select * from datashowset where showname='"+browserid+"'");
			rs.next();
			String sqlText = rs.getString("sqltext2");
			if("".equals(sqlText)) sqlText = searchSql;
			selSql = StringUtil.replace(sqlText, "?", "'"+name+"'");
		}
		if(!"".equals(keyfield)){
			customerId = getResultSetId(selSql,keyfield);
		}else{
			customerId = getResultSetId(selSql);
		}
		if (customerId == 0) {
			return -1;
		}
		return customerId;
	}
	
	
	
	/**
	 * 获取自定义多选 选择项id
	 * @param name    选择项名称 eg:选择项1
	 * @return
	 */
	public String getBrowserIds(String name,String browserid) {
		String customerIds = "";
		String cellValues[]=name.split(";");
		for (int i = 0; i < cellValues.length; i++) {
			if(i>0){
				customerIds+=",";
			}
			int customerid = getBrowserId(cellValues[i], browserid);
			if(customerid == -1){
				return "";
			}
			customerIds+=customerid;
			
		}
		return customerIds;
	}
	
	/**
	 * 获得查询结果Id
	 * @param sql  查询语句
  	 * @return
	 */
	public int getResultSetId(String sql,String keyfield) {
		int currentId = 0;
		recordSet.execute(sql);
		try {
			while (recordSet.next()) {
				currentId = recordSet.getInt(!"".equals(keyfield)?keyfield:"id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentId;
	}
	
	/**
	 * 获得查询结果Id
	 * @param sql  查询语句
  	 * @return
	 */
	public int getResultSetId(String sql) {
		int currentId = 0;
		recordSet.execute(sql);
		try {
			while (recordSet.next()) {
				currentId = recordSet.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentId;
	}
	
	/**
	 * 导入数据到数据库
	 * @param importType
	 * @return
	 */
	public List processMap() {
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();          //导入结果	
		try{
		boolean flag = true;
		String sql ="";
		List<String> uuidList = new ArrayList<String>();
		recordSet.execute("select  uuid from "+" Matrixtable_"+matrixid);
		while (recordSet.next()) {
			uuidList.add(recordSet.getString("uuid"));
		}
		
		float maxDataorder = 0;
		String dbType = recordSet.getDBType();
    	String dataorder = "dataorder";
    	if("oracle".equalsIgnoreCase(dbType)){
    		dataorder = "dataorder+0";
    	}else if("sqlserver".equalsIgnoreCase(dbType)){
    		dataorder = "cast(dataorder as float)";
    	}
		recordSet.execute("select  max("+dataorder+") dataorder from "+" Matrixtable_"+matrixid);
		while (recordSet.next()) {
			maxDataorder = recordSet.getFloat("dataorder");
		}
		if(maxDataorder == -1){
			maxDataorder = 0;
		}
		for(int i =0;i<dataList.size();i++){
			List<Object> rowDataList = dataList.get(i);
			if("update".equals(importType)){
				if(!uuidList.contains(rowDataList.get(0).toString())){
					Map<String, String> msg = new HashMap<String, String>();
					msg.put("operation", "更新");
					msg.put("status", "error");
					msg.put("msg", "第"+(i+1)+"行数据"+",UUID未能正确匹配，请重新导出数据进行更新!");
					resultList.add(msg);
					staticObj.putObject("matrix_resultList", resultList);
					continue;
				}
				sql="update Matrixtable_"+matrixid +" set ";
				for (int j = 0; j < voFields.size(); j++) {
					if(!"".equals(isSystem)&& "id".equalsIgnoreCase(voFields.get(j))){
						continue;
					}
					sql+=voFields.get(j)+"='"+rowDataList.get(j+1)+"',";
				}
				sql = sql.substring(0,sql.length()-1);
				sql+=" where uuid = '"+rowDataList.get(0)+"'";
				
			}else{
				maxDataorder++;
				String insertColunms="";
				String insertValues= "";
				for (int j = 0; j < voFields.size(); j++) {
					if(j>0){
						insertValues+=",";
						insertColunms+=",";
					}
					insertColunms+=voFields.get(j);
					insertValues+="'"+rowDataList.get(j)+"'";
				}
				sql="insert into Matrixtable_"+matrixid +" (uuid,dataorder,"+insertColunms+") values('"+UUID.randomUUID().toString()+"',"+maxDataorder+","+insertValues+")";
			}
			if(!execSql(sql))                    //添加人员信息 
				flag=false;
			Map<String, String> msg = new HashMap<String, String>();
			String opeType = "创建";
			if("update".equals(importType)){
				opeType = "更新";
			}
			msg.put("operation", opeType);
			if(flag){
				msg.put("status", "success");
				msg.put("msg", "第"+(i+1)+"行数据"+","+opeType+"成功!");
				resultList.add(msg);
			}else{
				msg.put("status", "error");
				msg.put("msg", "第"+(i+1)+"行数据"+","+opeType+"失败!");
				resultList.add(msg);
			}
			staticObj.putObject("matrix_resultList", resultList);
		}
		staticObj.putObject("matrixImportStatus", "importing");
		writeImportLog(resultList);
		staticObj.putObject("matrixLogFile", logFileName);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 执行插入操作
	 * @param sql
	 * @return
	 */
	
	public boolean execSql(String sql) {
		if(recordSet.execute(sql)){
		    return true;
		}
		else{
	 	    return false;
		}
	 }
	
	/**
	 * 写入导入日志
	 * @param resultList  导入结果集
	 */
	public void writeImportLog(List<Map<String,String>> resultList) {
		if (logFile.equals("")) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

            String logFile1=GCONST.getRootPath()+"/log/matrixImportLog";
			 logFile1=logFile1.replace("\\", "/");
		      File logFile2=new File(logFile1);
		        if (!logFile2.exists()) {
		       logFile2.mkdir();
		      }
		    logFileName="矩阵导入_"+ dateFormat.format(new Date()) + ".txt";
			logFile=GCONST.getRootPath()+"log"+File.separator+"matrixImportLog"+File.separator+logFileName;
	        logFile=logFile.replace("\\", "/");
			File file = new File(logFile);

			try {
				file.createNewFile();
			} catch (IOException e) {
				writeLog(e);
			}
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile,
					true));
			
			SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String logRecord = "导入时间 "+timeFormat.format(new Date())+"\r\n";
			out.write(logRecord);
			// 优先输出失败结果
			for (int i = 0; i < resultList.size(); i++) {
				Map log =  resultList.get(i);

				if ("error".equals(log.get("status"))) {
					logRecord = "失败|"+log.get("msg")+"\r\n";
					out.write(logRecord);
				}
			}

			// 输出成功结果
			for (int i = 0; i < resultList.size(); i++) {
				Map log =  resultList.get(i);
				if ("success".equals(log.get("status"))) {

					logRecord = "成功|"+log.get("msg")+"\r\n";
					out.write(logRecord);
				}
			}
			out.close();
		} catch (IOException e) {
			writeLog(e);
		}
	}

	
   /**
    * 创建日志对象
    * @param vo         HrmResourceVo  人员信息对象
    * @param operation  操作类型  验证|创建|更新
    * @param status     状态 成功|失败
    * @param reason     失败原因
    * @return           ImportLog对象
    */
	public ImportLog createLog(HrmResourceVo vo,String operation,String status,String reason){
		ImportLog log=new ImportLog();
		
		log.setWorkCode(vo.getWorkcode());  //编号
		log.setLastname(vo.getLastname());  //用户名
		log.setLoginid(vo.getLoginid());    //登录名
		log.setOperation(operation);        //操作类型
		if(vo.getSubcompanyid1()!=null&&vo.getDepartmentid()!=null)
		  log.setDepartment(vo.getSubcompanyid1()+">"+vo.getDepartmentid()); //分部部门
		else
		  log.setDepartment("");	
		log.setStatus(status);              //状态，成功、失败
	    log.setReason(reason);              //原因
	    
		return log;
	}
	
	
	
	
	
	
	
	

	/**
	 * excel单元格位置转换
	 * 
	 * @param cellIndex   列号
	 * @param rowNum      行号
	 * @return
	 */
	public String getCellPosition(int cellIndex, int rowNum) {

		int count = cellIndex / 26;
		String cellChar = String.valueOf((char) (cellIndex % 26 + 65));
		String cellPosition ="";
		
		if(count!=0)
		  cellPosition=String.valueOf((char) ((count-1) + 65))+cellChar;
		else
		  cellPosition=cellChar;
		cellPosition += rowNum;
		return cellPosition;
	}
	
	/**
	 * excel单元格位置转换
	 * 
	 * @param cellIndex   列号
	 * @param rowNum      行号
	 * @return
	 */
	public String getCellPosition(int cellIndex) {
		
		int count = cellIndex / 26;
		String cellChar = String.valueOf((char) (cellIndex % 26 + 65));
		String cellPosition ="";
		
		if(count!=0)
			cellPosition=String.valueOf((char) ((count-1) + 65))+cellChar;
		else
			cellPosition=cellChar;
		return cellPosition;
	}
	
	
	/**
	 * 获取excel单元格值
	 * @param cell   要读取的单元格对象
	 * @return
	 */
	public String getCellValue(HSSFCell cell){
    	String cellValue="";
    	if(cell==null)
    		return "";
    	switch(cell.getCellType()){   
        case HSSFCell.CELL_TYPE_BOOLEAN:                                  //得到Boolean对象的方法   
        	cellValue=String.valueOf(cell.getBooleanCellValue());
            break;   
        case HSSFCell.CELL_TYPE_NUMERIC:   
            if(HSSFDateUtil.isCellDateFormatted(cell)){//先看是否是日期格式  
            	SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd");
            	cellValue=String.valueOf(sft.format(cell.getDateCellValue()));   //读取日期格式  
            }else{ 
            	cellValue=String.valueOf(new Double(cell.getNumericCellValue())); //读取数字
            	if(cellValue.endsWith(".0"))
            		cellValue=cellValue.substring(0,cellValue.indexOf("."));
            }
            break;   
        case HSSFCell.CELL_TYPE_FORMULA:                               //读取公式   
        	   cellValue=cell.getCellFormula(); 
            break;   
        case HSSFCell.CELL_TYPE_STRING:                              //读取String   
        	cellValue=cell.getStringCellValue();
            break;
    	}
    	
    	return cellValue;
    }
	/**
	 * 获取指定行、列的单元格值
	 * @param rowNum
	 * @param cellNum
	 * @return
	 */
	public String getCellValue(int rowNum,int cellNum){
		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell=row.getCell((short)cellNum);
    	String cellValue=getCellValue(cell);
    	return cellValue;
    }
	
	//浮点数判断
	public  boolean isDecimal(String str) {
		  if(str==null || "".equals(str))
		   return false;  
		  Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
		  return pattern.matcher(str).matches();
		 }
	//整数判断
	public  boolean isInteger(String str){
		  if(str==null )
		   return false;
		  Pattern pattern = Pattern.compile("[0-9]+");
		  return pattern.matcher(str).matches();
		 }
	/**
	 * 获取字符串字节长度 由于java中中英字符都按1个字符，而数据库中汉字按两个字符计算
	 * @param str
	 * @return
	 */
	public int getStrLength(String str){
	  try{	
		if(str==null)
	         return 0;
		else
			 return new String(str.getBytes("gb2312"),"iso-8859-1").length();
	    }
	 catch (Exception e) {
		     writeLog(e);
		     return 0;
	   }
   }
}
