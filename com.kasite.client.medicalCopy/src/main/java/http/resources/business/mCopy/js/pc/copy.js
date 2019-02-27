var selectNum = parseInt(0);
var selectDate = '';
var ids = '';
$(function(){
	getMCopyList();
	$('.modals').bind('keypress', function (event) {
        if (event.keyCode == "13") {
        	openExpress(+1);
        }
	});
	$('#name').bind('keypress', function (event) {
        if (event.keyCode == "13") {
        	refresh();
        }
	});
	$('#courierNumber').bind('keypress', function (event) {
        if (event.keyCode == "13") {
        	refresh();
        }
	});
	$('#patientId').bind('keypress', function (event) {
        if (event.keyCode == "13") {
        	refresh();
        }
	});
	$('#payState').change(function(){  
		refresh();
	}); 
	$('#orderState').change(function(){  
		refresh();
	}); 

});


//获得快递单列表
function getMCopyList(){
	$('#orderTable').bootstrapTable({
		url: '/wsgw/medicalCopy/GetExpressOrderList/callApi.do',  //请求后台的URL（*）
        method: 'POST',                      //请求方式（*）
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        pagination: true,                   //是否显示分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 100,                     //每页的记录行数（*）
        queryParams : function (params) {
        	var apiData = {};	
        	apiData.name = $("#name").val();
        	apiData.id = $("#courierNumber").val();
        	apiData.orderState = $('#orderState option:selected').val();
        	apiData.endTime = $("#et").val();
        	apiData.startTime = $("#st").val();
        	apiData.patientId = $("#patientId").val();
        	apiData.payState = $('#payState option:selected').val();
        	apiData.outHosDate = $("#outTime").val();
        	var temp = {   
                     rows: params.limit,                         //页面大小
                     page: (params.offset / params.limit) + 1,   //页码
                     sort: params.sort,      //排序列名  
                     sortOrder: params.order, //排位命令（desc，asc） 
                     token : Commonjs.getToken(),
                     apiParam : Commonjs.getApiReqParams(apiData)
                 };
            return temp;
        },
        columns: [{
            checkbox: true,  
            visible: true  //是否显示复选框  
        }, {
            title: '序号',	
            align: 'center',
            valign: 'middle',
            //sortable: true,
            formatter: function (value, row, index) {
            	return index + 1;
            }
        },
        {
            field: 'ApplyTime',
            title: '申请时间',
           // sortable: true,
            formatter : function(value, row, index){
				if(row.ApplyTime != "" || row.ApplyTime != null || row.ApplyTime != undefined){
					return row.ApplyTime.substring(0,16);
            	}else{
            		return "暂无";
            	}
            }
        }, 
        {
            field: 'Name',
            title: '姓名',
            align: 'center',
            valign: 'middle',
            //sortable: true
        }, {
            field: 'PatientId',
            title: '病案号',
            align: 'center',
            valign: 'middle',
        }, {
            field: 'OutHosDate',
            title: '出院日期',
            align: 'center',
            valign: 'middle',
            //sortable: true,
            formatter : function(value, row, index){
				if(row.OutHosDate != "" || row.OutHosDate != null || row.OutHosDate != undefined){
					return row.OutHosDate.substring(0,10);
            	}else{
            		return "暂无";
            	}
            }
        }, {
            field: 'Hospitalization',
            title: '住院天数(天)',
            align: 'center',
            valign: 'middle',
            //sortable: true,
        }, {
            field: 'OperationName',
            title: '手术名称',
            valign: 'middle',
            //sortable: true,
            formatter : function(value, row, index){
				if(row.OperationName != "" || row.OperationName != null || row.OperationName != undefined){
					//return row.OperationName.substring(0,12);
					return row.OperationName.split(",")[0];
				}else{
            		return "暂无";
            	}
            }
        }, {
            field: 'CaseNumber',
            title: '份数(份)',
            align: 'center',
            valign: 'middle',
//            formatter : function(value, row, index){
//    			var numAll = 0;
//    			var a = row.CaseNumberAll.split(",");
//    			for(var i = 0;i<a.length;i++){
//    				numAll += parseInt(a[i]);
//    			}
//    			return numAll;
//            }      
            
        }, {
            field: 'OutDeptName',
            title: '出院科室',
            align: 'center',
            valign: 'middle',
            //sortable: true,
//            formatter : function(value, row, index){
//				if(row.OutDeptName != "" || row.OutDeptName != null || row.OutDeptName != undefined){
//					var start = row.OutDeptName.indexOf("科");
//					var end = row.OutDeptName.indexOf("楼");
//					return row.OutDeptName.substring(start+1,end+1);
//            	}else{
//            		return "暂无";
//            	}
//            }
        }, 
//        {
//            //field: 'Isoperation',
//        	field: 'Id',
//            title: '是否手术',
//            sortable: true,
//            formatter : function(value, row, index){
//				if(row.OperationName != "" || row.OperationName != null || row.OperationName != undefined){
//					return "有";
//            	}else{
//            		return "无";
//            	}
//            }
//        },
//        {
//            field: 'PayState',
//            title: '支付状态',
//            sortable: true,
//            formatter : function(value, row, index){
//                switch (value) {
//                case "1":
//                	return "待支付";
//                case "2":
//                	return "支付中";
//                case "3":
//                	return "已支付";
//                case "4":
//                	return "退款中";
//                case "5":
//                	return "已退款";
//                default:
//                	return "暂无数据";
//                }
//            }
//        },
        {
            field: 'OrderState',
            title: '订单状态',
            align: 'center',
            valign: 'middle',
            formatter : function(value, row, index){
                switch (value) {
                case "1":
                	return "申请成功";
                case "2":
                	return "已确认";
                case "3":
                	return "已寄送";
                case "4":
                	return "已取消";
                default:
                	return "暂无";
                }
            }

        },
        {
            title: '操作',
            align: 'center',
            valign: 'middle',
            formatter: formatter
        }],
        responseHandler: function(res) {
        	if(res.RespCode == 401){
        		alert(res.RespMessage);
        		top.location = "http://"+window.location.host;
        	}else{
        		if(res.Data == undefined){
        			return [];
        		}else{
        			return res.Data;
        		}
        	}
        },
        onLoadSuccess: function (result) {
        	
        },
        onLoadError: function () {
            alert("数据加载失败！");
        },
	});
}

