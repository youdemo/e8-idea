<script type="text/javascript" src="/morningcore/js/cw.js"></script>
    <script type="text/javascript">

/*明细全部为预算内或无预算外，主表预算内  0
*明细全部为不控制，主表不控制    1
*明细有一个预算外，主表预算外    1
*/
var yskzje = '#field14264';//预算控制金额
var jkje = '#field14265';//借款金额
var ygje = '#field14267';//预估金额
var ygje_dt1 = '#field14226_';//预估金额明细表1
var cfrq = '#field14209_';//出发日期
var ddrq = '#field14210_';//到达日期  12165
var cfdd = '#field14327_';//出发地点
var dddd = '#field14326_';//到达地点
var ysjl = '#field14268';//预算结论
var jlhz = '#field14271';//结论汇总
var mxjlhz = '#field14272_';//明细表结论汇总
var bmjebj_dt1 = '#field14299_';//部门金额比较
var bmysjl_dt1 = '#field14270_';//部门预算结论
var xmjebj_dt1 = '#field14300_';//项目金额比较
var xmysjl_dt1 = '#field14269_';//项目预算结论
var gsbm = '#field14274';//公司/部门
var xmmc = "#field14273";//项目名称
var km_dt1 = "#field14304_";//科目 明细一
var hbbh_dt1 = "#field14287_";//合并编号 明细一
var fycdbm = "#field14286";//费用承担部门
var yskzbz_dt1 = "#field14280_";//预算控制标准 明细一
var lsjkje = "#field14266";//历史借款金额 主表
var yskzfsDT1 = "#field14279_";//预算控制方式 明细一
var jyfsDT1 = "#field14225_";//交通方式 明细一
var djsjDT1 = "field15793_";//登记时间 明细一
var ygjeDT1 = "#field14226_";//预估金额 明细一
var zkDT1 = "field15794_";//折扣 明细一
var lxDT1 = "field16929_";//类型 明细一
var fybzDT1 = "#field14317_";//费用标准 明细一
var sfjk = "#field14218";//是否借款
var cslx = "#field14325_";//城市类型
var qujb = "#field14324_";//安全级别
var ccr = "field14219";//出差人
var msjs = "#field14333";//秘书角色
var ysjlms = "#field15735";//预算结论描述
var ccts = "#field14204";//出差天数
var cclx = "#field14214";//出差类型


function getMustChageDept(){
    var dept_1 = "#field14305";   // 项目带出
    var dept_2 = "#field14332";     //  申请部门
    var id_xxxs = "#field14274";   //  判断字段
    var dept_3 = "#field14286"; // 最终字段
    var id_xxxs_val = jQuery(id_xxxs ).val();

    if(id_xxxs_val != '' && id_xxxs_val  != '公司'){
        jQuery(dept_3 ).val( jQuery(dept_1 ).val());
        jQuery(dept_3 +"span" ).html( jQuery(dept_1 +"span").html());
    } else{
        jQuery(dept_3 ).val( jQuery(dept_2 ).val());
        jQuery(dept_3 +"span" ).html( jQuery(dept_2 +"span").html());
    }
}

