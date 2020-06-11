package tj.procedure;

import tj.methodbody.ReceiptInternalPurchase;
import tj.methodbody.ReceivingOutsourcing;
import tj.methodbody.RequestInterPurchase;
import tj.methodbody.RequestOutsourcing;
import weaver.general.BaseBean;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-2 下午11:37:23
 * 类说明
 */
public class HrTriggerImp {
	public String triggerFlowImp(String jsonstr,String flag){
		String retstr = "";
		BaseBean log = new BaseBean();
		log.writeLog("jsonstr-----"+jsonstr+"----flag----"+flag);
		if("TJ-QKD-001".equals(flag)){//请款单（内购）
			RequestInterPurchase rip = new RequestInterPurchase();
			retstr  = rip.triggerProcess(jsonstr, flag);
		}else if("TJ-QKD-002".equals(flag)){//请款单（外购）
			RequestOutsourcing rip = new RequestOutsourcing();
			retstr  = rip.triggerProcess(jsonstr, flag);
		}else if("TJ-SLD-001".equals(flag)){//收料单（内购）
			ReceiptInternalPurchase rcip = new ReceiptInternalPurchase();
			retstr  = rcip.triggerProcess(jsonstr, flag);
		}else if("TJ-SLD-002".equals(flag)){//收料单（外购）
			ReceivingOutsourcing rcop = new ReceivingOutsourcing();
			retstr  = rcop.triggerProcess(jsonstr, flag);
		}
		if(retstr.length()<2){
			retstr = "{\"return_type\":\"E\",\"return_message\":\"流程触发失败\",\"requestid\":\"0\"}";
		}
		return retstr;
		
	}
}
