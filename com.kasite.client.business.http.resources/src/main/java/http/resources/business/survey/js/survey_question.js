/// <summary>
/// 满意度调查--问题列表页JS
/// </summary>

var isSubmit = false;
var allQuesNum = 0;
var subjectId = 0;
var submitAnswer = '';
var replyType = 3;//回复设置 1 一个手机或电脑只能回复一次    2每个IP只能回复一次    3无限制
$(function () {
    //加载问卷试题
    QuerySubjectById(subjectId);
})

///// <summary>
///// 页面关闭后执行事件
///// </summary>
//$(window).bind('beforeunload', function () {
//    if (!isSubmit) {
//        return "您当前问卷还未完成，返回上个页面将中止调查，您确定要中止调查么？";
//    } 
//});

/// <summary>
/// 获取调查主题试卷
/// </summary>
function QuerySubjectById(subjectId) {
    //调查问卷ID
    if (subjectId==null || subjectId == "0") {
        alertNew('调查问卷ID为空!');
        return;
    }

	var apiData = {};
	apiData.SubjectId= subjectId;
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	param.apiParam =Commonjs.getApiReqParams(apiData);
    //查询调查问卷信息
	var url = '../../wsgw/survey/QuerySubjectById/callApi.do?t=' + new Date().getTime();
	var d = Commonjs.ajax(url,param,function(d){
    var listHtml = '';
    if (d) {
        if (d.RespCode == 10000) {
        	if(Commonjs.isEmpty(d.Data)){
        		myLayer.alert("没有找到问卷信息");
        		return;
        	}
        	var jsons = d.Data[0];
            if (jsons.Status == "0" || jsons.Status == "4" || jsons.Status == "5") {
                //0:无效，4:待审核，5:审核不通过
                $("#divNoSurvey").show();
                $("#divSurveyList").hide();
                $("#divNoSurvey").html('<i class="icon"></i>该问卷不存在，感谢您的参与！');
                $("#divSurveyList").hide();
                $("#divNoSurvey").show();
            } else if (jsons.Status == "1") {
                //1:有效(未发布)
                $("#divNoSurvey").show();
                $("#divSurveyList").hide();
                $("#divNoSurvey").html('<i class="icon"></i>该问卷已关闭，感谢您的参与！');
                $("#divSurveyList").hide();
                $("#divNoSurvey").show();
            } else if (jsons.Status == "3") {
                //3:已结束
                $("#divNoSurvey").show();
                $("#divSurveyList").hide();
                $("#divNoSurvey").html('<i class="icon"></i>该问卷已结束，感谢您的参与！');
                $("#divSurveyList").hide();
                $("#divNoSurvey").show();
            } else {
            	var IsOver = "0";	
                if (IsOver == "1") {
                    //已过期
                    $("#divNoSurvey").show();
                    $("#divSurveyList").hide();
                    $("#divNoSurvey").html('<i class="icon"></i>该问卷已结束，感谢您的参与！');
                    $("#divSurveyList").hide();
                    $("#divNoSurvey").show();
                } else {
                	replyType = jsons.Replytype;
                    if (Commonjs.ListIsNotNull(jsons.Result)) {
                        allQuesNum = jsons.Result.length == undefined? '1' :jsons.Result.length ;
                        Commonjs.BaseForeach(jsons.Result, function (i, item) {
                            listHtml += '<div class="satisfy_box" name="divQues' + (i + 1) + '">';

                            //未选择时跳转的锚链
                            switch (item.QuestType) {
                                case "1":
                                case "2":
                                case "3":
                                    listHtml += '<a name="questId' + item.QuestId + '" style="width:0px; height:0px; line-height:0px; font-size:0px;"></a>';
                                    break;
                                case "4":
                                case "5":
                                    Commonjs.BaseForeach(item.ChildrenMatrixQuestion, function (i, itemCM) {
                                        listHtml += '<a name="questId' + itemCM.QuestId + '" style="width:0px; height:0px; line-height:0px; font-size:0px;"></a>';
                                    });
                                    break;
                            }

                            listHtml += '<div class="saty_ques">';
                            //题号
                            listHtml += '<i class="cff8100">Q' + (i + 1) + '：</i>';
                            //题目内容
                            listHtml += item.Question;
                            if (item.Mustquest == "1") {
                                listHtml += '<i style="color:#FF0000;">(选填)</i>';
                            }

                            listHtml += '</div>';

                            //题型(1:单选,2:多选,3:填空,4:矩阵单选题,5:矩阵多选题)
                            switch (item.QuestType) {
                                case "1":
                                    //1:单选
                                    listHtml += '<div class="saty_answ ques1" quesNum="' + (i + 1) + '">';
                                    //存值隐藏域.题型,问题ID,当前题号,该题可跳过不回答(1:是;空值:否)
                                    listHtml += '<input type="hidden" name="hidQuestId" QuestType="' + item.QuestType + '" QuestId="' + item.QuestId + '" value="" quesNum="' + (i + 1) + '" Mustquest="' + item.Mustquest + '" Blank="" IfAddblank="" IfAllowNull=""/>';
                                    //循环生成选项
                                    Commonjs.BaseForeach(item.SvQuestionItems, function (iSv, itemSv) {
                                        var jumpQues = "";
                                        if (itemSv.JumpQuest != "") {
                                        	/*
                                            if (itemSv.JumpQuest == 11) {
                                                //不计入结果
                                                jumpQues = '<input type="hidden" name="hidEnd" value="1"></input>';
                                            } else if (itemSv.JumpQuest == 10) {
                                                //记入结果
                                                jumpQues = '<input type="hidden" name="hidIsToLast" value="1"></input>';
                                            }*/
                                            
                                            //当有跳转的题目时
                                        	Commonjs.BaseForeach(jsons.Result, function (i, itemResult) {
                                                if (itemSv.JumpQuest == itemResult.QuestId) {
                                                    jumpQues = '<i quesNum="' + (i + 1) + '">（跳转到第' + (i + 1) + '题）</i>';
                                                }
                                            });
                                        }
                                        //新增选项
                                        listHtml += '<div class="sradio" name="divSradio" value="' + itemSv.ItemId + '" ';
                                        //选项后是否增加填空框(1:添加;空值:不添加)
                                        listHtml += 'IfAddblank="' + itemSv.IfAddblank + '" ';
                                        //填空框是否必填(1:不必填;空值/0:必填)
                                        listHtml += 'IfAllowNull="' + itemSv.IfAllowNull + '">';
                                        //问题文字和跳转题目
                                        listHtml += '<label class="icon"></label><input type="radio" name="radio'+itemSv.QuestId+'"/><span>' + itemSv.ItemCont + '</span>' + jumpQues;
                                        //选项后是否增加填空框(1:添加;0或空:不添加)
                                        if (itemSv.IfAddblank == "1") {
                                            listHtml += '<input type="text" name="txtBlank" style="width:50%; height:25px; line-height:25px; border:1px solid #e1e1e1; margin-left:5px; padding:0 5px; ">';
                                            if (itemSv.IfAllowNull == "1") {//填空是否可以为空(1是;0或空否)
                                                listHtml += '<i style="color:#FF0000;" name="iXuan" >(选填)</i>';
                                            } else {
                                                listHtml += '<i style="color:#FF0000;" name="iXuan" >(必填)</i>';
                                            }
                                        }
                                        listHtml += '</div>';
                                    });
                                    listHtml += '</div>';
                                    break;
                                case "2":
                                    //2:多选
                                    listHtml += '<div class="saty_answ ques1">';
                                    //存值隐藏域.题型,问题ID,当前题号,该题可跳过不回答(1:是;空值:否),最小选择数,最大选择数
                                    listHtml += '<input type="hidden" name="hidQuestId" QuestType="' + item.QuestType + '" QuestId="' + item.QuestId + '" value="" quesNum="' + (i + 1) + '" Mustquest="' + item.Mustquest + '" Minoption="' + item.Minoption + '" Maxoption="' + item.Maxoption + '" />';
                                    Commonjs.BaseForeach(item.SvQuestionItems, function (iSv, itemSv) {
                                        //新增选项
                                        listHtml += '<div class="scheckbox" name="divScheckbox" value="' + itemSv.ItemId + '"><label class="icon"></label><input type="checkbox" name="radio'+item.QuestId+'" /><span>' + itemSv.ItemCont + '</span></div>';
                                    });
                                    listHtml += '</div>';
                                    break;
                                case "3":
                                    //3:填空
                                    listHtml += '<div class="saty_answ ques1">';
                                    //存值隐藏域.题型,问题ID,当前题号,该题可跳过不回答(1:是;空值:否)
                                    listHtml += '<input type="hidden" name="hidQuestId" QuestType="' + item.QuestType + '" QuestId="' + item.QuestId + '" value="" quesNum="' + (i + 1) + '" Mustquest="' + item.Mustquest + '" />';
                                    listHtml += '<textarea class="answtexa" name="txtContent"></textarea>';
                                    listHtml += '</div>';
                                    break;
                                case "4":
                                    //4:矩阵单选题
                                    Commonjs.BaseForeach(item.ChildrenMatrixQuestion, function (iCM, itemCM) {
                                        listHtml += '<div class="saty_answ ques1">';
                                        //存值隐藏域.题型,问题ID,当前题号,该题可跳过不回答(1:是;空值:否)
                                        listHtml += '<input type="hidden" name="hidQuestId" QuestType="' + item.QuestType + '" QuestId="' + itemCM.QuestId + '" value="" quesNum="' + (i + 1) + '" Mustquest="' + item.Mustquest + '" />';
                                        //矩阵的题目
                                        listHtml += '<div class="saty_tit">' + itemCM.Question + '</div>';
                                        Commonjs.BaseForeach(itemCM.MatrixQuestItems, function (iMQ, itemMQ) {
                                            //新增选项
                                            listHtml += '<div class="sradio" name="divSradio" value="' + itemMQ.ItemId + '"><label class="icon"></label><input type="radio" name="radio'+itemCM.QuestId+'" /><span>' + itemMQ.ItemCont + '</span></div>';
                                        });
                                        listHtml += '</div>';
                                    });
                                    break;
                                case "5":
                                    //5:矩阵多选题
                                    Commonjs.BaseForeach(item.ChildrenMatrixQuestion, function (iCM, itemCM) {
                                        listHtml += '<div class="saty_answ ques1">';
                                        //存值隐藏域.题型,问题ID,当前题号,该题可跳过不回答(1:是;空值:否)
                                        listHtml += '<input type="hidden" name="hidQuestId" QuestType="' + item.QuestType + '" QuestId="' + itemCM.QuestId + '" value="" quesNum="' + (i + 1) + '" Mustquest="' + item.Mustquest + '" Minoption="' + item.Minoption + '" Maxoption="' + item.Maxoption + '"/>';
                                        //矩阵的题目
                                        listHtml += '<div class="saty_tit">' + itemCM.Question + '</div>';
                                        Commonjs.BaseForeach(itemCM.MatrixQuestItems, function (iMQ, itemMQ) {
                                            //新增选项
                                            listHtml += '<div class="scheckbox" name="divScheckbox" value="' + itemMQ.ItemId + '"><label class="icon"></label><input type="checkbox" name="radio' + itemCM.QuestId + '" /><span>' + itemMQ.ItemCont + '</span></div>';
                                        });
                                        listHtml += '</div>';
                                    });
                                    break;
                            }

                            listHtml += '</div>';
                        });
                        //介绍
                        $("#divList").html(listHtml);
                        PageOneInstanceRodio();
                        PageOneInstance();
                        CheckBoxChick();
                        RadioChick();

                        $("#divSurveyList").show();
                        $("#divNoSurvey").hide();
                    } else {
                        $("#divSurveyList").hide();
                        $("#divNoSurvey").show();
                    }

                }
            }
        } else {
            //方法错误
			myLayer.alert(d.RespMessage);
        }
    } else {
        //通信失败 
    	myLayer.alert('网络繁忙,请刷新后重试');
    }

});
} 
function CommitAnswer() {
	myLayer.load('正在加载');
    var answers = "";       //答案字符串
    var quesNum = "";       //当前题号
    var alertString = "";   //提醒描述

    $("[name='hidQuestId']").each(function () {
        var questType = $(this).attr("QuestType");  //题型(1:单选,2:多选,3:填空,4:矩阵单选题,5:矩阵多选题)
        quesNum = $(this).attr("quesNum");

        if (!$("[name='divQues" + quesNum + "']").is(":hidden")) {
            //当前题目为显示时

            if ($(this).val() == "") {
                var mustquest = $(this).attr("Mustquest");  //该题可跳过不回答(1:是;空值:否)
                if (mustquest != "1") {
                    //当隐藏域值为空时
                    location.href = "#questId" + $(this).attr("QuestId"); //跳转到锚链
                    answers = "";
                    alertString = "请填写第" + quesNum + "题答案";
                    return false;
                }
            } else {
                //当隐藏域值不为空时

                if (questType == "1") {
                    //判断是否有文本框,以及文本框是否必填

                    //当有结束并不记录结果时,跳转到终结页
                    /*
                    if ($(this).attr("IsEnd") == "1") {
                        location.href = "survey_end.html?subjectId=" + subjectId;
                        answers = "结束并不记录结果";
                        return false;
                    }*/

                    var ifAddblank = $(this).attr("IfAddblank");    //选项后是否增加填空框(1:添加;空值:不添加)
                    var ifAllowNull = $(this).attr("IfAllowNull");  //填空框是否必填(1:必填;空值:不必填)
                    var blank = $(this).attr("Blank");              //填空框的值

                    if (ifAddblank != undefined && ifAddblank == "1" && ifAllowNull != "1") {
                        //填空框是否必填
                        if (blank == "") {
                            location.href = "#questId" + $(this).attr("QuestId"); //跳转到锚链
                            answers = "";
                            alertString = "第" + quesNum + "题选项后的说明为必填";
                            return false;
                        }
                    }

                } else if (questType == "2" || questType == "5") {
                    //多选时,比较最小和最大答题数
                    var minoption = $(this).attr("Minoption"); //最小答题数
                    var maxoption = $(this).attr("Maxoption"); //最大答题数

                    var ansNum = $(this).val().split(",").length;

                    //最小答题
                    if (minoption != undefined && minoption != "") {
                        if (ansNum < minoption) {

                            location.href = "#questId" + $(this).attr("QuestId"); //跳转到锚链
                            answers = "";
                            alertString = "第" + quesNum + "题最少要选" + minoption + "项";
                            return false;
                        }
                    }

                    //最大答题
                    if (maxoption != undefined && maxoption != "") {
                        if (ansNum > maxoption) {
                            location.href = "#questId" + $(this).attr("QuestId"); //跳转到锚链
                            answers = "";
                            alertString = "第" + quesNum + "题最多选" + maxoption + "项";
                            return false;
                        }
                    }
                }


                if (answers != "") {
                    answers += ",";
                }

                answers += '{';
                answers += '"QuestId":' + $(this).attr("QuestId");
                answers += ',';
                answers += '"Answer":"' + $(this).val() + '"';
                if (questType == "1") {
                    //填空框的值
                    answers += ',"Blank":"' + $(this).attr("Blank") + '"';
                }
                answers += '}';
            }
        }
    });
    if (answers == "") {
        var isAllXuanTian = true;   //是否全部都是选填题
        $("[name='hidQuestId']").each(function () {
            if (!$("[name='divQues" + quesNum + "']").is(":hidden")) {
                //当前题目为显示时
                var mustquest = $(this).attr("Mustquest");  //该题可跳过不回答(1:是;空值:否)
                if (mustquest != "1") {
                    isAllXuanTian = false;
                }
            }
        });
        if (isAllXuanTian) {
            isSubmit = true;
            location.href = Commonjs.goToUrl("survey_end.html?subjectId=" + subjectId+"&objtype="+objtype);
        } else {
        	myLayer.clear(); 
        	myLayer.alert(alertString);
        }
    } else {
        //调查问卷答案
        //answers = '{"Answers":[' + answers + ']}';
        //answers = escape(answers);
        //alert(answers);
//		var param = {};
//		var params = {};;//{"SubjectId":subjectId,"Answers":[answers]};
//		params.SubjectId = subjectId;
//		params.Answers = eval('([' + answers + '])');
//		param.Api = 'commitAnswer';
//		param.Params = Commonjs.jsonToString(params);
    	
    	submitAnswer = answers;
    	if(replyType!=null&&replyType!=undefined&&'1'==replyType){
    		//每个手机只能回复一次
    		myLayer.clear();
    		showMask();    		
    	}else{
    		ajaxCommitAnswers();
    	}

		
    }
}

