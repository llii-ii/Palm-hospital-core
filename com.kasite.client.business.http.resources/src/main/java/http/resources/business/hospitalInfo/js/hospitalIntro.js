var hospitalLevelArr = new Array("三甲","三乙","三丙","二甲","二乙","二丙","一乙","一丙");
var isHide = true;

$(function(){
	
	queryHospital();
	var height = $(".bd ul").width()/2 ;
	$(".bd ul li img").height(height);
	$(".bd ul").height(height);
	

	
	//其余信息的隐藏显示效果
	$('.c-main').on('click','.hasmore',function(){
		$(this).toggleClass('ashow').find('.c-list-info').toggleClass('c-nowrap');
	});
	

	//医院简介的隐藏显示效果特殊处理
	$("#hosBrief").find('p').hide();
	$('#hosBrief p:first-child').show();
	if($('#hosBrief p:first-child').attr('style')){
		$('#hosBrief p:first-child').attr('style',$('#hosBrief p:first-child').attr('style').replace('white-space: normal;',''));
	}
	$('#hosBrief p:first-child').toggleClass('c-nowrap');
	$('#hosBrief p:first-child').toggleClass('c-list-info');
	$('#hosBrief p:first-child').css('margin-bottom','0px');
	$('#hosBriefA').on('click',function(){
		
		if(isHide){
			//展开
			isHide= false;
			$("#hosBrief").find('p').show();
			$('#hosBrief p:first-child').toggleClass('c-nowrap');
			$('#hosBrief p:first-child').css('white-space','normal');
			$('#hosBrief p:first-child').css('margin-bottom','20px');
		}else{
			//隐藏
			isHide= true;
			$("#hosBrief").find('p').hide();
			$('#hosBrief p:first-child').show();
			$('#hosBrief p:first-child').toggleClass('c-nowrap');
			$('#hosBrief p:first-child').css('white-space','nowrap');
			$('#hosBrief p:first-child').css('margin-bottom','0px');
		}
		
	});
	

});



function queryHospital(){
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryHospitalLocal/callApi.do';
	var d = Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			if(Commonjs.isEmpty(d.Data)){
				myLayer.alert("返回数据为空!");
			}else{
				var data = d.Data[0];
				$("#level").html(hospitalLevelArr[data.HosLevel]);
				$("#phone").html(data.Tel);
				$("#tel").attr('href','tel:'+data.Tel);
				$("#address").html(data.Address);
				$("#hosBrief").html(unescape(data.HosBrief));
				$("#hosRoute").html(data.HosRoute);
				var photoUrl = eval('(' + data.PhotoUrl + ')');
				if(!Commonjs.isEmpty(photoUrl.photo1)){
					$("#tempImg1").attr("src",unescape(photoUrl.photo1));
				}else{
					$("#li1").remove();
				}
				if(!Commonjs.isEmpty(photoUrl.photo2)){
					$("#tempImg2").attr("src",unescape(photoUrl.photo2));
				}else{
					$("#li2").remove();
				}
				if(!Commonjs.isEmpty(data.PhotoUrl.photo3)){
					$("#tempImg3").attr("src",unescape(photoUrl.photo3));
				}else{
					$("#li3").remove();
				}
			}
		}else{
			myLayer.alert(d.RespMessage);

		}
		$('#datalist').empty().append(html);
		
		TouchSlide({ 
			slideCell:"#slideBox",
			titCell:".hd ul",
			mainCell:".bd ul", 
			effect:"leftLoop", 
			autoPage:true,
			autoPlay:true
		});
	});
}