Namespace("CgroupProxy");

CgroupProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.cgroups.formNew","main.cgroups.table", false);
	return false;
};

CgroupProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("cgroup.name")){ return false; }
	
	Cgroup = {name:null, guuid:null};
	
	Cgroup.name = dwr.util.getValue("cgroup.name");
	Cgroup.guuid = dwr.util.getValue("cgroup.guuid");
	
	CgroupProxy.addDBONew(Cgroup, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CgroupProxy.updateTable();
		}			
	});
	
	return false;	
};

CgroupProxy.updateTable = function() {
	Main.getTextFromServerToDiv("text.constant.CGROUPS_AS_HTML_ADMIN","main.cgroups.table", false);	
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

CgroupProxy.remove = function(uuid) {
	if(!confirm("Are you sure!? Вы уверены!?")){ return false; }
	
	CgroupProxy.removeDBO(uuid, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CgroupProxy.updateTable();
		}			
	});
	
	return false;
};
