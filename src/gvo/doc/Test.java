package gvo.doc;

import org.json.JSONObject;
import weaver.interfaces.workflow.action.WorkflowToDoc;

public class Test {


	public static void main(String[] args) throws Exception {
		String oaaddress = "";
		String localip ="10.1.96.16";
		String localiparr = "10.1.96.16:8083,10.1.96.58:8083";
		String  iparr[] = localiparr.split(",");
		for(String ipaddress:iparr){
			if(localip.equals(ipaddress.substring(0,ipaddress.indexOf(":")))){
				oaaddress=ipaddress;
				break;
			}
		}
		System.out.println(oaaddress);
		//WorkflowToDoc
	}

}
