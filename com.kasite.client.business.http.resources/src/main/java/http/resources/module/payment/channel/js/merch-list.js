var arrType;
var pCount ;
var account;
$(function () {
	getData();
});
var t =null;
function getData() {
	pCount = 0;
	t=$('#dataTable').DataTable(
					{	
						'lengthChange' : false,
						'searching' : false,
						'ordering' : false,
						'info' : true,
						'autoWidth' : false,
						"bProcessing" : true,
						"bServerSide" : true,
						"deferRender" : true,
						"pageLength" : 7,
						"pagingType": "full_numbers",
						"ajax" : { // ajax方式向后台发送请求
							"type" : "POST",
							"url" : "../../../channel/show-channel-list.do",
							"headers":{
								'token': Commonjs.getToken()
							},
							data: function (param) {
				            	param.pCount = pCount;
				            	param.account = account;
				                return param;
				            },
							dataSrc: function (resp) {
				            	pCount = resp.recordsTotal;
//				            	resp.draw = resp.draw;
//				            	resp.recordsTotal = resp.recordsTotal;
//				            	resp.recordsFiltered = resp.recordsFiltered;
				                return resp.data;
				            },
							"dataType" : "json"
						},
						"columns" : [// 对接收到的json格式数据进行处理，data为json中对应的key
						{
							"data": "rowNum"
						}, {
							"data" : "configkey"
						}, {
							"data" : "merchId"
						}, {
							"data" : "merchTypeName"
						}, {
							"data" : "bankNo"
						}, {
							"data" : "bankShortName"
						}, {
							"data" : "bankNo"
						}],
						"columnDefs" : [
							{ "width":20 ,  "targets": 0 },
								{
									"targets" : 6,//操作按钮目标列
									"data" : null,
									"render" : function(data, type,row,meta) {
									var code =  row.configkey ;
									var	html = "<td><a href='javascript:void(0)' onclick=\"findDetail('"+code+"','"+row.merchId+"','"+row.merchType+"','"+row.bankNo+"','"+meta.row+"','"+row.bankShortName+"')\" class=\"alinks-unline alinks-blue d-detail-edit\">查看详情</a></td>";
//										html += "&nbsp;&nbsp;&nbsp;<a href=\"javascript:delMerch('"+code+"');\" class=\"alinks-unline alinks-blue\">删除</a></td>";
										return html;
									}
								},
								/*{
				                    "targets": [ 6 ],
				                    "visible": false,
				                    "searchable": false
								},*/
								],
						'language' : {
							"sProcessing" : "处理中...",
							"sLengthMenu" : "显示 _MENU_ 项结果",
							"sZeroRecords" : "没有匹配结果",
							"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
							"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
							"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
							"sInfoPostFix" : "",
							"sSearch" : "搜索:",
							"sUrl" : "",
							"sEmptyTable" : "表中数据为空",
							"sLoadingRecords" : "载入中...",
							"sInfoThousands" : ",",
							"oPaginate" : {
								"sFirst" : "首页",
								"sPrevious" : "上页",
								"sNext" : "下页",
								"sLast" : "末页"
							},
							
							"oAria" : {
								"sSortAscending" : ": 以升序排列此列",
								"sSortDescending" : ": 以降序排列此列"
							}
						}
					})
	

}

function search() {
	pCount = 0;
	account = $('#account').val();
	console.info(account);
	/* 多条件查询 */
	var table = $('#dataTable').DataTable();
	//table.settings()[0].ajax.data = param;
	table.ajax.reload();
}

/*刷新表格*/
function refresh() {
	var table = $('#dataTable').DataTable();
	table.ajax.reload(null, false);// 刷新表格数据，分页信息不会重置
}

/**
 * 商户的新增和编辑 0-新增
 * @param dom
 * @returns
 */
function merchUpdate(dom,acc,type,bank,rowIndex,short){
	 code = dom;
	 var config="";
	var title;
	if(dom == 0){
		title = "新增商户";
	}else{
		title = "商户编辑";
	}
	var rowData = t.row( rowIndex ).data();
	data = {
			code:dom,
			acc:acc,
			type:type,
			bank:bank,
			shortName:short,
			config:rowData.MERCHPAYCONFIG,
			};
	layer.open({
	      type: 2,
	      title: title,
	      area: ['600px', '430px'],
	      fix: false, //不固定
	      maxmin: true,
	      shade:0.4,
	      shadeClose: true, //点击遮罩关闭
	      content: "../merch-add-update.html",
	    });
}

/**
 * 商户的新增和编辑 0-新增
 * @param dom
 * @returns
 */
function findDetail(dom,acc,type,bank,rowIndex,short){
	 code = dom;
	 var config="";
	 var title = "查看商户配置信息";
	 var rowData = t.row( rowIndex ).data();
	 data = {
			code:dom,
			acc:acc,
			type:type,
			bank:bank,
			shortName:short,
		};
	 layer.open({
	      type: 2,
	      title: title,
	      area: ['600px', '430px'],
	      fix: false, //不固定
	      maxmin: true,
	      shade:0.4,
	      shadeClose: true, //点击遮罩关闭
	      content: "../channel/merch-detail.html",
	    });
}

function delMerch(dom){
	layer.confirm("确认要删除吗，删除后不能恢复", { title: "删除确认" }, function (index) {  
		 $.ajax({
		        url:"channel/del-merch.do",    
		        dataType:"json",   
		        async:true,
		        data:{
		        	code:dom,
		            },    
		        type:"post",  
		        success:function(data){
		            var retCode = data.retCode;
		            if(retCode== -1){
		            	layer.msg('不能删除');
		            }else if(retCode== 1){
		            	 layer.alert("操作成功",function(index){
		                       refresh();
		                       layer.close(index);  
		                   })
		            }else{
		            	layer.msg('操作失败');
		            }
		        },
		        error:function(){
		            layer.msg('请求出错，请联系管理员');
		        }
		    });
       
    });   

}
//时间戳转日期
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = date.getDate() + ' ';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return Y+M+D+h+m+s;
}


