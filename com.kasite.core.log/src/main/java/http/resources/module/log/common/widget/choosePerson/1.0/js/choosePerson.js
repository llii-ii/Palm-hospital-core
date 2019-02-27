/**
 * choosePerson --选择管理人--
 * @param parmOrgData 组织POST data
 * @param artDialogTitle 弹出窗标题
 * @param initDataId 初始选中人员id
 * @param initDataName 初始选中人员name
 * @param initDataParent 初始选中人员是否是有子级的
 * @param ztreeJsonUrl 组织json ajax 地址
 * @param searchJsonUrl 查询json ajax 地址
 * @param selectNumber 最多选择几个 默认15个
 * @param selectPrompt 提示语 默认提示“最多只能选择”
 * @param async 是否异步加载ZTREE
 * @param async 是否异步加载ZTREE
 * @param ztreeFun 三个函数 {Check:function(){},AsyncSuccess:function(){},Click:function(){}}
 * @param callBack 回调函数
 * @param searchShow 是否需要搜索功能
 * @param selectAllBtn 是否需要"全选"功能
 * @param colseImgSrc 关闭图片的src
 * @param searchPlaceholder  搜索提示语
 * @param selectTitle  被选中的提示语
 * @param rootNodeSelect  根节点是否显示 “选择”
 * @param searchDataKey 搜索datakey {ORGNAME: 'ORGNAME',CNAME: 'CNAME',USERID: 'USERID'}
 */
function choosePerson(options){
    choosePerson.opts = $.extend(true, {}, choosePerson.defaults, options);
    choosePerson.init();
}
//----------choosePerson 默认参数
choosePerson.defaults = {
    parmOrgData:{},
    artDialogTitle:'\u8bf7\u9009\u62e9',
    initDataId:null,
    initDataName:null,
    initDataParent:null,
    ztreeJsonUrl:'',
    searchJsonUrl:'',
    selectNumber:15,
    selectTips: false,
    selectPrompt:'\u6700\u591a\u53ea\u80fd\u9009\u62e9\uff1a',
    async:false,
    ztreeFun:{
        Check:null,
        AsyncSuccess:null,
        Click:null
    },
    callBack:null,
    searchShow:true,
    selectAllBtn:true,
    colseImgSrc:'../widget/zTree/3.5.18/images/del.png',
    searchPlaceholder: '\u641c\u7d22\u90e8\u95e8\u4eba\u540d',
    selectTitle: '\u9009\u62e9\u4eba\u5458\u4fe1\u606f',
    rootNodeSelect:false,
    searchDataKey:{
        ORGNAME: 'ORGNAME',
        CNAME: 'CNAME',
        USERID: 'USERID'
    }
};
//----------入口 带参数
choosePerson.init = function(){
    choosePerson.initHtmlWrap();
    choosePerson.artDialog.init();
    choosePerson.ztreeFun = choosePerson.opts.ztreeFun;
    choosePerson.ztree(choosePerson.ztreeSetting());
    choosePerson.search();
    if(choosePerson.opts.initDataId!=null && choosePerson.opts.initDataName!=null && choosePerson.opts.initDataParent!=null){
        choosePerson.createInitPerson(choosePerson.opts.initDataId , choosePerson.opts.initDataName , choosePerson.opts.initDataParent);
    }
};
//----------ztree checkType 类型
choosePerson.ztreeSetting = function(){
    var callbackFun= {
        onCheck: function (event, treeId, treeNode){
            if(choosePerson.ztreeFun.Check){
                eval("choosePerson.ztreeFun.Check(event,treeId, treeNode)");
            }else{
                eval("choosePerson.ztreeOnCheck(event,treeId, treeNode)");
            }
        },
        onAsyncSuccess:function (event, treeId, treeNode, msg){
            $('#ztreeLoadingTips').hide();
            if(choosePerson.ztreeFun.AsyncSuccess){
                eval("choosePerson.ztreeFun.AsyncSuccess(event,treeId, treeNode, msg)");
            }else{
                eval("choosePerson.ztreeOnAsyncSuccess(event,treeId, treeNode, msg)");
            }
        },
        beforeClick: function (treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("choosePersonTree");
            if (treeNode.isParent) {
                if(zTree.expandNode(treeNode)){
                    $("#selectBtn"+treeNode.tId).css({"display":"inline-block"});
                }else{
                    $("#selectBtn"+treeNode.tId).css({"display":"none"});
                }
            } else {
                choosePerson.createOnePerson(treeNode);
            }
            if(!choosePerson.initClick){
                choosePerson.initClick = true;
                if(choosePerson.ztreeFun.Click ){
                    eval("choosePerson.ztreeFun.Click(treeId, treeNode)");
                }else{
                    eval("choosePerson.ztreeOnClick(treeId, treeNode)");
                }
            }
            return false;
        },
        beforeCollapse : function(treeId, treeNode){    //箭头收起
            var zTree = $.fn.zTree.getZTreeObj("choosePersonTree");
            if (treeNode.isParent) {
                if(!zTree.expandNode(treeNode)){
                    $("#selectBtn"+treeNode.tId).css({"display":"none"});
                }
            }
            return false;
        },
        beforeExpand: function(treeId, treeNode){       //箭头展开
            var zTree = $.fn.zTree.getZTreeObj("choosePersonTree");
            if (treeNode.isParent) {
                if(zTree.expandNode(treeNode)){
                    $("#selectBtn"+treeNode.tId).css({"display":"inline-block"});
                }
            }
            return false;
        }
    };
    if(choosePerson.opts.async){
        var _ztreeSettingVal = {
            async: {
                enable: true,
                url:choosePerson.opts.ztreeJsonUrl,
                autoParam: ["id"],
                otherParam:choosePerson.opts.parmOrgData,
                dataFilter: choosePerson.ztreeFilter,
                type: "post"
            },
            view: {
                selectedMulti: false,
                showIcon: choosePerson.showIconForTree,
                showTitle: false,
                dblClickExpand: false,
                showLine: false,
                addDiyDom: choosePerson.addDiyDom
            },
            callback:callbackFun
        };
    }else{
        var _ztreeSettingVal = {
            view: {
                selectedMulti: false,
                showIcon: choosePerson.showIconForTree,
                showTitle: false,
                dblClickExpand: false,
                showLine: false,
                addDiyDom: choosePerson.addDiyDom
            },
            callback:callbackFun
        };
    }
    return _ztreeSettingVal;
};
//----------ztree 是否显示节点的图标
choosePerson.showIconForTree = function(treeId, treeNode){
    return treeNode.isParent;
};

