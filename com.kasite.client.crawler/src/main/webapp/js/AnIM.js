var newMsgBodyCount = 0; //新联系人个数
//替换表情
var regPlace = /(\[jie\]|\[kun\]|\[xia\]|\[shx\]|\[wx\]|\[kl\]|\[bq\]|\[lengh\]|\[pp\]|\[sa\]|\[mg\]|\[xu\]|\[lq\]|\[kk\]|\[shl\]|\[bp\]|\[yb\]|\[ruo\]|\[aini\]|\[gd\]|\[dk\]|\[xin\]|\[bangbangt\]|\[xiayu\]|\[lh\]|\[fn\]|\[fendou\]|\[qt\]|\[yhh\]|\[hq\]|\[kb\]|\[qiang\]|\[xs\]|\[db\]|\[lw\]|\[zhh\]|\[zhm\]|\[cd\]|\[fan\]|\[xig\]|\[huaix\]|\[hx\]|\[ka\]|\[yx\]|\[qiao\]|\[kel\]|\[gz\]|\[dy\]|\[jk\]|\[shuai\]|\[fd\]|\[duoyun\]|\[bu\]|\[baiy\]|\[se\]|\[gy\]|\[dx\]|\[yun\]|\[cj\]|\[ws\]|\[zt\]|\[qq\]|\[hn\]|\[jy\]|\[hanx\]|\[dg\]|\[ll\]|\[cy\]|\[kuk\]|\[ty\]|\[zk\]|\[tuu\]|\[ng\]|\[kf\]|\[bs\]|\[zhem\]|\[pz\]|\[shui\]|\[pj\]|\[gg\]|\[am\]|\[yao\]|\[cp\]|\[ch\]|\[tx\]|\[tp\]|\[wq\]|\[zj\]|\[bz\]|\[hd\]|\[yl\]|\[yiw\]|\[ic_launcher\]|\[ic_launcher_an\]|\[ic_launcher_huan\])/g;
//发送图片默认大小
var imgSize = "50%";
//上次显示的时间
var oldDatetime;
//历史记录时间;
var hisDateTime;
var unReadCount = 0;
var regCallback = function(data) {
	return "<img src='images/bq/" + data.replace(/\[/, '').replace(/\]/, '')
			+ ".png'/ style='width:30px;height:30px'>";
};

var regCallback2 = function(data) {
	return "<img src='images/bq/" + data.replace(/\[/, '').replace(/\]/, '')
			+ ".png'/ style='width:20px;height:20px'>";
};

function loadErrorImg() {
	$(".a-user-avatar > img, .a-myinfo-avatar > img, .a-myavatar > img,.user-avatar>img")
			.error(function() {
				$(this).attr("src", "images/face.png");
			});
}
//发送人信息
var myInfo = {
	id : "",
	cName : "",
	photoUri : "",
	sex : 0,
	orgInfo : "",
	clientId : "",
	loginId : "",
	phone : "",
	email : "",
	birthday : "",
	provinceName : "",
	provinceID : "",
	cityName : "",
	cityID : "",
	orgName : "",
	departName : "",
	dutyName : ""
};
//消息条数
var messagesize = {
	total : 0
};

//操作字典
var OperaCode = {
	CODE_WNGG : "10000", // 院内公告
	CODE_DOC_TO_DOC : "11000", // 医生发送信息
	CODE_GROUP : "11001", // 医生对群组发送信息
	CODE_DOC_TO_PATIENT : "11002", // 医生对患者发送信息
	CODE_FRIENDS : "11003", // 好友申请
	CODE_SYS_GROUP : "10010", // 群组动态，退群
	CODE_SYS_GROUP_DEL : "10011", // 群组动态，群解散
	CODE_SYS_FRIENDS : "10020", // 医生同意好友申请后系统发起互相打招呼
	CODE_PATIENT_TO_DOC : "12000", // 患者对医生发送信息
	CODE_PHONE : "12001", // 预约回拨
	CODE_FREEBACK : "13000", // 意见反馈
	CODE_HEALTH_NEWS : "10001", // 健康报
	CODE_REFERRAL : "13004", // 转诊助手
	CODE_SYS_MESSAGE : "10002", // 系统公告
	CODE_LOVE_WEEK_NEWS : "13001", // 爱心值周排名
	CODE_LOVE_AWARD : "13002", // 爱心奖励
	CODE_PHONE_WEEK_NEWS : "13003", // 电话诊室周报
	CODE_HELP : "-9999" // 新手指导
};
//发送类型
var ContentType = {
	CONTENT_TEXT : 0,
	CONTENT_MP3 : 1,
	CONTENT_IMG : 2,
	CONTENT_IMG_TEXT : 3,
	CONTENT_RECOMMEND : 4
};

