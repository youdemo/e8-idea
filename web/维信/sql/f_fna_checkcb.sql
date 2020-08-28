create or replace function f_fna_checkcb(i_xm      in varchar2,
                                         i_nr    in varchar2,
                                         i_ksrq    in varchar2,
                                         i_jsrq    in varchar2,
                                         i_ccd     in varchar2,
                                         i_zw      in varchar2,
                                         i_dcgt    in varchar2,
                                         i_fplx    in varchar2,
                                         i_bxjermb in number,
                                         i_wsrs number,
                                         i_khrs number,
                                         i_txrids in varchar2)
--i_xm 费用项目类型  i_type 内容 i_ksrq 开始日期 i_jsrq 结束日期 i_ccd 出差地 i_zw 职务 i_dcgt 高铁动车 i_fplx发票类型
  --i_bxjermb 实际报销金额rmb
 return number is
  v_result varchar(2) := '0';
  v_count  integer := 0;
  v_days   integer := 0;
  v_bzje   number(15, 2) := 0;
  v_bzje_all  number(15, 2) := 0;
  v_zcbz  varchar(200) :='';
  v_maxzw varchar(200) :='';
  v_rs integer := 0;
begin
  if i_xm = ''  or i_ksrq = '' or i_jsrq = ''  then
    return v_result;
  end if;
  if i_xm = '24' then
    if i_ccd = '' or i_zw = '' then
        return v_result;
    end if;
    select TO_DATE(i_jsrq, 'YYYY-MM-DD') - TO_DATE(i_ksrq, 'YYYY-MM-DD') + 1
      into v_days
      from dual;
    if instr(i_nr,'餐费')>0 then
      --餐费then
      select cf * v_days
        into v_bzje
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
       v_bzje_all := v_bzje;
      if i_txrids is null  or i_txrids ='' or i_txrids ='''' then
       v_bzje_all := v_bzje;
      else
      select nvl(sum(b.cf*v_days),0) into v_bzje from hrmresource a,uf_ccbz b where ','||i_txrids||',' like '%,'||a.id||',%' and b.ccd=i_ccd
and b.zw=case when nvl(a.seclevel,0)<=8 then 0 when nvl(a.seclevel,0)<=11 then 2 else 3 end;
v_bzje_all:=v_bzje_all+v_bzje;
      end if;
      if i_bxjermb > v_bzje_all then
        v_result := '1';
      end if;
    elsif instr(i_nr,'住宿费')>0 then
      --住宿费
     if i_txrids is null or i_txrids ='' or i_txrids ='''' then
      select zsf * v_days
        into v_bzje_all
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
      else
        select max(case when nvl(seclevel,0)<=8 then 0 when nvl(seclevel,0)<=11 then 2 else 3 end) into v_maxzw from hrmresource where ','||i_txrids||',' like '%,'||id||',%';
        if v_maxzw < i_zw then
         v_maxzw := i_zw;
        end if;
        select zsf * v_days
        into v_bzje_all
        from uf_ccbz
       where ccd = i_ccd
         and zw = v_maxzw;
      end if;

      if i_bxjermb > v_bzje_all then
        v_result := '1';
      end if;
    elsif instr(i_nr,'油费')>0 then
      --油费
      select yf * v_days
        into v_bzje
        from uf_ccbz
       where ccd = i_ccd
         and zw = i_zw;
      if i_bxjermb > v_bzje then
        v_result := '1';
      end if;
    end if;
    if i_fplx = '8' then --座次
      select zx
          into v_zcbz
          from uf_ccbz
         where ccd = i_ccd
           and zw = i_zw;

       select count(1) into v_count from uf_zcbz where bzzc=v_zcbz and cbzc=i_dcgt;
       if v_count >0 then
        v_result := '1';
       end if;

    end if;
 elsif i_xm = '33' then
      if nvl(i_wsrs,0) >0 then
          select TO_DATE(i_jsrq, 'YYYY-MM-DD') - TO_DATE(i_ksrq, 'YYYY-MM-DD') + 1
              into v_days
              from dual;
          select v_days*150*(nvl(i_wsrs,0)+nvl(i_khrs,0)) into v_bzje from dual;
          if i_bxjermb > v_bzje then
            v_result := '1';
          end if;
          if nvl(i_khrs,0)>0 and nvl(i_wsrs,0)/nvl(i_khrs,0)>2  then
            v_result := '1';
          end if;
      end if;
  end if;
 return v_result;
end;
