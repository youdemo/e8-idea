
/**
 * QueryFaultException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package gvo.wsclient.costcontrol.emreim;

public class QueryFaultException0 extends java.lang.Exception{
    
    private gvo.wsclient.costcontrol.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.QueryFault faultMessage;
    
    public QueryFaultException0() {
        super("QueryFaultException0");
    }
           
    public QueryFaultException0(java.lang.String s) {
       super(s);
    }
    
    public QueryFaultException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(gvo.wsclient.costcontrol.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.QueryFault msg){
       faultMessage = msg;
    }
    
    public gvo.wsclient.costcontrol.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.QueryFault getFaultMessage(){
       return faultMessage;
    }
}
    