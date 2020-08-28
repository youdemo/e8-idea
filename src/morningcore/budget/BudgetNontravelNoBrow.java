package morningcore.budget;

import java.util.HashMap;
import java.util.Map;

import morningcore.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-8-1 下午2:00:02
 * 类说明
 * 非差旅报销单V2-预算（无预付借款）
 */
public class BudgetNontravelNoBrow implements Action {

	@Override
	public String execute(RequestInfo info) {
		GetUtil gu = new GetUtil();
		InsertUtil iu = new InsertUtil();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "select a.xmmc,a.sqrq,b.fykmbm,a.fycdbm,b.id as bid,b.fykm,b.fycdkm   from "+tablename+" a join "+tablename+"_dt1 b  on a.id = b.mainid where a.requestid = '"+requestid+"'  ";
		rs.executeSql(sql);
		while(rs.next()){
			Map<String,String> map = new HashMap<String, String>();
			String xmbh1 = Util.null2String(rs.getString("xmmc"));//项目名称 xmmc
			String fykmbm = Util.null2String(rs.getString("fykmbm"));//科目编码 fykmbm
			String fycdbm = Util.null2String(rs.getString("fycdbm"));//承担部门 fycdbm
			String sqrr = Util.null2String(rs.getString("sqrq"));//申请日期  sqrq
			String bid = Util.null2String(rs.getString("bid"));//事前明细id
			String fykm = Util.null2String(rs.getString("fycdkm"));//系统科目 
			String jkkm = Util.null2String(rs.getString("fykm"));//建模科目 fykm
			if(sqrr.length()<1){
				sqrr = gu.getNowdate().substring(0,4);
			}else{
				sqrr = sqrr.substring(0,4);
			}
			map.put("dqrid", requestid);//	当前流程id
			map.put("glrid", "");////shrid	关联流程id	
			map.put("xmmc", xmbh1);//xmmc	项目id	
			map.put("nf", sqrr);//nf	    年份	
			map.put("dqmxid", bid);//sqmxid	 事前明细id	
			map.put("glmxid", "");//shmxid	 事后明细id	
			map.put("bm", fycdbm);//bm	    部门	
			map.put("kmbm", fykmbm);//kmbm	  科目编码	
			map.put("zt", "");//zt	  状态 0 
			map.put("jmkm", jkkm);//jmkm  	建模科目
			map.put("xtkm", fykm);//xtkm 	系统科目
			String str = "select c1.groupCtrlId from FnaBudgetfeeType c1 where c1.id = '"+fykm+"' ";
			res.executeSql(str);
			if(res.next()){
				map.put("hbjdid", res.getString("groupCtrlId"));//xtkm 	系统科目
			}
			map.put("crsj", "##CONVERT(varchar,GETDATE(),120)");
			iu.insert(map, "uf_xmyszjb");
		}
		return SUCCESS;
	}

}

