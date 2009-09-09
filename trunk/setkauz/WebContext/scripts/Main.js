Namespace("Main");

Main.testLoadedJS = function(){  
	   if( "undefined" == typeof dwr)            { alert( "JS: Failed to Load [ dwr ]");            return false; }
	   if( "undefined" == typeof dwr.engine)     { alert( "JS: Failed to Load [ dwr.engine ]");     return false; }
	   if( "undefined" == typeof dwr.util)       { alert( "JS: Failed to Load [ dwr.util ]");       return false; }
	   if( "undefined" == typeof Tabs)           { alert( "JS: Failed to Load [ Tabs ]");           return false; }
	   if( "undefined" == typeof Session)        { alert( "JS: Failed to Load [ Session ]");        return false; }
	   if( "undefined" == typeof LocalUserProxy) { alert( "JS: Failed to Load [ LocalUserProxy ]"); return false; }
	   return true;
}; 




Main.hideAll = function(){
	dwr.util.byId("mainTabList").style.display = "none";
	dwr.util.byId("mainBody2").style.display = "none";
	
	return false;
};

Main.showMainTabList = function(){
	Main.hideAll();
	dwr.util.byId("mainTabList").style.display = "block";
	
	return false;
};

Main.loadPageContext = function(){
	Tabs.clearStack();	
	Main.getTextFromServer('mainTitle', false);
	Main.getTextFromServer('mainTabList', false);
	Main.showMainTabList();	
	
	return false;
};

Main.getTextFromServer = function(key, nonFormat){
	Session.getTextByKey(key,function (data) {
		dwr.util.setValue(key, data, {escapeHtml :nonFormat});
		Main.evaluateItByKey(key);
	});	
	
	return false;
};

Main.getTextFromServerToDiv = function(key, divId, nonFormat){
	Session.getTextByKey(key,function (data) {
		dwr.util.setValue(divId, data, {escapeHtml :nonFormat});
		Main.evaluateItByKey(key);
	});	
	
	return false;
};

Main.evaluateItByKey = function(key){
	var keyID = key + ".eval";	
	Session.hasMessageByKey(keyID, function(found){
		if(found){
				Session.getTextByKey(keyID, function(data){
					eval(data);
				});			
		}
	});	
	
	return false;
};

Main.loadFooter = function(data) {
	dwr.util.setValue("footer", data, {
		escapeHtml :false
	});
	
	return false;
};

Main.isOK = function(result){
	if(result.substring(0,3) == "OK:") return true;
	
	return false;
};

Main.trim = function(s)
{
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
};

Main.isValidValue = function(objName){
	if(dwr.util.byId(objName)){
		if(Main.trim(dwr.util.getValue(objName)).length == 0){
			Main.highlight(objName);
			return false;
		}
	}else{
		return false;
	}
	return true;
}

Main.highlight = function(objName){
	dwr.util.byId(objName).style.backgroundColor = "#FAF8CC";	
	
	return false;
};

// #### Kaptcha
Main.reloadKaptcha = function(){
	if(dwr.util.byId("kaptcha_img"))
		dwr.util.byId("kaptcha_img").src = 'kaptcha.jpg?' + Math.random()*100;
	
	return false;
};

Main.updateSessionTable = function() {
	Session.getSessionAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.session.table", result, {
			escapeHtml :false
		});
	});
	return false;
};