//通信类
function AnImClass() {
	this.appKey = 'drFnr1YojVjHrTfupuLDhppYLco7h6Ax';
	this.AnImObj = null;
}
//初始化通信
AnImClass.prototype.initEvent = function() {
	AnImClass.prototype.AnImObj = new AnIM(
			{
				key : this.appKey,
				onStatusChanged : function(isConnected, error) {//通信状态改变
					if (!isConnected) {

						ifConnecte();
					}
				},
				onReceivedMessage : function(msg_id, message, from,
						custome_data, timestamp) {//接收消息回调方法

					if (Structure.tarClientId == from
							&& $("#talking_box").css('display') == 'block') {

						loadMessage(custome_data, msg_id, message, false,
								timestampFormat(timestamp), "text", from);
					} else {

						showNewMessage(custome_data, message, from,
								timestampFormat(timestamp));
					}

				},
				onReceivedBinary : function(msg_id, fileType, from,
						custome_data, timestamp) {//接收二进制消息回调方法
					var url = "";
					var message = "";
					if (Structure.tarClientId == from&& $("#talking_box").css('display') == 'block') {
						if (fileType == 1) {
							url = AnImClass.prototype.AnImObj
									.getBinaryResourceURL(msg_id, "acc",
											"application/vnd.americandynamics.acc");
							message = '<audio src="'
									+ url
									+ '"  controls="controls">您的浏览器不支持 audio 标签。</audio>';
						} else {
							url = AnImClass.prototype.AnImObj
									.getBinaryResourceURL(msg_id, "jpg",
											"image/jpg");
							message = "<img src='" + url + "' style='width:"
									+ imgSize + "' ondblclick=showFullImg('" +url+ "') />";
						}
						loadMessage(custome_data, msg_id, message, false,
								timestampFormat(timestamp), "", from);
					} else {
						if (fileType == 1) {
							message = "[语音]";
						} else {
							message = "[图片]";
						}
						showNewMessage(custome_data, message, from,
								timestampFormat(timestamp));
					}
				},
				onReceivedTopicMessage : function(msg_id, message, from,
						topicId, cdata, timestamp) {//接收群消息回调方法
					if (Structure.tarClientId == topicId
							&& $("#talking_box").css('display') == 'block') {
						loadMessage(cdata, msg_id, message, false,
								timestampFormat(timestamp), "", topicId);
					} else {
						showNewMessage(cdata, message, topicId,
								timestampFormat(timestamp));
					}
					memberInfos[topicId] = {
						"data-id" : cdata.groupId,
						"data-name" : cdata.groupName,
						"data-clientId" : cdata.groupClientId,
						"data-photoUri" : cdata.groupPhoto,
						"data-sex" : "0"
					};
				},
				onReceivedTopicBinary : function(msg_id, fileType, from,
						topicId, cdata, timestamp) {//接收二进制群消息回调方法
					var url = "";
					var message = "";
					
					if (Structure.tarClientId == topicId&& $("#talking_box").css('display') == 'block') {
						if (fileType == 1) {
							url = AnImClass.prototype.AnImObj
									.getBinaryResourceURL(msg_id, "acc",
											"application/vnd.americandynamics.acc");
							message = '<audio src="'
									+ url
									+ '"  controls="controls">您的浏览器不支持 audio 标签。</audio>';
						} else {
							url = AnImClass.prototype.AnImObj
									.getBinaryResourceURL(msg_id, "jpg",
											"image/jpg");
							message = "<img src='" + url + "' style='width:"
									+ imgSize + "' ondblclick=showFullImg('" +url+ "') />";
						}
						loadMessage(cdata, msg_id, message, false,
								timestampFormat(timestamp), "", topicId);
					} else {
						if (fileType == 1) {
							message = "[语音]";
						} else {
							message = "[图片]";
						}
						showNewMessage(cdata, message, topicId,
								timestampFormat(timestamp));
					}
					memberInfos[topicId] = {
						"data-id" : cdata.groupId,
						"data-name" : cdata.groupName,
						"data-clientId" : cdata.groupClientId,
						"data-photoUri" : cdata.groupPhoto,
						"data-sex" : "0"
					};
				},
				onMessageSent : function(error, msg_id) {//发送回调方法
					$("#msg_" + msg_id).hide();
					if (error) {
						$("#msg_" + msg_id).empty();
						$("#msg_" + msg_id).addClass("tipico tipico-err");
					}
				},
				onReceivedReceiveACK : function(msg_id, from) {//发送确认回调方法
				},
				onReceivedReadACK : function(msg_id, from) {
				},
				onClientsRemovedFromTopic : function(event_id, topic_id,
						parties, timestamp) {

				},
				onTopicRemoved : function(event_id, topic_id, timestamp) {//群组解散回调方法

				},
				onReceivedNotice : function(msg_id, message, from, topicId,
						custome_data, timestamp) {//接收通知回调方法
					//alert('接收通知回调方法');
				}
			});
};
//连接通信
AnImClass.prototype.connect = function(clientId) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		AnImClass.prototype.AnImObj.connect(clientId);
	}
};
//发送文本消息
AnImClass.prototype.sendMessage = function(clientId, message, customData,
		receiveAck) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {

		var msgId = "";
		
		msgId = AnImClass.prototype.AnImObj.sendMessage(clientId, message,
				customData, receiveAck);
	
		var customeDataObj = $.parseJSON(customData);
		if (msgId) {
			$("#text_msg").val("");
		}
		loadMessage(customeDataObj, msgId, message, true, (new Date())
				.format("yyyy-MM-dd hh:mm:ss"), "text", clientId);
	}
};
//发送群组消息
AnImClass.prototype.sendMessageToTopic = function(topicId, message, customData,
		receiveAck) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		var msgId = AnImClass.prototype.AnImObj.sendMessageToTopic(topicId,
				message, customData, receiveAck);
		var customeDataObj = $.parseJSON(customData);
		if (msgId) {
			$("#text_msg").val("");
		}
		loadMessage(customeDataObj, msgId, message, true, (new Date())
				.format("yyyy-MM-dd hh:mm:ss"), "text", topicId);
	}
};

//获取私聊离线消息记录
AnImClass.prototype.getOfflineHistory = function(limit) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		AnImClass.prototype.AnImObj.getOfflineHistory(myInfo.clientId, limit,
				function(error, messages, leftCount) {
					if (error) {
						console.log("获取私聊离线消息记录出错："+error);
					} else {
						for ( var i in messages) {
							var message = messages[i];
							var messageStr = message.message;
							if (message.content_type != "text"
									&& message.customData
									&& message.customData.msgType == 1) {
								messageStr = '[语音]';
							} else if (message.content_type != "text"
									&& message.customData
									&& message.customData.msgType == 2) {
								messageStr = "[图片]";
							}
							showNewMessage(message.customData, messageStr,
									message.from,
									timestampFormat(message.timestamp),
									message.content_type, message.from);
						}
						
					}
				});
	}
};

