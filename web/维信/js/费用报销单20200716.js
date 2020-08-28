<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">
var indexNo = "0";//明细表下标，从0开始
var fpId = "128602";//发票
var fpZsId = "128603";//发票张数
var jeId = "128606";//金额
var je2Id = "128582";//实际报销金额
var zkanId = "128604";//展开按钮
var sxfpId = "128605";//所选发票id
var yrfwlxId = "128579";//货物或应税服务类型
var zwlxId = "128630";//座位类型
var sjbxje_dt1 = "#field128582_";//明细1实际报销金额
var bxje_dt1 = "#field128606_";//明细1票面金额
var fplx_dt1 = "#field128616_";//明细1发票类型
var sj_dt1 = "#field128590_";//明细1税金
var sfcb_dt1 = "#field128632_";//明细1是否超标
var bxxm_dt1 = "#field150064_";//明细1报销项目
var lcxz_dt1 = "field128634_";//明细1流程选择
var sfwzdf_dt1 = "#field128633_";//明细1是否为招待费
var dissfwzdf_dt1 = "#disfield128633_";//明细1是否为招待费
function bindFunc(value){
    var indexnum0 = -1;
    var _xm_array3 = jQuery("input[name='check_node_"+indexNo+"']");
    if(_xm_array3.length > 0){
        var _idx = parseInt(jQuery(_xm_array3[_xm_array3.length-1]).val());
        if(!isNaN(_idx) && _idx>=0){
            indexnum0 = _idx;
        }
    }
    if(indexnum0>=0){
        if(value==1){
            jQuery("#field"+fpId+"_"+indexnum0).bindPropertyChange(function(targetobj){
                var _idx1 = parseInt(jQuery(targetobj).attr("name").replace("field"+fpId+"_",""));
                invoiceChange(_idx1);
            });
            $("#field"+zkanId+"_"+indexnum0+"span").html("<input type='button' class='e8_btn_top_first' value='展开' title='展开' "+
                " onclick='zkAction("+indexnum0+");' style='max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>");
        }else if(value==2){
            jQuery("#field"+fpId+"_"+indexnum0).bindPropertyChange(function(targetobj){
                var _idx1 = parseInt(jQuery(targetobj).attr("name").replace("field"+fpId+"_",""));
                invoiceChange(_idx1);
            });
            var indexNum = $("#indexnum"+indexNo).val();
            var indexNums = indexNum.split(",");
            var indexNumsLen = indexNums.length;
            for(var i=0;i<indexNumsLen;i++){
                var _idx1 = indexNums[i];
                $("#field"+zkanId+"_"+_idx1+"span").html("<input type='button' class='e8_btn_top_first' value='展开' title='展开' "+
                    " onclick='zkAction("+_idx1+");' style='max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;'>");
            }
        }
    }
}
function setOtherFormData(__index,invoiceServiceYype,seat){
    $("#field"+yrfwlxId+"_"+__index).val(invoiceServiceYype);
    $("#field"+yrfwlxId+"_"+__index+"span").text(invoiceServiceYype);
    $("#field"+zwlxId+"_"+__index).val(seat);
    $("#field"+zwlxId+"_"+__index+"span").text(seat);
}
function invoiceChange(index){
    var invoices = jQuery("#field"+fpId+"_"+index).val();
    if(invoices==""){
        setFormData(index,0,"","0.00");
    }

}
function invoiceCallBack(e,json,name){
    var index = name.replace("field"+fpId+"_","");
    var ids = jQuery("#field"+fpId+"_"+index).val();
    var _data = "op=getInvoicedata&ids="+ids;
    jQuery.ajax({
        url:"/fna/invoice/invoiceLedger/invoiceAjax.jsp",
        type:"post", cache:false, processData:false, data:_data, dataType:"json", async:false,
        success: function do4Success(_json){
            var taxIncludedPrice = _json.taxIncludedPrice;
            setFormData(index,1,ids,taxIncludedPrice);
            //其他字段
            var invoiceServiceYype = _json.invoiceServiceYype;
            var seat = _json.seat;
            setOtherFormData(index,invoiceServiceYype,seat);
        }
    });
}
function zkAction(index){
    var xsfpVal = $("#field"+sxfpId+"_"+index).val();
    if(xsfpVal==""){
        xsfpVal = jQuery("#field"+fpId+"_"+index).val();
    }
    var _w = 700;
    var _h = 400;
    __openDialog("发票列表","/fna/invoice/invoiceLedger/FnaInvoice.jsp?ids="+xsfpVal,
        _w, _h, true);
}
function setFormData(__index,fpzsVal,xsfpVal,jeVal){
    $("#field"+fpZsId+"_"+__index).val(fpzsVal);
    $("#field"+fpZsId+"_"+__index+"span").text(fpzsVal);
    $("#field"+sxfpId+"_"+__index).val(xsfpVal);
    $("#field"+sxfpId+"_"+__index+"span").text(xsfpVal);
    $("#field"+jeId+"_"+__index).val(jeVal);
    $("#field"+jeId+"_"+__index+"span").text(jeVal);
    $("#field"+je2Id+"_"+__index).val(jeVal);
    $("#field"+je2Id+"_"+__index+"span").text(jeVal);
}
jQuery(document).ready(function(){
    jQuery("button[name='addbutton"+indexNo+"']").bind("click",function(){
        bindFunc(1);
        var indexnum0 = jQuery("#indexnum0").val();
        binddt(indexnum0-1);

    });
    setTimeout("bindFunc(1)",1000);
    setTimeout("binddetail()",1000);
    //setTimeout("binddetail()",1000);
    checkCustomize = function () {
        var indexnum0=jQuery("#indexnum0").val();
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
                    Dialog.alert("明细1中存在实际报销金额大于票面金额的数据，请检查");
                    return false;
                }

            }
        }
        return true;
    }
});
function binddetail(){
    var indexnum1 = jQuery("#indexnum0").val();
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
        jQuery(sfcb_dt1+index).parent().parent().css({"background-color":"yellow"});
    }else{
        jQuery(sfcb_dt1+index).parent().parent().removeAttr("style","");
    }
}
//增加必填
function changebt(index){
    var bxxm_dt1_val = jQuery(bxxm_dt1+index).val();
    if(bxxm_dt1_val == "24" || bxxm_dt1_val == "22"){//差旅   培训
        addcheck(lcxz_dt1+index,"1");
    }else{
        removecheck(lcxz_dt1+index,"1");
    }
}
function binddt(index){
    jQuery(sfcb_dt1+index).bindPropertyChange(function(){
        changecolor(index);

    });

    jQuery(bxxm_dt1+index).bindPropertyChange(function(){
        jQuery("#"+lcxz_dt1+index).val("");
        jQuery("#"+lcxz_dt1+index+"span").html("");
        var bxxm_dt1_val = jQuery(bxxm_dt1+index).val();
        changebt(index);
        if(bxxm_dt1_val=='33'){
            jQuery(sfwzdf_dt1+index).val("0");
            jQuery(dissfwzdf_dt1+index).val("0");
            jQuery(sfwzdf_dt1+index).trigger("change")
        }else{
            jQuery(sfwzdf_dt1+index).val("1");
            jQuery(dissfwzdf_dt1+index).val("1");
            jQuery(sfwzdf_dt1+index).trigger("change")
        }
    });


}


function __openDialog(_title, _url, _w, _h, _showCloseButton){
    diag = new window.top.Dialog();
    diag.currentWindow = window;
    diag.URL = _url;
    diag.Title = _title;
    diag.Width = _w;
    diag.Height = _h;
    if(_showCloseButton!=null){
        diag.ShowCloseButton = _showCloseButton;
    }
    diag.isIframe=false;
    diag.show();
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

</script>




