var memberJson = ''
$(function(){
	
	appConfig.indexMenus();
	//支付宝渠道先更新用户信息
	queryBatUser();
	
	var height = $(".bd ul").width()/2 ;
	$(".bd ul li img").height(height);
	$(".bd ul").height(height);
	//加载菜单
	initMenu();
	//加载医院信息
	queryHospital();
});
/**
 * 初始化图片幻灯片插件
 * @returns
 */
function initTouchSlide(){
	TouchSlide({
		slideCell:"#slideBox",
		titCell:".hd ul",
		mainCell:".bd ul", 
		effect:"leftLoop", 
		autoPage:true,
		autoPlay:true
	});
}
function queryBatUser(){
	var apiData = {};
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData,"",15103);
	var url = '../../wsgw/bat/QueryBatUser/callApi.do';
	Commonjs.ajax(url,param,function(d){
		if(d.RespCode != 10000){
			myLayer.alert(d.RespMessage,3000);
		}
	})
}
//查询医院信息
function queryHospital(){
	var apiData = {};
	//apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryHospitalListLocal/callApi.do';
	var d = Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			var data = d.Data;
			if(Commonjs.isEmpty(data)){
				myLayer.alert("返回数据为空!");
			}else{
				var urls = [];
				$.each(data,function(i,obj){
					if(!Commonjs.isEmpty(obj.PhotoUrl)){
						var urlObj = JSON.parse(obj.PhotoUrl);
						if(!Commonjs.isEmpty(urlObj.photo1)){
							urls.push(urlObj.photo1);
						}
						if(!Commonjs.isEmpty(urlObj.photo2)){
							urls.push(urlObj.photo2);
						}
						if(!Commonjs.isEmpty(urlObj.photo3)){
							urls.push(urlObj.photo3);
						}
					}
				})
				var html = '';
				$.each(urls,function(i,url){
					html = html +'<li id = "li'+i+'"><img id = "tempImg'+i+'" src="'+url+'" alt="" /></li>'
				})
				$("#slideUl").html(html);
//				if(!Commonjs.isEmpty(data.PhotoUrl)){
//					data.PhotoUrl = JSON.parse(data.PhotoUrl);
//					if(!Commonjs.isEmpty(data.PhotoUrl.photo1)){
//						$("#tempImg1").attr("src",unescape(data.PhotoUrl.photo1));
//					}else{
//						$("#li1").remove();
//					}
//					if(!Commonjs.isEmpty(data.PhotoUrl.photo2)){
//						$("#tempImg2").attr("src",unescape(data.PhotoUrl.photo2));
//					}else{
//						$("#li2").remove();
//					}
//					if(!Commonjs.isEmpty(data.PhotoUrl.photo3)){
//						$("#tempImg3").attr("src",unescape(data.PhotoUrl.photo3));
//					}else{
//						$("#li3").remove();
//					}
//				}
			}
		}else{
			myLayer.alert(d.RespMessage);
		}
		$('#datalist').empty().append(html);
		initTouchSlide();
	});
}

function initMenu(){
	var topMenuHtml = "";
	$.each(appConfig.indexMenus().clinicTopMenu,function(index,element){
		if( element.isEnable == 1){
			if( element.isBuilding == 1){
				topMenuHtml +=
					"<li><a href='javascript:void(0)'><i class='"+element.iconClass+"'></i>" +
							"<h4 class='c-f15'>"+element.name+"</h4><p class='c-999'>"+element.title+"</p><i class=\"seting\"></i></a></li>";
			}else{
				if( element.name=='门诊缴费'){
					topMenuHtml +=
						"<li><a onclick = \"location.href=Commonjs.goToUrl('"+element.url+"')\" style='cursor: pointer'><i class='"+element.iconClass+"'></i>" +
								"<h4 class='c-f15'>"+element.name+"</h4><p class='c-999'>"+element.title+"</p></a></li>";
//					topMenuHtml +=
//						"<li><a onclick = \"Commonjs.checkDefMember('"+element.url+"',1,false)\" style='cursor: pointer'><i class='"+element.iconClass+"'></i>" +
//								"<h4 class='c-f15'>"+element.name+"</h4><p class='c-999'>"+element.title+"</p></a></li>";
				}else{
					topMenuHtml +=
						"<li><a href='"+element.url+"'><i class='"+element.iconClass+"'></i>" +
								"<h4 class='c-f15'>"+element.name+"</h4><p class='c-999'>"+element.title+"</p></a></li>";
				}
			}
			
		}
		
		
		});
	$(".index-nav").html(topMenuHtml);
	
	var menuHtml = "";
	$.each(appConfig.indexMenus().clinicMenu,function(index,element){
		if( element.isEnable == 1){
			if( element.isBuilding == 1){
				menuHtml +=
					"<li><a href='javascript:void(0)'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p><i class=\"seting\"></i></a></li>";
			}else{
				if( element.name=='排队候诊'){
//					menuHtml +=
//						"<li><a  onclick = \"Commonjs.checkDefMember('"+element.url+"',1,false)\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
					menuHtml +=
						"<li><a  onclick = \"location.href=Commonjs.goToUrl('"+element.url+"')\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
				}else if( element.name=='诊间结算'){
//					menuHtml +=
//						"<li><a  onclick = \"Commonjs.checkDefMember('"+element.url+"',1,false)\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
					menuHtml +=
						"<li><a  onclick = \"location.href=Commonjs.goToUrl('"+element.url+"')\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
				}else if( element.name=='报告查询'){
					menuHtml +=
						"<li><a  onclick = \"location.href=Commonjs.goToUrl('"+element.url+"')\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
//					menuHtml +=
//						"<li><a  onclick = \"Commonjs.checkDefMember('"+element.url+"',1,false)\" style='cursor: pointer'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
				}else{
					menuHtml +=
						"<li><a href='"+element.url+"'><i class='"+element.iconClass+"'></i><p>"+element.name+"</p></a></li>";
				}
			}
		}
		
		
		});
	$(".index-list").html(menuHtml);
}


