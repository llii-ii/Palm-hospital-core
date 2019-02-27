var toType;
var toUrl;
$(function(){
	toType = Commonjs.getUrlParam("toType");
	if(toType=="yygh"){//预约挂号
		toUrl = "/business/yygh/yyghDeptList.html";
	}else if(toType=="zjpb"){//专家排班
		toUrl = "http://www.fyey.cn/NewsClass_View.aspx";
	}else if(toType=="yygk"){//医院概况
		toUrl = "/business/hospitalInfo/hospitalIntro.html";
	}else if(toType=="ksjs"){//科室介绍
		toUrl = "/business/dept/deptList.html";
	}else if(toType=="lydh"){//来院导航
		toUrl = "/business/hospitalInfo/hospitalMap.html";
	}else if(toType=="zyqd"){//住院清单
		toUrl = "/business/inHospital/hospitalCostDaily.html";
	}
	loadHospitalDistrict();
})


function loadHospitalDistrict(){
	var apiData = {};
	var param = {};
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	var url = '../../wsgw/basic/QueryHospitalListLocal/callApi.do';
	var d = Commonjs.ajax(url,param,function(d){
		var html = '';
		if(d.RespCode == 10000){
			var data = d.Data;
			if(Commonjs.isEmpty(data)){
				myLayer.alert("加载失败，没有找到医院信息!");
			}else{
				var html = '';
				$.each(data,function(i,obj){
					var url;
					if(!Commonjs.isEmpty(obj.PhotoUrl)){
						var urlObj = JSON.parse(obj.PhotoUrl);
						if(!Commonjs.isEmpty(urlObj.photo1)){
							url = urlObj.photo1;
						}else if(!Commonjs.isEmpty(urlObj.photo2)){
							url = urlObj.photo2;
						}else if(!Commonjs.isEmpty(urlObj.photo3)){
							url = urlObj.photo3;
						}
					}
					var goToUrl = '';
					if(toType=='zjpb'){
						if(obj.HosId=='F0A7F830074B434BA698B1E3222F2CA9'){
							goToUrl = toUrl+"?&Id=1134";
						}else if(obj.HosId=='9564B7D7ADEC438C8D4EC50D21D5F9A1'){
							goToUrl = toUrl+"?&Id=1130";
						}
						html += '<div style="text-align: center; margin-top: 5%">';
						html += '<a href="'+goToUrl+'">';
						html += '<img src="'+url+'" style="width:212px;height:128px;">';
						html += '</a>';
						html += '</div>';
						html += '<div style="text-align: center; margin-top: 2%">';
						html += '<a href="'+goToUrl+'"><h3>'+obj.HosName+'</h3></a>';
						html += '</div>';
					}else{
						if(toUrl.indexOf('?')>=0){
							goToUrl = toUrl + '&HosId='+obj.HosId;
						}else{
							goToUrl = toUrl + '?HosId='+obj.HosId;
						}
						html += '<div style="text-align: center; margin-top: 5%">';
						html += '<a href="'+goToUrl+'">';
						html += '<a href="javascript:location.href=Commonjs.goToUrl(\''+goToUrl+'\');">';
						html += '<img src="'+url+'" style="width:212px;height:128px;">';
						html += '</a>';
						html += '</div>';
						html += '<div style="text-align: center; margin-top: 2%">';
						html += '<a href="javascript:location.href=Commonjs.goToUrl(\''+goToUrl+'\');"><h3>'+obj.HosName+'</h3></a>';
						html += '</div>';
					}
				});
				$("#hosDistrictDiv").html(html)
			}
		}else{
			myLayer.alert(d.RespMessage);
		}
	});
}