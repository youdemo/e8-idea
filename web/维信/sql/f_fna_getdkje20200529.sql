create or replace function f_fna_getdkje(i_fpid in varchar2,
                                         i_fplx in varchar2,
                                         i_je   in number,
                                         i_bxje in number,
                                         i_sl   in number,
                                         i_type in varchar2)
--i_fplx 发票类型  i_je 金额 i_bxje 报销金额 i_sl 税率 i_type 类型 0可抵扣 1不可抵扣

 return number is
  v_result number(10, 2) := 0.00;
  v_count  integer := 0;
begin
  if i_type = '0' then
    if i_fplx = '10' or i_fplx = '8' then
      select round(nvl(i_bxje, 0) / (1 + sl) * sl, 2)
        into v_result
        from uf_slgx
       where fplx = i_fplx;
    elsif i_fplx = '2' then
      select round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
        into v_result
        from dual;
    elsif i_fplx = '15' then
      select count(1)
        into v_count
        from FnaInvoiceLedger
       where seller like '%滴滴出行%'
         and id = i_fpid;
      if v_count > 0 then
        select round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
          into v_result
          from dual;
      else
        v_result := 0;
      end if;

    end if;

  else
    if i_fplx = '10' or i_fplx = '8' then
      v_result := 0;
    elsif i_fplx = '2' then
      select round(nvl(i_je, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2) -
             round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
        into v_result
        from dual;
    elsif i_fplx = '15' then
      select count(1)
        into v_count
        from FnaInvoiceLedger
       where seller like '%滴滴出行%'
         and id = i_fpid;
      if v_count > 0 then
         select round(nvl(i_je, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2) -
             round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
        into v_result
        from dual;
      else
       select round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
        into v_result
        from dual;
      end if;
    else
      select round(nvl(i_bxje, 0) / (1 + (i_sl / 100)) * (i_sl / 100), 2)
        into v_result
        from dual;
    end if;
  end if;
  return v_result;
end;
