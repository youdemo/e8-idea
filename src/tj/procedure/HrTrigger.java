package tj.procedure;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-15 上午 09:05:20
 * 台嘉流程触发      
 */
@Path("/TJ")
public class HrTrigger {
	@GET
	@Path("/trigger/get") 
	@Produces("application/json")
	public  String say(){
		return "hello";
	}

	@POST
	@Path("/trigger/post") 
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String  triggerFlow(@FormParam("jsonstr") String jsonstr,
			@FormParam("flag") String flag) {
		HrTriggerImp ai = new HrTriggerImp();
		return ai.triggerFlowImp(jsonstr,flag);
	}

}
