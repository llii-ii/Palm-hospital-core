var memberInfo = {};
var flag = Commonjs.getUrlParam("flag");
var deptCode = Commonjs.getUrlParam("deptCode");
var doctorCode = Commonjs.getUrlParam("doctorCode");
var stime=Commonjs.getUrlParam("stime");
var disabledCheckBox = false;
var toCardListUrl = "cardList.html";
var callBackUrl;

$(function() {
			callBackUrl = Commonjs.getUrlParam('callBackUrl');
			memberInfo.memberId = Commonjs.getUrlParam('memberId');
			memberInfo.memberName = unescape(Commonjs.getUrlParam('memberName'));
			memberInfo.mobile = Commonjs.getUrlParam('mobile');
			memberInfo.cardType = Commonjs.getUrlParam('cardType');
			memberInfo.cardNo = Commonjs.getUrlParam('cardNo');
			memberInfo.idCardNo = Commonjs.getUrlParam('idCardNo');
			memberInfo.hospitalNo = Commonjs.getUrlParam('hospitalNo');
			memberInfo.isDefaultMember = Commonjs.getUrlParam('isDefaultMember');
			memberInfo.isChildren = Commonjs.getUrlParam('isChildren');
			memberInfo.certType = Commonjs.getUrlParam('certType');
			memberInfo.certNum = Commonjs.getUrlParam('certNum');
			memberInfo.guardianName = unescape(Commonjs.getUrlParam('guardianName'));
			memberInfo.guardianCertType = Commonjs.getUrlParam('guardianCertType');
			memberInfo.guardianCertNum = Commonjs.getUrlParam('guardianCertNum');
		
			initWidet();

			loadMemberData(memberInfo);

		});
/** 住院号验证 
function checkHospitalNo() {
	var apiData = {};
	apiData.MemberName = memberInfo.memberName;
	apiData.Mobile = memberInfo.mobile;
	apiData.HospitalNo = memberInfo.hospitalNo;
	apiData.IdCardNo = memberInfo.idCardNo;
	apiData.MemberId = memberInfo.memberId;
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/basic/CheckHospitalNo/callApi.do', param,
			function(resp) {
				if (resp.RespCode == 10000) {// 不存在则置空
					memberInfo.hospitalNo = "";
				}
			});
}
*/

function goCardList(){ 
	if (memberInfo.cardType == 1 && !Commonjs.isEmpty(memberInfo.cardNo)) {
		var memberObj = memberInfo.cardNo+","+memberInfo.cardType+","+encodeURIComponent(memberInfo.memberName)+","+memberInfo.memberId;
		window.location.href = Commonjs.goToUrl(toCardListUrl+"?memberInfo="+memberObj+"&type=1'");
	} else {
		return;
	}
}

function go() {
	if (memberInfo.cardType == 1 && !Commonjs.isEmpty(memberInfo.cardNo)) {
		var memberObj = memberInfo.cardNo+","+memberInfo.cardType+","+encodeURIComponent(memberInfo.memberName)+","+memberInfo.memberId;
		window.location.href = Commonjs.goToUrl('/business/outpatientDept/outpatientCardAccount.html?memberInfo='+memberObj+'&type=1');
	} else {
		return;
	}

}

/**
 * 初始化控件，事件等
 */
function initWidet() {
	// 自定义复选
	$('#setDefault').on('click', function() {
		if(disabledCheckBox){
			return;
		}
		disabledCheckBox = true;
		var checked = $(this).find('.input-pack').hasClass('checked');
		var apiData = {};
		apiData.MemberId = memberInfo.memberId;
		if (!checked) {// 设置默认就诊人
			apiData.IsDefaultMember = 1;
		} else {// 取消默认就诊人
			apiData.IsDefaultMember = 0;
		}
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('../../wsgw/basic/SetDefaultMember/callApi.do',param,function(dd){
			if(dd.RespCode==10000){
				$('.input-pack').toggleClass('checked');
				if($('.input-pack').hasClass('checked')){
					$('#isDefault').attr('checked', true);
				}else{
					$('#isDefault').attr('checked', false);
				}
			}
		});
		disabledCheckBox = false;
	});

	// 解除提示
	$('#unbind').on('click', function() {
		myLayer.confirm({
			con : '是否要解除绑定？',
			cancel : function() {
			},
			ok : function() {
				var apiData = {};
				apiData.MemberId = memberInfo.memberId;
				var param = {};
				param.apiParam = Commonjs.getApiReqParams(apiData);
				Commonjs.ajax('../../wsgw/basic/DelMemberInfo/callApi.do', param,
						function(resp) {
							if (!Commonjs.isEmpty(resp.RespCode)) {
								if (resp.RespCode == 10000) {
									myLayer.alert("解绑成功！", 1500, function() {
										window.location.href = Commonjs.goToUrl('/business/member/memberList.html');
									});
								} else {
									myLayer.alert(resp.RespMessage, 3000);
								}
							}

						});
				// //执行解绑
				// myLayer.tip({
				// con:'住院号已失效，患者已出院',
				// });
			}
		});
	});

	// 住院号充值页面跳转
	$('#bindHospitalNo').on('click', function() {
		if (!Commonjs.isEmpty(memberInfo.hospitalNo)){
			var memberObj = memberInfo.hospitalNo+","+Commonjs.constant.cardType_14+","+encodeURIComponent(memberInfo.memberName)+","+memberInfo.memberId;
			window.location.href = Commonjs.goToUrl('/business/inHospital/inpatientAccount.html?memberInfo=' + memberObj);
		}
	});

}

