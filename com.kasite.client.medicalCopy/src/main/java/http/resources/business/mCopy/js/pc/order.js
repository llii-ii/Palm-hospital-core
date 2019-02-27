var id = Commonjs.getUrlParam("id");
var mcId = Commonjs.getUrlParam("mcId");
var orderState = "";
var payState = "";
var courierCompany = "";
var courierCompanyCode = "";
var imgUrl = "";
var printApplicationHtml = '';
var printAddressHtml = '';
$(function(){
	getOrderInfo(id,mcId);
});


//获取订单数据
function getOrderInfo(id,mcId){
	var apiData = {};	
	apiData.id = id;
	apiData.mcId = mcId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('/wsgw/medicalCopy/GetExpressOrderList/callApi.do',param,function(dd){
		if(dd.RespCode == 10000){
			var info = dd.Data[0];
			$("#orderId").val(info.Id);
			orderState = info.OrderState;
			payState = info.PayState;
			if(info.OrderState == '1'){
				//$(".btn-success").text("确认订单");
				$("#orderState").val("申请成功");
			}else if(info.OrderState == '2'){
				$(".btn-success").hide();
				//$(".btn-success").text("取消确认");
				$("#orderState").val("已确认");
			}else if(info.OrderState == '3'){
				$(".btn-success").hide();
				//$(".btn-success").text("取消确认");
				$("#orderState").val("已寄送");
			}else if(info.OrderState == '4'){
				$(".btn-success").hide();
				//$(".btn-success").text("取消确认");
				$("#orderState").val("已取消");
			}
			
			$("#name").val(info.Name);
			$("#patientId").val(info.PatientId);
			
			imgUrl = 'http://'+location.host +"/"+info.IdcardImgName;
			$("#side1").css("background","url(\""+imgUrl+"1.png\")");
			$("#side1").css("background-size","100% 100%");
			$("#side2").css("background","url(\""+imgUrl+"2.png\")");
			$("#side2").css("background-size","100% 100%");
			if(info.OutDeptName != '' && info.OutDeptName != null){
				//var start = info.OutDeptName.indexOf("科");
				//var end = info.OutDeptName.indexOf("楼");
				//$("#outDeptName").val(info.OutDeptName.substring(start+1,end+1));
				$("#outDeptName").val(info.OutDeptName);
			}else{
				$("#outDeptName").val('暂无');
			}
			if(info.OutHosDate != '' && info.OutHosDate != null){
				$("#outHosDate").val(info.OutHosDate.substring(0,10));
			}else{
				$("#outHosDate").val('暂无');
			}
			$("#hospitalization").val(info.Hospitalization+"天");
			$("#operationName").val(info.OperationName);
			$("#totalMoney").val(parseInt(info.CaseNumber)*parseFloat(info.Money)+"元");
			$("#caseNumber").val(info.CaseNumber+"份");
			$("#courierNumber").text(info.CourierNumber);
			$("#courierCompany").text(info.CourierCompany);
			courierCompanyCode = info.CourierCompanyCode
			$("#yjTelephone").text(info.Telephone);
			$("#yjAddress").text(info.Province + info.Address);		
			$("#yjAddressee").text(info.Addressee);
			
			$("#yjdh").val(info.CourierNumber);
			$("#yjgs").text(info.CourierCompany);
			$("#yjlxdh").text(info.Telephone);
			$("#yjdz").text(info.Address);		
			$("#yjsjr").text(info.Addressee);	
			
			printApplicationHtml = "print_application.html?name="+info.Name+"&id="+info.PatientId+"&outDept="+info.OutDeptName+"&dayNum="+info.Hospitalization+"&isOper="+info.Isoperation+
			"&operName="+info.OperationName+"&outDate="+info.OutHosDate+"&cost="+info.TotalMoney+"&printNum="+info.CaseNumber;	

			printAddressHtml = "print_address.html?CN="+info.CourierNumber+"&CC="+info.CourierCompany+"&name="+info.Addressee+"&address="+info.Province+info.Address+"&telephone="+info.Telephone;

		}else{
			alert('无订单数据');
		}

	});
	
}
//放大图片
function enlargeImg(type){
//	if(type == 'side1'){
//		$("#enlargeImg").css("background","url(\""+imgUrl+"1.png\")");
//		$("#enlargeImg").css("background-size","100% 100%");
//	}else if(type == 'side2'){
//		$("#enlargeImg").css("background","url(\""+imgUrl+"2.png\")");
//		$("#enlargeImg").css("background-size","100% 100%");
//	}
//	$("#idCardImg").show();
}
//关闭身份证放大
function idCardImgClose(){
//	$("#enlargeImg").css("background","");
//	$("#enlargeImg").css("background-size","");
//	$("#idCardImg").hide();
}

