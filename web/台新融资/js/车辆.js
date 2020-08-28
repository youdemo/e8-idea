<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript" src="/txrz/js/cw.js"></script>
    <script type="text/javascript">
var ftlx = '#field6524'// 分摊类型
var dqxz = '#field6525'// 地区选择
var fthzje = '#field6527'// 分摊汇总金额
var wssezh = '#field6792'// 未税税额总和
var bmmc_dt3 = '#field6538_'// 明细 3部门名称
var bl_dt3 = '#field6539_'// 明细3 比例
var bmdm_dt3 = '#field6540_'// 明细3   部门代码
var ftje_dt3 = '#field6541_'// 明细3

var sezh = '#field6793'// 税额总和
var wshz = '#field6754'// 未税汇总
var sfyfp = '#field6521'// 是否有发票  0 是     1 否
var ybje = '#field6517'// 应补金额
var ytje = '#field6518'// 应退金额
var je = '#field6510'// 金额
var zjkje = '#field6636'// 暂借款金额
var fylxDT4 = '#field7053_'// 费用类型 明细四

jQuery(document).ready(function () {
    _C.run2('nodesnum3', function (p) {
        // doDetail(t4.fylx, 2)
        if (p.v.o !== undefined) {
            if (Number(p.v.n) < Number(p.v.o)) {
                if (p.v.n === '0') {
                    _C.deleteRow(2)
                } else {
                    doDetailIndex2()
                }
            }
        }
    })

    _C.run2('nodesnum0', function (p) {
        // doDetail(t4.fylx, 2)
        if (p.v.o !== undefined) {
            if (Number(p.v.n) < Number(p.v.o)) {
                doDetailIndex2()
            }
        }
    })

    _C.run2(t4.fylx, function (p) {
        if (p.v.o !== undefined) {
            // doDetail(p.k, 2)
            var feeType = p.v.o
            if (feeType !== '') {
                // 遍历差旅费明细表，删除行序号相同的明细行
                var delArr = []
                _C.stEach(t3.fylx, function (r) {
                    // console.log('linenum2=' + _C.v(t2.linenum + r))
                    if (_C.v(t3.fylx + r) === feeType) {
                        if (r.indexOf('__') === -1) {
                            delArr.push(r)
                        }
                    }
                })
                // console.log(delArr)
                delRow(delArr, 2)
            }
            getDetail3(p.r)
        }
    })

    _C.run2(t4.ftxs, function (p) {
        if (p.v.o !== undefined) {
            doDetail2(t4.fylx, p.r)
            getDetail3(p.r)
        }
    })

    _C.run2(t4.dqxz, function (p) {
        if (p.v.o !== undefined) {
            doDetail2(t4.fylx, p.r)
            getDetail3(p.r)
        }
    })

    _C.run2(t4.ftlx, function (p) {
        if (p.v.o !== undefined) {
            doDetail2(t4.fylx, p.r)
            getDetail3(p.r)
        }
    })

    _C.run2(t4.qkje, function (p) {
        if (p.v.o === undefined) {
            jQuery('#' + p.k + p.r).blur(function () {
                doDetail2(t4.fylx, p.r)
                getDetail3(p.r)
            })
        }
    })

    _C.run2(t1.fylx, function (p) {
        if (p.v.o !== undefined) {
            if (_C.v(t1.wsje + p.r) !== '') {
                // doDetail(p.k, 2)
                doDetail1(t1.fylx, p.r)
            }
        }
    })

    _C.run2(t1.wsje, function (p) {
        if (p.v.o === undefined) {
            jQuery('#' + p.k + p.r).blur(function () {
                if (_C.v(t1.wsje + p.r) !== '') {
                    // doDetail(p.k, 2)
                    doDetail1(t1.fylx, p.r)
                }
            })
        }
    })
    setTimeout("CtroBt()",500);

    _C.run2('field7053',function(p){
        if (p.v.o !== undefined) {
            CtroBt();
        }

        setFJ();
    })
    showhide()
    var ftlx_valnew = jQuery(ftlx).val()
    var ftlx_valnew1 = jQuery(ftlx).val()
    var ftlx_valnew2 = jQuery(ftlx).val()
    var ftlx_valnew2 = jQuery(ftlx).val()
    var dqxz_valnew = jQuery(dqxz).val()
    jQuery(ybje).attr('readonly', 'readonly')
    jQuery(ytje).attr('readonly', 'readonly')
    jQuery('#field6847').bindPropertyChange(function () {
        jQuery(ftlx).val('')
        ftlx_valnew3 = '00'
        deldt3()
    })
    var fylx = '#field6676'// 费用类型
    var fq1 = '#field7040'// 房1  使用期间
    var fq2 = '#field7041'// 房2  使用期间
    jQuery(fq1 + 'browser').attr('disabled', 'disabled')
    jQuery(fq2 + 'browser').attr('disabled', 'disabled')

    jQuery('#nodesnum3').bindPropertyChange(function () {
        CtroBt();
        setFJ();
    })

    // 设置未税金额
    jQuery('#nodesnum0').bindPropertyChange(function () {
        setdetail0()
    })

    //
    jQuery('button[name=addbutton2]').live('click', function () {
        var ftlx_val = jQuery(ftlx).val()
        if (ftlx_val == '2') {
            var ind = jQuery('#indexnum2').val()
            for (var i = 0; i < ind; i++) {
                if (jQuery(bl_dt3 + i).length > 0) {
                    jQuery(bl_dt3 + i).attr('disabled', 'disabled')
                }
            }
        }
    })
    jQuery(wshz).bindPropertyChange(function () {
        setmoney()
        getYtYb()
    })
    jQuery(sfyfp).bindPropertyChange(function () {
        getYtYb()
        setmoney()
    })
    jQuery(sezh).bindPropertyChange(function () {
        getYtYb()
    })
    jQuery(zjkje).bindPropertyChange(function () {
        getYtYb()
    })
    jQuery(je).bindPropertyChange(function () {
        setmoney()
        getYtYb()
    })

    _C.run2('field6752', function (p) {
        var sfyby_val = _C.v('field6752')// 是否使用备用金
        if (sfyby_val == '0') { // 0 是  1 否
            getBYYE()
            _C.v('field6626', '', '')//
            _C.deleteRow(1)
            jQuery('#mx1').hide()
        } else {
            _C.v('field6753', '', '')// 备用金余额
            _C.v('field6854', '', '')// 备用金库
            jQuery('#mx1').show()
        }
    })
    _C.run2('field6854', function (p) {
        var sfyby_val = _C.v('field6752')// 是否使用备用金
        var byjk_val = _C.v('field6854')// 备用金库
        if (byjk_val == '' || byjk_val.length < 1) {
            _C.v('field6753', '', '')// 备用金余额
            return
        }
        if (sfyby_val == '0') { // 0 是  1 否
            getBYYE()
        } else {
            _C.v('field6753', '', '')// 备用金余额
            _C.v('field6854', '', '')// 备用金库
        }
    })
    checkCustomize = function () {
        var fplxd = "#field6528_";//发票类型
        var n = 0;
        var bt = "#requestname";
        var bt_val = jQuery(bt).val();
        if(	bt_val.indexOf("~") != -1 || bt_val.indexOf("!") != -1 || bt_val.indexOf("@") != -1
            || bt_val.indexOf("#") != -1 || bt_val.indexOf("$") != -1 || bt_val.indexOf("%") != -1
            || bt_val.indexOf("^") != -1 || bt_val.indexOf("&") != -1 || bt_val.indexOf("*") != -1
            || bt_val.indexOf("(") != -1 || bt_val.indexOf(")") != -1 || bt_val.indexOf("+") != -1
            || bt_val.indexOf("/") != -1)
        {
            Dialog.alert("标题中存在特殊字符，请确认！");
            return false;
        }


        var fylxMaxValue = jQuery('#indexnum3').val();
        //
        for(var i = 0;i<fylxMaxValue;i++){
            if(jQuery('#field7053_' +i).length>0){
                // jQuery('#field6676').val(jQuery('#field7053_' + (fylxMaxValue - 1)).val())
                jQuery('#field6676').val(jQuery('#field7053_' + i).val());
                break;
            }
        }
        //

        var wshjValue = jQuery('#field6754').val()
        var ftjeValue = jQuery('#field6527').val()
        var jeValue = jQuery('#field6510').val()
        if(ftjeValue == ""){
            ftjeValue = "0";
        }
        if(wshjValue == ""){
            wshjValue = "0";
        }
        if(jeValue == ""){
            jeValue = "0";
        }
        if (jQuery('#field6521').val() == '0') {
            if (Number(wshjValue) != Number(ftjeValue)) {
                Dialog.alert('温馨提示:分摊金额合计不等于未税合计金额，请核对后重新提交。')
                return false
            }
        } else if (jQuery('#field6521').val() == '1') {
            if (Number(jeValue) != Number(ftjeValue)) {
                Dialog.alert('温馨提示:分摊金额合计不等于金额，请核对后重新提交。')
                return false
            }
        }

        if (jQuery('#nodesnum2').val() == '0') {
            Dialog.alert('温馨提示：分摊明细表中无数据，请核对后重新提交。')
            return false
        }

        var result = checkFeeType(_C.stEach(t4.fylx).concat())
        if (result === '0') {
            Dialog.alert('费用类型会签单位不一致,请检查')
            return false
        }

        var sf_val = jQuery(sfyfp).val()
        var nodenum = jQuery('#nodesnum2').val()
        var nodenum0 = jQuery('#nodesnum0').val()
        /// 20190716
        if (sf_val == '0' && Number(nodenum0) < 1) {
            Dialog.alert('您选择了有发票，请添加发票明细！')
            return false
        }
        var ht1 = '#field7042'// 合同1
        var ht2 = '#field7043'// 合同2
        var fq1_val = jQuery(fq1).val()
        var fq2_val = jQuery(fq2).val()
        var ht1_val = jQuery(ht1).val()
        var ht2_val = jQuery(ht2).val()
        if (fq1_val.length > 5 && fq2_val.length > 5 && ht1_val.length > 5 && ht2_val.length > 5) {
            if (fq1_val >= ht1_val && fq1_val <= ht2_val && fq1_val <= fq2_val && fq2_val >= ht1_val && fq2_val <= ht2_val) {

            } else {
                alert('房屋使用期间必须在合同期间内！')
                return false
            }
        }
        var fylx_val = jQuery(fylx).val()
        if (fylx_val == '52' || fylx_val == '13') {
            var htfjsc = '#field6522'
            var fjsc_val = jQuery(htfjsc).val()
            if (fjsc_val == 'null' || fjsc_val == '') {
                if (jQuery('.progressBarStatus').length > 0) {
                    jQuery('.progressBarStatus').each(function () {
                        if (jQuery(this).html().indexOf('Pending') < 0) {
                            if (jQuery('#field6522_tdwrap .progressBarStatus').html().indexOf('Pending') < 0) {
                                Dialog.alert('办公费租用费用 必须上传附件')
                                return false
                            }
                        }
                    })
                } else {
                    Dialog.alert('办公费租用费用 必须上传附件')
                    return false
                }
            }
        }
        //
        if (sf_val == '0' && Number(nodenum) > 0) {
            var je_val = jQuery(wshz).val()
            var fthzje_val = jQuery(fthzje).val()
            if (je_val == '') {
                je_val = '0'
            }
            if (fthzje_val == '') {
                fthzje_val = '0'
            }
            if (Number(je_val) != Number(fthzje_val)) {
                alert('明细分摊金额汇总不等于未税总金额，请检查')
                return false
            }
        } else {
            var je_val = jQuery(je).val()
            var fthzje_val = jQuery(fthzje).val()
            if (je_val == '') {
                je_val = '0'
            }
            if (fthzje_val == '') {
                fthzje_val = '0'
            }
            if (Number(je_val) != Number(fthzje_val)) {
                alert('明细分摊金额汇总不等于金额，请检查')
                return false
            }
        }
        if (sf_val == '0' && Number(nodenum0) > 0) {
            var zje_val = jQuery('#field6792').val()// 未税税额总和
            var jes_val = jQuery(je).val()// 金额
            if (zje_val == '') {
                zje_val = '0'
            }
            if (jes_val == '') {
                jes_val = '0'
            }
            if (Number(jes_val) != Number(zje_val)) {
                alert('金额和发票未税税额总和不相等，请检查')
                return false
            }
        }
        var skje = '#field6863'// 收款金额
        var jes = jQuery(je).val()// 金额
        var skje_val = jQuery(skje).val()
        if (skje_val == '') {
            skje_val = '0'
        }
        if (jes == '') {
            jes = '0'
        }
        var sfyby_val = _C.v('field6752')// 是否使用备用金、
        // 0409
        var gl = jQuery('#field6626').val()
        if (sfyby_val == '1' && gl == '') {
            if (Number(skje_val) != Number(jes)) {
                Dialog.alert('收款总额不等于 金额')
                return false
            }
        } else if (gl.length > 0 && gl != '') {
            var ybjr = jQuery('#field6517').val()
            if (Number(ybjr) > 0) {
                if (Number(skje_val) != Number(ybjr)) {
                    Dialog.alert('收款总额不等于 应补金额')
                    return false
                }
            } else {
                var indexs = jQuery('#nodesnum1').val()
                if (Number(indexs) > 0) {
                    Dialog.alert('有应补金额时才可填写收款人明细!!!')
                    return false
                }
            }
        }

        if (sfyby_val == '0') { // 0 是  1 否
            var byed_val = _C.v('field6753')//
            var zje_val = jQuery(je).val()// 金额
            if (byed_val == '') {
                byed_val = '0'
            }
            if (zje_val == '') {
                zje_val = '0'
            }
            if (Number(byed_val) < Number(zje_val)) {
                Dialog.alert('金额额大于备用金余额')
                return false
            }
        }

        var ckeckre = checkRep()
        if (!ckeckre) {
            alert('发票号码有重复，请检查确认')
            return false
        }
        var fps = checkFP()
        var isss = isEmptyObject(fps)
        if (!isss && fps.length > 0) {
            var fparr = unique4(fps)
            if (fparr.length > 0) {
                var rsu = checkFPs(fparr)
                if (!rsu) {
                    return false
                }
            }
        }
        var indexnum3=jQuery("#indexnum2").val();
        for( var index=0;index<indexnum3;index++){
            if( jQuery(bmdm_dt3+index).length>0){
                var bmdm_dt3_val = jQuery(bmdm_dt3+index).val();
                if(bmdm_dt3_val == ""){
                    Dialog.alert("分摊明细部门代码不能为空，请检查");
                    return false;
                }
            }
        }
        return true
    }
})