jQuery(document).ready(function () {
    var fykm = "field14304";//费用科目--   长途 129 ,123 住宿 132  125
    var fybz = "#field14317_";//费用标准
    var cfrq = "field14209";//出发日期
    var jsrq = "field14210";//结束日期
    var zsts = "field14213";//住宿天数
    var jtgj = "field14225";//交通工具
    var cfdd = "field14327";//出发地点
    var dddd = "field14326";//到达地点

    //费用承担部门检查
    jQuery("#field14274").bindPropertyChange(function(){
        getMustChageDept();
    })

    //生成requestid不执行该方法
    if(jQuery("input[name=requestid]").val() == '-1'){
        setTimeout('getMustChageDept()','500');
    }

    setTimeout('addDetailRow()','500');

    jQuery("#field14204").attr("readonly","readonly");

    //秘书角色
    jQuery(msjs).bindPropertyChange(function(){
        setCCR();
    })
    //出差天数 cclx
    jQuery(ccts).bindPropertyChange(function(){
        setJCJL();
    })

    //出差类型
    jQuery(cclx).bindPropertyChange(function(){
        setByCclx();
    })
    setTimeout('setCCR()','500')


    _C.run2(fykm, function (p) {

        if(p.v.n == ''){
            _C.v(cfrq+p.r,'');//赋值
            _C.rs(cfrq+p.r,false);//去除必填
            _C.v(jsrq+p.r,'');//
            _C.rs(jsrq+p.r,false);
            _C.v(zsts+p.r,'');//
            _C.rs(zsts+p.r,false);
            _C.v(jtgj+p.r,'');//
            _C.rs(jtgj+p.r,false);
            jQuery("#field14209"+p.r+"browser").attr("disabled","disabled");
            jQuery("#field14210"+p.r+"browser").attr("disabled","disabled");
            jQuery("#"+jtgj+p.r).attr("disabled","disabled");
        }else{
            var fykm_val = _C.v(fykm+p.r);
            var zsts_val = _C.v(zsts+p.r);

            if(fykm_val=='129' ||fykm_val=='123' ){
                if(_C.v(cfrq+p.r) == ''){
                    jQuery("#field14209"+p.r+"browser").removeAttr('disabled');
                    _C.rs(cfrq+p.r,true);//必填

                }
                if(_C.v(jsrq+p.r) == ''){
                    jQuery("#field14210"+p.r+"browser").removeAttr('disabled');
                    _C.rs(jsrq+p.r,true);//必填
                }
                if(_C.v(jtgj+p.r) == ''){
                    jQuery("#"+jtgj+p.r).removeAttr('disabled');
                    _C.rs(jtgj+p.r,true);//必填
                }
                _C.rs(cfdd+p.r,true);//必填
                _C.rs(dddd+p.r,true);//必填





            }else{
                _C.v(cfrq+p.r,'');//赋值
                _C.rs(cfrq+p.r,false);//去除必填
                _C.v(jsrq+p.r,'');//赋值
                _C.rs(jsrq+p.r,false);//去除必填
                _C.v(jtgj+p.r,'');//
                _C.rs(jtgj+p.r,false);
                _C.rs(cfdd+p.r,false);//必填
                _C.rs(dddd+p.r,false);//必填

                jQuery("#field14209"+p.r+"browser").attr("disabled","disabled");
                jQuery("#field14210"+p.r+"browser").attr("disabled","disabled");
                jQuery("#"+jtgj+p.r).attr("disabled","disabled");
            }


            if(fykm_val == '132' ){
                _C.rs(dddd+p.r,true);//必填
            }else{
                if(fykm_val != '129'){
                    _C.rs(dddd+p.r,false);//必填
                }
            }

        }
    })
    _C.run2(jtgj, function (p) {
        if(_C.v(jtgj+p.r) !=''){
            //alert(1);
            jQuery("#"+jtgj+p.r+"span").html('');
        }

    })
// _C.run2("field14268", function (p) {
    // var jsjl_zb = _C.v("field14268");//主表预算结论
    // if(jsjl_zb ==''){
    // //alert(1);
    // jQuery("#field14268").empty();

    // jQuery("#field14268").val("");
    // }else{
    // jQuery("#field14268").empty();
    // if(jsjl_zb == '0'){
    // jQuery("#field14268").append("<option value='0'>预算内</option>");
    // }else {
    // jQuery("#field14268").append("<option value='1'>预算外</option>");
    // }
    // }

    // })
    _C.run2("field14226", function (p) {//预估金额
        var kmid = _C.v("field14304"+p.r);
        if(kmid != '' ){
            setZhuJl();
        }

    })
    //20190807
    //20190811
    _C.run2(cfrq, function (p) {
        checkRQ(p.r);
        getCcts();
    })
    _C.run2(jsrq, function (p) {
        checkRQ(p.r);
        getCcts();
    })
    _C.run2("field14218", function (p) {//是否借款
        if(_C.v("field14218") == '0'){
            _C.v("field14265",'','');
        }

    })




    //项目的名称
    jQuery(xmmc).bindPropertyChange(function(){
        //changeDtJL1()
        accJcjl();
    })
    var nowIndex = document.getElementById("indexnum0").value * 1.0 - 1;

    jQuery("button[name='addbutton0']").bind("click",function() {
        bindDetail1(1);
    });

    //明细一变化事件
    jQuery("#nodesnum0").bindPropertyChange(function(){
        getCcts();
    })
    jQuery("#indexnum0").bindPropertyChange(function(){
        setJCJL();
    })
    setTimeout('bindDetail1(2)','500');
    setTimeout('setJCJL()','500');

    //提交校验
    checkCustomize = function () {
        var yskzje_v = jQuery(yskzje).val();//预算控制金额
        var jkje_v = jQuery(jkje).val();//借款金额
        var ygje_v = jQuery(ygje).val();//预估金额
        var lsjkje_v = jQuery(lsjkje).val();//历史借款金额
        var jlhz_v = jQuery(jlhz).val();
        var sfjk_v = jQuery(sfjk).val();
        var cclx =  jQuery("#field14214").val();//出差类型
        if(cclx == '0'){//待定
            var sfcb = checkIFcb();
            if(sfcb.length>0){
                var mymessage=confirm("第"+sfcb+"行，住宿预申请金额大于出差天数*出差住宿标准");
                if(!mymessage){
                    return false;
                }
            }
            var re = checkBtIFcb();
            if(re == '1'){
                return false;
            }
        }

        //借款金额 2019-08-13
        if(cclx == '0'){//境内
            if(Number(jkje_v)>Number(yskzje_v)||Number(jkje_v)>Number(ygje_v)){
                Dialog.alert("借款金额不可大于预算控制金额和预估金额");
                return false;
            }
        }else if (cclx == '1'){//境外
            if(Number(jkje_v)>Number(ygje_v)){
                Dialog.alert("借款金额不可大于预估金额");
                return false;
            }
        }


        if(lsjkje_v>0&&sfjk_v=='1' ){
            Dialog.alert("历史借款金额大于0不能提交");
            return false;
        }

        return true;
    }
})

