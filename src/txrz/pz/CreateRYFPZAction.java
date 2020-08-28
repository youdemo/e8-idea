package txrz.pz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import txrz.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-2-25 下午2:20:57
 * 类说明
 */
public class CreateRYFPZAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		BaseBean log = new BaseBean();
		GetUtil gu = new GetUtil();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		PzUtil pu = new PzUtil();
		InsertUtil iu = new InsertUtil();
		String modeid = pu.getModeId("uf_zjb");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String nowTime = sf.format(new Date());
		String tableName = "";
		String djbh = "";//单据编号
		String wsjezh = "";//未税金额
		String sezh = "";//税额总和
		String fpjezh = "";//发票金额总和
//		String qkr = "";//请款人
		String gh = "";//请款人工号
		String name = "";//请款人名
		String bgrgh = "";//保管员gh
		String bgyname = "";//保管员姓名
		String qkdwwb = "";//请款单位描述
		String pzzy = ""; //凭证摘要
		String gsdm = "";//公司代码
		String qkdw = "";//请款单位
		String kmlb = "";//科目类别
		String jfkm = "";
		String jfkmmc = "";
		String mainid = "";
		String dfkm = "";
		String requestnamenew = "";
		String account_name = "";
		String sfsybyj = "";//sfsybyj
		String zjkdh_new = "";//暂借款 单号（新）
		String jine = "";//暂借款金额
		String ybje = "";
		String ytje = "";
		String gysname = "";//供应商名称
		String PortalLoginid = "";//     供应商编码
		String gbdqbm = "";// 国别/地区编码
		String gbdqmc = "";// 国别/地区名称	
		String fylx = "";
		String fylxname = "";//费用类型名称
		int pdsfft = 0;//判断是否分摊
		String fkyh = "";//付款银行
		String fkyhname = "";
		String kdkjehj = "";//可抵扣金额合计  20190731
		String jdkmdm = "";//金蝶科目代码
		String jes = "";//金额 413
		String sfyfp = "";//是否有发票413
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select requestnamenew from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			requestnamenew = Util.null2String(rs.getString("requestnamenew"));
		}
		sql = "select kdkjehj,sfyfp,je,fkyh,jdkmdm,(select lastname from hrmresource where id =isnull((select xm from uf_byjedb where id =a.byjk ),0)) as bgyname," +
				"(select workcode from hrmresource where id =isnull((select xm from uf_byjedb where id =a.byjk ),0)) as bgrgh, (select  u.gbdq from uf_gbdq u where u.id = a.gbdq) as gbdq, (select u.gbdqbm from uf_gbdq u where u.id = a.gbdq) as gbdqbm,ybje,ytje,sfsybyj," +
				"(select gysmc from uf_gysjcb where id = a.gys) as gysname,(select gysbm from uf_gysjcb where id = a.gys) as PortalLoginid," +
				"zjkdh_new,jine,id,djh,wsjezh,sezh,qkr,gh,(select lastname from hrmresource where id=a.qkr) as name," +
				"qkdwms,(select account_name from uf_gsb where id=a.gsb)  as account_name,gsdm,qkdw from "+tableName+" a where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){// 
			djbh = Util.null2String(rs.getString("djh"));
			wsjezh = Util.null2String(rs.getString("wsjezh"));
			sezh = Util.null2String(rs.getString("sezh"));
			gh = Util.null2String(rs.getString("gh"));
			name = Util.null2String(rs.getString("name"));
			qkdwwb = Util.null2String(rs.getString("qkdwms"));
			account_name = Util.null2String(rs.getString("account_name"));
			gsdm = Util.null2String(rs.getString("gsdm"));
			qkdw = Util.null2String(rs.getString("qkdw"));
			mainid = Util.null2String(rs.getString("id"));
			sezh = Util.null2String(rs.getString("sezh"));
			sfsybyj = Util.null2String(rs.getString("sfsybyj"));
			zjkdh_new = Util.null2String(rs.getString("zjkdh_new"));
			jine = Util.null2String(rs.getString("jine"));
			ybje = Util.null2String(rs.getString("ybje"));
			ytje = Util.null2String(rs.getString("ytje"));
			gysname = Util.null2String(rs.getString("gysname"));
			PortalLoginid = Util.null2String(rs.getString("PortalLoginid"));