//获取群聊离线消息记录
AnImClass.prototype.getOfflineTopicHistory = function(limit) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		AnImClass.prototype.AnImObj.getOfflineTopicHistory(myInfo.clientId,
				limit, "", function(error, messages, leftCount) {
					if (error) {
						console.log("获取群聊离线消息记录出错："+error);
					} else {
						for ( var i in messages) {
							var message = messages[i];
							var messageStr = message.message;
							if (message.content_type != "text"
									&& message.customData
									&& message.customData.msgType == 1) {
								messageStr = '[语音]';
							} else if (message.content_type != "text"
									&& message.customData
									&& message.customData.msgType == 2) {
								messageStr = "[图片]";
							}
							showNewMessage(message.customData, messageStr,
									message.topic_id,
									timestampFormat(message.timestamp),
									message.content_type, message.topic_id);
						}
						
					}
				});
	}
};

// 获取私聊历史消息记录
AnImClass.prototype.getHistory = function(tarClientId, clientId, limit,
		timestamp) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		AnImClass.prototype.AnImObj
				.getHistory(
						tarClientId,
						clientId,
						limit,
						timestamp,
						function(error, messages) {

							if (error) {
								console.log("获取私聊历史消息记录出错:"+error);
							} else {
								$(".loading_box").show();

								$(".sec-main-con").html("");
								for ( var i in messages) {

									var message = messages[i];

									var url = "";
									var messageStr = message.message;
									if (message.content_type != "text"
											&& message.customData
											&& message.customData.msgType == 1) {
										url = AnImClass.prototype.AnImObj
												.getBinaryResourceURL(
														message.msg_id, "acc",
														"application/vnd.americandynamics.acc");
										messageStr = '<audio src="'
												+ url
												+ '"  controls="controls">您的浏览器不支持 audio 标签。</audio>';
									} else if (message.content_type != "text"
											&& message.customData
											&& message.customData.msgType == 2) {
										url = AnImClass.prototype.AnImObj
												.getBinaryResourceURL(
														message.msg_id, "jpg",
														"image/jpg");
										messageStr = "<img src='" + url
												+ "' class='dbimg' style='width:" + imgSize
												+ "' />";
									}

									if (i == 0) {
										showMsgListMessage(
												message,
												messageStr,
												false,
												timestampFormat(message.timestamp));
									}
									if (i == messages.length - 1) {
										hisDateTime = message.timestamp;
									}

									loadHistoryMessage(message.customData,
											message.msg_id, messageStr,
											message.from == myInfo.clientId,
											timestampFormat(message.timestamp),
											message.content_type, message.from);

								}
								$(".dbimg").dblclick(function(){
																		    
								    $('#show-full-img > img').attr("src",$(this).attr("src"));
								    var contentImg=$('#show-full-img').get(0);
								    
								   // var  contentImg='<img src="'+$(this).attr("src")+'">';
								    var d = art.dialog({
									    title: '图片',
									    padding: '1px 1px',	
									    lock: true,
									    content: contentImg
										});
										$('.aui_content').niceScroll({cursorborder:"",cursorcolor:"#cccdd1"});
										
										d.show();
								    								    
								  });
								setTimeout(scrollBottom, 100);
								$(".loading_box").hide();
							}
						});
	}
};


AnImClass.prototype.getHistory2 = function(tarClientId, clientId, limit,timestamp) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {

		AnImClass.prototype.AnImObj.getHistory(tarClientId,clientId,limit,timestamp,function(error, messages) {

							if (error) {
								console.log(error);
							} else {
								
								for ( var i in messages) {

									
									var message = messages[i];

									var url = "";
									var messageStr = message.message;
									if (message.content_type != "text" && message.customData
											&& message.customData.msgType == 1) {
										messageStr = '[语音]';
									} else if (message.content_type != "text" && message.customData
											&& message.customData.msgType == 2) {
										messageStr = '[图片]';
									}
									if (message.customData.msgType == ContentType.CONTENT_IMG
											&& message.content_type == "text") {
										messageStr = "[图片]";
									} else if (message.customData.msgType == 0) {
										messageStr = YiHu.Util.htmlspecialchars(messageStr).replace(regPlace, regCallback2);
									}
									
									var NowTime=new Date().getTime();
									if (NowTime-message.timestamp<=1000*60*60*24*7){
									
										showOldMessage(message.customData, messageStr,tarClientId,timestampFormat(message.timestamp),message.content_type);
										
									}
									
								}

						
							}
						});
	}
};

// 获取群聊历史消息记录
AnImClass.prototype.getTopicHistory2 = function(topicId, clientId, limit,timestamp) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		
		AnImClass.prototype.AnImObj
				.getTopicHistory(
						topicId,
						limit,
						timestamp,
						clientId,
						function(error, messages) {
							if (error) {
								console.log(error);
							} else {
								
								for ( var i in messages) {
									var message = messages[i];
									var url = "";
									var messageStr = message.message;
									if (message.content_type != "text" && message.customData
											&& message.customData.msgType == 1) {
										messageStr = '[语音]';
									} else if (message.content_type != "text" && message.customData
											&& message.customData.msgType == 2) {
										messageStr = '[图片]';
									}
									if (message.customData.msgType == ContentType.CONTENT_IMG
											&& message.content_type == "text") {
										messageStr = "[图片]";
									} else if (message.customData.msgType == 0) {
										messageStr = YiHu.Util.htmlspecialchars(messageStr).replace(regPlace, regCallback2);
									}
									
									var NowTime=new Date().getTime();
									if (NowTime-message.timestamp<=1000*60*60*24*7){
									
										showOldMessage(message.customData, messageStr,topicId,timestampFormat(message.timestamp),message.content_type);
										
									}
								}	
							}
						});
					}
	
};