function isShowGuardian(isShow){
	if(isShow){
		$("#guardian_li_name").show();
		$("#guardian_li_certtype").show();
		$("#guardian_li_certnum").show();
	}else{
		$("#guardian_li_name").hide();
		$("#guardian_li_certtype").hide();
		$("#guardian_li_certnum").hide();
	}
}

/**
 * 加载就诊人数据
 */
function loadMemberData(memberInfo) {
	$("#memberName").html(memberInfo.memberName);
//	$("#idCardNo").html(memberInfo.idCardNo);
	$("#mobile").html(memberInfo.mobile);
	$("#certType").html(Commonjs.getCertTypeName(memberInfo.certType));
	$("#certNum").html(memberInfo.certNum);
	if(memberInfo.isChildren==1){
		$("#isChildren").html('是');
		$("#guardianName").html(memberInfo.guardianName);
		$("#guardianCertType").html(Commonjs.getCertTypeName(memberInfo.certType));
		$("#guardianCertNum").html(memberInfo.guardianCertNum);
		isShowGuardian(true);
	}else{
		$("#isChildren").html('否');
		$("#guardianName").html('');
		$("#guardianCertType").html('');
		$("#guardianCertNum").html('');
		isShowGuardian(false);
	}
	//就诊卡为空时
	if (Commonjs.isEmpty(memberInfo.cardNo)) {
		$("#bindClinicCard").attr("href", "javascript:goBindClinicCard('"+memberInfo.memberId+"','"+memberInfo.cardType+"','"+memberInfo.memberName+"')");
		$("#cardNo").html('<i class="bindbq">去绑定</i>');
	} else if (memberInfo.cardType == 1 && !Commonjs.isEmpty(memberInfo.cardNo)) {
		$("#cardNo").html(memberInfo.cardNo);
		$("#bindClinicCard").attr("href", "javascript:goCardList();");
	}
	//住院号为空时
	if (Commonjs.isEmpty(memberInfo.hospitalNo)) {
		$("#bindHospitalNo").attr("href","javascript:goBindHospitalNo('"+memberInfo.memberId+"','"+memberInfo.cardType+"','"+memberInfo.memberName+"')");
		$("#hospitalNo").html('<i class="bindbq">去绑定</i>');
	} else {
		$("#hospitalNo").html(memberInfo.hospitalNo);
	}
	if (memberInfo.isDefaultMember == 1) {
		$('#setDefault').find('.input-pack').addClass('checked').find('input')
				.attr('checked', 'true');
	}
}
/**
 * 跳转到绑定就诊卡页面
 */
function goBindClinicCard(memberId,cardType,memberName){
	var url = '/business/member/bindClinicCard.html?memberId=' + memberId;
		url += "&cardType="+cardType
		url += "&memberName=" + escape(memberName);
	if(!Commonjs.isEmpty(callBackUrl)){
		url += "&callBackUrl="+callBackUrl;
	}
	if (flag == "addc") {
		url += "&deptCode=" + deptCode + "&doctorCode=" + doctorCode+ "&flag=" +flag+"&stime="+stime;
	}
	window.location.href = Commonjs.goToUrl(url);
}

/**
 * 跳转到绑定住院号页面
 */
function goBindHospitalNo(memberId,cardType,memberName){
	var url = '/business/member/bindHospitalNo.html?memberId=' + memberId;
	url += "&cardType="+cardType;
	url += "&memberName=" + escape(memberName);
	if(!Commonjs.isEmpty(callBackUrl)){
		url += "&callBackUrl="+callBackUrl;
	}
	window.location.href=Commonjs.goToUrl(url);
}