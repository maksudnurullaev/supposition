Namespace("AdsProxy");

AdsProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.ads.formNew","main.ads.table", false);
	return false;
};

AdsProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("text")){ return false; }
	
	Ads = {text:null, 
			kaptcha:null,
			price:null,
			weeks2keep:null,
			cuuid:null,
			guuid:null,
			type:null};

	dwr.util.getValues(Ads);
	
//	alert("Ads.type = " + Ads.type);
//	alert("Ads.text = " + Ads.text);
//	alert("Ads.price = " + Ads.price);
//	alert("Ads.weeks2keep = " + Ads.weeks2keep);
//	alert("Ads.cuuid = " + Ads.cuuid);
//	alert("Ads.guuid = " + Ads.guuid);
	
	AdsProxy.addDBONew(Ads, function(result){
		alert(result);
		if (Main.isOK(result)) {
			AdsProxy.updateTable();
		}			
	});
	
	return false;	
};

AdsProxy.updateTable = function() {
//	Main.getTextFromServerToDiv("text.constant.CGROUPS_AS_HTML_ADMIN","main.cgroups.table", false);	
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
