Load ("dwr/engine.js");  
Load ("dwr/util.js");
Load ("tabs/tabs.js");
Load ("dwr/interface/Session.js");
Load ("dwr/interface/LocalUserProxy.js");

Load ("scripts/Gup.js");
Load ("scripts/Stack.js");
Load ("scripts/Main.js");
Load ("scripts/LocalUserProxy.js");

initPage = function(){
	// Test [ Main.testLoadedJS ]
	if( "undefined" == typeof Main)              { alert( "JS: Failed to Load [ Main ]"); return; }
	if( "undefined" == typeof Main.testLoadedJS) { alert( "JS: Failed to Load [ Main.testLoadedJS ]"); return; }
	
	// Test Loaded js
	if(!Main.testLoadedJS()){
		dwr.util.byId("fail2LoadJS").style.display = "block";
		return;		
	}
	
	// Test gup redirection
	//if(Main.gup("action")){
	//	alert("go to action = " + Session.gup("action"));
	//}
	
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