/**
 * 每个手机只能回复一次的提交方式
 * */
function commitAnswersbyPhone(){
	var phone = $("#phoneOrPcText").val();
	if(phone==null||phone==undefined||phone.trim()==''){
		$("#warmId").show();
	}else if(!(/^1[34578]\d{9}$/.test(phone))){
		$("#warmId").show();
	}else{
		hideMask();
		ajaxCommitAnswers();
	}
}

function ajaxCommitAnswers(){
	var answers = submitAnswer;
	var apiData = {};
	apiData.HosId = Commonjs.getUrlParam("HosId");
	var param = {};
	apiData.SubjectId = subjectId;
	apiData.PhoneOrPc = $("#phoneOrPcText").val();
	apiData.Answers = eval('([' + answers + '])');
	param.api = ''; 
	param.apiParam =Commonjs.getApiReqParams(apiData);
	 //查询调查问卷信息
	Commonjs.ajax('../../wsgw/survey/CommitAnswer/callApi.do?t=' + new Date().getTime(),param,function(dd){
		if (dd) {
            if (dd.RespCode == 10000) {
                isSubmit = true;
                location.href = Commonjs.goToUrl("survey_end.html?subjectId=" + subjectId+"&objtype="+objtype);
            } else {
            	myLayer.alert(dd.RespMessage);
            }
        } else {
            //方法错误
        	myLayer.alert('网络繁忙,请刷新后重试');
        }
	})
   
}