//显示历史消息
function showOldMessage(customData, message, from, dateStr, dataType) {
	

	var memberInfo = memberInfos[from];
	if(!memberInfo){
		return;
	}
	
	message = message.replace(/\n/g, "<br/>");

	var $linewMsg = $("#linewMsg_" + from);
	

	if ($linewMsg.length > 0) {
		return;
	}
	var isGroup = false;
	
	if (customData.groupId && customData.groupId != "") {
		isGroup = true;
	}

	var photoUri = memberInfo["data-photoUri"];
	photoUri = (photoUri && photoUri != "") ? photoUri : "images/face.png";

	var str = [
			'<li class="boss-user" id="linewMsg_' + from + '" data-clientId="'
					+ from + '" '
					+ (isGroup ? "data-group='1'" : "data-group='0'") + '>',
			"<a href=\"javascript:;\" onclick=\"showNewChat('" + from + "',"
					+ (isGroup ? true : false) + ");\" >",
			' <div class="boss-user-face"><img class=\"c-images-block\" src="'
					+ photoUri + '"/></div>',		
			'<div class="boss-user-name c-nowrap c-f14 c-333"> <p class="new-c-nowrap c-f14 c-333">'
					+ memberInfo["data-name"]
					+ '</p>',
			'<p class="c-nowrap c-f12 c-909090">'
					+ (isGroup ? customData.sourceName + ":"
							+ message.replace(regPlace, regCallback2) : message
							.replace(regPlace, regCallback2)) + '</p>',
			'</div>',
			' <div class="boss-user-time c-position-a">'
					+ YiHu.Util.dataTimeToStr(dateStr) + '</div>',									
			'</a>',
			'</li>' ];
	
	$("#msgUl").append(str.join(''));
	$("#newMsg_" + from).hide();
	//sumUnreadCount();


}



// 获取群聊历史消息记录
AnImClass.prototype.getTopicHistory = function(topicId, limit, timestamp,
		clientId) {
	if (AnImClass.prototype.AnImObj == null) {
		console.log("未注册通信");
	} else {
		AnImClass.prototype.AnImObj
				.getTopicHistory(
						topicId,
						limit,
						timestamp,
						clientId,
						function(error, messages) {
							if (error) {
								console.log("获取群聊历史消息记录出错："+error);
							} else {
								$(".loading_box").show();
								
								$(".sec-main-con").html("");
								for ( var i in messages) {
									var message = messages[i];
									var url = "";
									var messageStr = message.message;
									if (message.content_type != "text"
											&& message.customData
											&& message.customData.msgType == 1) {
										url = AnImClass.prototype.AnImObj
												.getBinaryResourceURL(
														message.msg_id, "acc",
														"application/vnd.americandynamics.acc");
										messageStr = '<audio src="'
												+ url
												+ '"  controls="controls">您的浏览器不支持 audio 标签。</audio>';
									} else if (message.content_type != "text"
											&& message.customData
											&& message.customData.msgType == 2) {
										url = AnImClass.prototype.AnImObj
												.getBinaryResourceURL(
														message.msg_id, "jpg",
														"image/jpg");
										messageStr = "<img src='" + url
												+ "' class='dbimg' style='width:" + imgSize
												+ "' />";
									}
									if (i == 0) {
										showMsgListMessage(
												message,
												messageStr,
												true,
												timestampFormat(message.timestamp));
									}
									if (i == messages.length - 1) {
										hisDateTime = message.timestamp;
									}
									loadHistoryMessage(message.customData,
											message.msg_id, messageStr,
											message.from == myInfo.clientId,
											timestampFormat(message.timestamp),
											message.content_type,
											message.topicId);
								}
									$(".dbimg").dblclick(function(){
																		    
								    $('#show-full-img > img').attr("src",$(this).attr("src"));
								    var contentImg=$('#show-full-img').get(0);
								    
								   // var  contentImg='<img src="'+$(this).attr("src")+'">';
								    var d = art.dialog({
									    title: '图片',
									    padding: '1px 1px',	
									    lock: true,
									    content: contentImg
										});
										$('.aui_content').niceScroll({cursorborder:"",cursorcolor:"#cccdd1"});
										
										d.show();
								    								    
								  });
								setTimeout(scrollBottom, 100);
								$(".loading_box").hide();
							}
						});
	}

};

