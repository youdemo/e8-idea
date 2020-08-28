package txrz.sysn.org;

import com.weaver.general.Util;
import txrz.sysn.tmc.org.HrmDepartmentBean;
import txrz.sysn.tmc.org.HrmResourceBean;
import txrz.sysn.tmc.org.HrmJobTitleBean;
import txrz.sysn.tmc.org.HrmOrgAction;
import txrz.sysn.tmc.org.ReturnInfo;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * createby jianyong.tang
 * createTime 2020/8/18 19:44
 * version v1
 * desc
 */
public class SysnOrgDataJob extends BaseCronJob {
    public void execute() {

       writeLog("组织结构集成定时开始");
        sysDept();
        sysJobtitle();
        sysPerson();//人事表的都是正式的，批量离职 排除 LT开头的
        writeLog("组织结构集成定时结束");
    }

    public void sysPerson(){
        HrmOrgAction hoa = new HrmOrgAction();
        RecordSet rs = new RecordSet();
        String sql = "select rtrim(EMP_ID) as EMP_ID,CNAME,rtrim(BOSS_EMP_ID) as BOSS_EMP_ID,rtrim(DEP_CODE_FUN) as DEP_CODE_FUN,JOB_NAME1,COD_VAL,EMAIL from HR_DATA.DATAEXG.DBO.TSL_EMP";
        rs.execute(sql);
        while(rs.next()){
            HrmResourceBean hrb = new HrmResourceBean();
            String workcode = Util.null2String(rs.getString("EMP_ID"));
            hrb.setWorkcode(workcode);
            hrb.setLoginid(workcode);
            hrb.setStatus("1");
            hrb.setLastname(Util.null2String(rs.getString("CNAME")));
            hrb.setDeptIdOrCode(1);
            hrb.setDepartmentCode(Util.null2String(rs.getString("DEP_CODE_FUN")));
            // 所属岗位  0 是通过id获取  1是通过code获取
            hrb.setJobIdOrCode(0);
            hrb.setJobtitle(getJobtitle(Util.null2String(rs.getString("JOB_NAME1"))));
            // 上级领导  0 是通过id获取  1是通过code获取      2是通过岗位获取
            hrb.setManagerIdOrCode(1);
            hrb.setManagerid(Util.null2String(rs.getString("BOSS_EMP_ID")));
            hrb.setSeclevel(weaver.general.Util.getIntValue(Util.null2String(rs.getString("COD_VAL"))));
            hrb.setEmail(Util.null2String(rs.getString("EMAIL")));
            ReturnInfo rf = hoa.operResource(hrb);
            if(rf.isTure()){

            }else{
                writeLog("人员同步失败 ：workcode+"+workcode+" result:"+rf.getRemark());
            }
        }
        //批量离职
        sql = "update hrmresource set status=5,loginid=null where status=1 and workcode not like 'LT%' and workcode not in(select EMP_ID from HR_DATA.DATAEXG.DBO.TSL_EMP) ";
        rs.execute(sql);
    }

    public String getJobtitle(String jobtitlename) {
        RecordSet rs = new RecordSet();
        String jobid = "";
        String sql = "select id from HrmJobTitles where jobtitlename='"+jobtitlename+"'";
        rs.execute(sql);
        if(rs.next()) {
            jobid = weaver.general.Util.null2String(rs.getString("id"));
        }
        return jobid;
    }

    public void sysDept(){
        writeLog("部门同步开始");
        HrmOrgAction hoa = new HrmOrgAction();
        RecordSet rs = new RecordSet();
        String dep_name_layer = "";
        String dep_code_fun = "";
        String sql= "select dep_name_layer,rtrim(dep_code_fun) as dep_code_fun from (select distinct dep_name_layer,dep_code_fun from HR_DATA.DATAEXG.DBO.TSL_EMP) a  order by len(dep_name_layer) asc";
        rs.execute(sql);
        while(rs.next()){
            dep_name_layer = Util.null2String(rs.getString("dep_name_layer"));
            dep_code_fun = Util.null2String(rs.getString("dep_code_fun"));
            if(dep_name_layer.indexOf(">")<0){
                String deptid=getdpidbyname(dep_name_layer,"0");
                if(!"".equals(deptid)){
                    updatedeptcode(deptid,dep_code_fun);
                }else {
                    adddept(dep_name_layer, dep_code_fun, "");
                }
            }else{
                String[] nameattr = dep_name_layer.split(">");
               // writeLog("dep_name_layer:"+dep_name_layer);
                int lengtjh = nameattr.length;
                String supdeptid = "0";
                for(int i=0;i<lengtjh;i++){
                    String deptname = nameattr[i];
                   // writeLog("deptname:"+deptname);
                    String deptid = getdpidbyname(deptname,supdeptid);

                    String deptcode = "";
                    if((i+1)==lengtjh){
                        deptcode = dep_code_fun;
                    }else{
                        deptcode = "";
                    }
                   // writeLog("deptid:"+deptid+" deptcode:"+deptcode);
                    if("".equals(deptid)){
                        adddept(deptname,deptcode,supdeptid);
                        deptid = getdpidbyname(deptname,supdeptid);
                    }else{
                        if((i+1)==lengtjh){
                            updatedeptcode(deptid,deptcode);
                        }
                    }
                    supdeptid = deptid;
                }
            }
        }
        writeLog("部门同步结束");
    }

