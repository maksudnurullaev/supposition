Namespace("Main");

Main.mainBodyId    = "mainBody";
Main.loaderId      = "mainLoader";
Main.mainMenuId    = "mainMenu";
Main.isDebugMode   = true;

Main.hideLoader = function(){
	var loader = dwr.util.byId(Tabs.loaderId);
	if(loader){
		dwr.util.byId(Stack.stackId).appendChild(loader);
	}	
};

Main.showLoader = function(contextDivId){
	var loader = dwr.util.byId(Tabs.loaderId);
	if(loader){
		dwr.util.byId(contextDivId).appendChild(loader);
	}
};

//Updated
Main.hideAll = function() {
	dwr.util.byId("tabList").style.display = "none";
	dwr.util.byId("mainBody2").style.display = "none";

	return false;
};

Main.showMainTabList = function() {
	Main.hideAll();
	dwr.util.byId("tabList").style.display = "block";

	return false;
};

Main.loadInitialPageContext = function() {
	if(Main.isDebugMode){
		alert("WARNING: JS is debug mode!!!");
	}
	
	Stack.clear();
	Stack.setToStack(Main.loaderId);	
	Main.getTextFromServerToDiv('INITIAL_NAV_AND_CONTEXT','nav.and.context', false);
	return false;
};

Main.getTextFromServerToDiv = function(key, divId, nonFormat) {
	Session.getTextByKey2(key, function(result) {
		// Eval Part
		if(result.eval){
			eval(result.eval);
		}		
		// Text Part
		if(result.text){
			dwr.util.setValue(divId, result.text, {escapeHtml :nonFormat});
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

Main.isOK = function(result) {
	if (result.substring(0, 3) == "OK:") {
		return true;
	}

	return false;
};

Main.isERROR = function(result) {
	if (result.substring(0, 6) == "ERROR:") {
		return true;
	}

	return false;
};


Main.trim = function(s) {
	var l = 0;
	var r = s.length - 1;
	while (l < s.length && s[l] == ' ') {
		l++;
	}
	while (r > l && s[r] == ' ') {
		r -= 1;
	}
	return s.substring(l, r + 1);
};

Main.isValidValue = function(objName) {
	if (dwr.util.byId(objName)) {
		if (Main.trim(dwr.util.getValue(objName)).length === 0) {
			Main.highlight(objName);
			return false;
		}
	} else {
		return false;
	}
	return true;
};


Main.isValidNumber = function(objName){
	if (dwr.util.byId(objName)) {
		if (isNaN(dwr.util.getValue(objName))) {
			Main.highlight(objName);
			return false;
		}
	} else {
		return false;
	}
	return true;	
};

Main.highlight = function(objName) {
	dwr.util.byId(objName).style.backgroundColor = "#FAF8CC";

	return false;
};

// #### Kaptcha
Main.reloadKaptcha = function() {
	if (dwr.util.byId("kaptcha_img")) {
		dwr.util.byId("kaptcha_img").src = ("kaptcha.jpg?" + Math.random() * 100);
	}

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

Main.updateWeather = function(){
	Session.getWeatherAsHTML(dwr.util.byId("weather.cities").value, function(result){
		dwr.util.setValue("weather.context", result, {
			escapeHtml :false
		});		
	});
};