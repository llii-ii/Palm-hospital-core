//生命一个全局对象，存放全局变量，防止变量污染
var globalChannelMerch = {
		channelId:null,	
		channelInfo:null,
		channelMerchList:new Array(),
		isAdmin : Commonjs.getUrlParam('isAdmin')
}

$(function(){
	globalChannelMerch.channelId= Commonjs.getUrlParam('channelId')
	initWidget();
	
	loadChannelInfo();
	
	loadChannelMerchList();
});


/**
 * 初始化控件
 * @returns
 */
function initWidget(){
	
	$(".com-back").attr('href','channelMerchList.html?isAdmin='+globalChannelMerch.isAdmin);
	
	if(globalChannelMerch.isAdmin == 1){
		$("#editChannelBtn").show();
		$("#addChannelMerchBtn").show();
	}
	
	laydate.render({
		elem: '#payTimeLimit',
  		range: true,
  		type:'time',
  		value: '00:30 ~ 23:30',
  		format :'HH:mm'
	});
	
	laydate.render({
		elem: '#refundTimeLimit',
  		range: true,
  		type:'time',
  		value: '00:30 ~ 23:30',
  		format :'HH:mm'
	});
	
	$("#singlePriceLimitMin").keyup(function(){
		var val = $(this).val();
		val= val.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		val =val.replace(/^\./g,""); //验证第一个字符是数字
		val= val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		val= val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
		$(this).val(val);    
	});
	
	$("#singlePriceLimitMax").keyup(function(){
		var val = $(this).val();
		val= val.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		val =val.replace(/^\./g,""); //验证第一个字符是数字
		val= val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		val= val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
		$(this).val(val);    
	})
	
	//编辑渠道按钮
	$("#editChannelBtn").on('click',function(){
		if($(this).attr('editable')==1){
			var html = juicer($("#editChannelFormTmp").html(),globalChannelMerch.channelInfo);
			$("#editChannelForm").append(html);
			$(this).attr('editable',0);
			//$("#divChannelInfo").hide();
		}
	});
	
	//新增渠道商户配置
	$("#addChannelMerchBtn").on('click',function(){
		var param = {};
		param.reqParam = Commonjs.getApiReqParams({});
		//加载商户选择框
		Commonjs.ajaxSync('/channel/queryMerchConfigList.do',param,function(resp){
			var merchConfigMap = {};
			if(!Commonjs.isEmpty(resp.Data)){
				$("#selMerch").html("<option value=''>--请选择--</option>");
				$.each(resp.Data, function(index, value, array) {
					$("#selMerch").append("<option value='"+value.MerchCode+"' >"+value.MerchTypeName+"|"+value.MerchCode+"</option>");
					merchConfigMap[value.MerchCode]=value;
				});
				$("#selMerch").removeAttr("disabled");  
				$("#selMerch").change(function(){
					var merchCode = $("#selMerch").val();
					if(merchCode==''){
						$("#tdMerchAccount").html('');
						$("#tdBankName").html('');
					}else{
						$("#tdMerchAccount").html(merchConfigMap[merchCode].MerchAccount);
						$("#tdBankName").html(merchConfigMap[merchCode].BankShortName+":"+merchConfigMap[merchCode].BankNo);
					}
				});
			}else{
				Commonjs.alert("数据加载错误！");
				return ;
			}
		});
		//加载智付API选择框
		var param = {};
		param.reqParam = Commonjs.getApiReqParams({});
		Commonjs.ajaxSync('/channel/querySmartPayApi.do',param,function(apiList){
			if(!Commonjs.isEmpty(apiList.Data)){
				$("#smartPayApiList").empty();
				$.each(apiList.Data, function(index, value) {
					$("#smartPayApiList").append("<label class=\"checkbox mr40\" data-toggle=\"checkbox\">"+
	                            "<input type=\"checkbox\" name=\"channelMerchApi\" apiName="+value.Apiname+" value='"+value.Api+"'><i class=\"icon-chkbox\"></i>"+value.Apiname+"</label>");
				});
			}else{
				Commonjs.alert("数据加载错误！");
				return ;
			}
		});
		clearAddChannelMerchDialog();
		//打开弹出框
		var artBox=art.dialog({	
			lock: true,
			width: 900,
			padding:'10px 50px 20px 50px',
			title:'添加支付方式',
			content:$('#qAddpay').get(0),
			ok: function () {
				return saveChannelMerchRule(1);
			},
			cancel: true});
	});
	
}