function setdetail0 () {
    for (var i = 0; i < jQuery('#indexnum0').val(); i++) {
        (function (j) {
            // 发票类型
            jQuery('#field6528_' + j).bindPropertyChange(function () {
                setTimeout("setWSJE("+j+")","800");
                setTimeout("setKDK("+j+")","800");

                if(jQuery("#field6528_"+j).val() == '0' || jQuery("#field6528_"+j).val() == '1' || jQuery("#field6528_"+j).val() == '2'){
                    jQuery("#field7069_"+j).removeAttr("readonly");
                    jQuery("#field7069_"+j).val("");
                    jQuery("#field7069_"+j+"_readonlytext").html("");
                    jQuery("#field7069_"+j).change();
                    jQuery("#field7069_"+j).attr("readonly",'readonly');
                }

                if(jQuery("#field6528_"+j).val() == '6'){
                    jQuery("#field7069_"+j+"_format").focus();
                    jQuery("#field7069_"+j+"_format").attr('border','1px solid #E9E9E2');
                    jQuery("#field7069_"+j).css('border','1px solid #E9E9E2');
                    jQuery("#field7069_"+j).removeAttr("readonly");
                    jQuery("#field7069_"+j).val("");
                    jQuery("#field7069_"+j).change();
                }

            })

            // 可抵扣金额
            jQuery('#field7069_' + j).bindPropertyChange(function () {
                setTimeout("setWSJE("+j+")","800");
            })

            //设置可抵扣金额(保存容易刷掉)
            jQuery("#field8163_"+j).bindPropertyChange(function () {
                var kdkVal = jQuery("#field8163_"+j).val();
                if(jQuery("#field6528_"+j).val()  == '3' || jQuery("#field6528_"+j).val() == '4' || jQuery("#field6528_"+j).val() == '5'){
                    jQuery("#field7069_"+j).removeAttr("readonly");
                    jQuery("#field7069_"+j).val(kdkVal);
                    jQuery("#field7069_"+j).change();
                    jQuery("#field7069_"+j).attr("readonly",'readonly');
                    jQuery("#field7069_"+j+"_format").css("border",'none');
                }
            })
            // 金额合计
            bindchange('#field7120_', setWSJE, j);
            setKDK(j);
        })(i)
    }
}

