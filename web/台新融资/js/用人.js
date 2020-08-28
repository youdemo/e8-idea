<!-- script代码，如果需要引用js文件，请使用与HTML中相同的方式。 -->
<script type="text/javascript" src="/txrz/js/cw.js"></script>
    <script type="text/javascript">
var ftlx = '#field6550'// 分摊类型
var dqxz = '#field6551'// 地区选择
var fthzje = '#field6553'// 分摊汇总金额
var bmmc_dt3 = '#field6581_'// 明细 3部门名称
var bl_dt3 = '#field6582_'// 明细3 比例
var bmdm_dt3 = '#field6583_'// 明细3   部门代码
var ftje_dt3 = '#field6584_'// 明细3

var je = '#field6565'// 金额
var sfyfp = '#field6547'// 是否有发票  0 是     1 否
var wshz = '#field6760'// 未税汇总
var sezh = '#field6794'// 税额汇总
var ybje = '#field6543'// 应补金额
var ytje = '#field6544'// 应退金额
var zjkje = '#field6638'// 暂借款金额
var fylx = '#field6822'// 费用类型   34
var fjsc = '#field6549'// 附件
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
            if (_C.v(t1.fpje + p.r) !== '') {
                // doDetail(p.k, 2)
                doDetail1(t1.fylx, p.r)
            }
        }
    })

    _C.run2(t1.fpje, function (p) {
        if (p.v.o === undefined) {
            jQuery('#' + p.k + p.r).blur(function () {
                if (_C.v(t1.fpje + p.r) !== '') {
                    // doDetail(p.k, 2)
                    doDetail1(t1.fylx, p.r)
                }
            })
        }
    })

    // showhide()
    var ftlx_valnew = jQuery(ftlx).val()
    var ftlx_valnew1 = jQuery(ftlx).val()
    var ftlx_valnew2 = jQuery(ftlx).val()
    var ftlx_valnew3 = jQuery(ftlx).val()
    var dqxz_valnew = jQuery(dqxz).val()
    jQuery('#field6848').bindPropertyChange(function () {
        jQuery(ftlx).val('')
        ftlx_valnew3 = '00'
        deldt3()
    })

    // 设置未税金额
    jQuery('#nodesnum0').bindPropertyChange(function () {
        setdetail0()
    })
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
    jQuery(ftlx).bindPropertyChange(function () {
        var ftlx_val = jQuery(ftlx).val()
        if (ftlx_valnew != '' && ftlx_valnew != null) {
            deldt3()
        }
        ftlx_valnew = ftlx_val
        if (ftlx_val == '0') {
            // jQuery('button[name=delbutton2]').hide()
            // jQuery('button[name=addbutton2]').hide()
            jQuery(dqxz).val('')
            jQuery(dqxz + 'span').html('')
            jQuery('#field6551_readonlytext').text('')
            if (ftlx_valnew3 != '' && ftlx_valnew3 != null) {
                dodetail3()
            }
        }
        if (ftlx_val == '1') {
            // jQuery('button[name=delbutton2]').hide()
            // jQuery('button[name=addbutton2]').hide()
            if (ftlx_valnew2 != '' && ftlx_valnew2 != null) {
                jQuery(dqxz).val('')
                jQuery(dqxz + 'span').html('')
                jQuery('#field6551_readonlytext').text('')
            }
            ftlx_valnew2 = ftlx_val
            ftlx_valnew3 = ftlx_val
        }
        if (ftlx_val == '2') {
            // jQuery('button[name=delbutton2]').show()
            // jQuery('button[name=addbutton2]').show()
            jQuery(dqxz).val('')
            jQuery(dqxz + 'span').html('')
            jQuery('#field6551_readonlytext').text('')
            var indexnum2 = jQuery('#indexnum2').val()
            for (var index = 0; index < indexnum2; index++) {
                if (jQuery(bmmc_dt3 + index).length > 0) {
                    jQuery(bmmc_dt3 + index + '_browserbtn').removeAttr('disabled')
                    jQuery(bl_dt3 + index).removeAttr('readonly')
                }
            }
            ftlx_valnew3 = ftlx_val
        }
    })
    jQuery(dqxz).bindPropertyChange(function () {
        var ftlx_val = jQuery(ftlx).val()
        var dqxz_val = jQuery(dqxz).val()
        if (ftlx_val == '1') {
            if (dqxz_valnew != dqxz_val) {
                deldt3()
            }
            dqxz_valnew = dqxz_val
            if (dqxz_val != '') {
                dodetail3()
            }
        }
    })
    jQuery(wshz).bindPropertyChange(function () {
        // setmoney()
        getYtYb()
    })
    jQuery(sfyfp).bindPropertyChange(function () {
        getYtYb()
        // setmoney()
    })
    jQuery(sezh).bindPropertyChange(function () {
        getYtYb()
    })
    jQuery(zjkje).bindPropertyChange(function () {
        getYtYb()
    })
    jQuery(je).bindPropertyChange(function () {
        // setmoney()
        getYtYb()
    })

    _C.run2('field6758', function (p) {
        var sfyby_val = _C.v('field6758')// 是否使用备用金
        if (sfyby_val == '0') { // 0 是  1 否
            getBYYE()
            _C.v('field6627', '')//
            _C.deleteRow(1)
            jQuery('#mx1').hide()
        } else {
            _C.v('field6759', '', '')// 备用金余额
            _C.v('field6855', '', '')// 备用金库
            jQuery('#mx1').show()
        }
    })

    _C.run2('field6855', function (p) {
        var sfyby_val = _C.v('field6758')// 是否使用备用金
        var byjk_val = _C.v('field6855')// 备用金库
        if (byjk_val == '' || byjk_val.length < 1) {
            _C.v('field6759', '', '')// 备用金余额
            return
        }
        if (sfyby_val == '0') { // 0 是  1 否
            getBYYE()
        } else {
            _C.v('field6759', '', '')// 备用金余额
            _C.v('field6855', '', '')// 备用金库
        }
    })
    checkCustomize = function () {
        var fplx = "#field6571_";//发票类型
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
        var ftjeValue = jQuery('#field6553').val()
        var wsjehiValue = jQuery('#field6760').val()
        var jeValue = jQuery('#field6565').val()// 金额
        if(ftjeValue == ""){
            ftjeValue = "0";
        }
        if(wsjehiValue == ""){
            wsjehiValue = "0";
        }
        if(jeValue == ""){
            jeValue = "0";
        }

        if (jQuery('#nodesnum2').val() == '0') {
            Dialog.alert('温馨提示：分摊明细中无数据，请核对后重新提交')
            return false
        }

        if (jQuery('#field6547').val() == '0') { // 有发票
            if (Number(ftjeValue) != Number(wsjehiValue)) {
                Dialog.alert('温馨提示：未税合计金额不等于分摊合计金额，请核对后重新提交。')
                return false
            }
        } else if (jQuery('#field6547').val() == '1') { // 无发票
            if (Number(ftjeValue) !== Number(jeValue)) {
                Dialog.alert('温馨提示：金额不等于分摊合计金额，请核对后重新提交。')
                return false
            }
        }
        //
        var fylxMaxValue = jQuery('#indexnum3').val();
        //
        for(var i = 0;i<fylxMaxValue;i++){
            if(jQuery('#field7098_' +i).length>0){
                jQuery('#field6822').val(jQuery('#field7098_' + i).val());
                break;
            }
        }
        //
        //
        var result = checkFeeType(_C.stEach(t4.fylx).concat())
        if (result === '0') {
            Dialog.alert('费用类型会签单位不一致,请检查')
            return false
        }

        var sf_val = jQuery(sfyfp).val()
        var nodenum = jQuery('#nodesnum2').val()
        var nodenum0 = jQuery('#nodesnum0').val()
        // 20190716
        var fylx_val1 = jQuery(fylx).val()
        if (sf_val == '0' && Number(nodenum0) < 1) {
            Dialog.alert('您选择了有发票，请添加发票明细！')
            return false
        }
        if (fylx_val1 == '34') {
            var fjsc_val = jQuery(fjsc).val()
            if (fjsc_val == 'null' || fjsc_val == '') {
                if (jQuery('.progressBarStatus').length > 0) {
                    jQuery('.progressBarStatus').each(function () {
                        if (jQuery(this).html().indexOf('Pending') < 0) {
                            if (jQuery('#field6549_tdwrap .progressBarStatus').html().indexOf('Pending') < 0) {
                                Dialog.alert('台干差旅费  必须上传附件，请上传！')
                                return false
                            }
                        }
                    })
                } else {
                    Dialog.alert('台干差旅费  必须上传附件，请上传！')
                    return false
                }
            }
        }
        if (fylx_val1 == '8') {
            var htfjsc = '#field6548'
            var fjsc_val = jQuery(htfjsc).val()
            if (fjsc_val == 'null' || fjsc_val == '') {
                if (jQuery('.progressBarStatus').length > 0) {
                    jQuery('.progressBarStatus').each(function () {
                        if (jQuery(this).html().indexOf('Pending') < 0) {
                            if (jQuery('#field6548_tdwrap .progressBarStatus').html().indexOf('Pending') < 0) {
                                Dialog.alert('宿舍租金费用 必须上传合同或签呈附件')
                                return false
                            }
                        }
                    })
                } else {
                    Dialog.alert('宿舍租金费用 必须上传合同或签呈附件')
                    return false
                }
            }
        }
        ///
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
                alert('明细分摊金额汇总不等于未税总金额，请检查')
                return false
            }
        }

        if (sf_val == '0' && Number(nodenum0) > 0) {
            var zje_val = jQuery('#field6795').val()// 未税税额总和
            var jes_val = jQuery(je).val()
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

        var skje = '#field6864'// 收款金额
        var jes = jQuery(je).val()// 金额
        var skje_val = jQuery(skje).val()
        if (skje_val == '') {
            skje_val = '0'
        }
        if (jes == '') {
            jes = '0'
        }
        var sfyby_val = _C.v('field6758')// 是否使用备用金、
        // 0409
        var gl = jQuery('#field6627').val()
        if (sfyby_val == '1' && gl == '') {
            if (Number(skje_val) != Number(jes)) {
                Dialog.alert('收款总额不等于 金额')
                return false
            }
        } else if (gl.length > 0 && gl != '') {
            var ybjr = jQuery('#field6543').val()
            if (Number(ybjr) > 0) {
                if (Number(skje_val) != Number(ybjr)) {
                    Dialog.alert('收款总额不等于 应补金额')
                    return false
                } else {
                    var indexs = jQuery('#nodesnum1').val()
                    if (Number(indexs) > 0) {
                        Dialog.alert('有应补金额时才可填写收款人明细!!!')
                        return false
                    }
                }
            }
        }

        //
        if (sfyby_val == '0') { // 0 是  1 否
            var byed_val = _C.v('field6759')//
            var zje_val = jQuery(je).val()// 金额
            if (byed_val == '') {
                byed_val = '0'
            }
            if (zje_val == '') {
                zje_val = '0'
            }
            if (Number(byed_val) < Number(zje_val)) {
                Dialog.alert('金额大于备用金余额')
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
            jQuery('#field6571_' + j).bindPropertyChange(function () {
                setWSJE(j)
            })

            // 可抵扣金额
            jQuery('#field7073_' + j).bindPropertyChange(function () {
                setWSJE(j)
            })
            // 金额合计
            bindchange('#field6575_', setWSJE, j)
        })(i)
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
    var fplxValue = jQuery('#field6571_' + index).val()
    var jeheValue = jQuery('#field6575_' + index).val()
    var kdkjeValue = jQuery('#field7073_' + index).val()

    if (jeheValue > 0.00 && kdkjeValue > 0.00) {
        if (fplxValue === '3' || fplxValue === '4' || fplxValue === '5') {
            jQuery('#field6573_' + index).val(jeheValue - kdkjeValue)
            jQuery('#field6573_' + index).change()
            setTimeout('doDetail(\'field6573\', 2)', '1000')
        } else {
            if(fplxValue !== '6'){
                jQuery("#field6573_"+index).val("");
            }
            //jQuery('#field6573_' + index).val('')
        }
    }
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
        // jQuery('button[name=delbutton2]').hide()
        // jQuery('button[name=addbutton2]').hide()
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
    var ftxs = '#field6848'// 分摊形式
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
    // setmoney()
}