//function checkMember(url){
//	if (Commonjs.isEmpty(memberJson)){
//		queryMemberList();
//	}
//	memberInfoCheck(memberJson,url);
//}
////查询就诊人列表
//function queryMemberList(){
//	var apiData = {};
////	apiData.HosId = Commonjs.getUrlParam("HosId");
//	var page = {};
//	page.PIndex =0;
//	page.PSize = 0;
//	var param = {};
//	param.apiParam = Commonjs.getApiReqParams(apiData,page);
//	Commonjs.ajax('/wsgw/basic/QueryMemberList/callApi.do',param,function(jsons){
//		if(!Commonjs.isEmpty(jsons)){
//			if(!Commonjs.isEmpty(jsons.RespCode)){
//				if(jsons.RespCode==10000){
//					memberJson = jsons;
//				} else {
//	            	myLayer.alert(jsons.RespMessage,3000);
//	            }
//			}else{
//				myLayer.alert(Commonjs.jsonToString(jsons),3000);
//			}
//		} else{
//			//通信失败
//        	myLayer.alert('网络繁忙,请刷新后重试',3000);
//		}
//    },{async:false});
//
//}	
///**
// * 就诊人信息判断
// * 无就诊人询问是否添加就诊人
// * 有就诊人无默认就诊人询问是否添加默认就诊人
// * 有默认就诊人无就诊卡询问是否绑定就诊卡
// * */
//function memberInfoCheck(jsons,url){
//	var isSucc = true;
//	if(!Commonjs.ListIsNotNull(jsons.Data)){
//		isSucc = false;
//		myLayer.clear();
//		noPerson("执行该业务操作前需先添加默认就诊人","立即添加");
//		return;
//	}
//	var isHasDefault = false;
//	Commonjs.BaseForeach(jsons.Data, function(i,item){
//		if(!Commonjs.isEmpty(item.IsDefaultMember)&&item.IsDefaultMember=='1'&&!isHasDefault){
//			if(Commonjs.isEmpty(item.CardNo)){
//				isSucc = false;
//				myLayer.clear();
//				noPerson("执行该业务操作前默认就诊人需先绑定就诊卡","立即绑定");	
//			}	
//			isHasDefault = true;
//			return;
//		}
//	});
//	if(!isHasDefault&&isSucc){
//		isSucc = false;
//		myLayer.clear();
//		noPerson("执行该业务操作前需先设置默认就诊人","立即设置");	
//		
//	}
//	
//	if(isSucc){
//		window.location.href=url;
//	}
//}
//function noPerson(warmMsg,clickName){
//	var phtml = '';
//	phtml += '	<div class="addpson">';
//	phtml += '		<i class="sicon icon-anclose"></i>';
//	phtml += '		<i class="an-jzrtb"></i>';
//	phtml += '		<div class="h50 c-999 c-f15">'+warmMsg+'</div>';
//	phtml += '		<a href="javascript:location.href=Commonjs.goToUrl(\'/business/member/memberList.html\');" class="btn-anadd mt10">'+clickName+'</a>';
//	phtml += '	</div>';
//	phtml += '	<div class="addpson-mb"></div>';
//	$(".addpsonpop").html(phtml);
//	$('.icon-anclose').on('click',function(){
//		$('.addpsonpop').html("");
//	});
//}