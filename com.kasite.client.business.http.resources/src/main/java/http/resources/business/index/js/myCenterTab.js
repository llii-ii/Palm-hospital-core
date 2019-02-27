
var memberJson;//成员信息

$(function(){
	queryBatUser();
	initMenu();
});

function queryBatUser(){
	var apiData = {};
	var param = {};
	param.api = 'QueryBatUser'; 
	param.apiParam = Commonjs.getApiReqParams(apiData,"",15103);
	var url = '../../wsgw/bat/QueryBatUser/callApi.do';
	Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			if(!Commonjs.isEmpty(d.Data)){
				var data = d.Data[0];
				if(Commonjs.isEmpty(data.NickName)){
					$("#batName").html("我的医疗空间");
				}else{
					$("#batName").html(data.NickName+"的医疗空间");
				}
				if(data.Sex=="1"){
					$("#batPlace").html("<i class='sicon icon-nan'></i>"+data.Province+" "+data.City);
				}else{
					$("#batPlace").html("<i class='sicon icon-nv'></i>"+data.Province+" "+data.City);
				}
				if(!Commonjs.isEmpty(data.HeadImgUrl)){
					$("#batImg").attr("src",data.HeadImgUrl);
				}
			}
		}else{
			myLayer.alert(d.RespMessage,3000);
		}
	},{async:false});
}

function initMenu(){
	var topMenuHtml = "";
	$.each(appConfig.indexMenus().myCenterMenu,function(index,element){
		var url = element.url;
		var name = element.name;
		var iconClass = element.iconClass;
		if( element.isEnable == 1){
				if(element.name=='食堂职工卡中心'){
					topMenuHtml += '<li class="c-list-a">';
					topMenuHtml += '<a href="'+ url +'" class="c-arrow-r">';
					topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
					topMenuHtml += '</a>';
					topMenuHtml += '</li>';
				}else{
					topMenuHtml += '<li class="c-list-a">';
					topMenuHtml += '<a href="javascript:void(0)" onclick="location.href=Commonjs.goToUrl(\''+url+'\')" class="c-arrow-r">';
					topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
					topMenuHtml += '</a>';
					topMenuHtml += '</li>';
				}
				
//			if(element.name=='门诊预交金退款'){
//				topMenuHtml += '<li class="c-list-a">';
//				topMenuHtml += '<a href="javascript:void(0)" onclick="Commonjs.checkDefMember(\''+url+'\',1,true)" class="c-arrow-r">';
//				topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
//				topMenuHtml += '</a>';
//				topMenuHtml += '</li>';
//			}else if(element.name=='住院日清单'){
//				topMenuHtml += '<li class="c-list-a">';
//				topMenuHtml += '<a href="javascript:void(0)" onclick="Commonjs.checkDefMember(\''+url+'\',14,true)" class="c-arrow-r">';
//				topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
//				topMenuHtml += '</a>';
//				topMenuHtml += '</li>';
//			}else if(element.name=='门诊日清单'){
//				topMenuHtml += '<li class="c-list-a">';
//				topMenuHtml += '<a href="javascript:void(0)" onclick="Commonjs.checkDefMember(\''+url+'\',1,true)" class="c-arrow-r">';
//				topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
//				topMenuHtml += '</a>';
//				topMenuHtml += '</li>';
//			}else{
//				topMenuHtml += '<li class="c-list-a">';
//				topMenuHtml += '<a href="'+ url +'" class="c-arrow-r">';
//				topMenuHtml += '    <div class="c-list-key c-666 pr15"><i class="'+iconClass+'"></i>'+name+'</div>';
//				topMenuHtml += '</a>';
//				topMenuHtml += '</li>';
//			}
		}
	});
	$(".index-menu").html(topMenuHtml);
}
///**
// * 验证成员信息
// * @param url			需要跳转的页面
// * @param cardType		需要验证的成员卡类型
// * @param isInputMember	是否需要在跳转时带上默认就诊人信息
// * @returns
// */
//function checkMember(url,cardType,isInputMember){
//	//同步查询成员信息
//	if (Commonjs.isEmpty(memberJson)){
//		queryMemberList();
//	}
//	//无成员信息提示添加成员
//	if(!Commonjs.ListIsNotNull(memberJson.Data)){
//		myLayer.clear();
//		noPerson("执行该业务操作前需先添加默认就诊人","立即添加");
//		return;
//	}
//	//遍历成员信息，验证默认就诊人是否绑卡
//	var isHasDefault = false;//是否设置默认就诊人
//	var isSucc = false;//验证是否通过
//	var defMember;//
//	Commonjs.BaseForeach(memberJson.Data, function(i,item){
//		if(!Commonjs.isEmpty(item.IsDefaultMember) && item.IsDefaultMember=='1'){
//			isHasDefault = true;
//			if(!Commonjs.isEmpty(cardType)){
//				if(item.CardType == cardType && !Commonjs.isEmpty(item.CardNo)){
//					defMember = item;
//					isSucc = true;
//					return;
//				}	
//			}else{
//				//没有传入需要验证的卡类型时，默认验证就诊卡
//				if(item.CardType == Commonjs.constant.cardType_1 && !Commonjs.isEmpty(item.CardNo)){
//					defMember = item;
//					isSucc = true;
//					return;
//				}	
//			}
//		}
//	});
//	if(!isHasDefault){
//		myLayer.clear();
//		noPerson("执行该业务操作前需先设置默认就诊人","立即设置");	
//		return;
//	}
//	if(!isSucc){
//		myLayer.clear();
//		noPerson("执行该业务操作前默认就诊人需先绑定卡信息","立即绑定");
//		return;
//	}
//	if(isSucc){
//		if(isInputMember){
//			if(url.indexOf('?')>=0){
//				url = url+"&memberId="+defMember.MemberId+"&cardType="+defMember.CardType+"&cardNo="+defMember.CardNo;
//			}else{
//				url = url+"?memberId="+defMember.MemberId+"&cardType="+defMember.CardType+"&cardNo="+defMember.CardNo;
//			}
//			window.location.href= Commonjs.goToUrl(url);
//		}else{
//			window.location.href=url;
//		}
//	}
//}
//
////查询就诊人列表
//function queryMemberList(){
//	var apiData = {};
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
//
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