
/////////////////////////////主表字段
var sqrq = "#field53665";//申请日期
var backflag = "#field506722";//归还日期标志
var numflag = "#field506723";//数量标志
var ywlx = "#field378722";//业务类型
var factory = "#field53684";//工厂
//////////////////////////////明细表字段
var kmkyje_dt1 = "#field203229_";//明细1-科目可用金额
var djje_dt1 = "#field203230_";//明细1-冻结金额
var je_dt1 = "#field55496_";//明细1-总价
var km_dt1 = "#field53869_";//明细1-科目
var reqnum = "#field53675_";   //申请数量
var avablenum = "#field53709_";  //批次可用数量
var wlbhmx = "#field55108_"; //明细的物料编号
var backdate = "field496221_";//归还日期
var pici = "#field53676_"//批次


jQuery(document).ready(function(){
    jQuery("[name=copybutton0]").css('display','none');
///////////////////////////////////////产成品领料，归还日期必输，否则可编辑
    ghrqcheck();
    jQuery(ywlx).bindPropertyChange(function () {
        ghrqcheck( );
    });

    jQuery("[name=addbutton0]").live("click", function () {//只有点击加号时生效
        ghrqcheck( );
    });

    checkCustomize = function () {

        if("G099" == jQuery(factory).val() && "C" == jQuery(ywlx).val()){
            Dialog.alert("工厂为G099时，业务类型不能为“G001产成品领用”");
            return false;
        }
///////////////////////////////////////////
        var indexnum0_val = jQuery("#indexnum0").val();
        var sqrq_val = jQuery(sqrq).val();
        var comparedate =  getNextMonth(sqrq_val);
        var outmonth = false;//大于一个月标志
        var inmonth = false;//小于一个月标志
        var outnum = false;//申请数量大于规定片数标志
        var innum = false;//申请数量小于规定片数标志
        for ( var i = 0; i < indexnum0_val; i++) {
            var wlbhmxi = wlbhmx + i;  //物料编号
            if (jQuery(wlbhmxi).length > 0) {// 明细行不能为空
                var backdate_val = jQuery("#" + backdate + i).val();
                if(backdate_val > comparedate ){//大于一个月
                    outmonth = true;
                }else{
                    inmonth = true;
                }
                if(outmonth && inmonth ){//既有大于一个月的又有小于一个月的
                    Dialog.alert("明细表存在“归还日期”既有大于一个月又有小于一个月的物料，请分开提报");
                    return false;
                }

                var pici_val = jQuery(pici + i).val();
                var flag = pici_val.substring(0,1)//获取第一个字符
                var plag = pici_val.substring(7,8)//获取第八个字符
                var ruler = /[a-z]/i;
                var reqnum_val = jQuery(reqnum + i).val(); //获取申请数量
                if(ruler.test(flag)){//如果第一个字符为字母
                    if("A" == flag || "B" == flag || "C" == flag){
                        if(50 < reqnum_val){//大于50片
                            outnum = true;
                        }else{
                            innum = true;
                        }
                    }else if("K" == flag || "S" == flag){
                        if(150 < reqnum_val){//大于150片
                            outnum = true;
                        }else{
                            innum = true;
                        }
                    }else{
                        innum = true;
                    }
                }else if(ruler.test(plag)){//如果第八个字符为字母
                    if("A" == plag|| "B" == plag|| "C" == plag){
                        if(50 < reqnum_val){//大于50片
                            outnum = true;
                        }else{
                            innum = true;
                        }
                    }else if("K" == plag|| "S" == plag){
                        if(150 < reqnum_val){//大于150片
                            outnum = true;
                        }else{
                            innum = true;
                        }
                    }else{
                        innum = true;
                    }
                }else{//其他情况
                    innum = true;
                }
                if(outnum && innum){//既有片数大于要求的又有不满足
                    Dialog.alert("明细表存在“申请数量”不满足要求：A/B/C等级>50PCS或K/S等级>150PCS，请分开提报");
                    return false;
                }
            }
        }

        if(outmonth){
            jQuery(backflag).val(0);
        }else{
            jQuery(backflag).val(1);
        }
        if(outnum){
            jQuery(numflag).val(0);
        }else{
            jQuery(numflag).val(1);
        }
//////////////////////////////////////////////
        var index = 0;
        var indexnum0_val = jQuery("#indexnum0").val();
        if (Number(indexnum0_val) > 0) {
            for ( var i = 0; i < indexnum0_val; i++) {
                var wlbhmxi = wlbhmx + i;  //物料编号
                var reqnumi = reqnum + i; //申请数量
                var avablenumi = avablenum + i;//批次可用数量

                if (jQuery(wlbhmxi).length > 0) {// 明细行不能为空
                    index++;
                    var reqnumi_val = jQuery(reqnumi).val();// 申请数量值
                    var avablenumi_val = jQuery(avablenumi).val();// 批次可用数量值

                    if (reqnumi_val.trim().length > 0) {// 申请数量值不能为空
                        //alert("22222222222::" + reqnumi_val + ":::"+ avablenumi_val);
                        if (parseFloat(reqnumi_val) > parseFloat(avablenumi_val) ) {// 如果申请数量大于批次可用数量
                            //alert("3333344::" + reqnumi_val + ":::"+ avablenumi_val);
                            Dialog.alert("明细第" + index + "“申请数量”大于“批次可用数量”：" + avablenumi_val);
                            return false;
                        }
                    }
                }

            }
        }
/////////////////////////////////////////////

        var countnum=0;
        var indexnum0 = jQuery("#indexnum0").val();
        for(var index=0; index<indexnum0;index++){
            if(jQuery(km_dt1+index).length>0){
                countnum= countnum+1;
                var kmkyje_dt1_val = jQuery(kmkyje_dt1+index).val();
                var djje_dt1_val = jQuery(djje_dt1+index).val();
                var je_dt1_val = jQuery(je_dt1+index).val();
                var km_dt1_val = jQuery(km_dt1+index).val();
                if(km_dt1_val =="1221"){
                    continue;
                }
                if(je_dt1_val==""){
                    je_dt1_val = "0";
                }
                if(kmkyje_dt1_val==""){
                    kmkyje_dt1_val = "0";
                }
                if(djje_dt1_val==""){
                    djje_dt1_val = "0";
                }
                var total_je = getTotalJe(km_dt1_val);
                if(accSub(kmkyje_dt1_val,djje_dt1_val)<total_je ){
                    Dialog.alert("第"+countnum+"行明细存在申请金额大于科目可用金额-采购申请冻结金额，请检查！");
                    return false;
                }
            }
        }
        return true;
    }
});

