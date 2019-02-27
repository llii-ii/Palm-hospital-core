$(function(){
	loadData();
	$("#opPayMoneyLimit").keyup(function(){
		var val = $(this).val();
		val= val.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		val =val.replace(/^\./g,""); //验证第一个字符是数字
		val= val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		val= val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
		$(this).val(val);    
	});
	
	$("#ihPayMoneyLimit").keyup(function(){
		var val = $(this).val();
		val= val.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		val =val.replace(/^\./g,""); //验证第一个字符是数字
		val= val.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		val= val.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		val = val.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
		$(this).val(val);    
	});
	
	$("#savePayConfigBtn").on('click',function(){
		var opPayMoneyLimit = 0;
		if(Commonjs.isEmpty($("#opPayMoneyLimit").val())){
			Commonjs.alert("门诊预交金不能为空！");
			return ;
		}
		opPayMoneyLimit = $("#opPayMoneyLimit").val()*100;
		var ihPayMoneyLimit = 0;
		if(Commonjs.isEmpty($("#ihPayMoneyLimit").val())){
			Commonjs.alert("住院预交金不能为空！");
			return ;
		}
		ihPayMoneyLimit = $("#ihPayMoneyLimit").val()*100;
		var Service = {};
		Service.OpPayMoneyLimit = opPayMoneyLimit;
		Service.IhPayMoneyLimit = ihPayMoneyLimit;
		var param = {};
		param.reqParam = Commonjs.getApiReqParams(Service);
		var artLoading=art.dialog({lock: true, padding:'25px 60px 5px 60px', content: '<img src="../widget/artDialog/4.1.7/images/loading.gif" class="mr10" />正在请求…', tips:true});
		Commonjs.ajaxSync('/channel/updatePayConfig.do', param, function(data){
			artLoading.close();
			if( data.RespCode == 10000 ){
				art.dialog({
					lock : true,
					artIcon : "info",
					opacity : 0.4,
					width : 250,
					title : '提示',
					content : "修改成功！",
					ok : function() {
						
					}
				});			
			}else{	
				Commonjs.alert(data.RespMessage);
			}
		});
	});
});

function loadData(){
	var Service = {};
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/channel/queryPayConfig.do', param, function(resp){
		var configInfo = resp.Data[0];
		if(resp.RespCode == 10000 ){
			$("#opPayMoneyLimit").val(Commonjs.centToYuan(configInfo.OpPayMoneyLimit));
			$("#ihPayMoneyLimit").val(Commonjs.centToYuan(configInfo.IhPayMoneyLimit));
		}else{
			Commonjs.alert(data.RespMessage);	
		}
	});
}




