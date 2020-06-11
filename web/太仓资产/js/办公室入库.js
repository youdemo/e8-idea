<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">
var lm_dt1= "#field7837_";//类名_明细1
var wpmcs_dt1= "#field8442_";//物品名称树_明细1
var wpmc_dt1= "#field7830_";//物品名称_明细1

var ygdj_dt1= "#field8522_";//原有单价_明细1
var xh_dt1= "#field7831_";//型号_明细1
var sl_dt1= "#field8443_";//税率_明细1
var kcsl_dt1= "#field7832_";//库存数量_明细1

jQuery(document).ready(function () {
    jQuery("button[name=addbutton0]").live("click",function(){
        var indexnum0 = jQuery("#indexnum0").val();
        binddt(indexnum0-1);
    });
    setTimeout("binddetail()",500);

});
function binddetail(){
    var indexnum1 = jQuery("#indexnum0").val();
    for(var index = 0;index < indexnum1;index++){
        if(jQuery(wpmcs_dt1+index).length>0){
            binddt(index);

        }
    }
}

function binddt(index){
    jQuery(ygdj_dt1+index).attr("readonly", "readonly");
    jQuery(xh_dt1+index).attr("readonly", "readonly");
    jQuery(sl_dt1+index).attr("readonly", "readonly");
    jQuery(kcsl_dt1+index).attr("readonly", "readonly");
    jQuery(wpmcs_dt1+index).bindPropertyChange(function(){
        var wpmcs_dt1_val = jQuery(wpmcs_dt1+index).val();
        if(wpmcs_dt1_val == ""){
            jQuery(lm_dt1+index).val("");
            jQuery(lm_dt1+index+"span").html("");
            jQuery(wpmc_dt1+index).val("");
            jQuery(wpmc_dt1+index+"span").html("");
        }else{
            jQuery.ajax({
                type: "POST",
                url: "/tczc/wp/getwpxx.jsp",
                data: {"xzid":wpmcs_dt1_val},
                dataType: "text",
                async:false,//同步 true异步
                success: function(data){
                    data=data.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
                    var json = JSON.parse(data);
                    jQuery(lm_dt1+index).val(json.lm);
                    jQuery(lm_dt1+index+"span").html(json.flmc);
                    jQuery(wpmc_dt1+index).val(json.wpid);
                    jQuery(wpmc_dt1+index+"span").html(json.wpmc);
                    jQuery(ygdj_dt1+index).val(json.dj);
                    jQuery(xh_dt1+index).val(json.xh);
                    jQuery(sl_dt1+index).val(json.sl);
                    jQuery(kcsl_dt1+index).val(json.tst);

                }

            });
        }
    });
}
</script>


