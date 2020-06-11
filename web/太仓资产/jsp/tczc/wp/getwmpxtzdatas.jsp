<%@ page import="weaver.general.Util" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="weaver.general.BaseBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page" />
<%
    String pagenum = Util.null2String(request.getParameter("pagenum"));
    String limit = Util.null2String(request.getParameter("limit"));
    String offset = Util.null2String(request.getParameter("offset"));
    String sort = Util.null2String(request.getParameter("sort"));
    String sortOrder = Util.null2String(request.getParameter("sortOrder"));
    String nf = Util.null2String(request.getParameter("nf"));
    String yf = Util.null2String(request.getParameter("yf"));
    String nowstartdate = "";
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
    if("".equals(nf)||"".equals(yf)){
        nowstartdate = sf.format(new Date())+"-01";
    }else{
        nowstartdate = nf+"-"+yf+"-01";
    }

   int rowcount = 0;
   String sql = "select count(1) as count from uf_bgypk";
   rs.execute(sql);
   if(rs.next()){
       rowcount = rs.getInt("count");
   }
   sql = "select * from (" +
           "select row_number() over(order by id asc) as num,id, wpmc,xh,(select unitname from LgcAssetUnit where id=a.dw) as dw,dbo.f_get_wpdj(a.id,'"+nowstartdate+"','0') as sqjcdj,dbo.f_get_wpsl(a.id,'"+nowstartdate+"','0') as sqjcsl,cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','0')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','0') as numeric(10,2)) as sqjcxj ,cast(cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','0')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','0') as numeric(10,2))/(1+dbo.f_get_wprate(a.id,'"+nowstartdate+"','0')) as numeric(10,2)) as sqjcbhs" +
           ",dbo.f_get_wpdj(a.id,'"+nowstartdate+"','1') as bqgrdj,dbo.f_get_wpsl(a.id,'"+nowstartdate+"','1') as bqgrsl,cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','1')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','1') as numeric(10,2)) as bqgrxj,cast(cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','1')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','1') as numeric(10,2))/(1+dbo.f_get_wprate(a.id,'"+nowstartdate+"','1')) as numeric(10,2)) as bqgrbhs" +
           ",dbo.f_get_wpdj(a.id,'"+nowstartdate+"','2') as bqlydj,dbo.f_get_wpsl(a.id,'"+nowstartdate+"','2') as bqlysl,cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','2')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','2') as numeric(10,2)) as bqlyxj,cast(cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','2')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','2') as numeric(10,2))/(1+dbo.f_get_wprate(a.id,'"+nowstartdate+"','2')) as numeric(10,2)) as bqlybhs" +
           ",dbo.f_get_wpdj(a.id,'"+nowstartdate+"','3') as bqjcdj,dbo.f_get_wpsl(a.id,'"+nowstartdate+"','3') as bqjcsl,cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','3')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','3') as numeric(10,2)) as bqjcxj,cast(cast(dbo.f_get_wpdj(a.id,'"+nowstartdate+"','3')*dbo.f_get_wpsl(a.id,'"+nowstartdate+"','3') as numeric(10,2))/(1+dbo.f_get_wprate(a.id,'"+nowstartdate+"','3')) as numeric(10,2)) as bqjcbhs,dbo.f_get_wprate(a.id,'2020-04-01','3') as sl" +
           " from uf_bgypk a  ) a ";
   if(!"".equals(offset)&&!"".equals(limit)){
       sql = sql + "where a.num>"+offset+" and a.num<=("+offset+"+"+limit+")";
   }
   new BaseBean().writeLog("testaaaa","sql:"+sql);
   rs.execute(sql);
    JSONArray ja = new JSONArray();
    while(rs.next()){
        JSONObject jo = new JSONObject();
        jo.put("id",Util.null2String(rs.getString("id")));
        jo.put("wpmc",Util.null2String(rs.getString("wpmc")));
        jo.put("xh",Util.null2String(rs.getString("xh")));
        jo.put("dw",Util.null2String(rs.getString("dw")));
        jo.put("sqjcdj",Util.null2String(rs.getString("sqjcdj")));
        jo.put("sqjcsl",Util.null2String(rs.getString("sqjcsl")));
        jo.put("sqjcxj",Util.null2String(rs.getString("sqjcxj")));
        jo.put("sqjcbhs",Util.null2String(rs.getString("sqjcbhs")));
        jo.put("bqgrdj",Util.null2String(rs.getString("bqgrdj")));
        jo.put("bqgrsl",Util.null2String(rs.getString("bqgrsl")));
        jo.put("bqgrxj",Util.null2String(rs.getString("bqgrxj")));
        jo.put("bqgrbhs",Util.null2String(rs.getString("bqgrbhs")));
        jo.put("bqlydj",Util.null2String(rs.getString("bqlydj")));
        jo.put("bqlysl",Util.null2String(rs.getString("bqlysl")));
        jo.put("bqlyxj",Util.null2String(rs.getString("bqlyxj")));
        jo.put("bqlybhs",Util.null2String(rs.getString("bqlybhs")));
        jo.put("bqjcdj",Util.null2String(rs.getString("bqjcdj")));
        jo.put("bqjcsl",Util.null2String(rs.getString("bqjcsl")));
        jo.put("bqjcxj",Util.null2String(rs.getString("bqjcxj")));
        jo.put("bqjcbhs",Util.null2String(rs.getString("bqjcbhs")));
        jo.put("sl",Util.null2String(rs.getString("sl")));
        ja.put(jo);

    }
    JSONObject json = new JSONObject();
    json.put("total", rowcount);
    json.put("rows", ja);
    out.print(json);


%>
