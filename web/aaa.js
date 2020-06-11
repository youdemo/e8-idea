<script type="text/javascript" src="/morningcore/js/cw.js"></script>
    <script type="text/javascript">
var fphm = '#field14540_'; //发票号码
var fplx = '#field14539_'; //发票类型
var glccsq = '#field14594'; //关联出差申请
var ygje_dt1 = '#field14541_'; //报销金额
var ysjl = '#field14606'; //预算结论
var mxjlhz = '#field14560_'; //明细表结论汇总
var bmjebj_dt1 = '#field14599_'; //部门金额比较
var bmysjl_dt1 = '#field14608_'; //部门预算结论
var xmjebj_dt1 = '#field14600_'; //项目金额比较
var xmysjl_dt1 = '#field14607_'; //项目预算结论
var gsbm = '#field14577'; //公司/部门
var xmmc = "#field14573"; //项目名称
var km_dt1 = "#field14598_"; //科目 明细一
var hbbh_dt1 = "#field14583_"; //合并编号 明细一
var fycdbm = "#field14574"; //费用承担部门
var ysjlms = "#field15733"; //预算结论描述
var yskzbz_dt1 = "#field14582_"; //预算控制标准 明细一
var yskzfsDT1 = "#field14580_"; //预算控制方式 明细一
var isDelFlag = "#field14620_"; //是否带出的
var id_ysmk = "#field14612_"; // 预算科目
var id_ygje = "#field14538_"; //  预估金额
var id_bxje = "#field14541_"; //报销金额
var id_tyfkbs = "#field14617_"; // 统一费控标识
var id_bs = "#field14618_"; //  标识
var id_main_bs = "#field14571";
var id_fpsycs = "#field14552_"; // 发票使用次数
//  部门预算金额	项目预算金额	部门冻结金额	部门已发生金额	部门剩余金额	项目冻结金额	项目已发生金额	项目剩余金额
var id_01 = "#field14586_";
var id_02 = "#field14588_";
var id_03 = "#field14587_";
var id_04 = "#field14589_";
var id_05 = "#field14592_";
var id_06 = "#field14590_";
var id_07 = "#field14591_";
var id_08 = "#field14593_";
//  开始日期  达到日期 出发地点  达到地点
var idx_ksrq = "#field14523_";
var idx_jsrq = "#field14524_";
var idx_cfdd = "#field14658_";
var idx_dddd = "#field14659_";
var idx_jtgj = "#field14537_";
var idx_jtgjdis = "#disfield14537_";

var sy = "#field14517";//事由

