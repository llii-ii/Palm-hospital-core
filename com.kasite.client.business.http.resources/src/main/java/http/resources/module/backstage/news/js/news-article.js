var smallClick =1;
var bigClick =1;
var editorOption = {
        //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars:  [[ 
    		 		       //'anchor', //锚点
    	 		          'undo', //撤销
		 		          'redo', //重做
		 		          'bold', //加粗
		 		          'indent', //首行缩进
		 		          //'snapscreen', //截图
		 		          'italic', //斜体
		 		          'underline', //下划线
		 		         // 'strikethrough', //删除线
		 		          'subscript', //下标
		 		          'fontborder', //字符边框
		 		          'superscript', //上标
		 		          'formatmatch', //格式刷
		 		         // 'blockquote', //引用
		 		          'pasteplain', //纯文本粘贴模式
		 		          'selectall', //全选
		 		         // 'print', //打印
		 		         'insertcode', //代码语言
	   	 		          'fontfamily', //字体
	   	 		          'fontsize', //字号
	   	 		          'paragraph', //段落格式
	   	 		          'simpleupload', //单图上传
	   	 		          'insertimage', //多图上传
	   	 		          'edittable', //表格属性
	   	 		          'edittd', //单元格属性
	   	 		          'link', //超链接
	   	 		          //'emotion', //表情
	   	 		          'spechars', //特殊字符
	   	 		          'searchreplace', //查询替换
		 		          'horizontal', //分隔线
		 		          'removeformat', //清除格式
		 		          'justifyleft', //居左对齐
	   	 		          'justifyright', //居右对齐
	   	 		          'justifycenter', //居中对齐
	   	 		          'justifyjustify', //两端对齐
	   	 		          'forecolor', //字体颜色
	   	 		          'backcolor', //背景色
	   	 		          'insertorderedlist', //有序列表
	   	 		          'insertunorderedlist', //无序列表
	   	 		          'fullscreen', //全屏
	   	 		          'directionalityltr', //从左向右输入
	   	 		          'directionalityrtl', //从右向左输入
	   	 		          'rowspacingtop', //段前距
	   	 		          'rowspacingbottom', //段后距
		 		          'time', //时间
		 		          'date', //日期
		 		          'unlink', //取消链接
    		 		       //'anchor', //锚点
    	 		          'simpleupload', //单图上传
    	 		          //'insertimage', //多图上传
    	 		          'edittable', //表格属性
    	 		          'touppercase', //字母大写
   	 		              'tolowercase', //字母小写
    	 		          'imagecenter', //居中
    	 		          //'wordimage', //图片转存
    	 		          'edittip ', //编辑提示
    	 		          'charts' // 图表
]],
        //focus时自动清空初始化时的内容
        autoClearinitialContent: true,
        wordCount:false, 
         //关闭elementPath
        elementPathEnabled: false,
	 	catchRemoteImageEnable:false

    };
var formate_data =[{'id':'0','text':'新闻动态'}, //泉州儿童
           {'id':'1','text':'通知公告'},// 健康讲座
           {'id':'2','text':'健康宣教'},// 健康讲座
           {'id':'3','text':'就诊指南'},// 就诊指南
           {'id':'4','text':'住院需知'}];//住院需知
