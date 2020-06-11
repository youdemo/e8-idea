var lydh= "#field15651";//来源单号
jQuery(document).ready(function () {

	setTimeout("doMain()",500);
});

function doMain(){
	var lydh_val = jQuery(lydh).val();
	if (lydh_val != "") {
		var rqid = getCgRqids(lydh_val);
		if (rqid != "") {
			jQuery(lydh+"span").text("");
			jQuery(lydh+"span").html("<a href='javascript:showrq("+rqid+")'>"+lydh_val+"</a>");
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

//根据编号获取最大的流程号
function getCgRqids(lydh_val) {
    var rqids = "";
    jQuery.ajax({
        type: "POST",
        url: "/jh/cg/getcgrqid.jsp",
        data: {'djhs': lydh_val, 'type': '2'},
        dataType: "text",
        async: false,//同步   true异步
        success: function (data) {
            data = data.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
            rqids = data;
        }
    });
    return rqids;

}

