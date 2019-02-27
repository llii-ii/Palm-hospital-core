var hosId;
var orderId;
var payState;
var bizState;
var operatorId;
var operatorName;
var cardType;
var cardNo;
$(function(){
		hosId = Commonjs.getUrlParam("HosId");
		orderId = Commonjs.getUrlParam("orderId");
		payState = Commonjs.getUrlParam("payState");
		bizState = Commonjs.getUrlParam("bizState");
		operatorId = Commonjs.getUrlParam('operatorId');
		cardNo = Commonjs.getUrlParam('cardNo');
		cardType = Commonjs.getUrlParam('cardType');
		operatorName = unescape(Commonjs.getUrlParam('operatorName'));
		initWidget();
});
function initWidget(){
	$(function(){
		$('.c-main').css('min-height',$(window).height() - 30);
		$(window).resize(function(){
			$('.c-main').css('min-height',$(window).height() - 30);
		});
	});
	$('.c-main').on('click','.ri-title',function(){
		$(this).find('.ui-arrow').toggleClass('ahide');
		$(this).siblings().slideToggle(200);
	});
}

var orderDetail = "/business/yygh/yyghAppointmentDetails.html";
function goToOrderDetail(){
	if (orderDetail.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(orderDetail + "&HosId="+hosId+"&orderId=" + orderId + "&cardNo=" + cardNo + "&cardType=" + cardType + "&payState=" + payState + "&bizState=" + bizState + "&operatorId=" + operatorId  + "&operatorName=" + escape(operatorName));// + "&t"+ new Date().getTime();
    } else {
        location.href = Commonjs.goToUrl(orderDetail + "?HosId="+hosId+"&orderId=" + orderId + "&cardNo=" + cardNo + "&cardType=" + cardType + "&payState=" + payState + "&bizState=" + bizState + "&operatorId=" + operatorId  + "&operatorName=" + escape(operatorName));// + "&t"+ new Date().getTime();
    }
}
function gotoClinicIndex(){
    location.href = Commonjs.goToUrl("/business/index/clinicTab.html");
}