function showMsgListMessage(message, messageStr, isGroup, dateStr) {
	//                        $("#linewMsg_" + Structure.tarClientId + ">.fl>.hidden>.a-msg-time").html(YiHu.Util.dataTimeToStr(timestampFormat(message.timestamp)));
	var msg = messageStr;
	if (message.content_type != "text" && message.customData
			&& message.customData.msgType == 1) {
		msg = '[语音]';
	} else if (message.content_type != "text" && message.customData
			&& message.customData.msgType == 2) {
		msg = '[图片]';
	}
	if (message.customData.msgType == ContentType.CONTENT_IMG
			&& message.content_type == "text") {
		msg = "[图片]";
	} else if (message.customData.msgType == 0) {
		msg = YiHu.Util.htmlspecialchars(msg).replace(regPlace, regCallback2);
	}
	$(
			"#linewMsg_" + Structure.tarClientId
					+ ">a>.boss-user-name>.c-nowrap.c-f12.c-909090").html(
			(isGroup ? message.customData.sourceName + ":" + msg : msg));

	$("#linewMsg_" + Structure.tarClientId + ">a>.boss-user-time.c-position-a")
			.html(YiHu.Util.dataTimeToStr(dateStr));
}
function showMsgMessage(content_type, customData, messageStr, isGroup, from,
		dateStr) {
	//                        $("#linewMsg_" + Structure.tarClientId + ">.fl>.hidden>.a-msg-time").html(YiHu.Util.dataTimeToStr(timestampFormat(message.timestamp)));
	var msg = messageStr;
	if (content_type != "text" && customData && customData.msgType == 1) {
		msg = '[语音]';
	} else if (content_type != "text" && customData && customData.msgType == 2) {
		msg = '[图片]';
	}
	if (customData.msgType == ContentType.CONTENT_IMG && content_type == "text") {
		msg = "[图片]";
	} else if (customData.msgType == 0) {
		msg = YiHu.Util.htmlspecialchars(msg).replace(regPlace, regCallback2);
	}
	$("#linewMsg_" + from + ">a>.boss-user-name>.c-nowrap.c-f12.c-909090")
			.html((isGroup ? customData.sourceName + ":" + msg : msg));

	$("#linewMsg_" + Structure.tarClientId + ">a>.boss-user-time.c-position-a")
			.html(YiHu.Util.dataTimeToStr(dateStr));

}
//加载信息
function loadMessage(customData, msgId, message, isMy, dateStr, contentType,
		from) {
	
	showMsgMessage(contentType, customData, message,
			(customData.groupId && customData.groupId != ""), from, dateStr);
	if (customData.msgType == ContentType.CONTENT_IMG && contentType == "text") {
		message = "<img src='" + message + "'  style='width:50%' />";
	} else if (customData.msgType == 0) {
		message = YiHu.Util.htmlspecialchars(message);
	}
	if (!message) {
		message = "";
	}
	message = message.replace(/\n/g, "<br/>");
	var showTime = false;
	if (!oldDatetime) {
		oldDatetime = new Date();
		showTime = true;
	}
	if (oldDatetime && YiHu.Util.addMinutes(oldDatetime, 5) < new Date()) {
		oldDatetime = new Date();
		showTime = true;
	}
	var photoUri = customData.uri;
	photoUri = (photoUri && photoUri != "") ? photoUri : "images/face.png";

	var str = [
			'<div class="t-center">',
			showTime ? '<p class="msg-time">'
					+ YiHu.Util.dataTimeToStr(dateStr) + '</p></div>' : "",
			'<div class="' + (isMy ? 'msg-mine' : 'msg-other') + '">',
			'<div class="user-avatar"><img src="'
					+ photoUri
					+ '" alt="" style="width:40px;height:40px;border-radius:20px 20px" /></div>',
			' <div class="' + (isMy ? 'fr' : 'fl') + '">',
			'<div class="user-name">' + customData.sourceName + ' </div>',
			'<div class="msg-box">',
			'<p>' + message.replace(regPlace, regCallback) + '</p>',
			'<em class="msg-box-arr"><i></i></em>',
			(isMy ? '<i class="tipico" id="msg_' + msgId
					+ '"><img src="images/022.gif"></i>' : '')
					+ '</div>', '</div></div>' ];
	$(".sec-main-con").append(str.join(''));	
	setTimeout(scrollBottom, 100);
	// loadErrorImg();
}

//加载历史信息
function loadHistoryMessage(customData, msgId, message, isMy, dateStr,
		contentType, from) {

	if (customData.msgType == ContentType.CONTENT_IMG && contentType == "text") {
		message = "<img src='" + message + "'  style='width:100%' />";
	} else if (customData.msgType == 0) {
		message = YiHu.Util.htmlspecialchars(message);
	}

	if (!message) {
		message = "";
	}

	message = message.replace(/\n/g, "<br/>");
	var photoUri = customData.uri;

	photoUri = (photoUri && photoUri != "") ? photoUri : "images/face.png";

	var str = [
			'<div class="t-center" style="padding-top:50px;">',
			'<p class="msg-time">' + YiHu.Util.dataTimeToStr(dateStr)
					+ '</p></div>',
			'<div class="' + (isMy ? 'msg-mine' : 'msg-other') + '">',
			'<div class="user-avatar"><img src="'
					+ photoUri
					+ '" alt="" style="width:40px;height:40px;border-radius:20px 20px" /></div>',
			' <div class="' + (isMy ? 'fr' : 'fl') + '">',
			'<div class="user-name">' + customData.sourceName + ' </div>',
			'<div class="msg-box">',
			'<p>' + message.replace(regPlace, regCallback) + '</p>',
			'<em class="msg-box-arr"><i></i></em>', '</div>', '</div></div>' ];
	$(".sec-main-con").prepend(str.join(''));

	//window.frames['iframe-im-if'].contentWindow.loadMessage(str.join(''));	
}

