Namespace("Globals");

Globals.bCheckMainLibs = function() {
	if ("undefined" == typeof dwr 
			|| "undefined" == typeof DWREngine
			|| "undefined" == typeof DWRUtil) {
		return false;
	}
	return true;
};

Globals.initializeAll = function() {
	// 1. STAGE #1 - Test main js libraries
	if (!Globals.bCheckMainLibs()) {
		alert("ERROR: Could not load main libraries!");
		return;
	}
	
	// 2. STAGE #2 - Load data from service (dictionary)
	alert("!!!OK!!!");
};
