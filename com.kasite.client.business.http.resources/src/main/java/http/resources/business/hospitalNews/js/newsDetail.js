var articleId = -1;
var typeClass ='';
$(function(){
	$("#noInfo").hide();
	getURLParma();
	if(!Commonjs.isEmpty(typeClass)&&typeClass=='3'){
		$("title").html("就诊指南");
	}else if(!Commonjs.isEmpty(typeClass)&&typeClass=='4'){
		$("title").html("出入院流程");
	}
	
	$(".guide-con").html('');
	queryArticle();
	

});

function queryArticle(){
	$("#noInfo").hide();
	var apiData = {};
//	apiData.HosId= Commonjs.hospitalId();
	if(!Commonjs.isEmpty(articleId)&&articleId!=-1){
		apiData.ID = articleId;
	}
	if(!Commonjs.isEmpty(typeClass)){
		apiData.TypeClass = typeClass;//文章类型0 新闻动态；1 通知公告；2 健康宣教；3 就诊指南；4 住院需知
	}
	if((Commonjs.isEmpty(articleId)||articleId==-1)&&(Commonjs.isEmpty(typeClass))){
    	myLayer.clear();
		myLayer.alert("传入参数为空!");
		return;
	}
	apiData.Status = '1';//文章状态发布
	
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryArticle/callApi.do';
	var listHtml = '';
	var d = Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			var data = d.Data;
			if(Commonjs.ListIsNotNull(data)){
				listHtml = $(".guide-con").html();
				Commonjs.BaseForeach(data, function(i,item){
					listHtml += "<h1>"+item.Title+"</h1>";
					if(item.IsSueDate.length>19){
						listHtml += "<h6>"+item.IsSueDate.substring(0,19)+"</h6>";
					}else{
						listHtml += "<h6>"+item.IsSueDate+"</h6>";
					}
					var $pcontent = $("<p></p>");
				    var $content = $(unescape(item.Content));
				    $pcontent.append($content);
				    $pcontent.find('img').css('width','100%'); 
				    listHtml += "<p>"+$pcontent.html()+"</p>";
					listHtml += "<i class=\"sicon icon-guide\"></i>";
					if(!Commonjs.isEmpty(item.BigImgUrl)){
						listHtml += "<div class=\"c-f12 c-999 c-pack\"><img style=\"width:100%;\" src=\""+item.BigImgUrl+"\" alt=\"\" /></div>";
					}
					});
			}else {
		    	$("#noInfo").show();
			}
		}else{
			myLayer.alert(d.RespMessage);

		}
		$('.guide-con').html(listHtml);
	});

}


function getURLParma(){
	articleId = Commonjs.getUrlParam('id');
	typeClass = Commonjs.getUrlParam('typeClass');
}