//显示新消息
function showNewMessage(customData, message, from, dateStr, dataType) {
	if (customData.msgType == ContentType.CONTENT_IMG && dataType == "text") {
		message = "<img src='" + message + "' style='width:" + imgSize + "' />";
	} else if (customData.msgType == 0) {
		message = YiHu.Util.htmlspecialchars(message);
	}

	var memberInfo = memberInfos[from];

	if (!memberInfo) {

		if (customData.groupId && customData.groupId != "") {
			memberInfos[from] = {
				"data-id" : customData.groupId,
				"data-name" : customData.groupName,
				"data-clientId" : customData.groupClientId,
				"data-photoUri" : customData.groupPhoto,
				"data-sex" : "0"
			};
		} else {
			memberInfos[from] = {
				"data-id" : customData.sourceId,
				"data-name" : customData.sourceName,
				"data-clientId" : from,
				"data-photoUri" : customData.uri,
				"data-sex" : customData.sex
			};
		}
	}
	message = message.replace(/\n/g, "<br/>");
	
	var $newMsg = $("#newMsg_" + from);
	var msgCount = 1;
	
	if ($newMsg.length > 0) {
		var oldMsgCount = parseInt($newMsg.html());
		msgCount += oldMsgCount;
		if (msgCount > 99)
			msgCount = 99;
		$("#linewMsg_" + from).detach();
	} else {
		newMsgBodyCount += 1;
	}
	if($("#linewMsg_" + from).length > 0){
		$("#linewMsg_" + from).detach();
	}
	
	
	var isGroup = false;
	
	if (customData.groupId && customData.groupId != "") {
		isGroup = true;
	}

	var photoUri = isGroup ? customData.groupPhoto : customData.uri;
	photoUri = (photoUri && photoUri != "") ? photoUri : "images/face.png";

	var str = [
			'<li class="boss-user" id="linewMsg_' + from + '" data-clientId="'
					+ from + '" '
					+ (isGroup ? "data-group='1'" : "data-group='0'") + '>',
			"<a href=\"javascript:;\" onclick=\"showNewChat('" + from + "',"
					+ (isGroup ? true : false) + ");\" >",
			' <div class="boss-user-face"><img class=\"c-images-block\" src="'
					+ photoUri + '"/></div>',		
			'<div class="boss-user-name c-nowrap c-f14 c-333"> <p class="new-c-nowrap c-f14 c-333">'
					+ (isGroup ? customData.groupName : customData.sourceName)
					+ '</p>',
			'<p class="c-nowrap c-f12 c-909090">'
					+ (isGroup ? customData.sourceName + ":"
							+ message.replace(regPlace, regCallback2) : message
							.replace(regPlace, regCallback2)) + '</p>',
			'</div>',
			' <div class="boss-user-time c-position-a">'
					+ YiHu.Util.dataTimeToStr(dateStr) + '</div>',									
			'<div class="boss-user-number c-position-a" id="newMsg_' + from
					+ '">' + msgCount + '</div>', '</a>',
			'</li>' ];
	
	$("#msgUl").prepend(str.join(''));
	sumUnreadCount();


}
//计算未读数据
function sumUnreadCount() {
	unReadCount = 0;
	$(".boss-user-number.c-position-a").each(function(index, element) {
		unReadCount += parseInt($(element).html());
		if ($(".boss-user-number.c-position-a").length - 1 == index) {
			if (unReadCount > 0) {
				$("#im_count").html(unReadCount);
				$("#im_count").show();
			}
		}
	});
}


function scrollBottom() {
	
	$(".sec_scroll").scrollTop(10000);
}


//加载更多聊天记录
function loadMoreMessage() {
	if (Structure.isGroup) {
		anImClass.getTopicHistory(Structure.tarClientId, myInfo.clientId, 30,
				hisDateTime);
	} else {
		anImClass.getHistory(Structure.tarClientId, myInfo.clientId, 30,
				hisDateTime);
	}
}


function showNewChat(clientId, isGroup) {
	if(clientId&&clientId!=''){
		if (clientId == myInfo.clientId) {
        showMsg("不能和自己聊天!!!");
        $(".loading_box").hide();
        return false;
    }		
		var clickObj = memberInfos[clientId];
		var clickMsgCount = parseInt($("#newMsg_" + clientId).html());
		if (!clickMsgCount){
			
			clickMsgCount=0;
		}
		
		if (clickObj["data-clientId"] != Structure.tarClientId) {
			//event.stopPropagation();
			Structure.tarClientId = clickObj["data-clientId"];
			Structure.cName = clickObj["data-name"];
			Structure.isGroup = isGroup;
			Structure.id = clickObj["data-id"];
			Structure.photoUri = clickObj["data-photoUri"];
			Structure.photoUri = Structure.photoUri == "" ? "images/face.png"
					: Structure.photoUri;
			Structure.sex = clickObj["data-sex"];
			$(".section-header.clearfix > h4").html(Structure.cName);
			$("#talkingID").val(Structure.id);
			$("#clientId").val(Structure.tarClientId);   
			$("#talking_box").show();
			$('#iframe-im').show();
			$(".sec-main-con").html("");
			$("#text_msg").val("");
			$('#text_msg').focus(); 
			if (clickMsgCount > 0) {
	
				$("#newMsg_" + clientId).html('0').hide();
				$("#newMsg_" + clientId).detach();
				newMsgBodyCount -= 1;
			}
			
			sumUnreadCount();
			if (newMsgBodyCount <= 0) {
				newMsgBodyCount = 0;
				$("#im_count").hide();
				//$(".boss-user-number.c-position-a").remove();
			}
			if (isGroup) {
				$("#isGroup").val(1);
				anImClass.getTopicHistory(Structure.tarClientId,10 + parseInt(clickMsgCount), "", myInfo.clientId);
			} else{
				$("#isGroup").val(0);
				anImClass.getHistory(Structure.tarClientId, myInfo.clientId,10 + parseInt(clickMsgCount), "");
			}
		}
	}
}

