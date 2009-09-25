Namespace("RoleProxy");

RoleProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.admin.roles.formNew",'main.admin.roles.table', false);	
	return false;
};

RoleProxy.addNewRole = function(){
	// Check fields
	if(!Main.isValidValue("name")) { return false; }
	
	Role = {name:null};
	dwr.util.getValues(Role);
	
	RoleProxy.addDBORole(Role, function(result){
		alert(result);
		if (Main.isOK(result)) {
			RoleProxy.updateTable();
		}			
	});
	
	return false;	
};

RoleProxy.updateTable = function() {
	if(dwr.util.byId("RoleProxy.currentPage")){
		if(parseInt(dwr.util.getValue("RoleProxy.currentPage"),10) > 0){
			RoleProxy.go2Page(parseInt(dwr.util.getValue("RoleProxy.currentPage"),10));
		}
	}else{
		RoleProxy.go2Page(1);
	}
	
	return false;
};


RoleProxy.go2Page = function(inPage){
	RoleProxy.getPageAsHTMLTable(inPage, function(table) {
		dwr.util.setValue('main.admin.roles.table', table, {escapeHtml :false});
	});
	return false;	
};

RoleProxy.setPageDencity = function(){
	if(parseInt(dwr.util.getValue("RoleProxy.pageDencity"),10) > 0){
		RoleProxy.setPageSize(parseInt(dwr.util.getValue("RoleProxy.pageDencity"),10), function(){
			RoleProxy.go2Page(1);
		});
	}	
	
	return false;
};

RoleProxy.editRole = function(id) {
	RoleProxy.getFormUpdate(id, function(form) {
		dwr.util.setValue('main.admin.roles.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

RoleProxy.updateData = function() {
	// Check fields
	if(!Main.isValidValue("name")) { return false; }	
	
	var Role = {
		uuid :null,
		name :null
	};

	dwr.util.getValues(Role);

	RoleProxy.updateDBORole(Role, function(result) {
		alert(result);
	});
	
	return false;
};
