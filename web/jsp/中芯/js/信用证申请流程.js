var ANYBANK = "#field12866";//ANYBANK
var ISSUINGBANK = "#field12867";//ISSUINGBANK
var NEGOTIATION = "#field12868";//NEGOTIATION
var ACCEPTANCE = "#field12869";//ACCEPTANCE
var PAYMENT = "#field12870";//PAYMENT
var DEFERRED = "#field12871";//DEFERRED
var PAYMENTTEXT = "field12872";//PAYMENT文本
var DAYSAFTER = "field12873";//DAYS AFTER
///////////////////////
var ALLOWED1 = "#field12885";//ALLOWED1
var NOTALLOWED1 = "#field12886";//NOTALLOWED1
var ALLOWED2 = "#field12887";//ALLOWED2
var NOTALLOWED2 = "#field12888";//NOTALLOWED1
var TRANSFERABLE = "#field12889";//TRANSFERABLE
var NOTTRANSFERABLE = "#field12890";//NOTTRANSFERABLE
var WITHOUT = "#field12891";//WITHOUT
var CONFIRM = "#field12892";//CONFIRM
var MAYADD = "#field12893";//MAY ADD
////////////checkarr2
var SIGHT = "#field12874";//SIGHT
var DAYSAFTER2 = "#field12875";//DAYSAFTER2
var DRAFTSAGAINSTACCEPTANCE = "#field12876";//DRAFTSAGAINSTACCEPTANCE
var ISSUINGBANK2 = "#field12877";//ISSUINGBANK2
var FOR_C = "#field13150";//FOR_C
var FOR_F = "field12878";//PAYMENT文本
var FOR_T = "field12879";//DAYS AFTER
////////////checkarr3
var FOB = "#field12894";//FOB
var CFR = "#field12895";//CFR
var CIF = "#field12896";//CIF
var FCA = "#field12897";//FCA
var OTHER = "#field13151";//OTHER
var OTHERTERM = "field12898";//OTHERTERM
////////////
var D1 ="#field12899";//D1
var D1_1 ="field12900";//D1_1
var D1_2 ="field12901";//D1_2
var D1_3 ="field12902";//D1_3
//////checkarr4
var D2 ="#field12903";//D2
var D2_1 ="#field12904";//D2_1
var D2_2 ="field12905";//D2_2
var D2_3 ="field12906";//D2_3
var D2_4 ="#field12907";//D2_4
var D2_5 ="#field12908";//D2_5
var D2_6 ="#field12909";//D2_6
var D2_7 ="#field12910";//D2_7

var D3 ="#field12911";//D3
var D3_1 ="field12912";//D3_1
var D3_2 ="field12913";//D3_2
var D3_3 ="#field12914";//D3_3
var D3_4 ="#field12915";//D3_4

var D4 ="#field12916";//D4
var D4_1 ="field12917";//D4_1
var D4_2 ="field12918";//D4_2
var D4_3 ="field12919";//D4_3
var D4_4 ="field12920";//D4_4

var D5 ="#field12921";//D5
var D5_1 ="field12922";//D5_1
var D5_2 ="field12923";//D5_2
var D5_3 ="field12924";//D5_3
var D5_4 ="field12925";//D5_4

var D6 ="#field12926";//D6
var D6_1 ="field12927";//D6_1
var D6_2 ="field12928";//D6_2
var D6_3 ="field12929";//D6_3
var D6_4 ="field12930";//D6_4

var D7 ="#field12931";//D7
var D7_1 ="field12932";//D7_1
var D7_2 ="field12933";//D7_2
var D7_3 ="field12934";//D7_3
var D7_4 ="field12935";//D7_4

var D8 ="#field12936";//D8
var D8_1 ="field12937";//D8_1
var D8_2 ="field12938";//D8_2
var D8_3 ="field12939";//D8_3

var D9 ="#field12940";//D9
var D9_1 ="field12941";//D9_1
var D9_2 ="field12942";//D9_2

var D10 ="#field12943";//D10
var D10_1 ="field12944";//D10_1

var D11 ="#field12945";//D11
var D11_1 ="field12946";//D11_1

var A2 ="#field12948";//A2
var A2_1 ="field12949";//A2_1

