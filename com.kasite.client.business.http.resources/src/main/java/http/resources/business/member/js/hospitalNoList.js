DefaultMemberId = "";
var clinicSum = 0; //就诊人数
var flag=Commonjs.getUrlParam("flag");
var deptCode=Commonjs.getUrlParam("deptCode");
var doctorCode=Commonjs.getUrlParam("doctorCode");
var stime=Commonjs.getUrlParam("stime");

$(function() {
	loadData();
	if(flag=='addm'){
		myLayer.alert("请添加就诊人",2000);
		}
	else if(flag=='addc'){
		myLayer.alert("请绑定就诊卡",2000);
	}
	
}

);

/**
 * 加载数据
 */
function loadData() {
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	apiData.CardType = Commonjs.constant.cardType_14;//住院号对应的卡类型
	var page = {};
	page.PIndex = 0;
	page.PSize = 5;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData, page);
	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do', param,function(jsons) {
		if(!Commonjs.isEmpty(jsons)) {
			var listHtml = '';
			if(!Commonjs.isEmpty(jsons.RespCode)) {
				if(jsons.RespCode == 10000) {
					if(Commonjs.ListIsNull(jsons.Data)) {
						clinicSum = jsons.Data.length;
						Commonjs.BaseForeach(jsons.Data, function(i, item) {
							if(i == 0) {
								DefaultMemberId = item.MemberId;
							}
							if((Commonjs.isEmpty(item.IsDefaultMember) ||  item.IsDefaultMember!= 1) && clinicSum == 1) {
								setDefaultMember();
							}
							var goBindHtml = '';
							var cardNoHtml = '';
							var isDefaultHtml = '';

							if(!Commonjs.isEmpty(item.CardType) && !Commonjs.isEmpty(item.CardNo)) {
								cardNoHtml = '就诊卡:' + item.CardNo+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
							} else {
								cardNoHtml = '就诊卡:无&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
								goBindHtml = '<i class="fr bindbq mt12">去绑定</i>';
							}
							if(!Commonjs.isEmpty(item.HospitalNo)){
								cardNoHtml += '住院号:' + item.HospitalNo;	
							}else{
								cardNoHtml += '住院号:无';	
								goBindHtml = '<i class="fr bindbq mt12">去绑定</i>';
							}
							if(item.IsDefaultMember == 1 || clinicSum == 1) {
								isDefaultHtml = ' <i class="defaubq">默认就诊人</i>';
							}
							
//							if(!Commonjs.isEmpty(item.CardType) && !Commonjs.isEmpty(item.CardNo)) {
//								cardNoHtml = '就诊卡:' + item.CardNo;
//							} else {
//								goBindHtml = '<i class="fr bindbq mt12">去绑定</i>';
//							}
//							if(item.IsDefaultMember == 1 || Commonjs.isEmpty(clinicSum) || clinicSum == 1) {
//								isDefaultHtml = ' <i class="defaubq">默认就诊人</i>';
//							}
							var memberInfoHref = 'memberInfo.html?memberId=' + item.MemberId + '&cardNo=' + item.CardNo +
								'&cardType=' + item.CardType + '&memberName=' + escape(item.MemberName) + '&mobile=' + item.Mobile +
								'&idCardNo=' + item.IdCardNo + '&hospitalNo=' + item.HospitalNo + '&IsDefaultMember=' + item.IsDefaultMember;
							if(flag=="addc"){
								memberInfoHref +="&deptCode="+deptCode+"&doctorCode="+doctorCode+"&flag="+flag+"&stime="+stime;	
							}
							listHtml += '<li><a href="' + memberInfoHref + '" class="c-arrow-r"><h4>';
							listHtml += goBindHtml;
							listHtml += '<i class="sicon icon-jzr"></i>' + item.MemberName + '</h4><div class="jzr-card c-pack">';
							if(!Commonjs.isEmpty(item.CardNo)){
							listHtml += cardNoHtml;
							}
							else{
								listHtml +='<i class="sicon icon-gantan"></i>'+'尚未绑定就诊卡';
							}
							listHtml += isDefaultHtml;
							listHtml += '</div></a></li>'
						});
						$(".jzr-list").html(listHtml);
					}
				} else {
					myLayer.alert(jsons.RespMessage, 3000);
				}
			}
		} else {
			//通信失败
			myLayer.alert('网络繁忙,请刷新后重试', 3000);
		}
	});
}

/**
 * 添加就诊人判断
 */
function addMember() {
	if(clinicSum >= 5) {
		myLayer.tip({
			con: '最多可添加5位就诊人'
		});
		return;
	} else if(flag=='addm'){
		window.location.href = Commonjs.goToUrl("addMember.html?"+"deptCode="+deptCode+"&doctorCode="+doctorCode+"&flag="+flag+"&stime="+stime);
	}
	else{
		window.location.href = 'addMember.html';
	}
}

function setDefaultMember() {
	var apiData = {};
	apiData.MemberId = DefaultMemberId;
	apiData.OpId = Commonjs.getOpenId();
	apiData.IsDefault = 1;
	var param = {};
	param.api = 'SetDefaultMember';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/basic/SetDefaultMember/callApi.do',param,function(dd){
		if(dd.RespCode==10000){
			$('.input-pack').addClass('checked');
			$('#isDefault').attr('checked', 'true');
		}else{
			myLayer.alert(dd.RespMessage, 3000);
		}
	});
}