create or replace function f_fna_checksfsy(i_requestid      in varchar2,i_rqid in varchar2)

 return varchar is
  v_result varchar(2) := '0';
  v_count integer  :=0;
begin
 select count(1) into v_count from formtable_main_488 a,formtable_main_488_dt1 b where a.id=b.mainid and b.txrccsqdx is not null and a.requestid not in(i_rqid) and (','||b.txrccsqdx||',') like '%,'||i_requestid||',%';
 if v_count >0 then
 v_result := '1';
 end if;
 return v_result;
end;