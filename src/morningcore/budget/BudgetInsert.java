package morningcore.budget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import morningcore.util.GetUtil;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-7-4 下午4:12:53
 * 辰芯   预算数据写入
 */
public class BudgetInsert extends BaseCronJob{
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("写入开始-------");
		String tablename_u = "uf_yssjdr";//表单建模预算信息表
		InsertUtil  iu = new InsertUtil();
		String flag = "";//是否已写入的标志  0 未写入  1  写入 
		GetUtil gu = new GetUtil();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String deptid = "";
		int a = 0;
		List<String> list = new ArrayList<String>();
		String SQL = "select * from "+tablename_u +" where zt = 0";
		rs.executeSql(SQL);
		while(rs.next()){
			Map<String ,String > map = new HashMap<String, String>();
			String uid = Util.null2String(rs.getString("id"));//建模id
			String nf = Util.null2String(rs.getString("nf"));//年份
			String fsyf = Util.null2String(rs.getString("fsyf"));//月份
			String rjbm = Util.null2String(rs.getString("rjbm"));//二级部门
			String ejbm = Util.null2String(rs.getString("ejbm"));//三级部门
			String jtsm = Util.null2String(rs.getString("jtsm"));//说明
			String hjje = Util.null2String(rs.getString("hjje"));//合计金额
			String fykmbh = Util.null2String(rs.getString("fykmbh"));//科目编码
 			String zzid = "2";//0：总部； 1：分部； 2：部门； 18004：成本中心；
			String fnayear = gu.getNdqj(nf, fsyf);//预算年度表 id
			if(ejbm.length()>0){
				deptid  = ejbm;
			}else{
				deptid  = rjbm;
			}
			String viso = gu.getViso(deptid, fnayear);//获取版本
//			String xtfkkm = Util.null2String(rs.getString("xtfkkm"));//系统科目
			String xtfkkm = gu.getFieldVal("fnabudgetfeetype", "id", "codeName", fykmbh);//系统科目
			if(a == 0){
				String isEx = gu.checkIfExist(deptid, fnayear);//是否存在该部门，该期间    0 不存在 
				if(!"0".equals(isEx)){
					String str ="update fnabudgetinfo set status ='2' where budgetorganizationid = '"+deptid+"' and organizationtype='2' and budgetperiods = '"+fnayear+"'";
					res.executeSql(str);
				}
				///预算主表
				map.put("budgetstatus", "1");//预算状态	0：未审批； 1：已审批；
				map.put("createrid", "1");//创建人id	
				map.put("approverid", "1");//审批人  通过审批流程生效的预算才有数据
				map.put("approverdate",gu.getNowdate() );//审批日期   通过审批流程生效的预算才有数据
				map.put("budgetorganizationid",deptid);//组织ID  分部：分部id； 部门：部门id； 人员：人员id； 成本中心：成本中心id；-----------
				map.put("organizationtype", zzid);//0：总部； 1：分部； 2：部门； 18004：成本中心；
				map.put("budgetperiods", fnayear);//年度期间ID	 ------
				map.put("revision", viso);//版本
				map.put("status", "1");//状态	0 ：草稿；1 ：生效；2 ：历史；3 ：待审批；
				map.put("remark", jtsm);//备注
				map.put("createdate", gu.getNowdateSM());//创建日期	-
				map.put("opType", "");//生成方式	  j：结转；空：编制；				
				//end
				String mainid = iu.insert(map, "fnabudgetinfo");//预算主表id
				list.add(mainid);
				//预算明细 budgetstatus
				Map<String ,String > mapinfo = new HashMap<String, String>();
				mapinfo.put("budgetinfoid", mainid);//部门预算信息id
				mapinfo.put("budgetperiods", fnayear);//年度期间ID
				mapinfo.put("budgettypeid", xtfkkm);//科目id	 
//				mapinfo.put("budgetperiodslist", gu.getBudgetperiodslist(budgettypeid, fnayear, nowyear, "startdate", "enddate"));//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
				mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
				mapinfo.put("budgetresourceid", "");//人员id		预算相关人力资源
				mapinfo.put("budgetcrmid", "");//客户id				预算相关客户
				mapinfo.put("budgetprojectid", "");//项目id	  		预算相关项目
				mapinfo.put("budgetaccount", hjje);//金额	
				mapinfo.put("budgetremark", jtsm);//备注	
				mapinfo.put("fnaIncrement", "0");//偏差金额		当前版本与上一版本的差额，记录可能存在空数据，空请转换成0； 注：目前只有结转类型的操作会记录该数值，正常编制生成的版本该数值等于0；
				//end	
				iu.insert(mapinfo, "fnabudgetinfodetail");//
				String str = " update "+tablename_u+" set flag = '1' where id = '"+uid+"'";
				res.executeSql(str);				
			}else{
				String isEx = gu.checkIfExist(deptid, fnayear);//是否存在该部门，该期间    0 不存在 
				boolean  isE = list.contains(isEx);
				if(isE){
					String je = "";
					int cn = 0;
					String str = "select count(id) as cn from fnabudgetinfodetail where budgetinfoid = '"+isEx+"' and budgettypeid = '"+xtfkkm+"'";
					res.executeSql(str);
					if(res.next()){
						cn = res.getInt("cn");
					}
					if(cn>0){
						String id = "";
						str = "select id,budgetaccount from fnabudgetinfodetail where budgetinfoid = '"+isEx+"' and budgettypeid = '"+xtfkkm+"'";
						res.executeSql(str);
						if(res.next()){
							je = res.getString("budgetaccount");
							id = res.getString("id");
						}
						double jenew = gu.addDouble(hjje, je);
						str = "update fnabudgetinfodetail set budgetaccount = '"+jenew+"' where id = '"+id+"'";
						res.executeSql(str);						
					}else{
						//预算明细
						Map<String ,String > mapinfo = new HashMap<String, String>();
						mapinfo.put("budgetinfoid", isEx);//部门预算信息id
						mapinfo.put("budgetperiods", fnayear);//年度期间ID
						mapinfo.put("budgettypeid", xtfkkm);//科目id	 
						mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
						mapinfo.put("budgetresourceid", "");//人员id		预算相关人力资源
						mapinfo.put("budgetcrmid", "");//客户id				预算相关客户
						mapinfo.put("budgetprojectid", "");//项目id	  		预算相关项目
						mapinfo.put("budgetaccount", hjje);//金额	
						mapinfo.put("budgetremark", jtsm);//备注	
						mapinfo.put("fnaIncrement", "0");//偏差金额		当前版本与上一版本的差额，记录可能存在空数据，空请转换成0； 注：目前只有结转类型的操作会记录该数值，正常编制生成的版本该数值等于0；
						//end	
						iu.insert(mapinfo, "fnabudgetinfodetail");//
					}
					str = " update "+tablename_u+" set flag = '1' where id = '"+uid+"'";
					res.executeSql(str);
				}else{
					
					if(!"0".equals(isEx)){
						String strs ="update fnabudgetinfo set status ='2' where budgetorganizationid = '"+deptid+"' and organizationtype='2' and budgetperiods = '"+fnayear+"'";
						res.executeSql(strs);
					}
					
					///预算主表
					map.put("budgetstatus", "1");//预算状态	0：未审批； 1：已审批；
					map.put("createrid", "1");//创建人id	
					map.put("approverid", "1");//审批人  通过审批流程生效的预算才有数据
					map.put("approverdate",gu.getNowdate() );//审批日期   通过审批流程生效的预算才有数据
					map.put("budgetorganizationid",deptid);//组织ID  分部：分部id； 部门：部门id； 人员：人员id； 成本中心：成本中心id；-----------
					map.put("organizationtype", zzid);//0：总部； 1：分部； 2：部门； 18004：成本中心；
					map.put("budgetperiods", fnayear);//年度期间ID	 ------
					map.put("revision", viso);//版本
					map.put("status", "1");//状态	0 ：草稿；1 ：生效；2 ：历史；3 ：待审批；
					map.put("remark", jtsm);//备注
					map.put("createdate", gu.getNowdateSM());//创建日期	-
					map.put("opType", "");//生成方式	  j：结转；空：编制；				
					//end
					String mainid = iu.insert(map, "fnabudgetinfo");//预算主表id
					list.add(mainid);
					//预算明细
					Map<String ,String > mapinfo = new HashMap<String, String>();
					mapinfo.put("budgetinfoid", mainid);//部门预算信息id
					mapinfo.put("budgetperiods", fnayear);//年度期间ID
					mapinfo.put("budgettypeid", xtfkkm);//科目id	 
					mapinfo.put("budgetperiodslist", "1");//期间id		月度科目：1~12；季度科目：1~4；半年度科目：1~2；年度科目：1；
					mapinfo.put("budgetresourceid", "");//人员id		预算相关人力资源
					mapinfo.put("budgetcrmid", "");//客户id				预算相关客户
					mapinfo.put("budgetprojectid", "");//项目id	  		预算相关项目
					mapinfo.put("budgetaccount", hjje);//金额	
					mapinfo.put("budgetremark", jtsm);//备注	
					mapinfo.put("fnaIncrement", "0");//偏差金额		当前版本与上一版本的差额，记录可能存在空数据，空请转换成0； 注：目前只有结转类型的操作会记录该数值，正常编制生成的版本该数值等于0；
					//end	
					iu.insert(mapinfo, "fnabudgetinfodetail");//
					String str = " update "+tablename_u+" set flag = '1' where id = '"+uid+"'";
					res.executeSql(str);
				}
			}
			a++;
		}//while
		log.writeLog("写入结束--新增主表id--list---"+list.toString());
		
	}
}