var newPushMsgIds = '';
var limitBody = 1;
var newAppMsgIds = '';
var limitAppBody = 1;
//加载未读推送消息
function loadPushMsgList(frist) {

	$.ajax({
		url : "PushMsgListAction/getPushMsgList",
		dataType : "json",
		data : {
			readStatus : 1
		},
		type : 'POST',
		success : function(msg) {
			if (msg) {
				if (msg.Code == 10000) {

					//if (msg.Result && msg.Result != '') {
						var data = msg.Result;
						var sysMsgCount = 0;
						var appMsgCount = 0;
						var $newMsg = $("#newMsg_sysMessage");
						if ($newMsg.length > 0) {
							//$("#linewMsg_sysMessage").detach();
							var dateStr = '';
							var content;
							var title = '';
							var dateStrApp = '';
							var contentApp;
							var titleApp = '';
							var fFlag=0;
							var gFlag=0;
							newPushMsgIds = '';
							newAppMsgIds = '';
																					
														
							for ( var i = 0; i < data.length; i++) {
								if (data[i].operCode&& data[i].operCode != 11003) {
									if(data[i].operCode==10020){
										fFlag=1;
									}
									if(data[i].operCode==10010||data[i].operCode==10011){
										gFlag=1;
									}
									dateStr = timestampFormat(data[i]._msg.insertTimeMillis);
									content = data[i].content;
									if (typeof (content) == 'object') {
										if (data[i].content.describe) {
											content = data[i].content.describe;
										} else {
											content = '';
										}
									} else {
										content = (content && content != "") ? content	: "";
									}
									title = data[i].title;
									title = (title && title != "") ? title: "";

									newPushMsgIds += data[i].msgId+ ",";

									sysMsgCount++;
									if (sysMsgCount > 99)
										sysMsgCount = 99;

								}/*else if(data[i].operCode == 13006){
									dateStrApp = timestampFormat(data[i]._msg.insertTimeMillis);
									contentApp = data[i].content;
									if (typeof (contentApp) == 'object') {
										if (data[i].content.describe) {
											contentApp = data[i].content.describe;
										} else {
											contentApp = '';
										}
									} else {
										contentApp = (contentApp && contentApp != "") ? contentApp : "";
									}
									titleApp = data[i].title;
									titleApp = (titleApp && titleApp != "") ? titleApp: "";

									newAppMsgIds += data[i].msgId+ ",";
																		
									appMsgCount++;
									if (appMsgCount > 99)
										appMsgCount = 99;
								}*/
							}
							
							var appObj = msg.appArray; 
							
							for (var p in appObj) { 
							    // p就是得到的key值 
							  
							   var  appMessage=appObj[p];
							   var  appName='';
							   dateStrApp = timestampFormat(appMessage[0]._msg.insertTimeMillis);
									contentApp = appMessage[0].content;
									
									if (typeof (contentApp) == 'object') {
										if (appMessage[0].content.describe) {
											
											contentApp = appMessage[0].content.describe;																						
											
										} else {
											contentApp = '';
										}
										
										appName=appMessage[0].content.appName;
									} else {
										contentApp = (contentApp && contentApp != "") ? contentApp : "";
									}
									titleApp = appMessage[0].title;
									
									titleApp = (titleApp && titleApp != "") ? titleApp: "";

									//newAppMsgIds += appMessage[0].msgId+ ",";
											appMsgCount	=	appObj[p].length;					
									//alert(appMsgCount);
								//	if (appMsgCount > 99)
								//		appMsgCount = 99;
							   
							
							   
							var $newAppMsg = $("#newAppMsg_" + p);
	
	
							if ($newAppMsg.length > 0) {
								//var oldMsgCount = parseInt($newAppMsg.html());
								//appMsgCount += oldMsgCount;
								if (appMsgCount > 99)
									appMsgCount = 99;
								$("#lineAppMsg_" + p).detach();
								
							} else {
								newMsgBodyCount += 1;
							}
							if($("#lineAppMsg_" + p).length > 0){
								
								$("#lineAppMsg_" + p).detach();
							}						
							
							var str = [
									'<li class="boss-user" id="lineAppMsg_' + p +  '">',
									"<a href=\"javascript:;\" onclick=\"showAppMessage('" + p +  "');\" >",
									' <div class="boss-user-face"><img class=\"c-images-block\" src="images/dbsx.png"/></div>',		
									'<div class="boss-user-name"> <p class="new-c-nowrap c-f13 c-333">'+appName+'-待办工作'+ '</p>',
									'<p class="c-nowrap c-f12 c-909090">'+titleApp+':'+ contentApp + '</p>',
									'</div>',
									'<div class="boss-user-time c-position-a">'+ YiHu.Util.dataTimeToStr(dateStrApp) + '</div>',									
									'<div class="boss-user-number c-position-a" id="newAppMsg_' + p+ '">' + appMsgCount + '</div>',
									'</a>',
									'</li>' ];
							
							$("#msgUl").prepend(str.join(''));
							sumUnreadCount();							   							   							    
						}  

							if (sysMsgCount > 0) {
								$("#newMsg_sysMessage").html(sysMsgCount).show();
								$("#linewMsg_sysMessage >a > .boss-user-time.c-position-a").html(YiHu.Util.dataTimeToStr(dateStr));
								$("#title_sysMessage").html(title);
								$("#content_sysMessage").html(content);

								newMsgBodyCount += limitBody;
								limitBody = 0;

							}
						/*	
							if (appMsgCount > 0) {
								$("#newMsg_appMessage").html(appMsgCount).show();
								$("#linewMsg_appMessage >a > .boss-user-time.c-position-a").html(YiHu.Util.dataTimeToStr(dateStrApp));
								$("#title_appMessage").html(titleApp);
								$("#content_appMessage").html(contentApp);

								newMsgBodyCount += limitAppBody;
								limitAppBody = 0;

							}*/
							
							if(frist!=1){
								
								if(gFlag==1){
									loadGroups();
								}
								if(fFlag==1){
									loadFriends();
								}
							}
						}
						
						sumUnreadCount();
				//	}
				} else if (msg.Code == -8000) {
					//showHideMsg(data.Message, 3);
					window.location.href = "jump.jsp";
				} else {
					//showHideMsg("加载未读推送消息失败", 1);
				}
			} else {
				//showHideMsg("加载未读推送消息失败", 1);
			}

		},
		error : function() {
			//showHideMsg("加载未读推送消息失败", 1);
		}
	});

}

