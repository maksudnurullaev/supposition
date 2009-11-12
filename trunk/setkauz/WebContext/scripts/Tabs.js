Namespace("Tabs");

Tabs.mainContext = "context";
Tabs.mainMenu = "menu";
Tabs.stackId = "stack";

Tabs.onClik = function(elem) {
	return Tabs.onClickCommon(Tabs.mainMenu, Tabs.mainContext, elem);
};

//######################

Tabs.isValidMenu = function(selectedMenuElement){
	// Check ID
	if(!selectedMenuElement.id){
		alert("ERROR: Navigator does not have neccessary ID property!");
		return false;
	}	
	
	// Check "selected" state
	if(selectedMenuElement.parentNode.id == "selected"){ return false; }
	
	return true;
};

Tabs.clearSelection = function(rootUlId){
	var childLiNodes = dwr.util.byId(rootUlId).getElementsByTagName('li');
	for (var i = 0; i < childLiNodes.length; i++){
		childLiNodes[i].id = null;
	}	
};

Tabs.clearDivFromDiv = function(contextDivId){
	var childDivNodes = dwr.util.byId(contextDivId).getElementsByTagName('div');
	for (var i = 0; i < childDivNodes.length; i++){
		if(childDivNodes[i].id){
			dwr.util.byId("stack").appendChild(childDivNodes[i]);
		}
	}
	
	return false;
};

Tabs.onClickCommon = function(rootUlId, contextDivId, selectedMenuElement){
	if(!Tabs.isValidMenu(selectedMenuElement)){ return false; }
	
	// Clear "selected" state for each element
	Tabs.clearSelection(rootUlId);	
	
	// Move child elements to stack & clear context
	Tabs.clearDivFromDiv(contextDivId);
	
	// Generate Div ID
	var resultDivId = selectedMenuElement.id + ".context";
	
	// Try to get element from local stack...
	var stackedDiv = dwr.util.byId(resultDivId);
	
	if(!stackedDiv){ // ... or from server
		Session.getTextByKey2(resultDivId, function(result) {
			// Eval Part
			if(result.eval){ eval(result.eval);	}
			// Text Part
			if(result.text){ dwr.util.byId(contextDivId).innerHTML = result.text; }						
		});
	}else {
		// Clear ALL
		dwr.util.byId(contextDivId).innerHTML = "";
		// Append Child
		dwr.util.byId(contextDivId).appendChild(stackedDiv);
	}
	
	// Set Selected
	selectedMenuElement.parentNode.id = "selected";
	
	return false;
};