var pCount = 0;
$(function(){

	iniWidget();
	
	loadData();
	
});

function iniWidget(){
	
	//新增
	$('.brieadd').on('click', function(){
		art.dialog({
			lock: true,
			artIcon:'add',
			opacity:0.4,
			width: 400,
			title:'新增联系人',
			content: $("#brieAdd").get(0),
			ok: function () {
				var deptName = $("#deptName").val();
				var name = $("#name").val();
				var mobile = $("#mobile").val();
				if( ComWbj.isNull(deptName) ){
					Commonjs.alert('科室名称不能为空！');
					return ;
				}
				if( ComWbj.isNull(name) ){
					Commonjs.alert('姓名！');
					return ;
				}
				if( ComWbj.isNull(mobile) ){
					Commonjs.alert('联系方式！');
					return ;
				}
				$.ajax({
					type: 'POST',
					url: './bill_addBillNotify.do?v='+(new Date()),
					data: {'deptName':deptName,'name':name,'mobile':mobile},
					timeout : 6000,
					cache : false,
					dataType: 'json',
					success: function(data){
						if( data.respCode == 10000 ){
							Commonjs.alert('新增成功！');
							$("#deptName").val("");
							$("#name").val("");
							$("#mobile").val("");
							loadData();
						}else{
							Commonjs.alert(data.respMessage);
						}
		 			}
				});
			},
			cancel: true
		});
	});
	
	
	
	
}


/**
 * 加载数据
 */
function loadData(){
	  //初始化表格
	 $('#billNotify').DataTable({
		 destroy:true,	
		 "serverSide": true,
		 "ajax" : {
			url:"./bill_queryBillNotify.do",
			// 提交参数
		    data: function (param) {
		    	param.count = pCount;
		        return param;
		    },
		    dataSrc: function (data) {
		    	pCount = data.recordsTotal;
		        return data.data;
		    }
		},
		"columns": [
		            {"data": "id", title:"序号", render: function (data, type, row, meta) {
		                return meta.row + meta.settings._iDisplayStart + 1;
		            } },
		            {"data": "deptName",title:"科室" },
		            {"data": "name",title:"姓名"},
		            {"data": "mobile",title:"联系方式"},
		            {"data": "id",title:"操作",render:function(data, type, row, meta ){
		            	return "<a href=\"javascript:void(0);\" onClick=\"deleteNotify('"+data+"')\" class=\"alinks-unline alinks-blue briedel\">删除</a>";
		            }}
		        ],
		"bPaginate" : true,//分页工具条显示  
		"bProcessing" : true
		});
}


function deleteNotify(id){
	//删除
	art.dialog({
		lock: true,
		artIcon:'edit',
		opacity:0.4,
		width: 400,
		title:'提示',
		content: '<span class="c-333 c-f14">确定删除该条信息？</span>',
		ok: function () {
			$.ajax({
				type: 'POST',
				url: './bill_deleteBillNotify.do?v='+(new Date()),
				data: {'id':id},
				timeout : 6000,
				cache : false,
				dataType: 'json',
				success: function(data){
					if( data.respCode == 10000 ){
						Commonjs.alert('删除成功！');
					}else{
						Commonjs.alert(data.respMsg);
					}
					loadData();
	 			}
			});
		},
		cancel: true
	});
}