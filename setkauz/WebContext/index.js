Namespace("index");

Load ("dwr/engine.js");  
Load ("dwr/util.js");
Load ("tabs/tabs.js");
Load ("dwr/interface/Session.js");
Load ("dwr/interface/LocalUserProxy.js");
Load ("dwr/interface/AdsProxy.js");

//Load ("scripts/Gup.js");
Load ("scripts/Stack.js");
Load ("scripts/Main.js");
Load ("scripts/LocalUserProxy.js");
Load ("scripts/AdsProxy.js");

index.myErrorHandler = function (msg){
	alert("Error handler: " + msg);
	// Clear stack
	dwr.util.setValue("stack", "", {escapeHtml :false});
	// Re-load page
	Main.loadPageContext();
};

index.initPage = function(){
	if( "undefined" == typeof Namespace)      { alert("JS: Failed to Load [ Namespace ]");      return false; }	
	if( "undefined" == typeof dwr)            { alert("JS: Failed to Load [ dwr ]");            return false; }
	if( "undefined" == typeof dwr.engine)     { alert("JS: Failed to Load [ dwr.engine ]");     return false; }
	if( "undefined" == typeof dwr.util)       { alert("JS: Failed to Load [ dwr.util ]");       return false; }
	if( "undefined" == typeof Tabs)           { alert("JS: Failed to Load [ Tabs ]");           return false; }
	if( "undefined" == typeof LocalUserProxy) { alert("JS: Failed to Load [ LocalUserProxy ]"); return false; }
	if( "undefined" == typeof Main) 		  { alert("JS: Failed to Load [ Main ]");			return false; }
	if( "undefined" == typeof AdsProxy) 	  { alert("JS: Failed to Load [ AdsProxy ]");	    return false; }

	// #### DEBUG TIME ####
	dwr.engine.setErrorHandler(index.myErrorHandler);
	dwr.engine.setWarningHandler(index.myErrorHandler);
	dwr.engine.setTextHtmlHandler(index.myErrorHandler);

	// This turns off the starting message
	document.getElementById("start").style.display = "none";

	// Load page context
	Main.loadPageContext();
	
	return false;
};