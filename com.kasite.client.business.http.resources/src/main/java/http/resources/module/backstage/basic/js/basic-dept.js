var firstData={};
var firstIs = false;
var curDeptId = 0;
var curParentDeptCode = -1;
var ieType = 10;
var type = "";
var attachBuild = "";
var selectHospitalId = '';
$(function(){
	//泉州附二屏蔽该按钮
	if(Commonjs.hospitalId == "59" || Commonjs.hospitalId == 59){
		$("#dataSynchronous").hide();
	}
		loadHospitalListLocal();
	    ieType = browse();
	    type = "";
//		Commonjs.getJscrollpane.destroy();
// 		deptDeal._loadAllDeptInfo();
	    $("#hosNameSelect").change(function(){
		   	selectHospitalId =  $("#hosNameSelect").val();
		    loadDeptInfo();
		})
		
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
        loadDeptInfo();

    	$('#deptList > ul > li').hover(function(){
			$(this).children('a').addClass('hover');
//			$(this).children('ul').show();
//			$(this).append('<span class="arrow arrowR"></span>');
			var html =$(this).children('ul').html();
			if(html != undefined){
				$("#twoLeveDept").html("<ul>"+html+"</ul>");
				Throttle(fetureHeight(),50,30);
	    		Throttle(infoListScroll(),50,30);
	    		Throttle(userListScroll(),50,30);
			}else {
				$("#twoLeveDept").html("");
			}
		},function(){
			$(this).children('a').removeClass('hover');
//			$(this).children('ul').hide();
//			$(this).children('span').remove();
		});


    	$("#deptCancel").hide();
    	$('#edit-btns').on('click',function(){
    		$("#deptCancel").hide();
    		var $add=$('.info-edit-address'),
    			$text=$('#intro'),
    			$btn=$(this);
    		if($(this).hasClass('edityes')){
    			$("#deptCancel").hide();
    			$btn.text('编辑').removeClass('edityes');
    			//intro = intro.replace(/<br\s*\/?>/g,'\n');
    			
    			$("#intro").html($("#intro").find('textarea').val());
    			$("#addr").html($("#addr").find('textarea').val());
    			$("#orderCol").html($("#orderCol").find('input').val());
    			$("#selDeptType").val();
//    			$("#deptType").html($("#deptType").find('select').val());
//    			$("#deptType").html($("#deptType").find('select').val());
    			$("#deptTel").html($("#deptTel").find('textarea').val());
    			type = $("#selDeptType").val();
    			var param = {};
    			var Service = {};
    			Service.HosId = selectHospitalId
    			Service.DeptCode = curDeptId;
    			Service.DeptAddr =  $("#addr").html();
    			Service.DeptType =  $("#selDeptType").val();
    			Service.DeptTel =  $("#deptTel").html();
    			Service.OrderCol =  $("#orderCol").html();
    			Service.ParentDeptCode = curParentDeptCode;
				var tem = $("#intro").html();
				tem = tem.replace(/\n/g,'<br/>');
				$("#intro").html(tem);
				Service.DeptBrief =tem;
				param.reqParam = Commonjs.getApiReqParams(Service);
				Commonjs.ajaxSync('/basic/updateDept.do',param,function(d){
					if(d.RespCode == '10000'){
						art.dialog({
							lock: true,
							width: '300px',
							height: '100px',
						    time: 3,
						    content: d.RespMessage
						});
						  $("#deptCancel").hide();
					}
				});
    		}else{
    			$("#deptCancel").show();
    			$btn.text('保存').addClass('edityes');
    			var temp = $text.html().replace(/<br\s*\/?>/g,'\n');
    			$add.html('科室位置<br><br><textarea class="textarea" style="width:'+($add.width()-20)+'px; height:30px">'+$add.text()+'</textarea>');
    			$text.html('科室简介<br><br><textarea class="textarea" style="width:'+($text.width()-20)+'px; height:120px">'+temp+'</textarea>');
    			
    			$('#orderCol').html('科室排序&emsp;&emsp;<input type="text" id="txtOrderCol" class="text" value="'+$("#orderCol").html()+'"  />');
//    			$('#deptType').html('科室类型&emsp;&emsp;<select id="selDeptType"><option value="">请选择</option><option value="1">住院</option><option value="2">门诊</option></select>');
    			$("#selDeptType").val(type);
    			//$('#orderCol').html('科室排序<br><br><textarea class="textarea" style="width:'+($('#orderCol').width()-20)+'px; height:30px">'+temp+'</textarea>');
    			$('#deptTel').html('科室电话<br><br><textarea class="textarea" style="width:'+($('#deptTel').width()-20)+'px; height:30px">'+$("#deptTel").html()+'</textarea>');
    			$("#divDept").height($(window).height()-161).jScrollPane({"autoReinitialise": true,"overflow":true});
    		}
    	});
    	$('#deptCancel').on('click',function(){
    	
    		 deptDeal._loadDeptEditInfo(curDeptId);
    		 var $add=$('.info-edit-address'),
 			 $text=$('#intro'),
 			 $btn=$('#edit-btns');
    		 $btn.text('编辑').removeClass('edityes');
              $(this).hide();
    	
    });
    	function fetureHeight(){
    		var h=$(window).height()-111;
    		if($('.info-doctor').outerHeight(true)>$(window).height()){
    			
    			$('.info-details-fl').height($('.info-doctor').outerHeight(true)-49);
    		}else{
    			$('.info-doctor').height(h);
//    			$('.info-office-list').height(h-49).jScrollPane({"overflow":true});
    			$('.info-details-fl').height(h-49);
    		}
    	} 
    	fetureHeight();
    	
    	function infoListScroll(){
    			$(".info-office-box").height($(window).height()-161).jScrollPane({"autoReinitialise": true,"overflow":true});
    	}
    	infoListScroll();
    	
    	function userListScroll(){
    			$(".info-details-box").height($(window).height()-161).jScrollPane({"autoReinitialise": true,"overflow":true});
    	}
    	userListScroll();
    	
    	function deptScroll(){
			$("#divDept").height($(window).height()-161).jScrollPane({"autoReinitialise": true,"overflow":true});
    	}
    	deptScroll();
    	$(window).resize(function(){
    		Commonjs.getJscrollpane.destroy();
    		Throttle(fetureHeight(),50,30);
    		Throttle(infoListScroll(),50,30);
    		Throttle(userListScroll(),50,30);
    		Throttle(deptScroll(),50,30);
    	});
    	/*
    	   $('.doctor-textarea-focu').on('focus keydown keyup',function(){TextareaCount()});
       	
       	function TextareaCount(){
       		var $this=$('.doctor-textarea-focu'),
       			$tips=$('.art-doctor-tips');
       		var $val=$.trim($this.val());		
       		if(150-$val.length<0){
       			$tips.html('您已经超出<span>'+($val.length-150)+'</span>个字了哦');			
       		}else if (150-$val.length==0){
       			$tips.html('您已经输满<span>'+150+'</span>个字了哦');
       		}else if(150-$val.length==150){
       			$tips.html('<span class="old">0</span> 个字符，您还可以编辑 <span class="pck">150</span> 个字符');
       		}
       		else{
       			$tips.html('<span class="old">'+($val.length)+'</span> 个字符，您还可以编辑 <span class="pck">'+(150-$val.length)+'</span> 个字符');
       		}
       	}
       	*/
       	$('#adb-title').on('focus keydown',function(){ if($(this).val() == '请输入职称！'){
       		$(this).val('');
       	}});
       	
       	$('#dataSynchronous').on('click',function(){
       		var param = {};
       		var Service = {};
       		Service.HosId = selectHospitalId
       		param.reqParam = Commonjs.getApiReqParams(Service);
    		Commonjs.ajaxSync('/basic/dataSynchronous.do',param,function(d){
    			if(d.RespCode == 10000){
    				art.dialog({
	                    lock: true,
	                    width: '300px',
	                    height: '100px',
	                    time: 3,
	                    content: "同步成功",
	                    ok: function () {
	                    	window.location.reload();
	                    }
    				});
        		}else{
        			art.dialog({lock: true,width: '300px',height: '100px',time: 3,content: "同步失败"});
        		}
    		});
       	});
       	
});  