function formatter(value, row, index){
	var orderHtml = "./order.html?id="+row.Id+"&mcId="+row.McId;
	var kdnHtml = "./KDN.html?code="+row.CourierCompanyCode+"&cNum="+row.CourierNumber;
	//var printApplicationHtml = "./print_application.html?name="+row.Name+"&id="+row.PatientId+"&outDept="+row.OutDeptName+"&dayNum="+row.Hospitalization+"&isOper="+row.Isoperation+
	//"&operName="+row.OperationName+"&outDate="+row.OutHosDate+"&cost="+row.TotalMoney+"&printNum="+row.CaseNumber;
	//var printAddressHtml = "./print_address.html?CN="+row.CourierNumber+"&CC="+row.CourierCompany+"&name="+row.Addressee+"&address="+row.Address+"&telephone="+row.Telephone;
	var html = [];
	html.push('<a href="'+orderHtml+'" style="color:#3685ff;font-size:12px;">详情</a>&nbsp;&nbsp;');
	html.push('<a href="'+kdnHtml+'" title="物流信息" target="_blank" style="color:#3685ff;font-size:12px;">物流信息</a>&nbsp;&nbsp;');
	//html.push('<a href="'+printApplicationHtml+'" style="color:#3685ff;font-size:12px;">打印申请单</a>&nbsp;&nbsp;');
	//html.push('<a href="'+printAddressHtml+'" style="color:#3685ff;font-size:12px;">打印地址</a>');
	return html.join('')
//	return '<div class="formatter-control"><a href="javascript:">变更费用</a><div class="form-group form-inline"><input class="form-control" type="text" name="" id="" value="" /><button type="button" class="btn glyphicon glyphicon-remove"></button><button type="button" class="btn">提交</button></div></div>';
}