function setKDK (j){
    if(jQuery("#field6528_"+j).val() == '4' || jQuery("#field6528_"+j).val() == '3' || jQuery("#field6528_"+j).val() == '5'){
        jQuery("#field7069_"+j).attr("readonly",'readonly');
        jQuery("#field7069_"+j+"_format").css("border",'none');
    }
    if(jQuery("#field6528_"+j).val() == '6'){
        jQuery("#field7069_"+j+"_format").focus();
        jQuery("#field7069_"+j+"_format").attr('border','1px solid #E9E9E2');
        jQuery("#field7069_"+j).css('border','1px solid #E9E9E2');
        jQuery("#field7069_"+j).removeAttr("readonly");
        jQuery("#field7069_"+j).change();
    }
}

function bindchange (id, fun, index) {
    var old_val = jQuery(id).val()
    setInterval(function () {
        var new_val = jQuery(id).val()
        if (old_val != new_val) {
            old_val = new_val
            fun(index)
        }
    }, 100)
}

function setWSJE (index) {
    var fplxValue = jQuery('#field6528_' + index).val()
    var jeheValue = jQuery('#field6532_' + index).val()
    var kdkjeValue = jQuery('#field7069_' + index).val()
    if (jeheValue > 0.00 && kdkjeValue > 0.00) {
        if (fplxValue === '3' || fplxValue === '4' || fplxValue === '5') {
            jQuery('#field6596_' + index).val(keepTwoDecimalFull(jeheValue - kdkjeValue));
            jQuery('#field6596_' + index).change()
            // setTimeout('doDetail(\'field6596\', 2)', '1000')
            setTimeout(function () {
                doDetail1(t1.fylx, '_' + index)
            }, 1000)
        } else {


            if(fplxValue !== '6'){
                jQuery("#field6596_"+index).val("");
            }
        }
    }
    calSum(4);
    calSum(5);
}

function keepTwoDecimalFull(num) {
    var result = parseFloat(num);
    if (isNaN(result)) {
        return false;
    }
    result = Math.round(num * 100) / 100;
    var s_x = result.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0) {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + 2) {
        s_x += '0';
    }
    return s_x;
}