/**
 * 加载渠道信息
 * @returns
 */
function loadChannelInfo(){
	var Service = {};
	Service.ChannelId = globalChannelMerch.channelId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/channel/querySysChannelInfo.do',param,function(resp){
		if( resp.RespCode == 10000 ){
			var data = resp.Data[0];
			globalChannelMerch.channelInfo = data;
			if(!Commonjs.isEmpty(data)){
				$("#channelName").html(data.ChannelName+"   "+data.ChannelId);
				//data.Stauts操作
				if( data.Status == 1 ){
					$("#channelStatus").html("启用中");
				}else{
					$("#channelStatus").html("禁用中");
				}
			}else{
				$("#channelName").prepend("数据加载错误！请返回渠道管理！");
			}
		}else{
			Commonjs.alert(resp.RespMessage);
		}
	});
}


/**
 * 加载渠道商户配置表
 * @returns
 */
function loadChannelMerchList(){
	var Service = {};
	Service.ChannelId = globalChannelMerch.channelId;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	var resp = Commonjs.ajaxSync('/channel/queryChannelMerchAndApi.do',param,function(resp){
		if( resp.RespCode == 10000 ){
			var list = resp.Data;
			if( !Commonjs.isEmpty(list) ){
				$.each(list, function(index, value, array) {
					value.i = index+1;
					value.isAdmin = globalChannelMerch.isAdmin;
					globalChannelMerch.channelMerchList.push(value);
					var html = juicer($("#tmp").html(),value);
					$("#channelMerchList").append(html);
				});
			}
		}else{
			Commonjs.alert(resp.RespMessage);
		}
	});
}

/**
 * 保存渠道信息
 * @returns
 */
function saveChannelInfo(){
	var status = $("#selChannelStatus").val();
	var resp = Commonjs.ajax('/channel/updateSysChannelInfo.do',{'status':status,'channelId':globalChannelMerch.channelId},false);
	if( resp.respCode != 10000 ){
		Commonjs.alert(resp.respMessage);
	}
	loadChannelInfo();
	$("#editChannelForm").empty();
	$("#editChannelBtn").attr('editable',1);
	
}

/**
 * 保存渠道商户表
 * @returns
 */
function saveChannelMerchRule(isAdd){

	var param = {};
	if($('#isEnable').hasClass('checked')) {
	    param.isEnable=1;
	}else{
		param.isEnable=0;
	}
	if(Commonjs.isEmpty($("#selMerch").val())){
		Commonjs.alert("请选择商户！");
		return false;
	}
	param.merchCode = $("#selMerch").val();
	
	if(Commonjs.isEmpty($("#payTimeLimit").val())){
		Commonjs.alert("请选择支付限制时间！");
		return false;
	}
	param.payTimeLimit = $("#payTimeLimit").val();
	
	if(Commonjs.isEmpty($("#refundTimeLimit").val())){
		Commonjs.alert("请选择退费限制时间！");
		return false;
	}

	param.refundTimeLimit = $("#refundTimeLimit").val();
	param.isCredit = $("#isCredit").val();
	param.channelId =globalChannelMerch.channelId;
	param.singlePriceLimit = $("#singlePriceLimitMin").val()+"-"+$("#singlePriceLimitMax").val();
	param.channelMerchApis = "";
	param.channelMerchApiNames = "";
	$("#smartPayApiList>label").each(function(){
		if( $(this).hasClass('checked')){
			param.channelMerchApis+=$(this).find("input").val()+",";
			param.channelMerchApiNames+=$(this).find("input").attr("apiName")+",";
		}
	});
	
	
	var artLoading=art.dialog({lock: true, padding:'25px 60px 5px 60px', content: '<img src="../../widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在请求…', tips:true});
	if(isAdd){
		var resp = Commonjs.ajax('/channel/addChannelMerchRule.do',param,true,function(data){
			artLoading.close();
			if( data.respCode == 10000 ){
				art.dialog({
					lock : true,
					artIcon : "info",
					opacity : 0.4,
					width : 250,
					title : '提示',
					content : "新增成功！",
					ok : function() {
						document.location.reload(); 
					}
				});			}else{
				Commonjs.alert(data.respMessage);
			}
		});
	}else{
		var resp = Commonjs.ajax('/channel/updateChannelMerchRule.do',param,true,function(data){
			artLoading.close();
			if( data.respCode == 10000 ){
				art.dialog({
					lock : true,
					artIcon : "info",
					opacity : 0.4,
					width : 250,
					title : '提示',
					content : "修改成功！",
					ok : function() {
						document.location.reload(); 
					}
				});
			}else{
				Commonjs.alert(resp.respMessage);
			}
		});
	}
	
	
}