function accAdd (arg1, arg2) {
    var r1, r2, m
    try { r1 = arg1.toString().split('.')[1].length } catch (e) { r1 = 0 }
    try { r2 = arg2.toString().split('.')[1].length } catch (e) { r2 = 0 }
    m = Math.pow(10, Math.max(r1, r2))
    return (arg1 * m + arg2 * m) / m
}

function accMul (arg1, arg2) {
    var m = 0
    var s1 = arg1.toString()
    var s2 = arg2.toString()
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

/// /////////////
function checkFP () {
    var indexd = jQuery('#indexnum0').val()
    var nodenums = jQuery('nodesnum0').val()
    var fphm = '#field6572_'
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
    // alert(a);
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

/// //////////////
function checkFPs (fps) {
    var re = true
    var xhr = null
    if (window.ActiveXObject) { // IE浏览器
        xhr = new ActiveXObject('Microsoft.XMLHTTP')
    } else if (window.XMLHttpRequest) {
        xhr = new XMLHttpRequest()
    }
    var rid = jQuery('#field6647').val()
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
    var hrid = _C.v('field6567')
    var byjk_val1 = _C.v('field6855')// 备用金库
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
                        _C.v('field6759', text, text)// 备用金余额
                    }
                }
            }
        }
        xhr.send(null)
    }
}

