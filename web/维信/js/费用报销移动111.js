
var sjbxje_dt1 = "#field117088_";//明细1实际报销金额
var bxje_dt1 = "#field117112_";//明细1票面金额
var sfcb_dt1 = "#field117138_";//明细1是否超标
var sfcbys_dt1= "117138";//明细1是否超标颜色
var bxxm_dt1 = "#field150064_";//明细1报销项目
var lcxz_dt1 = "field117140_";//明细1流程选择
var sfwzdf_dt1 = "#field117139_";//明细1是否为招待费
var lcxzbt_dt1 = "#field121065_";//明细1是流程选择必填

jQuery(document).ready(function(){
    jQuery("button[name='addbutton0']").bind("click",function(){
        var indexnum0 = jQuery("#nodenum0").val();
        binddt(indexnum0-1);

    });
    setTimeout("binddetail()",1000);
    //setTimeout("binddetail()",1000);
    checkCustomize = function () {
        var indexnum0=jQuery("#nodenum0").val();
        for( var index=0;index<indexnum0;index++){
            if( jQuery(sjbxje_dt1+index).length>0){
                var sjbxje_dt1_val = jQuery(sjbxje_dt1+index).val();
                var bxje_dt1_val = jQuery(bxje_dt1+index).val();
                var sfcb_dt1_val = jQuery(sfcb_dt1+index).val();
                if(sjbxje_dt1_val==""){
                    sjbxje_dt1_val = "0";
                }
                if(bxje_dt1_val==""){
                    bxje_dt1_val = "0";
                }
                if(Number(sjbxje_dt1_val)>Number(bxje_dt1_val)){
                    alert("明细1中存在实际报销金额大于票面金额的数据，请检查");
                    return false;
                }

            }
        }
        return true;
    }
});
function binddetail(){
    var indexnum1 = jQuery("#nodenum0").val();
    for(var index = 0;index < indexnum1;index++){
        if(jQuery(sfcb_dt1+index).length>0){
            binddt(index);
            changecolor(index);
            changebt(index);
        }
    }

}
function changecolor(index){
    var sfcb_dt1_val = jQuery(sfcb_dt1+index).val();

    if(sfcb_dt1_val == "1"){
        jQuery("#isshow0_"+index+"_"+sfcbys_dt1).parent().parent().css({"background-color":"yellow"});
    }else{
        jQuery("#isshow0_"+index+"_"+sfcbys_dt1).parent().parent().removeAttr("style","");
    }
}
//增加必填
function changebt(index){
    var bxxm_dt1_val = jQuery(bxxm_dt1+index).val();
    var bxxm_dt1_val = jQuery(bxxm_dt1+index).val();
    //changebt(index);
    if(bxxm_dt1_val=='33'){
        jQuery(sfwzdf_dt1+index).val("0");
        jQuery(sfwzdf_dt1+index+"_d").val("0");
        jQuery(sfwzdf_dt1+index).trigger("change")
    }else{
        jQuery(sfwzdf_dt1+index).val("1");
        jQuery(sfwzdf_dt1+index+"_d").val("1");
        jQuery(sfwzdf_dt1+index).trigger("change")
    }
    if(bxxm_dt1_val == "24" || bxxm_dt1_val == "22"){//差旅   培训

        jQuery(lcxzbt_dt1+index).val("0");
        jQuery(lcxzbt_dt1+index+"_d").val("0");
        jQuery(lcxzbt_dt1+index).trigger("change")
    }else{
        jQuery(lcxzbt_dt1+index).val("1");
        jQuery(lcxzbt_dt1+index+"_d").val("1");
        jQuery(lcxzbt_dt1+index).trigger("change")
     }
}
function binddt(index){
    jQuery(sfcb_dt1+index).bindPropertyChange(function(){
        changecolor(index);

    });

    jQuery(bxxm_dt1+index).bindPropertyChange(function(){
        jQuery("#"+lcxz_dt1+index).val("");
        jQuery("#"+lcxz_dt1+index+"span").html("");
        setTimeout("changebt("+index+")",500);
    });


}









