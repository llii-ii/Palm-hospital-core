
	var session = null;
	
	var SubjectId = 0;
 	var title = '';
 	
	$(function(){
 		SubjectId = QueryString('SubjectId');
 		$('#setting3').prev('.mask').hide().end().fadeOut();
 		queryWJTJData();
	});
	
	function showPie(id,data,title,is3d,height,width){//饼图
		$('#'+id).css('height',height);
		$('#'+id).css('width',width);
		$('#'+id).highcharts({
			chart: { 
		 	type: 'pie', 
		 	options3d: {
		 		enabled: is3d, alpha: 45, beta: 0 
		 	} 
		 }, title: {
		 	text: title
		 }, tooltip: { 
		 	pointFormat: '{point.percentage:.1f}%</b>' 
		 }, plotOptions: { 
		 	pie: { 
		 		allowPointSelect: true, cursor: 'pointer', depth: 35, 
		 		dataLabels: { 
		 		enabled: true, format: '{point.name}' 
		 		} 
		 	} 
		 },credits: {
	    	enabled: false
	   	 },exporting:{
	   		enabled:false
	  	 },series: [{ 
		 		type: 'pie', name: title, 
		 		data: data 
		 	}]
		}); 
	}
		
	function showBar1(id,data,title,width,height){//条形图
		$('#'+id).css('width',width);
		$('#'+id).css('height',height);
		$('#'+id).highcharts({                                           
	        chart: {                                                           
	            type: 'bar'                                                    
	        },
	      	title: {                                                           
	            text: ''                    
	        },
        	xAxis: {                                                           
	            categories: title
            },
         	exporting:{
	        	enabled:false
	        },                                                             
	        yAxis: {                                                           
	            min: 0,                                                        
	            title: {                                                       
	                text: '',                             
	                align: 'high'                                              
	            }                                                             
	        },                                                                 
	        tooltip: {                                                         
	            valueSuffix: '（%）'                                       
	        },
	      	legend: {
	            backgroundColor: '#FFFFFF',
	            reversed: true
	        },                                                            
	        plotOptions: {                                                     
	            bar: {                                                         
	                dataLabels: {                                              
	                    enabled: false 
	                }                                                          
	            }                                                              
	        },                                                                 
	        credits: {
	            enabled: false
	        },   
	        series: data
	 	});   
	}	                                                                
 	
	function QueryString(val) {
		var uri = window.location.search;
		var re = new RegExp("" +val+ "\=([^\&\?]*)", "ig");
		return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
	}
	
	function queryWJTJData(){
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/survey/allStatisticaBySubjectId.do',param,function(_d){
			initHtml(_d)
		});
	}
	
	function downLoadFile(o,id){
		if(!id){
			id = '';
		}
		var param = {};
		var Service = {};
		Service.HosId = Commonjs.getUrlParam("HosId");
		Service.Questid = id;
		Service.SubjectId = SubjectId;
		param.reqParam = Commonjs.getApiReqParams(Service);
		Commonjs.ajaxSync('/survey/downLoadFile.do', param, function(_d){
			if(_d.RespCode == 10000){
				window.location.href = "../../../" + _d.filePath + "?token=" + Commonjs.getToken();
			}else{
				ComWbj.artTips("提示","warning","操作异常",2,null);
			}
		});
//		o.href = '/survey/downLoadFile.do?SubjectId='+SubjectId+'&Questid='+id;
	}
	
	//暂时没用到
	function detailMsg(){
		window.location.href = 'myd-jgtj-list.html?subjectid='+SubjectId+'&subjecttitle='+title+'&typeV=2&Status=2';
	}
	
	function initHtml(tmpd){
		var d = tmpd.Data[0];
		var _d = d.Result;
		$('#allCount').text(d.SubjectTotal);
		if(!Commonjs.isEmpty(_d) && d.SubjectTotal > 0){
			title = d.SubjectTitle;
			$('#title').text(d.SubjectTitle);
			for(var i = 0;i<_d.length;i++){
				if(parseInt(_d[i].QuestType) == 1){//选题
					$('#chartDiv').append(SCHtml(_d[i],i));
					var pieData = getPieData(_d[i]);
					showPie('chart'+_d[i].QuestId,pieData,d.Question,false,300,'auto');
				}else if(parseInt(_d[i].QuestType) == 2){//多选题
					$('#chartDiv').append(MCHtml(_d[i],i));
					var barData = getBarData1(_d[i]);
					showBar1('chart'+_d[i].QuestId,barData,getBarTitle1(_d[i]),800,300);
				}else if(parseInt(_d[i].QuestType) == 3){//填空题
					$('#chartDiv').append(TKHtml(_d[i],i));
				}else if(parseInt(_d[i].QuestType) == 4){//矩阵单选题
					var barData = getBarData2(_d[i]);
					var titles = getBarTitle2(_d[i]);
					$('#chartDiv').append(JZSHtml(getBarData3(_d[i]),_d[i].TotalSamp,titles,_d[i],i));
					showBar2('chart'+_d[i].QuestId,barData,titles,800,300);
				}else if(parseInt(_d[i].QuestType) == 5){//矩阵多选选题
					var barData = getBarData2(_d[i]);
					var titles = getBarTitle2(_d[i]);
					$('#chartDiv').append(JZSHtml(getBarData3(_d[i]),_d[i].TotalSamp,titles,_d[i],i));
					showBar1('chart'+_d[i].QuestId,barData,titles,800,300);
				}
			}
		}else{
			$('#divCount').empty().html('<span style="">暂无数据统计<span>');
		}
	}
	
	function getBarData2(d){
		var _d = new Array();
		var _data = d.series;
		for(var i = 0;i<_data.length;i++){
			var p = {};
			p.name = _data[i].Question;
			var _data1 = _data[i].data;
			if(!Commonjs.isEmpty(_data1)){
				var _arr = new Array(); 
				for(var j = 0;j<_data1.length;j++){
					var num = parseFloat(isNaN(_data1[j].Sum*100/d.TotalSamp)?0:_data1[j].Sum*100/d.TotalSamp).toFixed(2);
					_arr.push(parseFloat(num+''));
				}
				p.data = _arr;
			}
			_d.push(p);
		}
		return _d;
	}
	
	function getBarData3(d){
		var _d = new Array();
		var _data = d.series;
		for(var i = 0;i<_data.length;i++){
			var p = {};
			p.name = _data[i].Question;
			var _data1 = _data[i].data;
			if(!Commonjs.isEmpty(_data1)){
				var _arr = new Array(); 
				for(var j = 0;j<_data1.length;j++){
					_arr.push(_data1[j].Sum);
				}
				p.data = _arr;
			}
			_d.push(p);
		}
		return _d;
	}
	
	function getBarTitle2(d){
		var _d = new Array();
		var _data = d.categories;
		for(var i = 0;i<_data.length;i++){
			_d.push(_data[i].ItemCont);
		}
		return _d;
	}
		
	function showBar2(id,data,title,width,height){
		$('#'+id).css('width',width);
		$('#'+id).css('height',height);
		$('#'+id).highcharts({
	        chart: {
	            type: 'bar'
	        },
	        title: {
	            text: ''
	        },
	        xAxis: {
	            categories: title
	        },credits:{
	        	enabled:0,text:"",href:""
	        },
	        yAxis: {
	            min: 0,
	            title: {
	                text: ''
	            }
	        },
	        exporting:{
	        	enabled:false
	        },
	        tooltip: {                                                         
	            valueSuffix: '（%）'                                       
	        },
	        legend: {
	            backgroundColor: '#FFFFFF',
	            reversed: true
	        },
	        plotOptions: {//重叠
	            series: {
	                stacking: 'normal'
	            }
	        },
	            series: data
	    });
	}
	
	function TKHtml(d,index){//填空题
		if(!Commonjs.isEmpty(d)){
			var html = '<div class="stat-sec"><div class="stat-sec-tit">';
				html += '<span class="q-num">Q'+(index+1)+'</span>'+d.Question+'（填空）';
				html += '<a class="btn btn-gray btn-w65" onclick="downLoadFile(this,'+d.QuestId+')">导出</a></div>';
				html += '<div class="stat-sec-con"><div class="t-center c-222 mt15">'+d.Question+'</div>';
				html += '<div class="mt30"></div><div class="mt30"><table class="tb tb-border" width="100%">';
				html += ' <tr><th>答案</th></tr>';
				var flag = false;
				for(var i = 0;i<d.SvQuestionItems.length;i++){
					if(i > 4){
						flag = true;
						html += '<tr style="display:none" name="showTr"><td>'+d.SvQuestionItems[i].Answer+'</td></tr>';
					}else html += '<tr><td>'+d.SvQuestionItems[i].Answer+'</td></tr>';
				}
	        	html += '<tr style="background-color:#f8f8f8;">';
	        	html += '<td class="relative" style="padding-left:20px;text-align:left">';
				html += '<span class="c-222">答题人数：</span><span class="c-org">'+d.TotalSamp+'人</span>';
				html += '<span class="absolute" style="right:20px; font-size:12px;">'+d.TotalSamp+'条';
				if(flag){
					html += '<a id="show'+d.QuestId+'" href="javascript:;" onclick="optShowTr(this,'+d.QuestId+')">展开全部</a>';
					html += '<a id="hide'+d.QuestId+'" style="display:none" href="javascript:;" onclick="optHideTr(this,'+d.QuestId+')">隐藏部份</a></span>';
				}else{
					html += '</span>';
				}
				html += '</td> </tr>';
				html += '</table> </div></div></div>';
			return html;
		} 
	}
	
	function optShowTr(o,id){
		$('tr[name="showTr"]').each(function(i){
			$(this).show();
		})
		$(o).hide();
		$('#hide'+id).show();
	}
	
	function optHideTr(o,id){
		$('tr[name="showTr"]').each(function(i){
			$(this).hide();
		})
		$(o).hide();
		$('#show'+id).show();
	}
	
	function JZSHtml(dbarData,count,titles,d,index){//矩阵单选题
		if(!Commonjs.isEmpty(d)){
			var html = ' <div class="stat-sec"><div class="stat-sec-con">';
			html += '<div class="stat-sec-tit"><span class="q-num">Q'+(index+1)+'</span>'+d.Question+'（多选题）';
			html += '<a class="btn btn-gray btn-w65" onclick="downLoadFile(this,'+d.QuestId+')">导出</a></div>';
			html += '<div class="t-center c-222">'+d.Question+'</div>';
			html += '<div class="t-center f12 mt5">答题人数：<span class="c-org">'+d.TotalSamp+'</span></div>';
			html += '<div class="mt30" align="center">';
			html += '<div id="chart'+d.QuestId+'" style=" width:400px; height:400px;"></div></div>';
			html += '<div class="mt30"><table class="tb tb-border" width="100%">';
			html += '<tr><th>选项</th>';
			var num = 1;
			for(var i = 0;i<titles.length;i++){
				html += '<th>'+titles[i]+'</th>';
				num++;
			}
			html += '</tr>';
			for(var i = 0;i<dbarData.length;i++){
				html += '<tr><th>'+dbarData[i].name+'</th>';
				for(var k = 0;k<dbarData[i].data.length;k++){
					html += '<th>'+dbarData[i].data[k]+'（'+(isNaN(dbarData[i].data[k]*100/count)?0:dbarData[i].data[k]*100/count).toFixed(2)+'%）</th>';
				}
			}				
			html += '</tr>';
        	html += '<tr style="background-color:#f8f8f8;"><td colspan="'+num+'" style="padding-left:20px;text-align:left">';
        	html += '<span class="c-222">答题人数：</span><span class="c-org">'+d.TotalSamp+'人</span></td></tr>';
			html += '</table>  </div> </div> </div>';
			return html;
		} 
	}
	
	function JZMHtml(dbarData,titles,d,index){//矩阵多选题
		if(!Commonjs.isEmpty(d)){
			var html = ' <div class="stat-sec"><div class="stat-sec-con">';
			html += '<div class="stat-sec-tit"><span class="q-num">Q'+(index+1)+'</span>'+d.Question+'（多选题）';
			html += '<input type="button" class="btn btn-gray btn-w65" value="导出" /></div>';
			html += '<div class="t-center c-222">'+d.Question+'</div>';
			html += '<div class="t-center f12 mt5">答题人数：<span class="c-org">'+d.TotalSamp+'</span></div>';
			html += '<div class="mt30" align="center">';
			html += '<div id="chart'+d.QuestId+'" style=" width:400px; height:400px;"></div></div>';
			html += '<div class="mt30"><table class="tb tb-border" width="100%">';
			html += '<tr><th>选项</th>';
			var num = 1;
			for(var i = 0;i<dbarData.length;i++){
				html += '<th>'+dbarData[i].name+'</th>';
				num++;
			}
			html += '</tr>';
			var n = 0;
			for(var i = 0;i<titles.length;i++){
				html += '<tr><th>'+titles[i]+'</th>';
				for(var j = 0;j<dbarData.length;j++){
					for(var k = 0;k<dbarData[j].data.length;k++){
						html += '<th>'+dbarData[j].data[k]+'%</th>';
					}
					j = dbarData.length;
				}
				n++;
			}				
			html += '</tr>';
        	html += '<tr style="background-color:#f8f8f8;"><td colspan="'+num+'" style="padding-left:20px;text-align:left">';
        	html += '<span class="c-222">答题人数：</span><span class="c-org">'+d.TotalSamp+'人</span></td></tr>';
			html += '</table>  </div> </div> </div>';
			return html;
		} 
	}
	
	function MCHtml(d,index){//多选题
		if(!Commonjs.isEmpty(d)){
			var html = ' <div class="stat-sec"><div class="stat-sec-con">';
			html += '<div class="stat-sec-tit"><span class="q-num">Q'+(index+1)+'</span>'+d.Question+'（多选题）';
			html += '<a class="btn btn-gray btn-w65" onclick="downLoadFile(this,'+d.QuestId+')">导出</a></div>';
			html += '<div class="t-center c-222">'+d.Question+'</div>';
			html += '<div class="t-center f12 mt5">答题人数：<span class="c-org">'+d.TotalSamp+'</span></div>';
			html += '<div class="mt30" align="center">';
			html += '<div id="chart'+d.QuestId+'" style=" width:400px; height:400px;"></div></div>';
			html += '<div class="mt30"><table class="tb tb-border" width="100%">';
			html += '<tr><th>选项</th><th>回复数（占比）</th></tr>';
			for(var i = 0;i<d.SvQuestionItems.length;i++){
				html += '<tr><td>'+d.SvQuestionItems[i].ItemCont
				+'</td><td><span class="c-org">'+d.SvQuestionItems[i].Sum
				+'</span>（'+(isNaN(d.SvQuestionItems[i].Sum*100/d.TotalSamp)?0:d.SvQuestionItems[i].Sum*100/d.TotalSamp).toFixed(2)+'%）</td></tr>';
			}
        	html += '<tr style="background-color:#f8f8f8;"><td colspan="2" style="padding-left:20px;text-align:left">';
        	html += '<span class="c-222">答题人数：</span><span class="c-org">'+d.TotalSamp+'人</span></td></tr>';
			html += '</table>  </div> </div> </div>';
			return html;
		} 
	}
	
	function getBarData1(d){
		var _d = new Array();
		var p = {};
		p.name = d.Question;
		var t = new Array();
		for(var i = 0;i<d.SvQuestionItems.length;i++){
			var num = parseFloat(isNaN(d.SvQuestionItems[i].Sum*100/d.TotalSamp)?0:d.SvQuestionItems[i].Sum*100/d.TotalSamp).toFixed(2);
			t.push(parseFloat(num+''));
		}
		p.data = t;
		_d.push(p);
		return _d;
	}
	
	function getBarTitle1(d){
		var _d = new Array();
		for(var i = 0;i<d.SvQuestionItems.length;i++){
			_d.push(d.SvQuestionItems[i].ItemCont);
		}
		return _d;
	}
	
	function getPieData(d){
		var _d = new Array();
		for(var i = 0;i<d.SvQuestionItems.length;i++){
			var t = new Array();
			t.push(d.SvQuestionItems[i].ItemCont);
			t.push(parseInt(d.SvQuestionItems[i].Sum));
			_d.push(t);
		}
		return _d;
	}
	
	function SCHtml(d,index){//单选题
		if(!Commonjs.isEmpty(d)){
			var html = ' <div class="stat-sec"><div class="stat-sec-con">';
			html += '<div class="stat-sec-tit"><span class="q-num">Q'+(index+1)+'</span>'+d.Question+'（单选题）';
			html += '<a class="btn btn-gray btn-w65" onclick="downLoadFile(this,'+d.QuestId+')">导出</a></div>';
			html += '<div class="t-center c-222">'+d.Question+'</div>';
			html += '<div class="t-center f12 mt5">答题人数：<span class="c-org">'+d.TotalSamp+'</span></div>';
			html += '<div class="mt30" align="center">';
			html += '<div id="chart'+d.QuestId+'" style=" width:400px; height:400px;"></div></div>';
			html += '<div class="mt30"><table class="tb tb-border" width="100%">';
			html += '<tr><th>选项</th><th>回复数（占比）</th></tr>';
			for(var i = 0;i<d.SvQuestionItems.length;i++){
				html += '<tr><td>'+d.SvQuestionItems[i].ItemCont+'</td>';
				html += '<td><span class="c-org">'+d.SvQuestionItems[i].Sum+'</span>';
				html += '（'+parseFloat(isNaN(d.SvQuestionItems[i].Sum*100/d.TotalSamp)?0:d.SvQuestionItems[i].Sum*100/d.TotalSamp).toFixed(2)+'%）';
				if(d.SvQuestionItems[i].OtherAnswer.length > 0){
					var str = '';
					for(var n = 0;n < d.SvQuestionItems[i].OtherAnswer.length;n++){
						if(str == ''){
							str += d.SvQuestionItems[i].OtherAnswer[n].OAnswer;
						}else str += '-'+d.SvQuestionItems[i].OtherAnswer[n].OAnswer;
					}
					html += '<a onclick="showOtherAnswer(\''+str+'\',\''+d.SvQuestionItems[i].ItemCont+'\')" class="absolute" style="right:16%;font-size:12px;" href="javascript:;">查看详情</a>';
				}
				html += '</td></tr>';
			}
        	html += '<tr style="background-color:#f8f8f8;"><td colspan="2" style="padding-left:20px;text-align:left">';
        	html += '<span class="c-222">答题人数：</span><span class="c-org">'+d.TotalSamp+'人</span></td></tr>';
			html += '</table>  </div> </div> </div>';
			return html;
		} 
	}
	
	function showOtherAnswer(str,t){
	
		$('#otherAnswerTitle').text('选择“'+t+'”所填写的内容');
		$('#setting3').prev('.mask').show().end().fadeIn();
		var arr = str.split('-');
		var html = '';
		for(var i = 0;i<arr.length;i++){
			html +='<tr ><td >'+(Commonjs.isEmpty(arr[i])?"无":arr[i])+'</td></tr>';
		}
	 	$('#otherAnswer tr').eq(0).nextAll().remove();
		$(html).insertAfter($("#otherAnswer tr").eq(0));
 	 	setPopAlign('setting3');
	}