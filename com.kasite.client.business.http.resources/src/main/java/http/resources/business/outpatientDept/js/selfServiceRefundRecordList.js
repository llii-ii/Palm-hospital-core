var memberPickerDefaultlValue;
var selectedMember;
$(function() {
	var cardNo = Commonjs.getUrlParam("cardNo");
	var cardType = Commonjs.getUrlParam("cardType");
	var memberName = Commonjs.getUrlParam("name");
	var memberId = Commonjs.getUrlParam("memberId");
//	var hisMemberId = Commonjs.getUrlParam("hisUserId");
	memberPickerDefaultlValue = cardNo+","+cardType+","+encodeURIComponent(memberName)+","+memberId;
	initWidget();
});


/**
 * 初始化控件
 */
function initWidget(){
	
	$("#filter-jzr").memberPicker({
		defaultlValue:memberPickerDefaultlValue,
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			querySelfRefundRecordList();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			querySelfRefundRecordList();
		}
	})
	
	
//	Commonjs.memberPicker({
//		page:'selfServiceRefundRecordList',
//		defaultlValue:memberPickerDefaultlValue,
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			querySelfRefundRecordList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			querySelfRefundRecordList();
//		}
//	});
}

function querySelfRefundRecordList(){
	var listHtml = '';
	var apiData = {};
  	apiData.CardNo = selectedMember.CardNo;
  	apiData.CardType = selectedMember.CardType;
  	apiData.MemberId = selectedMember.MemberId;
  	apiData.HisMemberId = selectedMember.HisMemberId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	$("#orderList").html('');
	$("#noData").hide();
	$("#loading").show();
	Commonjs.ajax('/wsgw/smartPay/QuerySelfRefundRecordList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
            	 if (Commonjs.ListIsNotNull(jsons.Data)) {
            		 Commonjs.BaseForeach(jsons.Data, function(i, item) { 
							listHtml += "<li class='c-list-a' onclick='queryQuerySelfRefundRecordInfo("+item.Id+",\""+getStateDesc(item.State)+"\","+item.RefundAmount+","+item.RefundCount+")'>";
							listHtml += "<div class='pm-title'><span class='fr c-ffac0b' style='padding-right:.75rem'>"+getStateDesc(item.State)+"</span>门诊预交金退款</div><ul class='visit-mess c-f14'>";
							listHtml += "<li><div class='vm-key c-666'>退款申请：</div><div class='vm-info'><span >￥" + Commonjs.centToYuan(item.RefundableBalance) + "   共"+item.RefundableCount+"笔</span></div>";
							listHtml += "</li><li> <div class='vm-key c-666'>实际到账：</div><div class='vm-info'><span class='c-ffac0b'>￥" + Commonjs.centToYuan(item.RefundAmount)+ "  共"+item.RefundCount+"笔</span></div>";
							listHtml += "</li><li><div class='vm-key c-666'>到账时间：</div><div class='vm-info'>"+item.UpdateTime+"</div> </li></ul></li>";
							
						});
            		 $("#loading").hide();
            		 $("#noData").hide();
            	 }else{
            		 $("#loading").hide();
            		 $("#noData").show();
            	 }
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
        	//$("#loading").hide();
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
		//$("#loading").hide();
		$("#orderList").append(listHtml);
    });
}

function getStateDesc(state){
	var str = '未知';
	if( state == 0 ){
		str = '未执行';
	}else if( state == 1 ){
		str = '退款中';
	}else if( state == 2 ){
		str = '全部到账';
	}else if( state == 3 ){
		str = '部分到账';
	}else if( state == 4 ){
		str = '退款失败';
	}
	return str;
}

function queryQuerySelfRefundRecordInfo(selfRefundRecordId,stateName,refundAmount,refundCount){
	location.href = Commonjs.goToUrl("selfServiceRefundRecordInfo.html?selfRefundRecordId="+selfRefundRecordId
			+"&stateName="+stateName+"&refundAmount="+refundAmount+"&refundCount="+refundCount);
}