package morningcore.hr.sysn.service;

import morningcore.hr.sysn.tmc.org.HrmJobTitleBean;
import morningcore.hr.sysn.tmc.org.HrmOrgAction;
import morningcore.hr.sysn.tmc.org.HrmResourceBean;
import morningcore.hr.sysn.tmc.org.ReturnInfo;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * createby jianyong.tang
 * createTime
 * version v1
 * desc
 */
public class SysnHrInfo extends BaseCronJob {
    public void execute(){
        BaseBean log = new BaseBean();
        log.writeLog("SysnHrInfo","开始同步岗位信息");
        sysnJobtitle();
        log.writeLog("SysnHrInfo","同步岗位信息结束");
        log.writeLog("SysnHrInfo","开始同步人员信息");
        sysnPersonInfo();
        log.writeLog("SysnHrInfo","同步人员信息结束");
    }
    public void sysnJobtitle(){
        HrmOrgAction hoa = new HrmOrgAction();
        BaseBean log = new BaseBean();
        RecordSetDataSource rsd = new RecordSetDataSource("SHCX");//使用OA集成模块数据源
        String sql = "select jobtitlename,b.jobactivityname,c.jobgroupname from HrmJobTitles a left join HrmJobActivities b on a.jobactivityid=b.id left join HrmJobGroups c on b.jobgroupid=c.id";
        rsd.execute(sql);
        while (rsd.next()){
            HrmJobTitleBean hjt = new HrmJobTitleBean();
            String jobtitlename = Util.null2String(rsd.getString("jobtitlename"));
            hjt.setJobtitlename(jobtitlename);
           // hjt.setJobtitlemark(rsd.getString("jobtitlename"));
            hjt.setJobactivityName(Util.null2String(rsd.getString("jobactivityname")));
            hjt.setJobGroupName(Util.null2String(rsd.getString("jobgroupname")));
            ReturnInfo result = hoa.operJobtitle(hjt);
            if(!result.isTure()){
                log.writeLog("执行失败！失败详细 jobtitlename:"+jobtitlename+"："+result.getRemark());
            }

        }

    }

