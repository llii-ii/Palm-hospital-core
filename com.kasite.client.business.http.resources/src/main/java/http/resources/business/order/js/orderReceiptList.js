var loading = false;
//被选中用户的全局变量
var selectedMember = {};

//复选框选中的hisorderid
var selecedReceiptNames = "";
var selecedReceiptNos = "";
var selecedReceiptPrices="";
var selecedRegID ="";
//复选框选中数
var selectedCount = 0;
//选中金额总数
var selectedMoney = 0;
$(function(){
    initWidget();
});

/**
 * 初始化控件
 */
function initWidget(){

    $("#noData").show();


    $("#filter-jzr").memberPicker({
        cardType: Commonjs.constant.cardType_1,
        pickerOnClose:function(obj,member){
            selectedMember = member;
            //初始化多选相关控件数据
            selectedMoney = 0;
            selectedCount = 0;
            selecedReceiptNos = "";
            selecedReceiptNames = "";
            selecedReceiptPrices="";
            $("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
            $("#orderNum").html(selectedCount);
            queryUnsettledReceiptList();
        },
        ajaxSuccess:function(data,defaultMember){
            selectedMember = defaultMember;
            queryUnsettledReceiptList();
        }
    });

//	Commonjs.memberPicker({
//		page:'orderReceiptList',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			//初始化多选相关控件数据
//			selectedMoney = 0;
//			selectedCount = 0;
//			selecedReceiptNos = "";
//			selecedReceiptNames = "";
//			selecedReceiptPrices="";
//			$("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
//			$("#orderNum").html(selectedCount);
//			queryUnsettledReceiptList();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			queryUnsettledReceiptList();
//		}
//	});

    //选择
    $('.c-main').on('click','.input-checkbox',function(){
        var checked = $(this).find('.input-pack').hasClass('checked');
        var all = $(this).attr('data-check') == 'all';
        if(checked){
            $(this).find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
            if(all){
                $('div[data-check=check]').find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
            }else{
                ischeckAll();
            }
        }else{
            $(this).find('.input-pack').addClass('checked').find('input').attr('checked','true');
            if(all){
                $('div[data-check=check]').find('.input-pack').addClass('checked').find('input').attr('checked','true');
            }else{
                ischeckAll();
            }
        }
        selectedMoney = 0;
        selectedCount = 0;
        selecedReceiptNos = "";
        selecedRegID = "";
        selecedReceiptNames = "";
        selecedReceiptPrices ="";
        $('div[data-check=check]').find('.input-pack').each(function(i){
            if($(this).find('input').attr('checked')){
                selectedMoney += Number($(this).parent().parent().find("span[data-info='1']").attr("data-money"));
                var receiptNo = $(this).parent().parent().find("span[data-info='1']").attr("data-receiptNo")+ ",";
                var regID = $(this).parent().parent().find("span[data-info='1']").attr("data-regID")+ ",";
                var receiptName = $(this).parent().parent().find("span[data-info='1']").attr("data-receiptName")+ ",";
                var receiptPrices = $(this).parent().parent().find("span[data-info='1']").attr("data-money")+ ",";
                if( !Commonjs.isEmpty(receiptNo) ){
                    selecedReceiptNos +=receiptNo;
                    selecedRegID +=regID;
                    selecedReceiptNames +=receiptName;
                    selecedReceiptPrices +=receiptPrices;
                }
                selectedCount++;
            }
        });
        selecedReceiptNos = selecedReceiptNos.substring(0,selecedReceiptNos.length-1);
        selecedRegID = selecedRegID.substring(0,selecedReceiptNos.length-1);
        selecedReceiptNames = selecedReceiptNames.substring(0,selecedReceiptNames.length-1);
        selecedReceiptPrices = selecedReceiptPrices.substring(0,selecedReceiptPrices.length-1);
        $("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
        $("#orderNum").html(selectedCount);
    });

    $('.c-main').on('click','.hasmore',function(){
        $(this).toggleClass('ashow').find('.ui-grid').toggle();
    });
}

/**
 * 查询订单列表
 */
function queryUnsettledReceiptList(){

    var listHtml = '';
    var apiData = {};
    var page = {};
    apiData.CardNo = selectedMember.CardNo;
    apiData.CardType = selectedMember.CardType;
    apiData.MemberId = selectedMember.MemberId;
    var param = {};
    param.apiParam = Commonjs.getApiReqParams(apiData,page,"");
    $("#orderList").html("");
    $("#totalMoney").html(Commonjs.centToYuan(selectedMoney));
    $("#orderNum").html(selectedCount);
    $("#noData").hide();
    $("#loading").show();
    Commonjs.ajax('/wsgw/order/QueryUnsettledOrderReceiptList/callApi.do?t=' + new Date().getTime(),param,function(jsons){
        if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                    Commonjs.BaseForeach(jsons.Data, function (i, data) {
                        listHtml += '<li>';
                        listHtml += '<div class="input-checkbox" data-check="check">';
                        listHtml += '<div class="input-pack mr5">';
                        if(data.IsSettlement == 0){
                            listHtml += '<i class="sicon"></i>';
                            listHtml += '<input type="checkbox" name="n1" >';
                        }
                        listHtml += '</div>';
                        listHtml += '</div>';
                        listHtml += '<a href="javascript:void(0)" class="c-arrow-r hasmore">';
                        listHtml += '<h4><span class="fr c-ffac0b">';
                        if(data.IsSettlement == '1'){
                            listHtml += '已结算';
                        }else{
                            listHtml += '末结算';
                        }
                        listHtml += '</span>'+data.ReceiptName+'</h4>';
                        listHtml += '<div class="ui-grid ol-mess" style="display:none;">';
                        listHtml += '<table class="charges-detail"><thead>'
                        listHtml += '<tr><td width="20%">项目</td><td width="20%">规格</td><td width="20%">数量</td><td width="20%">单价(元）</td>';
                        listHtml += '<td width="20%">金额(元）</td></tr></thead>';
                        listHtml += '<tbody>';
                        if(Commonjs.ListIsNotNull(data.ItemList)  ){
                            Commonjs.BaseForeach(data.ItemList, function (i, item) {
                                listHtml += '<tr>';
                                listHtml += 	'<td>'+item.Project+'</td>';
                                listHtml +=  	'<td>'+item.Specifications+'</td>';
                                listHtml += 	'<td>'+item.Number+'</td>';
                                listHtml += 	'<td>'+Commonjs.centToYuan(item.UnitPrice)+'</td>';
                                listHtml += 	'<td>'+Commonjs.centToYuan(item.SumOfMoney)+'</td>';
                                listHtml +=  '</tr>';
                            });
                        }else{
                            listHtml += '<tr><td colspan="5">无</td></tr>';
                        }
                        listHtml += '</tbody></table>';
                        listHtml += '</div>';
                        listHtml += ' <div class="ol-bot">';
                        listHtml += '	<span class="fr">小计：<span class="c-ffac0b">￥'+Commonjs.centToYuan(data.TotalPrice)+'</span></span>';
                        listHtml += ' 	<span class=""c-999" data-receiptName='+data.ReceiptName+' data-money="'+data.TotalPrice+'" data-receiptNo="'+data.ReceiptNo+'" data-regID="'+data.RegId+'"  data-info="1">'+data.ReceiptTime+'</span>';
                        listHtml += ' </div>';
                        listHtml += '</a>';
                        listHtml += ' </li>';
                    });
                    $("#noData").hide();
                }
            } else {
                //$("#loading").hide();
                myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
            //$("#loading").hide();
            myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
        $("#loading").hide();
        $("#orderList").append(listHtml);

    });
}

function settleNow(){
    if(Commonjs.isEmpty(selectedMoney) || selectedMoney == 0){
        myLayer.alert("请选择订单",3000);
        return;
    }
    myLayer.confirm({
        title:'',
        con:'是否进行结算?',
        cancel: function(){
        },
        cancelValue:'\u53d6\u6d88',
        ok: function(){
            settlePay();
        },
        okValue:'\u786e\u5b9a'
    });
}

//是否全选
function ischeckAll(){
    var isall = true;
    $('div[data-check=check]').each(function() {
        if(!$(this).find('.input-pack').hasClass('checked')){
            isall = false;
        }
    });
    if(isall){
        $('div[data-check=all]').find('.input-pack').addClass('checked').find('input').attr('checked','true');
    }else{
        $('div[data-check=all]').find('.input-pack').removeClass('checked').find('input').removeAttr('checked');
    }
}

function settlePay(){
    var apiData = {};
    var param = {};
    apiData.CardNo=selectedMember.CardNo;
    apiData.CardType=selectedMember.CardType;
    apiData.MemberId = selectedMember.MemberId;
    apiData.TotalPrice = selectedMoney;
    apiData.ReceiptNos = selecedReceiptNos;
    apiData.ReceiptHisRegId = selecedRegID;
    apiData.ReceiptNames = selecedReceiptNames;
    apiData.ReceiptPrices = selecedReceiptPrices;
    param.apiParam = Commonjs.getApiReqParams(apiData);
    Commonjs.ajax("/wsgw/order/MergeSettledPayReceipt/callApi.do?t=" + new Date().getTime(),param,function(jsons){
        if (Commonjs.ListIsNotNull(jsons)) {
            if (jsons.RespCode == 10000) {
                var url =  "/business/pay/payment.html?orderId="+jsons.Data[0].OrderId+"&toUrl=/business/pay/paySuccess.html";
                window.location.href = Commonjs.goToUrl(url);;
            }else{
                myLayer.alert(jsons.RespMsg,3000);
            }
        } else {
            myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}