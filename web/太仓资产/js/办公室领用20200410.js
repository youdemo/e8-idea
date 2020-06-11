var lysl = '#field7749_';//领用数量
var wpmc = '#field7877_';//物品名称
var sysl = '#field7777_';//剩余数量
var bmsyje = '#field7966';//部门剩余金额
var lyjehj = '#field7967';//领用金额合计
var djbmje = '#field7968';//部门冻结金额
var djsl = '#field7969_';//剩余冻结数量

jQuery(document).ready(function(){

    //提交
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
        if((parseFloat(jQuery(lyjehj).val())+parseFloat(jQuery(djbmje).val())) > jQuery(bmsyje).val()){
            Dialog.alert("温馨提示:本次申请的金额大于部门剩余额度，请核对后重新提交。");
            return false;
        }
        return returnV;
    }
})