//			fylx = Util.null2String(rs.getString("fylx"));
			gbdqbm = Util.null2String(rs.getString("gbdqbm"));
			gbdqmc = Util.null2String(rs.getString("gbdq"));
//			fylxname = Util.null2String(rs.getString("fylxname"));
			bgrgh = Util.null2String(rs.getString("bgrgh"));
			bgyname = Util.null2String(rs.getString("bgyname"));
			fkyh = Util.null2String(rs.getString("fkyh"));
			jdkmdm = Util.null2String(rs.getString("jdkmdm"));
			jes = Util.null2String(rs.getString("je"));
			sfyfp = Util.null2String(rs.getString("sfyfp"));// 0 是  1 否 
			kdkjehj = Util.null2String(rs.getString("kdkjehj"));
		}
		fkyhname = gu.getFieldVal("uf_fkyhxx", "khyh", "id", fkyh);
		if("".equals(wsjezh)) {
			wsjezh = "0";
		}	
		if("".equals(sezh)) {
			sezh = "0";
		}	
		if("".equals(jine)) {
			jine = "0";
		}
		if("".equals(ybje)) {
			ybje = "0";
		}
		if("".equals(ytje)) {
			ytje = "0";
		}
		if("".equals(jes)) {
			jes = "0";
		}
		if("".equals(kdkjehj)) {
			kdkjehj = "0";
		}
		sql = "select  ";
		if(sfyfp.equals("0")){
//			sql = sql+wsjezh+"+"+sezh+" as fpjezh";
			sql = sql+wsjezh+"+"+sezh+"+"+kdkjehj+" as fpjezh";
			
		}else{
			sql = sql+jes+"+"+sezh+"+"+kdkjehj+" as fpjezh";
		}
		rs.execute(sql);
		if(rs.next()) {
			fpjezh = Util.null2String(rs.getString("fpjezh"));
		}
		
//		sql = "select kmdm,kmmc,fylx,kmlb from uf_kmxx where fylx = '"+fylx+"'";
//		rs.execute(sql);
//		while(rs.next()){
//			jfkm = Util.null2String(rs.getString("kmdm"));
//			jfkmmc = Util.null2String(rs.getString("kmmc"));
//			kmlb = Util.null2String(rs.getString("kmlb"));
//		}
//		if(kmlb.length()>0){
//			sql = "select kmlb from uf_bmfylxdzb where bmmc = '"+qkdw+"'";
//			rs.execute(sql);
//			if(rs.next()){
//				kmlb = Util.null2String(rs.getString("kmlb"));
//				sql = "select kmdm,kmmc,fylx,hsxm from uf_kmxx where fylx = '"+fylx+"' and kmlb = '"+kmlb+"'";
//				rs.execute(sql);
//				if(rs.next()){
//					jfkm = Util.null2String(rs.getString("kmdm"));
//					jfkmmc = Util.null2String(rs.getString("kmmc"));
////					hsxm = Util.null2String(rs.getString("hsxm"));
//				}
//			}
//		}	
		//////
		
		
//		sql = "select kmlb from uf_bmfylxdzb where bmmc='"+qkdw+"'";
//		rs.executeSql(sql);
//		if(rs.next()) {
//			kmlb = Util.null2String(rs.getString("kmlb"));
//		}
//		if("1".equals(kmlb)) {
//			jfkm = "6602.07.01";
//			jfkmmc = "管理费用_差旅费_员工差旅费";
//		}else{
//			jfkm = "6601.07.01";
//			jfkmmc = "销售费用_差旅费_员工差旅费";
//			
//		}formtable_main_35_dt4  
		sql = "select fylx,(select uf.fylx from uf_fylx uf where uf.id = a.fylx) as fylxname from "+tableName+"_dt4 a where a.mainid = '"+mainid+"'";
		rs.execute(sql);
		while(rs.next()){
			if(fylxname.length()>0){
				fylxname = fylxname+"/" + Util.null2String(rs.getString("fylxname"));
			}else{
				fylxname = Util.null2String(rs.getString("fylxname"));
			}
		}
		pzzy = "付"+qkdwwb+fylxname;
		//借方
