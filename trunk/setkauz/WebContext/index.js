Namespace("index");

//Load ("dwr/engine.js");  
//Load ("dwr/util.js");
//Load ("dwr/interface/Session.js");
//Load ("dwr/interface/LocalUserProxy.js");
//Load ("dwr/interface/AdsProxy.js");
//
////Load ("scripts/Gup.js");
//Load ("tabs/tabs.js");
//Load ("scripts/Stack.js");
//Load ("scripts/Main.js");
//Load ("scripts/LocalUserProxy.js");
//Load ("scripts/AdsProxy.js");

index.initPage = function(){
//	if( "undefined" == typeof Namespace)      { alert("JS: Failed to Load [ Namespace ]");      return false; }	
//	if( "undefined" == typeof dwr)            { alert("JS: Failed to Load [ dwr ]");            return false; }
//	if( "undefined" == typeof Tabs)           { alert("JS: Failed to Load [ Tabs ]");           return false; }
//	if( "undefined" == typeof LocalUserProxy) { alert("JS: Failed to Load [ LocalUserProxy ]"); return false; }
//	if( "undefined" == typeof Main) 		  { alert("JS: Failed to Load [ Main ]");			return false; }
//	if( "undefined" == typeof AdsProxy) 	  { alert("JS: Failed to Load [ AdsProxy ]");	    return false; }

	// This turns off the starting message
	document.getElementById("start").style.display = "none";

	// Load page context
	Main.loadPageContext();
	
	return false;
};