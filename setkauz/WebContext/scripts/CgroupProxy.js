Namespace("CgroupProxy");

CgroupProxy.showNewForm = function() {
	if( "undefined" == typeof Main.getTextFromServerToDiv) { alert("JS: Failed to Load [ Main.getTextFromServerToDiv ]"); return false; }
	Main.getTextFromServerToDiv("main.cgroups.formNew","main.cgroups.table", false);	
	return false;
};

CgroupProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("name")){ return false; }
	
	Cgroup = {name:null, guuid:null};
	dwr.util.getValues(Cgroup);
	
	CgroupProxy.addDBONew(Cgroup, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CgroupProxy.updateTable();
		}			
	});
	
	return false;	
};

CgroupProxy.updateTable = function() {
	if( "undefined" == typeof Main.getTextFromServerToDiv) { alert("JS: Failed to Load [ Main.getTextFromServerToDiv ]"); return false; }
	Main.getTextFromServerToDiv("text.constant.CGROUPS_AS_HTML","main.cgroups.table", false);	
	return false;
};

CgroupProxy.edit = function(id) {
	RoleProxy.getFormUpdate(id, function(form) {
		dwr.util.setValue('main.admin.roles.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

CgroupProxy.update = function() {
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