function openEditChannelMerchDialog(index){
	var param = {};
	param.reqParam = Commonjs.getApiReqParams({});
	//加载商户选择框
	Commonjs.ajaxSync('/channel/queryMerchConfigList.do',param,function(resp){
		var merchConfigMap = {};
		if(!Commonjs.isEmpty(resp.Data)){
			$("#selMerch").html("<option value=''>--请选择--</option>");
			$.each(resp.Data, function(index, value) {
				$("#selMerch").append("<option value='"+value.MerchCode+"' >"+value.MerchTypeName+"|"+value.MerchCode+"</option>");
				merchConfigMap[value.MerchCode]=value;
			});
		}else{
			Commonjs.alert("数据加载错误！");
			return ;
		}
		$("#selMerch").attr("disabled","disabled"); 
	});
	//加载智付API选择框
	Commonjs.ajaxSync('/channel/querySmartPayApi.do',param,function(apiList){
		if(!Commonjs.isEmpty(apiList.Data)){
			$("#smartPayApiList").empty();
			$.each(apiList.Data, function(index, value) {
				$("#smartPayApiList").append("<label class=\"checkbox mr40\" data-toggle=\"checkbox\">"+
	                        "<input type=\"checkbox\" name=\"channelMerchApi\" apiName="+value.Apiname+" value='"+value.Api+"'><i class=\"icon-chkbox\"></i>"+value.Apiname+"</label>");
			});
		}else{
			Commonjs.alert("数据加载错误！");
			return ;
		}
	});
	//给输入框赋值
	var channelMerch = globalChannelMerch.channelMerchList[index-1];
	if(channelMerch.isEnable){
		if(!$('#isEnable').hasClass('checked')) {
			$('#isEnable').addClass('checked')
		}
	}else{
		if($('#isEnable').hasClass('checked')) {
			$('#isEnable').removeClass('checked')
		}
	}
	$("#selMerch").val(channelMerch.MerchCode);
	$("#tdMerchAccount").html(channelMerch.MerchAccount);
	$("#tdBankName").html(channelMerch.BankName+":"+channelMerch.BankNo);
	
	$("#payTimeLimit").val(channelMerch.PayTimeLimit);
	$("#refundTimeLimit").val(channelMerch.RefundTimeLimit);
	$("#isCredit").val(channelMerch.IsCredit);
	if( !Commonjs.isEmpty(channelMerch.SinglePriceLimit) ){
		$("#singlePriceLimitMin").val(channelMerch.SinglePriceLimit.split('~')[0]);
		$("#singlePriceLimitMax").val(channelMerch.SinglePriceLimit.split('~')[1]);
	}
	if(  !Commonjs.isEmpty(channelMerch.ApiList ) ){
		$.each(channelMerch.ApiList, function(index, value, array) {
			$("input[name='channelMerchApi'][value='"+value.Api+"']").parent().addClass("checked");
		});
	}
	//打开弹出框
	var artBox=art.dialog({	
		lock: true,
		width: 900,
		padding:'10px 50px 20px 50px',
		title:'添加支付方式',
		content:$('#qAddpay').get(0),
		ok: function () {
			return saveChannelMerchRule(0);
		},
		cancel: true});
}

/**
 * 清除弹出框
 * @returns
 */
function clearAddChannelMerchDialog(){
	if(!$('#isEnable').hasClass('checked')) {
		$('#isEnable').addClass('checked')
	}
	$("#selMerch").val('')
	$("#tdMerchAccount").html('');
	$("#tdBankName").html('');
	
	$("#payTimeLimit").val('00:30 ~ 23:30');
	$("#refundTimeLimit").val('00:30 ~ 23:30');
	$("#isCredit").val('1');
	$("#singlePriceLimitMin").val(1.00);
	$("#singlePriceLimitMax").val(2000.0);
}



