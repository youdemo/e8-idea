select * from v_fybx_lcxz where type='$formtable_main_71_dt1_expensess$' and ry='$Name$' and  requestid not in(select b.lcxz from formtable_main_71 a,formtable_main_71_dt1 b where a.id=b.mainid and b.lcxz is not null and a.requestid not in($rqid$))

doFieldSQL(" select f_fna_getdkje('$88602$','$92567$',$88606$,$92066$,$92568$,'$88592$','0') from dual ")

doFieldSQL(" select f_fna_checkcb('$117098$','$117085$','$117083$','$117084$','$117128$','$117129$','$117136$',$117087$) from dual ")