//打印申请单
function printApplication(){
	$("#printList  tr:not(:first)").html("");
	var getSelections= $("#orderTable").bootstrapTable('getSelections'); 
	if(getSelections.length<=0){
		alert("未选择订单");
//	}else if(getSelections.length == 1){
//		var html = "../../html/pc/print_application.html?name="+getSelections[0].Name+
//		"&id="+getSelections[0].PatientId+
//		"&outDept="+getSelections[0].OutDeptName+
//		"&dayNum="+getSelections[0].Hospitalization+
//		"&isOper="+getSelections[0].Isoperation+
//		"&operName="+getSelections[0].OperationName+
//		"&outDate="+getSelections[0].OutHosDate+
//		"&cost="+getSelections[0].TotalMoney+
//		"&printNum="+getSelections[0].CaseNumber;
//		//window.open(html); 
//		window.location.href = html;
	}else{
      var param = [];
      var start = '';
      var end = '';
      for(var i = 0; i<getSelections.length;i++){
          var p = {};
          p.PatientId = getSelections[i].PatientId;
          p.Name = getSelections[i].Name;
          p.OutHosDate = getSelections[i].OutHosDate.substring(0,10);
          p.Hospitalization = getSelections[i].Hospitalization;
          p.OperationName = getSelections[i].OperationName.split(",")[0];
          p.CaseNumber = getSelections[i].CaseNumber;
          p.OutDeptName = getSelections[i].OutDeptName;
          param.push(p);
      }
	  var url = 'print_application_all.html?printApplication='+JSON.stringify(param)+"&selectDate="+selectDate;
	  location.href = Commonjs.goToUrl(url);
		
//		if(selectDate != '' && selectDate != null && selectDate != '~'){
//			$(".tit1").text("申请日期:"+selectDate);
//			$(".tit1").show();
//		}else {
//			$(".tit1").text("");
//			$(".tit1").hide();
//		}
//	 	var html = '';
//	 	var xh = '';
//		for(var i = 0; i<getSelections.length; i++){
//			if(getSelections[i].Id != ''){
//				xh = (i+1);
//			}else {
//				xh = "--"
//			}
//			html+='<tr><td>'+xh+'</td><td>'+getSelections[i].PatientId+'</td><td>'+getSelections[i].Name+'</td><td>'+getSelections[i].OutHosDate.substring(0,10)+'</td><td>'+getSelections[i].Hospitalization+'</td><td style="text-align: left;">'+getSelections[i].OperationName.substring(0,11)+'</td><td>'+getSelections[i].CaseNumber+'</td><td>'+getSelections[i].OutDeptName.substring(0,4)+'</td></tr>';
//		}
//		$("#printList").append(html);
//		$("#orderList").hide();
//		$("#printPage").show();
//	 	setTimeout(function(){
//			window.print();
//			setTimeout(function(){
//				$("#orderList").show();
//				$("#printPage").hide();
//			},300)
//		},1000)

	}
	
}

//打印地址
function printAddress(){
	var getSelections= $("#orderTable").bootstrapTable('getSelections'); 
	if(getSelections.length<=0){
		alert("未选择订单");
//	}else if(getSelections.length == 1){
//		var html = "../../html/pc/print_address.html?CN="+getSelections[0].CourierNumber+"&CC="+getSelections[0].CourierCompany+"&name="+getSelections[0].Addressee+"&address="+getSelections[0].Address+"&telephone="+getSelections[0].Telephone;
//		window.location.href = html;
	}else{
		var param = [];
		for(var i = 0; i<getSelections.length;i++){
			var p = {};
			p.Id = getSelections[i].Id;
			p.PatientId = getSelections[i].PatientId;
			p.Name = getSelections[i].Name;
			p.Addressee = getSelections[i].Addressee;
			p.Address = getSelections[i].Province+getSelections[i].Address;
			p.Telephone = getSelections[i].Telephone;
			param.push(p);
		}
		var url = 'print_address_all.html?printAddress='+JSON.stringify(param)+"&selectDate="+selectDate;
		location.href = Commonjs.goToUrl(url);

	}
}

//查询刷新
function refresh(){
	if($("#st").val() != "" && $("#et").val() == ""){
		selectDate = $("#st").val()+"至今";
	}else if($("#st").val() == "" && $("#et").val() != ""){
		selectDate = "至"+$("#et").val();
	}else if($("#st").val() == $("#et").val()){
		selectDate = $("#st").val();
	}else{
		selectDate = $("#st").val()+"~"+$("#et").val();
	}

	$("#orderTable").bootstrapTable('destroy');
	getMCopyList();
}