//加载好友邀请中列表信息
function loadFriendInvite() {

	$.ajax({
		url : "UserFriendInvite/queryFriendInviteList",
		dataType : "json",
		type : 'POST',
		success : function(msg) {
			if (msg) {
				if (msg.Code == 10000) {
					var $newMsg = $("#newMsg_VMessage");
					if ($newMsg.length > 0) {
						var clickMsgCount = parseInt($(
								"#newMsg_VMessage").html());

						if (clickMsgCount > 0) {

							$("#newMsg_VMessage").html('0').hide();
							newMsgBodyCount -= 1;
						}

						sumUnreadCount();
						if (newMsgBodyCount <= 0) {
							newMsgBodyCount = 0;
							$("#im_count").hide();
							//$(".boss-user-number.c-position-a").remove();
						}
						$("#linewMsg_VMessage").detach();
					}
					if (msg.Result && msg.Result != '') {
						var data = msg.Result;
						var sysMsgCount = 0;
						var VMessageCount = 0;

						var str = '';

						for ( var i = 0; i < data.length; i++) {

							var photoUri = data[i].photoUri;
							photoUri = (photoUri && photoUri != "") ? photoUri
									: "images/face.png";

							var dateStr = data[i].updateTime;

							VMessageCount++;
							if (VMessageCount > 99)
								VMessageCount = 99;
							str = [
									'<li class="boss-user" id="linewMsg_VMessage">',
									'<a href=\"javascript:;\" onclick=\"showVMessage();\">',
									'<div class="boss-user-face"><img class=\"c-images-block\" src="'
											+ photoUri + '"/></div>',
									'<div class="boss-user-name c-nowrap c-f14 c-333"> <p class="c-nowrap c-f14 c-333">'
											+ '您有一条验证消息' + '</p>',
									'<p class="c-nowrap c-f12 c-909090">'
											+ data[i].userName
											+ '请求添加您为好友' + '</p>',
									'</div>',
									'<div class="boss-user-time c-position-a">'
											+ YiHu.Util
													.dataTimeToStr(dateStr)
											+ '</div>',
									'<div class="boss-user-number c-position-a" id="newMsg_VMessage">'
											+ VMessageCount + '</div>',
									'</a></li>' ];

						}

						if (str != '') {
							
								newMsgBodyCount += 1;
							
							$("#msgUl").prepend(str.join(''));
						}
						
						sumUnreadCount();
					}
				} else if (msg.Code == -8000) {
					//showHideMsg(data.Message, 3);
					window.location.href = "jump.jsp";
				} else {
					//showHideMsg("加载好友邀请中列表信息失败", 1);
				}
			} else {
				//showHideMsg("加载好友邀请中列表信息失败", 1);
			}

		},
		error : function() {
			//showHideMsg("加载好友邀请中列表信息失败", 1);
		}
	});

}

function showVMessage() {
	$('#iframe-userInfo').attr("src", "");
	showIframe('verificationMessage.html');

}

function showSysMessage() {
	var clickMsgCount = parseInt($("#newMsg_sysMessage").html());

	if (clickMsgCount > 0) {

		$("#newMsg_sysMessage").html('0').hide();
		newMsgBodyCount -= 1;
		limitBody = 1;
	}

	sumUnreadCount();
	if (newMsgBodyCount <= 0) {
		newMsgBodyCount = 0;
		$("#im_count").hide();
		//$(".boss-user-number.c-position-a").remove();
	}
	$('#iframe-userInfo').attr("src", "");
	showIframe('sysMessage.html');

}

function showAppMessage(id) {
	var clickMsgCount = parseInt($("#newAppMsg_"+id).html());

	if (clickMsgCount > 0) {

		$("#newAppMsg_"+id).html('0').hide();
		newMsgBodyCount -= 1;
		//limitAppBody = 1;
	}

	sumUnreadCount();
	if (newMsgBodyCount <= 0) {
		newMsgBodyCount = 0;
		$("#im_count").hide();
		//$(".boss-user-number.c-position-a").remove();
	}
	var src='appMessage.html?appId='+id;
	$('#iframe-userInfo').attr("src", "");
	showIframe(src);

}

function showFullImg(url){
	
	$('#show-full-img > img').attr("src",url);
  var contentImg=$('#show-full-img').get(0);
  
 // var  contentImg='<img src="'+$(this).attr("src")+'">';
  var d = art.dialog({
  	title: '图片',    
    padding: '1px 1px',	
    lock: true,
    content: contentImg
	});
	$('.aui_content').niceScroll({cursorborder:"",cursorcolor:"#cccdd1"});
	
	d.show();
	
}


function ifConnecte() {

	art.dialog({
		lock : false,
		artIcon : 'ask',
		opacity : 0.4,
		time: 5,	
		width : '15%',
		height: '10%',	
		title : '提示',
		content : '已经在其他地方登入，IM会话已断开（您如果还想继续进行IM聊天，请重新登入系统）',
		left: '95%',				// X轴坐标
		top: '100%', 				// Y轴坐标
		init: function() {
			$(".aui_close").hide();
		}
	});
	
}
