var param1,param2;
var ruleId = null;
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
    
    var da =decodeURI(decodeURI(Commonjs.getUrlParam('param1')));
    var da2 =decodeURI(Commonjs.getUrlParam('param2'));
    var a = eval('(' + da + ')'); 
    var b = eval('(' + da2 + ')'); 
    loadOrderRule(a);
    loadYuYueRule(b);
})
function loadOrderRule(data){
	if(data==null||data==undefined||data==""||data=={}){
		var param = {};
		param.state = 1;
		Commonjs.ajax('../../../yyRule/queryRule.do',param,function(d){
			data = d.result;
		});
	}
	param1=data;
	$("#startDay").html(data.startDay);
	$("#startTime").html(data.startTime);
	$("#endDay").html(data.endDay);
	$("#endTime").html(data.endTime);
	$("#drawPoint").html(data.srawPoint);
	$("#amTakeNum").html(data.amTakeNum);
	$("#pmTakeNum").html(data.pmTakeNum);
	$('#breachDay').html(data.breachDay);
	$('#breachTimes').html(data.breachTimes);
	$('#numbernDes').html(data.numberDes);
	ruleId = data.ruleId;
	if(data.state ==1){
		$('#wyxzgz').attr("checked",true);
		$('#wyxzgzRuleSwitch').attr("class","my-switch-box");
	}else{
		$('#wyxzgz').attr("checked",false);
		$('#wyxzgzRuleSwitch').attr("class","my-switch-box red");
	}
}
function loadYuYueRule(data){
	if(data==null||data==undefined||data==""||data=={}){
		//Service.HosId = Commonjs.hospitalId;
		var param = {};
		Commonjs.ajax('../../../yyRule/queryLimit.do',param,function(d){
			data = d.result;
		});
	}
	param2=data;
	for(var i=0;i< data.length;i++){
		var textMessage = data[i].textMessage.split(",");
		var countNum = data[i].countNum;
		if(data[i].state == 1){
    		$('#xhgz').attr("checked",true);
    		$('#xhgzRuleSwitch').attr("class","my-switch-box");
    	}else{
    		$('#xhgz').attr("checked",false);
    		$('#xhgzRuleSwitch').attr("class","my-switch-box red");
    	}
		$("#gz").append("<tr><td class=\"t\" >"+textMessage[0]+"</td><td><span class=\"ml5 mr5 c-green\" />"+countNum+"</span></td><td>"+textMessage[1]+"</td></tr>");
	}
}
function edit(){
	var a = encodeURI(encodeURI(Commonjs.jsonToString(param1)));
	var b = encodeURI(encodeURI(Commonjs.jsonToString(param2)));
	//Commonjs.alert(a+"\n"+b);
	window.location.href='rule-edit.html?param1='+a+'&param2='+b;
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
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}