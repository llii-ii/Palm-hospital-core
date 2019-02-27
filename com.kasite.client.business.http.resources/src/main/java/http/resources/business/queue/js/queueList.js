var cardNO = new Array();
//就诊人全局对象数据
var selectedMember ={};
$(function(){
	$(".nomess").hide();
	initWidget();
//		//筛选职称
//		$("#filter-jzr").picker({
//			title: "就诊人切换",
//			cols: [
//				{
//					textAlign: 'center',
//					values: ['小雪','小明','小包','小李','大智']
//				},
//			],
//			onChange:function($k, values, displayValues){
//				$('#jzrword').html(values);
//			},
//			onClose:function(){
//				//执行切换
//			}
//		});
//	loadData();
});
/**
 * 初始化控件
 */
function initWidget(){ 
	//就诊人控件
	$("#filter-jzr").memberPicker({
		isMustCardNo : true,//必须有就诊卡
		cardType: Commonjs.constant.cardType_1,
		pickerOnClose:function(obj,member){
			selectedMember = member;
			loadData();
		},
		ajaxSuccess:function(data,defaultMember){
			selectedMember = defaultMember;
			loadData();
		}
	});
//	Commonjs.memberPicker({
//		page:'reportIndex',
//		cardType: Commonjs.constant.cardType_1,
//		id:"filter-jzr",
//		pickerOnClose:function(obj,member){
//			selectedMember = member;
//			loadData();
//		},
//		ajaxSuccess:function(data,defaultMember){
//			selectedMember = defaultMember;
//			loadData();
//		}
//	});
}
/**
 * 加载数据
 */
function loadData(){
	var cardNo = selectedMember.CardNo;
	var memberName = selectedMember.MemberName;
	
	var apiData = {};
	apiData.CardNo=cardNo;
	apiData.CardType=Commonjs.constant.cardType_1;
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.apiParam = Commonjs.getApiReqParams(apiData);
	Commonjs.ajax('../../wsgw/queue/GetQueueInfo/callApi.do',param,function(jsons){
		if(!Commonjs.isEmpty(jsons)){
			var listHtml='';
			if(!Commonjs.isEmpty(jsons.RespCode)){
			if(jsons.RespCode==10000){
				if(Commonjs.ListIsNull(jsons.Data)){
					clinicSum=jsons.Data.length;
					Commonjs.BaseForeach(jsons.Data, function(i,item){
						var queueTip="";
						if(item.MaxNo<=0)
						{
							queueTip="尚未进入候诊队列";
						}else
						{
							var waitNum=item.No-item.MaxNo;
							queueTip="<span class=\"c-0093ff\">"+item.No+"</span>号 ";
							if (waitNum == '0'){
								queueTip += "<span class=\"c-0093ff\">已到您就诊</span>";
							}else if(waitNum < 0){
								queueTip += "<span class=\"c-0093ff\">已过就诊号序</span>";
							}else{
								queueTip += "还需等待<span class=\"c-0093ff\">"+waitNum+"</span>人";
							}
							
						}
						var url = '/business/queue/queueDetail.html?DoctorName='
						+escape(item.DoctorName)+'&DeptName='+escape(item.DeptName)
						+'&Location='+escape(item.Location)+"&No="+item.No
						+'&MaxNo='+item.MaxNo+'&QueryId='+item.QueryId
						//+'&CardNo='+cardNO[$("#filter-jzr").val()]
						+'&CardNo='+cardNo
						+'&MemberName='+escape(memberName)+'&IfMsg='+item.IfMsg
						+'&ReMindNo='+item.ReMindNo+'&RegisterDate='+item.RegisterDate
						+'&TimeId='+item.TimeId+'&PhoneNo='+item.PhoneNo+'&NextNo='+item.NextNo
						+'&CardType='+item.CardType;
						
						var temp="<li><a href=\"javascript:location.href=Commonjs.goToUrl('"+url+"');\">"
						+" <div class=\"wl-top\"><i class=\"sicon icon-wait\"></i>候诊信息</div>"
						+"<ul class=\"visit-mess c-f13\"><li><div class=\"vm-key c-999\">科室</div><div class=\"vm-info\">"+item.DeptName+"</div></li>"
						+"<li><div class=\"vm-key c-999\">医生</div><div class=\"vm-info\">"+item.DoctorName+"</div></li>"
						+"<li><div class=\"vm-key c-999\">您的候诊号序</div><div class=\"vm-info\">"+queueTip+"</div></li></ul>";
						
						listHtml=listHtml+temp;
						
					});
					$(".wait-list").html(listHtml);	
				}else
				{
					$(".nomess").show();
				}	
					
			}
			else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
			}
		}
		else{
			//通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
		}
    });
}

