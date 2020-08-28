<%@ page language="java" contentType="text/html; charset=UTF-8" %> 
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ page import="weaver.general.Util" %>
	<%@ page import="morningcore.util.AutoRequestService" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="weaver.general.BaseBean" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs1" class="weaver.conn.RecordSet" scope="page" />
<jsp:useBean id="rs2" class="weaver.conn.RecordSet" scope="page" />
<%!
   public  double subDouble(String m1, String m2) {
        BigDecimal p1 = new BigDecimal(m1);
        BigDecimal p2 = new BigDecimal(m2);
        return p1.subtract(p2).doubleValue();
    }
	public  String  getDatenow() {
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(da);
    }

%>

<%
BaseBean log = new BaseBean();
int userId = user.getUID();
String method = request.getParameter("method");
String ids = request.getParameter("ids");
String desc = request.getParameter("desc");

AutoRequestService ars = new AutoRequestService();
String workflowCode = "2078";
String strJson = "";
String createrid = "" + userId;
String isNext = "1";


String fromSql = " moc_fktzd_view  x ";//sql查询的表   x.djbh,x.requestid,x.sqr,x.sqrq,x.sqbm,x.hzzfje,(select isnull(sum(bcfke),0.00) from formtable_main_251 where  re_ID = x.requestid ) as yfje
String sql = "select x.fktjdm,x.djbh,x.requestid,x.sqr,x.sqrq,x.sqbm,x.hzzfje,(select isnull(sum(bcfke),0.00) from formtable_main_265 where  re_ID = x.requestid ) as yfje,x.gysmc,x.gysdm,x.sc,x.khyh,x.zh,x.pzlx,x.cgsm,x.bzlx,x.type,x.dysy,x.lhh,x.pjqx,x.zffs,x.ydfkr  from  " + fromSql + " where x.requestid in(" + ids +"0)";
if (method.equals("one")){
	rs.executeSql(sql );
	
	JSONObject json = new JSONObject();
	JSONObject header = new JSONObject();
	JSONArray detailsArrDT1 = new JSONArray();
	JSONArray detailsArrDT2 = new JSONArray();
	JSONObject detailsDT = new JSONObject();
	String sqr = "";
	// String lcNo = "";
	String sid = "";
	String sqrbm = "";
	String skdw1 = "";//收款单位(供应商)	付款单 gysmc
	String khyh = "";//开户银行	 khyh
	String yhzh = "";//银行账号	  zh
	String djbh = "";//费控单据号	 djbh
	String kzlx = "";///凭证类型	 pzlx
	String cfrq = getDatenow();//触发日期	
	String bz = "";//备注	 cgsm
	String hb = "";//货币	hb  ---通知单   付款单  bzlx
	String fktjdm = "";//付款条件代码
	String gysdm = "";//供应商编号
	String sc = "";//swift code码
	String dysy = "";//打印事由
	String lhh = "";//联行号
	String pjqx = "";//票据期限（天）
	String zffs = "";//支付方式
	String ydfkr = "";//约定付款日
	String type = "";//采购类型
	
	if(rs.next()){
   		sqr = Util.null2String(rs.getString("sqr"));		
   		djbh = Util.null2String(rs.getString("djbh"));
   		sid = Util.null2String(rs.getString("requestid"));
   		sqrbm = Util.null2String(rs.getString("sqbm"));	
		
   		skdw1 = Util.null2String(rs.getString("gysmc"));	
   		khyh = Util.null2String(rs.getString("khyh"));	
   		yhzh = Util.null2String(rs.getString("zh"));	
   		kzlx = Util.null2String(rs.getString("pzlx"));	
   		bz = Util.null2String(rs.getString("cgsm"));	
   		hb = Util.null2String(rs.getString("bzlx"));
   		fktjdm = Util.null2String(rs.getString("fktjdm"));
		gysdm = Util.null2String(rs.getString("gysdm"));
		sc = Util.null2String(rs.getString("sc"));
		type = Util.null2String(rs.getString("type"));
		dysy = Util.null2String(rs.getString("dysy")); 
lhh = Util.null2String(rs.getString("lhh")); 
pjqx = Util.null2String(rs.getString("pjqx")); 
zffs = Util.null2String(rs.getString("zffs")); 
ydfkr = Util.null2String(rs.getString("ydfkr")); 
		
		
		String ddze = "";//订单总额	ddze
		String cgdd = "";//采购订单	
		String yzfje = "";//已支付金额	
		String cglx = "";//采购类型	
		String djje = "";//冻结金额	
		String wlh = "";//物料号	
		String wzfje = "";//未支付金额				
		String cghtbh = "";//采购合同编号	
		String bczfje = "";//本次支付金额	
		String hzzfje = "";//核准支付金额	
		String yydm = "";//原因代码	
		String yydmbm = "";//原因代码编码
		String hbm = "";//货币码	--	预付无字段
		
		
		// maindt4.ddze,maindt4.cgdd,maindt4.yzfje,maindt4.cglx,maindt4.djje,maindt4.wlh,maindt4.wzfje,maindt4.cghtbh,maindt4.bczfje,maindt4.hzzfje,maindt4.yydm,maindt4.yydmbm

		
		//获取明细表数据
		String tablename = "";
		sql = "Select tablename From Workflow_bill Where id in "
			+"(Select formid From workflow_base Where id= (select workflowid from workflow_requestbase where requestid = '"
			+sid+"' ))";
		log.writeLog("sql1----"+sql);
		rs1.executeSql(sql);
		if(rs1.next()){
			tablename = Util.null2String(rs1.getString("tablename"));

			if("formtable_main_259".equals(tablename)){//采购预付申请
				sql = "select maindt4.ddze,maindt4.cgdd,maindt4.yzfje,maindt4.cglx,maindt4.djje,maindt4.wlh,maindt4.wzfje,maindt4.cghtbh,maindt4.bczfje,maindt4.hzzfje,maindt4.yydmbm from "+ tablename +" main join "+tablename
					+"_dt1 maindt4 on main.id = maindt4.mainid where main.requestid ='"+sid+"'";
				log.writeLog("sql2----"+sql);
				rs2.executeSql(sql);
				while(rs2.next()){
					ddze = Util.null2String(rs2.getString("ddze"));
					cgdd = Util.null2String(rs2.getString("cgdd"));
					yzfje = Util.null2String(rs2.getString("yzfje"));
					cglx = Util.null2String(rs2.getString("cglx"));
					djje = Util.null2String(rs2.getString("djje"));
					wlh = Util.null2String(rs2.getString("wlh"));
					wzfje = Util.null2String(rs2.getString("wzfje"));
					cghtbh = Util.null2String(rs2.getString("cghtbh"));
					bczfje = Util.null2String(rs2.getString("bczfje"));
					hzzfje = Util.null2String(rs2.getString("hzzfje"));
					
					yydmbm = Util.null2String(rs2.getString("yydmbm"));
					
					JSONObject detailsDT2 = new JSONObject();
					
					detailsDT2.put("ddze",ddze); //
					detailsDT2.put("cgdd",cgdd); //
					detailsDT2.put("yzfje",yzfje); //
					detailsDT2.put("cglx",cglx); //
					detailsDT2.put("djje",djje); //
					detailsDT2.put("wlh",wlh); //
					detailsDT2.put("wzfje",wzfje); //
					detailsDT2.put("cghtbh",cghtbh); //
					detailsDT2.put("bczfje",bczfje); //
					detailsDT2.put("hzzfje",hzzfje); //
					
					detailsDT2.put("yydmbm",yydmbm); //
					
					detailsArrDT2.put(detailsDT2);
				}
				header.put("cglx","0" );     //  采购触发类型  0 预付款申请触发  1 付款申请触发
			}else if ("formtable_main_260".equals(tablename)){//采购付款申请
				sql = "select maindt2.ddxe,maindt2.cgdd,maindt2.yzfje,maindt2.cglx,maindt2.djje,maindt2.wlh,maindt2.wzfje,maindt2.cghtbh,maindt2.bczfje,maindt2.hzzfje,maindt2.yydmbm from "+ tablename +" main join "+tablename
					+"_dt2 maindt2 on main.id = maindt2.mainid where main.requestid ='"+sid+"'";
				rs2.executeSql(sql);
				while(rs2.next()){
					ddze = Util.null2String(rs2.getString("ddxe"));
					cgdd = Util.null2String(rs2.getString("cgdd"));
					yzfje = Util.null2String(rs2.getString("yzfje"));
					cglx = Util.null2String(rs2.getString("cglx"));
					djje = Util.null2String(rs2.getString("djje"));
					wlh = Util.null2String(rs2.getString("wlh"));
					wzfje = Util.null2String(rs2.getString("wzfje"));
					cghtbh = Util.null2String(rs2.getString("cghtbh"));
					bczfje = Util.null2String(rs2.getString("bczfje"));
					hzzfje = Util.null2String(rs2.getString("hzzfje"));
					yydm = Util.null2String(rs2.getString("yydm"));
					yydmbm = Util.null2String(rs2.getString("yydmbm"));
					
					JSONObject detailsDT2 = new JSONObject();
					
					detailsDT2.put("ddxe",ddze); //
					detailsDT2.put("cgdd",cgdd); //
					detailsDT2.put("yzfje",yzfje); //
					detailsDT2.put("cglx",cglx); //
					detailsDT2.put("djje",djje); //
					detailsDT2.put("wlh",wlh); //
					detailsDT2.put("wzfje",wzfje); //
					detailsDT2.put("cghtbh",cghtbh); //
					detailsDT2.put("bczfje",bczfje); //
					detailsDT2.put("hzzfje",hzzfje); //
					
					detailsDT2.put("yydmbm",yydmbm); //
					
					detailsArrDT2.put(detailsDT2);
				}
				header.put("cglx","1" );     //  采购触发类型  0 预付款申请触发  1 付款申请触发
			}	
		}

	}
	header.put("sqr",sqr );     //  申请人
	header.put("bcfke",desc );     //  本次付款额
	header.put("re_ID",sid );     //  关联requestid
	header.put("dylc",sid );     //  对应流程
	header.put("skdw1",skdw1 );     //
	header.put("khyh",khyh );     //
	header.put("yhzh",yhzh );     //
	header.put("djbh",djbh );     //
	header.put("kzlx",kzlx );     //
	header.put("cfrq",cfrq );     //
	header.put("bz",bz );     //
	header.put("hb",hb );     //
	header.put("fktjdm",fktjdm );     //
	header.put("gysbh",gysdm );     //
	header.put("sc",sc );     //
		header.put("dysy", dysy);   // 打印事由
	header.put("lhh", lhh);   // 联行号
	header.put("pjqx", pjqx);   // 票据期限（天）
	header.put("zffs", zffs);   // 支付方式
	header.put("ydfkr", ydfkr);   // 约定付款日
	header.put("cglx",type );     //
	//封装数据
	//主表
	json.put("HEADER", header);
	log.writeLog("json----"+json.toString());
	//明细表
	detailsDT.put("DT1",detailsArrDT1);
	detailsDT.put("DT2",detailsArrDT2);
	json.put("DETAILS",detailsDT);
	
    strJson = json.toString();
    //out.print(strJson);
	String s = ars.createRequest(workflowCode,strJson,createrid,isNext);
	response.sendRedirect("financeSure1.jsp?isclose=1");
}else if (method.equals("more")){	
	rs.executeSql(sql );
	String sqr = "";
	// String lcNo = "";
	String sid = "";
	String sqrbm = "";
	String hzzfje = "";//核准总金额
	String yfje = "";
	
	String skdw1 = "";//收款单位(供应商)	付款单 gysmc
	String khyh = "";//开户银行	 khyh
	String yhzh = "";//银行账号	  zh
	String djbh = "";//费控单据号	 djbh
	String kzlx = "";///凭证类型	 pzlx
	String cfrq = getDatenow();//触发日期	
	String bz = "";//备注	 cgsm
	String hb = "";//货币	hb  ---通知单   付款单  bzlx
	String fktjdm = "";//付款条件代码
	String gysdm = "";//供应商编号
	String sc = "";//swift code码
		String dysy = "";//打印事由
	String lhh = "";//联行号
	String pjqx = "";//票据期限（天）
	String zffs = "";//支付方式
	String ydfkr = "";//约定付款日
	String type = "";//采购类型
	
	
	while(rs.next()){
   		sqr = Util.null2String(rs.getString("sqr"));		
   		djbh = Util.null2String(rs.getString("djbh"));
   		sid = Util.null2String(rs.getString("requestid"));
   		sqrbm = Util.null2String(rs.getString("sqbm"));		
   		hzzfje = Util.null2String(rs.getString("hzzfje"));		
   		yfje = Util.null2String(rs.getString("yfje"));		
		
		skdw1 = Util.null2String(rs.getString("gysmc"));	
   		khyh = Util.null2String(rs.getString("khyh"));	
   		yhzh = Util.null2String(rs.getString("zh"));	
   		kzlx = Util.null2String(rs.getString("pzlx"));	
   		bz = Util.null2String(rs.getString("cgsm"));	
   		hb = Util.null2String(rs.getString("bzlx"));
   		fktjdm = Util.null2String(rs.getString("fktjdm"));
		gysdm = Util.null2String(rs.getString("gysdm"));
		sc = Util.null2String(rs.getString("sc"));
				dysy = Util.null2String(rs.getString("dysy")); 
lhh = Util.null2String(rs.getString("lhh")); 
pjqx = Util.null2String(rs.getString("pjqx")); 
zffs = Util.null2String(rs.getString("zffs")); 
ydfkr = Util.null2String(rs.getString("ydfkr")); 
		type = Util.null2String(rs.getString("type"));
		
		
		String ddze = "";//订单总额	ddze
		String cgdd = "";//采购订单	
		String yzfje = "";//已支付金额	
		String cglx = "";//采购类型	
		String djje = "";//冻结金额	
		String wlh = "";//物料号	
		String wzfje = "";//未支付金额				
		String cghtbh = "";//采购合同编号	
		String bczfje = "";//本次支付金额	
		String hzzfjemx = "";//核准支付金额	
		String yydm = "";//原因代码	
		String yydmbm = "";//原因代码编码
		String hbm = "";//货币码	--	预付无字段
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		JSONArray detailsArrDT2 = new JSONArray();
		JSONArray detailsArrDT1 = new JSONArray();
		JSONObject detailsDT = new JSONObject();


		//获取明细表数据
		String tablename = "";
		sql = "Select tablename From Workflow_bill Where id in "
			+"(Select formid From workflow_base Where id= (select workflowid from workflow_requestbase where requestid = '"
			+sid+"' ))";
		rs1.executeSql(sql);
		if(rs1.next()){
			tablename = Util.null2String(rs1.getString("tablename"));

			
			if("formtable_main_259".equals(tablename)){//采购预付申请
				sql = "select maindt4.ddze,maindt4.cgdd,maindt4.yzfje,maindt4.cglx,maindt4.djje,maindt4.wlh,maindt4.wzfje,maindt4.cghtbh,maindt4.bczfje,maindt4.hzzfje,maindt4.yydmbm from "+ tablename +" main join "+tablename
					+"_dt1 maindt4 on main.id = maindt4.mainid where main.requestid ='"+sid+"'";
				rs2.executeSql(sql);
				while(rs2.next()){
					ddze = Util.null2String(rs2.getString("ddze"));
					cgdd = Util.null2String(rs2.getString("cgdd"));
					yzfje = Util.null2String(rs2.getString("yzfje"));
					cglx = Util.null2String(rs2.getString("cglx"));
					djje = Util.null2String(rs2.getString("djje"));
					wlh = Util.null2String(rs2.getString("wlh"));
					wzfje = Util.null2String(rs2.getString("wzfje"));
					cghtbh = Util.null2String(rs2.getString("cghtbh"));
					bczfje = Util.null2String(rs2.getString("bczfje"));
					hzzfjemx = Util.null2String(rs2.getString("hzzfje"));
					
					yydmbm = Util.null2String(rs2.getString("yydmbm"));
					
					JSONObject detailsDT2 = new JSONObject();
					
					detailsDT2.put("ddze",ddze); //
					detailsDT2.put("cgdd",cgdd); //
					detailsDT2.put("yzfje",yzfje); //
					detailsDT2.put("cglx",cglx); //
					detailsDT2.put("djje",djje); //
					detailsDT2.put("wlh",wlh); //
					detailsDT2.put("wzfje",wzfje); //
					detailsDT2.put("cghtbh",cghtbh); //
					detailsDT2.put("bczfje",bczfje); //
					detailsDT2.put("hzzfje",hzzfjemx); //
					
					detailsDT2.put("yydmbm",yydmbm); //
					
					detailsArrDT2.put(detailsDT2);
				}
				header.put("cglx","0" );     //  采购触发类型  0 预付款申请触发  1 付款申请触发
			}else if ("formtable_main_260".equals(tablename)){//采购付款申请
				sql = "select maindt2.ddxe,maindt2.cgdd,maindt2.yzfje,maindt2.cglx,maindt2.djje,maindt2.wlh,maindt2.wzfje,maindt2.cghtbh,maindt2.bczfje,maindt2.hzzfje,maindt2.yydm,maindt2.yydmbm from "+ tablename +" main join "+tablename
					+"_dt2 maindt2 on main.id = maindt2.mainid where main.requestid ='"+sid+"'";
				rs2.executeSql(sql);
				while(rs2.next()){
					ddze = Util.null2String(rs2.getString("ddxe"));
					cgdd = Util.null2String(rs2.getString("cgdd"));
					yzfje = Util.null2String(rs2.getString("yzfje"));
					cglx = Util.null2String(rs2.getString("cglx"));
					djje = Util.null2String(rs2.getString("djje"));
					wlh = Util.null2String(rs2.getString("wlh"));
					wzfje = Util.null2String(rs2.getString("wzfje"));
					cghtbh = Util.null2String(rs2.getString("cghtbh"));
					bczfje = Util.null2String(rs2.getString("bczfje"));
					hzzfjemx = Util.null2String(rs2.getString("hzzfje"));
					yydm = Util.null2String(rs2.getString("yydm"));
					yydmbm = Util.null2String(rs2.getString("yydmbm"));
					
					JSONObject detailsDT2 = new JSONObject();
					
					detailsDT2.put("ddxe",ddze); //
					detailsDT2.put("cgdd",cgdd); //
					detailsDT2.put("yzfje",yzfje); //
					detailsDT2.put("cglx",cglx); //
					detailsDT2.put("djje",djje); //
					detailsDT2.put("wlh",wlh); //
					detailsDT2.put("wzfje",wzfje); //
					detailsDT2.put("cghtbh",cghtbh); //
					detailsDT2.put("bczfje",bczfje); //
					detailsDT2.put("hzzfje",hzzfjemx); //
					detailsDT2.put("yydm",yydm); //
					detailsDT2.put("yydmbm",yydmbm); //
					
					detailsArrDT2.put(detailsDT2);
				}
				
			}		
		}
		double bcfke = subDouble(hzzfje,yfje);
		header.put("sqr",sqr );     //  申请人
		header.put("bcfke",bcfke+"" );     //  本次付款额
		header.put("re_ID",sid );     //  关联requestid
		header.put("dylc",sid );     //  对应流程
		header.put("skdw1",skdw1 );     //
		header.put("khyh",khyh );     //
		header.put("yhzh",yhzh );     //
		header.put("djbh",djbh );     //
		header.put("kzlx",kzlx );     //
		header.put("cfrq",cfrq );     //
		header.put("bz",bz );     //
		header.put("hb",hb );     //
		header.put("fktjdm",fktjdm );     //
		header.put("gysbh",gysdm );     //
		header.put("sc",sc );     //
				header.put("dysy", dysy);   // 打印事由
	header.put("lhh", lhh);   // 联行号
	header.put("pjqx", pjqx);   // 票据期限（天）
	header.put("zffs", zffs);   // 支付方式
	header.put("ydfkr", ydfkr);   // 约定付款日
		header.put("cglx",type);     //
		json.put("HEADER", header);
		detailsDT.put("DT1",detailsArrDT1);
		detailsDT.put("DT2",detailsArrDT2);
		json.put("DETAILS",detailsDT);
	
        strJson = json.toString();
        //out.print(strJson);
		String s = ars.createRequest(workflowCode,strJson,createrid,isNext);	
	}
	response.sendRedirect("financeSure2.jsp?isclose=1");
}


%>