//出差人
function setCCR(){
    if(jQuery(msjs).val() >0 ){
        jQuery("#"+ccr+"span span span").show();
        jQuery("#"+ccr+"_browserbtn").show();
        jQuery("#out"+ccr+"div").css("border-left","1px solid #E7E7E7");
        jQuery("#innerContent"+ccr+"div").css("border","1px solid #E9E9E2");
    }else if (jQuery(msjs).val() == '0' ){
        jQuery("#"+ccr+"span span span").hide();
        jQuery("#"+ccr+"_browserbtn").hide();
        jQuery("#out"+ccr+"div").css("border-left","0px solid #E7E7E7");
        jQuery("#innerContent"+ccr+"div").css("border","0px solid #E9E9E2");
    }
}

//明细表预算控制方式
function setJCJL(){
    for(var i = 0;i<jQuery("#indexnum0").val();i++){
        (function(j){
            //预算控制方式
            // jQuery(yskzfsDT1+j).bindPropertyChange(function(){
            // setBMYS(j);
            // setXMYS(j)
            // });
            // //部门金额（辅）
            // jQuery(bmjebj_dt1+j).bindPropertyChange(function(){
            // setBMYS(j);
            // });
            // //项目金额（辅）
            // jQuery(xmjebj_dt1+j).bindPropertyChange(function(){
            // setXMYS(j)
            // });
            //交通方式
            jQuery(jyfsDT1+j).bindPropertyChange(function(){
                jtfschange(j)
            });

            //科目
            jQuery(km_dt1+j).bindPropertyChange(function(){
                setTimeout("setYGJE("+j+")","1000");
                setTimeout("setLDZ("+j+")","1000");
            });

            //费用标准
            jQuery(fybzDT1+j).bindPropertyChange(function(){
                setTimeout("setYGJE("+j+")","1000");
                setTimeout("setLDZ("+j+")","1000");
            });


            //生成requestid不执行该方法
            if(jQuery("input[name=requestid]").val() == '-1'){
                setTimeout("setYGJE("+j+")","1000");//预算结论
            }

            setTimeout("setLDZ("+j+")","1000");
            setTimeout("jtfschange("+j+")","1000");
        })(i);
    }
}
//部门预算结论
// function setBMYS(index){
// var bmjeValue = jQuery(bmjebj_dt1+index).val();
// var ysfsValue = jQuery(yskzfsDT1+index).val();

// if(ysfsValue === '0'){ //年度总量控制
// if(bmjeValue >= 0.00){
// jQuery(bmysjl_dt1+index).val();
// jQuery(bmysjl_dt1+index).empty();
// jQuery(bmysjl_dt1+index).append("<option value='0'>预算内</option>");
// }else if(bmjeValue <0.00){
// jQuery(bmysjl_dt1+index).val();
// jQuery(bmysjl_dt1+index).empty();
// jQuery(bmysjl_dt1+index).append("<option value='1'>预算外</option>");
// }else{
// jQuery(bmysjl_dt1+index).val();
// jQuery(bmysjl_dt1+index).empty();
// jQuery(bmysjl_dt1+index).append("<option value='2'>不控制</option>");
// }
// }else if(ysfsValue === '1'){//事项对照控制
// jQuery(bmysjl_dt1+index).empty();
// jQuery(bmysjl_dt1+index).append("<option value=''></option>");
// jQuery(bmysjl_dt1+index).val("");
// }else if(ysfsValue === '2'){//不控制
// jQuery(bmysjl_dt1+index).val();
// jQuery(bmysjl_dt1+index).empty();
// jQuery(bmysjl_dt1+index).append("<option value='2'>不控制</option>");
// }else{
// jQuery(bmysjl_dt1+index).val();
// jQuery(bmysjl_dt1+index).empty();
// }
// setZhuJl();





