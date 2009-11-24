Namespace ("Stack"); 

Stack.stackId = "stack";

//Updated
Stack.hideShow =  function (hideElemId, showElemId){
	Stack.setToStack(hideElemId);
	Stack.showInMainBody(showElemId);
	
	return false;
};

Stack.setToStack = function (elemId){
	if(!dwr.util.byId(elemId)){
		if(Main.isDebugMode){
			alert("Could not find element by Id:" + elemId);
		}
		return false;
	}
	
	dwr.util.byId(elemId).style.display = "none";
	dwr.util.byId(Stack.stackId).appendChild(dwr.util.byId(elemId));
	
	return false;
};

Stack.showInMainBody = function (elemId){
	if(!dwr.util.byId(Main.mainBodyId)){
		alert("Could not find HTML mainBody element");
		return false;
	}
	dwr.util.byId(Main.mainBodyId).appendChild(dwr.util.byId(elemId));
	if(dwr.util.byId(elemId).style.display == "none"){
		dwr.util.byId(elemId).style.display = "block";
	}
	
	return false;
};

Stack.clear = function(){
	// Clear stack element
	dwr.util.setValue(Stack.stackId, "");
};