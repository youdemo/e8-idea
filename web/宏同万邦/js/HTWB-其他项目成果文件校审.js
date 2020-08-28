var stdw = "#field9097";//受托单位
var wh = "field9098";//文号
jQuery(document).ready(function () {
    var stdw_val = jQuery(stdw).val();
    if(stdw_val == "1" ||stdw_val == "0"){
        jQuery("#"+wh).attr("readonly", "readonly");
    }else{
        addcheck(wh,"0");
    }
    jQuery(stdw).bind('change',function(){
        var stdw_val = jQuery(stdw).val();
        if(stdw_val == "1" ||stdw_val == "0"){
            jQuery("#"+wh).val("");
            jQuery("#"+wh).attr("readonly", "readonly");
            removecheck(wh,"0");
        }else{
            jQuery("#"+wh).removeAttr("readonly");
            addcheck(wh,"0");
        }
    })
});
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