    public void sysnPersonInfo(){
        BaseBean log = new BaseBean();
        HrmOrgAction hoa = new HrmOrgAction();
        RecordSetDataSource rsd = new RecordSetDataSource("SHCX");
        RecordSetDataSource rsd_dt = new RecordSetDataSource("SHCX");
        RecordSet rs = new RecordSet();
        String sql_bd = "";
        String sql_dt = "";
        String sql = "select t.*,(select workcode from hrmresource where id=isnull(t.belongto,-1)) as belongtoworkcode,(select workcode from hrmresource where id=isnull(t.managerid,-1)) as mangercode,(select jobtitlename from HrmJobTitles where id=t.jobtitle) as jobtitlename,(select ldaplogin from ldap_multiset where id=t.Ldapid) as ldaplogin from hrmresource t ";
        rsd.execute(sql);
        while(rsd.next()){
            HrmResourceBean hrb = new HrmResourceBean();
            String workcode = Util.null2String(rsd.getString("workcode"));
            if("".equals(workcode)){
                log.writeLog("执行失败！失败详细 workcode 不能为空 id:"+ rsd.getString("id"));
                continue;
            }
            hrb.setWorkcode(workcode);
            hrb.setLoginid(Util.null2String(rsd.getString("loginid")));
            hrb.setLastname(Util.null2String(rsd.getString("lastname")));
            //hrb.setPassword(Util.null2String(rsd.getString("password")));
            hrb.setStatus(Util.null2String(rsd.getString("status")));
            //ad信息
            String Ldapid = "";
            String ldaplogin = Util.null2String(rsd.getString("ldaplogin"));
            sql_bd = "select Ldapid from ldap_multiset where ldaplogin='"+ldaplogin+"'";
            rs.execute(sql);
            if(rs.next()){
                Ldapid = Util.null2String(rs.getString("Ldapid"));
            }
            hrb.setLdapid(Ldapid);
            hrb.setIsAdAccount(Util.null2String(rsd.getString("isAdAccount")));
            // 所属分部   部门所对应的分部   省略
            // 所属部门  0 是通过id获取  1是通过code获取
            //部门
            hrb.setDeptIdOrCode(0);
            hrb.setDepartmentid(Util.null2String(rsd.getString("departmentid")));
            // 所属岗位  0 是通过id获取  1是通过code获取
            //岗位
            hrb.setJobIdOrCode(1);
            hrb.setJobtitleCode(Util.null2String(rsd.getString("jobtitlename")));
            // 上级领导  0 是通过id获取  1是通过code获取      2是通过岗位获取
            hrb.setManagerIdOrCode(1);
            hrb.setManagerCode(Util.null2String(rsd.getString("mangercode")));
            hrb.setSeclevel(Util.getIntValue(rsd.getString("Seclevel"),10));

            //次账号

            String belongtoworkcode = Util.null2String(rsd.getString("belongtoworkcode"));
            if(!"".equals(belongtoworkcode)) {
                hrb.setBelongIdOrCode(1);
                hrb.setBelongtoCode(rsd.getString("belongtoworkcode"));
            }
            /**
             *  当然可以下还有 都可以设置  在HrmResourceBean中，可以set值
             */
            hrb.setSex(Util.null2String(rsd.getString("sex")));
            hrb.setBirthday(Util.null2String(rsd.getString("birthday")));
            hrb.setNationality(Util.null2String(rsd.getString("nationality")));
            hrb.setSystemlanguage(Util.null2String(rsd.getString("systemlanguage")));
            hrb.setMaritalstatus(Util.null2String(rsd.getString("maritalstatus")));
            hrb.setTelephone(Util.null2String(rsd.getString("telephone")));
            hrb.setMobile(Util.null2String(rsd.getString("mobile")));
            hrb.setMobilecall(Util.null2String(rsd.getString("mobilecall")));
            hrb.setEmail(Util.null2String(rsd.getString("email")));
             String dsporder=Util.null2String(rsd.getString("dsporder"));
            if("".equals(dsporder)){
                dsporder="0";
            }
            hrb.setDsporder(Util.getFloatValue(dsporder,0));
            hrb.setCreatedate(Util.null2String(rsd.getString("createdate")));
            hrb.setLocationid(Util.null2String(rsd.getString("locationid")));
            hrb.setWorkroom(Util.null2String(rsd.getString("workroom")));
            hrb.setHomeaddress(Util.null2String(rsd.getString("homeaddress")));
            hrb.setStartdate(Util.null2String(rsd.getString("startdate")));
            hrb.setEnddate(Util.null2String(rsd.getString("enddate")));
            hrb.setDatefield1(Util.null2String(rsd.getString("datefield1")));
            hrb.setDatefield2(Util.null2String(rsd.getString("datefield2")));
            hrb.setDatefield3(Util.null2String(rsd.getString("datefield3")));
            hrb.setDatefield4(Util.null2String(rsd.getString("datefield4")));
            hrb.setDatefield5(Util.null2String(rsd.getString("datefield5")));
            hrb.setNumberfield1(Util.null2String(rsd.getString("numberfield1")));
            hrb.setNumberfield2(Util.null2String(rsd.getString("numberfield2")));
            hrb.setNumberfield3(Util.null2String(rsd.getString("numberfield3")));
            hrb.setNumberfield4(Util.null2String(rsd.getString("numberfield4")));
            hrb.setNumberfield5(Util.null2String(rsd.getString("numberfield5")));
            hrb.setTextfield1(Util.null2String(rsd.getString("textfield1")));
            hrb.setTextfield2(Util.null2String(rsd.getString("textfield2")));
            hrb.setTextfield3(Util.null2String(rsd.getString("textfield3")));
            hrb.setTextfield4(Util.null2String(rsd.getString("textfield4")));
            hrb.setTextfield5(Util.null2String(rsd.getString("textfield5")));

            hrb.setTinyintfield1(Util.null2String(rsd.getString("tinyintfield1")));
            hrb.setTinyintfield1(Util.null2String(rsd.getString("tinyintfield2")));
            hrb.setTinyintfield1(Util.null2String(rsd.getString("tinyintfield3")));
            hrb.setTinyintfield1(Util.null2String(rsd.getString("tinyintfield4")));
            hrb.setTinyintfield1(Util.null2String(rsd.getString("tinyintfield5")));
            hrb.setJobactivitydesc(Util.null2String(rsd.getString("jobactivitydesc")));
            hrb.setCertificatenum(Util.null2String(rsd.getString("certificatenum")));
            hrb.setNativeplace(Util.null2String(rsd.getString("nativeplace")));
            hrb.setEducationlevel(Util.null2String(rsd.getString("educationlevel")));
            hrb.setResidentplace(Util.null2String(rsd.getString("residentplace")));
            hrb.setPolicy(Util.null2String(rsd.getString("policy")));
            hrb.setDegree(Util.null2String(rsd.getString("degree")));
            hrb.setHeight(Util.null2String(rsd.getString("height")));
            hrb.setAccumfundaccount(Util.null2String(rsd.getString("accumfundaccount")));
            hrb.setBirthplace(Util.null2String(rsd.getString("birthplace")));
            hrb.setFolk(Util.null2String(rsd.getString("folk")));
            hrb.setExtphone(Util.null2String(rsd.getString("extphone")));
            hrb.setFax(Util.null2String(rsd.getString("fax")));
            hrb.setWeight(Util.null2String(rsd.getString("weight")));
            hrb.setTempresidentnumber(Util.null2String(rsd.getString("tempresidentnumber")));
            hrb.setProbationenddate(Util.null2String(rsd.getString("probationenddate")));
            hrb.setBankid1(Util.null2String(rsd.getString("bankid1")));
            hrb.setAccountid1(Util.null2String(rsd.getString("accountid1")));
            hrb.setJoblevel(Util.null2String(rsd.getString("joblevel")));
            hrb.setCostcenterid(Util.null2String(rsd.getString("costcenterid")));
            hrb.setAssistantid(Util.null2String(rsd.getString("assistantid")));

            //自定义信息
            sql_dt = "select * from cus_fielddata where scopeid=-1 and id="+rsd.getString("id");
            rsd_dt.execute(sql_dt);
            if(rsd_dt.next()) {
                hrb.addCusMap("field0",Util.null2String(rsd_dt.getString("field0")));
                hrb.addCusMap("field1",Util.null2String(rsd_dt.getString("field1")));
                //自定义字段名称，值
				
				hrb.addCusMap("field3",Util.null2String(rsd_dt.getString("field3")));
				
				hrb.addCusMap("field5",Util.null2String(rsd_dt.getString("field5")));
				hrb.addCusMap("field6",Util.null2String(rsd_dt.getString("field6")));
				hrb.addCusMap("field7",Util.null2String(rsd_dt.getString("field7")));
				hrb.addCusMap("field8",Util.null2String(rsd_dt.getString("field8")));
				hrb.addCusMap("field9",Util.null2String(rsd_dt.getString("field9")));
				hrb.addCusMap("field10",Util.null2String(rsd_dt.getString("field10")));
				hrb.addCusMap("field11",Util.null2String(rsd_dt.getString("field11")));
				hrb.addCusMap("field12",Util.null2String(rsd_dt.getString("field12")));
				hrb.addCusMap("field13",Util.null2String(rsd_dt.getString("field13")));
				hrb.addCusMap("field14",Util.null2String(rsd_dt.getString("field14")));
				hrb.addCusMap("field15",Util.null2String(rsd_dt.getString("field15")));
				hrb.addCusMap("field16",Util.null2String(rsd_dt.getString("field16")));
				hrb.addCusMap("field17",Util.null2String(rsd_dt.getString("field17")));
				hrb.addCusMap("field18",Util.null2String(rsd_dt.getString("field18")));
				hrb.addCusMap("field19",Util.null2String(rsd_dt.getString("field19")));
				hrb.addCusMap("field20",Util.null2String(rsd_dt.getString("field20")));
				hrb.addCusMap("field21",Util.null2String(rsd_dt.getString("field21")));
				hrb.addCusMap("field22",Util.null2String(rsd_dt.getString("field22")));
				hrb.addCusMap("field23",Util.null2String(rsd_dt.getString("field23")));
				hrb.addCusMap("field24",Util.null2String(rsd_dt.getString("field24")));
				hrb.addCusMap("field25",Util.null2String(rsd_dt.getString("field25")));
				hrb.addCusMap("field26",Util.null2String(rsd_dt.getString("field26")));
				hrb.addCusMap("field27",Util.null2String(rsd_dt.getString("field27")));
				hrb.addCusMap("field28",Util.null2String(rsd_dt.getString("field28")));
				hrb.addCusMap("field29",Util.null2String(rsd_dt.getString("field29")));
				hrb.addCusMap("field30",Util.null2String(rsd_dt.getString("field30")));
				hrb.addCusMap("field31",Util.null2String(rsd_dt.getString("field31")));
				hrb.addCusMap("field32",Util.null2String(rsd_dt.getString("field32")));
				hrb.addCusMap("field33",Util.null2String(rsd_dt.getString("field33")));
				hrb.addCusMap("field34",Util.null2String(rsd_dt.getString("field34")));
				hrb.addCusMap("field35",Util.null2String(rsd_dt.getString("field35")));
				hrb.addCusMap("field36",Util.null2String(rsd_dt.getString("field36")));
				hrb.addCusMap("field37",Util.null2String(rsd_dt.getString("field37")));
				hrb.addCusMap("field38",Util.null2String(rsd_dt.getString("field38")));
				hrb.addCusMap("field39",Util.null2String(rsd_dt.getString("field39")));
				hrb.addCusMap("field40",Util.null2String(rsd_dt.getString("field40")));
				hrb.addCusMap("field41",Util.null2String(rsd_dt.getString("field41")));
				hrb.addCusMap("field42",Util.null2String(rsd_dt.getString("field42")));
				hrb.addCusMap("field43",Util.null2String(rsd_dt.getString("field43")));
				hrb.addCusMap("field44",Util.null2String(rsd_dt.getString("field44")));
				hrb.addCusMap("field45",Util.null2String(rsd_dt.getString("field45")));
				hrb.addCusMap("field46",Util.null2String(rsd_dt.getString("field46")));
				hrb.addCusMap("field47",Util.null2String(rsd_dt.getString("field47")));
				hrb.addCusMap("field48",Util.null2String(rsd_dt.getString("field48")));
				hrb.addCusMap("field49",Util.null2String(rsd_dt.getString("field49")));
				hrb.addCusMap("field50",Util.null2String(rsd_dt.getString("field50")));
				hrb.addCusMap("field51",Util.null2String(rsd_dt.getString("field51")));
				hrb.addCusMap("field52",Util.null2String(rsd_dt.getString("field52")));
				hrb.addCusMap("field53",Util.null2String(rsd_dt.getString("field53")));
				hrb.addCusMap("field54",Util.null2String(rsd_dt.getString("field54")));
				hrb.addCusMap("field55",Util.null2String(rsd_dt.getString("field55")));
				hrb.addCusMap("field56",Util.null2String(rsd_dt.getString("field56")));
				hrb.addCusMap("field57",Util.null2String(rsd_dt.getString("field57")));
				hrb.addCusMap("field58",Util.null2String(rsd_dt.getString("field58")));
				hrb.addCusMap("field59",Util.null2String(rsd_dt.getString("field59")));
				hrb.addCusMap("field60",Util.null2String(rsd_dt.getString("field60")));
				hrb.addCusMap("field61",Util.null2String(rsd_dt.getString("field61")));
				hrb.addCusMap("field62",Util.null2String(rsd_dt.getString("field62")));
				hrb.addCusMap("field63",Util.null2String(rsd_dt.getString("field63")));
				hrb.addCusMap("field64",Util.null2String(rsd_dt.getString("field64")));
				hrb.addCusMap("field65",Util.null2String(rsd_dt.getString("field65")));
				hrb.addCusMap("field66",Util.null2String(rsd_dt.getString("field66")));
				hrb.addCusMap("field67",Util.null2String(rsd_dt.getString("field67")));
				hrb.addCusMap("field68",Util.null2String(rsd_dt.getString("field68")));
				hrb.addCusMap("field69",Util.null2String(rsd_dt.getString("field69")));
				hrb.addCusMap("field70",Util.null2String(rsd_dt.getString("field70")));
				hrb.addCusMap("field71",Util.null2String(rsd_dt.getString("field71")));
				hrb.addCusMap("field72",Util.null2String(rsd_dt.getString("field72")));
				hrb.addCusMap("field73",Util.null2String(rsd_dt.getString("field73")));
				hrb.addCusMap("field74",Util.null2String(rsd_dt.getString("field74")));
				hrb.addCusMap("field75",Util.null2String(rsd_dt.getString("field75")));
				hrb.addCusMap("field76",Util.null2String(rsd_dt.getString("field76")));
				hrb.addCusMap("field77",Util.null2String(rsd_dt.getString("field77")));
				hrb.addCusMap("field78",Util.null2String(rsd_dt.getString("field78")));
				hrb.addCusMap("field79",Util.null2String(rsd_dt.getString("field79")));
				hrb.addCusMap("field80",Util.null2String(rsd_dt.getString("field80")));
				hrb.addCusMap("field81",Util.null2String(rsd_dt.getString("field81")));
				hrb.addCusMap("field82",Util.null2String(rsd_dt.getString("field82")));
				hrb.addCusMap("field83",Util.null2String(rsd_dt.getString("field83")));
				hrb.addCusMap("field84",Util.null2String(rsd_dt.getString("field84")));
				hrb.addCusMap("field85",Util.null2String(rsd_dt.getString("field85")));
				hrb.addCusMap("field86",Util.null2String(rsd_dt.getString("field86")));
				hrb.addCusMap("field87",Util.null2String(rsd_dt.getString("field87")));
				hrb.addCusMap("field88",Util.null2String(rsd_dt.getString("field88")));
				hrb.addCusMap("field89",Util.null2String(rsd_dt.getString("field89")));
				hrb.addCusMap("field90",Util.null2String(rsd_dt.getString("field90")));
				hrb.addCusMap("field91",Util.null2String(rsd_dt.getString("field91")));
				hrb.addCusMap("field92",Util.null2String(rsd_dt.getString("field92")));
				hrb.addCusMap("field93",Util.null2String(rsd_dt.getString("field93")));
				hrb.addCusMap("field94",Util.null2String(rsd_dt.getString("field94")));
				hrb.addCusMap("field95",Util.null2String(rsd_dt.getString("field95")));
				hrb.addCusMap("field96",Util.null2String(rsd_dt.getString("field96")));
				hrb.addCusMap("field97",Util.null2String(rsd_dt.getString("field97")));
				hrb.addCusMap("field98",Util.null2String(rsd_dt.getString("field98")));
				hrb.addCusMap("field99",Util.null2String(rsd_dt.getString("field99")));
				hrb.addCusMap("field100",Util.null2String(rsd_dt.getString("field100")));
				hrb.addCusMap("field101",Util.null2String(rsd_dt.getString("field101")));
				hrb.addCusMap("field102",Util.null2String(rsd_dt.getString("field102")));
				hrb.addCusMap("field103",Util.null2String(rsd_dt.getString("field103")));
				hrb.addCusMap("field104",Util.null2String(rsd_dt.getString("field104")));
				hrb.addCusMap("field105",Util.null2String(rsd_dt.getString("field105")));
				hrb.addCusMap("field106",Util.null2String(rsd_dt.getString("field106")));
				hrb.addCusMap("field107",Util.null2String(rsd_dt.getString("field107")));
				hrb.addCusMap("field108",Util.null2String(rsd_dt.getString("field108")));
				hrb.addCusMap("field109",Util.null2String(rsd_dt.getString("field109")));
				hrb.addCusMap("field110",Util.null2String(rsd_dt.getString("field110")));
				hrb.addCusMap("field111",Util.null2String(rsd_dt.getString("field111")));
				hrb.addCusMap("field112",Util.null2String(rsd_dt.getString("field112")));
				hrb.addCusMap("field113",Util.null2String(rsd_dt.getString("field113")));
				hrb.addCusMap("field114",Util.null2String(rsd_dt.getString("field114")));
				hrb.addCusMap("field115",Util.null2String(rsd_dt.getString("field115")));
				hrb.addCusMap("field116",Util.null2String(rsd_dt.getString("field116")));
				hrb.addCusMap("field117",Util.null2String(rsd_dt.getString("field117")));
				hrb.addCusMap("field118",Util.null2String(rsd_dt.getString("field118")));
				hrb.addCusMap("field119",Util.null2String(rsd_dt.getString("field119")));
				hrb.addCusMap("field120",Util.null2String(rsd_dt.getString("field120")));
				hrb.addCusMap("field121",Util.null2String(rsd_dt.getString("field121")));
				hrb.addCusMap("field122",Util.null2String(rsd_dt.getString("field122")));
				hrb.addCusMap("field123",Util.null2String(rsd_dt.getString("field123")));
				hrb.addCusMap("field124",Util.null2String(rsd_dt.getString("field124")));
				hrb.addCusMap("field125",Util.null2String(rsd_dt.getString("field125")));
				hrb.addCusMap("field126",Util.null2String(rsd_dt.getString("field126")));
				hrb.addCusMap("field127",Util.null2String(rsd_dt.getString("field127")));
				hrb.addCusMap("field128",Util.null2String(rsd_dt.getString("field128")));
				hrb.addCusMap("field129",Util.null2String(rsd_dt.getString("field129")));
				hrb.addCusMap("field130",Util.null2String(rsd_dt.getString("field130")));
            }
            // 执行结果  可以直接打印result 查看直接结果
            ReturnInfo result = hoa.operResource(hrb);
            if(!result.isTure()){
                log.writeLog("执行失败！失败详细 workcode:"+workcode+"："+result.getRemark());
            }
        }
    }
}