//确认申请
function sureApplication(){
	var getSelections= $("#orderTable").bootstrapTable('getSelections');  
	if(getSelections.length<=0){
		alert("未选择订单");
	}else{
		var flag = true;
		for(var i = 0; i<getSelections.length;i++){
			if(getSelections[i].OrderState == '1' && getSelections[i].PayState == '2'){
				var apiData = {};	
				apiData.id = getSelections[i].Id;
				apiData.orderState = "2";
				var param = {};
				param.apiParam = Commonjs.getApiReqParams(apiData);
				Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
					if(dd.RespCode!=10000){
						flag = false;
					}
				},{async:false});
			}else{
				flag = false;
			}
		}
		if(flag){
			$('#orderState').val(2);
			refresh();
		}else{
			alert("部分订单无法确认，请联系管理员！");
		}
	}
}

//当单号改变时查询其快递公司
$(".yjdh").bind("input propertychange", function () {
	if($(this).val() != '' && $(this).val() != null && $(this).val() != undefined ){
	var param = {};
	param.LogisticCode = $(this).val();
	Commonjs.ajax('/kdniao/orderDistinguish.do',param,function(dd){
		if(dd.Shippers != undefined && dd.Shippers != '' && dd.Shippers != null){
			$("#courierCompany").text(dd.Shippers[0].ShipperName);
			$("#courierCompanyCode").text(dd.Shippers[0].ShipperCode);
		}else{
			$("#courierCompany").text('');
		}
	});
}
});



//点击快递单号录入
function openExpress(num){
	$('.modals').focus();
	var getSelections= $("#orderTable").bootstrapTable('getSelections'); //选中的行数据	
	var selectLength = getSelections.length;
	if(selectLength <= 0){
		alert("未选择订单");
	}else if(selectLength == 1){
		if(num != 0){
			var newCN = $('.yjdh').val();
			var newCC =  $("#courierCompany").text();
			var newCode = $("#courierCompanyCode").text();
			if(newCN != getSelections[0].CourierNumber){
//				var updateCode = updateCN(getSelections[0].Id,newCN,newCC,newCode,getSelections[0].OrderState,getSelections[0].PayState);//修改数据库
//				if(updateCode){
//					getSelections[0].CourierNumber = newCN;
//					getSelections[0].CourierCompany = newCC;
//				}
				if(getSelections[0].OrderState != '2'){
					alert('该订单处于未确认的状态，无法寄送');
				}else if(getSelections[0].PayState != '2'){
					alert('该订单不处于已支付的状态，无法寄送');
				}else{
					var apiData = {};	
					apiData.id = getSelections[0].Id;
					apiData.courierNumber = newCN;
					apiData.courierCompany = newCC;
					apiData.courierCompanyCode = newCode;
					//apiData.orderState = "3";
					var param = {};
					param.apiParam = Commonjs.getApiReqParams(apiData);
					Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
						if(dd.RespCode == 10000){
							getSelections[0].CourierNumber = newCN;
							getSelections[0].CourierCompany = newCC;	
						}
					},{async:false});
				}
			}
		}
		$('.yjdh').val(getSelections[0].CourierNumber);
		$("#courierCompany").text(getSelections[0].CourierCompany);
		$("#index").text($(getSelections[0]).attr('data-index'));
		$("#yjName").text(getSelections[0].Addressee);
		$("#yjTelephone").text(getSelections[0].Telephone);
		$("#yjAddress").text(getSelections[0].Address);
		//显示弹框
		$('.modals').show();
		if($('.yjdh').val() != '' && $('.yjdh').val() != null && $('.yjdh').val() != undefined ){
			$('.yjdh').attr("disabled","disabled");
		}else{
			correc();
		}

	}else {
		var newCN = $('.yjdh').val();//获得的新快递单号
		var newCC = $("#courierCompany").text();
		var newCode = $("#courierCompanyCode").text();
		var selected = $(".selected");
		
		selectNum = parseInt(selectNum) + parseInt(num);
		if(num == 1 && selectNum >= selectLength){
			selectNum = selectLength - 1 ;
			if(newCN != getSelections[selectNum].CourierNumber){
//				var updateCode = updateCN(getSelections[selectNum].Id,newCN,newCC,newCode,getSelections[selectNum].OrderState,getSelections[selectNum].PayState);//修改数据库
//				if(updateCode){
//					getSelections[selectNum].CourierNumber = newCN;
//					getSelections[selectNum].CourierCompany = newCC;
//				}
				if(getSelections[selectNum].OrderState != '2'){
					alert('该订单处于未确认的状态，无法寄送');
				}else if(getSelections[selectNum].PayState != '2'){
					alert('该订单不处于已支付的状态，无法寄送');
				}else{
					var apiData = {};	
					apiData.id = getSelections[selectNum].Id;
					apiData.courierNumber = newCN;
					apiData.courierCompany = newCC;
					apiData.courierCompanyCode = newCode;
					//apiData.orderState = "3";
					var param = {};
					param.apiParam = Commonjs.getApiReqParams(apiData);
					Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
						if(dd.RespCode == 10000){
							getSelections[selectNum].CourierNumber = newCN;
							getSelections[selectNum].CourierCompany = newCC;	
						}
					},{async:false});
			
				}
			}
		}else if(num == -1 && selectNum < 0){
			selectNum = 0;
		}else{
			var oldCN = getSelections[selectNum-num].CourierNumber;//数据加载时候获得的旧快递单号
			if(selectNum != 0 && selectNum-num >= 0 && newCN != oldCN){
//				var updateCode = updateCN(getSelections[selectNum-num].Id,newCN,newCC,newCode,getSelections[selectNum-num].OrderState,getSelections[selectNum-num].PayState);//修改数据库
//				if(updateCode){
//					for(var i = 0;i < selectLength;i++){
//						if(getSelections[selectNum-num].Id == getSelections[i].Id){
//							getSelections[i].CourierNumber = newCN;
//							getSelections[i].CourierCompany = newCC;
//						}
//					}
//				}
				if(getSelections[selectNum-num].OrderState != '2'){
					alert('该订单处于未确认的状态，无法寄送');
				}else if(getSelections[selectNum-num].PayState != '2'){
					alert('该订单不处于已支付的状态，无法寄送');
				}else{
					var apiData = {};	
					apiData.id = getSelections[selectNum-num].Id;
					apiData.courierNumber = newCN;
					apiData.courierCompany = newCC;
					apiData.courierCompanyCode = newCode;
					//apiData.orderState = "3";
					var param = {};
					param.apiParam = Commonjs.getApiReqParams(apiData);
					Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
						if(dd.RespCode == 10000){
							for(var i = 0;i < selectLength;i++){
								if(getSelections[selectNum-num].Id == getSelections[i].Id){
									getSelections[i].CourierNumber = newCN;
									getSelections[i].CourierCompany = newCC;
								}
							}
						}
					},{async:false});
				}
			}
		}
		var index =  parseInt($(selected[selectNum]).attr('data-index'));//选中的当前行行号
		var info = getSelections[selectNum];//选中的当前行的数据
		//数据渲染
		$("#index").text(index+1);
		$('.yjdh').val(info.CourierNumber);
		$("#courierCompany").text(info.CourierCompany);
		$("#yjName").text(info.Addressee);
		$("#yjTelephone").text(info.Telephone);
		$("#yjAddress").text(info.Address);
		//显示弹框
		$('.modals').show();
		
		if($('.yjdh').val() != '' && $('.yjdh').val() != null && $('.yjdh').val() != undefined ){
			$('.yjdh').attr("disabled","disabled");
		}else{
			correc();
		}
		
	}
	
}