function loadHospitalListLocal(){
    	 var param = {};
		 var Service = {};
		 param.reqParam = Commonjs.getApiReqParams(Service);
    	 Commonjs.ajax('/basic/queryHospitalListLocal.do',param,function(d){
   					if(d.RespCode == 10000){
   						if(!Commonjs.isEmpty(d.Data)){
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
   							if(d.Data.length<=1){
   								$("#hosNameSelect").hide();
   							}
   						}
   					}
   				},{async:false});
    }
    
function checkMobile(str) {
   var re = /^1\d{10}$/
   if (re.test(str)) {
     return true;
   } else {
      return false;
   }
}


function editDoctor(doctorId, deptCode){
	deptDeal._loadDoctorInfoById(deptCode,doctorId);
	var contents=$('.art-doctor-box').get(0);

		var artBox=art.dialog({
			lock: true,
			artIcon:'edit',
			opacity:0.4,
			width: 900,
			padding: '20px 20px 0',
			title:'编辑',
			header:false,
			content: contents,
		
			ok: function () {
			var docInfo = {};
			docInfo.doctorName =$("#adb-name").val();
			docInfo.doctorSex =$("#adb-sex").val();
			docInfo.tel =$("#adb-tel").val();
			docInfo.title =$("#adb-title").val();
			docInfo.teachTitle =$("#teachTitle").val();
			docInfo.show =$("#show").val();
			docInfo.doctorDegree =$("#doctorDegree").val();
			docInfo.introduction = $("#adb-intro").val();
			docInfo.spec = $("#spec").val();
			docInfo.doctorCode = doctorId;
			docInfo.deptCode = deptCode;
			docInfo.orderCol =$("#orderCol").val();			
			if(!checkMobile(docInfo.tel) && docInfo.tel != ''){
				art.dialog({
					lock: true,
					width: '300px',
					height: '100px',
				    time: 3,
				    content: '手机号格式有误'
				});
				return false;
			}
			
			if( Commonjs.isEmpty(docInfo.doctorName)){
				art.dialog({
					lock: true,
					width: '300px',
					height: '100px',
				    time: 3,
				    content: '姓名不能为空'
				});
				return false;
			}
			deptDeal._saveDoctor(docInfo);
			//console.log("editDoctor" +curDeptId)
		    deptDeal._loadDoctorInfo(curDeptId,1);
		    
		},		
		cancel: true
	});	
}
	var saveDotorInfo = function(docInfo){
		var param = {};
		var Service = {};
		Service.HosId = selectHospitalId
		Service.Introduction = docInfo.introduction;
		Service.Spec = docInfo.spec;
		Service.DeptCode = docInfo.deptCode;
		Service.DoctorCode = docInfo.doctorCode;
		var photoUrl =$("#SmallImgV").attr("src");
		var regex = location.protocol + '//' + location.host;
		photoUrl = photoUrl.replace(regex, "");
		Service.PhotoUrl = photoUrl;
		if( Commonjs.isEmpty(docInfo.doctorSex)){
			Service.DocotrSex = '0';
		}else{
			Service.DocotrSex = docInfo.doctorSex;
		}
		Service.OrderCol = docInfo.orderCol;
		Service.Tel = docInfo.tel;
		Service.DoctorName = docInfo.doctorName;
		Service.Title = docInfo.title;
		Service.TeachTitle = docInfo.teachTitle;
		Service.IsShow = docInfo.show;
		Service.DoctorDegree = docInfo.doctorDegree;
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/basic/updateDoctor.do',param,function(d){
			if(d.RespCode == 10000){
				art.dialog({
					lock: true,
					width: '300px',
					height: '100px',
				    time: 3,
				    content: "更新成功!"
				});
			
			}else{
				Commonjs.alert(d.RespMessage);
			}
		});
	}
   var loadDeptInfo = function(){
	    var param = {};
		var Service = {};
		Service.HosId = selectHospitalId;
		Service.IsShow = "1";
	    param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajax('/basic/queryDeptInfoByHosIdForTree.do',param,function(d){
			if(d.RespCode == 10000){
				if(ieType == 8){
					deptDeal._getUIForIe(d.Data);
				}else{
					deptDeal._getUl(d.Data);
				}
			}
		});
   }

   var findDept = function(deptId){
	   $btn=$('#edit-btns');
	   $("#deptCancel").hide();
	   $btn.text('编辑').removeClass('edityes');
	 //  alert(deptId);
	   curDeptId = deptId;
	   deptDeal._loadDeptEditInfo(deptId);
	   deptDeal._loadDoctorInfo(deptId,1);
	   curDeptId = deptId;
	 
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
				+ '","SmallImgV"); onpaste="return false;" type="file" name="newsFile"><img id="SmallImgV" src="http://f1.yihuimg.com/TFS/upfile/WBJ/1024727/2014-07-23/003320_1406097193619_fullsize.jpg" style="height:120px;width:120px;"  /></div>';
		$("#updateSmallImg").empty();
		$("#updateSmallImg").append(html);
	
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
	            if (image=='SmallImgV') {
	            	$("#SmallImgV").attr("src", uri_target);
	            	$("#ImgUrl").val(uri_target);
	            	$("#hidVal").val(uri_target);
				}
		},
		error : function(data, status, e) {
			//YihuUtil.art.warning("图片上传失败：建议您选择不超过1M的图片且在良好的网络环境下继续上传");
		}
	
		});
	}
   
  var deptDeal = function(){
	  var getUiIe = function(data){
		  // var ui =$("<ul></ul>");
		   var ui = "<ul>";
		   var obj={};
		  
		   $.each(data,function(k,v){
			   var litext ='';
			   if(!firstIs){
				   obj.id = v.id;
				   obj.val = v.text;
				   firstData = obj;
				   firstIs = true;
			   }
			   if(v.text.length > 10){
				   litext = litext + "<li title="+"'"+v.text+"'"+"><a href=\"javascript:;\" onclick=\"findDept("+"'"+v.id+"'"+")\">"+v.text+"</a></li>";
			   }else{
				   litext = litext + "<li><a href=\"javascript:;\" onclick=\"findDept("+"'"+v.id+"'"+")\">"+v.text+"</a></li>";
			   }
			  
			  if(typeof(v.children.id) != 'undefined'  && v.children.text != undefined){
				  var _textArr = [];
				  var _idArr = [];
				  var _ui = "<ul>";
				  if($.isArray(v.children.text ) ){
					    _textArr = v.children.text;
					   _idArr = v.children.id;
					  $.each(_textArr,function(v1){
						  var li_text ='';
						  var _id =_idArr[v1];
						  li_text = li_text +"<li><a href=\"javascript:;\" onclick=\"findDept("+"'"+_id+"'"+")\">"+this+"</a></li>";
					  });
				  }else{
					  if(v.text.length > 10){
						  li_text = li_text +"<li title="+"'"+v.children.text+"'"+"><a href=\"javascript:;\" onclick=\"findDept("+"'"+v.children.id+"'"+")\">"+v.children.text+"</a></li>";
					   }else{
						  li_text = li_text +"<li><a href=\"javascript:;\" onclick=\"findDept("+"'"+v.children.id+"'"+")\">"+v.children.text+"</a></li>";
					   }
					
				
				  }				  
				  _ui = _ui + li_text+"</ul>";
				  
				  litext= litext + _ui;
			  }
			  ui = ui + litext;
			  
		  }) ;
		   ui = ui +"</ul>";
		  $("#deptList").html(ui);
		  if(obj.id !='' && obj.id != undefined){
			  findDept(obj.id);
		  }
	   }
	   var getUl = function(data){
		   var ui =$("<ul ></ul>");
		   var obj={};
		   $.each(data,function(k,v){
			   var litext ='';
			   if(k==0){
				   obj.id = v.Id;
				   obj.val = v.Text;
				   firstData = obj;
				   firstIs = true;
			   }
			   if(v.Text.length > 10){
				   var li =$("<li title="+v.Text+"><a href=\"javascript:findDept("+"'"+v.Id+"'"+")\">"+v.Text+"</a></li>");
			   }else{
				   var li =$("<li><a href=\"javascript:findDept("+"'"+v.Id+"'"+")\">"+v.Text+"</a></li>");
			   }
			  if(typeof(v.Children) != 'undefined'  && v.Children != undefined){
				  var _ui =$("<ul ></ul>");
				  if($.isArray(v.Children ) ){
					  $.each(v.Children,function(v1,v2){
						  if(v2.Text != undefined){
							  var _li =$("<li><a href=\"javascript:findDept("+"'"+v2.Id+"'"+")\">"+v2.Text+"</li>");
							  _ui.append(_li);
						  }
					  });
				  }else{
					  if(v.Children.Text != undefined ){
						  if(v.Text.length > 10){
							  var _li =$("<li title="+v.Children.Text+"><a href=\"javascript:findDept("+"'"+v.Children.Id+"'"+") \" >"+v.Children.Text+"</li>");
						   }else{
							   var _li =$("<li><a href=\"javascript:findDept("+"'"+v.Children.Id+"'"+") \">"+v.Children.Text+"</li>");
						   }
						
						  _ui.append(_li); 
					  }
					 
				  }				  
				  
				  li.append(_ui);
			  }
		
			  ui.append(li);
			  
		  }) ;
		  
		  $("#deptList").html(ui);
		  if(obj.id !='' && obj.id != undefined){
			  findDept(obj.id);
		  }
		  
		  
	   }
	
	   var loadEditInfo = function(deptId){
		   type = "";
		   var Service = {};
		   Service.HosId = selectHospitalId
		   Service.DeptCode = deptId;
		   var page = {};
		   page.PIndex = 0;
		   page.PSize = 100;
		   Service.Page = page;
		   var param = {};
		   param.reqParam = Commonjs.getApiReqParams(Service);
		   Commonjs.ajax('/basic/queryDept.do',param,function(backData){
			   if(backData.RespCode != 10000 ){
				   Commonjs.alert(backData.RespMessage);
				   return;
			   }
			   $(".info-edit-top").html("");
				$("#addr").html("");
				$("#intro").html("");
				$("#orderCol").html("");
//				$("#selDeptType").val();
				$("#deptTel").html("");
				if(backData.Data!=null && backData.Data!=undefined){
					$(".info-edit-top").html(backData.Data[0].DeptName);
					$("#addr").html(backData.Data[0].Address);
					var intro = backData.Data[0].Intro;
					//intro = intro.replace(/<br\s*\/?>/g,'\n');
					$("#intro").append(intro);
					$("#orderCol").html(backData.Data[0].OrderCol);
					$('#deptType').html('科室类型&emsp;&emsp;<select id="selDeptType"><option value="">请选择</option><option value="1">住院</option><option value="2">门诊</option></select>');
					$("#selDeptType").val(backData.Data[0].DeptType);
					type = backData.Data[0].DeptType;
					$("#deptTel").html(backData.Data[0].DeptTel);
					curParentDeptCode = backData.Data[0].ParentId;
				}
		   });
	   }
       var loadDoctorInfoByDoctorCode = function(deptCode,doctorCode){
    		$("#adb-name").val("");
			$("#adb-user").val("");
			$("#adb-tel").val("");
			//$("#adbImg").attr("src",unescape(backData.Doctor.Url));
			$("#adb-dept").val("");
			$("#spec").val("");
			$("#intro").val("");
			$("#orderCol").val("");
			var Service = {};
			Service.HosId = selectHospitalId
			Service.DeptCode = deptCode;
			Service.DoctorCode = doctorCode;
			var param = {};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/basic/queryDoctor.do',param,function(backData){
				if(!Commonjs.isEmpty(backData)){
					if(backData.RespCode == 10000 ){
						if( !Commonjs.isEmpty(backData.Data) && backData.Data.length>0 ){
							var docInfo = backData.Data[0];
							$("#adb-name").val(docInfo.DoctorName);
							if( Commonjs.isEmpty(docInfo.DoctorTitle)){
								$("#adb-title").val("请输入职称！");
							}else{
								$("#adb-title").val(docInfo.DoctorTitle);
							}
				    	    var tempUrl = "";
							if( Commonjs.isEmpty(docInfo.PhotoUrl)){
								 tempUrl ="/common/images/male.png";
							}else{
								tempUrl =unescape(docInfo.PhotoUrl)
							}
							if( docInfo.TeachTitle === "" || docInfo.TeachTitle == undefined || docInfo.TeachTitle == null){
								$("#teachTitle").val("");
							}else{
								
								$("#teachTitle").val(docInfo.TeachTitle);
							}
							$("#show").val(docInfo.IsShow);
							$("#doctorDegree").val(docInfo.DoctorDegree);
							$("#SmallImgV").attr("src",tempUrl);
							$("#adb-sex").val(docInfo.Sex);
							if( Commonjs.isEmpty(docInfo.RelativeDeptName)){
								$("#adb-depart").text(docInfo.RelativeDeptName);
							}else{
								$("#adb-depart").text('无');
							}
							if(!Commonjs.isEmpty(docInfo.DoctorTel) ){
								$("#adb-tel").val(docInfo.DoctorTel);
							}else{
								$("#adb-tel").val('');
							}
							$("#orderCol").val(docInfo.OrderCol);
							$("#spec").val(docInfo.Spec);
							$("#adb-intro").val(docInfo.Intro);
							$("#adb-dept").text(docInfo.DeptName);
							//console.log(backData.result.DeptName);
							$("#zhishu").text((Commonjs.isEmpty(docInfo.Intro)) ? 0 : docInfo.Intro.length);
							$(".old").text((Commonjs.isEmpty(docInfo.Intro)) ? 0 : docInfo.Intro.length);
						}else{
							Commonjs.alert('查询不到医生信息！');
						}
					}else{
						Commonjs.alert(backData.RespMessage);
					}
				}else{
					Commonjs.alert('系统错误！查询医生信息出错！');
				}
			});
       }
	   var loadDoctorInfo = function(deptId,index){
		   	var Service = {};
		   	Service.HosId = selectHospitalId
		    Service.DeptCode = deptId;
			$('#pagenumber').val(index);
			var pageIndex = index;
			var pageSize = 10;
			var page = {};
			page.PIndex = pageIndex;
			page.PSize = pageSize;
			Service.Page = page;
			var param = {};
			param.reqParam = Commonjs.getApiReqParams(Service);
			Commonjs.ajax('/basic/queryDoctor.do',param,function(backData){
				$("#docTable").html('');
				if(backData.RespCode == 10000){
					//  $("#docTable").empty();
					  var ui =$("<ul></ul>");
					  var tep = [];
					  if(backData.Data == undefined){
						  $('#docTable').html("<ul align=center><li><font color=red>未找到相关数据!</font></li></ul>");
						  return;
					  }
					  if(!$.isArray(backData.Data)){
							if( backData.Data.length == undefined && !Commonjs.isEmpty(backData.Data.DoctorCode)){
								tep.push(backData.Data);
								backData.Data = tep;
							}
					  }
					  $.each(backData.Data,function(k,v){

						  var li =$("<li></li>")
						  
						  if(v.PhotoUrl == '' || v.PhotoUrl ==undefined){
							  var _divOne = $("<div class=\"img\"><img src=\"/common/images/male.png\"></div>");
							  li.append(_divOne);
						  }else{
						  var _divOne = $("<div class=\"img\"><img src=\""+unescape(v.PhotoUrl)+"\"></div>");
						  li.append(_divOne);
						  }
						  if(v.DoctorName == undefined  || v.DoctorName ==''){
							  var _divTwo = $("<div class=\"use\"></div>");
							  li.append(_divTwo);
						  }else{
							  var _divTwo = $("<div class=\"use\">"+v.DoctorName+"</div>");
							  li.append(_divTwo);
						  }
						  if(v.DoctorTitle == undefined || v.DoctorTitle ==''){
							  var _divThe = $("<div class=\"txt nowrap\"></div>");
							  li.append(_divThe);
						  }else{
							  var _divThe = $("<div class=\"txt nowrap\">"+v.DoctorTitle+"</div>");
							  li.append(_divThe);
						  }
						  var cal = "javascript:editDoctor('"+v.DoctorCode+"','"+v.DeptCode+"')";
						  var _divFour = $("<div class=\"btns\" ><a href=\"javascript:;\" onclick="+cal+" class=\"edit-doctor\">编辑</a></div>");
						  li.append(_divFour);
						  ui.append(li);
					  });
					 
					 $("#docTable").html(ui);
					 if($("#docTable").parent().css("top")!='0px'){
					  	$("#docTable").parent().css("top","0px");
					 }
				}else {
					Page(0,pageSize,'pager');
					return;
				}
//				if((backData.Page.PCount != undefined && backData.Page.PCount!=0) || (backData.Page.PCount==0 && index==1)){
//					$("#totalcount").val(backData.Page.PCount);
//				}
				Page(backData.Page.PCount,backData.Page.PSize,'pager');
			})
	   }
	  /* var loadAllDept = function(){
		   var Service = {};
			var page = {};
			var code = 1001;
			Service.Page = page;
			page.PIndex = 0;
			page.PSize = 100;
			var param = {};
			var params = Commonjs.getParams(code,Service);//获取参数
			param.Api = "QueryDept";
			param.Params = Commonjs.jsonToString(params);
			var backData = Commonjs.ajax('./basic_callBasicApi.do',param,false);
			if(backData.RespCode == '10000'){
				 $.each(backData.result,function(k,v){
						$("#adb-dept").append($("<option>").val(v.Code).text(v.Name));
				 });
			
			}else{
				art.dialog({
					lock: true,
					width: '300px',
					height: '100px',
				    time: 3,
				    content: backData.RespMessage
				});
			}
	   }*/
	   return {
		   _getUl : getUl,
		   _getUIForIe : getUiIe,
		   _loadDeptEditInfo : loadEditInfo,
		   _loadDoctorInfo : loadDoctorInfo,
		   _loadDoctorInfoById : loadDoctorInfoByDoctorCode,
		   _saveDoctor : saveDotorInfo,
		   //_loadAllDeptInfo : loadAllDept
	   }
  }();
  function browse(){
	  var browser=navigator.appName ;
	  var b_version=navigator.appVersion ;
	  var version=b_version.split(";"); 
	  var trim_Version='';
	  if(version.lenth!=undefined&&version.lenth>1) {
		  trim_Version = version[1].replace(/[ ]/g,"");
		  if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE11.0") { 
			  return 11;
		  }
		  else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE10.0") { 
			  return 10;
		  }else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE9.0") { 
			  return 9;
		  }else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0")  { 
			  return 8;
		  }else{
			  return 1;
		  } 
	  }else {
		  return 1;
	  }
	  
	  
	 

  }
  function Page(totalcounts, pagecount,pager) {
  	$("#"+pager).pager( {
	  		totalcounts : totalcounts,//总记录数
	  		pagesize : 10,
	  		pagenumber : $("#pagenumber").val(),//当前页
	  		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),//总页数
	  		buttonClickCallback : function(al) {
	  			$("#pagenumber").val(al);
				 deptDeal._loadDoctorInfo(curDeptId,al);
	  			//queryDetail(al);
	  		}
	  	});
  }
  
