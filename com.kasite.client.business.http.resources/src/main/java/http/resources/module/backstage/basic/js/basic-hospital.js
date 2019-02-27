var contend = "";
var isImageUrl = new Array(false,false,false);//判断三张图片tempImg1，tempImg2，tempImg3是否是有效上传图片；false否，则保存不需要入库
var editorOption = {
        //这里可以选择自己需要的工具按钮名称,此处仅选择如下五个
        toolbars:  [[ 
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
    	 		        
    	 		         // 'help' //帮助
]],
        //focus时自动清空初始化时的内容
        autoClearinitialContent: true,
         //关闭elementPath
         elementPathEnabled: false,
         scaleEnabled:true
    };

//点击了编辑之后才能上传1可以,0不能
var uploadFlag = 0;
var selectHospitalId = '';
$(function(){
		
		Commonjs.getJscrollpane.destroy();
		$("#cancel").hide();
        $('#datetimepicker1,#datetimepicker2').datetimepicker({
            yearOffset:0,
            lang:'ch',
            timepicker:false,
            format:'d/m/Y',
            formatDate:'Y/m/d',
            minDate:'-1970/01/02', // yesterday is minimum date
            maxDate:'+1970/01/02' // and tommorow is maximum date calendar
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
        initImage();
        loadHospitalListLocal();
        function rightBox (){
    		$('.doctor-info-fl table.table-form').width($('.container-wrap .scroll-pane').outerWidth(true)-281-40);
    		$('.doctor-info-fr').height($(window).height()-$('.doctor-info-top').outerHeight(true)-111);

    	}
        rightBox ();
        var doctorScroll=$('.doctor-scroll-pane').height($(window).height()-$('.doctor-info-top').outerHeight(true)-111).jScrollPane({"autoReinitialise": true}).data('jsp');

        $(window).resize(function(){
        	Commonjs.getJscrollpane.destroy();
        	Throttle(rightBox(),50,30);
        	var doctorScroll=$('.doctor-scroll-pane').height($(window).height()-$('.doctor-info-top').outerHeight(true)-111).jScrollPane({"autoReinitialise": true});
        });
        
        $('#cancel').on('click',function(){
        	uploadFlag = 0;
        	var $input=$('.table-input'),
    		$textarea=$('.table-textarea'),
    		$btn=$('#edit-btns');
        	$("#showBrief").hide();
    		$("#showInfo").html(contend);
    		$("#showInfo").show();
    			for(var i=0; i<$input.length; i++){
    			$input.eq(i).html($input.eq(i).find('input').val());
    		}
    		for(var j=0; j<$textarea.length; j++){
    			$textarea.eq(j).html($textarea.eq(j).find('textarea').val());	
    		}
    		 $btn.text('编辑').removeClass('edityes');
    		 $(this).hide();
    		 $("#hosLevel").hide();
    		 $("#_hoslevel").show();
			 $("#tempImg1").show();
			 $("#tempImg2").show();
			 $("#tempImg3").show();
			 $("#updateSmallImg").hide();
        });
        $('#edit-btns').on('click',function(){
        	
        	uploadFlag = 1;
        	$("#cancel").hide();
        	var $input=$('.table-input'),
        		$textarea=$('.table-textarea'),
        		$btn=$(this);
        	$('.doctor-table-box').toggleClass('table-text');
        	$("#showBrief").show();
        	$("#showInfo").hide();
        //	$("#pic").attr("style","height:600px;position:fixed;top:100px");
        	$("#tempImg1").show();
        	$("#tempImg2").show();
        	$("#tempImg3").show();
			$("#updateSmallImg").hide();
			
        	if($(this).hasClass('edityes')){
        	
        		$("#hosLevel").hide();
    			$("#_hoslevel").show();
        		$("#cancel").hide();
        		$("#showBrief").hide();
        		//$("#showInfo").text(UE.getEditor('editor').getContent());
        		$("#showInfo").show();
        		$btn.text('编辑').removeClass('edityes');
        		
        		for(var i=0; i<$input.length; i++){
        			$input.eq(i).html($input.eq(i).find('input').val());
        		}
        		for(var j=0; j<$textarea.length; j++){
        			$textarea.eq(j).html($textarea.eq(j).find('textarea').val());	
        		}
        		var temX = clearNoNum($("#hosX").html());
        		var temY =clearNoNum($("#hosY").html());
        		if(temX != $("#hosX").html() ){
        			art.dialog({
    					lock: true,
    					width: '300px',
    					height: '100px',
    				    time: 3,
    				    content: '经度，纬度请输入数字与小数点'
    				});
        			return false;
        		}
        		if(temY != $("#hosY").html() ){
        			art.dialog({
    					lock: true,
    					width: '300px',
    					height: '100px',
    				    time: 3,
    				    content: '经度，纬度输入数字与小数点'
    				});
        			return false;
        		}
        		var param = {};
        		var Service = {};
//   				alert("<![CDATA["+UE.getEditor('editor').getContent()+"]]>");
        		Service.HosRoute = $("#hosRoute").html();
        		Service.HosId = selectHospitalId;
        		Service.HosBrief = escape(UE.getEditor('editor').getContent());
        		Service.Address =$("#hosAddr").html();
        		Service.HosLevel =$("#hosLevel").val();
        		Service.HosLevelName =$("#hosLevel").find("option:selected").text();
        		Service.HosName =$("#hosName").html();
   				//Service.Remark =$("#hosRemark").html();
        		Service.CoordinateX =clearNoNum($("#hosX").html());
        		Service.CoordinateY =clearNoNum($("#hosY").html());
        		Service.Province =$("#hosProvince").html();
        		Service.Tel =$("#hosTel").html();
        		var regex = location.protocol + '//' + location.host;
        		var photoArr = {};
        		if(isImageUrl[0]){
        			var photo1 = $("#SmallImgV1").attr("src");
        			photo1 = photo1.replace(regex, "");
            		photoArr.photo1 = photo1;
        		}
        		if(isImageUrl[1]){
        			var photo2 = $("#SmallImgV2").attr("src");
        			photo2 = photo2.replace(regex, "");
            		photoArr.photo2 = photo2;
        		}
        		if(isImageUrl[2]){
        			var photo3 = $("#SmallImgV3").attr("src");
        			photo3 = photo3.replace(regex, "");
            		photoArr.photo3 = photo3;
        		}
        		Service.PhotoUrl = Commonjs.jsonToString(photoArr);
        		param.reqParam = Commonjs.getApiReqParams(Service);
   				Commonjs.ajaxSync('/basic/updateHospital.do',param,function(d){
   					if(d.RespCode == 10000){
   						art.dialog({
   							lock: true,
   							width: '300px',
   							height: '100px',
   						    time: 3,
   						    content: d.RespMessage
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
   				});
   			  $("#editor").remove();
   			   hospital.findData();
        	}else{
        		$("#tempImg1").hide();
        		$("#tempImg2").hide();
        		$("#tempImg3").hide();
				$("#updateSmallImg").show();
        		$("#hosLevel").show();
    			$("#_hoslevel").hide();
        		$("#cancel").show();
        	//	$("#pic").attr("style","position:fixed;top:100px;height:500px;");
        		$btn.text('保存').addClass('edityes');
        		for(var i=0; i<$input.length; i++){
        			$input.eq(i).html($input.eq(i).find('textarea').val());	
    				$input.eq(i).html('<input class="text big" style="width:200px" value="'+$input.eq(i).text()+'" />');
    				if($input.eq(i).hasClass('map')){
    					$('<a href="http://api.map.baidu.com/lbsapi/getpoint/?qq-pf-to=pcqq.c2c" target="_blank" class="a-maps"><i class="icon icon-map"></i></a>').appendTo($input.eq(i));
    				}
    				if($input.eq(i).hasClass('ask')){
    					$('<span class="ask-wrap"><i class="icon icon-ask"></i><div class="tipBox"><div class="bd">请点击图标通过 <a href="http://api.map.baidu.com/lbsapi/getpoint/?qq-pf-to=pcqq.c2c" target="_blank">百度地图坐标拾取系统</a> 获取经纬度信息</div><div class="hd"><s class="ui-arrow ui-arrow-b"><s></s></s></div></div></span>').appendTo($input.eq(i));
    				}
    				$('.ask-wrap').hover(function(){
    						$('.ask-wrap').find('.tipBox').show();
    					},function(){
    						$('.ask-wrap').find('.tipBox').hide();
    				})
    			}
        		for(var j=0; j<$textarea.length; j++){
        			$textarea.eq(j).html($textarea.eq(j).find('textarea').val());	
        			$textarea.eq(j).html('<textarea class="textarea" style="width:500px; height:'+($textarea.eq(j).height()+50)+'px">'+$textarea.eq(j).text()+'</textarea>');
        		}
        	}
        });
		$("#hosNameSelect").change(function(){
		   	selectHospitalId =  $("#hosNameSelect").val();
		    hospital.findData();
		})
    });
    function loadHospitalListLocal(){
    	 var param = {};
		 var Service = {};
		 param.reqParam = Commonjs.getApiReqParams(Service);
    	 Commonjs.ajax('/basic/queryHospitalListLocal.do',param,function(d){
   					if(d.RespCode == 10000){
   						if(!Commonjs.isEmpty(d.Data)){
   							if(d.Data.length<=1){
   								$("#hosNameSelect").hide();
   								selectHospitalId = d.Data[0].HosId;
   								$("#hosName").html(d.Data[0].HosName);
   								$("#hosName").show();
   							}else{
   								var hosHtml = '';
	   							$.each(d.Data,function(i,hos){
	   								var selected = '';
	   								if(i==0){
	   									selected = 'selected';
	   									selectHospitalId = hos.HosId;
	   								}
	   								hosHtml += '<option value="'+hos.HosId+'" '+selected+'>'+hos.HosName+'</option>';
	   							})
	   							$("#hosNameSelect").html(hosHtml);
	   							$("#hosName").hide();
	   							$("#hosNameSelect").show();
   							}
   						}
   					}
   					hospital.findData();
   				},{async:false});
    }
	function clearNoNum(obj){   
	  
		obj = obj.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符  

		obj = obj.replace(/^\./g,"");  //验证第一个字符是数字而不是. 
	
		obj = obj.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.   
	
		obj = obj.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		return obj;

	}
	
	function initUe(val){
		$("#showBrief").html('');
		$("#showBrief").append("<script id=\"editor\" type=\"text/plain\" style=\"width:100%;height:150px;\"></script>");
		var UE = new baidu.editor.ui.Editor(editorOption);
		UE.render('editor');
		UE.ready(function() {
			//editor_a.hide();//隐藏编辑器
			UE.setContent(val);
			  //赋值给UEditor du.Data.Contents
		});
		//UE.setHeight(300);
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
		var id1 =  newGuid();
		var id2 =  newGuid();
		var id3 =  newGuid();
		var html = '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
				+ id1
				+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:120px;width:120px;cursor: pointer;" title="点击上传图片" value="点击上传图片" onchange=upload("'
				+ id1
				+ '","SmallImgV1"); onpaste="return false;" type="file" name="newsFile"><img id="SmallImgV1" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:120px;width:120px;"  /></div>'
		html += '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
			+ id2
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:120px;width:120px;cursor: pointer;" title="点击上传图片" value="点击上传图片" onchange=upload("'
			+ id2
			+ '","SmallImgV2"); onpaste="return false;" type="file" name="newsFile"><img id="SmallImgV2" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:120px;width:120px;"  /></div>';
		html += '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
			+ id3
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:120px;width:120px;cursor: pointer;" title="点击上传图片" value="点击上传图片" onchange=upload("'
			+ id3
			+ '","SmallImgV3"); onpaste="return false;" type="file" name="newsFile"><img id="SmallImgV3" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:120px;width:120px;"  /></div>';
		$("#updateSmallImg").empty();
		$("#updateSmallImg").append(html);
	
	}

	function upload(id,image) {	
		var filename = $("#"+id).val();
		var index = filename.lastIndexOf('.');
		var type = filename.substring(index+1,filename.length);
		if(type.toLowerCase() != 'jpg' && type.toLowerCase() != 'gif'
			&& type.toLowerCase() != 'png'&&type.toLowerCase() != 'jpeg'){
			YihuUtil.art.warning('注意喔：图片格式必须为.jpeg|.gif|.jpg|.png','warning');
			return ;
		}
	
		var arrID = [ id ];
		$.yihuUpload.ajaxFileUpload( {
			url : "/upload/uploadFile.do", // 用于文件上传的服务器端请求地址
			secureuri : false,// 一般设置为false
			fileElementId : arrID,// 文件上传空间的id属性 <input type="file" id="file" name="file" />
			data: 'token='+Commonjs.getToken(),							
			dataType : 'json',// 返回值类型 一般设置为json
			success : function(data, status) {
				var uri = data.url;
				uri=uri.replace('fullsize','small');
				var uri_target = location.protocol + '//' + location.host +"/"+uri;
				
				var name = data.NewFileName;  
				var fname = data.FileName;
				var size = data.Size;
				var old = $("#" + id + "_f");	
	            if (image=='SmallImgV1') {
	            	$("#SmallImgV1").attr("src", uri_target);
	            	isImageUrl[0] = true ;
				}else if (image=='SmallImgV2') {
					$("#SmallImgV2").attr("src", uri_target);
					$("#ImgUrl").val(uri_target);
					$("#hidVal").val(uri_target);
	            	isImageUrl[1] = true ;
				}else if (image=='SmallImgV3') {
					$("#SmallImgV3").attr("src", uri_target);
					$("#ImgUrl").val(uri_target);
					$("#hidVal").val(uri_target);
	            	isImageUrl[2] = true ;
				}
		},
		error : function(data, status, e) {
			YihuUtil.art.warning("图片上传失败：建议您选择不超过1M的图片且在良好的网络环境下继续上传");
		}
	
		});
	}
  var hospital = function(){
	  var queryHospital = function(){
		  var param = {};
		  var Service = {};
		  Service.HosId = selectHospitalId;
//		  var params = Commonjs.getParams(2006,Service);//获取参数
//		  param.params = Commonjs.jsonToString(params);
		  param.reqParam = Commonjs.getApiReqParams(Service);
		  Commonjs.ajaxSync('/basic/queryHospital.do',param,function(backData){
			  $("#showInfo").show();
				if(backData.RespCode == 10000){
					$("#showInfo").html(unescape(backData.Data[0].HosBrief));
					$("#hosAddr").text(backData.Data[0].Address);
					$("#hosProvince").text(backData.Data[0].Province);
					$("#hosTel").text(backData.Data[0].Tel);
					var obj = backData.Data[0];
					if(!Commonjs.isEmpty(obj.PhotoUrl)){
						var urlObj = JSON.parse(obj.PhotoUrl);
						if(!Commonjs.isEmpty(urlObj.photo1)){
							var photo = urlObj.photo1;
							$("#SmallImgV1").attr("src",unescape(photo));
							$("#tempImg1").attr("src",unescape(photo));
			            	isImageUrl[0] = true ;
						}
						if(!Commonjs.isEmpty(urlObj.photo2)){
							var photo = urlObj.photo2;
							$("#SmallImgV2").attr("src",unescape(photo));
							$("#tempImg2").attr("src",unescape(photo));
			            	isImageUrl[1] = true ;
						}
						if(!Commonjs.isEmpty(urlObj.photo3)){
							var photo = urlObj.photo3;
							$("#SmallImgV3").attr("src",unescape(photo));
							$("#tempImg3").attr("src",unescape(photo));
			            	isImageUrl[2] = true ;
						}
					}
					$("#hosX").text(backData.Data[0].CoordinateX);
					$("#hosY").text(backData.Data[0].CoordinateY);
					$("#hosRoute").text(backData.Data[0].HosRoute);
					$("#hosName").text(backData.Data[0].HosName);
					$("#hosLevel").val(backData.Data[0].HosLevel);
					//$("#hosLevel").text(backData.Data[0].Level);
					$("#hosLevel").hide();
					$("#_hoslevel").html(backData.Data[0].HosLevelName);
					$("#updateSmallImg").hide();
					contend = unescape(backData.Data[0].HosBrief);
//					setTimeout(function(){
//						initUe(contend);
//					},300);
					initUe(contend);
				
					$('#cancel').click();
					//var val = backData.Data[0].Level;
				//	$("#hosLevel option[value='"+val+"']").attr("selected", true);
					//$("#hosRemark").text(backData.Hos.Remark);
					/*
					 var editor_a = new baidu.editor.ui.Editor(editorOption);
				     editor_a.render('editor');
				     editor_a.ready(function() {
				     //editor_a.hide();//隐藏编辑器
				     editor_a.setContent(unescape(backData.Data[0].HosBrief));  //赋值给UEditor
				     $("#showInfo").html(unescape(backData.Data[0].HosBrief));
				     });
				     */
				}
		  })
	  }
	  var editInfo = function(){
		  
	  }
	  return{
		  findData : queryHospital,
		  updateHos : editInfo
	  }
	  
  }();