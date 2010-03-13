if (Globals == null){
	var Globals = {'version': '0.0.1'};
};

/* Values */
Globals.H2LogosID      = "logos";
Globals.DivContextID   = "content";
Globals.ULNavListID    = "navlist";
Globals.DivFooterID    = "footer";
Globals.DivInvisibleID = "stack"

/* Debugger */
Globals.DebugMode = true;

Globals.debug = function(message){
	if(Globals.DebugMode) alert(message);
};

/* Logos methods*/
Globals.LogosInnerHTML = "Unknown";

Globals.logosSet = function(message){
	$(Globals.H2LogosID).innerHTML = message;
};

Globals.logosReset = function(){
	$(Globals.H2LogosID).innerHTML = Globals.LogosInnerHTML;
};

/* JS Loader */
Globals.loadJS = function(pathToJS, onLoadFn){
	new Asset.javascript(pathToJS, {onload: onLoadFn});
};

/* Global initializator */
window.addEvent('domready', function() {
    //alert("The DOM is ready.");
    Globals.initializeAll();
});


Globals.initializeAll = function() {
	// Initialize vars
	Globals.LogosInnerHTML = $(Globals.H2LogosID).innerHTML;
	
	// Initialize load stages
	Globals.loadStages();
};

/* Define stages status */
Globals.isLoadedStage1 = function(){
	return Globals.isLoadedStage11() 
			&& Globals.isLoadedStage12()
			&& Globals.isLoadedStage13();
};

Globals.isLoadedStage11 = function(){
	return typeof DWREngine != "undefined";	
};

Globals.isLoadedStage12 = function(){
	return typeof DWRUtil != "undefined";
};

Globals.isLoadedStage13 = function(){
	return typeof MessageHandler != "undefined";
};

Globals.isLoadedStage14 = function(){
	return typeof MessageHandler.applyIncomingMessages != "undefined";
};

/* Loading Stages */
Globals.loadStages = function(){
	Globals.loadStage1();
};

Globals.loadStage1 = function() {
	if (!Globals.isLoadedStage1()) {
		Globals.logosSet("Loading DWR libraries...");
		Globals.loadStage11()
	}
};

Globals.loadStage11 = function() {
	if (!Globals.isLoadedStage11()) {
		Globals.loadJS("dwr/engine.js", Globals.loadStage12);
	}
};

Globals.loadStage12 = function() {
	if (!Globals.isLoadedStage12()) {
		Globals.loadJS("dwr/util.js", Globals.loadStage13);
	}
};

Globals.loadStage13 = function() {
	if (!Globals.isLoadedStage13()) {
		Globals.loadJS("dwr/interface/MessageHandler.js", Globals.loadStage14);
	}
};

Globals.loadStage14 = function() {
	if (!Globals.isLoadedStage14()) {
		Globals.loadJS("scripts/MessageHandler.js", Globals.loadStage2);
	}
};

Globals.loadStage2 = function(){
	// Check Stage #1
	if(!Globals.isLoadedStage1()){
		Globals.debug("ERROR: Could not load stage #1");
		return;
	}

	Globals.logosSet("Loading page...");
	
	alert("!!!Start STAGE 2!!!");
	
	Globals.logosReset();
};

Globals.testGetMessage = function(){
	var proxyMessage = {type:'html.context', data:null};
	MessageHandler.sendMessage(proxyMessage, MessageHandler.applyIncomingMessages);
};