//确认订单
function sureApplication(){
	if(orderState == '3'){
		alert('该订单已经寄送无需再次确认');
	}else if(orderState == '4'){
		alert('该订单已经取消无需再次确认');
	}else{
		var apiData = {};	
		apiData.id = id;
		apiData.orderState = "2";
//		if($(".btn-success").text() == "确认订单"){
//			apiData.orderState = "2";
//			$(".btn-success").text("取消确认");
//		}else if($(".btn-success").text() == "取消确认"){
//			apiData.orderState = "1";
//			$(".btn-success").text("确认订单");
//		}
		
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
			if(dd.RespCode == 10000){
//				if($(".btn-success").text() == "确认订单"){
//					$("#orderState").val("已确认");
//				}else if($(".btn-success").text() == "取消确认"){
//					$("#orderState").val("申请成功");
//				}
				getOrderInfo(id,mcId);
				$(".btn-success").hide();
				alert("确认成功");
			}else{
				alert("确认失败");
			}
		});
	}

}

//当单号改变时查询其快递公司
$(".yjdh").bind("input propertychange", function () {
	if($(this).val() != '' && $(this).val() != null && $(this).val() != undefined ){
	var param = {};
	param.LogisticCode = $(this).val();
	Commonjs.ajax('/kdniao/orderDistinguish.do',param,function(dd){
		if(dd.Shippers != undefined && dd.Shippers != '' && dd.Shippers != null){
			courierCompany = dd.Shippers[0].ShipperName;
			courierCompanyCode = dd.Shippers[0].ShipperCode;
			$("#yjgs").text(dd.Shippers[0].ShipperName);
		}else{
			$("#yjgs").text('');
		}
	});
}
});

//关闭模态框
function clos(){
	$('.yjdh').attr("disabled","disabled");
	getOrderInfo(id,mcId);
	$('#yjModals').hide();
}


//点击单号录入
function openExpress(){
	$('#yjModals').show();
}


//修正
function correc(){
	$('.yjdh').attr("disabled",false);
	$('.yjdh').val('');
}
//保存
function saveCorrec(){
	if(orderState == '2' && payState == "2"){
		var courierNumber = $("#yjdh").val();
		var apiData = {};	
		apiData.id = id;
		apiData.courierNumber = courierNumber;
		apiData.courierCompany = courierCompany;
		apiData.courierCompanyCode = courierCompanyCode;
		apiData.orderState = "3";
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
			if(dd.RespCode == 10000){
				$('.yjdh').attr("disabled","disabled");
				$("#courierNumber").text(courierNumber);
				$("#courierCompany").text(courierCompany);
				alert("修改成功");
			}else{
				alert("修改失败");
			}

		});	
	}else{
		alert("该订单未确认或未支付无法寄送");
	}
}
//打印申请单
function printApplication(){
	window.location.href = printApplicationHtml;
}
//打印地址
function printAddress(){
	window.location.href = printAddressHtml;
}


//物流信息查询
function kdnSearch(){
	var kdnHtml = "../../html/pc/KDN.html?code="+courierCompanyCode+"&cNum="+$("#courierNumber").text();
	window.open(kdnHtml); 
}