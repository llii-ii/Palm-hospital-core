var priceId = "";
var isUpdate = false;
$(function(){
	getPriceManage();
});

function getPriceManage(){
	$('#exampleTablePagination').bootstrapTable({
		url: '/wsgw/medicalCopy/GetPriceManageInfo/callApi.do',  //请求后台的URL（*）
        method: 'POST',                      //请求方式（*）
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        pagination: true,                   //是否显示分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: 10,                     //每页的记录行数（*）
        queryParams : function (params) {
        	var apiData = {};	
        	apiData.name = $("#name").val();
        	//apiData.endTime = $("#et").val();
        	apiData.startTime = $("#st").val();
        	apiData.priceType = "2";
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
            field: 'Name',
            title: '科室名称',
            sortable: true
        }, {
            field: 'Money',
            title: '复印费用(元)',
            sortable: true,
        }, {
            field: 'UpdateTime',
            title: '变更时间',
            sortable: true,
        }, {
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
	return '<div class="formatter-control"><a href="javascript:" onclick="openExpress(\''+row.Name+'\',\''+row.Money+'\',\''+row.Id+'\')">变更费用</a></div>';
}


//查询刷新
function refresh(){
	$("#exampleTablePagination").bootstrapTable('destroy');
	getPriceManage();
}


//关闭变更费用模态框
function clos(){
	$('.modals2').hide();
	if(isUpdate){
		isUpdate = false;
		refresh();
	}
	
}
//点击变更费用
function openExpress(name,money,id){
	priceId = id;
	$("#deptName").text(name);
	$("#cost").text(money);
	$('.modals2').show();
}


//修改复印费用
function saveMoney(){
	var money = $(".yjdh").val();
	if(money != ''){
		var apiData = {};	
		apiData.id = priceId;
		apiData.money = money;
		var param = {};
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/medicalCopy/UpdatePriceManage/callApi.do',param,function(dd){
			if(dd.RespCode == 10000){
				$("#cost").text(money);
				isUpdate = true;
				$(".yjdh").val("");
				alert("修改成功");
			}else{
				alert("修改失败");
			}
		});		
	}else{
		alert("请输入费用");
	}

}