// }
//项目预算结论
// function setXMYS(index){
// var xmjeValue = jQuery(xmjebj_dt1+index).val();
// var ysfsValue = jQuery(yskzfsDT1+index).val();

// if(ysfsValue === '0'){ //年度总量控制
// if(xmjeValue >= 0.00){
// jQuery(xmysjl_dt1+index).val();
// jQuery(xmysjl_dt1+index).empty();
// jQuery(xmysjl_dt1+index).append("<option value='0'>预算内</option>");
// }else if(bmjeValue <0.00){
// jQuery(xmysjl_dt1+index).val();
// jQuery(xmysjl_dt1+index).empty();
// jQuery(xmysjl_dt1+index).append("<option value='1'>预算外</option>");
// }else{
// jQuery(xmysjl_dt1+index).val();
// jQuery(xmysjl_dt1+index).empty();
// jQuery(xmysjl_dt1+index).append("<option value='2'>不控制</option>");
// }
// }else if(ysfsValue === '1'){//事项对照控制
// jQuery(xmysjl_dt1+index).empty();
// jQuery(xmysjl_dt1+index).append("<option value=''></option>");
// jQuery(xmysjl_dt1+index).val("");
// }else if(ysfsValue === '2'){//不控制
// jQuery(xmysjl_dt1+index).val();
// jQuery(xmysjl_dt1+index).empty();
// jQuery(xmysjl_dt1+index).append("<option value='2'>不控制</option>");
// }else{
// jQuery(xmysjl_dt1+index).val();
// jQuery(xmysjl_dt1+index).empty();
// }
// setZhuJl();
// }

function setByCclx(){
    if(jQuery(cclx).val() === '1'){
        if(jQuery("#nodesnum0").val()>0){
            jQuery("[name = check_node_0]:checkbox").attr("checked", true);
            deleteRow0(0);
        }
    }
}

//删除确认方法
isdel=function(){
    return true;
}

//预估金额计算
function setYGJE(index){
    if(jQuery("input[name=requestid]").val() == '-1'){
        if (jQuery(km_dt1+index).val() === '131'){
            jQuery(ygjeDT1+index).val(Number(4*150+80*Number(jQuery(ccts).val())).toFixed(2));
            jQuery(ygjeDT1+index).change();
            jQuery(ygjeDT1+index).focus();
            jQuery(ygjeDT1+index).blur();
        }
        if (jQuery(km_dt1+index).val() === '134'){
            jQuery(ygjeDT1+index).val((Number(jQuery(ccts).val()) * Number(jQuery(fybzDT1+index).val())).toFixed(2));
            //jQuery(ygjeDT1+index+"span").html((Number(jQuery(ccts).val()) * Number(jQuery(fybzDT1+index).val())).toFixed(2));
            jQuery(ygjeDT1+index).change();
            jQuery(ygjeDT1+index).focus();
            jQuery(ygjeDT1+index).blur();
        }
        calSum(0);
    }
}

//交通方式改变事件
function jtfschange(index){

    //交通工具飞机
    if(jQuery(jyfsDT1+index).val() === '0'){
        jQuery("#"+djsjDT1+index).removeAttr("disabled");
        jQuery("#"+zkDT1+index).removeAttr("disabled");
        _C.rs(djsjDT1+index,true);//登记时间
        _C.rs(zkDT1+index,true);//折扣

    }else{
        jQuery("#"+djsjDT1+index).val("");
        jQuery("#"+zkDT1+index).val("");
        jQuery("#"+djsjDT1+index).attr("disabled","disabled");
        jQuery("#"+zkDT1+index).attr("disabled","disabled");
        _C.rs(djsjDT1+index,false);//登记时间
        _C.rs(zkDT1+index,false);//折扣
    }
}