function getYtYb () {
    var sfyfp_val = jQuery(sfyfp).val()
    // alert("sfyfp_val:"+sfyfp_val);
    var sezh_val = jQuery(sezh).val()
    // alert("sezh_val:"+sezh_val);
    var wshz_val = jQuery(wshz).val()
    // alert("wshz_val:"+wshz_val);
    var zjkje_val = jQuery(zjkje).val()
    // alert("zjkje_val:"+zjkje_val);
    var zje = accAdd(sezh_val, wshz_val)
    // alert("zje:"+zje);
    var je_val = jQuery(je).val()
    // alert("je_val:"+je_val);
    if (sfyfp_val == '0') {
        // alert(0);
        if (zjkje_val > 0) {
            if (zje > zjkje_val) {
                // alert("zje>zjkje_val");
                jQuery(ytje).val(0)
                jQuery(ybje).val(accSub(zje, zjkje_val))
            } else {
                // alert("zje<zjkje_val");
                jQuery(ytje).val(accSub(zjkje_val, zje))
                jQuery(ybje).val(0)
            }
        } else {
            jQuery(ytje).val(0)
            jQuery(ybje).val(0)
        }
    } else {
        // alert(1);
        if (zjkje_val > 0) {
            if (je_val > zjkje_val) {
                // alert("je_val>zjkje_val");
                jQuery(ytje).val(0)
                jQuery(ybje).val(accSub(je_val, zjkje_val))
            } else {
                // alert("je_val<zjkje_val");
                jQuery(ytje).val(accSub(zjkje_val, je_val))
                jQuery(ybje).val(0)
            }
        } else {
            jQuery(ytje).val(0)
            jQuery(ybje).val(0)
        }
    }
}

function setmoney () {
    var ftlx_val = jQuery(ftlx).val()
    var dqxz_val = jQuery(dqxz).val()
    // 415
    var je_val = ''
    var sf_val = jQuery(sfyfp).val()// 是否有发票 0是 1否
    if (sf_val == '0') {
        je_val = jQuery(wshz).val()
    } else {
        je_val = jQuery(je).val()
    }
    //
    if (ftlx_val == '0' || (ftlx_val == '1' && dqxz_val != '')) {
        var nowsum = '0'
        var count = 1
        var nodesnum2 = jQuery('#nodesnum2').val()
        var indexnum2 = jQuery('#indexnum2').val()
        for (var index = 0; index < indexnum2; index++) {
            if (jQuery(bmmc_dt3 + index).length > 0) {
                var bl_val = jQuery(bl_dt3 + index).val()
                if (bl_val == '') {
                    bl_val = '0'
                }
                var ftfy_val = accMul(je_val, bl_val).toFixed(2)
                if (count == nodesnum2) {
                    ftfy_val = accSub(je_val, nowsum)
                } else {
                    nowsum = accAdd(ftfy_val, nowsum)
                }
                jQuery(ftje_dt3 + index).val(ftfy_val)
                jQuery(ftje_dt3 + index + 'span').html('')
                count = count + 1
            }
        }
        calSum(2)
        jQuery(fthzje).val(je_val)
    }
}

function showhide () {
    var ftlx_val = jQuery(ftlx).val()
    if (ftlx_val != '2' && (ftlx_val == '' || ftlx_val == null)) {
        // jQuery("button[name=delbutton2]").hide();
        // jQuery("button[name=addbutton2]").hide();
        var indexnum2 = jQuery('#indexnum2').val()
        for (var index = 0; index < indexnum2; index++) {
            if (jQuery(bmmc_dt3 + index).length > 0) {
                jQuery(bmmc_dt3 + index + '_browserbtn').attr('disabled', true)
                jQuery(bl_dt3 + index).attr('readonly', 'readonly')
            }
        }
    }
}

function dodetail3 () {
    var ftlx_val = jQuery(ftlx).val()
    var dqxz_val = jQuery(dqxz).val()
    var indexnum2 = jQuery('#indexnum2').val()
    var ftxs = '#field6847'// 分摊形式
    var st = new Date().getTime()
    var ftxs_val = jQuery(ftxs).val()

    var xhr = null
    if (window.ActiveXObject) { // IE浏览器
        xhr = new ActiveXObject('Microsoft.XMLHTTP')
    } else if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest()
    }
    if (xhr != null) {
        xhr.open('GET', '/txrz/get_ft_detail.jsp?ftlx=' + ftlx_val + '&dqxz=' + dqxz_val + '&type=' + ftxs_val + '&st=' + st, false)
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var text = xhr.responseText
                    text = text.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '')
                    var text_arr = text.split('@@@')
                    var length1 = Number(text_arr.length) - 1 + Number(indexnum2)
                    for (var i = indexnum2; i < length1; i++) {
                        addRow2(2)
                        var tmp_arr = text_arr[i - indexnum2].split('###')
                        jQuery(bmmc_dt3 + i).val(tmp_arr[1])
                        jQuery(bmmc_dt3 + i + 'span').text(tmp_arr[2])
                        jQuery(bmmc_dt3 + i + '_browserbtn').attr('disabled', true)
                        jQuery(bl_dt3 + i).val(tmp_arr[3])
                        jQuery(bl_dt3 + i + 'span').html('')
                        jQuery(bl_dt3 + i).attr('readonly', 'readonly')
                        jQuery(bmdm_dt3 + i).val(tmp_arr[0])
                        jQuery(bmdm_dt3 + i + 'span').text(tmp_arr[0])
                    }
                }
            }
        }
        xhr.send(null)
    }
    setmoney()
}

function accAdd (arg1, arg2) {
    var r1, r2, m
    try { r1 = arg1.toString().split('.')[1].length } catch (e) { r1 = 0 }
    try { r2 = arg2.toString().split('.')[1].length } catch (e) { r2 = 0 }
    m = Math.pow(10, Math.max(r1, r2))
    return (arg1 * m + arg2 * m) / m
}

function accMul (arg1, arg2) {
    var m = 0, s1 = arg1.toString(), s2 = arg2.toString()
    try { m += s1.split('.')[1].length } catch (e) {}
    try { m += s2.split('.')[1].length } catch (e) {}
    return Number(s1.replace('.', '')) * Number(s2.replace('.', '')) / Math.pow(10, m)
}

function accSub (arg1, arg2) {
    var r1, r2, m, n
    try { r1 = arg1.toString().split('.')[1].length } catch (e) { r1 = 0 }
    try { r2 = arg2.toString().split('.')[1].length } catch (e) { r2 = 0 }
    m = Math.pow(10, Math.max(r1, r2))
    // 动态控制精度长度
    n = (r1 >= r2) ? r1 : r2
    return ((arg1 * m - arg2 * m) / m).toFixed(2)
}

function deldt3 () {
    var nodesnum2 = jQuery('#nodesnum2').val()
    if (nodesnum2 > 0) {
        jQuery('[name = check_node_2]:checkbox').attr('checked', true)
        adddelid(2, bmmc_dt3)
        removeRow0(2)
    }
}

