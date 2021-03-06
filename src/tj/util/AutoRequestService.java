package tj.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.DetailTableInfo;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.RequestService;
import weaver.soa.workflow.request.Row;

public class AutoRequestService extends BaseBean {

	BaseBean log = new BaseBean();

	public String createRequest(String workflowCode, String strJson,
			String createrid, String isNext) {

		Map<String, String> retMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		// log.writeLog("json" + strJson);
		// 获取触发哪条流程
		String workflowID = workflowCode;
		String sql = "select count(1) as count from workflow_base where id="
				+ workflowID;
		rs.executeSql(sql);
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("count");
		}
		if (count <= 0) {
			retMap.put("return_type", "E");
			retMap.put("return_message", "流程号错误！");
			retMap.put("requestid", "0");

			return getJsonStr(retMap);
		}

		// 解析 json 获取人员的编号 REQ_BP
		String creater = createrid;

		if (creater.length() > 0 && !"1".equals(creater)) {
			sql = "select count(1) as count_cc from hrmresource "
					+ "where id='" + creater + "' and status in(0,1,2,3)";
			rs.executeSql(sql);
			if (rs.next()) {
				int count_cc = rs.getInt("count_cc");
				if (count_cc == 0) {
					creater = "";
				}
			}
		}

		if (creater.length() < 1) {
			retMap.put("return_type", "E");
			retMap.put("return_message", "人员编号无法匹配！");
			retMap.put("requestid", "0");

			return getJsonStr(retMap);
		}

		String requestLevel = "0";
		String remindType = "0";
		String requestid = "";

		// 根据 workflowCode 查询实际的流程

		RequestInfo requestinfo = new RequestInfo();

		// 放主表数据
		MainTableInfo mti = new MainTableInfo();
		try {
			mti = getMainTableInfo(strJson);
		} catch (Exception e2) {
			retMap.put("return_type", "E");
			retMap.put("return_message", "主表Json格式错误！");
			retMap.put("requestid", "0");
			e2.printStackTrace();
			return getJsonStr(retMap);
		}
		if (mti == null) {
			retMap.put("return_type", "E");
			retMap.put("return_message", "主表Json格式错误！");
			retMap.put("requestid", "0");

			return getJsonStr(retMap);
		}

		DetailTableInfo dti = new DetailTableInfo();
		try {
			dti = getDetailTableInfo(strJson);
		} catch (Exception e1) {
			e1.printStackTrace();
			retMap.put("return_type", "E");
			retMap.put("return_message", "明细Json格式错误");
			retMap.put("requestid", "0");
			return getJsonStr(retMap);
		}

		requestinfo.setDetailTableInfo(dti);
		requestinfo.setMainTableInfo(mti);
		if (!"1".equals(isNext)) {
			requestinfo.setIsNextFlow("0");
		}
		requestinfo.setCreatorid(creater);

		requestinfo.setDescription(getRequestName(workflowID, creater));

		requestinfo.setWorkflowid(workflowID);
		requestinfo.setRequestlevel(requestLevel);
		requestinfo.setRemindtype(remindType);
		requestinfo.setObjtype(2);
		RequestService res = new RequestService();
		