var A5 ="#field12952";//A5
var A5_1 ="field12953";//A5_1

var A9 ="#field12957";//A9
var A9_1 ="field12958";//A9_1
//明细1
var sqyfje_dt1 ="#field13178_";//申请预付金额 明细1
var scgddsylcje_dt1 ="#field12972_";//采购订单剩余lc金额 明细1

jQuery(document).ready(function(){
    bindclickfun(ANYBANK +","+ISSUINGBANK);
    var checkarr1 = NEGOTIATION+","+ACCEPTANCE+","+PAYMENT+","+DEFERRED;
    bindclickfun(checkarr1);
    bindclickfun(ALLOWED1 +","+NOTALLOWED1);
    bindclickfun(ALLOWED2 +","+NOTALLOWED2);
    bindclickfun(TRANSFERABLE +","+NOTTRANSFERABLE);
    bindclickfun(WITHOUT +","+CONFIRM+","+MAYADD);
    bindclickfun(SIGHT +","+DAYSAFTER2+","+DRAFTSAGAINSTACCEPTANCE);
    var checkarr2 = ISSUINGBANK2+","+FOR_C;
    bindclickfun(checkarr2);
    var checkarr3 = FOB +","+CFR+","+CIF+","+FCA+","+OTHER;
    bindclickfun(checkarr3);
    bindclickfun(D1);
    bindclickfun( D2 +","+D2_1);
    bindclickfun(D2_4+","+D2_5);
    bindclickfun(D3_3+","+D3_4);
    bindclickfun(D3);
    bindclickfun(D4);
    bindclickfun(D5);
    bindclickfun(D6);
    bindclickfun(D7);
    bindclickfun(D8);
    bindclickfun(D9);
    bindclickfun(D10);
    bindclickfun(D11);
    bindclickfun(A2);
    bindclickfun(A5);
    bindclickfun(A9);

    var readcheck = DEFERRED +","+FOR_C+","+OTHER+","+D1+","+D2_1+","+D3+","+D4+","+D5+
        ","+D6+","+D7+","+D8+","+D9+","+D10+","+D11+","+A2+","+A5+","+A9;
    var readcheckarr = readcheck.split(",");
    for(var index=0;index<readcheckarr.length;index++){
        addremovecheck(readcheckarr[index]);
    }

    checkCustomize = function () {

        var indexnum0 = jQuery("#indexnum0").val();
        for(var index=0;index<indexnum0;index++){
            if(jQuery(sqyfje_dt1+index).length>0){
                var scgddsylcje_dt1_val = jQuery(scgddsylcje_dt1+index).val();
                var sqyfje_dt1_val = jQuery(sqyfje_dt1+index).val();
                if(scgddsylcje_dt1_val == ""){
                    scgddsylcje_dt1_val = "0";
                }
                if(sqyfje_dt1_val == ""){
                    sqyfje_dt1_val = "0";
                }
                if(Number(sqyfje_dt1_val)>Number(scgddsylcje_dt1_val)){
                   Dialog.alert("明细中申请预付金额不能大于采购订单剩余LC金额，请检查");
                   return false;
                }
            }

        }
        return true;
    }
});