    /**
     * 更新部门编码
     * @param deptid
     * @param deptcode
     */
    public void updatedeptcode(String deptid,String deptcode){
        RecordSet rs = new RecordSet();
        String departmentcode = "";
        String sql = "";
//        String sql = "select departmentcode from hrmdepartment where id="+deptid;
//        rs.execute(sql);
//        if(rs.next()){
//            departmentcode = Util.null2String(rs.getString("departmentcode"));
//        }
        sql = "update hrmdepartment set departmentcode='"+deptcode+"' where id="+deptid;
        rs.execute(sql);
        sql = "update hrmdepartmentdefined set kdbmbm='"+deptcode+"' where deptid="+deptid;
        rs.execute(sql);

    }

    /**
     * 新增部门
     * @param dep_name_layer
     * @param dep_code_fun
     * @param supdeptid
     */
    public void adddept(String dep_name_layer,String dep_code_fun,String supdeptid){
        HrmOrgAction hoa = new HrmOrgAction();
        HrmDepartmentBean hdb = new HrmDepartmentBean();
        hdb.setDepartmentcode(dep_code_fun);
        hdb.setDepartmentname(dep_name_layer);
        hdb.setDepartmentark(dep_name_layer);
        // 分部的获取操作方式     0 是通过id获取  1是通过code获取
        hdb.setComIdOrCode(0);
        hdb.setSubcompanyid1("5");
        // 上级的操作方式     0 是通过id获取  1是通过code获取
        hdb.setIdOrCode(0);
        hdb.setSuperID(supdeptid);

        //排序字段
        hdb.setOrderBy(0);

        hdb.setStatus(0);

        hdb.addCusMap("kdbmbm", dep_code_fun);
        ReturnInfo result = hoa.operDept(hdb);
        if (!result.isTure()) {
            writeLog("部门同步失败 ：dep_name_layer：" + dep_name_layer + " " + result.getRemark());
        }
    }
    public String getdpidbyname(String deptname,String supdeptid){
        RecordSet rs = new RecordSet();
        String deptid = "";
        String sql = "select id from hrmdepartment where departmentname='"+deptname+"' and isnull(supdepid,0)="+supdeptid;
        rs.execute(sql);
        if(rs.next()){
            deptid = Util.null2String(rs.getString("id"));
        }
        return deptid;
    }

    public String getdpid(String departmentname,String deptcode){
        RecordSet rs = new RecordSet();
        String deptid = "";
        String sql = "select id from hrmdepartment where departmentname='"+departmentname+"' and departmentcode='"+deptcode+"'";
        rs.execute(sql);
        if(rs.next()){
            deptid = Util.null2String(rs.getString("id"));
        }
        return deptid;
    }

    public void sysJobtitle(){
        writeLog("同步岗位开始");
        HrmOrgAction hoa = new HrmOrgAction();
        RecordSet rs = new RecordSet();
        String jobtitlename = "";
        String sql = "select distinct job_name1 as jobtitlename from HR_DATA.DATAEXG.DBO.TSL_EMP";
        rs.execute(sql);
        while(rs.next()){
            HrmJobTitleBean hjt = new HrmJobTitleBean();
            jobtitlename = Util.null2String(rs.getString("jobtitlename"));
            hjt.setJobtitlename(jobtitlename);
            hjt.setJobtitlemark(jobtitlename);
            hjt.setSuperJobCode("");
            hjt.setJobactivityName(jobtitlename);
            hjt.setJobGroupName(jobtitlename);
            // 执行结果  可以直接打印result 查看直接结果
            ReturnInfo rf = hoa.operJobtitle(hjt);
            if(!rf.isTure()){
                writeLog("岗位同步失败 ：PLANS：" + jobtitlename + " " + rf.getRemark());
            }
        }
        writeLog("同步岗位结束");
    }
    private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
