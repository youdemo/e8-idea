create or replace function f_fna_getbzje(i_xm      in varchar2,
                                         i_nr    in varchar2,
                                         i_ksrq    in varchar2,
                                         i_jsrq    in varchar2,
                                         i_ccd     in varchar2,
                                         i_zw      in varchar2,
                                         i_dcgt    in varchar2,
                                         i_fplx    in varchar2,
                                         i_bxjermb in number,
                                         i_wsrs number)
--i_xm 费用项目类型  i_type 内容 i_ksrq 开始日期 i_jsrq 结束日期 i_ccd 出差地 i_zw 职务 i_dcgt 高铁动车 i_fplx发票类型
  --i_bxjermb 实际报销金额rmb
 return varchar is
  v_count  integer := 0;
  v_days   integer := 0;
  v_bzje   number(15, 2) := 0;
  v_zcbz  varchar(200) :='';
  v_result varchar(200) :='';
  v_ccdms varchar(200) :='';
  v_zwms varchar(200) :='';
begin
  if i_xm = ''  or i_ksrq = '' or i_jsrq = '' then
    return v_result;
  end if;
  if i_xm = '24' then
      if  i_ccd = '' or i_zw = '' then
        return v_result;
      end if;
  select selectname into v_ccdms from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_ccbz' and a.fieldname='ccd' and c.selectvalue=i_ccd;
    select selectname into v_zwms from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_ccbz' and a.fieldname='zw' and c.selectvalue=i_zw;

    select TO_DATE(i_jsrq, 'YYYY-MM-DD') - TO_DATE(i_ksrq, 'YYYY-MM-DD') + 1
      into v_days
      from dual;
    if instr(i_nr,'餐费')>0 then
      --餐费
      select cf * v_days
        into v_bzje
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
         v_result :=v_ccdms||' '||v_zwms||' '||v_bzje;
    elsif instr(i_nr,'住宿费')>0 then
      --住宿费
      select zsf * v_days
        into v_bzje
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
         v_result :=v_ccdms||' '||v_zwms||' '||v_bzje;
    elsif instr(i_nr,'油费')>0 then
      --油费
      select yf * v_days
        into v_bzje
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
         v_result :=v_ccdms||' '||v_zwms||' '||v_bzje;
    end if;
    if i_fplx = '8' then --座次
      select zx
          into v_zcbz
          from uf_ccbz
         where ccd = i_ccd
           and zw = i_zw;
         v_result :=v_ccdms||' '||v_zwms||' '||v_bzje||' '||v_zcbz;
    end if;
  elsif i_xm = '33' then
      select TO_DATE(i_jsrq, 'YYYY-MM-DD') - TO_DATE(i_ksrq, 'YYYY-MM-DD') + 1
          into v_days
          from dual;
      select v_days*150*nvl(i_wsrs,0) into v_bzje from dual;
      v_result :='当前行招待费标准 '||v_bzje||' 我司与客户人数比例不得大于 2:1';
  end if;
 return v_result;
end;