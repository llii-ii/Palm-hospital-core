/*
* jQuery pager plugin
* Version 1.0 (12/22/2008)
* @requires jQuery v1.2.6 or later
*
* Example at: http://jonpauldavies.github.com/JQuery/Pager/PagerDemo.html
*
* Copyright (c) 2008-2009 Jon Paul Davies
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
* 
* Read the related blog post and contact the author at http://www.j-dee.com/2008/12/22/jquery-pager-plugin/
*
* This version is far from perfect and doesn't manage it's own state, therefore contributions are more than welcome!
*
* Usage: .pager({ pagenumber: 1, pagecount: 15, buttonClickCallback: PagerClickTest });
*
* Where pagenumber is the visible page number
*       pagecount is the total number of pages to display
*       buttonClickCallback is the method to fire when a pager button is clicked.
*
* buttonClickCallback signiture is PagerClickTest = function(pageclickednumber) 
* Where pageclickednumber is the number of the page clicked in the control.
*
* The included Pager.CSS file is a dependancy but can obviously tweaked to your wishes
* Tested in IE6 IE7 Firefox & Safari. Any browser strangeness, please report.
*/
(function($) {

    $.fn.pager = function(options) {

        var opts = $.extend({}, $.fn.pager.defaults, options);

        return this.each(function() {

        // empty out the destination element and then render out the pager with the supplied options
        $(this).empty().append(renderpager(parseInt(options.totalcounts),parseInt(options.pagesize),parseInt(options.pagenumber), parseInt(options.pagecount), options.buttonClickCallback));
            
//             specify correct cursor activity
//            $('.pages li').mouseover(function() { document.body.style.cursor = "pointer"; }).mouseout(function() { document.body.style.cursor = "auto"; });
        });
    };    
   

    // render and return the pager with the supplied options
    function renderpager(totalcounts,pagesize,pagenumber, pagecount, buttonClickCallback) {       	
    	// setup $pager to hold render 
    	var $pager = $("<div class=\"fl\"></div>");
    	$pager.append("共 <b>"+totalcounts+"</b>条数据");
    	var $pageBox = $("<div class=\"fr\"></div>");
    	var $pageLine = $("<ul class=\"page-box\"></ul>");
        // add in the previous and next buttons
    	$pageLine.append(renderButton('上一页', totalcounts,pagesize,pagenumber, pagecount, buttonClickCallback));
        var pgIndex = "";
        if(pagecount > 0 && pagecount <= 8){
        	for(var i=1; i<=pagecount; i++){
        		if(pagenumber==i) pgIndex += "<li><a class=\"curr\" href=\"javascript:loadTb("+i+","+totalcounts+");\">"+i+"</a></li>";
        		else pgIndex += "<li><a href=\"javascript:loadTb("+i+","+totalcounts+");\">"+i+"</a></li>";
        	}
        }else if(pagecount > 8){
        	for(var i=1; i<=7; i++){
        		if(pagenumber==i) pgIndex += "<li><a class=\"curr\" href=\"javascript:loadTb("+i+","+totalcounts+");\">"+i+"</a></li>";
        		else pgIndex += "<li><a href=\"javascript:loadTb("+i+","+totalcounts+");\">"+i+"</a></li>";
        	}
        	if(pagenumber==pagecount) pgIndex += "<li><a href=\"javascript:;\">...</a></li><li><a class=\"curr\" href=\"javascript:loadTb("+pagecount+","+totalcounts+");\">"+pagecount+"</a></li>";
    		else pgIndex += "<li><a href=\"javascript:;\">...</a></li><li><a href=\"javascript:loadTb("+pagecount+","+totalcounts+");\">"+pagecount+"</a></li>";
        }else{
        	pgIndex = "";
        }
        $pageLine.append(pgIndex);
        // render in the next and last buttons before returning the whole rendered control back.
        $pageLine.append(renderButton('下一页', totalcounts,pagesize,pagenumber, pagecount, buttonClickCallback));
        $pageLine.append(renderGotoPagenum(pagenumber,totalcounts,pagesize,pagenumber, pagecount, buttonClickCallback));
        $pageBox.append($pageLine)
        return $pager.after($pageBox);        
    }

    // renders and returns a 'specialized' button, ie 'next', 'previous' etc. rather than a page number button
    function renderButton(buttonLabel, totalcounts,pagesize,pagenumber, pagecount, buttonClickCallback) {

        var $Button = $('<li><a href="javascript:;">' + buttonLabel + '</a></li>');

        var destPage = 1;

        // work out destination page for required button type
        switch (buttonLabel) {
            case "上一页":
                destPage = pagenumber - 1;
                break;
            case "下一页":
                destPage = pagenumber + 1;
                break;
        }

        // disable and 'grey' out buttons if not needed.
        if (buttonLabel == "上一页") {
            pagenumber <= 1 ? $Button.children("a").addClass('disable') : $Button.click(function() { buttonClickCallback(destPage); });
        }
        else {
            pagenumber >= pagecount ? $Button.children("a").addClass('disable') : $Button.click(function() { buttonClickCallback(destPage); });
        }

        return $Button;
    }
    
    //根据输入的input的值，转到某个页面
    function renderGotoPagenum(gopagenumber, totalcounts, pagesize, pagenumber, pagecount, buttonClickCallback) {    	
            	
    	var $Input = $("<li><div class=\"page-jump\"><input id=\"gopagenum\" type=\"text\" value=\""+pagenumber+"\" /><a href=\"javascript:;\">确定</a></div></li>");     	
        
    	$Input.change(function() { 
        	 var gonum=$('#gopagenum').val();
        	 var num_reg = /^[0-9]*$/; //必须为数字        	 
        	 
        	 if(gonum!="" & num_reg.test(gonum)){ 
        		 gopagenumber = gonum; 
        	 }else{
        		 $('#gopagenum').css({"color":"#555","font-size":"11px"});
        		 $('#gopagenum').val("输入数字");
        		 return false;
        	 }   	 
        	         	 
             if(gonum <= 1){ 
            	 gopagenumber = 1; 
             }else if(gonum >= pagecount){ 
            	 gopagenumber = pagecount; 
             }else{
            	 gopagenumber = gonum; 
             } 
             
        	buttonClickCallback(gopagenumber);         	
        }); 
        
    	$Input.focus(function() { $('#gopagenum').val(""); $('#gopagenum').css("color","#f03800"); });  
    	
        return $Input;
    }
    
    //初始化select的值:每页显示该值的数目列表内容
    function renderSelectCount(totalcounts, ispgEmpty) {  
    	
    	var $Select = $('<select id=\"selectcount\" class="'+ispgEmpty+'"></select>');     	
    	$Select.empty();
    	
    	//添加下拉框的option    	
    	var options = "";
    	var size = $("#defaultsize").val();
    	var pagesize=size;
    	var selectval = $("#pagesize").val();    	
    	for(var i=2; i<=8; i++) {  
    		
    		if(selectval==pagesize){ 
    			options += "<option value="+pagesize+" selected>"+pagesize+"</option>";
    		}else{
    		    options += "<option value="+pagesize+">"+pagesize+"</option>";
    		}  
    		
    		if( totalcounts < pagesize ){ //如果列表总数小于下拉列表值，其他的option则不显示
    			pagesize=totalcounts; 
    			break; 
    		}else{
    			pagesize=size*(i+1); //这里控制下拉列表的值的大小
    		}
    	}    	
    	$Select.append(options); 
    	
    	//改变下拉列表的值
    	$Select.change(function() {     		
		   selectval = $Select.find("option:selected").val();
		   $("#pagesize").val(selectval);
		   
		   //控制跳转的页是否存在
		   var pagenum = $('#gopagenum').val();
		   if( totalcounts%selectval == 0 ){
			   pagenum = totalcounts/selectval;
		   }else{
			   pagenum = parseInt(totalcounts/selectval)+1;
		   }		   
		   if( $('#pagenumber').val() > pagenum ){
			   $('#pagenumber').val(pagenum);	
		   }		  	   
		   
		   init();
	    });
       
        return $Select;
    } 

    // pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version
    $.fn.pager.defaults = {
    	totalcounts:1,
    	pagesize:1,
        pagenumber: 1,
        pagecount: 1
    };

})(jQuery);