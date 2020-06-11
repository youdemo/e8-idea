alter function f_get_wpdj(@i_wpid varchar(100),@i_rq varchar(100),@i_type varchar(10))
     returns decimal(10,2)
	 -- @i_wpid 物品id @i_rq 本月开始日期 @i_type 0上期1本期
as
begin
     declare
		@v_result decimal(10,2),
		@v_laststartdate varchar(20),
		@v_lastenddate varchar(20),
		@v_nowenddate varchar(20),
		@v_maxid varchar(20)

		set @v_result=0.00
		SELECT @v_laststartdate=convert(varchar(100),DATEADD(mm, DATEDIFF(mm,0,@i_rq)-1, 0),23)
		SELECT @v_lastenddate=convert(varchar(100),DATEADD(ms,-3,DATEADD(mm, DATEDIFF(m,0,@i_rq), 0)),23)
		SELECT @v_nowenddate=convert(varchar(100),DATEADD(ms,-3,DATEADD(mm, DATEDIFF(m,0,@i_rq)+1, 0)),23)
		--上期单价
		if @i_type=0
			begin
			   select @v_maxid=max(b.id) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3  and a.sqrq<=@v_lastenddate and b.wpmc=@i_wpid
			   if @v_maxid <> ''
			   begin
					select @v_result=isnull(dj,0) from formtable_main_87_dt1 where id=@v_maxid
			   end
			end
		else if  @i_type=1 --本期购入
			begin
			   select @v_maxid=max(b.id) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3 and a.sqrq>=@i_rq and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			   if @v_maxid <> ''
			   begin
					select @v_result=isnull(dj,0) from formtable_main_87_dt1 where id=@v_maxid
			   end
			end
		else if  @i_type=2 --本期领用
			begin
			   select @v_maxid=max(b.id)  from uf_lyktzb a,uf_lyktzb_dt1 b where a.id=b.mainid and a.sqrq>=@i_rq and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			   if @v_maxid <> ''
			   begin
					select @v_result=isnull(dj,0) from uf_lyktzb_dt1 where id=@v_maxid
			   end
			end
		else if @i_type=3 --本期结存
			begin
			   select @v_maxid=max(b.id) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3  and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			   if @v_maxid <> ''
			   begin
					select @v_result=isnull(dj,0) from formtable_main_87_dt1 where id=@v_maxid
			   end
			end
		return @v_result

end;