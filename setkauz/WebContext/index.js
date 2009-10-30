Namespace("index");

index.initPage = function(){
	
	// This turns off the starting message
	document.getElementById("start").style.display = "none";
	// Load page context
	Main.loadPageContext();
	
	return false;
};