/*
function queryMemberList(){
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var page = {};
	page.PIndex =0;
	page.PSize = 0;
	var param = {};
	param.apiParam =Commonjs.getApiReqParams(apiData,page);
	Commonjs.ajax('../../wsgw/basic/QueryMemberList/callApi.do',param,function(jsons){
		if(!Commonjs.isEmpty(jsons)){
			if(!Commonjs.isEmpty(jsons.RespCode)){
			if(jsons.RespCode==10000){
				var menberIDs = new Array();
				var menberNames = new Array();
				menberNameArr = new Array();
				cardNO = new Array();
				if(Commonjs.ListIsNotNull(jsons.Data)){
//					if(jsons.Data.length==undefined){
//						menberIDs[0] = jsons.Data.MemberId,
//						menberNames[0] = jsons.Data.MemberName,
//						menberNameArr[jsons.Data.MemberId] = jsons.Data.MemberName,
//						cardNO[jsons.Data.MemberId] = jsons.Data.CardNo;
//					}else{
						Commonjs.BaseForeach(jsons.Data, function(i,item){
								menberIDs[i] = item.MemberId,
								menberNames[i] = item.MemberName,
								menberNameArr[item.MemberId] = item.MemberName,
								cardNO[item.MemberId] = item.CardNo;
						});
						
//					}
					$("#filter-jzr").picker({
						title: "就诊人切换",
						cols: [
							{
						textAlign: 'center',
						values:menberIDs,
						displayValues:menberNames
						
				}
			],
			onChange:function($k, values, displayValue){
			},
			onClose:function(){
				
				//执行切换
				$(".wait-list").empty();	
				$('#jzrword').html(menberNameArr[$("#filter-jzr").val()]);
				if(Commonjs.isEmpty(cardNO[$("#filter-jzr").val()])){
					$('.c-999').html("未绑定就诊卡");
					$(".nomess").show();
					}else{
						$(".nomess").hide();
						$('.c-999').html("就诊卡："+cardNO[$("#filter-jzr").val()]);
						loadData(cardNO[$("#filter-jzr").val()],menberNameArr[$("#filter-jzr").val()]);
				}
			}
		});
					$(".wait-list").empty();
					$("#filter-jzr").val(menberIDs[0]);
					$('#jzrword').html(menberNameArr[$("#filter-jzr").val()]);
					if(Commonjs.isEmpty(cardNO[$("#filter-jzr").val()])){
						$(".nomess").show();
					}else{
						$(".nomess").hide();
						$('.c-999').html("就诊卡："+cardNO[$("#filter-jzr").val()]);
						loadData(cardNO[$("#filter-jzr").val()],menberNameArr[$("#filter-jzr").val()]);
					}
				}else
					{$(".nomess").show();}
			}
			else {
            	myLayer.alert(jsons.RespMessage,3000);
            }
			}
			else{
				myLayer.alert("返回码为空",3000);
			}
			
		}
		else{
			//通信失败
        	myLayer.alert('网络繁忙,请刷新后重试',3000);
		}
    });

}	
*/