function checkRep () {
    var fphm = '#field6572_'
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
            qkje = _C.v(t1.fpje + r1)
        }
    })

    var qkje1 = sumGroupBy(t1.fylx, t1.fpje, fylx)
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
                    qkje = _C.v(t1.fpje + r1)
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

var t0 = {
    _index: 0,
    zjkdh: 'field6542' /** 暂借款单号-int**/,
    ybje: 'field6543' /** 应补金额-decimal(38,2)**/,
    ytje: 'field6544' /** 应退金额-decimal(38,2)**/,
    tkfj: 'field6545' /** 退款附件-text**/,
    sfyhthqc: 'field6546' /** 是否有合同或签呈-int**/,
    sfyfp: 'field6547' /** 是否有发票-int**/,
    hthqcfj: 'field6548' /** 合同或签呈附件-text**/,
    fjsc: 'field6549' /** 附件上传-text**/,
    ftlx: 'field6550' /** 分摊类型-int**/,
    dqxz: 'field6551' /** 地区选择-browser.zd**/,
    fhbs: 'field6552' /** 返回标识-int**/,
    ftjehz: 'field6553' /** 分摊金额汇总-decimal(38,2)**/,
    gsb: 'field6554' /** 公司别-browser.gsb**/,
    djh: 'field6555' /** 单据编号-varchar(300)**/,
    qkdw: 'field6556' /** 请款单位-int**/,
    sqrq: 'field6558' /** 申请日期-char(10)**/,
    je: 'field6565' /** 金额-decimal(38,2)**/,
    dxje: 'field6566' /** 大写金额（合计）-decimal(38,2)**/,
    qkr: 'field6567' /** 申请人-int**/,
    fkfs: 'field6568' /** 付款方式-int**/,
    fkrq: 'field6569' /** 付款日期-char(10)**/,
    fygsbm: 'field6570' /** 费用归属部门-int**/,
    tdr: 'field6585' /** 填单人-int**/,
    tdryx: 'field6586' /** 填单人邮箱-varchar(300)**/,
    sqryx: 'field6587' /** 申请人邮箱-varchar(300)**/,
    gtcyr: 'field6588' /** 共同参与人-text**/,
    dzzz: 'field6612' /** 大组组长-int**/,
    bjzg: 'field6613' /** 部级主管-int**/,
    cjzg: 'field6614' /** 处级主管-int**/,
    zjkdh_new: 'field6627' /** 暂借款单号（新）-browser.ZZD_common**/,
    jine: 'field6638' /** 暂借款金额-decimal(38,2)**/,
    rid: 'field6647' /** 流程id-varchar(200)**/,
    gsdm: 'field6757' /** 公司代码-varchar(300)**/,
    sfsybyj: 'field6758' /** 是否使用备用金-int**/,
    byjsyje: 'field6759' /** 备用金剩余金额-decimal(38,2)**/,
    wsjezh: 'field6760' /** 未税金额总和-decimal(38,2)**/,
    gh: 'field6761' /** 工号-varchar(300)**/,
    qkdwms: 'field6762' /** 请款单位描述-varchar(300)**/,
    hqfhbs: 'field6763' /** 会签返回标识-int**/,
    sezh: 'field6794' /** 税额总和-decimal(38,2)**/,
    wssezh: 'field6795' /** 未税税额总和-decimal(38,2)**/,
    gys: 'field6803' /** 供应商-browser.gys**/,
    gbdq: 'field6804' /** 国别地区-browser.gbdq**/,
    fylx_new: 'field6805' /** 费用类型（废弃）-browser.fylx**/,
    lcfl: 'field6819' /** 流程分类-int**/,
    fylx: 'field6822' /** 费用类型-browser.fylx**/,
    ftxs: 'field6848' /** 分摊形式-int**/,
    byjk: 'field6855' /** 备用金库-browser.byjk**/,
    skje: 'field6864' /** 收款金额汇总-decimal(38,2)**/,
    lj: 'field6915' /** 拦截-varchar(300)**/,
    ymbs: 'field6921' /** 页面标识-varchar(300)**/,
    fkyh: 'field6938' /** 付款银行-browser.fkxxan**/,
    fkyhzh: 'field6939' /** 付款银行账号-varchar(300)**/,
    jdkmdm: 'field6940' /** 金蝶科目代码-varchar(300)**/,
    htbs: 'field6941' /** 合同标识-int**/,
    kjjb: 'field6966' /** 会计经办-int**/,
    zdfkrq: 'field6972' /** 指定付款日期-char(10)**/,
    syrxm: 'field7048' /** 使用人姓名-int**/,
    syqj: 'field7049' /** 使用期间-char(10)**/,
    syqj1: 'field7050' /** 使用期间1-char(10)**/,
    xzbs: 'field7052' /** 行政标识-int**/
}
var t1 = {
    _index: 1,
    fplx: 'field6571' /** 发票类型-int**/,
    fphm: 'field6572' /** 发票号码-varchar(300)**/,
    fpje: 'field6573' /** 未税金额-decimal(38,2)**/,
    se: 'field6574' /** 税额-decimal(38,2)**/,
    jehj: 'field6575' /** 金额合计-decimal(38,2)**/,
    fpsc: 'field6576' /** 发票上传-text**/,
    pj: 'field7070' /** 票价-decimal(38,2)**/,
    fxfzjj: 'field7071' /** 风险发展基金-decimal(38,2)**/,
    ryfjf: 'field7072' /** 燃油附加费-decimal(38,2)**/,
    kdkje: 'field7073' /** 可抵扣金额-decimal(38,2)**/,
    fylx: 'field7096' /** 费用类型-browser.fylx**/
}
var t2 = {
    _index: 2,
    zh: 'field6577' /** 账号-browser.skxx**/,
    skr: 'field6578' /** 收款人-varchar(300)**/,
    skyh: 'field6579' /** 收款银行-varchar(300)**/,
    je: 'field6580' /** 金额-decimal(38,2)**/,
    lhh: 'field6951' /** 联行号-varchar(300)**/,
    ymbs: 'field6955' /** 页面标识-varchar(300)**/,
    khh: 'field6982' /** 开户行-varchar(300)**/,
    skdx: 'field7109' /** 收款对象-int**/
}
var t3 = {
    _index: 3,
    bmmc: 'field6581' /** 部门名称-int**/,
    bl: 'field6582' /** 比例-varchar(300)**/,
    bmdm: 'field6583' /** 部门代码-varchar(300)**/,
    je: 'field6584' /** 分摊金额-decimal(38,2)**/,
    fylx: 'field7076' /** 费用类型-browser.fylx**/
}
var t4 = {
        _index: 4,
        fylx: 'field7098' /** 费用类型-browser.fylx**/,
        ftxs: 'field7099' /** 分摊形式-int**/,
        ftlx: 'field7100' /** 分摊类型-int**/,
        dqxz: 'field7101' /** 地区选择-browser.zd**/,
        qkje: 'field7102' /** 请款金额-decimal(38,2)**/
    }
    </script>









