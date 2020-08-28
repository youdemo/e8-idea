<script type="text/javascript">
var xh_mx1 = "#field6875_";//明细1序号
var xh_mx2 = "#field6876_";//明细2序号
window.onload = function(){
    setTimeout(setCheck,1000);
    setTimeout(setRead,1000);
    jQuery("button[name=addbutton1]").live("click",function(){
        var idd = jQuery("#indexnum1").val();
        var aa = idd-1;
        setTimeout(function(){getMincs(aa)},200);
        jQuery("#field6426_"+aa).attr('readonly','true');//最小厂商
        jQuery("#field6573_"+aa).attr('readonly','true');//最小单价
        jQuery("#field6587_"+aa).attr('readonly','true');//最小税率
        jQuery("#field6428_"+aa).attr('readonly','true');//最小单价4
    });
    checkCustomize = function (){
        var res = checkSlGx();
        if(!res){
            return false
        }
        return true;
    }
}


function setCheck(){
    var index1 = jQuery("#indexnum0").val();
    for(var i=0;i<index1;i++){
        checksl(i);
        getMincs(i);

    }
}

function checksl(ind){
    var sl_1 = "#field6409_";//明细1 数量
    var sl_2 = "#field6427_";//明细2 数量
    jQuery(sl_1+ind).bindPropertyChange(function () {
        var sl1_v = jQuery(sl_1+ind).val();
        jQuery(sl_2+ind).val(sl1_v);
        jQuery("#field6427_"+ind).blur();//数量
        jQuery("#field6428_"+ind).blur();
    })
}
function setRead(){
    var sl_2 = "#field6427_";//明细2 数量
    var indexnum1 = jQuery("#indexnum1").val();
    for(var index=0;index<indexnum1;index++){
        if(jQuery(sl_2+index).length>0){
            //jQuery(sl_2+index).attr("readOnly","true");
            jQuery("#field6426_"+index).attr('readonly','true');//最小厂商
            jQuery("#field6573_"+index).attr('readonly','true');//最小单价
            jQuery("#field6587_"+index).attr('readonly','true');//最小税率
            jQuery("#field6428_"+index).attr('readonly','true');//最小单价4
        }
    }
}
function getMincs(inde){
    var dj1 = "#field6415_";//单价1
    var dj2 = "#field6419_";//单价2
    var dj3 = "#field6423_";//单价3

    var jg1 = "#field6584_";//价格1
    var jg2 = "#field6585_";//价格2
    var jg3 = "#field6586_";//价格3
    var slv1 = "#field6565_";//税率小数1 浏览按钮
    var slv2 = "#field6566_";//税率2 浏览按钮
    var slv3 = "#field6567_";//税率3 浏览按钮

    var cs1 = "#field6414_";//厂商1
    var cs2 = "#field6418_";//厂商2
    var cs3 = "#field6422_";//厂商3



    // var minCs = "#field6426_";//最小厂商
    // var minDj = "#field6573_";//最小单价
    jQuery(cs1+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(dj1+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(jg1+inde).bindPropertyChange(function(){
        var jg1_val = jQuery(jg1+inde).val();
//alert(jg1_val);
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(cs2+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(dj2+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(jg2+inde).bindPropertyChange(function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(cs3+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(dj3+inde).bind('change',function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
    jQuery(jg3+inde).bindPropertyChange(function(){
        var jg1_val = jQuery(jg1+inde).val();
        var jg2_val = jQuery(jg2+inde).val();
        var jg3_val = jQuery(jg3+inde).val();
        var cs1_val = jQuery(cs1+inde).val();
        var cs2_val = jQuery(cs2+inde).val();
        var cs3_val = jQuery(cs3+inde).val();
        getAndSet(jg1_val,cs1_val,jg2_val,cs2_val,jg3_val,cs3_val,inde);

    })
}
function getAndSet(dj1_val,cs1_val,dj2_val,cs2_val,dj3_val,cs3_val,inde){

    var minCs = "#field6426_";//最小厂商
    var minDj = "#field6573_";//最小单价
    var slv1 = "#field6568_";//税率小数1
    var slv2 = "#field6569_";//税率2
    var slv3 = "#field6570_";//税率3
    var minslv = "#field6587_";//最小税率

    var dj11 = "#field6415_";//单价1
    var dj22 = "#field6419_";//单价2
    var dj33 = "#field6423_";//单价3
    var dj44 = "#field6428_";//单价3

    var nm1 = "#field6603_";//
    var nm2 = "#field6604_";//
    var nm3 = "#field6605_";//
    var nm = "#field6606_";//

    var slv1_val = jQuery(slv1+inde).val();
    var slv2_val = jQuery(slv2+inde).val();
    var slv3_val = jQuery(slv3+inde).val();

    var nm1_val = jQuery(nm1+inde).val();
    var nm2_val = jQuery(nm2+inde).val();
    var nm3_val = jQuery(nm3+inde).val();

    var dj11_val = jQuery(dj11+inde).val();
    var dj22_val = jQuery(dj22+inde).val();
    var dj33_val = jQuery(dj33+inde).val();
    //alert("dj1_val-----"+dj1_val+"----dj2_val---"+dj2_val+"----dj3_val---"+dj3_val+"----cs1_val--"+cs1_val+"----cs2_val--"+cs2_val+"----cs3_val--"+cs3_val);
    //alert("slv1_val-----"+slv1_val+"----slv2_val---"+slv2_val+"----slv3_val---"+slv3_val);
    var n1 = 0;
    var n2 = 0;
    var n3 = 0;
    if(cs1_val.length>0&&dj1_val.length>0){
        n1 = 1 ;
    }
    if(cs2_val.length>0&&dj2_val.length>0){
        n2 = 2 ;
    }
    if(dj3_val.length>0&&cs3_val.length>0){
        n3 = 3 ;
    }
    if(n1>0&&n2>0&&n3>0){
        var rs = getM(dj1_val,dj2_val,dj3_val);
        if(rs == '1'){
            jQuery(minCs+inde).val(cs1_val);
            // jQuery(minCs+inde+"span").text(cs1_val);
            jQuery(minDj+inde).val(dj1_val);
            // jQuery(minDj+inde+"span").text(dj1_val);
            jQuery(minslv+inde).val(slv1_val);
            jQuery(dj44+inde).val(dj11_val);
            jQuery(nm+inde).val(nm1_val);
            // jQuery(minslv+inde+"span").text(slv1_val);
        }else if(rs == '2'){
            jQuery(minCs+inde).val(cs2_val);
            // jQuery(minCs+inde+"span").text(cs2_val);
            jQuery(minDj+inde).val(dj2_val);
            // jQuery(minDj+inde+"span").text(dj2_val);
            jQuery(minslv+inde).val(slv2_val);
            jQuery(dj44+inde).val(dj22_val);
            jQuery(nm+inde).val(nm2_val);
            // jQuery(minslv+inde+"span").text(slv2_val);
        }else if(rs == '3'){
            jQuery(minCs+inde).val(cs3_val);
            // jQuery(minCs+inde+"span").text(cs3_val);
            jQuery(minDj+inde).val(dj3_val);
            // jQuery(minDj+inde+"span").text(dj3_val);
            jQuery(minslv+inde).val(slv3_val);
            // jQuery(minslv+inde+"span").text(slv3_val);
            jQuery(dj44+inde).val(dj33_val);
            jQuery(nm+inde).val(nm3_val);
        }
    }else if(n1>0&&n2>0){
        if(Number(dj1_val)>=Number(dj2_val)){
            jQuery(minCs+inde).val(cs2_val);
            // jQuery(minCs+inde+"span").text(cs2_val);
            jQuery(minDj+inde).val(dj2_val);
            // jQuery(minDj+inde+"span").text(dj2_val);
            jQuery(minslv+inde).val(slv2_val);
            // jQuery(minslv+inde+"span").text(slv2_val);
            jQuery(dj44+inde).val(dj22_val);
            jQuery(nm+inde).val(nm2_val);
        }else{
            jQuery(minCs+inde).val(cs1_val);
            // jQuery(minCs+inde+"span").text(cs1_val);
            jQuery(minDj+inde).val(dj1_val);
            // jQuery(minDj+inde+"span").text(dj1_val);
            jQuery(minslv+inde).val(slv1_val);
            // jQuery(minslv+inde+"span").text(slv1_val);
            jQuery(dj44+inde).val(dj11_val);
            jQuery(nm+inde).val(nm1_val);
        }
    }else if(n1>0&&n3>0){
        if(Number(dj1_val)>=Number(dj3_val)){
            jQuery(minCs+inde).val(cs3_val);
            // jQuery(minCs+inde+"span").text(cs3_val);
            jQuery(minDj+inde).val(dj3_val);
            // jQuery(minDj+inde+"span").text(dj3_val);
            jQuery(minslv+inde).val(slv3_val);
            // jQuery(minslv+inde+"span").text(slv3_val);
            jQuery(dj44+inde).val(dj33_val);
            jQuery(nm+inde).val(nm3_val);
        }else{
            jQuery(minCs+inde).val(cs1_val);
            // jQuery(minCs+inde+"span").text(cs1_val);
            jQuery(minDj+inde).val(dj1_val);
            // jQuery(minDj+inde+"span").text(dj1_val);
            jQuery(minslv+inde).val(slv1_val);
            // jQuery(minslv+inde+"span").text(slv1_val);
            jQuery(dj44+inde).val(dj11_val);
            jQuery(nm+inde).val(nm1_val);
        }
    }else if(n2>0&&n3>0){
        if(Number(dj2_val)>=Number(dj3_val)){
            jQuery(minCs+inde).val(cs3_val);
            // jQuery(minCs+inde+"span").text(cs3_val);
            jQuery(minDj+inde).val(dj3_val);
            // jQuery(minDj+inde+"span").text(dj3_val);
            jQuery(minslv+inde).val(slv3_val);
            // jQuery(minslv+inde+"span").text(slv3_val);
            jQuery(dj44+inde).val(dj33_val);
            jQuery(nm+inde).val(nm3_val);
        }else{
            jQuery(minCs+inde).val(cs2_val);
            // jQuery(minCs+inde+"span").text(cs2_val);
            jQuery(minDj+inde).val(dj2_val);
            // jQuery(minDj+inde+"span").text(dj2_val);
            jQuery(minslv+inde).val(slv2_val);
            // jQuery(minslv+inde+"span").text(slv2_val);
            jQuery(dj44+inde).val(dj22_val);
            jQuery(nm+inde).val(nm2_val);
        }
    }else if(n1>0){
        jQuery(minCs+inde).val(cs1_val);
        // jQuery(minCs+inde+"span").text(cs1_val);
        jQuery(minDj+inde).val(dj1_val);
        // jQuery(minDj+inde+"span").text(dj1_val);
        jQuery(minslv+inde).val(slv1_val);
        // jQuery(minslv+inde+"span").text(slv1_val);
        jQuery(dj44+inde).val(dj11_val);
        jQuery(nm+inde).val(nm1_val);
    }else if(n2>0){
        jQuery(minCs+inde).val(cs2_val);
        // jQuery(minCs+inde+"span").text(cs2_val);
        jQuery(minDj+inde).val(dj2_val);
        // jQuery(minDj+inde+"span").text(dj2_val);
        jQuery(minslv+inde).val(slv2_val);
        // jQuery(minslv+inde+"span").text(slv2_val);
        jQuery(dj44+inde).val(dj22_val);
        jQuery(nm+inde).val(nm2_val);
    }else if(n3>0){
        jQuery(minCs+inde).val(cs3_val);
        // jQuery(minCs+inde+"span").text(cs3_val);
        jQuery(minDj+inde).val(dj3_val);
        // jQuery(minDj+inde+"span").text(dj3_val);
        jQuery(minslv+inde).val(slv3_val);
        // jQuery(minslv+inde+"span").text(slv3_val);
        jQuery(dj44+inde).val(dj33_val);
        jQuery(nm+inde).val(nm3_val);
    }else{
        jQuery(minCs+inde).val('');
        // jQuery(minCs+inde+"span").text('');
        jQuery(minDj+inde).val('');
        // jQuery(minDj+inde+"span").text('');
        jQuery(minslv+inde).val('');
        jQuery(nm+inde).val('');
        jQuery(dj44+inde).val('');
        // jQuery(minslv+inde+"span").text('');
    }
    jQuery("#field6427_"+inde).blur();//数量
    jQuery("#field6428_"+inde).blur();
}



function getM(m,n,k){
    if(Number(m)<=Number(n)&&Number(m)<=Number(k)){
        return '1';
    }else if(Number(n)<=Number(m)&&Number(n)<=Number(k)){
        return '2';
    }else if(Number(k)<=Number(m)&&Number(k)<=Number(n)){
        return '3';
    }
}

function checkSlGx(){
    var sl_1 = "#field6409_";//明细1 数量
    var sl_2 = "#field6427_";//明细2 数量
    var wl_1 = "#field6555_";//明细1 物料
    var resu = true;
    var index1 = jQuery("#indexnum0").val();
    var index2 = jQuery("#indexnum1").val();
    for(var i =0;i<index1;i++){
        if(jQuery(xh_mx1+i).length>0){
            var wlbh = jQuery(wl_1+i).val();
            var sl_1_v = jQuery(sl_1+i).val();
            var xh_mx1_v = jQuery(xh_mx1+i).val();
            var sum =0 ;
            if(wlbh.length>0){
                for(var j =0;j<index2;j++){
                    if(jQuery(xh_mx2+j).length>0){
                        var xh_mx2_v = jQuery(xh_mx2+j).val();
                        if(xh_mx2_v == xh_mx1_v){
                            var sl_2_v = jQuery(sl_2+j).val();
                            if(sl_2_v.length<1){
                                sl_2_v = "0";
                            }
                            sum = accAdd(sum,sl_2_v);
                        }
                    }
                }

            }
            if(Number(sum) != Number(sl_1_v)){
                resu = false;
                Dialog.alert("标识为"+xh_mx1_v+" 的数量之和与明细1中对应的数据不一样，请检查！");
                return resu;
            }
        }
    }
    return resu;
}

function accAdd(arg1,arg2){
    var r1,r2,m; try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m
}


</script>






