Load ("dwr/engine.js");  
Load ("dwr/util.js");
Load ("tabs/tabs.js");
Load ("dwr/interface/Session.js");
Load ("dwr/interface/LocalUserProxy.js");

Load ("scripts/Gup.js");
Load ("scripts/Stack.js");
Load ("scripts/Main.js");
Load ("scripts/LocalUserProxy.js");

// ### DEBUG TIME ###
function myErrorHandler(msg){
	alert(msg);
};

initPage = function(){
	// Test [ Main.testLoadedJS ]
	if( "undefined" == typeof Main)              { alert( "JS: Failed to Load [ Main ]"); return; }
	if( "undefined" == typeof Main.testLoadedJS) { alert( "JS: Failed to Load [ Main.testLoadedJS ]"); return; }
	
	// Test Loaded js
	if(!Main.testLoadedJS()){
		dwr.util.byId("fail2LoadJS").style.display = "block";
		return;		
	}
	
	// #### DEBUG TIME ####
	dwr.engine.setErrorHandler(myErrorHandler);
	dwr.engine.setWarningHandler(myErrorHandler);

	// Handle Session Expire Event 
	dwr.engine.setTextHtmlHandler(function(){
		alert("sessionExpiredHandler event handled");
		LocalUserProxy.showMainEnterForm();		
	});
	
	
	// This turns off the no-javascript message
	document.getElementById("start").style.display = "none";

	// Load page context
	Main.loadPageContext();
	
	// Setup footer
	Session.getHTMLTextFromFile("/footer.html",Main.loadFooter);
	
	// Finish
	dwr.util.byId("error").style.display = "none";
	// We dont need this for a while yet
	//dwr.util.byId("footer").style.display = "block";
	
	
	return false;
};