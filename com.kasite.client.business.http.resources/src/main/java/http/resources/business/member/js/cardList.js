var memberInfo = {};
var flag = Commonjs.getUrlParam("flag");
var deptCode = Commonjs.getUrlParam("deptCode");
var doctorCode = Commonjs.getUrlParam("doctorCode");
var stime=Commonjs.getUrlParam("stime");
var disabledCheckBox = false;
var cardList = [];
$(function() {
	var memberObj = Commonjs.getUrlParam('memberInfo');
	memberInfo.cardNo = memberObj.split(",")[0];
	memberInfo.cardType = memberObj.split(",")[1];
	memberInfo.memberName = decodeURIComponent(memberObj.split(",")[2]);
	memberInfo.memberId = memberObj.split(",")[3];
	
	loadMemberCardList();
	
});

function loadMemberCardList(){
	var param = {};
	var apiData={};
	apiData.MemberId = memberInfo.memberId;
	apiData.CardType = memberInfo.cardType;
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/basic/QueryMemberCardList/callApi.do?time=' + new Date().getTime(),param,function(dd){
		if(dd.RespCode!=10000 || Commonjs.isEmpty(dd.Data)){
			myLayer.alert('获取卡列表失败',3000);
			return;
		}
		init(dd.Data);
	});
}

function init(cardList){
	var html = "";
	$.each(cardList,function(index,val){
		var cardNo = val.CardNo;
		var isDefault = val.IsDefault;
		var check = "";
		var isDefaultHtml = " c-999\" >就诊卡  :  "+cardNo;
		if(isDefault == 1){
			isDefaultHtml = "c-ffac0b\" >默认就诊卡  :  "+cardNo;
			check = ' checked="checked" ';
		}
		html += '<li><div class="c-list-info mt5 c-t-left '+ isDefaultHtml +'</div>';
		if(cardList.length>1){
			html += '<label class="switch"><input name="ckb" type="checkbox" data-value="'+val.MemberId+'_'+val.CardType+'_'+val.CardNo+'" value="'+ cardNo +'" '+ check +' /> <div class="slider round"></div></label>';
		}
		html += '</li>';
	});
	$("#cardListUl").html(html);
	
	$('#cardListUl').find('input[type=checkbox]').bind('click', function(){
			var checkCardNo = $(this).val();
			var dataValue = $(this).attr("data-value");
			var memberId = dataValue.split("_")[0];
			var cardType = dataValue.split("_")[1];
			var checked = $(this).attr("checked");
			if(Commonjs.isEmpty(checked) || checked!='checked'){
				setDefaultCard(memberId,cardType,checkCardNo,cardList);
			}else{
				init(cardList)
			}
		});
}

function setDefaultCard(memberId,cardType,cardNo,cardList){
	var apiData = {};
	apiData.IsDefault = 1;
	apiData.MemberId = memberId;
	apiData.CardType = cardType;
	apiData.CardNo = cardNo;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/basic/SetDefaultCard/callApi.do',param,function(dd){
		if(dd.RespCode==10000){
			$.each(cardList,function(index,val){
				if(val.CardNo==cardNo){
					val.IsDefault = 1;
				}else{
					val.IsDefault = 0;
				}
			})
			init(cardList);
		}
	});
}

function gotoBindClinic(){
	location.href = Commonjs.goToUrl("/business/member/bindClinicCard.html?memberId=" + memberInfo.memberId
				+ "&cardType="+memberInfo.cardType
				+ "&memberName=" + escape(memberInfo.memberName));
}

function go() {
	if (memberInfo.cardType == 1 && !Commonjs.isEmpty(memberInfo.cardNo)) {
		var memberObj = memberInfo.cardNo+","+memberInfo.cardType+","+encodeURIComponent(memberInfo.memberName)+","+memberInfo.memberId;
		window.location.href = Commonjs.goToUrl('/business/outpatientDept/outpatientCardAccount.html?memberInfo='+memberObj+'&type=1');
	} else {
		return;
	}

}
