

	var name = '';

	var ZTQS_TIME = 'W'; //整体趋势: W周;M月
	var HZLY_TIME = 'W'; //患者来源: W周;M月
	var SEL_TYPE = 1;  //指标类型1~6
	var SEL_TYPE_HZLY = 3; //3累计绑定就诊卡数; 4累计线上门诊人数;
	
	$(function () {
		$('#btn-blue-1 .circlebg-blue').click(function(){
			$('#btn-blue-1 .circlebg-blue').removeClass('t-sortby');
			$(this).addClass('t-sortby');
		})
		$('#btn-blue-2 .circlebg-blue').click(function(){
			$('#btn-blue-2 .circlebg-blue').removeClass('t-sortby');
			$(this).addClass('t-sortby');
		})

		$('#datepickerA1').blur(function(){
        	var start = $('#datepickerA1').val();
        	var end = $('#datepickerA2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
        });
        $('#datepickerA2').blur(function(){
        	var start = $('#datepickerA1').val();
        	var end = $('#datepickerA2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
        });
        $('#datepickerB1').blur(function(){
        	var start = $('#datepickerB1').val();
        	var end = $('#datepickerB2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
        });
        $('#datepickerB2').blur(function(){
        	var start = $('#datepickerB1').val();
        	var end = $('#datepickerB2').val();
        	if (start>end){
        		Commonjs.alert("开始时间应小于结束时间");
        	}
        });
        
		 $('#btn-week-1').click(function(){ 
			 ZTQS_TIME = 'W';
			 //$('#btn-month-1').removeClass('t-sortby');
			 //$('#btn-week-1').addClass('t-sortby');
        	 $('#datepickerA1').datetimepicker({value:Commonjs.getDate(-7)});
        	 $('#datepickerA2').datetimepicker({value:Commonjs.getDate(-1)});
        	 loadGrid(1);
        	 setSelType(SEL_TYPE);
        });
        $('#btn-month-1').click(function(){ 
        	 ZTQS_TIME = 'M';
        	 //$('#btn-week-1').removeClass('t-sortby');
			 //$('#btn-month-1').addClass('t-sortby');
        	 $('#datepickerA1').datetimepicker({value:Commonjs.getDate(-30)});
        	 $('#datepickerA2').datetimepicker({value:Commonjs.getDate(-1)});
        	 loadGrid(1);
        	 setSelType(SEL_TYPE);
        });
        
        $('#btn-week-2').click(function(){ 
			 HZLY_TIME = 'W';
        	 $('#datepickerB1').datetimepicker({value:Commonjs.getDate(-7)});
        	 $('#datepickerB2').datetimepicker({value:Commonjs.getDate(-1)});
        	 setSelTypeHzly(SEL_TYPE_HZLY);
        });
        $('#btn-month-2').click(function(){ 
        	 HZLY_TIME = 'M';
        	 $('#datepickerB1').datetimepicker({value:Commonjs.getDate(-30)});
        	 $('#datepickerB2').datetimepicker({value:Commonjs.getDate(-1)});
        	 setSelTypeHzly(SEL_TYPE_HZLY);
        });
        
        //点击日期文本框
        $('#datepickerA1,#datepickerA2').datetimepicker({onSelectDate:function(dp,input){
	        	var start = $('#datepickerA1').val();
	        	var end = $('#datepickerA2').val();
	        	if (start>end){
	        		Commonjs.alert("开始时间应小于结束时间");
	        	}else {
	        		loadGrid(1);
		      		setSelType(SEL_TYPE);
	        	}
        }});
        $('#datepickerB1,#datepickerB2').datetimepicker({onSelectDate:function(dp,input){
        	var start = $('#datepickerB1').val();
	        	var end = $('#datepickerB2').val();
	        	if (start>end){
	        		Commonjs.alert("开始时间应小于结束时间");
	        	}else {
	        		setSelTypeHzly(SEL_TYPE_HZLY);
	        	}
        }});
        
        
	    getDCsummary('Y');			  
	    //spline(setSplineDivName(0));
	    //showBar(setBarDivName(0));
	    setTimeout(function(){
	    	setSelType(SEL_TYPE);
	    	setSelTypeHzly(SEL_TYPE_HZLY);
	    	loadGrid(1);
	    },500);
	});			
	/**
	 * 加载表格数据
	 * 
	 * @param {Object} index
	 * @return {TypeName} 
	 */
	var loadGrid = function (index){
		$('#pagenumber').val(index);
		var pageIndex = index-1;
		var pageSize = 8;
		var startDate = $('#datepickerA1').val();
		var endDate = $('#datepickerA2').val()+ " 23:59:59";
		var param = {};
		param.reqParam = Commonjs.getReqParams({'StartDate':startDate,'EndDate':endDate,'Page':{'PIndex':pageIndex,'PSize':pageSize}});
		Commonjs.ajax('/rf/getDataCollectionGrid.do',param,function(d){
			$("#alltableid tr:gt(0)").remove();
			if(d.RespCode != 10000){
				Page(0,pageSize,'pager');
				return;
			}
			if(d.Page.PCount!=undefined){
				if(d.Page.PCount!=0){
					$("#totalcount").val(d.Page.PCount);
				}else{
					if(d.Page.PIndex==0)
						$("#totalcount").val(0);
				}
			}else{
				$("#totalcount").val(0);
			}
			Page($("#totalcount").val(),d.Page.PSize,'pager');
			var log = d.Data;
			if(!Commonjs.isEmpty(log)) {
				var tep = [];
				for(var i=0;i<log.length;i++){
					$('#alltableid').append("<tr><td>"+log[i].OperTime+"</td><td>"+log[i].Type1+"</td><td>"+log[i].Type3+"</td><td>"+log[i].Type4+"</td><td>"+log[i].Type5+"</td><td><font color=red>￥</font>"+Commonjs.centToYuan(log[i].Type6)+"</td></tr>");
				}
			}else {
				$('#alltableid').append("<tr align=center><td colspan=7><font color=red>未找到相关数据!</font></td></tr>");
			}
		});
	};

	/**
	 * 整体趋势中的选中
	 * @param {Object} t
	 */
	var setSelType = function(t) {
		SEL_TYPE = t;
		setDCline(t);
	}
	/**
	 * 患者来源中的选中
	 * @param {Object} t
	 */
	var setSelTypeHzly = function(t) {
		SEL_TYPE_HZLY = t;
		setDCbar(t);
	}
	/**
	 * 点折图数据
	 * @param {Object} type
	 * @return {TypeName} 
	 */
	var setDCline = function(type) {
		var startDate = $('#datepickerA1').val();
		var endDate = $('#datepickerA2').val() +" 23:59:59";
		
		var param = {};
		param.reqParam = Commonjs.getReqParams({'StartDate':startDate,'EndDate':endDate,'DataType':type});
		
		Commonjs.ajax('/rf/getDCLine.do',param,function(d){
			if(d.RespCode != 10000){
				return;
			}
			if(!Commonjs.isEmpty(d.Data)) {
				var div = setSplineDivName(type-1);
				spline(div, d.Data);
			}
		});
	}
	/**
	 * 条形图数据
	 * @param {Object} type
	 * @return {TypeName} 
	 */
	var setDCbar = function(type) {
		var param = {};
		
		var startDate = $('#datepickerB1').val();
		var endDate = $('#datepickerB2').val()+ " 23:59:59";
		param.reqParam = Commonjs.getReqParams({'StartDate':startDate,'EndDate':endDate,'DataType':type});
		Commonjs.ajax('/rf/getDCbar.do',param,function(d){
			if(d.RespCode != 10000){
				return;
			}
			if(!Commonjs.isEmpty(d.Data)) {
				showBar(setBarDivName(type-3),d.Data)
			}
		});
		
	}
	/**
	 * 获取概要统计值
	 * @param {Object} flag
	 * @return {TypeName} 
	 */
	var getDCsummary = function(flag) {
		$('#type1').html('0');
		$('#type3').html('0');
		$('#type4').html('0');
		$('#type5').html('0');
		$('#type6').html('<font color=red>￥</font>0.00');
		
		var param = {};
		param.reqParam = Commonjs.getReqParams({'Param':flag});
		Commonjs.ajax('/rf/getDCSummary.do',param,function(d){
			if(d.RespCode == 10000 && d.Data!=undefined){
				var res = d.Data;
				var tep = [];
				if(res.length==undefined && !Commonjs.isEmpty(res.dataType)){
					tep.push(res);
					res = tep;
				}
				if(!Commonjs.isEmpty(res)&&!Commonjs.isEmpty(res.length)) {
					for(var i=0;i<res.length;i++) {
						if(res[i].DataType!=6) {
							if(res[i].DataType!=2){
								$('#type'+res[i].DataType).html(res[i].DataValue);
							}
						}else {
							$('#type'+res[i].DataType).html('<font color=red>￥</font>'+Commonjs.centToYuan(res[i].DataValue));
						}
					}
				}
			}
		});
	}
	/**
	 * 点折图
	 * 
	 */
	function spline(id, data){
		$('#'+id).empty();
		var categories = [];
		var series = [];
		if(data){
			$.each(data,function(i,oo){
				//日期数组
				categories.push(oo.OperTime);
				//遍历各个渠道日期下的数据
				$.each(oo.ChannelData,function(j,dd){
					var isAdd = true;
					//series 已经存在时，遍历判断是否已经存在某个渠道的数据，存在的话直接将值加入已存在的series对象中
					if(series.length>0){
						$.each(series,function(k,cc){
							if(cc.name == dd.ChannelName){
								cc.data.push(dd.DataValue);
								isAdd = false;
								return false;
							}
						})
					}
					//series中不存在时添加数据
					if(isAdd){
						var channel = {};
						channel.data = [];
						channel.name = dd.ChannelName;
						if((j+1)%2==0){
							channel.color = '#4FB947';
						}else if((j+1)%3==0){
							channel.color = '#FF7523';
						}else{
							channel.color = '#2DC0E8';
						}
						channel.data.push(dd.DataValue);
						series.push(channel);
					}
				})
			})
		}
		
		
		$('#'+id).highcharts({
	        title: {
	            text: '', x: -10 
	        },
	        subtitle: {
	            text: '',x: -10
	        },
	        xAxis: {
	            categories: eval(categories)//['01-01', '02-01', '03-01']
	        },
	        yAxis: {
	            title: {
	                text: ''
	            },
	            plotLines: [{value: 10,width: 1,color: '#808080'}]
	        },
	        tooltip: {
	            valueSuffix: ''
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'middle',
	            borderWidth: 0
	        },credits: {
		    	enabled: false
			},exporting:{
		   		enabled:false
			},series: series
				
//				[{
//	            name: '所有',
//	            data: eval(alldata),//[7.0, 6.9, 9.5],
//	            color : '#2DC0E8'
//	        }, {
//	            name: '微信',
//	            data: eval(wx),//[ 11.3, 17.0, 22.0],
//	            color : '#4FB947'
//	        }, {
//	            name: '支付宝',
//	            data: eval(zfb),//[3.5, 8.4, 13.5],
//	            color : '#FF7523'
//	        }]
	    });
		
	}
//	/**
//	 * 点折图
//	 * 
//	 * @param {Object} id
//	 * @param {Object} categoriesP
//	 * @param {Object} data   格式: eval('[2,3,4]')
//	 */
//	var spline = function(id, categoriesP, alldata, wx,bd, zfb){
//		$('#'+id).empty();
//		
//		$('#'+id).highcharts({
//	        title: {
//	            text: '', x: -10 
//	        },
//	        subtitle: {
//	            text: '',x: -10
//	        },
//	        xAxis: {
//	            categories: eval(categoriesP)//['01-01', '02-01', '03-01']
//	        },
//	        yAxis: {
//	            title: {
//	                text: ''
//	            },
//	            plotLines: [{value: 10,width: 1,color: '#808080'}]
//	        },
//	        tooltip: {
//	            valueSuffix: ''
//	        },
//	        legend: {
//	            layout: 'vertical',
//	            align: 'right',
//	            verticalAlign: 'middle',
//	            borderWidth: 0
//	        },credits: {
//		    	enabled: false
//			},exporting:{
//		   		enabled:false
//			},series: [{
//	            name: '所有',
//	            data: eval(alldata),//[7.0, 6.9, 9.5],
//	            color : '#2DC0E8'
//	        }, {
//	            name: '微信',
//	            data: eval(wx),//[ 11.3, 17.0, 22.0],
//	            color : '#4FB947'
//	        }, {
//	            name: '支付宝',
//	            data: eval(zfb),//[3.5, 8.4, 13.5],
//	            color : '#FF7523'
//	        }]
//	    });
//		
//	}
	//条形图
	function showBar(id, data){
		var serialname = SEL_TYPE_HZLY==3?'累计绑定就诊卡数':'累计线上门诊人数';
		var categories = [];
		var seriesData = [];
		if(!Commonjs.isEmpty(data)){
			$.each(data,function(i,d){
				categories.push(d.ChannelName);
				var color = '#4FB947';
				if((i+1)%2==0){
					color = '#FF7523';
				}
				seriesData.push({y:parseInt(d.DataValue),color:color})
			})
		}
		$('#'+id).highcharts({
	        chart: {                                                           
	            type: 'bar'                                                    
	        },
	      	title: {                                                           
	            text: ''                    
	        },
        	xAxis: {                                                           
	            categories: categories
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
	            valueSuffix: ''                                       
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
	        series: [{                                                         
			            name: serialname,                                             
			            data: seriesData                                  
			        }]        
	 	});   
	}	
//	//条形图
//	function showBar(id, wx,zfb,bd){
//		var serialname = SEL_TYPE_HZLY==3?'累计绑定就诊卡数':'累计线上门诊人数';
//		$('#'+id).highcharts({                                           
//	        chart: {                                                           
//	            type: 'bar'                                                    
//	        },
//	      	title: {                                                           
//	            text: ''                    
//	        },
//        	xAxis: {                                                           
//	            categories: ['微信','支付宝']
//            },
//         	exporting:{
//	        	enabled:false
//	        },                                                             
//	        yAxis: {                                                           
//	            min: 0,                                                        
//	            title: {                                                       
//	                text: '',                             
//	                align: 'high'                                              
//	            }                                                             
//	        },                                                                 
//	        tooltip: {                                                         
//	            valueSuffix: ''                                       
//	        },
//	      	legend: {
//	            backgroundColor: '#FFFFFF',
//	            reversed: true
//	        },                                                            
//	        plotOptions: {                                                     
//	            bar: {                                                         
//	                dataLabels: {                                              
//	                    enabled: false 
//	                }                                                          
//	            }                                                              
//	        },                                                                 
//	        credits: {
//	            enabled: false
//	        },   
//	        series: [{                                                         
//			            name: serialname,                                             
//			            data: [{
//			            	y:parseInt(wx), //107
//			            	color: '#4FB947'
//			            },{
//			            	y:parseInt(zfb), //20
//			            	color: '#FF7523'
//			            }]                                   
//			        }]        
//	 	});   
//	}	 
	
	function setBarDivName(index){
		switch(index){
			case 0:return 'allBindCardDiv';
			case 1:return 'allClinicNumDiv';
			default:return '';
		}
	}
	
	function setSplineDivName(index){
		switch(index){
			case 0:return 'newConcernDiv';
			case 1:return 'activeUserDiv';
			case 2:return 'bindCardDiv';
			case 3:return 'clinicNumDiv';
			case 4:return 'strikeTimeDiv';
			case 5:return 'strikeAmountDiv';
			default:return '';
		}
	}
	
	 //tab
    function setTab(tabEleBoxId,tabConBoxId) {
        var _this=this;
        _this.tabEles=$('#'+tabEleBoxId).find('li'),
        _this.tabCons=$('#'+tabConBoxId).find('.trend-tab-con');

        if(!_this.tabEles.filter('.current').length){
            _this.tabEles.eq(0).addClass('current');
            _this.tabCons.eq(0).show();
        }
        _this.tabEles.click(function(){
            if(!$(this).hasClass('current')) {
                _this.tabEles.removeClass('current');
                $(this).addClass('current');
                _this.tabCons.hide();
                _this.tabCons.eq($(this).index()).fadeIn();
                if(tabEleBoxId == 'trendTab1')
                	spline(setSplineDivName($(this).index()));
                else 
                	showBar(setBarDivName($(this).index()));
            }
        });
    }
    
/**
 * 分页
 * @param {Object} totalcounts
 * @param {Object} pagecount
 * @param {Object} pager
 */
var Page = function(totalcounts, pagecount,pager) {
	$("#"+pager).pager( {
		totalcounts : totalcounts,
		pagesize : 8,
		pagenumber : $("#pagenumber").val(),
		pagecount : parseInt(totalcounts/pagecount)+(totalcounts%pagecount >0?1:0),
		buttonClickCallback : function(al) {
			$("#pagenumber").val(al);
			loadGrid(al);
		}
	});
};
	