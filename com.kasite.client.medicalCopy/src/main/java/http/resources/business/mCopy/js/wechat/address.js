var totalMoney = Commonjs.getUrlParam("totalMoney");
var McopyUserId = Commonjs.getUrlParam("McopyUserId");
var caseId = Commonjs.getUrlParam("caseId");
var caseMoney = Commonjs.getUrlParam("caseMoney");
var caseNumber = Commonjs.getUrlParam("caseNumber");

var addressee = parseNone(decodeURI(Commonjs.getUrlParam("addressee")));
var address = parseNone(decodeURI(Commonjs.getUrlParam("address")));
var province = parseNone(decodeURI(Commonjs.getUrlParam("province")));
var telephone = parseNone(Commonjs.getUrlParam("telephone"));
var mediaName = Commonjs.getUrlParam("mediaName");

var authentication = Commonjs.getUrlParam("authentication");
$(function(){
	$("#name").val(addressee);
	$("#address").val(address);
	$("#addr").val(province);
	$("#phone").val(telephone);
});

//快递信息
function expressInfo() {
	if(!nameRegular($("#name").val()) || parseNone($("#name").val()) == ''){
		mui.alert("姓名输入有误请重新输入");
	}else if(!phoneRegular($("#phone").val()) || parseNone($("#phone").val()) == ''){
		mui.alert("手机输入有误请重新输入");
	}else if(parseNone($("#address").val()) == '' || parseNone($("#addr").val()) == ''){
		mui.alert("地址输入有误请重新输入");
	}else if(addressRegular($("#address").val()) == '' || addressRegular($("#addr").val()) == ''){
		mui.alert("请新疆和西藏的患者请致电400-888-4027办理");
	}else{
		var reSelectAddress = Commonjs.getUrlParam("reSelectAddress");
		if(reSelectAddress == 1){
			var url = '../../pay.html?totalMoney='+totalMoney+
			"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
			"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaName+
			"&addressee="+$("#name").val()+"&telephone="+$("#phone").val()+"&address="+$("#address").val()+"&province="+$("#addr").val();
			location.href = Commonjs.goToUrl(url);			
		}else{
			var url = 'auth.html?totalMoney='+totalMoney+
			"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
			"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaName+
			"&addressee="+$("#name").val()+"&telephone="+$("#phone").val()+"&address="+$("#address").val()+"&province="+$("#addr").val();
			location.href = Commonjs.goToUrl(url);
		}

	}
}	

//返回上一步
function returnStep(){
	var url = 'list.html?totalMoney='+totalMoney+
		"&McopyUserId="+Commonjs.getUrlParam("McopyUserId")+"&authentication="+authentication+
		"&caseId="+caseId+"&caseMoney="+caseMoney+"&caseNumber="+caseNumber+"&mediaName="+mediaName+
		"&addressee="+$("#name").val()+"&telephone="+$("#phone").val()+"&address="+$("#address").val()+"&province="+$("#addr").val();
	location.href = Commonjs.goToUrl(url);
}

//将需要的值由null undefined 转成''
function parseNone(value){
	if(value == null || value == undefined || value == "null"){
		return '';
	}else{
		return value;
	}
}


//姓名正则
function nameRegular(name){
	var regular = /^[\u4E00-\u9FA5\uf900-\ufa2d·s]{2,20}$/;
	if(regular.test(name)){
		return true;
	}else{
		return false;
	}
}

//手机正则
function phoneRegular(phone){
	var regular = /^[1][3,4,5,7,8][0-9]{9}$/;
	if(regular.test(phone)){
		return true;
	}else{
		return false;
	}
}

//
function addressRegular(address){
	if(address.indexOf("西藏") != -1 || address.indexOf("新疆") != -1){
		return false;
	}else{
		return true;
	}
}