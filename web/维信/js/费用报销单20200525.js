<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript">
var indexNo = "0";//明细表下标，从0开始
var fpId = "26690";//发票
var fpZsId = "26415";//发票张数
var jeId = "26683";//金额
var zkanId = "26416";//展开按钮
var sxfpId = "26417";//所选发票id
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
}
jQuery(document).ready(function(){
    jQuery("button[name='addbutton"+indexNo+"']").bind("click",function(){
        bindFunc(1);
        
    });
    setTimeout("bindFunc(1)",1000);

});
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
</script>