		try {
			requestid = res.createRequest(requestinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String return_type = "S";
		String return_message = "";

		int s_requestid = Integer.parseInt(requestid);
		if (s_requestid < 1) {
			return_type = "E";
			if (s_requestid == -1) {
				return_message = "创建流程失败";
			} else if (s_requestid == -2) {
				return_message = "用户没有流程创建权限";
			} else if (s_requestid == -3) {
				return_message = "创建流程基本信息失败";
			} else if (s_requestid == -4) {
				return_message = "保存表单主表信息失败";
			} else if (s_requestid == -4) {
				return_message = "保存表单主表信息失败";
			} else if (s_requestid == -5) {
				return_message = "更新紧急程度失败";
			} else if (s_requestid == -6) {
				return_message = "流程操作者失败";
			} else if (s_requestid == -7) {
				return_message = "流转至下一节点失败";
			} else if (s_requestid == -8) {
				return_message = "节点附加操作失败";
			} else {
				return_message = "创建流程失败";
			}
		} else {
			return_message = "流程创建成功";
		}

		retMap.put("return_type", return_type);
		retMap.put("return_message", return_message);
		retMap.put("requestid", requestid);
		// log.writeLog(getJsonStr(retMap));
		return getJsonStr(retMap);
	}

	/**
	 * 获取流程标题
	 * 
	 * @param workflowID
	 * @param creater
	 * @return
	 */
	private String getRequestName(String workflowID, String creater) {
		String requestName = "";
		String workflowname = "";
		String name = "系统管理员";
		SimpleDateFormat aa = new SimpleDateFormat("yyyy-MM-dd");
		String date = aa.format(new Date());
		String sql = "select lastname from hrmresource where id=" + creater;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		if (rs.next()) {
			name = Util.null2String(rs.getString("lastname"));
		}

		sql = "select workflowname from workflow_base where id=" + workflowID;
		rs.executeSql(sql);
		if (rs.next()) {
			workflowname = Util.null2String(rs.getString("workflowname"));
		}
		requestName = workflowname + "-" + name + "-" + date;
		//requestName = workflowname +"-" + date;
		return requestName;
	}

	/**
	 * 获取明细表信息
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private DetailTableInfo getDetailTableInfo(String jsonStr) throws Exception {
		DetailTableInfo details = new DetailTableInfo();
		JSONObject json = null;
		json = new JSONObject(jsonStr);
		JSONObject dts = json.getJSONObject("DETAILS");
		List<DetailTable> list_detail = new ArrayList<DetailTable>();
		Iterator it_dts = dts.keys();
		int length = 0;
		while (it_dts.hasNext()) {
			length = length + 1;
			it_dts.next();
		}
		for (int j = 1; j <= length; j++) {

			JSONArray arr = null;
			try {
				arr = dts.getJSONArray("DT" + j);
			} catch (Exception e) {
				continue;
			}
			List<Row> list_row = new ArrayList<Row>();
			DetailTable dt = new DetailTable();
			for (int i = 0; i < arr.length(); i++) {
				JSONObject jo = arr.getJSONObject(i);

				Row row = new Row();
				List<Cell> list_cell = new ArrayList<Cell>();
				Iterator it = jo.keys();
				while (it.hasNext()) {
					String key = it.next().toString();
					String value = jo.getString(key);
					//
					Cell cel = new Cell();
					cel.setName(key);
					if (Util.null2String(value).length() > 0) {
						cel.setValue(value);
						list_cell.add(cel);

					}
				}
				int size = list_cell.size();
				Cell cells[] = new Cell[size];
				for (int index = 0; index < list_cell.size(); index++) {
					cells[index] = list_cell.get(index);
				}
				row.setCell(cells);
				row.setId("" + i);
				list_row.add(row);
			}
			int size = list_row.size();
			// if(size == 0) break;
			Row rows[] = new Row[size];
			for (int index = 0; index < list_row.size(); index++) {

				rows[index] = list_row.get(index);
			}
			dt.setRow(rows);
			dt.setId("" + j);
			list_detail.add(dt);
		}
		int size = list_detail.size();
		DetailTable detailtables[] = new DetailTable[size];
		for (int index = 0; index < list_detail.size(); index++) {
			detailtables[index] = list_detail.get(index);
		}
		details.setDetailTable(detailtables);
		return details;

	}

	/**
	 * 获取主表信息
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private MainTableInfo getMainTableInfo(String jsonStr) throws Exception {
		MainTableInfo mti = new MainTableInfo();

		List<Property> list = new ArrayList<Property>();
		JSONObject json = null;
		try {
			json = new JSONObject(jsonStr).getJSONObject("HEADER");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (json == null)
			return null;
		Iterator it = json.keys();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = json.getString(key);
			Property pro = new Property();
			pro.setName(key);
			if (Util.null2String(value).length() > 0) {
				pro.setValue(value);
				list.add(pro);

			}
		}

		int size = list.size();
		if (size == 0)
			return null;

		Property properties[] = new Property[size];
		for (int index = 0; index < list.size(); index++) {
			properties[index] = list.get(index);
		}
		mti.setProperty(properties);
		return mti;
	}

	// map转json格式
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return json.toString();
	}

}