//2019-08-21 整体调整
jQuery(document).ready(function() {
    alert("123");
    //关联出差申请
    jQuery(glccsq).bindPropertyChange(function() {
        checkDtFor();
        setTimeout('forxh()', '500');
        setBXJE_v1();
    })



    jQuery(sy).change(function(){
        getCCSQ();
    })

    //明细表一
    var nowIndex = document.getElementById("indexnum0").value * 1.0 - 1;
    jQuery("button[name='addbutton0']").bind("click", function() {
        bindDetail1(1);
        var indexnum0ff = jQuery("#indexnum0").val();
        bindOtherFiled(indexnum0ff - 1);
        dealrqzd(indexnum0ff - 1);
    });


    setTimeout('checkDtDeal()', '700');
    setTimeout('setJTGJready()', '700');

    //提交校验
    checkCustomize = function() {
        var result = 0;
        var cclx = "#field14528"; //境内外  0内 1外
        var cclx_val = jQuery(cclx).val();
        if (cclx_val == '0') {
            var sfcb = checkIFcb();
            if (sfcb.length > 0) {
                var mymessage = confirm("第" + sfcb + "行，住宿预申请金额大于出差天数*出差住宿标准");
                if (!mymessage) {
                    return false;
                }
            }

        }
        var re = checkFplx();
        if (re == '1') {
            return false;
        }
        for (var i = 0; i < jQuery('#indexnum0').val(); i++) {
            if (jQuery(fphm + i).length > 0) {
                var fphm_v = jQuery(fphm + i).val();
                var fplx_v = jQuery(fplx + i).val();
                var requestid = jQuery("input[name='requestid']").val();
                var xhr = null;
                if (result > 0) {
                    return false;
                    break;
                }
                var check = checkfphm(fphm_v, i, fphm);
                if (check == '1') {
                    Dialog.alert("发票号码不能重复，请检查后填写！");
                    return false;
                    break;
                }
            }
            //交通工具去除只读
            if(jQuery(isDelFlag + i).val() == '1'){
                jQuery(idx_jtgj + i).removeAttr("disabled");
            }

        }



        return true;
    }
})
function dealrqzd(){
    var indexnum0 = jQuery("#indexnum0").val();
    for (var index = 0; index < indexnum0; index++) {
        if(jQuery(km_dt1+index).length>0){
            var km_dt1_val = jQuery(km_dt1+index).val();
            if(km_dt1_val == '131' || km_dt1_val == '132' || km_dt1_val == '134'){
                setzd1(index);
            }
            bindkmchange(index);
        }
    }
}
function bindkmchange(index){
    jQuery(km_dt1+index).bindPropertyChange(function(){
        var km_dt1_val = jQuery(km_dt1+index).val();
        if(km_dt1_val == '131' || km_dt1_val == '132' || km_dt1_val == '134'){
            setzd1(index);
        }else{
            removezd1(index);
        }
    })
}
function setzd1(index){
    jQuery(idx_ksrq+index+"browser").attr("disabled", "disabled");
    jQuery(idx_ksrq+index).val("");
    jQuery(idx_ksrq+index+"span").html("");
    jQuery(idx_jsrq+index+"browser").attr("disabled", "disabled");
    jQuery(idx_jsrq+index).val("");
    jQuery(idx_jsrq+index+"span").html("");
    jQuery(idx_cfdd+index+"_browserbtn").attr("disabled", "disabled");
    jQuery(idx_cfdd+index).val("");
    jQuery(idx_cfdd+index+"span").html("");
    jQuery(idx_dddd+index+"_browserbtn").attr("disabled", "disabled");
    jQuery(idx_dddd+index).val("");
    jQuery(idx_dddd+index+"span").html("");
    jQuery(idx_jtgj+index).val("");
    jQuery(idx_jtgjdis+index).val("");
    jQuery(idx_jtgj+index).attr("disabled", "disabled");


}

