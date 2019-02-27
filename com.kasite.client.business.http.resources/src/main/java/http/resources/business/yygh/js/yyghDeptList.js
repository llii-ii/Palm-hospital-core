var doctorUrl = "yyghDoctorList.html";
var historyDoctorUrl = "historyDocList.html";
var deptDoctorSearch = "searchDocList.html";
$(function(){
	var hosId = Commonjs.getUrlParam("HosId");
	if(Commonjs.isNull(hosId)){
		hosId = Commonjs.hospitalId();
	}
	initWidget(hosId);
	quertDept(hosId);
});

/**
 * 初始化控件
 */
function initWidget(hosId){
	$('.dept-box').height($(window).height() - $('.com-lab').height() - $('.searchhold').height() - $('.deptspace').height() - 84);
	$(window).resize(function(){
		$('.dept-box').height($(window).height() - $('.com-lab').height() - $('.searchhold').height() - $('.deptspace').height() - 84);
	});
	
	//搜索
	$('.c-main').on('click','.ser-into',function(){
		$('.ser-into').hide();
		$('.ser-main').show();
		$('.ser-input').focus();
	});
	//点击历史医生
	$('#goHistory').on('click',function(){
		if (historyDoctorUrl.indexOf('?') > 0) {
	        location.href = Commonjs.goToUrl(historyDoctorUrl + "&HosId="+hosId);
	    } else {
	        location.href = Commonjs.goToUrl(historyDoctorUrl + "?HosId="+hosId);
	    }
	})
	//点击搜索
	$('.ser-btn').on('click',function(){
		if(!Commonjs.ListIsNotNull($("#searchName").val())){
			myLayer.alert("请输入查询条件",3000);
			return;
		}
	    if (deptDoctorSearch.indexOf('?') > 0) {
	        location.href = Commonjs.goToUrl(deptDoctorSearch + "&HosId="+hosId+"&searcheName=" + escape($("#searchName").val()));
	    } else {
	        location.href = Commonjs.goToUrl(deptDoctorSearch + "?HosId="+hosId+"&searcheName=" + escape($("#searchName").val()));
	    }
	})
}
//查询科室
function quertDept(hosId){
	var apiData = {};
	apiData.HosId = hosId;
	var param = {}; 
	param.api = 'QueryClinicDept'; 
	param.apiParam = Commonjs.getApiReqParams(apiData,"",2008);
	Commonjs.ajax('../../wsgw/yy/QueryClinicDept/callApi.do?t=' + new Date().getTime(),param,function(jsons){
		if (Commonjs.ListIsNotNull(jsons)) {
			var listHtml = '';
            if (jsons.RespCode == 10000) {
                if (Commonjs.ListIsNotNull(jsons.Data)) {
                	Commonjs.BaseForeach(jsons.Data, function (i, item) {
                		listHtml += '<li ';
                		if(i == 0){
                			listHtml += 'class="curr"';
                		}
                		listHtml += ' ><a href="javascript:;" onclick="readyGoToDoctor(\''+hosId+'\',\'' + item.DeptCode + '\',\'' + item.DeptName + '\',this);">'+item.DeptName+'</a></li>';
                    });
                } else {
                	myLayer.alert("无数据",3000);
                }
                $(".dept ul").append(listHtml);
            } else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
        } else {
            //通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
        }
    });
}
function readyGoToDoctor(hosId,deptCodeParam, deptNameParam,obj) { //为了加载效果停半秒
	$(obj).parent().addClass('curr').siblings().removeClass('curr');
	setTimeout("goToDoctor('"+hosId+"','"+deptCodeParam+"','"+deptNameParam+"')",500);
}
function goToDoctor(hosId,deptCodeParam, deptNameParam){
	if (doctorUrl.indexOf('?') > 0) {
        location.href = Commonjs.goToUrl(doctorUrl + "&HosId="+hosId+"&deptCode=" + deptCodeParam + "&deptName=" + escape(deptNameParam));
    } else {
        location.href = Commonjs.goToUrl(doctorUrl + "?HosId="+hosId+"&deptCode=" + deptCodeParam + "&deptName=" + escape(deptNameParam));
    }
}