//根据科目 设置 类型 登记方式 折扣只读
function setLDZ(index){
    //住宿、境内住宿交通费、补贴  到达地点
    if( jQuery(km_dt1+index).val()  === '132' || jQuery(km_dt1+index).val()  === '131' || jQuery(km_dt1+index).val()  === '134' ){
        _C.rs(lxDT1+index,false);//折扣
        jQuery("#"+djsjDT1+index).val("");
        jQuery("#"+djsjDT1+index).attr("disabled","disabled");
        jQuery("#"+lxDT1+index).val("");
        jQuery("#"+lxDT1+index).attr("disabled","disabled");
        jQuery("#"+lxDT1+index+"_browserbtn").attr("disabled","disabled");
        jQuery("#"+lxDT1+index+"_browserbtn").hide();
        jQuery("#"+lxDT1+index+"__").attr("disabled","disabled");
        jQuery("#innerContent"+lxDT1+index+"div").css("border",'rgb(255,255,255)');
        jQuery("#out"+lxDT1+index+"div").css("border",'rgb(255,255,255)');
        jQuery("#"+zkDT1+index).val("");
        jQuery("#"+zkDT1+index).attr("disabled","disabled");
        jQuery("#"+cfdd.replace("#","")+index).val("");
        jQuery("#"+cfdd.replace("#","")+index).attr("disabled","disabled");
        jQuery("#"+cfdd.replace("#","")+index+"_browserbtn").attr("disabled","disabled");
        jQuery("#"+cfdd.replace("#","")+index+"_browserbtn").hide();
        jQuery("#"+cfdd.replace("#","")+index+"__").attr("disabled","disabled");
        jQuery("#innerContent"+cfdd.replace("#","")+index+"div").css("border",'rgb(255,255,255)');
        jQuery("#out"+cfdd.replace("#","")+index+"div").css("border",'rgb(255,255,255)');

        jQuery("#out"+cfdd.replace("#","")+index+"div").css("border",'rgb(255,255,255)');
    }

    //到达地点
    if(jQuery(km_dt1+index).val()  === '131' || jQuery(km_dt1+index).val()  === '134'){

        jQuery("#"+dddd.replace("#","")+index).val("");
        jQuery("#"+dddd.replace("#","")+index).attr("disabled","disabled");
        jQuery("#"+dddd.replace("#","")+index+"_browserbtn").attr("disabled","disabled");
        jQuery("#"+dddd.replace("#","")+index+"_browserbtn").hide();
        jQuery("#"+dddd.replace("#","")+index+"__").attr("disabled","disabled");
        jQuery("#innerContent"+dddd.replace("#","")+index+"div").css("border",'rgb(255,255,255)');
        jQuery("#out"+dddd.replace("#","")+index+"div").css("border",'rgb(255,255,255)');
    }

    //类型
    if(jQuery(km_dt1+index).val()  === '129'){
        if (jQuery(km_dt1+index).val() === ''){
            jQuery("#"+lxDT1+index).val("");
            jQuery("#"+lxDT1+index).removeAttr("disabled");
            jQuery("#"+lxDT1+index+"_browserbtn").removeAttr("disabled");
            jQuery("#"+lxDT1+index+"_browserbtn").show();
            jQuery("#"+lxDT1+index+"__").removeAttr("disabled");
            jQuery("#innerContent"+lxDT1+index+"div").css("border",'1px solid #E7E7E7');
            jQuery("#out"+lxDT1+index+"div").css("border-left",'1px solid #E7E7E7');
        }
        _C.rs(lxDT1+index,true);//类型

    }
}



function bindDetail1(value) {
    //alert(value);
    var nowIndex = document.getElementById("indexnum0").value * 1.0 - 1;
    if (nowIndex >= 0) {
        if (value == 1) { //当前添加的行
            //setTimeout('changeDtJL('+nowIndex+')','500');
            changeDtJL(nowIndex);
            //accJcjl();
        }
        if (value == 2) {//初始化
            jQuery("input[name='check_node_0']").each(function(i) {
                var nowIndex = this.value;
                //setTimeout('changeDtJL('+nowIndex+')','500');
                changeDtJL(nowIndex);
                //accJcjl();
            });
        }
    }

}
function changeDtJL(j){
    //alert('j=' + j);
    //alert(j);

    jQuery(km_dt1+j).bindPropertyChange(function(){
        var hbbh = jQuery(hbbh_dt1+j).val().toString();
        jQuery(hbbh_dt1+j).val("");
        setTimeout('jQuery(hbbh_dt1+j).val('+hbbh.toString()+')','1000');
    })
    jQuery(yskzbz_dt1+j).bindPropertyChange(function(){
        changeDt1JL(j);
        //setTimeout('accJcjl()','500');
        accJcjl();
    })
    jQuery(ygje_dt1+j).bindPropertyChange(function(){
        //alert('xmjebj_dt1='+xmjebj_dt1);
        changeDt1JL(j);
        //setTimeout('accJcjl()','500');
        accJcjl();
    })


}

