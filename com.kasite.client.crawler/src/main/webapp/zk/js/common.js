window.S = {
	//阻止事件冒泡
	stopBubble:function(e){
	　　 e = e || window.event;  
        if(e.stopPropagation) { //W3C阻止冒泡方法  
        	e.preventDefault();
            e.stopPropagation();  
        } else {  
            e.cancelBubble = true; //IE阻止冒泡方法  
        }  
　　	}
}
String.prototype.Trim=function(){ 	return this.replace(/(^\s*)|(\s*$)/g, "");   }   // 去掉左右空格