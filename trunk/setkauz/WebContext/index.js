Load ("dwr/engine.js");  
Load ("dwr/util.js");
Load ("dwr/interface/Intro.js");
Load ("tabs/tabs.js");
Load ("dwr/interface/UserProxy.js");
Load ("scripts/user.js");

function init() {
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
	Intro.getHTMLTextFromFile("/footer.html",loadFooter);
	
	// Finish
	$("error").style.display = "none";
	// We dont need this for a while yet
	//$("footer").style.display = "block";
	
}

function showMainRegisterForm(){
	hideAll();
	getTextFromServerToDiv("main.user.formNew", "mainBody2", false);
	$("mainBody2").style.display = "block";
}

function showMainEnterForm(){
	hideAll();
	getTextFromServerToDiv("main.user.formEnter", "mainBody2", false);
	$("mainBody2").style.display = "block";
}

function hideAll(){
	$("mainTabList").style.display = "none";
	$("mainBody2").style.display = "none";
}

function showMainTabList(){
	hideAll();
	$("mainTabList").style.display = "block";
}

// ##################

// Stack functions

function Stack(){}

Stack.hideShow =  function (hideElemId, showElemId){
	Stack.setToStack(hideElemId);
	Stack.showInMainBody(showElemId);
}

Stack.setToStack = function (elemId){
	$(elemId).style.display == "none";
	$("stack").appendChild($(elemId));
}

Stack.showInMainBody = function (elemId){
	$("mainBody").appendChild($(elemId));
	if($(elemId).style.display == "none")
		$(elemId).style.display = "block";
}

// #################
function loadPageContext() {
	Tabs.clearStack();	
	getTextFromServer('mainTitle', false);
	getTextFromServer('mainTabList', false);
}

function getTextFromServer(key, nonFormat){
	Intro.getTextByKey(key,function (data) {
		dwr.util.setValue(key, data, {escapeHtml :nonFormat});
		evaluateItByKey(key);
	});	
}

function getTextFromServerToDiv(key, divId, nonFormat){
	Intro.getTextByKey(key,function (data) {
		dwr.util.setValue(divId, data, {escapeHtml :nonFormat});
		evaluateItByKey(key);
	});	
}

function evaluateItByKey(key){
	var keyID = key + ".eval";	
	Intro.hasMessageByKey(keyID, function(found){
		if(found){
				Intro.getTextByKey(keyID, function(data){
					eval(data);
				});			
		}
	});	
}


function loadFooter(data) {
	dwr.util.setValue("footer", data, {
		escapeHtml :false
	});
}

function isOK(result){
	if(result.substring(0,3) == "OK:") return true;
	return false;
}