$(function(){
	    initImage();
	 
        $('#datetimepicker1').datetimepicker({
            yearOffset:0,
            lang:'ch',
            timepicker:false,
            format:'Y-m-d',
            formatDate:'Y-m-d',
          //  minDate:'-1970-01-02', // yesterday is minimum date
          //  maxDate:'+1970-01-02' // and tommorow is maximum date calendar
            yearStart:1901,
    		yearEnd:2999,
        });
       
        $('#datetimepicker2').datetimepicker({
            yearOffset:0,
            lang:'ch',
            timepicker:false,
            format:'Y-m-d',
            formatDate:'Y-m-d',
          //  minDate:'-1970-01-02', // yesterday is minimum date
          //  maxDate:'+1970-01-02' // and tommorow is maximum date calendar
            yearStart:1901,
    		yearEnd:2999,
        });
        $('#datetimepicker1').datetimepicker({value:Commonjs.getDate(0)});
        $('#datetimepicker2').datetimepicker({value:Commonjs.getDate(1)});
        $('#datetimepicker1').blur(function(){
        	var start = $('#datetimepicker1').val();
        	var end = $('#datetimepicker2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
        });
        $('#datetimepicker2').blur(function(){
        	var start = $('#datetimepicker1').val();
        	var end = $('#datetimepicker2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
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
        });
		$(".btn-add").on("click",function(){
			 initUe('');
			 clearData();
			 $("#dtitle").val('');
			 $("#trStatus").show();
			 
			 //添加按钮弹出的控件（多余）
//			var artBox=art.dialog({
//				lock: true,
//				artIcon:'add',
//				width: 700,
//				height:350,
//				opacity:0.4,
//				title:'新增文章',
//				content: document.getElementById('add-form'),
//				ok: function () {
//				// title,smallPic,bigPic,contents,statusif
//				if($("#dtypeClass").val() == 0  ){
//				 if(smallClick <=1){
//					// art.dialog.tips('小图需要上传','1000');
//					// alert('小图需要上传');
//						
//						art.dialog({
//							lock: true,
//							width: '300px',
//							height: '100px',
//						    time: 3,
//					        content: '小图需要上传！'
//						});
//					 return false;
//				 }else if(bigClick <=1){
//					// art.dialog.tips('大图需要上传','1000');
//					// alert('大图需要上传');
//						
//						art.dialog({
//							lock: true,
//							width: '300px',
//							height: '100px',
//						    time: 3,
//					        content: '大图需要上传！'
//						});
//					 return false;
//				 }
//				}
//				 article._addArtice($("#dtype").val(),$("#dtitle").val(),$("#SmallImgV").attr('src'),$("#BigImgV").attr('src'),UE.getEditor('editor').getContent(),$("#dstatus").val(),$("#dlink").val(),$("#dtypeClass").val());
//				 return true;
//
//			},
//				cancel: function(){
//					$("#editor").remove();
//					return true;
//				}
//			});	
			
	
			//editor
		});
		$("#queryBut").on("click",function(){
			_query();

		});
		
		$('.select-img').on("change",function(){
			if($(this).find("option:selected").attr('data-val')	== 'img'){
				$("#ar3").show();
			}else{
				$("#ar3").hide();
			}
		});
		$(".artClass").on("change",function(){
			if($(this).find("option:selected").attr('data-val')	== 'article'){
				$("#ar2").show();
				$("#ar1").hide();
			}else{
				$("#ar2").hide();
				$("#ar1").show();
			}
		});
	        if(QueryString('isBack') != undefined && QueryString('isBack') != null){
		    	_query();
		    	return;
		    }
		_query();
		
		$('#title').val('');
    });
function _query(){
	if ($('#datetimepicker1').val()>$('#datetimepicker2').val()){
		Commonjs.alert("开始时间应小于结束时间");
		return;
	}
	var bdate = $("#datetimepicker1").val();
	var edate = $("#datetimepicker2").val();
	var typeClass = $("#typeClass").val();
	var type = $("#type").val();
	var head = $("#isHead").val();
	var title = $("#title").val();
	article._queryData(bdate,edate,typeClass,type,head,title,1);
}
function clearData(){
	 $("#dtype").val(0);
	 $("#title").val('');
	 $("#dlink").val('');
	 $("#dstatus").val(0);
	 $("#dtypeClass").val(0);
	 $('#SmallImgV').attr('src','../commons/themes/default/images/wdz/20170714150416.png');
	 $('#BigImgV').attr('src','../commons/themes/default/images/wdz/20170714150409.png');

	 if($("#dtypeClass").find("option:selected").attr('data-val')	== 'img'){
			$("#ar3").show();
		}else{
			$("#ar3").hide();
		}
	 if($("#dtype").find("option:selected").attr('data-val')	== 'article'){
			$("#ar2").show();
			$("#ar1").hide();
		}else{
			$("#ar2").hide();
			$("#ar1").show();
		}
}
function initUe(val){
	$("#_td").append("<script id=\"editor\" type=\"text/plain\" style=\"width:500px;height:200px;\"></script>");
	var UE = new baidu.editor.ui.Editor(editorOption);
	UE.render('editor');
	UE.ready(function() {
		//editor_a.hide();//隐藏编辑器
		UE.setContent(unescape(val));
		  //赋值给UEditor du.Data.Contents
		UE.addListener('beforeInsertImage', function (t,arg)
   		{
   	        //  alert('这是图片地址：'+arg[0].src);
   		});

	});
}
function edit(id){
	window.location.href="article_edit.html?id="+id;

	//article._loadData(id);
}
function fetureHeight(){
	var h=$(window).height()-111;
	if($('.info-doctor').outerHeight(true)>$(window).height()){
		
		$('.info-details-fl').height($('.info-doctor').outerHeight(true)-49);
	}else{
		$('.info-doctor').height(h);
		$('.info-office-list').height(h-49).jScrollPane({"overflow":true});
		$('.info-details-fl').height(h-49);
	}
}

function newGuid() {
	var guid = "";
	for (var i = 1; i <= 32; i++){
		var n = Math.floor(Math.random()*16.0).toString(16);
		guid +=   n;
		if((i==8)||(i==12)||(i==16)||(i==20))
			guid += "-";
	}	
	return guid; 
}
function initImage(){
	var id = newGuid();
	var html = '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
			+ id
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:120px;width:120px;cursor: pointer;" title="点击上传图片" value="点击上传图片" onchange=upload("'
			+ id
			+ '","SmallImgV"); onpaste="return false;" type="file" name="0"><img id="SmallImgV" src="../commons/themes/default/images/wdz/20170714150416.png" style="height:120px;width:120px;"  /></div>';
	$("#addSmallImg").empty();
	$("#addSmallImg").append(html);
	
	var id2 = newGuid();
	var html = '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
			+ id2
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:80px;width:230px;cursor: pointer;" title="点击上传图片" value="点击上传图片" onchange=upload("'
			+ id2
			+ '","BigImgV"); onpaste="return false;" type="file" name="0"><img id="BigImgV" src="../commons/themes/default/images/wdz/20170714150409.png" style="height:80px;width:230px;"  /></div>';
	$("#addBigImg").empty();
	$("#addBigImg").append(html);
	

}
function upload(id,image) {	
	var filename = $("#"+id).val();
	var index = filename.lastIndexOf('.');
	var type = filename.substring(index+1,filename.length);
	if(type.toLowerCase() != 'jpg' && type.toLowerCase() != 'gif'
		&&type.toLowerCase() != 'png'&&type.toLowerCase() != 'jpeg'){
		YihuUtil.art.warning('注意喔：图片格式必须为.jpeg|.gif|.jpg|.png','warning');
		return ;
	}

	var arrID = [ id ];
	$.yihuUpload.ajaxFileUpload( {
		url : '/upload/uploadFile.do', // 用于文件上传的服务器端请求地址
		secureuri : false,// 一般设置为false
		fileElementId : arrID,// 文件上传空间的id属性 <input type="file" id="file"
								// name="file" />
		dataType : 'json',// 返回值类型 一般设置为json
		success : function(data, status) {
			var uri = data.url;
			uri=uri.replace('fullsize','small');
			var name = data.NewFileName;  
			var fname = data.FileName;
			var size = data.Size;
			var old = $("#" + id + "_f");	
            if (image=='SmallImgV') {
            	$("#SmallImgV").attr("src", uri);
            	$("#ImgUrl").val(uri);
            	$("#hidVal").val(uri);
            	smallClick++;
//            	if($("#BigImgUrl").val()==''||$("#BigImgUrl").val()==null){
//            	$("#BigImgV").attr("src", uri);
//				$("#BigImgUrl").val(uri);}
            	
			}else{
				bigClick++;
				$("#BigImgV").attr("src", uri);
				$("#BigImgUrl").val(uri);
			}
	},
	error : function(data, status, e) {
		//YihuUtil.art.warning("图片上传失败：建议您选择不超过1M的图片且在良好的网络环境下继续上传");
	}

	});
}
function toHead(id,isHead){
//	var finalDeal = getSingleInfo(id);
		
		art.dialog({
			lock: true,
	        width: '300px',
			height: '100px',
	        title:'置顶确认',
	        content: '您确定要对这篇文章进行置顶操作吗?',
	        ok : function() {
				var Service = {};
				Service.HosId = Commonjs.getUrlParam("HosId");
				if(isHead == 0){
					Service.IsHead = 1;
				}else{
					Service.IsHead = 0;
				}
				Service.ArticleId = id;
				var param = {};
				param.reqParam = Commonjs.getApiReqParams(Service);
				Commonjs.ajaxSync('/article/toHead.do',param,function(d){
					if(d.RespCode==10000){
						art.dialog({
							lock: true,
							width: '300px',
							height: '100px',
						    time: 3,
						    content: "操作成功!"
						});
					}else{
						art.dialog({
							lock: true,
							width: '300px',
							height: '100px',
						    time: 3,
						    content: d.RespMessage
						});
					}
					_query();
					if(Service.IsHead == 1){
						$("._class_"+id).text("取消置顶");
					}else{
						$("._class_"+id).text("置顶");
					}
				});
				return true;
	        },
        cancel : true
	 });    
		
}
function formateData(dat){
	if(dat == undefined){
		return "";
	}
	var back = "";
	$.each(formate_data,function(k,v){
		
		if(v.id == dat){
			back = v.text;
			return false;
		}
	});
	return back;
}
function toIssue(id,isSueDate,o){
	var finalDeal = getSingleInfo(id);
		art.dialog({
			lock: true,
	        width: '300px',
			height: '100px',
	        title:'发布确认',
	        content: '您确定要更改文章状态吗?',
	        ok : function() {
				var state = 0;
				if($(o).attr('class') == 'my-switch-box red'){
					state = 1;
				}
			    var Service = {};
			    Service.HosId = Commonjs.getUrlParam("HosId");
				if(state == 1){
					Service.Status = 1;
				}else{
					Service.Status = 0;
				}
				Service.IsSueDate = isSueDate;
				Service.ArticleId = id; 
				var param = {};
				param.reqParam = Commonjs.getApiReqParams(Service);
				Commonjs.ajaxSync('/article/toIsSue.do',param,function(d){
					if(d != null && d.RespCode == 10000  ){
						if(state == 0){
						 	$(o).attr('class','my-switch-box red');
						 	$("._cla_"+id).text('未发布');
						}else{
							$(o).attr('class','my-switch-box');
							$("._cla_"+id).text('已发布');
						}
						
					}else if(d != null){
						art.dialog({
							lock: true,
					        width: '300px',
							height: '100px',
					        title:'发布确认',
						    time: 3,
					        content: d.RespMessage});
					}
				});
				_query();
				return true;
	        },
	        cancel : true
	    });   
		
	
}
function getSingleInfo(id){
	if ($('#datetimepicker1').val()>$('#datetimepicker2').val()){
		Commonjs.alert("开始时间应小于结束时间");
		return;
	}
	var bdate = $("#datetimepicker1").val();
	var edate = $("#datetimepicker2").val();
    var Service = {};
	Service.Id = id;
	Service.TypeClass = -1;
	Service.IsHead = -1;
	Service.Status = -1;
	Service.StartDate = bdate;
	Service.EndDate = edate;

	var pageIndex = 0;
	var pageSize = 10;
	var page = {};
	page.PIndex = pageIndex;
	page.PSize = pageSize;
	Service.Page = page;
	var param = {};
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajax('/article/queryArticle.do',param,function(du){
		if(du.RespCode == '10000'){
			if(du.Data != undefined && du.Data != ''){
			 return du.Data.FinalDeal;
			}
			 
		}
	});
	return 0;
}
function getSystemDay(){
	var dd = new Date();
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;//获取当前月份的日期
	var d = dd.getDate();
	var hour = dd.getHours();
	var minu = dd.getMinutes();
	var sec = dd.getSeconds();
	if(m < 10) m = "0"+m;
	if(d<10) d = "0"+d;	
    if (hour < 10) hour = "0" + hour;
    if (minu < 10) minu = "0" + minu;
    if (sec < 10) sec = "0" + sec;
    return y+"-"+m+"-"+d +" "+hour + ":" + minu + ":" + sec ;
	
	
}
function del(id){
	art.dialog({
		lock: true,
        width: '300px',
		height: '100px',
        title:'删除确认',
        content: '您确定要删除这篇文章吗?',
        ok : function() {
        	var param = {};
        	var Service = {};
        	Service.HosId = Commonjs.getUrlParam("HosId");
		 	Service.ArticleId = id;
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/article/delArticle.do',param,function(d){
				if(d.RespCode==10000){
					art.dialog({
						lock: true,
						width: '300px',
						height: '100px',
					    time: 3,
					    content: "删除" + d.RespMessage
					});
				}else{
					art.dialog({
						lock: true,
						width: '300px',
						height: '100px',
					    time: 3,
					    content: "删除失败," + d.RespMessage
					});
				}
				_query();
			});
			return true;
        },
        cancel : true
    });    
	
}
  var article = function(){
	
	   var loadData = function(id){
		    var index = 1;
		    var Service = {};
		    Service.HosId = Commonjs.getUrlParam("HosId");
			Service.StartDate ='';
			Service.EndDate = '';
			Service.Title = '';
			Service.TypeClass = -1;
			Service.IsHead = -1;
			Service.Status = -1;
			Service.ArticleId = id;
			$('#pagenumber').val(index);
			var pageIndex = index;
			var pageSize = 10;
			var page = {};
			page.PIndex = pageIndex;
			page.PSize = pageSize;
			Service.Page = page;
			var param = {};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/article/queryArticle.do',param,function(du){
				if(du.RespCode == 10000){
					 smallClick =1;
					 bigClick =1;
					 initUe((du.Data.Contents == undefined || du.Data.Contents == '') ? '' : du.Data.Contents);
					 $("#SmallImgV").attr('src',du.Data.ImgUrl);
					 $("#BigImgV").attr('src',du.Data.BigImgUrl);
					 $("#dtitle").val(du.Data.Title);
					 $("#dtypeClass").val(du.Data.TypeClass);
					 $("#dtype").val(du.Data.Type);
					 $("#dlink").val(du.Data.LinkUrl);
					 $("#dstatus").val(du.Data.Status);
			         if( $("#dtypeClass").val()==0){
			        		$("#ar3").show();
						}else{
							$("#ar3").hide();
			         }
					   if( parseInt(du.Data.FinalDeal) ==  0){
						   $("#ar2").show();
							$("#ar1").hide();
							$("#dtype").val(0);
						}else{
							$("#dtype").val(1);
							$("#ar2").hide();
							$("#ar1").show();
						}
						$("#trStatus").hide();
						
				 	var artBox=art.dialog({
						lock: true,
						artIcon:'add',
						opacity:0.4,
						title:'修改文章',
						width: 600,
						height: 350,
						content: document.getElementById('add-form'),
						ok: function () {
						 article._editArtice($("#dtype").val(),$("#dtitle").val(),$("#SmallImgV").attr('src'),$("#BigImgV").attr('src'),UE.getEditor('editor').getContent(),$("#dstatus").val(),$("#dlink").val(),$("#dtypeClass").val(),du.Data.IsHead,du.Data.ArticleId);
						 $("#editor").remove();
						 return true;
				 		},
						cancel: function(){
							$("#editor").remove();
							return true;
						}
					});		
				}else{
					art.dialog({
						lock: true,
						width: '300px',
						height: '100px',
					    time: 3,
					    content: du.RespMessage
					});
				}
			});
	   }

	   var queryInfo = function(bdate,edate,typeClass,type,head,title,index){
		    if(title==Commonjs.getDate(0)) {
		    	$('#title').val('');
		    	title = '';
		    }
		    var Service = {};
//			Service.StartDate = "2017-05-01";
//			Service.EndDate = "2018-10-01";
		    Service.HosId = Commonjs.getUrlParam("HosId");
			Service.StartDate =bdate;
			Service.EndDate = edate;
			Service.Title = title;
			Service.TypeClass = typeClass;
			Service.IsHead = head;
			Service.Status = type;
			if(head >=1 ){
				Service.Status = 1;
			}
			$('#pagenumber').val(index);
			var pageIndex = index;
			var pageSize = 10;
			var page = {};
			page.PIndex = pageIndex;
			page.PSize = pageSize;
			Service.Page = page;
			var param={};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/article/queryArticle.do',param,function(du){
				$("#thead tr:gt(0)" ).remove();
				if(du.RespCode == 10000 && du.Data != undefined ){
					var tep = [];
					if( du.Data.length == undefined && !Commonjs.isEmpty(du.Data.ArticleId)){
						tep.push(du.Data);
						du.Data = tep;
					}
					$.each(du.Data,function(k,v){
						$tr = $("<tr></tr>");
						var _td =  $("<td></td>");
						_td.append(formateData(v.TypeClass == undefined ? "" :v.TypeClass));
						var _td1 =  $("<td></td>");
						_td1.append(v.Title);//name
						var _td2 =  $("<td></td>");
						var html='';
						if(v.Status  == 0){
							html +=  '<div class="my-switch-box red" onclick="toIssue(\''+v.ArticleId+'\',\''+v.IsSueDate+'\',this)"><span></span></div><span class="_cla_'+v.ArticleId+'">未发布</span>';
						}else{
							html +=  '<div class="my-switch-box" onclick="toIssue(\''+v.ArticleId+'\',\''+v.IsSueDate+'\',this)"><span></span></div><span class="_cla_'+v.ArticleId+'">已发布</span>';
						}
						_td2.append(html);//name
						var _td3 =  $("<td></td>");
						var tem = v.LastModify == null ? v.UpdateTime : v.LastModify;
						var temp = tem.split(".")[0];
						_td3.append(temp);
						var _td4 =  $("<td></td>");
						if(v.IsSueDate != undefined && v.IsSueDate != ''){
							var tem = v.IsSueDate ;
							var temp = tem.split(".")[0];
							_td4.append(temp);
						
						}else{
							_td4.append('');
						}
						var _td5 =  $("<td></td>");
						var isHead_text='';
						if(v.IsSueDate != '' &&  v.IsSueDate != undefined ){
							 var tem = "_class_"+v.ArticleId;
								if(v.IsHead == 1){
								
									isHead_text = '<a href="javascript:toHead(\''+v.ArticleId+'\',\''+v.IsHead+'\');" class="i-btn"><i class="i-notop"></i><span class="'+tem+'">取消置顶';
								}else{
									isHead_text = '<a href="javascript:toHead(\''+v.ArticleId+'\',\''+v.IsHead+'\');" class="i-btn"><i class="i-notop"></i><span class="'+tem+'">置顶';
								}
							
						}
						var tem ='<ul class="i-btn-list"><li><a href="javascript:edit('+"'"+v.ArticleId+"'"+');" class="i-btn"><i class="i-edit"></i>编辑</a> </li>'+
						          '<li><a href="javascript:del('+"'"+v.ArticleId+"'"+');" class="i-btn"><i class="i-del"></i>删除</a></li>'+
						          '<li>'+isHead_text+'</span></a></li></ul>';
						_td5.append(tem);
						$tr.append(_td).append(_td1).append(_td2).append(_td3).append(_td4).append(_td5);
						$("#thead").append($tr);
					});
					
				}else {
					Page(0,pageSize,'pager');
					if(du.page == undefined){
						$('#thead').append("<tr align=center><td colspan=6><font color=red>未找到相关数据!</font></td></tr>");
						return;
					}
					return;
				}
				//alert(du.page.pcount);
				if((du.Page.PCount!=undefined && du.Page.PCount!=0)||(du.Page.PCount==0 && index==1)){
					$("#totalcount").val(du.Page.PCount);
				}
				Page($("#totalcount").val(),du.Page.PSize,'pager');
			});
	   };
	   var editInfo = function(dtype,title,smallPic,bigPic,contents,status,link,typeClass,isHead,id){
			 //  alert(title+" "+smallPic+" "+bigPic+" "+contents+" "+status+" "+link+" "+typeClass+" "+id);
			    var Service = {};
				var page = {};
				var code = 1016;
				Service.Title =title;
				Service.HosId = Commonjs.getUrlParam("HosId");
				Service.SmallImgUrl = "<![CDATA["+smallPic+"]]>";
				Service.BigImgUrl = "<![CDATA["+bigPic+"]]>";
				Service.TypeClass = typeClass;
				Service.LinkUrl = link;
				Service.Status = status;
				Service.Contents =  escape(contents);
				if(escape(contents) == null || escape(contents) == ''){
					Service.isNeed = 0;
				}else{
					Service.IsNeed = 1;
				}
				Service.ArticleId = id;
				
				Service.Lastmodify = 1;
				Service.Type = dtype;
			
			//	Service.IssueDate =1;
				Service.IsHead = isHead;
				var param = {};
				var params = Commonjs.getParams(code,Service);//获取参数
				param.Api = "UpdateArticle";
				param.Params = Commonjs.jsonToString(params);
				var du = Commonjs.ajax('./art_callArticleApi.do',param,false);
				if(du.RespCode == 10000){
					art.dialog({
						lock: true,
						width: '300px',
						height: '100px',
					    time: 3,
					    content: du.RespMessage
					});
				
				}else{
					art.dialog({
						lock: true,
						width: '300px',
						height: '100px',
					    time: 3,
					    content: du.RespMessage
					});
				}
				_query();
		   };
	   return {
		   _loadData : loadData,
		   _queryData : queryInfo,
		  // _addArtice : addInfo,
		  _editArtice : editInfo,
		  
	   }
  }();
  function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}

//分页
  function Page(totalcounts, pagecount,pager) {
  	$("#"+pager).pager( {
  		totalcounts : totalcounts,
  		pagesize : 10,
  		pagenumber : $("#pagenumber").val(),
  		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
  		buttonClickCallback : function(al) {
  			$("#pagenumber").val(al);
  			if ($('#datetimepicker1').val()>$('#datetimepicker2').val()){
  				Commonjs.alert("开始时间应小于结束时间");
  				return;
  			}
  			var bdate = $("#datetimepicker1").val();
			var edate = $("#datetimepicker2").val();
			var typeClass = $("#typeClass").val();
			var type = $("#type").val();
			var head = $("#isHead").val();
			var title = $("#title").val();
			article._queryData(bdate,edate,typeClass,type,head,title,al);
  			//queryDetail(al);
  		}
  	});
  }