//项目比较
function changeDtJL1(){
    for(var j = 0 ;j<jQuery("#indexnum0").val();j++){
        //费用科目清空的
        jQuery(km_dt1+j).val("");
        jQuery(km_dt1+j+"span span").html("");
        jQuery(km_dt1+j).change();//change 触发改变事件
        changeDt1JL(j);

    }
}
//明细所有逻辑
function changeDt1JL(j){

    var yskzbz_v = jQuery(yskzbz_dt1+j).val();
    var gsbm_v = jQuery(gsbm).val();
    var bmjl_v = jQuery(bmysjl_dt1+j).val();
    var xmjl_v = jQuery(xmysjl_dt1+j).val();
    //alert('bmjl_v1='+bmjl_v);
    //alert('xmjl_v1='+xmjl_v);
    if(jQuery(xmmc).val() == '1'){
        if(bmjl_v=='1'){//预算外
            jQuery(mxjlhz+j).val(1);
        }else if(bmjl_v=='0'){//预算内
            jQuery(mxjlhz+j).val(0);
        }else if(bmjl_v=='2'){//不控制
            jQuery(mxjlhz+j).val(2);
        }else{
            jQuery(mxjlhz+j).val('');
        }
    }else{
        if(gsbm_v=='公司'){
            if(xmjl_v=='1'){//预算外
                jQuery(mxjlhz+j).val(1);
            }else if(xmjl_v=='0'){//预算内
                jQuery(mxjlhz+j).val(0);
            }else if(xmjl_v=='2'){//不控制
                jQuery(mxjlhz+j).val(2);
            }else{
                jQuery(mxjlhz+j).val('');
            }
        }else{
            if(bmjl_v=='1'){//预算外
                jQuery(mxjlhz+j).val(1);
            }else if(bmjl_v=='0'){//预算内
                jQuery(mxjlhz+j).val(0);
            }else if(bmjl_v=='2'){//不控制
                jQuery(mxjlhz+j).val(2);
            }else{
                jQuery(mxjlhz+j).val('');
            }
        }

    }

}
function accJcjl(){
    //alert('进入----');
    var temp = '';
    for(var i=0;i<jQuery('#indexnum0').val();i++){
        if(jQuery(km_dt1+i).length>0){
            var yskzbz_v = jQuery(yskzbz_dt1+i).val();
            var mxjlhz_v = jQuery(mxjlhz+i).val();
            //alert('mxjlhz_v----'+mxjlhz_v);
            temp += mxjlhz_v;
        }

    }
}

