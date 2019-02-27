
$(function(){
	$(".c-list").html('');
	$("#noInfo").hide();
	queryArticleList();
	

});

function queryArticleList(){
	$("#noInfo").hide();
	var apiData = {};
	apiData.TypeClass= '4';//文章类型0 新闻动态；1 通知公告；2 健康宣教；3 就诊指南；4 住院需知
	apiData.Status = '1';//文章状态发布

	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryArticleList/callApi.do';
	var d = Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			var data = d.Data;
			if(Commonjs.ListIsNotNull(data)){
				listHtml = $(".c-list").html();
				Commonjs.BaseForeach(data, function(i,item){
					listHtml += "<li class=\"c-list-a\">";
					if(item.Type=='0'){
						//详情为文章
						listHtml += "<a href=\"newsDetail.html?id="+item.ArticleId+"&typeClass=4\" class=\"c-arrow-r\">";
					}else{
						//详情为链接
						listHtml += "<a href=\""+item.LinkUrl+"\" class=\"c-arrow-r\">";
					}
					listHtml += "<div class=\"c-list-info pr10\">";
					listHtml += "<i class=\"sicon icon-guide\"></i>";
					listHtml += "<h4>"+item.Title+"</h4>";
					if(Commonjs.isEmpty(item.ImgUrl)){
						if(!Commonjs.isEmpty(item.IsSueDate)&&item.IsSueDate.length>10){
							listHtml += "</div><div class=\"c-list-key c-f12 c-999 c-pack\">"+item.IsSueDate.substr(0,10)+"</div>";
						}else{
							listHtml += "</div><div class=\"c-list-key c-f12 c-999 c-pack\"></div>";
						}
					}else{
						if(!Commonjs.isEmpty(item.IsSueDate)&&item.IsSueDate.length>10){
							listHtml += "<p class=\"c-f12 c-999\">"+item.IsSueDate.substr(0,10)+"</p></div>";
						}else{
							listHtml += "<p class=\"c-f12 c-999\"></p></div>";
						}
						listHtml += "<div class=\"c-list-key c-f12 c-999 c-pack\"><img src=\""+item.ImgUrl+"\" alt=\"\" /></div>";
					}
					listHtml += "</a></li>";
					});
			}else {
		    	$("#noInfo").show();
			}
		}else{
			myLayer.alert(d.RespMessage);
		}
		$('.c-list').html(listHtml);
	});
}