function adddelid (dtid, fieldidqf) {
    var ids_dt1 = ''
    var flag1 = ''
    var indexnum0 = jQuery('#indexnum' + dtid).val()
    for (var index = 0; index < indexnum0; index++) {
        if (jQuery(fieldidqf + index).length > 0) {
            var pp = document.getElementsByName('dtl_id_' + dtid + '_' + index)[0].value
            ids_dt1 = ids_dt1 + flag1 + pp
            flag1 = ','
        }
    }
    var deldtlid0_val = jQuery('#deldtlid' + dtid).val()
    if (deldtlid0_val != '') {
        deldtlid0_val = deldtlid0_val + ',' + ids_dt1
    } else {
        deldtlid0_val = ids_dt1
    }
    jQuery('#deldtlid' + dtid).val(deldtlid0_val)
}

function removeRow0 (groupid, isfromsap) {
    try {
        var flag = false
        var ids = document.getElementsByName('check_node_' + groupid)
        for (i = 0; i < ids.length; i++) {
            if (ids[i].checked == true) {
                flag = true
                break
            }
        }
        if (isfromsap) { flag = true }
        if (flag) {
            if (isfromsap || _isdel()) {
                var oTable = document.getElementById('oTable' + groupid)
                var len = document.forms[0].elements.length
                var curindex = parseInt($G('nodesnum' + groupid).value)
                var i = 0
                var rowsum1 = 0
                var objname = 'check_node_' + groupid
                for (i = len - 1; i >= 0; i--) {
                    if (document.forms[0].elements[i].name == objname) {
                        rowsum1 += 1
                    }
                }
                rowsum1 = rowsum1 + 2
                for (i = len - 1; i >= 0; i--) {
                    if (document.forms[0].elements[i].name == objname) {
                        if (document.forms[0].elements[i].checked == true) {
                            var nodecheckObj
                            var delid
                            try {
                                if (jQuery(oTable.rows[rowsum1].cells[0]).find('[name=\'' + objname + '\']').length > 0) {
                                    delid = jQuery(oTable.rows[rowsum1].cells[0]).find('[name=\'' + objname + '\']').eq(0).val()
                                }
                            } catch (e) {}
                            // 记录被删除的旧记录 id串
                            if (jQuery(oTable.rows[rowsum1].cells[0]).children().length > 0 && jQuery(jQuery(oTable.rows[rowsum1].cells[0]).children()[0]).children().length > 1) {
                                if ($G('deldtlid' + groupid).value != '') {
                                    // 老明细
                                    $G('deldtlid' + groupid).value += ',' + jQuery(oTable.rows[rowsum1].cells[0].children[0]).children()[1].value
                                } else {
                                    // 新明细
                                    $G('deldtlid' + groupid).value = jQuery(oTable.rows[rowsum1].cells[0]).children().eq(0).children()[1].value
                                }
                            }
                            // 从提交序号串中删除被删除的行
                            var submitdtlidArray = $G('submitdtlid' + groupid).value.split(',')
                            $G('submitdtlid' + groupid).value = ''
                            var k
                            for (k = 0; k < submitdtlidArray.length; k++) {
                                if (submitdtlidArray[k] != delid) {
                                    if ($G('submitdtlid' + groupid).value == '') {
                                        $G('submitdtlid' + groupid).value = submitdtlidArray[k]
                                    } else {
                                        $G('submitdtlid' + groupid).value += ',' + submitdtlidArray[k]
                                    }
                                }
                            }
                            oTable.deleteRow(rowsum1)

                            curindex--
                        }
                        rowsum1--
                    }
                }
                $G('nodesnum' + groupid).value = curindex
                calSum(groupid)
            }
        } else {
            alert('请选择需要删除的数据')
            return
        }
    } catch (e) {}
    try {
        var indexNum = jQuery('span[name=\'detailIndexSpan0\']').length
        for (var k = 1; k <= indexNum; k++) {
            jQuery('span[name=\'detailIndexSpan0\']').get(k - 1).innerHTML = k
        }
    } catch (e) {}
}

function _isdel () {
    return true
}

/// //
function checkFP () {
    var indexd = jQuery('#indexnum0').val()
    var nodenums = jQuery('nodesnum0').val()
    var fphm = '#field6529_'
    var b = 0
    for (var i = 0; i < indexd; i++) {
        if (jQuery(fphm + i).length > 0) {
            var fphm_val = jQuery(fphm + i).val()
            if (fphm_val.length > 0) {
                b++
            }
        }
    }
    var arr = new Array(b)
    var a = 0
    for (var i = 0; i < indexd; i++) {
        if (jQuery(fphm + i).length > 0) {
            var fphm_val = jQuery(fphm + i).val()
            if (fphm_val.length > 0) {
                arr[a] = fphm_val
                a++
            }
        }
    }
    return arr
}

function unique4 (arr) {
    var ree = ''
    var b = 0
    var hash = []
    for (var i = 0; i < arr.length; i++) {
        for (var j = i + 1; j < arr.length; j++) {
            if (arr[i] === arr[j]) {
                ++i
            }
        }
        var st = arr[i]
        if (st.length > 0) {
            hash.push(arr[i])
        }
    }
    for (var k = 0; k < hash.length; k++) {
        if (b < 1) {
            ree = hash[k]
        } else {
            ree = ree + ',' + hash[k]
        }
        b++
    }
    return ree
}

///

function checkFPs (fps) {
    var re = true
    var xhr = null
    if (window.ActiveXObject) { // IE浏览器
        xhr = new ActiveXObject('Microsoft.XMLHTTP')
    } else if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest()
    }
    var rid = jQuery('#field6646').val()
    var st = new Date().getTime()
    if (xhr != null) {
        xhr.open('GET', '/txrz/checkFP.jsp?fps=' + fps + '&rid=' + rid + '&st=' + st, false)
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var text = xhr.responseText
                    text = text.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '')
                    if (text.indexOf('@@@@@@') < 0) {
                        Dialog.alert(text + ' 号码已存在发票库')
                        re = false
                    }
                }
            }
        }
        xhr.send()
    }

    return re
}

function isEmptyObject (e) {
    var t
    for (t in e) { return !1 }
    return !0
}

function getBYYE () {
    var hrid = _C.v('field6512')
    var byjk_val1 = _C.v('field6854')// 备用金库
    var xhr = null
    if (window.ActiveXObject) { // IE浏览器
        xhr = new ActiveXObject('Microsoft.XMLHTTP')
    } else if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest()
    }
    var st = new Date().getTime()
    if (xhr != null) {
        xhr.open('GET', '/txrz/getByed.jsp?byjk=' + byjk_val1 + '&st=' + st, false)
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    var text = xhr.responseText
                    text = text.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '')
                    if (text.indexOf('######') < 0) {
                        _C.v('field6753', text, text)// 备用金余额
                    }
                }
            }
        }
        xhr.send(null)
    }
}