choosePerson.addDiyDom = function(treeId, treeNode){
    if (treeNode.isParent && choosePerson.opts.selectAllBtn) {
        if(!choosePerson.opts.rootNodeSelect && treeNode.level==0){
            return false;
        }
        var aObj = $("#" + treeNode.tId + "_a");
        var _ahtml = '<a href="javascript:;" id="selectBtn'+ treeNode.tId +'">\u9009\u62e9</a>';
        aObj.append(_ahtml);
        var selectBtn = $("#selectBtn"+treeNode.tId);
        selectBtn.css({
            "display"  :   "none",
            "color"     :   "#4fc1e9",
            "line-height" :   "2.25",
            "margin-left"   :   "15px"
        }).on('click',function(event){
            event.stopPropagation();
            event.preventDefault();
            choosePerson.clickDomBtn(treeNode);
        });
        if(treeNode.open){
            selectBtn.css({"display":"inline-block"});
        }
        return false;
    }
};
//----------单击自定义按钮方法
choosePerson.clickDomBtn = function(treeNode){
    choosePerson.createOnePerson(treeNode);
};
//----------ztree checkbok readio 被选中后的回调
choosePerson.ztreeOnCheck = function(event,treeId, treeNode){

};
//----------ztree 异步请求数据时的回调
choosePerson.ztreeOnAsyncSuccess = function(event,treeId, treeNode, msg){

};
//----------ztree 某节点单击后的回调
choosePerson.ztreeOnClick = function(treeId, treeNode){

};
//----------ztree 异步请求
choosePerson.ztreeFilter = function (treeId, parentNode, responseData) {
    return responseData;
};
//----------ztree 初始化
choosePerson.ztree = function(setting){
    if(choosePerson.opts.async){
        $.fn.zTree.init($("#choosePersonTree"), setting);
    }else{
        choosePerson.ztreeNodes(setting);
    }
};
//----------ztree 一次性读取JSON
choosePerson.ztreeNodes=function(setting){
    $.ajax({
        dataType: "json",
        type: "POST",
        data:choosePerson.opts.parmOrgData,
        cache:false,
        url: choosePerson.opts.ztreeJsonUrl,
        success: function(data){
            $.fn.zTree.init($("#choosePersonTree"), setting,data);
            $('#ztreeLoadingTips').hide();
        }
    });
};
//----------初化选中人员
choosePerson.createInitPerson = function(id,name,parent){
    if(!(id && id!='')) {
        $('#choosePerson-selecteBox').html('');
        return;
    }
    var _listHtml = '',
        _id = id.split(","),
        _name = name.split(","),
        _parent = parent.split(",");

    for(var s=0; s<_id.length; s++){
        _listHtml += choosePerson.createPerson(_id[s],_name[s],_parent[s]);
    }

    $('#choosePerson-selecteBox').html(_listHtml);
};
//----------判断选中人员是否符合条件
choosePerson.judgeSelectPerson = function(id){
    if($('#choosePerson-selecteBox>li').length>=choosePerson.opts.selectNumber){    //判断是否超出
        choosePerson.orerunDialog();
        return false;
    }else{  //判断是否已经存在
        var b = true;
        for(var s=0;s<$('#choosePerson-selecteBox>li').length;s++){
            if(id==$('#choosePerson-selecteBox>li').eq(s).data('id')){
                b=false;
            }
        }
        return b;
    }
};
//----------选择单个单位
choosePerson.createOnePerson = function(treeNode){
    if(choosePerson.judgeSelectPerson(treeNode.id)){
        var _listHtml = choosePerson.createPerson(treeNode.id,treeNode.name,treeNode.isParent);
        $('#choosePerson-selecteBox').append(_listHtml);
    }
};
//----------生成人员Html
choosePerson.createPerson = function(id,name,parent){
    var parentIcon = '';
    if((typeof (parent)=='boolean' &&  parent ) || (typeof (parent)=='string' &&  parent === 'true')){
        parentIcon = '<i class="mr5" style="width: 16px; height: 16px; display: inline-block; background: url(../widget/zTree/3.5.18/images/diy/file.png) no-repeat center; position: relative; top: 2px;"></i>';
    }

    var _listHtml = '<li class="clearfix c-line-h2" data-parent="'+parent+'" data-name="'+name+'" data-id="'+id+'">'+
                        '<div class="fl">'+parentIcon+' '+name+'</div> '+
                        '<div class="fr"><a href="javascript:;" onclick="choosePerson.delPerson(this)" class="alinks alinks-black"><img src="'+choosePerson.opts.colseImgSrc+'" /></a> </div>'+
                    '</li>';
    return _listHtml;
};
//----------删除选择人员
choosePerson.delPerson=function(dom){
    $(dom).parents('li').remove();
};
//----------选中的人员超出提示
choosePerson.orerunDialog = function(){
    art.dialog({
        lock: true,
        artIcon:'error',
        opacity:0.4,
        width: 250,
        title:'报错',
        content: choosePerson.opts.selectPrompt + choosePerson.opts.selectNumber,
        ok: function () {}
    });
};
//----------返回选中的人员
choosePerson.makeAllPerson = function(){
    var personArray =   {
            id : '',
            name : '',
            parent : ''
        },
        $li=$('#choosePerson-selecteBox>li');
    for(var i=0; i<$li.length;i++){
        personArray.id+=$li.eq(i).data('id')+',';
        personArray.name+=$li.eq(i).data('name')+',';
        personArray.parent+=$li.eq(i).data('parent')+',';
    }
    personArray.id=personArray.id.substring(0,personArray.id.length-1);
    personArray.name=personArray.name.substring(0,personArray.name.length-1);
    personArray.parent=personArray.parent.substring(0,personArray.parent.length-1);
    return personArray;
};
//----------搜索
choosePerson.search = function(){
    //输入框架焦点 focus 、 blur
    $('#choosePerson-dialog').on('focus','.list-search-input',function(){
        $(this).parent().find('.list-search-clear').show();
        if(!$(this).parent().next('.search-overlay').is(':visible')) {
            $(this).parent().next('.search-overlay').show();
        }
    }).on('blur','.list-search-input',function(){
        var $this=$(this);
        if($this.val()=='' && $('#choosePerson-searchTbody>tr').length<=0){
            $this.parent().find('.list-search-clear').hide();
            $(this).parent().next('.search-overlay').hide();
        }
    }).on('keyup','.list-search-input',function(e){
        var ev = document.all ? window.event : e;
        if(ev.keyCode==13) {
            searchAjax($('#choosePerson-dialog').find('input.list-search-input'));
        }
    });
    //搜索button
    $('#choosePerson-dialog').on('click','.list-search-icon',function(){
        searchAjax($('#choosePerson-dialog').find('input.list-search-input'));
    });
    //搜索AJAX
    function searchAjax(dom){
        var _val= $.trim(dom.val());
        if(_val=='') return false;
        choosePerson.opts.parmOrgData.userName=_val;
        $.ajax({
            dataType: "json",
            type: "POST",
            data:choosePerson.opts.parmOrgData,
            cache:false,
            url: choosePerson.opts.searchJsonUrl,
            success: function(data){
                var Html='';
                for(var i=0; i<data.Result.length; i++){
                    var _USERID = eval('data.Result[i].'+choosePerson.opts.searchDataKey.USERID),
                        _CNAME = eval('data.Result[i].'+choosePerson.opts.searchDataKey.CNAME),
                        _ORGNAME = eval('data.Result[i].'+choosePerson.opts.searchDataKey.ORGNAME);
                    Html+='<tr data-parent="false" data-name="'+_CNAME+'" data-id="'+_USERID+'">' +
                            '<td class="c-t-center"><span class="c-nowrap">'+_CNAME+'</span></td>' +
                            '<td><span class="c-nowrap">'+_ORGNAME+'</span></td>' +
                        '</tr>';
                }
                $('#choosePerson-searchTbody').html(Html);
            }
        });
    }
    //选中人员
    $('#choosePerson-searchTbody').on('click','tr',function(){
        var _id =  $(this).data('id'),
            _name = $(this).data('name'),
            _parent = $(this).data('parent'),
            _listHtml = '';
        if(choosePerson.judgeSelectPerson(_id)){
            _listHtml = choosePerson.createPerson(_id,_name,_parent);
            $('#choosePerson-selecteBox').append(_listHtml);
        }
    });
    //清空搜索内容
    $('#choosePerson-dialog').on('click','.list-search-clear',function(){
        $(this).parent().find('input.list-search-input').val('').blur();
        $(this).hide().parent().next('.search-overlay').hide();
        $('#choosePerson-searchTbody>tr').off();
        $('#choosePerson-searchTbody').html('');
    });
};
//----------弹出框内容HTML
choosePerson.htmlWrap = '' +
'<div class="c-main clearfix c-hide" id="choosePerson-dialog">'+
    '<div class="fl w350 h350 c-border c-hidden">'+
        '<div class="c-position-r c-border-b" id="searchShow">'+
            '<div class="c-666 ptb10 plr15 c-line-h1">'+
                '<input type="text" id="searchPlaceholder" placeholder="" class="width-90 c-f14 list-search-input">'+
                '<i class="iconfont c-position-a c-position-rt10 mr30 c-cursor-p c-hide list-search-clear" title="清空">&#xe635;</i>'+
                '<i class="iconfont c-position-a c-position-rt10 c-cursor-p list-search-icon" title="搜索">&#xe62c;</i>'+
            '</div>'+
            '<div class="bgc-fff c-position-a width-100 c-border-t c-hide search-overlay" style="height: 360px; overflow: auto">'+
                '<table class="tb width-100 c-t-center c-position-r" data-toggle="tbHover" style="top: -1px">'+
                    '<thead>'+
                        '<tr>'+
                            '<th>姓名</th>'+
                            '<th>组织名称</th>'+
                        '</tr>'+
                    '</thead>'+
                    '<tbody id="choosePerson-searchTbody"></tbody>'+
                '</table>'+
            '</div>'+
        '</div>'+
        '<div class="bgc-fdfdfd" id="ztreeWrap" style="height: 360px; overflow: auto">'+
            '<div class="pt100 c-t-center c-909090 c-f14" id="ztreeLoadingTips">数据正在加载中。。。</div>'+
            '<div class="p10">'+
                '<ul id="choosePersonTree" class="ztree ztree-slack ztree-solid-arrow"></ul>'+
            '</div>'+
        '</div>'+
    '</div>'+
    '<div class="fr w350 h350 c-border c-hidden">'+
        '<div class="plr15 ptb10 c-f14 c-666 c-border-b" id="selectTitle"></div>'+
        '<div class="bgc-fdfdfd" style="height:360px; overflow:auto;">'+
            '<div class="p10">'+
                '<ul class="c-666" id="choosePerson-selecteBox"></ul>'+
            '</div>'+
        '</div>'+
    '</div>'+