//修改单号
function updateCN(id,courierNumber,newCC,newCode,orderState,payState){
	if(orderState != '2'){
		alert('该订单处于未确认的状态，无法寄送');
		return false;
	}else if(payState != '2'){
		alert('该订单不处于已支付的状态，无法寄送');
		return false;
	}else{
		var apiData = {};	
		apiData.id = id;
		apiData.courierNumber = courierNumber;
		apiData.courierCompany = newCC;
		apiData.courierCompanyCode = newCode;
		apiData.orderState = "3";
		param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/UpdateOrder/callApi.do',param,function(dd){
			if(dd.RespCode == 10000){
				return true;
			}else{
				return false;
			}
		},{async:false});
//		var param = {};
//		param.LogisticCode = courierNumber;
//		Commonjs.ajax('/kdniao/orderDistinguish.do',param,function(dd){
//			
//		});
	}
}


//修正
function correc(){
	$('.yjdh').attr("disabled",false);
	$('.yjdh').val('');
	$(".yjdh").focus(); 
}

//关闭模态框
function clos(){
	$('.yjdh').val('');
	$("#courierCompany").text('');
	selectNum = 0;
	$('.modals').hide();
	refresh();
}


function formatTableUnit(value, row, index) {
	if(row.Id != ""){
		return {
	        css: {
	            "color":'red'
	        }
	    }		
	}else {
		return "";
	}
}