//		String hsxmjf = pu.getFZHS(jfkm);
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("xglcid", requestid);//相关流程id
		mapStr.put("PZZ", "AP");//凭证字
		mapStr.put("LSPZH", djbh);//系统临时凭证号
		mapStr.put("PZMC", requestnamenew);//凭证名称
		mapStr.put("PZMBID", workflowID);//凭证模板ID
		mapStr.put("CJSJ", nowTime);//创建时间
		mapStr.put("JDZTBH", gsdm);//金蝶账套编号
		mapStr.put("JDZTMC", account_name);//金蝶账套名称
		mapStr.put("modedatacreatedate", nowDate);
		mapStr.put("modedatacreater", "1");
		mapStr.put("modedatacreatertype", "0");
		mapStr.put("formmodeid", modeid);
		mapStr.put("PZRQ", nowDate);//凭证日期
		//mapStr.put("ZXSJ", bs);//执行时间
		//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
		//mapStr.put("PZZT", bs);//凭证状态
		//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
		//mapStr.put("FHMS", bs);//返回描述	
		int flh = 1;
		sql = "select a.fylx,(select uf.fylx from uf_fylx uf where uf.id = a.fylx) as fylxname,a.bmmc as deptid,(select departmentname from hrmdepartment where id=a.bmmc) as bmmc,je,bmdm from "+tableName+"_dt3 a where mainid='"+mainid+"'";
		rs.executeSql(sql);
		while(rs.next()) {		
			String bmmc = Util.null2String(rs.getString("bmmc"));
			String je = Util.null2String(rs.getString("je"));
			String bmdm = Util.null2String(rs.getString("bmdm"));
			String deptid = Util.null2String(rs.getString("deptid"));
			String fylxdt = Util.null2String(rs.getString("fylx"));
			String fylxnamedt = Util.null2String(rs.getString("fylxname"));
			String pzzydt = "付"+qkdwwb+fylxnamedt;
			//先判断 kmlb 是否有值，没数据的话，就是默认该费用类型对应的科目和名称，如果有，再去部门中确认是哪种科目
			String st = "select kmdm,kmmc,fylx,kmlb from uf_kmxx where fylx = '"+fylxdt+"'";
			res.execute(st);
			log.writeLog("stsql1---"+st);
			while(res.next()){
				jfkm = Util.null2String(res.getString("kmdm"));
				jfkmmc = Util.null2String(res.getString("kmmc"));
				kmlb = Util.null2String(res.getString("kmlb"));
			}
			String hsxmjf = pu.getFZHS(jfkm);
			log.writeLog("kmlb---"+kmlb);
			if(kmlb.length()>0){
				String str = "select kmlb from uf_bmfylxdzb where bmmc = '"+deptid+"'";
				res.execute(str);
				if(res.next()){
					kmlb = Util.null2String(res.getString("kmlb"));
					str = "select kmdm,kmmc,fylx,hsxm from uf_kmxx where fylx = '"+fylxdt+"' and kmlb = '"+kmlb+"'";
					res.execute(str);
					if(res.next()){
						jfkm = Util.null2String(res.getString("kmdm"));
						jfkmmc = Util.null2String(res.getString("kmmc"));
//						hsxm = Util.null2String(rs.getString("hsxm"));
					}
				}
			}
			mapStr.put("lx", "0");//lx			
			if("0".equals(hsxmjf)) {
				mapStr.put("gbdqbm", gbdqbm);//国别/地区编码
				mapStr.put("gbdqmc",gbdqmc);//国别/地区名称
			}else {
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称
			}
			if("1".equals(hsxmjf)) {
				mapStr.put("zybm", gh);//职员编码
				mapStr.put("zymc", name);//职员名称
			}else {
				mapStr.put("zybm", "");//职员编码
				mapStr.put("zymc", "");//职员名称
			}
			if("2".equals(hsxmjf)) {
				mapStr.put("gysbm", PortalLoginid);//供应商编码
				mapStr.put("gysmc", gysname);//供应商名称
			}else {
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
			}
			if("3".equals(hsxmjf)) {
				mapStr.put("bmbm", bmdm);//部门编码
				mapStr.put("bmmc", bmmc);//部门名称
			}else {
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
			}
			mapStr.put("KMBM", jfkm);//科目编码
			mapStr.put("KMMC", jfkmmc);//科目名称
			mapStr.put("JFJE", je);//借方金额
			//mapStr.put("DFJE", bs);//贷方金额
			mapStr.put("ZY", pzzydt);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");	
			flh++;
			pdsfft++;
		}