function addremovecheck(fieldid){
    if(fieldid == DEFERRED){
        if(jQuery(fieldid).is(':checked')){
            addcheck(PAYMENTTEXT,0);
            addcheck(DAYSAFTER,0);
        }else{
            removecheck(PAYMENTTEXT,0);
            removecheck(DAYSAFTER,0);
        }
    }else if(fieldid == FOR_C){
        if(jQuery(fieldid).is(':checked')){
            addcheck(FOR_F,0);
            addcheck(FOR_T,0);
        }else{
            removecheck(FOR_F,0);
            removecheck(FOR_T,0);
        }
    }else if(fieldid == OTHER){
        if(jQuery(fieldid).is(':checked')){
            addcheck(OTHERTERM,0);
        }else{
            removecheck(OTHERTERM,0);
        }
    }else if(fieldid == D1){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D1_1,0);
            addcheck(D1_2,0);
            addcheck(D1_3,0);
        }else{
            removecheck(D1_1,0);
            removecheck(D1_2,0);
            removecheck(D1_3,0);
        }
    }else if(fieldid == D2_1){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D2_2,0);
            addcheck(D2_3,0);
        }else{
            removecheck(D2_2,0);
            removecheck(D2_3,0);
        }
    }else if(fieldid == D3){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D3_1,0);
            addcheck(D3_2,0);
        }else{
            removecheck(D3_1,0);
            removecheck(D3_2,0);
        }
    }else if(fieldid == D4){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D4_1,0);
            addcheck(D4_2,0);
            addcheck(D4_3,0);
            addcheck(D4_4,0);
        }else{
            removecheck(D4_1,0);
            removecheck(D4_2,0);
            removecheck(D4_3,0);
            removecheck(D4_4,0);
        }
    }else if(fieldid == D5){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D5_1,0);
            addcheck(D5_2,0);
            addcheck(D5_3,0);
            addcheck(D5_4,0);
        }else{
            removecheck(D5_1,0);
            removecheck(D5_2,0);
            removecheck(D5_3,0);
            removecheck(D5_4,0);
        }
    }else if(fieldid == D6){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D6_1,0);
            addcheck(D6_2,0);
            addcheck(D6_3,0);
            addcheck(D6_4,0);
        }else{
            removecheck(D6_1,0);
            removecheck(D6_2,0);
            removecheck(D6_3,0);
            removecheck(D6_4,0);
        }
    }else if(fieldid == D7){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D7_1,0);
            addcheck(D7_2,0);
            addcheck(D7_3,0);
            addcheck(D7_4,0);
        }else{
            removecheck(D7_1,0);
            removecheck(D7_2,0);
            removecheck(D7_3,0);
            removecheck(D7_4,0);
        }
    }else if(fieldid == D8){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D8_1,0);
            addcheck(D8_2,0);
            addcheck(D8_3,0);
        }else{
            removecheck(D8_1,0);
            removecheck(D8_2,0);
            removecheck(D8_3,0);
        }
    }else if(fieldid == D9){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D9_1,0);
            addcheck(D9_2,0);
        }else{
            removecheck(D9_1,0);
            removecheck(D9_2,0);
        }
    }else if(fieldid == D10){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D10_1,0);
        }else{
            removecheck(D10_1,0);
        }
    }else if(fieldid == D11){
        if(jQuery(fieldid).is(':checked')){
            addcheck(D11_1,0);
        }else{
            removecheck(D11_1,0);
        }
    }else if(fieldid == A2){
        if(jQuery(fieldid).is(':checked')){
            addcheck(A2_1,0);
        }else{
            removecheck(A2_1,0);
        }
    }else if(fieldid == A5){
        if(jQuery(fieldid).is(':checked')){
            addcheck(A5_1,0);
        }else{
            removecheck(A5_1,0);
        }
    }else if(fieldid == A9){
        if(jQuery(fieldid).is(':checked')){
            addcheck(A9_1,0);
        }else{
            removecheck(A9_1,0);
        }
    }
}

function bindclickfun(fieldids){
    var fieldarr = fieldids.split(",");
    for(var index=0;index<fieldarr.length;index++){
        var field = fieldarr[index];
        bindclick(fieldids,field);
    }
}
function bindclick(fieldids,fieldid){
    jQuery(fieldid).click(function () {
        changeselect(fieldids,fieldid);
        addremovecheck(fieldid);
        return true;
    })
}

//fieldids check框id组 fieldid onclick字段
function changeselect(fieldids,fieldid){
    var fieldarr = fieldids.split(",");
    for(var index=0;index<fieldarr.length;index++){
        var field = fieldarr[index];
        if(field != fieldid){
            jQuery(field).next().removeClass("jNiceChecked");
            jQuery(field).attr("checked", false);
            addremovecheck(field);
        }
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
    var htmlval= jQuery("#"+btid+"span").html();
    if(htmlval.indexOf("<img")>=0){
        if(flag=='0'){
            jQuery("#"+btid+"span").html("");
        }else{
            jQuery("#"+btid+"spanimg").html("");
        }
    }
    var parastr = document.all('needcheck').value;
    if(parastr == btid){
        parastr = "";
    }else{
        parastr = parastr.replace(","+btid,"");
    }

    document.all('needcheck').value=parastr;
}
