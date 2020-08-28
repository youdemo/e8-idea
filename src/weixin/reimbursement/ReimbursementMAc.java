package weixin.reimbursement;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weixin.util.InsertUtil;
import weixin.util.WxUtil;

public class ReimbursementMAc implements Action {

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean(); 
		log.writeLog("Mbegin------");
		RecordSet rs = new RecordSet(); 
		InsertUtil iu = new InsertUtil();
		WxUtil wu = new WxUtil();
		String workflowID = info.getWorkflowid(); 
		String requestid = info.getRequestid();
		String tableName ="";
		String id ="";
		String bh ="";
		String cbzx = "";
		String gsdm = "";
		int num = 1;
		String sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")"; 
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id,FormID,Costcenter, Site from "+tableName+" where requestid = '"+requestid+"'";
		rs.execute(sql);
		if (rs.next()) {
			id = Util.null2String(rs.getString("id"));
			bh = Util.null2String(rs.getString("FormID"));
			cbzx = Util.null2String(rs.getString("Costcenter"));//成本中心
			gsdm = Util.null2String(rs.getString("Site"));//厂区 
		}
		//gsdm = wu.getSiteName(gsdm);
		sql ="select t.*,(Cost-(round(Cost/(1+sl),2)))  as sj,round((Cost-(round(Cost/(1+sl),2)))*nvl(ExchangeRate1,1),2) as sjrmb,0-nvl(bkdkje,0) as fsbkdkje,round((0-nvl(bkdkje,0))*nvl(ExchangeRate1,1),2) as fsbkdkjermb,round(Cost/(1+sl),2)+nvl(bkdkje,0) as bhsje,round(round(Cost/(1+sl),2)+nvl(bkdkje,0)*nvl(ExchangeRate1,1),2) as bhsjermb from "+tableName+"_dt1 t where mainid = '"+id+"'";
		rs.executeSql(sql);
		while(rs.next()){
			Map<String ,String> map =new HashMap<String, String>();
			String BDID = bh;
			String LINE = String.valueOf(num);
			String VR01 = Util.null2String(rs.getString("bxxmnew"));//选择框
			String EFFF = Util.null2String(rs.getString("StartDate"));
			String EFFT = Util.null2String(rs.getString("EndDate"));
			String DSC1 = Util.null2String(rs.getString("Remark"));
			String CRCD = Util.null2String(rs.getString("Currency"));//币种
			String CRR = Util.null2String(rs.getString("ExchangeRate1"));
			String AEXP = Util.null2String(rs.getString("CostRMB"));//之前是Cost
			String FEA = Util.null2String(rs.getString("Cost"));//之前是CostRMB
			String sj = Util.null2String(rs.getString("sj"));//税金
			String sjrmb = Util.null2String(rs.getString("sjrmb"));//税金
			String fsbkdkje = Util.null2String(rs.getString("fsbkdkje"));//不可抵扣金额负数
			String fsbkdkjermb = Util.null2String(rs.getString("fsbkdkjermb"));//不可抵扣金额负数rmb

			String bhsje = Util.null2String(rs.getString("bhsje"));//不含税金额
			String bhsjermb = Util.null2String(rs.getString("bhsjermb"));//不含税金额rmb
			String DSC2 = Util.null2String(rs.getString("Number2"));
			String fplx = Util.null2String(rs.getString("fplx"));//发票类型
			String MCU = cbzx;//
			String aa = cbzx.substring(0,4);
			String OBJ = wu.getObj("uf_kemu", "kemu", VR01,aa);//Util.null2String(rs.getString("expensess"));//-----
			String SUB = "";
			String AID = "";
			String CO = gsdm;//公司代码
			if(CO.equals("3")){
				CO = "00420"; 
			}else{
				CO ="00418";
			}
			map.put("BDID", BDID);
			map.put("LINE", LINE);
			map.put("VR01", wu.getVro1(VR01));
			map.put("EFFF", EFFF);
			map.put("EFFT", EFFT);
			map.put("DSC1", DSC1);
			map.put("CRCD", wu.getSelect("8060", CRCD));
			map.put("CRR", CRR);
			map.put("AEXP", bhsje);
			map.put("FEA", bhsjermb);
			map.put("DSC2", DSC2);
			map.put("MCU", MCU);
			map.put("OBJ", OBJ);//科目
			map.put("SUB", SUB); 
			map.put("AID", AID);
			map.put("CO", CO);
			num++;
			iu.insert(map, "F550411");
			if(Util.getFloatValue(sj,0)>(float) 0){
                String sjkm = "";
                if("1".equals(fplx)||"2".equals(fplx)||"15".equals(fplx)||"16".equals(fplx)){
                	if("4181".equals(aa)||"4182".equals(aa)||"4183".equals(aa)||"4184".equals(aa)||"4185".equals(aa)){
						sjkm = "418.2560.113";
					}else{
						sjkm = "420.2560.113";
					}
				}else{
					if("4181".equals(aa)||"4182".equals(aa)||"4183".equals(aa)||"4184".equals(aa)||"4185".equals(aa)){
						sjkm = "418.2560.111";
					}else{
						sjkm = "420.2560.111";
					}
				}
				map.put("VR01", "税金 Tax");
				map.put("LINE", num+"");
				map.put("OBJ", sjkm);//科目
				map.put("AEXP", sj);
				map.put("FEA", sjrmb);
				iu.insert(map, "F550411");
				num++;
			}
			if(Util.getFloatValue(fsbkdkje,0)<(float) 0){
				map.put("VR01", "税金转出");
				map.put("LINE", num+"");
				map.put("OBJ", "418.2560.14");//科目
				map.put("AEXP", fsbkdkje);
				map.put("FEA", fsbkdkjermb);
				iu.insert(map, "F550411");
				num++;
			}
		} 
		
		
		return SUCCESS;
	}

}
