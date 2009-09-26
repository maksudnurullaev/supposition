Namespace("AdsProxy");

AdsProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.ads.formNew","main.ads.table", false);
	return false;
};

AdsProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("name")){ return false; }
	
	Cgroup = {name:null, guuid:null};
	dwr.util.getValues(Cgroup);
	
	AdsProxy.addDBONew(Cgroup, function(result){
		alert(result);
		if (Main.isOK(result)) {
			AdsProxy.updateTable();
		}			
	});
	
	return false;	
};

AdsProxy.updateTable = function() {
	Main.getTextFromServerToDiv("text.constant.CGROUPS_AS_HTML_ADMIN","main.cgroups.table", false);	
	return false;
};

AdsProxy.edit = function(id) {
	RoleProxy.getFormUpdate(id, function(form) {
		dwr.util.setValue('main.admin.roles.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

AdsProxy.remove = function(uuid) {
	alert("Go to move cgroup by uuid = " + uuid);
		
	AdsProxy.removeDBO(uuid, function(result){
		alert(result);
		if (Main.isOK(result)) {
			AdsProxy.updateTable();
		}			
	});
	
	return false;
};
