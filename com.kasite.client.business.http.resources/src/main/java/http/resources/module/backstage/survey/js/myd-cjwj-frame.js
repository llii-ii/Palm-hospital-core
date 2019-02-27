
	var session = YihuUtil.getSession();
	
	$(function(){
		showModel(1);  
	});

	function showModel(type){
		$('#cs1').removeClass('on');
		$('#cs2').removeClass('on');
		$('#cs'+type).attr('class','on');
		for(var i = 1;i<=2;i++){
			if(i == type){
				$('#cs'+i).attr('class','on');
				$('#frame_'+i).show();
			}else{
				$('#cs'+i).removeClass('on');
				$('#frame_'+i).hide();
			}
		}
		var url1 = 'myd-cjwdwj.html';
		var url2 = 'myd-cjyywj.html';
		var url = 'myd-cjyywj.html';
		if(type == 1){
			url = url1;
		}else url = url2;
		if(isEmpty($('#frame_'+type).attr('src'))){
			$('#frame_'+type).attr('src',url)
		}	
	}
 	
  	function _ajax(url,param,flag){
		
		var obj = null;
		try{
			$.ajax({
				type: 'POST',
				url: url,
				data: param,
				async: flag,
				timeout : 8000,
				dataType: 'json',
				success: function(data){
					obj = data;
	 			}
			});
		}catch(err){
			ComWbj.alertIconNo('提示：',err,'error');
		}
		if(!flag) return obj;
	}
	  
  
  	function isEmpty(s){
  		
  		if(s == undefined){
  			return true;
  		}else{
  			if(s == null || s == '' ||
  				s == 'null' || s.length < 1){
  				return true;
  			}
  		}
  		return false;
  	}