function accAdd(arg1,arg2){
    var r1,r2,m;
    try{
        r1=arg1.toString().split(".")[1].length
    }catch(e){
        r1=0
    }
    try{
        r2=arg2.toString().split(".")[1].length
    }catch(e){
        r2=0
    }
    m=Math.pow(10,Math.max(r1,r2))
    return (arg1*m+arg2*m)/m
}
function sethbbh(hbbh,j){
    jQuery(hbbh_dt1+j).val(hbbh);
}
function setZhuJl(){
    var gsbm_val = jQuery(gsbm).val();
    var xmmc_val = jQuery(xmmc).val();
    var xmky = "#field14294_";//项目可用
    var bmky = "#field14293_";//部门可用
    var mxyg = "#field14226_";//明细预估
    var mxtyfkid = "#field14309_";//明细统一费控id
    var yskzbz1 = "#field14280_";//预算控制标准
    var num1 = jQuery("#indexnum0").val();

    for(var i=0;i<num1;i++){
        var sumYg = 0;//预估之和
        if(jQuery(xmysjl_dt1+i).length>0){//field14269_
            var xmjl_val = jQuery(xmysjl_dt1+i).val();
            var bmjl_val = jQuery(bmysjl_dt1+i).val();
            var xmky_val = jQuery(xmky+i).val();
            var bmky_val = jQuery(bmky+i).val();
            var mxtyfkid_val = jQuery(mxtyfkid+i).val();
            var mxyg_val = jQuery(mxyg+i).val();
            var yskzbz1_v = jQuery(yskzbz1+i).val();
            if(yskzbz1_v == '2'){
                continue;
            }
            if(mxyg_val.length<1){
                mxyg_val = 0;
            }
            sumYg = mxyg_val;
            for(var j=0;j<num1;j++){
                if(jQuery(xmysjl_dt1+j).length>0){
                    if(j!=i){
                        var mxtyfkid_valnew = jQuery(mxtyfkid+j).val();
                        var yskzbz1_vnew = jQuery(yskzbz1+j).val();
                        if(yskzbz1_vnew == '2'){
                            continue;
                        }
                        var mxyg_valnew = jQuery(mxyg+j).val();
                        if(mxtyfkid_valnew == mxtyfkid_val){
                            if(mxyg_valnew.length<1){
                                mxyg_valnew = 0;
                            }
                            sumYg = accAdd(sumYg,mxyg_valnew);
                        }
                    }
                }

            }
            var a = '0';
            var b = '0';
            if(Number(sumYg)>Number(bmky_val)){
                a = '1';
            }
            if(Number(sumYg)>Number(xmky_val)){
                b = '1';
            }
            if(a == '0' && b == '0'){
                jQuery(ysjlms).val('');
                jQuery(ysjlms).val("部门预算内，项目预算内");
            }else if(a == '0' && b== '1'){
                jQuery(ysjlms).val('');
                jQuery(ysjlms).val("部门预算内，项目预算外");
            }else if(a == '1' && b== '0'){
                jQuery(ysjlms).val('');
                jQuery(ysjlms).val("部门预算外，项目预算内");
            }else if(a == '1' && b== '1'){
                jQuery(ysjlms).val('');
                jQuery(ysjlms).val("部门预算外，项目预算外");
            }
            if(gsbm_val=='公司' && xmmc_val !='215'){
                if(Number(sumYg)>Number(xmky_val)){
                    if(xmjl_val != '2'){
                        jQuery(ysjl).val();
                        jQuery(ysjl).empty();
                        jQuery(ysjl).append("<option value='1'>预算外</option>");
                        return ;
                    }

                }
            }else{
                if(Number(sumYg)>Number(bmky_val)){
                    if(bmjl_val != '2'){
                        jQuery(ysjl).val();
                        jQuery(ysjl).empty();
                        jQuery(ysjl).append("<option value='1'>预算外</option>");
                        return ;
                    }
                }
            }

        }
    }
    jQuery(ysjl).val();
    jQuery(ysjl).empty();
    jQuery(ysjl).append("<option value='0'>预算内</option>");

}
function getCcts(){//出差天数
    var ind = jQuery("#indexnum0").val();
    var num = jQuery("#nodesnum0").val();
    var ccts = "#field14204";//出差天数
    var cfrq1 = "#field14209_";//出发日期
    var jsrq1 = "#field14210_";//结束日期
    var fykm1 = "#field14304_";//费用科目--   长途 129 ,123
    var arr1 = new Array(num);
    var arr2 = new Array(num);
    var a=0;
    var b=0;
    var hash1=[];
    var hash2=[];
    for(var i=0;i<ind;i++){
        if(jQuery(fykm1+i).length>0){
            var cfrq1_val = jQuery(cfrq1+i).val();
            if(cfrq1_val != ''){
                hash1.push(new Date(cfrq1_val));
            }
        }
        if(jQuery(fykm1+i).length>0){
            var jsrq1_val = jQuery(jsrq1+i).val();
            if(jsrq1_val != ''){
                hash2.push(new Date(jsrq1_val));
            }
        }

    }
    //alert("hash1---"+hash1+"--hash2--"+hash2);
    //alert("a---"+a+"--b--"+b);
    // getTs(hash1,hash2)
    getTs(hash1,hash2);

}
function getTs(arr1,arr2){
    var ccts = "#field14204";//出差天数
    if(arr1.length<1||arr2.length<1){
        jQuery(ccts).val('0');
        return;
    }
    var ts = 0;
    var max = new Date(Math.max.apply(null,arr2));
    var min = new Date(Math.min.apply(null,arr1));

    //alert("max--"+max+"---min---"+min);
    var date3=max.getTime()-min.getTime(); //时间差秒
    ts=Math.floor(date3/(24*3600*1000))+1;//计算出相差天数
    jQuery(ccts).val(ts);
    jQuery("#field14262").val(dateFtt("yyyy-MM-dd",new Date(Math.min.apply(null,arr1))));//开始日期
    jQuery("#field14262span").text(dateFtt("yyyy-MM-dd",new Date(Math.min.apply(null,arr1))));//开始日期
    jQuery("#field14263").val(dateFtt("yyyy-MM-dd",new Date(Math.max.apply(null,arr2))));//结束日期
    jQuery("#field14263span").text(dateFtt("yyyy-MM-dd",new Date(Math.max.apply(null,arr2))));//结束日期
    //alert("max--"+max+"---min---"+min);

}
function checkIFcb(){//判断是否超
    var ind = jQuery("#indexnum0").val();
    var bzed = "#field14317_";//标准
    var ccts_val = jQuery("#field14204").val();
    var fykm1 = "#field14304_";//费用科目--
//alert(1);
    var a = 1;
    var st = "";
    for(var i = 0;i<ind;i++){
        if(jQuery(fykm1+i).length>0){
            var fykm1_val = jQuery(fykm1+i).val();
            if(fykm1_val=='132' ||fykm1_val=='125' ){
                var bzed_val = jQuery(bzed+i).val();//
                var ygje_dt1_val = jQuery("#field14226_"+i).val();//
                var ts = ccts_val-1;
                var bz_ = accMul(bzed_val,ts);
                if(Number(ygje_dt1_val)>Number(bz_)){
                    if(st.length<1){
                        st = st+a;
                    }else{
                        st = st+","+a;
                    }
                }
            }
            a++;
        }
    }
    return st;


}
function accMul(arg1,arg2){
    var m=0,s1=arg1.toString(),s2=arg2.toString();
    try{m+=s1.split(".")[1].length}catch(e){}
    try{m+=s2.split(".")[1].length}catch(e){}
    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
}
function checkBtIFcb(){//判断补贴是否超
    var res = '0';
    var ind = jQuery("#indexnum0").val();
    var bzed = "#field14317_";//标准
    var ccts_val = jQuery("#field14204").val();
    var fykm1 = "#field14304_";//费用科目--
    var a = 1;
    var st = "";
    for(var i = 0;i<ind;i++){
        if(jQuery(fykm1+i).length>0){
            var fykm1_val = jQuery(fykm1+i).val();
            if( fykm1_val=='134' ||fykm1_val=='127' ){
                var bzed_val = jQuery(bzed+i).val();//
                var ygje_dt1_val = jQuery("#field14226_"+i).val();//
                var ts = ccts_val;
                var bz_ = accMul(bzed_val,ts);
                if(Number(ygje_dt1_val)>Number(bz_)){
                    if(st.length<1){
                        st = st+a;
                    }else{
                        st = st+","+a;
                    }
                }
            }
            a++;
        }
    }
    if(st.length>0){
        Dialog.alert("第"+st+"行，补贴预申请金额大于出差天数*出差补贴标准");
        res = '1';
    }
    return res;
}
function checkRQ(_dt){
    var cfrq_v = _C.v("field14209"+_dt);
    var jsrq_v = _C.v("field14210"+_dt);
    if(cfrq_v.length>0 && jsrq_v.length>0){
        if(cfrq_v>jsrq_v){
            Dialog.alert("结束日期必须不晚于开始日期");
            _C.v("field14210"+_dt , '');
        }
    }
}
function dateFtt(fmt,date){ //author: meizz
    var o = {
        "M+" : date.getMonth()+1,     //月份
        "d+" : date.getDate(),     //日
        "h+" : date.getHours(),     //小时
        "m+" : date.getMinutes(),     //分
        "s+" : date.getSeconds(),     //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S" : date.getMilliseconds()    //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

function addDetailRow(){
    var reqVal = jQuery("input[name=requestid]").val();
    if (reqVal === '-1' ){
        $.ajax({
            async: false,
            type : "POST",
            url : "/morningcore/finance/getFykmInfo.jsp",
            dataType : "text",
            success : function(data){
                data= data.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
                var indexnum0 = jQuery("#indexnum0").val();
                var text_arr = data.split("@@@");
                var length=Number(text_arr.length)-1+Number(indexnum0);
                for(var i=indexnum0;i<length;i++){
                    addRow0(0);
                    var tmp_arr = text_arr[i-indexnum0].split("###");
                    jQuery(km_dt1+i).val(tmp_arr[0]);
                    jQuery(km_dt1+i+"span").html("<span><a href = '/formmode/view/AddFormMode.jsp?type=0&modeId=1030&formId=-105&billid="
                        +tmp_arr[0]+"' target='_blank'>"+tmp_arr[1]+"</a></span>");
                    jQuery(km_dt1+i+"spanimg").html("");
                    setLDZ(i);
                }
            }
        });
    }else{

    }
}
</script>













