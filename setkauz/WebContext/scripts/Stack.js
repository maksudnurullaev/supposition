Namespace ("Stack");  

Stack.hideShow =  function (hideElemId, showElemId){
	Stack.setToStack(hideElemId);
	Stack.showInMainBody(showElemId);
	
	return false;
};

Stack.setToStack = function (elemId){
	dwr.util.byId(elemId).style.display == "none";
	dwr.util.byId("stack").appendChild(dwr.util.byId(elemId));
	
	return false;
};

Stack.showInMainBody = function (elemId){
	dwr.util.byId("mainBody").appendChild(dwr.util.byId(elemId));
	if(dwr.util.byId(elemId).style.display == "none")
		dwr.util.byId(elemId).style.display = "block";
	
	return false;
};