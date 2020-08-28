<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">

var sjbxje_dt1 = "#field128582_";//明细1实际报销金额
var bxje_dt1 = "#field128606_";//明细1票面金额
var sfcb_dt1 = "#field128632_";//明细1是否超标
var sfcbys_dt1= "128632";//明细1是否超标颜色
var bxxm_dt1 = "#field150064_";//明细1报销项目
var lcxz_dt1 = "field128634_";//明细1流程选择
var sfwzdf_dt1 = "#field128633_";//明细1是否为招待费
var lcxzbt_dt1 = "#field128635_";//明细1是流程选择必填
var fpId = "128602";//发票
var fpZsId = "128603";//发票张数
var jeId = "128606";//金额
var sjbxje = "128582";//实际报销金额
var zkanId = "128604";//展开按钮
var sxfpId = "128605";//所选发票id
var yrfwlxId = "128579";//货物或应税服务类型
var zwlxId = "128630";//座位类型
var indexNo = 0;

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
function bindFunc(){
    var _xm_array = get_wfDetail_xm_array();
    var _xm_array_length = _xm_array.length;
    for(var i0=0;i0<_xm_array_length;i0++){
        var _idx = parseInt(jQuery(_xm_array[i0]).attr("id").replace("field"+zkanId+"_",""));
        $("#field"+zkanId+"_"+_idx+"_span").html("<button type='button' style='width:80px;height:35px;background-color:#55B1F9;border-color:#55B1F9;border-width:0px;border-radius:5px;' onclick='zkAction("+_idx+");'><font color='white'>展开</font></button>");
    }
}
function zkAction(index){
    var xsfpVal = $("#field"+sxfpId+"_"+index).val();
    if(xsfpVal==""){
        xsfpVal = jQuery("#field"+fpId+"_"+index).val();
    }
    invoiceHasSelect(xsfpVal);
}
function setFormData(__index,fpzsVal,xsfpVal,jeVal){
    setDtlFieldSpanNew(fpZsId,__index,fpzsVal,fpzsVal,indexNo);
    setDtlFieldSpanNew(sxfpId,__index,xsfpVal,xsfpVal,indexNo);
    setDtlFieldSpanNew(jeId,__index,jeVal,jeVal,indexNo);
    setDtlFieldSpanNew(sjbxje,__index,jeVal,jeVal,indexNo);
}
jQuery(document).ready(function(){
    jQuery("button[name='addbutton"+indexNo+"']").bind("click",function(){
        var _xm_array = get_wfDetail_xm_array();
        if(_xm_array.length > 0){
            var _idx = parseInt(jQuery(_xm_array[_xm_array.length-1]).attr("id").replace("field"+zkanId+"_",""));
            $("#field"+zkanId+"_"+_idx+"_span").html("<button type='button' style='width:80px;height:35px;background-color:#55B1F9;border-color:#55B1F9;border-width:0px;border-radius:5px;' onclick='zkAction("+_idx+");'><font color='white'>展开</font></button>");
        }
    });
    setTimeout("bindFunc()",1000);
});

function get_wfDetail_xm_array(){
    var _dt1_objTagName = "span";
    var _xm_array = jQuery(_dt1_objTagName+"[id^='field"+zkanId+"_']");
    return _xm_array;
}

function setOtherFormData(__index,invoiceServiceYype,seat){
    setDtlFieldSpanNew(yrfwlxId,__index,invoiceServiceYype,invoiceServiceYype,indexNo);
    setDtlFieldSpan(zwlxId,__index,seat,seat,indexNo);
}

function setDtlFieldSpanNew(fieldId,indexno,idValue,nameValue,_dindex_dtlIdx){
    jQuery("#field"+fieldId+"_"+indexno).attr("value",idValue);
    jQuery("#field"+fieldId+"_"+indexno).val(idValue);
    if(jQuery("#field"+fieldId+"_"+indexno+"_d").length>0){
        jQuery("#field"+fieldId+"_"+indexno+"_d").val(idValue);
    }
    jQuery("#field"+fieldId+"_"+indexno+"_span").html(nameValue);
    jQuery("#field"+fieldId+"_"+indexno+"_span_d").html(nameValue);
    jQuery("#isshow"+_dindex_dtlIdx+"_"+indexno+"_"+fieldId).html(nameValue);
    setTimeout(function () {
        jQuery("#field"+fieldId+"_"+indexno).trigger("change");
        jQuery("#field"+fieldId+"_"+indexno).trigger("blur");
    },1000);
}

//移动建模发起报销
$(document).ready(function(){
    var workflowid = GetQueryString("workflowid");
    var selectIds = GetQueryString("selectIds");
    if(selectIds!=null&&selectIds!=undefined&&selectIds!=""){
        try{
            $.ajax({
                url: '/mobile/plugin/szwxdz/szwxdzAjax2.jsp',
                type: 'post',
                data: {selectIds:selectIds,workflowid:workflowid},
                dataType: 'json',
                timeout: 60000,
                success: function (_json) {
                    if (_json.flag) {
                        var data = _json.data;
                        var dataLen = data.length;
                        for(var i = 0;i<dataLen;i++){
                            var dataDetail = data[i];
                            var id = dataDetail.id;
                            var invoicenumber = dataDetail.invoicenumber;
                            var taxIncludedPrice = dataDetail.taxIncludedPrice;
                            var invoiceServiceYype = dataDetail.invoiceServiceYype;
                            var seat = dataDetail.seat;
                            var all = dataDetail.all;
                            if(indexNo==0){
                                addRow0(0,"trigroupinfo");
                            }else if(indexNo==1){
                                addRow1(1,"trigroupinfo");
                            }else if(indexNo==2){
                                addRow2(2,"trigroupinfo");
                            }
                            var nodenum0= document.getElementById("nodenum"+indexNo).value * 1.0 - 1;
                            setDtlFieldSpan(fpId,nodenum0,id,invoicenumber,indexNo);
                            var fpzs = all.split(",").length;
                            console.log(taxIncludedPrice);
                            console.log(invoiceServiceYype);
                            console.log(seat);
                            console.log(all);
                            console.log(fpzs);
                            setFormData(nodenum0,fpzs,all,taxIncludedPrice);
                            setOtherFormData(nodenum0,invoiceServiceYype,seat)
                            if(indexNo==0){
                                $("#save0_"+nodenum0).click();
                            }else if(indexNo==1){
                                $("#save1_"+nodenum0).click();
                            }else if(indexNo==2){
                                $("#save2_"+nodenum0).click();
                            }
                        }
                    }else{
                        alert("生成发票明细失败");
                    }
                },
                error: function (XHR, textStatus, errorThrown) {
                    alert("生成发票明细失败");
                },
                complete:function(){
                }
            });
        }catch(e){}
    }
});
function GetQueryString(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

</script>















