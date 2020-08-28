var kmzt_dt1 = "#field20380_";//科目状态
var zcyssx_dt1 = "field20143_";//转出预算事项
var zrsx_dt1 = "field20276_";//转入事项
jQuery(document).ready(function(){
    jQuery("#indexnum0").bindPropertyChange(function(){
        dodetail();
    })
    setTimeout("dodetail()",500);
})
function dodetail(){
    var indexnum0 = jQuery("#indexnum0").val();
    for(var index = 0;index<indexnum0;index++){
        if(jQuery(kmzt_dt1+index).length>0){
            changesx(index);
            bindchange(index);
        }
    }
}
function bindchange(index){
    jQuery(kmzt_dt1+ index).bindPropertyChange(function(){
        changesx(index)
    })
}
function changesx(index){
    var kmzt_dt1_val = jQuery(kmzt_dt1+index).val();
    if(kmzt_dt1_val == "1"){
        addcheck(zcyssx_dt1+index,'1');
        addcheck(zrsx_dt1+index,'0');
        jQuery("#"+zrsx_dt1+index).removeAttr("readonly");
        jQuery("#"+zcyssx_dt1+index+"_browserbtn").attr('disabled',false);
    }else{
        jQuery("#"+zcyssx_dt1+index).val("");
        jQuery("#"+zcyssx_dt1+index+"span").text("");
        jQuery("#"+zrsx_dt1+index).val("");
        jQuery("#"+zrsx_dt1+index+"span").text("");
        removecheck(zcyssx_dt1+index,'1');
        removecheck(zrsx_dt1+index,'0');
        jQuery("#"+zrsx_dt1+index).attr("readonly", "readonly");
        jQuery("#"+zcyssx_dt1+index+"_browserbtn").attr('disabled',true);
    }
}
function addcheck(btid,flag){
    var btid_val = jQuery("#"+btid).val();
    var btid_check=btid;
    if(btid_val==''){
        if(flag=='0'){
            jQuery("#"+btid+"span").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");

        }else{
            jQuery("#"+btid+"spanimg").html("<img align='absmiddle' src='/images/BacoError_wev8.gif'>");
        }

    }
    if(flag=='0'){
        jQuery("#"+btid).attr("viewtype","1");
    }
    var needcheck = document.all("needcheck");
    if(needcheck.value.indexOf(","+btid_check)<0){
        if(needcheck.value!='') needcheck.value+=",";
        needcheck.value+=btid_check;
    }
}

function removecheck(btid,flag){
    if(flag=='0'){
        jQuery("#"+btid+"span").html("");
    }else{
        jQuery("#"+btid+"spanimg").html("");
    }
    var parastr = document.all('needcheck').value;
    if(parastr == btid){
        parastr = "";
    }else{
        parastr = parastr.replace(","+btid,"");
    }

    document.all('needcheck').value=parastr;
}
