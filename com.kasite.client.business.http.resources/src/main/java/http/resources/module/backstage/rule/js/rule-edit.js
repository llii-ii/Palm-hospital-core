   param1 = getUrlParam('param1');
   param2 = getUrlParam('param2');
   var da = decodeURI(param1);
    var da2 = decodeURI(param2);
    
$(function(){
    $('#datetimepicker').datetimepicker({
        yearOffset:0,
        lang:'ch',
        timepicker:false,
        format:'d/m/Y',
        formatDate:'Y/m/d',
        minDate:'-1970/01/02', // yesterday is minimum date
        maxDate:'+1970/01/02' // and tommorow is maximum date calendar
    });
    
    $('#layer').click(function(){
        var artBox=art.dialog({
            lock: true,
            icon:'question',
            opacity:0.4,
            width: 250,
            title:'提示',
            content: '页面模板会覆盖编辑区域已有组件，是否继续？',
            ok: function () {
                
            },
            cancel: true
        });         
    })

 
    var a = eval('(' + da + ')'); 
    var b = eval('(' + da2 + ')'); 
    //Commonjs.alert(a.DrawPoint);
    loadOrderRule(a);
    loadYuYueRule(b);
})
function loadOrderRule(data){
	/*var Service = {};
	var page = {};
	var code = 2005;
	Service.State = 1;
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "QueryRule";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('./rule_callHopApi.do',param,false);
	var data = d.Data;*/
	$("#startDay").val(data.startDay);
	$("#startTime").val(data.startTime);
	$("#endDay").val(data.endDay);
	$("#endTime").val(data.endTime);
	$("#drawPoint").val(data.drawPoint);
	$("#amTakeNum").val(data.amTakeNum);
	$("#pmTakeNum").val(data.pmTakeNum);
	$('#breachDay').val(data.breachDay);
	$('#breachTimes').val(data.breachTimes);
	$('#numberDes').val(data.numberDes);
	if(data.state ==1){
		$('#wyxzgz').attr("checked",true);
		$('#wyxzgzSwitch').attr("class","my-switch-box");
	}else{
		$('#wyxzgz').attr("checked",false);
		$('#wyxzgzSwitch').attr("class","my-switch-box red");
	}
	ruleId=data.ruleId;
}
var yygz_num;
function loadYuYueRule(data){
	/*var Service = {};
	var page = {};
	var code = 2006;
	Service.RuleId = "";
	var param = {};
	var params = Commonjs.getParams(code,Service);//获取参数
	param.Api = "QueryLimit";
	param.Params = Commonjs.jsonToString(params);
	var d = Commonjs.ajax('./rule_callHopApi.do',param,false);
	var data = d.Data;*/
	yygz_num = data.length;
	for(var i=0;i< yygz_num;i++){
		var textMessage = data[i].textMessage.split(",");
		var countNum = data[i].countNum;
		if(data[i].state == 1){
    		$('#xhgz').attr("checked",true);
    		$('#xhgzSwitch').attr("class","my-switch-box");
    	}else{
    		$('#xhgz').attr("checked",false);
    		$('#xhgzSwitch').attr("class","my-switch-box red");
    	}
		$("#gz").append("<tr><td class=\"t\" width=\"220\">"+textMessage[0]+"</td><td width=\"285\"><div class=\"custom-subtraction ml5 mr5 clearfix\"><input type=\"hidden\" id=\"orId"+i+"\" value=\""+data[i].limitId+"\"><div class=\"fl\"><input type=\"text\" id=\"gz"+i+"\" value=\""+countNum+"\" onblur=\"validateNumber(this);\"></div><div class=\"fr\"><a class=\"add\" href=\"javascript:;\" onclick=\"add(this);\">+</a><a class=\"cut\" href=\"javascript:;\" onclick=\"cut(this);\">-</a></div></div>"+textMessage[1]+"</td></tr>");
	}
}