/// <summary>
/// 文本输入题文本框值改变事件
/// </summary>
function PageOneInstance() {
    PageInputBind("[name='txtContent']", function () {
        var txtValue = $.trim($(this).val());
        if (txtValue.length >= 250) {
            alertNew('最多只能输入250个字!');
            txtValue = txtValue.substring(0, 250);
            $(this).val(txtValue);
        }
        //设置隐藏域的值
        if(txtValue){
        	//txtValue = txtValue.replace(/(["\\])/g, '\\$1');//转换json字符串中特殊符号
        	txtValue = txtValue.replace(/(["'\\])/g, '');
        }
        $(this).parent().find("[name='hidQuestId']").val(txtValue);
    });
}

/// <summary>
/// 单选题文本框值改变事件
/// </summary>
function PageOneInstanceRodio() {
    PageInputBind("[name='txtBlank']", function () {
        var txtValue = $.trim($(this).val());
        if (txtValue.length >= 250) {
            alertNew('最多只能输入250个字!');
            txtValue = txtValue.substring(0, 250);
            $(this).val(txtValue);
        }
        //设置隐藏域的值
        if(txtValue){
        	//txtValue = txtValue.replace(/(["\\])/g, '\\$1');//转换json字符串中特殊符号
        	txtValue = txtValue.replace(/(["'\\])/g, '');
        }
        $(this).parent().parent().find("[name='hidQuestId']").attr("Blank", txtValue);
    });
}

/// <summary>
/// 复选框选定事件
/// </summary>
function CheckBoxChick() {

    $("[name='divScheckbox']").click(function () {
        $(this).find("label").toggleClass("checked");
        var scheckboxs = $(this).parent().find("[name='divScheckbox']");
        var values = "";
        scheckboxs.each(function () {
            if ($(this).find(":checkbox").get(0).checked) {
                if (values != "") {
                    values += ",";
                }
                values += $(this).attr("value");
            }
        });

        //设置隐藏域的值
        $(this).parent().find("[name='hidQuestId']").val(values);
    });
}

/// <summary>
/// 单选框选定事件
/// </summary>
function RadioChick() {

    $("[name='divSradio']").click(function () {
        //当前题号
    	if($(this).find("input[type='radio']").is(":checked")){
    	}else {
    		return;
    	}
        var thisQNum = $(this).parent().attr("quesNum");
        $(this).siblings("[name='divSradio']").find("label").removeClass("checked");    //移除所有选项的选定样式
        $(this).find("label").addClass("checked");     
//        $(this).siblings("[name='divSradio']").find("input[type='radio']").removeAttr('checked');    //移除所有选项的选定样式
//        $(this).find("input[type='radio']").attr('checked','true');   


        //设置隐藏域的值
        $(this).parent().find("[name='hidQuestId']").val($(this).attr("value"));

        //显示剩下题目
        for (var i = parseInt(thisQNum) + 1; i <= allQuesNum; i++) {
            //隐藏当前题目到跳转题目之间的题目
            $("[name='divQues" + i + "']").show();
        }

        var ifAddblank = $(this).attr("IfAddblank");    //选项后是否增加填空框(1:添加;空值:不添加)
        var ifAllowNull = $(this).attr("IfAllowNull");  //填空框是否必填(0或空值:必填;1:不必填)
        var blank = $(this).attr("Blank");              //填空框的值

        $(this).parent().find("[name='txtBlank']").hide();  //隐藏该题所有文本框

        if ($(this).find("[name='hidEnd']") != null && $(this).find("[name='hidEnd']").val() == "1") {
            //结束不记入结果

            //设置隐藏域(结束不记入结果)
            $(this).parent().find("[name='hidQuestId']").attr("IsEnd", $(this).find("[name='hidEnd']").val());
            //隐藏剩下题目
            for (var i = parseInt(thisQNum) + 1; i <= allQuesNum; i++) {
                //隐藏当前题目到跳转题目之间的题目
                $("[name='divQues" + i + "']").hide();
            }

            if (ifAddblank == "1") {
                $(this).find("[name='txtBlank']").show();           //显示该选项文本框
                //$(this).find("[name='iXuan']").show();              //显示该选项文本框选填
                //设置隐藏域的值               
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", $(this).find("[name='txtBlank']").val());
            } else {
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", "");
            }

        } else if ($(this).find("[name='hidIsToLast']") != null && $(this).find("[name='hidIsToLast']").val() == "1") {
            //结束记入结果隐藏剩下题目

            //隐藏剩下题目
            for (var i = parseInt(thisQNum) + 1; i <= allQuesNum; i++) {
                //隐藏当前题目到跳转题目之间的题目
                $("[name='divQues" + i + "']").hide();
            }

            if (ifAddblank == "1") {
                $(this).find("[name='txtBlank']").show();           //显示该选项文本框
                //$(this).find("[name='iXuan']").show();              //显示该选项文本框选填
                //设置隐藏域的值               
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", $(this).find("[name='txtBlank']").val());
            } else {
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", "");
            }
        }
        else {
            //设置隐藏域
            $(this).parent().find("[name='hidQuestId']").attr("IsEnd", "0");
            $(this).parent().find("[name='iXuan']").hide();     //隐藏该题所有文本框选填

            if (ifAddblank == "1") {
                $(this).find("[name='txtBlank']").show();           //显示该选项文本框
                $(this).find("[name='iXuan']").show();              //显示该选项文本框选填
                //设置隐藏域的值               
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", $(this).find("[name='txtBlank']").val());
            } else {
                $(this).parent().find("[name='hidQuestId']").attr("IfAddblank", ifAddblank);
                $(this).parent().find("[name='hidQuestId']").attr("IfAllowNull", ifAllowNull);
                $(this).parent().find("[name='hidQuestId']").attr("Blank", "");
            }

            //获取该题跳转题目的集合
            var quesNumListStr = "";
            $(this).parent().find("i").each(function () {
                if (quesNumListStr != "") {
                    quesNumListStr += ",";
                }
                quesNumListStr += $(this).attr("quesNum");
            });
            var maxNum = 0;     //最大跳转题目
            var quesNumList = quesNumListStr.split(",");
            for (var i = 0; i < quesNumList.length; i++) {
                if (parseInt(quesNumList[i]) > maxNum) {
                    maxNum = parseInt(quesNumList[i]);
                }
            }

            if ($(this).find("i").length > 0) {
                //当前选项子级有i标签,则该选项为需要跳转选项

                //跳转的题号
                var toQNum = $(this).find("i").attr("quesNum");

                for (var i = parseInt(thisQNum) + 1; i < maxNum; i++) {
                    //显示当前题目到最大跳转题目之间的题目
                    $("[name='divQues" + i + "']").show();
                }

                for (var i = parseInt(thisQNum) + 1; i < toQNum; i++) {
                    //隐藏当前题目到跳转题目之间的题目
                    $("[name='divQues" + i + "']").hide();
                }

            } else {
                if ($(this).parent().find("i") != null) {
                    //当前选项子级有i标签,并且相邻的选项子级有i标签的,则该选项为需要跳转选项的兄弟选项

                    for (var i = parseInt(thisQNum) + 1; i < maxNum; i++) {
                        //显示当前题目到最大跳转题目之间的题目
                        $("[name='divQues" + i + "']").show();
                    }
                }
            }
        }
    });

}

//显示遮罩层    
function showMask(answers){  
    $("#phoneOrPc").show();   
    $("#warmId").hide();  
    $("#mask").css("height",$(document).height());     
    $("#mask").css("width",$(document).width());     
    $("#phoneOrPc").css("width",$(document).width());     
    $("#mask").show();       
    $("#phoneOrPcText").focus();  
    
}  
//隐藏遮罩层  
function hideMask(){     
      
	$("#phoneOrPc").hide();     
    $("#mask").hide();  
    $("#warmId").hide();     
}  

function getUrlParam(name) {
	subjectId = Commonjs.getUrlParam(name);
	}

/// <summary>
/// 文本框值改变事件
/// </summary>
function PageInputBind(id, funObj) {
    $(id).keyup(funObj);
    $(id).change(funObj);
    $(id).bind('focus', funObj);
    $(id).bind("input", funObj);
    $(id).bind("propertychange", funObj);
}


 