//		if(kmlb.length()>0 && pdsfft< 1 ){
//			sql = "select kmlb from uf_bmfylxdzb where bmmc = '"+qkdw+"'";
//			rs.execute(sql);
//			if(rs.next()){
//				kmlb = Util.null2String(rs.getString("kmlb"));
//				sql = "select kmdm,kmmc,fylx,hsxm from uf_kmxx where fylx = '"+fylx+"' and kmlb = '"+kmlb+"'";
//				rs.execute(sql);
//				if(rs.next()){
//					jfkm = Util.null2String(rs.getString("kmdm"));
//					jfkmmc = Util.null2String(rs.getString("kmmc"));
//	//				hsxm = Util.null2String(rs.getString("hsxm"));
//				}
//			}
//		}
		if(Util.getFloatValue(sezh, 0)>0) {
			mapStr.put("lx", "0");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称			
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称			
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", "2221.01.01");//科目编码
			mapStr.put("KMMC", "应缴税金-应缴增值税-进项税额");//科目名称
			mapStr.put("JFJE", sezh);//借方金额
			//mapStr.put("DFJE", bs);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
			flh++;
		}
		if(Util.getFloatValue(kdkjehj, 0)>0) {
			mapStr.put("lx", "0");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称			
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称			
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", "2221.01.09");//科目编码
			mapStr.put("KMMC", "应交税费-应交增值税-国内旅客运输进项税");//科目名称
			mapStr.put("JFJE", kdkjehj);//借方金额
			//mapStr.put("DFJE", bs);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
			flh++;
		}
		if(!"0".equals(sfsybyj)&&!"".equals(zjkdh_new)) {
			if(Util.getFloatValue(ytje, 0)>0) {
				mapStr.put("lx", "0");//lx
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称			
				mapStr.put("zybm", "");//职员编码
				mapStr.put("zymc", "");//职员名称
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称			
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				mapStr.put("KMBM", jdkmdm);//科目编码
				mapStr.put("KMMC", fkyhname);//科目名称
//				mapStr.put("KMBM", "1002.01.16");//科目编码
//				mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
				mapStr.put("JFJE", ytje);//借方金额
				//mapStr.put("DFJE", bs);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				flh++;
			}
		}
		String pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='0'";
		rs.executeSql(sql);
		while(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
					Integer.valueOf(pzid));
			
		}
		if(!"0".equals(sfsybyj)&&"".equals(zjkdh_new)) {
			dfkm = "1002.01.16";
//			String hsxmdf = pu.getFZHS(dfkm);
			mapStr = new HashMap<String, String>();
			mapStr.put("xglcid", requestid);//相关流程id
			mapStr.put("PZZ", "AP");//凭证字
			mapStr.put("LSPZH", djbh);//系统临时凭证号
			mapStr.put("PZMC", requestnamenew);//凭证名称
			mapStr.put("PZMBID", workflowID);//凭证模板ID
			mapStr.put("CJSJ", nowTime);//创建时间
			mapStr.put("JDZTBH", gsdm);//金蝶账套编号
			mapStr.put("JDZTMC", account_name);//金蝶账套名称
			mapStr.put("modedatacreatedate", nowDate);
			mapStr.put("modedatacreater", "1");
			mapStr.put("modedatacreatertype", "0");
			mapStr.put("formmodeid", modeid);
			mapStr.put("PZRQ", nowDate);//凭证日期
			//mapStr.put("ZXSJ", bs);//执行时间
			//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
			//mapStr.put("PZZT", bs);//凭证状态
			//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
			//mapStr.put("FHMS", bs);//返回描述			
			mapStr.put("lx", "1");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			mapStr.put("KMBM", jdkmdm);//科目编码
			mapStr.put("KMMC", fkyhname);//科目名称
//			mapStr.put("KMBM", dfkm);//科目编码
//			mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
			//mapStr.put("JFJE", "");//借方金额
			mapStr.put("DFJE", fpjezh);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
		}else if("0".equals(sfsybyj)) {
			dfkm = "1221.05";
			String hsxmdf = pu.getFZHS(dfkm);
			mapStr = new HashMap<String, String>();
			mapStr.put("xglcid", requestid);//相关流程id
			mapStr.put("PZZ", "AP");//凭证字
			mapStr.put("LSPZH", djbh);//系统临时凭证号
			mapStr.put("PZMC", requestnamenew);//凭证名称
			mapStr.put("PZMBID", workflowID);//凭证模板ID
			mapStr.put("CJSJ", nowTime);//创建时间
			mapStr.put("JDZTBH", gsdm);//金蝶账套编号
			mapStr.put("JDZTMC", account_name);//金蝶账套名称
			mapStr.put("modedatacreatedate", nowDate);
			mapStr.put("modedatacreater", "1");
			mapStr.put("modedatacreatertype", "0");
			mapStr.put("formmodeid", modeid);
			mapStr.put("PZRQ", nowDate);//凭证日期
			//mapStr.put("ZXSJ", bs);//执行时间
			//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
			//mapStr.put("PZZT", bs);//凭证状态
			//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
			//mapStr.put("FHMS", bs);//返回描述			
			mapStr.put("lx", "1");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称
			if("1".equals(hsxmdf)) {
				mapStr.put("zybm", bgrgh);//职员编码
				mapStr.put("zymc", bgyname);//职员名称
			}
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", dfkm);//科目编码
			mapStr.put("KMMC", "其他应收款备用金");//科目名称
			//mapStr.put("JFJE", "");//借方金额
			mapStr.put("DFJE", fpjezh);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
		}else if(!"0".equals(sfsybyj)&&!"".equals(zjkdh_new)) {
			if(("0".equals(ybje)&&"0".equals(ytje))||Util.getFloatValue(ytje, 0)>0) {
				dfkm = "1221.06";
				String hsxmdf = pu.getFZHS(dfkm);
				mapStr = new HashMap<String, String>();
				mapStr.put("xglcid", requestid);//相关流程id
				mapStr.put("PZZ", "AP");//凭证字
				mapStr.put("LSPZH", djbh);//系统临时凭证号
				mapStr.put("PZMC", requestnamenew);//凭证名称
				mapStr.put("PZMBID", workflowID);//凭证模板ID
				mapStr.put("CJSJ", nowTime);//创建时间
				mapStr.put("JDZTBH", gsdm);//金蝶账套编号
				mapStr.put("JDZTMC", account_name);//金蝶账套名称
				mapStr.put("modedatacreatedate", nowDate);
				mapStr.put("modedatacreater", "1");
				mapStr.put("modedatacreatertype", "0");
				mapStr.put("formmodeid", modeid);
				mapStr.put("PZRQ", nowDate);//凭证日期
				//mapStr.put("ZXSJ", bs);//执行时间
				//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
				//mapStr.put("PZZT", bs);//凭证状态
				//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
				//mapStr.put("FHMS", bs);//返回描述			
				mapStr.put("lx", "1");//lx
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称
				if("1".equals(hsxmdf)) {
					mapStr.put("zybm", gh);//职员编码
					mapStr.put("zymc", name);//职员名称
				}else {
					mapStr.put("zybm", "");//职员编码
					mapStr.put("zymc", "");//职员名称
				}
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				
				mapStr.put("KMBM", dfkm);//科目编码
				mapStr.put("KMMC", "其他应收款-职员往来");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", jine);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
			}else if(Util.getFloatValue(ybje, 0)>0) {
				dfkm = "1221.06";
				String hsxmdf = pu.getFZHS(dfkm);
				mapStr = new HashMap<String, String>();
				mapStr.put("xglcid", requestid);//相关流程id
				mapStr.put("PZZ", "AP");//凭证字
				mapStr.put("LSPZH", djbh);//系统临时凭证号
				mapStr.put("PZMC", requestnamenew);//凭证名称
				mapStr.put("PZMBID", workflowID);//凭证模板ID
				mapStr.put("CJSJ", nowTime);//创建时间
				mapStr.put("JDZTBH", gsdm);//金蝶账套编号
				mapStr.put("JDZTMC", account_name);//金蝶账套名称
				mapStr.put("modedatacreatedate", nowDate);
				mapStr.put("modedatacreater", "1");
				mapStr.put("modedatacreatertype", "0");
				mapStr.put("formmodeid", modeid);
				mapStr.put("PZRQ", nowDate);//凭证日期
				//mapStr.put("ZXSJ", bs);//执行时间
				//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
				//mapStr.put("PZZT", bs);//凭证状态
				//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
				//mapStr.put("FHMS", bs);//返回描述			
				mapStr.put("lx", "1");//lx
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称
				if("1".equals(hsxmdf)) {
					mapStr.put("zybm", gh);//职员编码
					mapStr.put("zymc", name);//职员名称
				}else {
					mapStr.put("zybm", "");//职员编码
					mapStr.put("zymc", "");//职员名称
				}
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				
				mapStr.put("KMBM", dfkm);//科目编码
				mapStr.put("KMMC", "其他应收款-职员往来");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", jine);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				flh++;
				dfkm = "1002.01.16";
				hsxmdf = pu.getFZHS(dfkm);
				mapStr = new HashMap<String, String>();
				mapStr.put("xglcid", requestid);//相关流程id
				mapStr.put("PZZ", "AP");//凭证字
				mapStr.put("LSPZH", djbh);//系统临时凭证号
				mapStr.put("PZMC", requestnamenew);//凭证名称
				mapStr.put("PZMBID", workflowID);//凭证模板ID
				mapStr.put("CJSJ", nowTime);//创建时间
				mapStr.put("JDZTBH", gsdm);//金蝶账套编号
				mapStr.put("JDZTMC", account_name);//金蝶账套名称
				mapStr.put("modedatacreatedate", nowDate);
				mapStr.put("modedatacreater", "1");
				mapStr.put("modedatacreatertype", "0");
				mapStr.put("formmodeid", modeid);
				mapStr.put("PZRQ", nowDate);//凭证日期
				//mapStr.put("ZXSJ", bs);//执行时间
				//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
				//mapStr.put("PZZT", bs);//凭证状态
				//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
				//mapStr.put("FHMS", bs);//返回描述			
				mapStr.put("lx", "1");//lx
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称
				mapStr.put("zybm", "");//职员编码
				mapStr.put("zymc", "");//职员名称
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				mapStr.put("KMBM", jdkmdm);//科目编码
				mapStr.put("KMMC", fkyhname);//科目名称
//				mapStr.put("KMBM", dfkm);//科目编码
//				mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", ybje);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				
			}
		}
		//贷方
		
		pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='1'";
		rs.executeSql(sql);
		while(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
				Integer.valueOf(pzid));
		}
		
		return SUCCESS;
	}

}