function accSub(arg1,arg2){
    var r1,r2,m,n;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    //动态控制精度长度
    n=(r1>=r2)?r1:r2;
    return ((arg1*m-arg2*m)/m).toFixed(n);
}

function getTotalJe(km_val){
    var indexnum0 = jQuery("#indexnum0").val();
    var je=0;
    for(var index=0; index<indexnum0;index++){
        if(jQuery(km_dt1+index).length>0){
            var km_dt1_val =  jQuery(km_dt1+index).val();
            var je_dt1_val =  jQuery(je_dt1+index).val();
            if(je_dt1_val ==''){
                je_dt1_val ='0';
            }
            if(km_val==km_dt1_val ){
                je = je + Number(je_dt1_val);
            }
        }
    }
    return je;
}
function getNextMonth(date) {
    var arr = date.split('-');
    var year = arr[0]; //获取当前日期的年份
    var month = arr[1]; //获取当前日期的月份
    var day = arr[2]; //获取当前日期的日
    var days = new Date(year, month, 0);
    days = days.getDate(); //获取当前日期中的月的天数
    var year2 = year;
    var month2 = parseInt(month) + 1;
    if (month2 == 13) {
        year2= parseInt(year2) + 1;
        month2 = 1;
    }
    var day2 = day;
    var days2 = new Date(year2, month2, 0);
    days2 = days2.getDate();
    if (day2 > days2) {
        day2 = days2;
    }
    if (month2 < 10) {
        month2 = '0'+month2;
    }
    var t2 = year2 + '-' + month2 + '-' + day2;
    return t2;
}


function ghrqcheck(){
    var indexnum0_val = jQuery("#indexnum0").val();
    for ( var i = 0; i < indexnum0_val; i++) {
        var wlbhmxi = wlbhmx + i;  //物料编号
        if (jQuery(wlbhmxi).length > 0) {// 明细行不能为空

            if("C" == jQuery(ywlx).val()){//G001产成品领用
                WfForm.changeFieldAttr(backdate + i, 3);
            }else{
                WfForm.changeFieldAttr(backdate + i, 2);
            }
        }
    }
}
