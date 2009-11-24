Namespace("Tabs");

var Tabs = {};

Tabs.onClik = function(elem) {
	return Tabs.onClickCommon(Main.mainMenuId, Main.mainBodyId , elem);
};

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
		childLiNodes[i].setAttribute("class", null);
	}	
};

Tabs.clearDivFromDiv = function(contextDivId){
	var childDivNodes = dwr.util.byId(contextDivId).getElementsByTagName('div');
	for (var i = 0; i < childDivNodes.length; i++){
		if(childDivNodes[i].id){
			dwr.util.byId("stack").appendChild(childDivNodes[i]);
		}
	}
	dwr.util.byId(contextDivId).innerHTML = "";
	return false;
};

Tabs.onClickCommon = function(rootUlId, contextDivId, selectedMenuElement){
	if(!Tabs.isValidMenu(selectedMenuElement)){ return false; }
	
	// Clear "selected" state for each element
	Tabs.clearSelection(rootUlId);	
	
	var divElementId = "div." + selectedMenuElement.id;
	
	// Move child elements to stack & clear context
	Tabs.clearDivFromDiv(contextDivId);
		
	// Try to get element from local stack...
	var stackedDiv = dwr.util.byId(divElementId);
	
	if(!stackedDiv){ // ... or from server
		Main.showLoader(contextDivId);
	
		Session.getTextByKey2(divElementId, function(result) {
			// Eval Part
			if(result.eval){ eval(result.eval);	}
			// Text Part
			if(result.text){
				Main.hideLoader();
				dwr.util.byId(contextDivId).innerHTML = result.text; 
			}						
		});
	}else {
		// Clear ALL
		dwr.util.byId(contextDivId).innerHTML = "";
		// Append Child
		dwr.util.byId(contextDivId).appendChild(stackedDiv);
	}
	
	// Set Selected
	selectedMenuElement.parentNode.setAttribute("class", "selected");
	
	return false;
};