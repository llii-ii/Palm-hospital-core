/// <summary>
/// 科室选择页面JS
/// </summary>

var firstDept, secDept;
var deptUrl = './deptInfo.html';
var hosId;
function loaded() {
	firstDept = new iScroll('divFirstDept', {
		checkDOMChanges : true
	});
	secDept = new iScroll('divSecond', {
		checkDOMChanges : true
	});
}
document.addEventListener('DOMContentLoaded', loaded, false);
function secDeptshow(sid, parentDeptCode, parentDeptName) {

	GetSecondDept(sid, parentDeptCode, parentDeptName);

}

$(function() {
	hosId =  Commonjs.getUrlParam("HosId");
	GetFirstDept();
})

/// <summary>
/// 获取一级科室列表
/// </summary>
function GetFirstDept() {
	var param = {};
	var apiData = {};
	apiData.HosId = hosId;
	param.api = '';
	param.apiParam = Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryBaseDeptLocal/callApi.do';
	Commonjs.ajax(url, param, function(d) {
		if (d.RespCode == 10000) {
			initFirstDeptHtml(d.Data);//初始化页面
		} else {
			alert(d.RespMessage);// 错误提示
		}
	});
}

function initFirstDeptHtml(deptData) {
	var listHtml = '';
	if (Commonjs.ListIsNull(deptData)) {
		Commonjs.BaseForeach(deptData, function(i, item) {
			if (item.DeptName.indexOf('东海') != -1) {
				if(item.DeptName.indexOf('东海门') != -1){
					item.DeptName = item.DeptName.substr(3) + "(东海)";
				}else{
					item.DeptName = item.DeptName.substr(2) + "(东海)";
				}
			} else {
				if(item.DeptName.indexOf('门') != -1){
					item.DeptName = item.DeptName.substr(1) + "(钟楼)";
				}else{
					item.DeptName = item.DeptName + "(钟楼)";
				}
			}
			var url;
			if (deptUrl.indexOf('?') > 0) {
				url = deptUrl + '&HosId='+hosId+'&deptCode='+ escape(item.DeptCode) + '&deptName='+ escape(item.DeptName);
			} else {
				url = deptUrl + '?HosId='+hosId+'&deptCode='+ escape(item.DeptCode) + '&deptName='+ escape(item.DeptName); 
			}
			listHtml += '<li><a href="javascript:location.href=Commonjs.goToUrl(\'' + url +'\');">';
			listHtml += item.DeptName;
			listHtml += '<div class="icon cdeptjt"></div></a></li>';
		});
	} else {
		//无数据
		$("#divNoMess").show();
	}
	//赋值科室列表
	$("#divFirstDept ul").html(listHtml);
}
/// <summary>
/// 获取二级科室列表
/// </summary>
function GetSecondDept(sid, parentDeptCode, parentDeptName) {
	//数据定义
	var page = {};
	var Service = {};
	var code = 1001;
	Service.ParentDeptCode = parentDeptCode;
	Service.HosId = Commonjs.hospitalId;
	var model = 'hos';
	page.PIndex = 0;
	page.PSize = 99;
	var params = Commonjs.getParams(code, Service, page);//
	//var url = Commonjs.getUrl(model)+ "?t=" + new Date().getTime();//访问地址ַ
	var url = "../hos_queryBaseDept.do" + "?t=" + new Date().getTime();
	var param = {};
	param.Api = 'QueryDept';
	param.Params = Commonjs.jsonToString(params);//参数组装
	alertLoad('正在加载');
	//ajax 同步请求
	//		var d = Commonjs.ajax(url,param,true);
	//		alertClose(); //关闭提示
	//		if(d.RespCode == 10000){
	//			initSecondDeptHtml(d.Data,parentDeptCode,parentDeptName,sid)//初始化页面
	//		}else{
	//			alert(d.RespMessage)//错误提示
	//		}			
	// ajax 同步请求
	var d = Commonjs.ajax(url, param, function(d) {
		if (d) {
			if (d.RespCode == 10000) {
				initSecondDeptHtml(d.Data, parentDeptCode, parentDeptName, sid)//初始化页面
			} else {
				alert(d.RespMessage);// 错误提示
			}
		}
	});

}

function initSecondDeptHtml(secondDeptData, parentDeptCode, parentDeptName, sid) {
	var listHtml = '';
	if (ListIsNull(secondDeptData)) {
		BaseForeach(secondDeptData, function(i, item) {
			listHtml += '<li>';
			if (deptUrl.indexOf('?') > 0) {
				//listHtml += '<a href="' + deptUrl + '&' + AdditionalParam + '&deptCode=' + escape(item.DeptCode) + '&deptName=' + escape(item.DeptName) + '">';
				listHtml += '<a href="' + deptUrl + '&deptCode='
						+ escape(item.DeptCode) + '&deptName='
						+ escape(item.DeptName) + '">';
			} else {
				//listHtml += '<a href="' + deptUrl + '?' + AdditionalParam + '&deptCode=' + escape(item.DeptCode) + '&deptName=' + escape(item.DeptName) + '">';
				listHtml += '<a href="' + deptUrl + '?deptCode='
						+ escape(item.DeptCode) + '&deptName='
						+ escape(item.DeptName) + '">';
			}
			listHtml += item.DeptName;
			listHtml += '</a>';
			listHtml += '</li>';
		});
		//赋值科室列表
		$("#divSecond ul").html(listHtml);

		$("#divFirstDept ul a").removeClass("hit");
		$(sid).addClass("hit");
		$("#divSecond").show();
		$("#divSecond").animate({
			"right" : "0"
		}, 200);
		$("#divFirstDept").width("45%");

		secDept.refresh();
	} else {
		//没有数据
		if (deptUrl.indexOf('?') > 0) {
			//location.href = deptUrl + "&" + AdditionalParam + "&deptCode=" + escape(parentDeptCode) + "&deptName=" + escape(parentDeptName);
			location.href = Commonjs.goToUrl(deptUrl + "&deptCode=" + escape(parentDeptCode)
					+ "&deptName=" + escape(parentDeptName));
		} else {
			//location.href = deptUrl + "?" + AdditionalParam + "&deptCode=" + escape(parentDeptCode) + "&deptName=" + escape(parentDeptName);
			location.href = Commonjs.goToUrl(deptUrl + "?deptCode=" + escape(parentDeptCode)
					+ "&deptName=" + escape(parentDeptName));
		}
	}
}