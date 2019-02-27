!function(n){
	var scriptsComponent = document.getElementsByTagName('script')//获所有script标签    , 
		scriptsComponent = scriptsComponent[scriptsComponent.length - 1];//获取当前加载到的script标签
	var ww = scriptsComponent.getAttribute('screen-width')||750;
    var  e=n.document,
         t=e.documentElement,
         s=window.screen,
         i=ww,
         d=i/100,
         o="orientationchange"in n?"orientationchange":"resize",
         a=function(){
//       	if(s.availWidth>=s.availHeight){
//       	}else{
         		var n=s.availWidth||320;n>i&&(n=i);
	             t.style.fontSize=n/d+"px";
	             t.setAttribute('class','rem-'+i);
//       	}
         };
         e.addEventListener&&(n.addEventListener(o,a,!1),a())
}(window);