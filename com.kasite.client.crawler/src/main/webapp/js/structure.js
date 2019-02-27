
/// <reference path="jquery-1.8.3.min.js" />
function loadScript(url, callback) {
    var script = document.createElement_x("script");
    script.type = "text/javascript";
    if (script.readyState) { //IE 
        script.onreadystatechange = function() {
            if (script.readyState == "loaded" ||
                script.readyState == "complete") {
                script.onreadystatechange = null;
                callback();
            }
        };
    } else { //Others: Firefox, Safari, Chrome, and Opera 
        script.onload = function() {
            callback();
        };
    }
    script.src = url;
    document.body.appendChild(script);
}

function Structure(strJson, appElement) {
    this.keyWord = "";
    this.strJson = strJson;
    this.appElement = appElement;
    this.tarClientId = "";
    this.topic_id = "";
    this.cName = "";
    this.id = "";
    this.photoUri = "";
    this.sex = "";
    this.isGroup = false;
}

Structure.prototype.showUI = function (keyWord) {
    var _this = this;
    _this.keyWord = keyWord;
    if (keyWord && _this.keyWord.length) {

    } else {
    }

     
    var json = _this.strJson;
    
    if (json.Code == 10000) {
    		_this.appElement.html("");
        if (json.Depart.length||json.Emp.length) {
            var parent = $('<ul class="im-user-tree"></ul>');
            showwall(json, parent, 0);
            _this.appElement.append(parent);
            //loadErrorImg();
        }
    }
    

   
};

 function showwall(json, parent,index) {
 			parent.html("");
    	var ind=parseInt(index)+1;
        for (var menu = 0; menu < json.Depart.length; menu++) {
            var li = $("<li></li>");
            
            if (json.Depart[menu].OrgID) {
                li.html("<div class=\"tree-hd\"><a href=\"javascript:;\" title=\"" + json.Depart[menu].OrgName  + "(" + json.Depart[menu].UserCount + ")" +"\" onclick=\"getTree('"+json.Depart[menu].OrgID+"',"+ind+");\"><i class=\"iconfont\">&#xe609;</i><span class=\"c-f14\">" + json.Depart[menu].OrgName + '(' + json.Depart[menu].UserCount + ')</span></a></div>').attr(
                    {
                        "data-id": json.Depart[menu].OrgID,
                        "data-name": json.Depart[menu].OrgName,
                        "data-type": "1"
                    }).append("<ul id=SubDepart"+json.Depart[menu].OrgID+"></ul>").appendTo(parent);

                //showwall(structure_list[menu].structure_list, $(li).find("ul")[0], index + 1);
            }
        
        }
        for (var i = 0; i < json.Emp.length; i++) {
        	var li = $("<li></li>");

          var photoUri = json.Emp[i].PhotoUri;
          var sex = json.Emp[i].Sex;
          if (photoUri && photoUri != "") {

          } else if (sex == 1) {
              photoUri = "images/face.png";
          } else if (sex == 0) {
              photoUri = "images/face.png";
          } else {
              photoUri = "images/face.png";
          }
          if(json.Emp[i].ImClientId&&json.Emp[i].ImClientId!=''){
          
	          li.html("<div class=\"tree-bd\"><a href=\"javascript:;\" onclick=\"showMember('"+json.Emp[i].ImClientId+"');\"><div class=\"tree-bd-box c-position-r\" style=\"padding-left:"+ind*15+"px;\"><div class=\"tree-bd-face\"><img class=\"c-images-block\" src=\"" + photoUri + "\"/></div>"
	              + '<div class=\"tree-bd-name c-nowrap c-f14 c-333\">' + json.Emp[i].CName + '</div></div></a>' 
	              + '</div>').attr({
	                  "data-id": json.Emp[i].ImClientId,
	                  "data-type": "2"
	              }).appendTo(parent);
	          memberInfos[json.Emp[i].ImClientId] = {
	              "data-id": json.Emp[i].UserID,
	              "data-name": json.Emp[i].CName,
	              "data-clientId": json.Emp[i].ImClientId,
	              "data-photoUri": json.Emp[i].PhotoUri,
	              "data-sex": json.Emp[i].Sex
	          };
          
         	}
	 
        
      	}
    }

function showMember(ImClientId) {
	if(clientId&&clientId!=''){
		showChat(ImClientId);
	}
}
function getTree(orgId,index) {		
  		
	$.ajax({
        url: "SubDepartAction/getSubDepart",
        dataType: "json",
        type: 'POST',
        data: { orgId: orgId },
        success: function(orgData) {
        	
            if (orgData) {
                showwall(orgData, $("#SubDepart"+orgId),index);

                //$("#showLoading").hide();
            }
        
        },
        error: function(errorData) {
        }
    });
  	
}

function showMsgChat(clientId, isGroup) {
   if(clientId&&clientId!=''){
    if (clientId == myInfo.clientId) {
        showMsg("不能和自己聊天!!!");
        $(".loading_box").hide();
        return false;
    } 
    $("#talking_box").show();
		$('#iframe-im').show();
		$('#text_msg').focus(); 
    if (clientId != Structure.tarClientId) {
    		var clickMsgCount = parseInt($("#newMsg_" + clientId).html());
        var memberInfo = memberInfos[clientId];
        
        Structure.tarClientId = memberInfo["data-clientId"];
        
        Structure.cName = memberInfo["data-name"];
        
        Structure.id = memberInfo["data-id"];
        Structure.isGroup = isGroup;
        Structure.photoUri = memberInfo["data-photoUri"];
        Structure.photoUri = Structure.photoUri == "" ? "images/face.png" : Structure.photoUri;
        Structure.sex = memberInfo["data-sex"];
    
        $(".section-header.clearfix > h4").html(Structure.cName);
        $("#talkingID").val(Structure.id);
        
        //$("#iframe-im").show();
        $(".sec-main-con").html("");
        $("#text_msg").val("");
        $("#clientId").val(Structure.tarClientId);   
        
        if (clickMsgCount > 0) {
      	
          $("#newMsg_" + clientId).html('0').hide();
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
            anImClass.getTopicHistory(Structure.tarClientId, 10, "", myInfo.clientId);
        } else{
            $("#isGroup").val(0);
          	anImClass.getHistory(Structure.tarClientId, myInfo.clientId, 10, "");
        }
        
    }
   }
}