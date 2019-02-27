var pagenumber = undefined;
$(function(){
	pagenumber = $("#pagenumber_").val();
	loadChannelMerch(pagenumber, 0);
});

/**
 * 加载渠道-商户配置信息
 * @returns
 */
function loadChannelMerch(index, totalCount){
	pagenumber = index;
	$("#pagenumber_").val(index);
	var Service = {};
	var isAdmin = Commonjs.getUrlParam('isAdmin');
	if(isAdmin !=1 ){
		Service.Status = 1;
	}
	var pageIndex = index;
	var pageSize = 100;
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/channel/queryChannelMerchInfo.do',param,function(resp){
		if( resp.RespCode == 10000 ){
			/*var page = resp.Page;
			if(page.PCount!=undefined){
				if(page.PCount!=0){
					totalCount = page.PCount;
				}else{
					if(page.PIndex==0) totalCount = 0;
				}
			}else{
				totalCount = 0;
			}
			PageModel(totalCount, page.PSize, 'pager');*/
			var list = resp.Data;
			if( !Commonjs.isEmpty(list) ){
				var html = "";
				$.each(list, function(index, value) {
					html+="<tr><td>"+value.ChannelName+"</td>"
						+"<td>"+value.ChannelId+"</td>"
						+"<td>"+getIsEnable(value.IsEnable)+"</td>"
						+"<td>"+value.MerchTypeName+"</td>"
						+"<td>"+value.MerchAccount+"</td>"
						+"<td>"+value.MerchCode+"</td>"
						+"<td>"+getBankNameAndNo(value)+"</td>"
						+"<td><a href=\"channelMerchInfo.html?isAdmin="+isAdmin+"&channelId="+value.ChannelId+"\" class=\"alinks-unline alinks-blue\">查看</a></td></tr>";
				});
				$("#channelMerchTab").html(html);
				$("#channelMerchTab").rowspan(0);
				$("#channelMerchTab").rowspan(1);
			}
		}else{
			//异常提示
//			PageModel(0,pageSize,'pager');
			Commonjs.alert(resp.RespMessage);
		}
	});
}


function getMerchTypeName(merchType){
	if( merchType == 'WX'){
		return "微信";
	}else if( merchType == 'ZFB'){
		return "支付宝";
	}if( merchType == 'YL'){
		return "银联";
	}else{
		return "";
	}
}

function getIsEnable(isEnable){
	if(isEnable == 1){
		return "启用";
	}else if(status == 0){
		return "禁用";
	}
}

function getBankNameAndNo(chanelMerchInfo){
	if( Commonjs.isEmpty(chanelMerchInfo.BankName) 
			&& Commonjs.isEmpty(chanelMerchInfo.BankNo)){
		return "";
	}else{
		return chanelMerchInfo.BankName+":"+chanelMerchInfo.BankNo;
	}
}


function getChannelStatus(status){
	if(status == 1){
		return "是";
	}else{
		return "否";
	}
}

//分页
function PageModel(totalcounts, pagecount, pager) {
	$("#"+pager).pager( {
		totalcounts : totalcounts,
		pagesize : 10,
		pagenumber : pagenumber,
		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
		buttonClickCallback : function(al) {
			loadChannelMerch(al, totalcounts);
		}
	});
}
