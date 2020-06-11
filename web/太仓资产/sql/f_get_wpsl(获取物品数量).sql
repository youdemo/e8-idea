alter function f_get_wpsl(@i_wpid varchar(100),@i_rq varchar(100),@i_type varchar(10))
     returns int
	 -- @i_wpid 物品id @i_rq 本月开始日期 @i_type 0上期结余 1本期购入数量 2本期领用
as
begin
     declare
		@v_result int,
		@v_laststartdate varchar(20),
		@v_lastenddate varchar(20),
		@v_nowenddate varchar(20),
		@v_zrksl int,
		@v_zlysl int


		set @v_result=0
		SELECT @v_laststartdate=convert(varchar(100),DATEADD(mm, DATEDIFF(mm,0,@i_rq)-1, 0),23)
		SELECT @v_lastenddate=convert(varchar(100),DATEADD(ms,-3,DATEADD(mm, DATEDIFF(m,0,@i_rq), 0)),23)
		SELECT @v_nowenddate=convert(varchar(100),DATEADD(ms,-3,DATEADD(mm, DATEDIFF(m,0,@i_rq)+1, 0)),23)
		--上期单价
		if @i_type=0
			begin
			   select @v_zrksl=isnull(sum(b.rksl),0) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3  and a.sqrq<=@v_lastenddate and b.wpmc=@i_wpid
			   select @v_zlysl=isnull(sum(b.lysl),0) from uf_lyktzb a,uf_lyktzb_dt1 b where a.id=b.mainid and a.sqrq<=@v_lastenddate and b.wpmc=@i_wpid
			   set @v_result = @v_zrksl-@v_zlysl
			end
		else if  @i_type=1 --本期购入
			begin
			   select @v_result=isnull(sum(b.rksl),0) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3 and a.sqrq>=@i_rq and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			end
		else if  @i_type=2 --本期领用
			begin
			   select @v_result=isnull(sum(b.lysl),0) from uf_lyktzb a,uf_lyktzb_dt1 b where a.id=b.mainid and a.sqrq>=@i_rq and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			end
		else if @i_type=3 --本期结存
			begin
			   select @v_zrksl=isnull(sum(b.rksl),0) from formtable_main_87 a,formtable_main_87_dt1 b,workflow_requestbase c where a.requestId=c.requestid and a.id=b.mainid and c.currentnodetype>=3  and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			   select @v_zlysl=isnull(sum(b.lysl),0) from uf_lyktzb a,uf_lyktzb_dt1 b where a.id=b.mainid and a.sqrq<=@v_nowenddate and b.wpmc=@i_wpid
			   set @v_result = @v_zrksl-@v_zlysl
			end
		return @v_result

end;