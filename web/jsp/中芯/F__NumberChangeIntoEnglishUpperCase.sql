alter FUNCTION [dbo].[F__NumberChangeIntoEnglishUpperCase]

(@num numeric(18,4),@bz varchar(20))

returns varchar(400) --with encryption

as

begin
  --declare @num numeric(17, 4) --支持到千亿计数,保留四位小数
  declare @i int,@hundreds int,@tenth int,@one int
  declare @thousand int,@million int,@billion int
  declare @numbers varchar(400),@s varchar(18),@result varchar(400)
  if @num is null
	begin
	 return ''
	end
  --set @num = '1231231231.00121'
  set @numbers='one       two       three     four      five      '
              +'six       seven     eight     nine      ten       '
              +'eleven    twelve    thirteen  fourteen  fifteen   '
              +'sixteen   seventeen eighteen  nineteen  '
              +'twenty    thirty    forty     fifty     '
              +'sixty     seventy   eighty    ninety    '
  set @s=right('000000000000000000'+cast(@num as varchar(17)),17)

  set @billion=cast(substring(@s,1,3) as int)--将12位整数分成4段：十亿、百万、千、百十个
  set @million=cast(substring(@s,4,3) as int)
  set @thousand=cast(substring(@s,7,3) as int)
 
--   Print '@thousand:'+convert( nvarchar(10),@thousand )
--   Print '@million:'+convert( nvarchar(10),@million )
--   Print '@billion:'+convert( nvarchar(10), @billion )

  set @result=''
  set @i=0
  while @i<=3
  begin
    set @hundreds=cast(substring(@s,@i*3+1,1) as int)--百位0-9
    set @tenth=cast(substring(@s,@i*3+2,1) as int)
    set @one=(case @tenth when 1 then 10 else 0 end)+cast(substring(@s,@i*3+3,1) as int)--个位0-19
    set @tenth=(case when @tenth<=1 then 0 else @tenth end)--十位0、2-9
    if (@i=1 and @billion>0 and (@million>0 or @thousand>0 or @hundreds>0)) or
       (@i=2 and (@billion>0 or @million>0) and (@thousand>0 or @hundreds>0)) or
       (@i=3 and (@billion>0 or @million>0 or @thousand>0) and (@hundreds>0))
      set @result=@result+' '--百位不是0则每段之间加连接符,
   
    if (@i=3 and (@billion>0 or @million>0 or @thousand>0) and (@hundreds=0 and (@tenth>0 or @one>0)))
      set @result=@result+' and '--百位是0则加连接符AND
    if @hundreds>0
      set @result=@result+rtrim(substring(@numbers,@hundreds*10-9,10))+' hundred'
    if @tenth>=2 and @tenth<=9
    begin
      if @hundreds>0
        set @result=@result+' and '
      set @result=@result+rtrim(substring(@numbers,@tenth*10+171,10))
    end
    if @one>=1 and @one<=19
    begin
      if @tenth>0
        set @result=@result+'-'
      else
        if @hundreds>0
          set @result=@result+' and '
      set @result=@result+rtrim(substring(@numbers,@one*10-9,10))
    end
    if @i=0 and @billion>0
      set @result=@result+' billion'
    if @i=1 and @million>0
      set @result=@result+' million'
    if @i=2 and @thousand>0
      set @result=@result+' thousand'
    set @i=@i+1
  end 

  /*判断是否有小数位*/
  if substring(@s,14,4)<>'0000'
  begin
    set @result=@result+' point '
    /*判断小数点第一位数字是否不为零*/
    if substring(@s,14,1)='0'
      set @result=@result+'zero'
    else
      set @result = @result+rtrim(substring(@numbers,cast(substring(@s,14,1) as int)*10-9,10))
    /*判断小数点后第二位数字是否为零*/
    if substring(@s,15,1)='0'
      set @result=@result
    else
      set @result = @result+' '+rtrim(substring(@numbers,cast(substring(@s,15,1) as int)*10-9, 10))
    /*判断小数点后第三位数字是否为零
    if substring(@s,16,1)='0'
      set @result = @result+' zero '
    else
      set @result = @result+rtrim(substring(@numbers,cast(substring(@s,16,1) as int)*10-9, 10))*/
    /*判断小数点后第四位数字是否为零
    if substring(@s,17,1)<>'0'
      set @result = @result+' '+rtrim(substring(@numbers,cast(substring(@s,17,1) as int)*10-9,10))
    else
      set @result = @result+' zero'*/
  end

--   Print '总字符:'+@s
--   Print '大写:'+@result
  set @result='SAY '+ @bz+' '+Upper(@result)+' ONLY'
  return(@result)
end