function checkRep () {
    var fphm = '#field6529_'
    var resu = true
    var aa = 0
    var indexd = jQuery('#indexnum0').val()
    for (var i = 0; i < indexd; i++) {
        if (jQuery(fphm + i).length > 0) {
            var fphm_vals = jQuery(fphm + i).val()
            if (fphm_vals.length > 0) {
                for (var j = 0; j < indexd; j++) {
                    if (i != j) {
                        if (jQuery(fphm + j).length > 0) {
                            var fphm_valnew = jQuery(fphm + j).val()
                            if (fphm_valnew.length > 0) {
                                if (fphm_valnew == fphm_vals) {
                                    aa++
                                    break
                                }
                            }
                        }
                    }
                }
            }
            if (aa > 0) {
                resu = false
                break
            }
        }
    }
    return resu
}

function getDetail3 (r) {
    var ftlx = _C.v(t4.ftlx + r)
    var dqxz = _C.v(t4.dqxz + r)
    var type = _C.v(t4.ftxs + r)
    var fylx = _C.v(t4.fylx + r)
    var qkje = _C.v(t4.qkje + r)

    // 遍历明细1
    _C.stEach(t1.fylx, function (r1) {
        var fylx1 = _C.v(t1.fylx + r1)
        if (fylx1 === fylx) {
            qkje = _C.v(t1.wsje + r1)
        }
    })

    var qkje1 = sumGroupBy(t1.fylx, t1.wsje, fylx)
    if (qkje1 > 0) {
        qkje = qkje1
    }

    // 定义ajax参数
    var a = {
        // 请求目标地址getReqInfo.jsp
        url: '/txrz/get_ft_detailNew.jsp' +
            '?ftlx=' + ftlx +
            '&dqxz=' + dqxz +
            '&type=' + type +
            '&qkje=' + qkje +
            '&fylx=' + fylx,
        type: 'post',
        // 定义请求完成时的回调函数
        success: function (d) {
            // Dialog.alert('data=' + d)
            var obj = JSON.parse(d)
            if (obj.length > 0) {
                for (var i = 0; i < obj.length; i++) {
                    // Dialog.alert('data=' + obj[i].TRKORR)
                    (function () {
                        var fylx1 = obj[i].fylx
                        var fylxVal = obj[i].fylx_val
                        var bl = obj[i].bl
                        var bmdm = obj[i].bmdm
                        var bmname = obj[i].bmname
                        var bm = obj[i].bm
                        _C.addRow(2, function (r) {
                            // alert('r=' + r)
                            _C.vt(t3.fylx + r, fylx1, fylxVal)
                            _C.v(t3.bl + r, bl)
                            _C.v(t3.bmdm + r, bmdm)
                            _C.vt(t3.bmmc + r, bm, bmname)

                            if (i === obj.length - 1 && obj.length > 1) {
                                var tempSum = sumGroupBy(t3.fylx, t3.je, fylx)
                                _C.v(t3.je + r, Number(qkje) - Number(tempSum))
                            } else {
                                _C.v(t3.je + r, accMul(qkje, bl))
                            }

                            for (var key in t3) {
                                var val = t3[key]
                                if (val !== '_index') {
                                    jQuery('#' + val + r).attr('readonly', 'readonly')
                                }
                            }
                        })
                    })(i)
                }
            } else {
                Dialog.alert('分摊数据获取失败,请重新选择')
            }
            // sumMoney()
        }
    }
    // top.Dialog.alert(a.url)
    if (ftlx === '1') {
        if (dqxz !== '' && fylx !== '' && type !== '' && qkje !== '') {
            // 发送请求
            jQuery.ajax(a)
        }
    } else if (ftlx === '0') {
        if (ftlx !== '' && fylx !== '' && type !== '' && qkje !== '' && fylx !== undefined) {
            // 发送请求
            jQuery.ajax(a)
        }
    }
}

/**
 *
 * @param {Array} arr 数组
 * @param {int} i 明细表
 */
function delRow (arr, i) {
    jQuery.each(arr, function (index, value) {
        _C.deleteRow(i, value)
    })
}

function sumMoney () {
    var sumAll = 0
    _C.stEach(t4.fylx, function (r) {
        var feeType = _C.v(t4.fylx + r)
        var qkje = _C.v(t4.qkje + r)
        if (feeType !== '') {
            // 遍历明细1
            _C.stEach(t1.fylx, function (r1) {
                var fylx1 = _C.v(t1.fylx + r1)
                if (fylx1 === feeType) {
                    qkje = _C.v(t1.wsje + r1)
                }
            })
        }
        sumAll += Number(qkje)
    })
    _C.v(t0.je, sumAll)
}

/**
 *
 * @param {string} field 字段
 * @param {int} r0 明细表
 */
function doDetail1 (field, r0) {
    var r1 = ''
    var feeType = _C.v(field + r0)
    _C.stEach(t4.fylx, function (r) {
        if (_C.v(t4.fylx + r) === feeType) {
            // getDetail3(r)
            r1 = r
        }
    })
    var ftType = _C.v(t4.ftlx + r1)
    if (ftType !== '2') {
        doDetail2(field, r0)
        getDetail3(r1)
    }
}

function doDetail2 (field, r0) {
    var fylx = _C.v(field + r0)
    if (fylx !== '') {
        // 遍历差旅费明细表，删除行序号相同的明细行
        var delArr = []
        _C.stEach(t3.fylx, function (r) {
            // console.log('linenum2=' + _C.v(t2.linenum + r))
            if (_C.v(t3.fylx + r) === fylx) {
                if (r.indexOf('__') === -1) {
                    delArr.push(r)
                }
            }
        })
        // console.log(delArr)
        delRow(delArr, 2)
    }
}

function doDetailIndex (field) {
    var delArr = []
    _C.stEach(field, function (r) {
        var fylx = _C.v(field + r)
        if (fylx !== '') {
            // 遍历差旅费明细表，删除行序号相同的明细行
            _C.stEach(t3.fylx, function (r0) {
                if (_C.v(t3.fylx + r0) !== fylx) {
                    if (r0.indexOf('__') === -1) {
                        delArr.push(r0)
                    }
                }
            })
            // console.log(delArr)
        }
        getDetail3(r)
    })
    // console.log('delArr:' + delArr)
    delRow(delArr, 2)
}

function doDetailIndex2 () {
    _C.deleteRow(2)
    _C.stEach(t4.fylx, function (r) {
        getDetail3(r)
    })
}

