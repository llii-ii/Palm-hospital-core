/***
 * 就诊人选择插件
 * 页面初始化时，调用后台接口查询就诊人信息
 * 1、当就诊人不存在时，弹出窗口提示需要新增就诊人，用户点击新增就诊人按钮，新增就诊人后回调传入的回调URL指定页面
 * 2、存在就诊人信息时，验证是否有设置默认就诊人且默认就诊人有默认的卡信息，当存在默认就诊人且默认就诊人有默认卡信息时，
 * 	直接选中该就诊人及就诊卡，不存默认就诊人或默认就诊人不存在默认卡，直接弹出选择就诊人窗口
 * 
 * 
 */
;(function($,window){
	//全局成员对象信息
	var _memberList = {};
	//默认参数
	var defaluts={
			//加载控件的页面名称
			page : null,
			//是否开放无卡，目前仅查询就诊卡时有效，未开放时会过滤无就诊卡的数据
			isOpenNotCard : Commonjs.getIsOpenYYNotClinicCard(),
			//是否必须卡号，为true时，必须有卡号  无卡号时弹出对应的提示框
			isMustCardNo : false,
			//卡类型  默认就诊卡
			cardType : Commonjs.constant.cardType_1,
			//回调地址，用于无就诊人或就诊人没有绑卡时，点击弹出窗的新增就诊人或绑定卡信息，新增成功之后回调的地址，默认为弹出提示框的那个页面
			callBackUrl : location.pathname+location.search,
			//默认选中的就诊人信息	格式：CardNo+","+CardType+","+encodeURIComponent(MemberName)+","+MemberId+","+HisMemberId;
			defaultlValue : null,
			//关闭插件的回调函数
			pickerOnClose : null,
			//插件初始化成功后执行的回调函数
			ajaxSuccess : null,
			//无卡时，执行的回调函数
			nonCardFunction : null,
			//显示格式转换
			formatValue:function (p, values, displayValues){
				return displayValues;
			}
		}
	var MemberPicker = function(elementObj,opts){
		//显示出来的值
		var disObj = new Array();
		//实际存储的值
		var valObj = new Array();
		//是否存在就诊人
		var hasMember = false;
		
		var titeDesc = "就诊卡";
		if( opts.cardType == 14 ){
			titeDesc = "住院号";
		}
		
		//方法
		var _Methods = {
			/**
			 * 获取key
			 */
			getMemberKey : function(memberObj){
				return memberObj.CardNo+","+memberObj.CardType+","+encodeURIComponent(memberObj.MemberName)+","+memberObj.MemberId;
			},
			/**
			 * 获取key对应的值
			 */
			getMemberObj : function(memberKey){
				return _memberList[memberKey];
			},
			getSelectMember : function(){
				if(!Commonjs.isEmpty(opts.defaultlValue)){
					return _memberList[opts.defaultlValue];
				}else{
					return _memberList[valObj[0]];
				}
			},
			/**
			 * 回填就诊人信息
			 */
			setMemberNameAndCard : function(titeDesc,memberObj){
				//就诊人名称回填
				if($("#memberPicker_name").length>0){
					$("#memberPicker_name").html(memberObj.MemberName);
				}else if(elementObj.parent().find("label.c-f15").length>0){
					elementObj.parent().find("label.c-f15").html(memberObj.MemberName);
				}
				//就诊人卡号回填
				if($("#memberPicker_cardNo").length>0){
					$("#memberPicker_cardNo").html(Commonjs.formatCardNo(memberObj.CardNo));
				}else if(elementObj.parent().find("span.c-999").length>0){
					elementObj.parent().find("span.c-999").html(titeDesc+"："+Commonjs.formatCardNo(memberObj.CardNo));
				}
				//预约挂号选择就诊人回填
				if(elementObj.parent().find("label.list-text").length>0){
					elementObj.parent().find("label.list-text").html(memberObj.MemberName+"("+ Commonjs.formatCardNo(memberObj.CardNo) +")");
				}
			},
			/**
			 * 没有成员信息时，弹出新增成员提示框
			 */
			openNoMemberDialog : function(){
				//无就诊人信息，弹窗提示绑定
//				var url = "../member/addMember.html";
//				if(!Commonjs.isEmpty(opts.callBackUrl)){
//					url = url + "?callBackUrl="+opts.callBackUrl;
//				}
//				Commonjs.msgDialog("未找到有效的就诊人信息，请先新增就诊人信息。","立即新增",url);
//				return;
				myLayer.confirm({
					title:'提示',
					con: '未找到有效的就诊人信息，请先新增就诊人信息。',
					okValue : '立即新增',
					ok: function() {
						var url = "/business/member/addMember.html";
						if(!Commonjs.isEmpty(opts.callBackUrl)){
							url = url + "?callBackUrl="+encodeURIComponent(opts.callBackUrl);
						}
						location.href = Commonjs.goToUrl(url);
					},
					cancelValue :'就诊人管理',
					cancel:function(){
						var url = "/business/member/memberList.html";
						if(!Commonjs.isEmpty(opts.callBackUrl)){
							url = url + "?callBackUrl="+encodeURIComponent(opts.callBackUrl);
						}
						location.href = Commonjs.goToUrl(url);
					}
				});
			},
			/**
			 * 没有卡信息时，弹出绑卡提示框
			 */
			openNoCardDialog : function(memberObj){
				//卡类型不是就诊卡，或没有开启无卡，弹出窗口提示绑定卡信息
				if(Commonjs.isEmpty(memberObj)){
					memberObj = this.getSelectMember();
				}
				myLayer.confirm({
					title:'提示',
					isClose:true,
					con: '该就诊人还未绑定'+titeDesc+' ,是否现在去绑定'+titeDesc+'?',
					ok: function() {
						var url = '';
						if(opts.cardType==1){
							url = '/business/member/bindClinicCard.html';
						}else if(opts.cardType==14){
							url = '/business/member/bindHospitalNo.html';
						}
						url = url+'?memberId='+memberObj.MemberId+'&memberName='+escape(memberObj.MemberName);
						if(!Commonjs.isEmpty(opts.callBackUrl)){
							url = url + "&callBackUrl="+encodeURIComponent(opts.callBackUrl);
						}
						location.href = Commonjs.goToUrl(url);
					},
					cancelValue :'就诊人管理',
					cancel:function(){
						var url = "/business/member/memberList.html";
						if(!Commonjs.isEmpty(opts.callBackUrl)){
							url = url + "?callBackUrl="+encodeURIComponent(opts.callBackUrl);
						}
						location.href = Commonjs.goToUrl(url);
					}
				});
			}
		}
		
		//查询就诊人信息
		var apiData = {};
		var param = {};
		apiData.HosId = Commonjs.getUrlParam("HosId");
		apiData.OpId = Commonjs.getOpenId();
		if(!Commonjs.isEmpty(opts.cardType)){
			apiData.CardType = opts.cardType;
		}
		if(!Commonjs.isEmpty(opts.cardNo)){
			apiData.CardNo = opts.cardNo;
		}
		if(!Commonjs.isEmpty(opts.memberId)){
			apiData.MemberId = opts.memberId;
		}
		param.apiParam = Commonjs.getApiReqParams(apiData);
		Commonjs.ajax('/wsgw/basic/QueryMemberCardList/callApi.do?t=' + new Date().getTime(),param,function(resp){
			if(Commonjs.isEmpty(resp)){
				myLayer.alert('网络繁忙，获取成员信息失败，请刷新后重试',3000);
				return;
			}
			if(resp.RespCode!=10000){
				myLayer.alert(jsons.RespMessage,3000);
				return;
			}
			if(Commonjs.ListIsNotNull(resp.Data)){
				
				hasMember = true;
				
				//有就诊人信息时，遍历就诊人信息，验证是否存在默认就诊人
				$.each(resp.Data,function(i,item){
					var defMember = item.IsDefaultMember;//是否默认就诊人
					var defCard = item.IsDefault;//是否默认就诊卡
					//成员对象key
					var memberKey = _Methods.getMemberKey(item);
					if(defMember == 1 && defCard == 1){
						if(Commonjs.isEmpty(opts.defaultlValue)){
                			//不存在需要默认选中的值时，默认选中默认就诊人的默认就诊卡
                			opts.defaultlValue = memberKey;
            			}
					}
					if(item.cardType==1){
						//就诊卡为空，且没有开放无卡预约时，过滤该数据
//						if(!opts.isOpenNotCard && Commonjs.isEmpty(item.CardNo)){
//							return true;
//						}
						disObj.unshift(item.MemberName+"("+ Commonjs.formatCardNo(item.CardNo) +")");
	            		valObj.unshift(memberKey);
					}else{
						disObj.unshift(item.MemberName+"("+ Commonjs.formatCardNo(item.CardNo) +")");
            			valObj.unshift(memberKey);
					}
					_memberList[memberKey] = item;
				})
			}
		},{async:false});
		if(valObj.length<=0){
			if(!hasMember){
				//无就诊人信息，弹窗提示绑定
				_Methods.openNoMemberDialog();
				return;
			}
		}
		//存在就诊人信息时，初始化插件
		var page = opts.page;
		var cardType = opts.cardType;
		var defaultlValue = opts.defaultlValue;
		var pickerOnCloseFunction = opts.pickerOnClose;
		var ajaxSuccessFunction = opts.ajaxSuccess;
		var nonCardFunction = opts.nonCardFunction;
		var formatValueFunction = opts.formatValue;
		var selectCardNo;
		
		if( !Commonjs.isEmpty(defaultlValue) ){
			//默认选中的就诊人信息 
			var memberObj = _Methods.getMemberObj(defaultlValue);
			selectCardNo = memberObj.CardNo;
			_Methods.setMemberNameAndCard(titeDesc,memberObj);
			elementObj.val(defaultlValue);
    	}else{
    		//无默认需要选中的就诊人，默认选中第一个就诊人
			var memberObj = _Methods.getMemberObj(valObj[0]);
			selectCardNo = memberObj.CardNo;
			_Methods.setMemberNameAndCard(titeDesc,memberObj);
			elementObj.val(valObj[0]);
    	}
    	//首次进入页面时，选中的就诊人无卡时，验证是否弹出窗口提示绑卡，当卡号必填 或 当前是就诊卡且没有开放无卡，或非就诊卡且无卡时，需要弹出绑定窗口提示
    	if(Commonjs.isEmpty(selectCardNo) && (opts.isMustCardNo || opts.cardType!=1 || !opts.isOpenNotCard)){
			//卡类型不是就诊卡，或没有开启无卡，弹出窗口提示绑定卡信息
			_Methods.openNoCardDialog();
			return;
		}
    	//就诊人插件初始化
		elementObj.picker({
			title: "就诊人选择",
			cols: [
				{
					textAlign: 'center',
					displayValues:disObj,
					values: valObj
				}
			],
			onClose:function(obj){
				var memberObj = _Methods.getMemberObj(obj.cols[0].value);
				
				//卡号为空，且卡号不能为空 或 没有开放无卡 或 查询的非就诊卡时，弹出窗口提示绑卡
				if(Commonjs.isEmpty(memberObj.CardNo) && (opts.isMustCardNo || opts.cardType!=1 || !opts.isOpenNotCard )){
					_Methods.openNoCardDialog(memberObj);
					return;
				}
				_Methods.setMemberNameAndCard(titeDesc,memberObj);
				elementObj.val(obj.cols[0].value);
				//设置浏览器缓存，方便回退的时候，重新设值
				if (window.localStorage) {
					Commonjs.setLocalItem("selectedMember",obj.cols[0].value,true);
//				    localStorage.setItem("selectedMember",obj.cols[0].value);	
				}
				//执行关闭插件回调函数
				if( typeof  pickerOnCloseFunction === "function" ){
					pickerOnCloseFunction(obj,memberObj,_memberList);
				}
			},
			formatValue:function (p, values, displayValues){
				return displayValues;
			}
		});
		//执行初始化成功回调函数
		if( typeof  ajaxSuccessFunction === "function" ){
			var defaultMember = {};
			if(!Commonjs.isEmpty(defaultlValue)){
				defaultMember = _Methods.getMemberObj(defaultlValue);
			}else{
				defaultMember = _Methods.getMemberObj(valObj[0]);
			}
			ajaxSuccessFunction(_memberList,defaultMember);
		}
	}
	
	MemberPicker.prototype.getMemberInfo = function(memberKey){
		return _memberList[memberKey];
	}
	
	$.fn.memberPicker = function(options){
		var opts = $.extend({}, defaluts, options);
		if(opts){
			
		}
	 	return this.each(function () {
            if (!this) {
                return
            }
            var $this = $(this);
            var memberPicker = $this.data("memberPicker");
            if (!memberPicker) {
                $this.data("memberPicker", new MemberPicker($this, opts));
            }
            return memberPicker;
        });
	}
})(jQuery,window);

//页面加载完成后触发插件
$(function(){
	//初始化就诊人插件
//	if($("#member_picker").length>0){
//		$("#member_picker").memberPicker();
//	}
	
})