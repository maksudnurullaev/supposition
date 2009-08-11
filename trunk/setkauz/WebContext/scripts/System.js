Namespace("System");

System.updateSessionTable = function() {
	System.getSessionAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.session.table", result, {
			escapeHtml :false
		});
	});
	return false;
};

System.updateDefaultsTable = function() {
	System.getDefaultsAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.defaults.table", result, {
			escapeHtml :false
		});
	});
	return false;
};

System.showNewDefaultForm = function() {
	Intro.getTextByKey("main.admin.system.defaults.formNew", function(result){
		dwr.util.setValue("main.admin.defaults.table", 
				result, 
				{escapeHtml :false});
	});
	return false;
}

System.addNewDefaultkeyValue = function(){
	alert("System.addDefaultkeyValue");
}