function SaveRuleAndLimit(){
	
	var Limits=[];
	var param = {};
	if(!check()){
		Commonjs.alert("您输入的格式有误，请核对！");
		return;
	}
	param.ruleId = ruleId;
	param.startDay=$('#startDay').val();
	param.startTime=$('#startTime').val();
	param.endDay=$('#endDay').val();
	param.endTime=$('#endTime').val();
	param.drawPoint= ($('#drawPoint').val());
	param.amTakeNum=$('#amTakeNum').val();
	param.pmTakeNum=$('#pmTakeNum').val();
	param.breachDay=$('#breachDay').val();
	param.breachTimes=$('#breachTimes').val();
	param.numberDes=$('#numberDes').val();
   	if($('#wyxzgzSwitch').attr("class")=="my-switch-box"){
   		param.state=1;
   	}else{
   		param.state=2;
   	}
   	
   	var limitState=0;
   	if($('#xhgzSwitch').attr("class")=="my-switch-box"){
   		limitState=1;
   	}else{
   		limitState=2;
   	}
   	for (var i=0;i<yygz_num; i++) {
   		var Limit={};
   		Limit.countNum=$("#gz"+i).val();
   		Limit.limitId=$("#orId"+i).val();
   		Limit.state=limitState;
   		Limits[i]=Limit;
   	}
   	param.limits=Limits;
	var paramStr = Commonjs.jsonToString(param);
	Commonjs.ajax('../../../yyRule/updateRuleAndLimit.do',paramStr,function(){
		if(d.RespCode == 10000){
			window.location.href='rule.html';
		}else{
			Commonjs.alert(d.RespMessage);
			return;
		}
	});
}
function cancel(){
	window.location.href='rule.html?param1='+param1+'&param2='+param2;
}
function check(){
	if(isNaN($('#startDay').val()))return false;
	if(isNaN($('#endDay').val()))return false;
	if(isNaN($('#breachDay').val()))return false;
	if(isNaN($('#breachTimes').val()))return false;
	for (var i=0;i<yygz_num; i++) {
		if(isNaN($('#gz'+i).val()))return false;
   	}
	if(!isHHMM($('#startTime').val()))return false;
	if(!isHHMM($('#endTime').val()))return false;
	if(!isHHMM($('#amTakeNum').val()))return false;
	if(!isHHMM($('#pmTakeNum').val()))return false;
	return true;
}
function add(obj){
	var b = $(obj).parents().prev('div').children('input');
	if(isNaN(b.val())){
		return;
	}else{
		b.val(Number(b.val())+1);
	}
}
function cut(obj){
	var b = $(obj).parents().prev('div').children('input');
	if(isNaN(b.val())||Number(b.val())<1){
		return;
	}else{
		b.val(Number(b.val())-1);
	}
}
function validateNumber(obj){
	if(isNaN($(obj).val())){
		Commonjs.alert("只能为数字，请重新输入！");
	}
	return;
}
function validateHHMM(obj){
	//hh:mm 08:05
	var time_hm=/^(0\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/;  
	if(!time_hm.test($(obj).val())){
	   Commonjs.alert("抱歉，您输入的日期格式有误，正确格式例如:08:05");
	}
	return;
}
function validateHHMMAM(obj){
	//hh:mm 08:05
	var time_hm=/^(0\d{1}|1[0-1]):([0-5]\d{1})$/;  
	if(!time_hm.test($(obj).val())){
	   Commonjs.alert("抱歉，请输入正确的上午时间，正确格式例如:08:05");
	}
	return;
}
function validateHHMMPM(obj){
	//hh:mm 14:05
	var time_hm=/^(1[2-9]|2[0-3]):([0-5]\d{1})$/;  
	if(!time_hm.test($(obj).val())){
	   Commonjs.alert("抱歉，请输入正确的下午时间，正确格式例如:14:05");
	}
	return;
}
function isHHMM(str){
	var time_hm=/^(0\d{1}|1\d{1}|2[0-3]):([0-5]\d{1})$/;  
	return time_hm.test(str);
}
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
function myPrint(msg){
	art.dialog({
		lock : true,
		artIcon : 'error',
		opacity : 0.4,
		width : 250,
		title : '提示',
		time : 3,
		content : msg,
		ok : function() {

		}
	});			
}