function removezd1(index){
    jQuery(idx_ksrq+index+"browser").removeAttr("disabled");
    jQuery(idx_jsrq+index+"browser").removeAttr("disabled");
    jQuery(idx_cfdd+index+"_browserbtn").removeAttr("disabled");
    jQuery(idx_dddd+index+"_browserbtn").removeAttr("disabled");
    jQuery(idx_jtgj+index).removeAttr("disabled");

    function checkDtDeal() {
        //  check框 td中一个隐藏的标识字段  dtl_id_0_
        var checkBro = 'input[name="dtl_id_0_';
        var checkBroF = '"]'
        var ind = jQuery("#indexnum0").val();
        for (var i = 0; i < ind; i++) {
            if (jQuery(isDelFlag + i).length > 0) {
                // 增加一个绑定事件方法
                bindOtherFiled(i);
                var isDelVal = jQuery(isDelFlag + i).val();
                if (isDelVal == '1') { // check框需要无效或删除
                    // 获取check 同一级的去查找父类
                    var t_name = checkBro + i + checkBroF;
                    var td_val = jQuery(t_name).parent().find(':checkbox');
                    // 将其内容清空
                    //   alert( "1 - >" + td_val.html());
                    td_val.attr("checked", false).attr('disabled', true);
                    //  以下内容修改成只读
                    jQuery(km_dt1 + i + "_browserbtn").attr('disabled', true);
                    jQuery(id_tyfkbs + i).attr("readonly", "readonly");
                    jQuery(id_bs + i).attr("readonly", "readonly");
                    jQuery(id_fpsycs + i).attr("readonly", "readonly");
                    jQuery(mxjlhz + i).attr("readonly", "readonly");
                    jQuery(id_01 + i).attr("readonly", "readonly");
                    jQuery(id_02 + i).attr("readonly", "readonly");
                    jQuery(id_03 + i).attr("readonly", "readonly");
                    jQuery(id_04 + i).attr("readonly", "readonly");
                    jQuery(id_05 + i).attr("readonly", "readonly");
                    jQuery(id_06 + i).attr("readonly", "readonly");
                    jQuery(id_07 + i).attr("readonly", "readonly");
                    jQuery(id_08 + i).attr("readonly", "readonly");
                }
            }
        }
    }

    function setJTGJready() {
        var checkBro = 'input[name="dtl_id_0_';
        var checkBroF = '"]'
        var ind = jQuery("#indexnum0").val();
        for (var i = 0; i < ind; i++) {
            if (jQuery(isDelFlag + i).length > 0) {
                bindOtherFiled(i);
                var isDelVal = jQuery(isDelFlag + i).val();
                if (isDelVal == '1') {
                    jQuery(idx_jtgj + i).attr("disabled", "disabled");
                }
            }
        }
    }

    function bindOtherFiled(index) {
        jQuery(id_bs + index).bindPropertyChange(function() {
            otherCal(); // 重新计算所有的
        })
    }



    function otherCal() {
        var ind = jQuery("#indexnum0").val();
        var tmp_all = 0;
        for (var i = 0; i < ind; i++) {
            if (jQuery(id_bs + i).length > 0) {
                var tmp_valx = jQuery(id_bs + i).val();
                tmp_all = Number(tmp_all) + Number(tmp_valx);
            }
        }
        jQuery(id_main_bs).val(tmp_all);
    }



    function checkDtFor() {
        var nodesnum0 = jQuery("#nodesnum0").val();
        if (nodesnum0 > 0) {
            jQuery("[name = check_node_0]:checkbox").attr('disabled', false).attr("checked", true);
            adddelid(0,"#field14598_");//随意找个明细id
            removeRow0(0);
        }
        var xzht_val = jQuery(glccsq).val();
        $.ajax({
            type: "POST",
            url: "/morningcore/finance/gethtdetail.jsp",
            data: {
                'hth': xzht_val
            },
            dataType: "text",
            async: false, //同步 true异步
            success: function(data) {
                data = data.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
                var text_arr = data.split("@@@");
                var ind = jQuery("#indexnum0").val();
                var indexnum1 = jQuery("#indexnum0").val();
                var length1 = Number(text_arr.length) - 1 + Number(indexnum1);
                for (var i = indexnum1; i < length1; i++) {
                    addRow0(0);
                    var tmp_arr = text_arr[i - indexnum1].split("###");
                    jQuery(km_dt1 + i).val(tmp_arr[0]);
                    jQuery(km_dt1 + i + "span").text(tmp_arr[2]);
                    jQuery(km_dt1 + i + "spanimg").html("");
                    datainputd('field14598_' + i);
                    jQuery(id_ygje + i).val(tmp_arr[1]);
                    jQuery(id_ygje + i + "span").text(tmp_arr[1]);
                    jQuery(id_bxje + i).val(tmp_arr[1]);
                    jQuery(id_bxje + i + "span").html("");
                    jQuery(idx_ksrq + i).val(tmp_arr[3]);
                    jQuery(idx_ksrq + i + "span").text(tmp_arr[3]);
                    jQuery(idx_jsrq + i).val(tmp_arr[4]);
                    jQuery(idx_jsrq + i + "span").text(tmp_arr[4]);
                    jQuery(idx_cfdd + i).val(tmp_arr[5]);
                    jQuery(idx_cfdd + i + "wrapspan").text(tmp_arr[5]);
                    jQuery(idx_dddd + i).val(tmp_arr[6]);
                    jQuery(idx_dddd + i + "wrapspan").text(tmp_arr[6]);
                    jQuery(isDelFlag + i).val("1");
                    jQuery("#field14577").val(tmp_arr[7]);
                    jQuery("#field14661_" + i).val(tmp_arr[8]);
                    jQuery("#field14661_" + i + "span").text(tmp_arr[8]);
                    jQuery("#field14537_" + i).append(setJTGJ(i,tmp_arr[9]))
                    //jQuery("#field14537_" + i).val(tmp_arr[9]);

                    jQuery(fplx+i).val(tmp_arr[10]);

                    setTimeout("setFPLX_V2('"+i+"')",'1000');//设置发票类型  之前的逻辑没看懂 新添加  glt
                }
            }
        });
        checkDtDeal();
    }

    //交通工具
    function setJTGJ(index,value){
        jQuery(idx_jtgj+index).empty();
        if(value === '0'){
            return "<option value='0'>飞机</option>";
        }else if (value === '1'){
            return "<option value='1'>火车</option>";
        }else if (value === '2'){
            return "<option value='2'>长途汽车</option>";
        }else if(value === '3'){
            return "<option value='3'>轮船</option>";
        }else if(value === '0'){
            return "<option value='4'>其他</option>";
        }else{
            return "";
        }

    }

    //设置发票类型
    function setFPLX_V2(index){
        if(jQuery(km_dt1+index).val() === '129' ||jQuery(km_dt1+index).val() === '131'){
            jQuery(fplx+index).val("1");
        }else if(jQuery(km_dt1+index).val() === '132'){
            jQuery(fplx+index).val("0");
        }
    }

    function adddelid(dtid,fieldidqf){
        var ids_dt1="";
        var flag1="";
        var indexnum0=  jQuery("#indexnum"+dtid).val();
        for(var index=0;index <indexnum0;index++){
            if(jQuery(fieldidqf+index).length>0){
                var pp=document.getElementsByName('dtl_id_'+dtid+'_'+index)[0].value;
                ids_dt1=ids_dt1+flag1+pp;
                flag1=",";
            }
        }
        var deldtlid0_val=jQuery("#deldtlid"+dtid).val();
        if(deldtlid0_val !=""){
            deldtlid0_val=deldtlid0_val+","+ids_dt1;
        }else{
            deldtlid0_val=ids_dt1;
        }
        jQuery("#deldtlid"+dtid).val(deldtlid0_val);
    }

    function bindDetail1(value) {
        var nowIndex = document.getElementById("indexnum0").value * 1.0 - 1;
        if (nowIndex >= 0) {
            if (value == 1) { //当前添加的行
                changeDtJL(nowIndex);
            }
            if (value == 2) { //初始化
                jQuery("input[name='check_node_0']").each(function(i) {
                    var nowIndex = this.value;
                    changeDtJL(nowIndex);
                });
            }
        }
    }

    function changeDtJL(j) {
        jQuery(km_dt1 + j).bindPropertyChange(function() {
            var hbbh = jQuery(hbbh_dt1 + j).val().toString();
            jQuery(hbbh_dt1 + j).val("");
            setTimeout('jQuery(hbbh_dt1+j).val(' + hbbh.toString() + ')', '1000');
        })
        jQuery(yskzbz_dt1 + j).bindPropertyChange(function() {
            changeDt1JL(j);
        })
        jQuery(ygje_dt1 + j).bindPropertyChange(function() {
            changeDt1JL(j);
        })
    }

    function forxh() {
        for (var i = 0; i < jQuery('#indexnum0').val(); i++) {
            if (jQuery(ygje_dt1 + i).length > 0) {
                changeDtJL(i);
            }
        }
    }

    function checkfphm(fphm_v, index, fphm) {
        if (fphm_v.length < 1) {
            return '0';
        }
        var indexnum0 = jQuery("#indexnum0").val();
        for (var j = index + 1; j < indexnum0; j++) {
            if (jQuery(fphm + j).length > 0) {
                var fphm_val = jQuery(fphm + j).val();
                if (fphm_v == fphm_val) {
                    return '1';
                }
            }
        }
        return '0';
    }



    function accAdd(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        return (arg1 * m + arg2 * m) / m
    }

    function sethbbh(hbbh, j) {
        jQuery(hbbh_dt1 + j).val(hbbh);
    }


    function removeRow0(groupid, isfromsap) {
        try {
            var flag = false;
            var ids = document.getElementsByName("check_node_" + groupid);
            for (i = 0; i < ids.length; i++) {
                if (ids[i].checked == true) {
                    flag = true;
                    break;
                }
            }
            if (isfromsap) {
                flag = true;
            }
            if (flag) {
                if (isfromsap || _isdel()) {
                    var oTable = document.getElementById("oTable" + groupid)
                    var len = document.forms[0].elements.length;
                    var curindex = parseInt($G("nodesnum" + groupid).value);
                    var i = 0;
                    var rowsum1 = 0;
                    var objname = "check_node_" + groupid;
                    for (i = len - 1; i >= 0; i--) {
                        if (document.forms[0].elements[i].name == objname) {
                            rowsum1 += 1;
                        }
                    }
                    rowsum1 = rowsum1 + 2;
                    for (i = len - 1; i >= 0; i--) {
                        if (document.forms[0].elements[i].name == objname) {
                            if (document.forms[0].elements[i].checked == true) {
                                var nodecheckObj;
                                var delid;
                                try {
                                    if (jQuery(oTable.rows[rowsum1].cells[0]).find("[name='" + objname + "']").length > 0) {
                                        delid = jQuery(oTable.rows[rowsum1].cells[0]).find("[name='" + objname + "']").eq(0).val();
                                    }
                                } catch (e) {}
                                //记录被删除的旧记录 id串
                                if (jQuery(oTable.rows[rowsum1].cells[0]).children().length > 0 && jQuery(jQuery(oTable.rows[rowsum1].cells[0]).children()[0]).children().length > 1) {
                                    if ($G("deldtlid" + groupid).value != '') {
                                        //老明细
                                        $G("deldtlid" + groupid).value += "," + jQuery(oTable.rows[rowsum1].cells[0].children[0]).children()[1].value;
                                    } else {
                                        //新明细
                                        $G("deldtlid" + groupid).value = jQuery(oTable.rows[rowsum1].cells[0]).children().eq(0).children()[1].value;
                                    }
                                }
                                //从提交序号串中删除被删除的行
                                var submitdtlidArray = $G("submitdtlid" + groupid).value.split(',');
                                $G("submitdtlid" + groupid).value = "";
                                var k;
                                for (k = 0; k < submitdtlidArray.length; k++) {
                                    if (submitdtlidArray[k] != delid) {
                                        if ($G("submitdtlid" + groupid).value == '') {
                                            $G("submitdtlid" + groupid).value = submitdtlidArray[k];
                                        } else {
                                            $G("submitdtlid" + groupid).value += "," + submitdtlidArray[k];
                                        }
                                    }
                                }
                                oTable.deleteRow(rowsum1);
                                curindex--;
                            }
                            rowsum1--;
                        }
                    }
                    $G("nodesnum" + groupid).value = curindex;
                    calSum(groupid);
                }
            } else {
                alert('请选择需要删除的数据');
                return;
            }
        } catch (e) {}
        try {
            var indexNum = jQuery("span[name='detailIndexSpan0']").length;
            for (var k = 1; k <= indexNum; k++) {
                jQuery("span[name='detailIndexSpan0']").get(k - 1).innerHTML = k;
            }
        } catch (e) {}
    }

    function _isdel() {
        return true;
    }
    //20190811
    jQuery(document).ready(function() {
        var fykm = "field14598";
        var cfrq = "field14523"; //出发日期
        var jsrq = "field14524"; //结束日期
        var jtgj = "field14537"; //交通工具
        var sfdc = "field14620"; //是否流程带出的数据
        var fplxdt = "field14539"; //发票类型
        var cfdd = "field14658"; //出发地点
        var dddd = "field14659"; //到达地点
        jQuery("#field14518").attr('readonly', 'readonly'); //天数
        _C.run2(fykm, function(p) {
            if (_C.v(sfdc + p.r) != '1') {
                if (p.v.n == '') {
                    _C.v(cfrq + p.r, ''); //赋值
                    _C.rs(cfrq + p.r, false); //去除必填
                    _C.v(jsrq + p.r, ''); //
                    _C.rs(jsrq + p.r, false);
                    _C.v(jtgj + p.r, ''); //
                    _C.rs(jtgj + p.r, false);
                    jQuery("#field14523" + p.r + "browser").attr("disabled", "disabled");
                    jQuery("#field14524" + p.r + "browser").attr("disabled", "disabled");
                    jQuery("#" + jtgj + p.r).attr("disabled", "disabled");
                    _C.v(fplxdt + p.r, ''); //
                    _C.rs(fplxdt + p.r, true); //必填
                } else {
                    var fykm_val = _C.v(fykm + p.r);
                    if (fykm_val == '129' || fykm_val == '123') {
                        if (_C.v(cfrq + p.r) == '') {
                            jQuery("#field14523" + p.r + "browser").removeAttr('disabled');
                            _C.rs(cfrq + p.r, true); //必填
                        }
                        if (_C.v(jsrq + p.r) == '') {
                            jQuery("#field14524" + p.r + "browser").removeAttr('disabled');
                            _C.rs(jsrq + p.r, true); //必填
                        }
                        if (_C.v(jtgj + p.r) == '') {
                            jQuery("#" + jtgj + p.r).removeAttr('disabled');
                            _C.rs(jtgj + p.r, true); //必填
                        }
                    } else {
                        _C.v(cfrq + p.r, ''); //赋值
                        _C.rs(cfrq + p.r, false); //去除必填
                        _C.v(jsrq + p.r, ''); //赋值
                        _C.rs(jsrq + p.r, false); //去除必填
                        _C.v(jtgj + p.r, ''); //
                        _C.rs(jtgj + p.r, false);
                        jQuery("#field14523" + p.r + "browser").attr("disabled", "disabled");
                        jQuery("#field14524" + p.r + "browser").attr("disabled", "disabled");
                        jQuery("#" + jtgj + p.r).attr("disabled", "disabled");
                    }
                    if (fykm_val == '127' || fykm_val == '134') {
                        _C.v(fplxdt + p.r, ''); //
                        _C.rs(fplxdt + p.r, false); //
                        jQuery("#" + fplxdt + p.r).attr("disabled", "disabled");
                    } else {
                        if (_C.v(fplxdt + p.r) == '') {
                            jQuery("#" + fplxdt + p.r).removeAttr("disabled");
                            _C.rs(fplxdt + p.r, true); //必填
                        }
                    }
                    //出发地点  到达地点 校验
                    if (fykm_val === '129') {
                        _C.rs(cfdd + p.r, true); //必填
                        _C.rs(dddd + p.r, true); //必填
                    } else if (fykm_val === '132') {
                        _C.rs(cfdd + p.r, false); //必填
                        _C.rs(dddd + p.r, true); //必填
                    } else {
                        _C.rs(cfdd + p.r, false); //必填
                        _C.rs(dddd + p.r, false); //必填
                    }
                }
            }
        })
        _C.run2(cfrq, function(p) {
            checkRQ(p.r);
            getCcts();
        })
        _C.run2(jsrq, function(p) {
            checkRQ(p.r);
            getCcts();
        })
        _C.run2(fplxdt, function(p) {
            if (_C.v(fplxdt + p.r) != '') {
                jQuery("#" + fplxdt + p.r + "span").html('');
            }
            if (_C.v(fplxdt + p.r) == '2') {
                if (_C.v("field14540" + p.r) == '') {
                    _C.rs("field14540" + p.r, true);
                }
            } else {
                _C.rs("field14540" + p.r, false);
            }
            if (_C.v(fplxdt + p.r) == '0') {
                if (_C.v("field14601" + p.r) == '') {
                    _C.rs("field14601" + p.r, true);
                }
            } else {
                _C.rs("field14601" + p.r, false);
            }
        })
        _C.run2("field14540", function(p) {
            if (_C.v("field14540" + p.r) != '') {
                jQuery("#field14540" + p.r + "span").html('');
            }
        })
    }) //20190811
    function getCcts() { //出差天数
        var ind = jQuery("#indexnum0").val();
        var num = jQuery("#nodesnum0").val();
        var cfrq1 = "#field14523_"; //出发日期
        var jsrq1 = "#field14524_"; //结束日期
        var fykm1 = "#field14598_"; //费用科目--   长途 129 ,123
        var hash1 = [];
        var hash2 = [];
        for (var i = 0; i < ind; i++) {
            if (jQuery(fykm1 + i).length > 0) {
                var cfrq1_val = jQuery(cfrq1 + i).val();
                if (cfrq1_val != '') {
                    hash1.push(new Date(cfrq1_val));
                }
            }
            if (jQuery(fykm1 + i).length > 0) {
                var jsrq1_val = jQuery(jsrq1 + i).val();
                if (jsrq1_val != '') {
                    hash2.push(new Date(jsrq1_val));
                }
            }
        }
        // alert("hash1---"+hash1+"--hash2--"+hash2);
        //alert("a---"+a+"--b--"+b);
        // getTs(hash1,hash2)
        getTs(hash1, hash2);
    }

    function getTs(arr1, arr2) {
        var ccts = "#field14518"; //出差天数
        if (arr1.length < 1 || arr2.length < 1) {
            jQuery(ccts).val('0');
            return;
        }
        var ts = 0;
        var max = new Date(Math.max.apply(null, arr2));
        var min = new Date(Math.min.apply(null, arr1));
        // alert("max--"+max+"---min---"+min);
        var date3 = max.getTime() - min.getTime(); //时间差秒
        ts = Math.floor(date3 / (24 * 3600 * 1000)) + 1; //计算出相差天数
        jQuery(ccts).val(ts);
    }

    function checkRQ(_dt) {
        var cfrq_v = _C.v("field14523" + _dt);
        var jsrq_v = _C.v("field14524" + _dt);
        if (cfrq_v.length > 0 && jsrq_v.length > 0) {
            if (cfrq_v > jsrq_v) {
                Dialog.alert("结束日期必须不晚于开始日期");
                _C.v("field14524" + _dt, '');
            }
        }
    }

    function checkIFcb() { //判断是否超
        var ind = jQuery("#indexnum0").val();
        var bzed = "#field14661_"; //标准
        var ccts_val = jQuery("#field14518").val();
        var fykm1 = "#field14598_"; //费用科目--
        //alert(1);
        var a = 1;
        var st = "";
        for (var i = 0; i < ind; i++) {
            if (jQuery(fykm1 + i).length > 0) {
                var fykm1_val = jQuery(fykm1 + i).val();
                if (fykm1_val == '132' || fykm1_val == '125') {
                    var bzed_val = jQuery(bzed + i).val(); //
                    var ygje_dt1_val = jQuery("#field14541_" + i).val(); //
                    var ts = ccts_val - 1;
                    var bz_ = accMul(bzed_val, ts);
                    if (Number(ygje_dt1_val) > Number(bz_)) {
                        if (st.length < 1) {
                            st = st + a;
                        } else {
                            st = st + "," + a;
                        }
                    }
                }
                a++;
            }
        }
        return st;
    }

    function accMul(arg1, arg2) {
        var m = 0,
            s1 = arg1.toString(),
            s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {}
        try {
            m += s2.split(".")[1].length
        } catch (e) {}
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }



    function checkFplx() { //判断发票类型是否填写
        var km = "#field14598_"; //费用科目
        var fplxd1 = "#field14539_"; //发票类型
        var res = '0';
        var a = 1;
        var ind = jQuery("#indexnum0").val();
        for (var i = 0; i < ind; i++) {
            if (jQuery(km + i).length > 0) {
                var fykm1_v = jQuery(km + i).val(); //费用科目
                var fplx1_v = jQuery(fplxd1 + i).val(); //发票类型
                if (fykm1_v != '134' && fykm1_v != '127') {
                    if (fplx1_v == '' || fplx1_v == 'undefined') {
                        res = '1';
                        Dialog.alert("第" + a + "行，发票类型未填写，请确认");
                        break;
                    }
                }
                a++;
            }
        }
        return res;
    }
</script>














