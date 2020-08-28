
--  获取事项金额信息   关联id    类型  0 审批中(冻结)  1 已审批（扣减）
ALTER function [dbo].[get_sxje](@uid varchar(20),@type varchar(20))
returns decimal(13,2)
as
begin
declare @zfje1 decimal(13,2) --总金额
declare @zfje2 decimal(13,2)-- 非差旅金额未引用借款 未归档
declare @zfje3 decimal(13,2)-- 预借款金额未归档
declare @zfje4 decimal(13,2)-- 预借款 未被 非差 引用 金额

declare @zfje5 decimal(13,2)-- 预借款 被 非差 引用 总金额
declare @zfje6 decimal(13,2)-- 预借款流程  被 非差 引用  中所有金额
declare @zfje7 decimal(13,2)-- 预借款流程  被 非差 引用 已扣减金额
declare @maxje decimal(13,2)-- 最大冻结额度
declare @v_jtsm varchar(300)

select @v_jtsm=jtsm from uf_yssjdr where id=@uid

if @type = '0'
	begin
		select  @zfje2= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_268  m join formtable_main_268_dt1 dt on  m.id = dt.mainid join workflow_requestbase w
		 on m.requestId=w.requestid where w.currentnodetype>0 and w.currentnodetype<3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm and ( CONVERT(varchar, m.yfjkd) = '' or m.yfjkd is null );

		select  @zfje3= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_267   m  join workflow_requestbase w  on m.requestId=w.requestid join formtable_main_267_dt1 dt
		on dt.mainid=m.id   where  w.currentnodetype>0 and w.currentnodetype<3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm;

		select  @zfje4= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_267 m  join workflow_requestbase w  on m.requestId=w.requestid join formtable_main_267_dt1 dt
		on dt.mainid=m.id   where  w.currentnodetype>=3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm and m.requestid
		 not in ( SELECT distinct B.yfjkd FROM(  SELECT id, yfjkd = CONVERT(xml,'<root><v>' + REPLACE(CONVERT(varchar,[yfjkd]), ',', '</v><v>') + '</v></root>') FROM formtable_main_268 where yfjkd is
  not null and CONVERT(varchar,yfjkd) <> '' )A OUTER APPLY( SELECT yfjkd = N.v.value('.', 'varchar(100)') FROM A.[yfjkd].nodes('/root/v') N(v))B );


		select  @zfje5= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_267 m  join workflow_requestbase w  on m.requestId=w.requestid join formtable_main_267_dt1 dt
		on dt.mainid=m.id   where  w.currentnodetype>=3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm and m.requestid
		in ( SELECT distinct B.yfjkd FROM(  SELECT id, yfjkd = CONVERT(xml,'<root><v>' + REPLACE(CONVERT(varchar,[yfjkd]), ',', '</v><v>') + '</v></root>') FROM formtable_main_268 where yfjkd is
  not null and CONVERT(varchar,yfjkd) <> '' )A OUTER APPLY( SELECT yfjkd = N.v.value('.', 'varchar(100)') FROM A.[yfjkd].nodes('/root/v') N(v))B );

		select  @zfje6= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_268  m join formtable_main_268_dt1 dt on  m.id = dt.mainid join workflow_requestbase w
		 on m.requestId=w.requestid where w.currentnodetype>0  and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm and  CONVERT(varchar, m.yfjkd) <> '' and m.yfjkd is not null ;

		 if @zfje5>=@zfje6
			begin
				set @maxje = @zfje5;
			end
		else
			begin
				set @maxje = @zfje6;
			end
		 select  @zfje7= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_268  m join formtable_main_268_dt1 dt on  m.id = dt.mainid join workflow_requestbase w
		 on m.requestId=w.requestid where w.currentnodetype>=3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm and  CONVERT(varchar, m.yfjkd) <> '' and m.yfjkd is not null ;





		 select @zfje1=@zfje2+@zfje3+@zfje4+@maxje-@zfje7;
	end
else if @type = '1'
	begin

		select  @zfje2= isnull(sum(isnull(dt.jysje,0.00)),0.00) from formtable_main_268  m join formtable_main_268_dt1 dt on  m.id = dt.mainid join workflow_requestbase w
		 on m.requestId=w.requestid where w.currentnodetype>=3 and (select jtsm from uf_yssjdr where id=dt.sxkz) = @v_jtsm ;

		 select @zfje1=@zfje2;
	end

  return @zfje1;

end
GO


