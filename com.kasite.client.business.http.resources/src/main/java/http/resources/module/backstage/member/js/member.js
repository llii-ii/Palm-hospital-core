var index = 1;
$(function() {
	$(".mt6").css("height", $("body").height() * 0.42);
	loadMemberList();
})

function loadMemberList() {
	var code = "";
	$('#pagenumber').val(index);
	var param = {};
	var data = {};
	data.Page = {'PIndex':index-1,'PSize':5};
	data.MemberNameLike = $('#name').val();
	data.IdCardNo = $('#idCardNo').val();
	data.CardNo = $('#cardNo').val();
	param.reqParam = Commonjs.getReqParams(data);
	
	Commonjs.ajax('/basic/queryMemberList.do', param, function(d){
		if(d.RespCode == 10000) {
			Page(d.Page.PCount, d.Page.PSize, 'pager');
			$('#memberInfo').html('');
			var listHtml = "";
			if(Commonjs.ListIsNull(d.Data)) {
				Commonjs.BaseForeach(d.Data, function(i, item) {
					listHtml += '<tr>';
					listHtml += '<td>' + item.MemberName + '</td>';
					listHtml += '<td>' + item.IdCardNo + '</td>';
					listHtml += '<td>' + item.CardNo + '</td>';
					listHtml += '<td>' + item.Mobile + '</td>';
					listHtml += "<td><a href='javascript:;' class='btn btn-gray' id='hh' onclick='unbind(\""+item.MemberId+"\");'>解绑</a> ";
					listHtml += '</td></tr>';
				})
			}
			if(listHtml == "") {
				$('#memberInfo').html("<tr align=center><td colspan=4><font color=red>未找到相关数据!</font></td></tr>");
				return;
			}
			$('#memberInfo').html(listHtml);
			return;
		}
	});
	
}


function query() {
	index=1;
	loadMemberList();
}
//分页
function Page(totalcounts, pagecount, pager) {
	$("#" + pager).pager({
		totalcounts: totalcounts,
		pagesize: 5,
		pagenumber: $("#pagenumber").val(),
		pagecount: parseInt(totalcounts / pagecount) + (totalcounts % pagecount > 0 ? 1 : 0),
		buttonClickCallback: function(al) {
			$("#pagenumber").val(al);
			index = al;
			loadMemberList();
		}
	});
}

function unbind(memberId) {
	var code = "";
	var param = {};
	param.reqParam = Commonjs.getReqParams({'MemberId':memberId});
	Commonjs.ajax('/basic/delMemberInfo.do', param, function(d){
		if(d.RespCode == 10000) {
			Commonjs.alert("解绑成功!");
			loadMemberList();
		}else{
			Commonjs.alert(d.RespMessage);
			return;
		}
	});
}