function checkFeeType (type) {
    var result = ''
    // 定义ajax参数
    var a = {
        // 请求目标地址getWorkcode.jsp
        url: '/txrz/checkFeeType.jsp?type=' + type,
        type: 'post',
        async: false,
        // 定义请求完成时的回调函数
        success: function (d) {
            // Dialog.alert('data=' + d)
            d = d.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '')
            result = d
        }
    }
    // top.Dialog.alert(a.url);
    // 发送请求
    jQuery.ajax(a)
    return result
}

/**
 * 其他费用生成财务凭证明细
 *
 * @param {string} field1 费用科目id
 * @param {string} field2 汇总金额
 * @param {string} value 费用类型的值
 */
function sumGroupBy (field1, field2, value) {
    var obj = _C.stEach(field1).values
    var new_arr = []
    var sumTotal = 0
    for (var i in obj) {
        if (obj[i] === value) {
            // console.log(i + '--' + obj[i])
            // 判断元素是否存在于new_arr中，如果不存在则插入到new_arr的最后
            if (jQuery.inArray(obj[i], new_arr) === -1) {
                new_arr.push(obj[i])
                _C.stEach(field1, function (r) {
                    var tmpNum = _C.v(field1 + r)
                    if (tmpNum === obj[i]) {
                        sumTotal += Number(_C.v(field2 + r))
                    }
                })
            }
        }
    }
    return sumTotal
}
//zrk  field6801
function CtroBt(){
    var fwsyksrq = "field7040";//房屋开始日期
    var fwsyjsrq = "field7041";//房屋结束日期
    var htksrq = "field7042";//合同开始日期
    var htjsrq = "field7043";//合同结束日期

    var fwsyksrq_v = jQuery("#"+fwsyksrq).val();
    var fwsyjsrq_v = jQuery("#"+fwsyjsrq).val();
    var htksrq_v = jQuery("#"+htksrq).val();
    var htjsrq_v = jQuery("#"+htjsrq).val();
    var gb = jQuery("#field6801").val();//国别地区
    var mx4ind = jQuery("#indexnum3").val();
    for(var i = 0;i<mx4ind;i++){
        if(jQuery('#field7053_' +i).length>0){
            var fy4 = jQuery('#field7053_' +i).val();
            if(fy4 == '52' || fy4 == '13'){
                jQuery("#"+fwsyksrq + 'browser').removeAttr('disabled');
                jQuery("#"+fwsyjsrq + 'browser').removeAttr('disabled');
                if(gb.length<1){
                    addInputCheckField('field6801', 'field6801spanimg');
                }
                if(fwsyksrq_v.length<1){
                    addInputCheckField(fwsyksrq, fwsyksrq+'span');
                }
                if(fwsyjsrq_v.length<1){
                    addInputCheckField(fwsyjsrq, fwsyjsrq+'span');
                }
                if(htksrq_v.length<1){
                    addInputCheckField(htksrq, htksrq+'span');
                }
                if(htjsrq_v.length<1){
                    addInputCheckField(htjsrq, htjsrq+'span');
                }
                break;
            }else{
                removeInputCheckField('field6801', 'field6801spanimg');
                removeInputCheckField(fwsyksrq, fwsyksrq+'span');
                removeInputCheckField(fwsyjsrq, fwsyjsrq+'span');
                removeInputCheckField(htksrq, htksrq+'span');
                removeInputCheckField(htjsrq, htjsrq+'span');
                jQuery("#"+fwsyksrq + 'browser').attr('disabled', 'disabled')
                jQuery("#"+fwsyjsrq + 'browser').attr('disabled', 'disabled')
            }

        }
    }
}
//
var addInputCheckField = function(fieldId, spanImgId) {
    $('#' + fieldId).attr('viewtype', '1');
    var fieldStr = $('input[name=needcheck]').val();
    if(fieldStr.indexOf(fieldId)<0){
        if (fieldStr.charAt(fieldStr.length - 1) != ',') {
            fieldStr += ',';
        }
        $('input[name=needcheck]').val(fieldStr + fieldId + ',');
        $('#' + spanImgId).html('<img src="/images/BacoError_wev8.gif" align="absMiddle">');
    }
};
var removeInputCheckField = function(fieldId, spanImgId) {
    $('#' + fieldId).attr('viewtype', '0');
    var fieldStr = $('input[name=needcheck]').val();
    $('input[name=needcheck]').val(fieldStr.replace(fieldId + ',', ''));
    $('#' + spanImgId).html('');
    $('#' + fieldId).val('');
};
var removeInputCheckField2 = function(fieldId, spanImgId) {
    $('#' + fieldId).attr('viewtype', '0');
    var fieldStr = $('input[name=needcheck]').val();
    $('input[name=needcheck]').val(fieldStr.replace(fieldId + ',', ''));
    $('#' + spanImgId).html('');
};
function setFJ(){
    var fj = "field6523";//附件
    var fj_val = jQuery("#"+fj).val();
    var mx4ind = jQuery("#indexnum3").val();
    var flags = true;
    var fylx4 = "#field7053_";//费用类型
    for(var i = 0;i<mx4ind;i++){
        if(jQuery(fylx4+i).length>0){
            var fy4 = jQuery(fylx4 +i).val();
            if(fy4 =='29'){//办公用品
                if (fj_val == "null" || fj_val == '' || fj_val.length<1) {
                    $('#' + fj).attr('viewtype', '1');
                    var fieldStr = $('input[name=needcheck]').val();
                    if(fieldStr.indexOf("field6523")<0){
                        if (fieldStr.charAt(fieldStr.length - 1) != ',') {
                            fieldStr += ',';
                        }
                        $('input[name=needcheck]').val(fieldStr + fieldId + ',');
                        $('#field_6523span').html('(必填)');
                    }

                }else{
                    removeInputCheckField2(fj, 'field_6523span');
                }
                break;
            }else{
                removeInputCheckField2(fj, 'field_6523span');
            }
        }
    }
}
var t0 = {
    _index: 0,
    gsb: 'field6499' /** 公司别-browser.gsb**/,
    djh: 'field6500' /** 单据编号-varchar(300)**/,
    qkdw: 'field6501' /** 请款单位-int**/,
    sqrq: 'field6503' /** 申请日期-char(10)**/,
    syqj: 'field6504' /** 使用期间-varchar(300)**/,
    htqdqj: 'field6505' /** 合同签订期间-varchar(300)**/,
    ssdq: 'field6506' /** 所属地区-varchar(300)**/,
    syqj1: 'field6507' /** 使用期间-varchar(300)**/,
    ccqj: 'field6508' /** 乘车区间/及时间-varchar(300)**/,
    ssdq1: 'field6509' /** 所属地区-varchar(300)**/,
    je: 'field6510' /** 金额-decimal(38,2)**/,
    dxje: 'field6511' /** 大写金额（合计）-decimal(38,2)**/,
    qkr: 'field6512' /** 请款人-int**/,
    fkfs: 'field6513' /** 付款方式-int**/,
    fkrq: 'field6514' /** 付款日期-char(10)**/,
    fygsbm: 'field6515' /** 费用归属部门-int**/,
    zjkdh: 'field6516' /** 暂借款单号-int**/,
    ybje: 'field6517' /** 应补金额-decimal(38,2)**/,
    ytje: 'field6518' /** 应退金额-decimal(38,2)**/,
    tkfj: 'field6519' /** 退款附件-text**/,
    sfyhthqc: 'field6520' /** 是否有合同或签呈-int**/,
    sfyfp: 'field6521' /** 是否有发票-int**/,
    hthqcfj: 'field6522' /** 合同或签呈附件-text**/,
    fjsc: 'field6523' /** 附件上传-text**/,
    ftlx: 'field6524' /** 分摊类型-int**/,
    dqxz: 'field6525' /** 地区选择-browser.zd**/,
    fhbs: 'field6526' /** 返回标识-int**/,
    ftjehz: 'field6527' /** 分摊金额汇总-decimal(38,2)**/,
    dzzz: 'field6608' /** 大组组长-int**/,
    bjzg: 'field6609' /** 部级主管-int**/,
    cjzg: 'field6610' /** 处级主管-int**/,
    zjkdh_new: 'field6626' /** 暂借款单号（新）-browser.ZZD_common**/,
    jine: 'field6636' /** 暂借款金额-decimal(38,2)**/,
    rid: 'field6646' /** 流程id-varchar(200)**/,
    fylx: 'field6676' /** 费用类型-browser.fylx**/,
    hqfhbs: 'field6677' /** 会签返回标识-int**/,
    gsdm: 'field6751' /** 公司代码-varchar(300)**/,
    sfsybyj: 'field6752' /** 是否使用备用金-int**/,
    byjsyje: 'field6753' /** 备用金剩余金额-decimal(38,2)**/,
    wsjezh: 'field6754' /** 未税金额总和-decimal(38,2)**/,
    gh: 'field6755' /** 工号-varchar(300)**/,
    qkdwms: 'field6756' /** 请款单位描述-varchar(300)**/,
    wssrzh: 'field6792' /** 未税税额总和-decimal(38,2)**/,
    sezh: 'field6793' /** 税额总和-decimal(38,2)**/,
    gys: 'field6799' /** 供应商-browser.gys**/,
    gysbm: 'field6800' /** 供应商编码-varchar(300)**/,
    gbdq: 'field6801' /** 国别地区-browser.gbdq**/,
    gbdqbm: 'field6802' /** 国别地区编码-varchar(300)**/,
    lcfl: 'field6818' /** 流程分类-int**/,
    ftxs: 'field6847' /** 分摊形式-int**/,
    byjk: 'field6854' /** 备用金库-browser.byjk**/,
    skje: 'field6863' /** 收款金额汇总-decimal(38,2)**/,
    lj: 'field6914' /** 拦截-varchar(300)**/,
    ymbs: 'field6920' /** 页面标识-varchar(300)**/,
    fkyh: 'field6935' /** 付款银行-browser.fkxxan**/,
    fkyhzh: 'field6936' /** 付款银行账号-varchar(300)**/,
    jdkmdm: 'field6937' /** 金蝶科目代码-varchar(300)**/,
    htbs: 'field6942' /** 合同标识-int**/,
    kjjb: 'field6965' /** 会计经办-int**/,
    zdfkrq: 'field6971' /** 指定付款日期-char(10)**/,
    fwksrq: 'field7040' /** 房屋开始日期-char(10)**/,
    rwjsrq: 'field7041' /** 房屋结束日期-char(10)**/,
    htksrq: 'field7042' /** 合同开始日期-char(10)**/,
    htjsrq: 'field7043' /** 合同结束日期-char(10)**/,
    clksrq: 'field7044' /** 车辆开始日期-char(10)**/,
    cljsrq: 'field7045' /** 车辆结束日期-char(10)**/,
    ccksrq: 'field7046' /** 乘车开始日期-char(10)**/,
    ccjsrq: 'field7047' /** 乘车结束日期-char(10)**/,
    xzbs: 'field7051' /** 行政标识-int**/
}
var t1 = {
    _index: 1,
    fplx: 'field6528' /** 发票类型-int**/,
    fphm: 'field6529' /** 发票号码-varchar(300)**/,
    se: 'field6531' /** 税额-decimal(38,2)**/,
    jehj: 'field6532' /** 金额合计-decimal(38,2)**/,
    fpsc: 'field6533' /** 发票上传-text**/,
    wsje: 'field6596' /** 未税金额-decimal(38,2)**/,
    pj: 'field7066' /** 票价-decimal(38,2)**/,
    fxfzjj: 'field7067' /** 风险发展基金-decimal(38,2)**/,
    ryfjf: 'field7068' /** 燃油附加费-decimal(38,2)**/,
    kdkje: 'field7069' /** 可抵扣金额-decimal(38,2)**/,
    fylx: 'field7095' /** 费用类型-browser.fylx**/
}
var t2 = {
    _index: 2,
    zh: 'field6534' /** 账号-browser.skxx**/,
    skr: 'field6535' /** 收款人-varchar(300)**/,
    skyh: 'field6536' /** 收款银行-varchar(300)**/,
    je: 'field6537' /** 金额-decimal(38,2)**/,
    lhh: 'field6950' /** 联行号-varchar(300)**/,
    ymbs: 'field6954' /** 页面标识-varchar(300)**/,
    khh: 'field6981' /** 开户行-varchar(300)**/
}
var t3 = {
    _index: 3,
    bmmc: 'field6538' /** 部门名称-int**/,
    bl: 'field6539' /** 比例-varchar(300)**/,
    bmdm: 'field6540' /** 部门代码-varchar(300)**/,
    je: 'field6541' /** 分摊金额-decimal(38,2)**/,
    fylx: 'field7075' /** 费用类型-browser.fylx**/
}
var t4 = {
        _index: 4,
        fylx: 'field7053' /** 费用类型-browser.fylx**/,
        ftxs: 'field7054' /** 分摊形式-int**/,
        ftlx: 'field7055' /** 分摊类型-int**/,
        dqxz: 'field7056' /** 地区选择-browser.zd**/,
        qkje: 'field7057' /** 请款金额-decimal(38,2)**/
    }
    </script>





