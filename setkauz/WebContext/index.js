Load ("dwr/engine.js");  
Load ("dwr/util.js");
Load ("dwr/interface/Session.js");
Load ("dwr/interface/LocalUserProxy.js");
Load ("tabs/tabs.js");
Load ("scripts/LocalUserProxy.js");

function gup(name, url) {
	if (!url) url = window.location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");	
	var results = new RegExp("[\\?&]"+name+"=([^&#]*)").exec(url);
	if( results == null ) return null;	
	else 
		return decodeURIComponent(results[1].replace(/\+/g," ")); 
}

function init() {
	if(gup("action")){
		alert("go to action = " + gup("action"));
	}
	// This turns off the no-javascript message
	document.getElementById("start").style.display = "none";
	
	// This checks for file: URLs and loading problems
	if (window.dwr == null || window.dwr.engine == null
			|| window.dwr.util == null) {
		document.getElementById("file").style.display = "block";
		return;
	}

	// Load page context
	loadPageContext()	
	
	// Setup footer
	Session.getHTMLTextFromFile("/footer.html",loadFooter);
	
	// Finish
	dwr.util.byId("error").style.display = "none";
	// We dont need this for a while yet
	//dwr.util.byId("footer").style.display = "block";
	
	
	return false;
};

function showMainRegisterForm(){
	hideAll();
	getTextFromServerToDiv("main.user.formNew", "mainBody2", false);
	dwr.util.byId("mainBody2").style.display = "block";
	
	return false;
};

function showMainEnterForm(){
	hideAll();
	getTextFromServerToDiv("main.user.formEnter", "mainBody2", false);
	dwr.util.byId("mainBody2").style.display = "block";
	
	return false;
};

function hideAll(){
	dwr.util.byId("mainTabList").style.display = "none";
	dwr.util.byId("mainBody2").style.display = "none";
	
	return false;
};

function showMainTabList(){
	hideAll();
	dwr.util.byId("mainTabList").style.display = "block";
	
	return false;
};

// ##################

// Stack functions

function Stack(){}

Stack.hideShow =  function (hideElemId, showElemId){
	Stack.setToStack(hideElemId);
	Stack.showInMainBody(showElemId);
	
	return false;
};

Stack.setToStack = function (elemId){
	dwr.util.byId(elemId).style.display == "none";
	dwr.util.byId("stack").appendChild(dwr.util.byId(elemId));
	
	return false;
};

Stack.showInMainBody = function (elemId){
	dwr.util.byId("mainBody").appendChild(dwr.util.byId(elemId));
	if(dwr.util.byId(elemId).style.display == "none")
		dwr.util.byId(elemId).style.display = "block";
	
	return false;
};

// #################
function loadPageContext() {
	Tabs.clearStack();	
	getTextFromServer('mainTitle', false);
	getTextFromServer('mainTabList', false);
	
	return false;
};

function getTextFromServer(key, nonFormat){
	Session.getTextByKey(key,function (data) {
		dwr.util.setValue(key, data, {escapeHtml :nonFormat});
		evaluateItByKey(key);
	});	
	
	return false;
};

function getTextFromServerToDiv(key, divId, nonFormat){
	Session.getTextByKey(key,function (data) {
		dwr.util.setValue(divId, data, {escapeHtml :nonFormat});
		evaluateItByKey(key);
	});	
	
	return false;
};

function evaluateItByKey(key){
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


function loadFooter(data) {
	dwr.util.setValue("footer", data, {
		escapeHtml :false
	});
	
	return false;
};

function isOK(result){
	if(result.substring(0,3) == "OK:") return true;
	
	return false;
};

function trim(s)
{
	var l=0; var r=s.length -1;
	while(l < s.length && s[l] == ' ')
	{	l++; }
	while(r > l && s[r] == ' ')
	{	r-=1;	}
	return s.substring(l, r+1);
}

function isValidValue(objName){
	if(dwr.util.byId(objName)){
		if(trim(dwr.util.getValue(objName)).length == 0){
			highlight(objName);
			return false;
		}
	}else{
		return false;
	}
	return true;
}

function highlight(objName){
	dwr.util.byId(objName).style.backgroundColor = "#FAF8CC";	
	
	return false;
};

// #### Kaptcha
function reloadKaptcha(){
	if(dwr.util.byId("kaptcha_img"))
		dwr.util.byId("kaptcha_img").src = 'kaptcha.jpg?' + Math.random()*100;
	
	return false;
};
