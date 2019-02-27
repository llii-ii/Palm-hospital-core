var smallClick =1;
var bigClick =1;
var isSaveSImg = false;//是否保存小图地址
var isSaveBImg = false;//是否保存大图地址
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
		 		        // 'insertcode', //代码语言
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
	 	catchRemoteImageEnable:false,
	 	scaleEnabled:true

    };
var formate_data =[{'id':'0','text':'新闻动态'}, //泉州儿童
                   {'id':'1','text':'通知公告'},// 健康讲座
                   {'id':'2','text':'健康宣教'},// 健康讲座
                   {'id':'3','text':'就诊指南'},// 就诊指南
                   {'id':'4','text':'住院需知'}];//住院需知
$(function(){
	    initImage();
	    initUe('');
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
		//_query();
    });

function clearData(){
	 $("#dtype").val(0);
	 $("#title").val('');
	 $("#dlink").val('');
	 //$("#dstatus").val(0);
	 $("#dtypeClass").val(0);
	 $('#SmallImgV').attr('src','http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg');
	 $('#BigImgV').attr('src','http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg');

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

function checkMsg(){
	var title = $("#dtitle").val();
	var contents = UE.getEditor('editor').getContent();
	var link = $("#dlink").val();
	var dtype = $("#dtype").val();
	var msg = '';
	if(Commonjs.isEmpty(title)){
		msg+="请输入文章标题!\n";
	}
	if(dtype=='0'&&Commonjs.isEmpty(contents)){
		msg+="请输入文章内容!";
	}else if(dtype=='1'&&Commonjs.isEmpty(link)){
		msg+="请输入链接!";
	}
	return msg;
	
}

function saveInfo(){
	
	
	var msg = checkMsg();
	if(!Commonjs.isEmpty(msg)){
		art.dialog({
		lock: true,
		width: '300px',
		height: '100px',
	    time: 3,
	    content: msg
	});
		return;
		
	}
	save($("#dtype").val(),$("#dtitle").val(),$("#SmallImgV").attr('src'),$("#BigImgV").attr('src'),UE.getEditor('editor').getContent(),$("#dstatus").val(),$("#dlink").val(),$("#dtypeClass").val());
	var typ = $("#dtype").val() == undefined ? 0 : $("#dtype").val();
}
function save(dtype,title,smallPic,bigPic,contents,status,link,typeClass){
	var param = {};
    var Service = {};
    Service.HosId = Commonjs.getUrlParam("HosId");
	Service.Title =title;
	var regex = location.protocol + '//' +location.host;
	if(isSaveSImg){
		var smallImgUrl = "<![CDATA["+smallPic+"]]>";
		smallImgUrl = smallImgUrl.replace(regex,"");
		Service.ImgUrl = smallImgUrl;
	}
	if(isSaveBImg){
		var bigImgUrl = "<![CDATA["+bigPic+"]]>";
		bigImgUrl = bigImgUrl.replace(regex,"");
		Service.BigImgUrl = bigImgUrl;
	}
	Service.TypeClass = typeClass;
	Service.LinkUrl = link;
	Service.Status = status;
	Service.Contents = escape(contents);
	Service.Type = dtype;
	Service.OperatorId = Commonjs.getUserID();
	Service.OperatorName = Commonjs.getUserName();
	param.reqParam = Commonjs.getApiReqParams(Service);
	Commonjs.ajaxSync('/article/addArticle.do',param,function(du){
		if(du.RespCode == 10000){
			art.dialog({
				lock: true,
				width: '300px',
				height: '100px',
			    time: 3,
			    content: du.RespMessage
			});
			window.location.href='./article.html?isBack=0';
			  clearData();
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
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:120px;width:120px;cursor: pointer;" title="点击上传图片" value="点击上传1图片" onchange=upload("'
			+ id
			+ '","SmallImgV"); onpaste="return false;" type="file" name="newsFile"><img id="SmallImgV" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:120px;width:120px;"  /></div>';
	$("#addSmallImg").empty();
	$("#addSmallImg").append(html);
	
	var id2 = newGuid();
	var html = '<div style="margin-top: 2px; position:relative;" class="sel"><input id="'
			+ id2
			+ '" style="position:absolute;filter:alpha(opacity=0);opacity:0;height:80px;width:230px;cursor: pointer;" title="点击上传图片" value="点击上传1图片" onchange=upload("'
			+ id2
			+ '","BigImgV"); onpaste="return false;" type="file" name="newsFile"><img id="BigImgV" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:80px;width:230px;"  /></div>';
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
		url : "/upload/uploadFile.do", // 用于文件上传的服务器端请求地址
		secureuri : false,// 一般设置为false
		fileElementId : arrID,// 文件上传空间的id属性 <input type="file" id="file" name="file" />
		data: 'token='+Commonjs.getToken(),					
		dataType : 'json',// 返回值类型 一般设置为json
		success : function(data, status) {
			var uri = data.url;
			uri=uri.replace('fullsize','small');
			var name = data.NewFileName;  
			var fname = data.FileName;
			var size = data.Size;
			var old = $("#" + id + "_f");	
			var uri_target = location.protocol + '//' + location.host +"/"+uri;

            if (image=='SmallImgV') {
            	isSaveSImg = true;
            	$("#SmallImgV").attr("src", uri_target);
            	$("#ImgUrl").val(uri_target);
            	$("#hidVal").val(uri_target);
            	smallClick++;
//            	if($("#BigImgUrl").val()==''||$("#BigImgUrl").val()==null){
//            	$("#BigImgV").attr("src", uri);
//				$("#BigImgUrl").val(uri);}
            	
			}else{
            	isSaveBImg = true;
				bigClick++;
				$("#BigImgV").attr("src", uri_target);
				$("#BigImgUrl").val(uri_target);
			}
	},
	error : function(data, status, e) {
		//YihuUtil.art.warning("图片上传失败：建议您选择不超过1M的图片且在良好的网络环境下继续上传");
	}

	});
}
