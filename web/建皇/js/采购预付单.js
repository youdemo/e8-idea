var jydh_dt1 = "#field15631_";//明细1 交易单号
jQuery(document).ready(function () {

	setTimeout("dodetail()",500);
});

function dodetail(){
	var jydhall_val = getjydhAll();
	if (jydhall_val != "") {
		var rqids = getCgRqids(jydhall_val);
		var rqidarr = rqids.split(",");
		var num =0;
		var indexnum0 = jQuery("#indexnum0").val();
		for (var index = 0; index < indexnum0; index++) {
			var jydh_dt1_val = jQuery(jydh_dt1 + index).val();
			if (jydh_dt1_val != "") {
                if(rqidarr[num] == ""){
					num++;
                	continue;
				}
				jQuery(jydh_dt1 + index+"span").text("");
				jQuery(jydh_dt1 + index+"span").html("<a href='javascript:showrq("+rqidarr[num]+")'>"+jydh_dt1_val+"</a>");

				num++;
			}

		}
	}
}

function showrq(rqid){
	var title = "";
	var url = "/workflow/request/ViewRequest.jsp?requestid="+rqid;
	var diag_vote = new window.top.Dialog();
	diag_vote.currentWindow = window;
	diag_vote.Width = 1000;
	diag_vote.Height = 650;
	diag_vote.Modal = true;
	diag_vote.Title = title;
	diag_vote.URL = url;
	diag_vote.isIframe=false;
	diag_vote.CancelEvent=function(){diag_vote.close();
	};
	diag_vote.show();
}
//获取明细行所有不为空的单据号
function getjydhAll() {
    var jydhall_val = "";
    var flag = "";
    var indexnum0 = jQuery("#indexnum0").val();
    for (var index = 0; index < indexnum0; index++) {
        var jydh_dt1_val = jQuery(jydh_dt1 + index).val();
        if (jydh_dt1_val != "") {
            jydhall_val = jydhall_val + flag + jydh_dt1_val;
            flag = ",";
        }

    }
    return jydhall_val;

}
//根据编号获取最大的流程号
function getCgRqids(jydhall_val) {
    var rqids = "";
    jQuery.ajax({
        type: "POST",
        url: "/jh/cg/getcgrqid.jsp",
        data: {'djhs': jydhall_val, 'type': '1'},
        dataType: "text",
        async: false,//同步   true异步
        success: function (data) {
            data = data.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
            rqids = data;
        }
    });
    return rqids;

}

