<script type="text/javascript">
var lm_dt1= "#field7876_";//类名_明细1
var wpmcs_dt1= "#field7809_";//物品名称树_明细1
var wpmc_dt1= "#field7877_";//物品名称_明细1

var ygdj_dt1= "#field7750_";//原有单价_明细1
var xh_dt1= "#field7747_";//型号_明细1
var sl_dt1= "#field8445_";//税率_明细1
var kcsl_dt1= "#field7748_";//库存数量_明细1

var lysl = '#field7749_';//领用数量
var wpmc = '#field7877_';//物品名称
var sysl = '#field7777_';//剩余数量
var bmsyje = '#field7966';//部门剩余金额
var lyjehj = '#field7967';//领用金额合计
var djbmje = '#field7968';//部门冻结金额
var djsl = '#field7969_';//剩余冻结数量
jQuery(document).ready(function () {
    jQuery("button[name=addbutton0]").live("click",function(){
        var indexnum0 = jQuery("#indexnum0").val();
        binddt(indexnum0-1);
    });
    setTimeout("binddetail()",500);
    checkCustomize = function(){
            var returnV = true;
            for(var i = 0;i<jQuery("#indexnum0").val();i++){

                //重复物品
                for(var j = i+1;j<jQuery("#indexnum0").val();j++){
                    if(jQuery(wpmc+i).val() === jQuery(wpmc+j).val() &&jQuery(wpmc+i).val() != ''  ){
                        Dialog.alert("温馨提示：同一个物品名称只能领用一次，请核对重新提交。");
                        return false;
                    }
                }

                //领用库存数量
                if((jQuery(sysl+i).val()-jQuery(djsl+i).val()) < 0){
                    Dialog.alert("温馨提示：申请的领用单第"+((i++)+1)+"行物品库存数量不足，请核对后重新提交。");
                    return false;
                }
            }

            //部门剩余金额
			//if(jQuery(wpmc+i).val() != '3' || jQuery(wpmc+i).val() != '10' ){
				//if((parseFloat(jQuery(lyjehj).val())+parseFloat(jQuery(djbmje).val())) > jQuery(bmsyje).val()){
					//Dialog.alert("温馨提示:本次申请的金额大于部门剩余额度，请核对后重新提交。");
					//return false;
				//}
			//}
            return returnV;
        }
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
                    jQuery(kcsl_dt1+index).trigger("change");

                }

            });
        }
    });
}
</script>








