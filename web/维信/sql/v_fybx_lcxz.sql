create or replace view v_fybx_lcxz as
select * from(
select '24' as type,b.requestnamenew,a.	Applicant as ry,b.requestid from formtable_main_63 a,workflow_requestbase b where a.requestid=b.requestid and b.currentnodetype=3
union all
select '22' as type,b.requestnamenew,b.creater as ry,b.requestid from formtable_main_62 a,workflow_requestbase b where a.requestid=b.requestid and b.currentnodetype=3)
order by requestid desc