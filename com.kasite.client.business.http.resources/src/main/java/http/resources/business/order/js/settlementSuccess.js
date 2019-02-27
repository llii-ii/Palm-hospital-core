var hisOrderId = Commonjs.getUrlParam("hisOrderId");
var prescNo = Commonjs.getUrlParam("prescNo");
var orderId = Commonjs.getUrlParam("orderId");
var isPay = Commonjs.getUrlParam("isPay");
function goToOrderDetail() {
	if (isPay == 1) {
		var infoUrl = "orderPrescriptionInfo.html?orderId=" + orderId;
		if (!Commonjs.isEmpty(hisOrderId)) {
			infoUrl += "&hisOrderId=" + hisOrderId;
		}
		if (!Commonjs.isEmpty(prescNo)) {
			infoUrl += "&prescNo=" + prescNo;
		}
		location.href = infoUrl;
	} else {
		var infoUrl = "orderSettlementInfo.html?orderId=" + orderId;
		if (!Commonjs.isEmpty(hisOrderId)) {
			infoUrl += "&hisOrderId=" + hisOrderId;
		}
		if (!Commonjs.isEmpty(prescNo)) {
			infoUrl += "&prescNo=" + prescNo;
		}
		location.href = Commonjs.goToUrl(infoUrl);
	}
}