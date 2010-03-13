if (GlobalsNav == null){
	var GlobalsNav = {'version': '0.0.1'};
};

GlobalsNav.SelectedMenuID = "selected";

GlobalsNav.test = function(){
	$each(GlobalsNav.getList(), function(elem, elemIndex){
		alert(elem.innerHTML + "\n\nIndex: " + elemIndex);
	});
};

GlobalsNav.getList = function(){
	return $$(Globals.ULNavListID,"li");
};

GlobalsNav.onClik = function(elem) {
	return GlobalsNav.onClickCommon(Globals.ULNavListID, Globals.DivContextID, elem);
};

GlobalsNav.showAgreement = function() {
	return GlobalsNav.onClickCommon(Globals.ULNavListID, Globals.DivContextID, dwr.util.byId("agreement"));
};

GlobalsNav.isNonActiveMenu = function(selectedMenuElement){
	// Check ID
	if(!selectedMenuElement.id){
		alert("ERROR: Navigator does not have neccessary ID property!\n\n" + selectedMenuElement.innerHTML);
		return false;
	}	
	
	// Check "selected" state
	if(selectedMenuElement.parentNode.id == GlobalsNav.SelectedMenuID){ return false; }
	
	return true;
};

GlobalsNav.clearSelection = function(rootUlId){
	$each(GlobalsNav.getList(), function(elem, elemIndex){
		elem.id = null;
	});	
};

GlobalsNav.clearDivFromDiv = function(contextDivId){
	var childDivNodes = $$(contextDivId,'div');
	
	$each(childDivNodes.length, function(elem, elemIndex){
		if(elem.id){
			$(Globals.DivInvisibleID).appendChild(childDivNodes[i]);
		}
	});
	
	$(contextDivId).innerHTML = "";
	
	return false;
};

GlobalsNav.onClickCommon = function(rootUlId, contextDivId, selectedMenuElement){
	if(!GlobalsNav.isNonActiveMenu(selectedMenuElement)){ return false; }
	
	// Clear "selected" state for each element
	GlobalsNav.clearSelection(rootUlId);	
	
	// Move child elements to stack & clear context
	GlobalsNav.clearDivFromDiv(contextDivId);
	
	// Generate Div ID
	var resultDivId = selectedMenuElement.id + ".context";
	
	// Try to get element from local stack...
//	if(!$(resultDivId)){ // ... or from server
//		Session.getTextByKey2(resultDivId, function(result) {
//			// Eval Part
//			if(result.eval){ eval(result.eval);	}
//			// Text Part
//			if(result.text){
//				GlobalsNav.hideLoader();
//				dwr.util.byId(contextDivId).innerHTML = result.text; 
//			}						
//		});
		$(contextDivId).innerHTML = selectedMenuElement.id;
//	}else {
//		// Clear ALL
//		dwr.util.byId(contextDivId).innerHTML = "";
//		// Append Child
//		dwr.util.byId(contextDivId).appendChild($(resultDivId));
//	}
	
	// Set Selected
	selectedMenuElement.parentNode.id = GlobalsNav.SelectedMenuID;
	
	return false;
};