'</div>';
//----------构造弹出框
choosePerson.artDialog = {
    init : function (){
        this.obj = art.dialog({
            artIcon:'add',
            lock: true,
            opacity:0.4,
            width: 780,
            padding: '15px 25px',
            title:choosePerson.opts.artDialogTitle,
            content: $('#choosePerson-dialog').get(0),
            ok: function(){
                if(choosePerson.opts.callBack){
                    var _data=choosePerson.makeAllPerson();
                    choosePerson.opts.callBack(_data.id,_data.name,_data.parent);
                }
                choosePerson.dialogClose();
            },
            cancel:function(){
                choosePerson.dialogClose();
            }
        });
    },
    obj : null
};
//----------弹出框关闭回调
choosePerson.dialogClose = function(){
    choosePerson.removeHtmlWrap();
};
//----------构造弹出框内容
choosePerson.initHtmlWrap = function () {
    choosePerson.removeHtmlWrap();
    $('body').append(this.htmlWrap);
    $('#selectTitle').html(choosePerson.opts.selectTitle);
    $('#searchPlaceholder').attr('placeholder',choosePerson.opts.searchPlaceholder);
    if(!choosePerson.opts.searchShow){
        $('#searchShow').hide();
        $('#ztreeWrap').height('400px');
    }
};
//----------删除弹出框内容
choosePerson.removeHtmlWrap = function () {
    if($('#choosePerson-dialog').length){
        $('#choosePerson-dialog').remove();
    }
};