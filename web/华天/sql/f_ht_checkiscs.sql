create or replace function f_ht_checkiscs(i_requestid in varchar2,i_nodeid in varchar2,i_operatedate in varchar2,i_operatetime in varchar2,i_logid in varchar2)
--i_requestid 流程id i_nodeid节点id i_operatedate提交日期 i_operatetime 提交时间 i_logid记录id
 return varchar is
  v_result varchar(2) := '0';
  v_count integer  :=0;
  v_receivedate varchar2(20);--接收日期
  v_receivetime varchar2(20);--接收时间
  v_operatedate varchar2(20);--提交日期
  v_operatetime varchar2(20);--提交时间
  v_logid varchar2(20);--上一操作id
  v_zcsj_bz number(10,2):=0;--标准操作时长
  v_zcsj number(10,2):=0;--节点操作时长
  v_days integer:=0;--操作天数
begin
  if i_operatedate = '' or i_operatetime = '' then
    return v_result;
  end if;
  v_operatedate := i_operatedate;
  v_operatetime := i_operatetime;
  select czsc into v_zcsj_bz from uf_workflow_csmt where jdid=i_nodeid;
 select count(1) into v_count from workflow_requestlog where requestid=i_requestid and destnodeid=i_nodeid and logtype in('0', '2', '3','i') and logid<i_logid;
 if v_count<=0 then
  return v_result;
 end if;
  select max(logid) into v_logid from workflow_requestlog where requestid=i_requestid and destnodeid=i_nodeid and logtype in('0', '2', '3','i') and logid<i_logid;
  select operatedate,operatetime into v_receivedate,v_receivetime from workflow_requestlog where logid=v_logid;
  select to_date(v_operatedate,'yyyy-mm-dd')-to_date(v_receivedate,'yyyy-mm-dd') into v_days from dual;
  if v_days = 0 then
      if v_receivetime > '22:00:00' then
          return v_result;
      elsif v_receivetime <'08:30:00' then
         v_receivetime := '08:30:00';
      end if;
      if v_operatetime < '08:30:00' then
         return v_result;
      elsif v_operatetime>'22:00:00' then
            v_operatetime :='22:00:00';
      end if;
      select round((to_date(v_operatedate||' '||v_operatetime,'yyyy-mm-dd HH24:mi:ss')-to_date(v_receivedate||' '||v_receivetime,'yyyy-mm-dd HH24:mi:ss'))*24,2) into v_zcsj from dual;

  else
     if v_receivetime > '22:00:00' then
           v_receivetime := '22:00:00';
      elsif v_receivetime <'08:30:00' then
         v_receivetime := '08:30:00';
      end if;
      if v_operatetime < '08:30:00' then
         v_operatetime :='08:30:00';
      elsif v_operatetime>'22:00:00' then
            v_operatetime :='22:00:00';
      end if;
      select (v_days-1)*13+round(((to_date(v_receivedate||' 22:00:00','yyyy-mm-dd HH24:mi:ss')-to_date(v_receivedate||' '||v_receivetime,'yyyy-mm-dd HH24:mi:ss'))*24)+((to_date(v_operatedate||' '||v_operatetime,'yyyy-mm-dd HH24:mi:ss')-to_date(v_operatedate||' 08:30:00','yyyy-mm-dd HH24:mi:ss'))*24),2) into v_zcsj from dual;
  end if;
  if v_zcsj>v_zcsj_bz then
    v_result :='1';
  end if;
  return v_result;
end;
