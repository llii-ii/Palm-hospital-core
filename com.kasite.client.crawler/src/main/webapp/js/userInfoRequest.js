//修改用户信息
function setUserInfo() {
    var name = $("#myInfo_name_edit").val();
    var sex = $("#myInfo_sex_edit").text();
    if (sex == "男") {
        sex = 1;
    }else if (sex == "女") {
        sex = 2;
    } else {
        sex = 3;
    }
    var phone = $("#myInfo_phone_edit").val();
    var email = $("#myInfo_email_edit").val();
    var birthday = $("#myInfo_birthday_edit").val();
    var provinceName = $("#myInfo_provinceName_edit").text();
    var provinceID=$("#myInfo_provinceName_edit").attr("data-value");
    var cityName = $("#myInfo_cityName_edit").text();
    var cityID=$("#myInfo_cityName_edit").attr("data-value");
    var operatorId = parent.myInfo.id;
    var operatorName = parent.myInfo.cName;
    var param = {
            operatorId: operatorId,
            operatorName: operatorName,
            userID: operatorId,
            cName: name,
            sex: sex,
            mainPhone: phone,
            email: email,
            birthDate: birthday,
            provinceName: provinceName,
            provinceID: provinceID,
            cityName: cityName,
            cityID:cityID
        };
    $.ajax({
        url: "UserInfoRequestAction/setUserInfo",
        dataType: "json",
        type: 'POST',
        data:  param ,
        success: function (data) {
          if (data) {
            if (data.Code == 10000) {
            	parent.myInfo.cName=name;
            	parent.myInfo.sex = sex;
            	parent.myInfo.phone= phone;
	            parent.myInfo.email= email;
	            parent.myInfo.birthday= birthday;
	            parent.myInfo.provinceName=provinceName;
	            parent.myInfo.provinceID=provinceID;
	            parent.myInfo.cityName=cityName;
	            parent.myInfo.cityID=cityID;
              parent.showHideMsg(data.Message,1);
              
              parent.$('#iframe-userInfo').attr("src","myUserInfo.html");
							window.location.href = "myUserInfo.html";
							parent.$(".c-f20.c-nowrap").html(parent.myInfo.cName);
              //location.reload();
            }else if(data.Code == -8000){
             	//parent.showHideMsg(data.Message,3);
             	window.parent.location.href = "jump.jsp";
           }else if(data.Message!=null&&data.Message!=""){
           		parent.showHideMsg(data.Message,1);
           }else{
             	parent.showHideMsg("修改信息出错",1);
           }
          }else{
          	parent.showHideMsg("修改信息出错",1);
          }
        },
        error: function () {
					parent.showHideMsg("修改信息出错",1);
        }
    });
}

//修改密码
function resetPwd(paramsObj) {
    $.ajax({
        url: "UserInfoRequestAction/resetPwd",
        dataType: "json",
        type: 'POST',
        data:  paramsObj ,
        success: function (data) {
            if (data) {
                if (data.Code == 10000) {
                    parent.showHideMsg(data.Message,3);
                    parent.imClosed();
                } else if(data.Code == -10000) {
                    parent.showHideMsg("原密码错误",3);
                    
                }else if(data.Code == -8000){
	               	//parent.showHideMsg(data.Message,3);
	               	window.parent.location.href = "jump.jsp";
	             }else if(data.Message!=null&&data.Message!=""){
	           		parent.showHideMsg(data.Message,3);
	          	 }
            }
        },
        error: function () {
					parent.showHideMsg("修改密码出错",3);
        }
    });
}


//意见反馈
function createFeedBackBySend(paramsObj) {
    $.ajax({
        url: "UserInfoRequestAction/feedBack",
        dataType: "json",
        type: 'POST',
        data:  paramsObj ,
        success: function (data) {
            if (data) {
                if (data.Code == 10000) {
                    parent.showHideMsg("意见反馈成功",3);
                    $("#feedback_txt").val('');
                }else if(data.Code == -8000){
		             	//parent.showHideMsg(data.Message,3);
		             	window.parent.location.href = "jump.jsp";
		            }else{
		            	parent.showHideMsg("意见反馈出错",1);
		            }
            }
        },
        error: function () {
						
						parent.showHideMsg("意见反馈出错",1);
        }
    });
}

//查询是否好友
function isfriend(fid) {
    var param = {
        
        passiveUserId: fid
    };
    $.ajax({
        url: "IsFriendAction/isFriend",
        dataType: "json",
        type: 'POST',
        data:  param ,
        success: function (data) {
            if (data.Code == 10000) {
                if (data.isFirend == 1) {
                    $("#addFriend").hide();
                    $("#delFriend").show();
                } else {
                    $("#addFriend").show();
                    $("#delFriend").hide();
                }
            }else if(data.Code == -8000){
             	//parent.showHideMsg(data.Message,3);
             	window.parent.location.href = "jump.jsp";
            }else{
            	parent.showHideMsg("查询是否好友出错",1);
            }
        }
    });
}

//加为好友
function addFriend() {
    var param = {
        
        passiveUserId: $("#userid").val()
    };

    $.ajax({
        url: "IsFriendAction/addFriend",
        dataType: "json",
        type: 'POST',
        data:  param,
        success: function (data) {
            if (data.Code == "10000") {
                parent.showHideMsg("请求加为好友成功，正在等待好友验证",3);
                parent.imClosed();
                //parent.$('.sec .message_box').hide();
            }else if(data.Code == -8000){
             	//parent.showHideMsg(data.Message,3);
             	window.parent.location.href = "jump.jsp";
            }else{
            	parent.showHideMsg("加好友出错",1);
            }
        }
    });
}

//删除好友
function delFriend() {
	if (!confirm("确定要删除好友吗？")) {
			return;
	}
    var param = {
        
        passiveUserId: $("#userid").val()
    };
    $.ajax({
        url: "IsFriendAction/delFriend",
        dataType: "json",
        data:  param,
        type: 'POST',
        success: function (data) {
            if (data.Code == 10000) {
                parent.showHideMsg("删除成功",3);
                parent.imClosed();
                parent.$(".user-scroll").html("");
                parent.loadFriends();
            }else if(data.Code == -8000){
             	//parent.showHideMsg(data.Message,3);
             	window.parent.location.href = "jump.jsp";
            }else{
            	parent.showHideMsg("删除出错",1);
            }
        }
    });
}