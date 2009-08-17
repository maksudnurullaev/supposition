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

System.addNewDefaultKeyValue = function(){
	KeyValue = {key:null, value:null}

	dwr.util.getValues(KeyValue);
	
	System.addDBOKeyValue(KeyValue, function(result) {
		alert(result);
		if (isOK(result)) {
			System.updateDefaultsTable();
			KeyValue = {key:null, value:null}
			dwr.util.setValues(KeyValue);
		}

	});
	return false;
}

System.updateDefault = function(id){
	alert("Update " + id);
	return false;
}

System.deleteDefault = function(id){
	Intro.getTextByKey("message.are.you.sure", function(result){
		if (confirm(result)) {
			System.deleteDBOKeyValue(id, function(result) {
				alert(result);
				if (isOK(result)) {
					System.updateDefaultsTable();
				}						
				
			});
		}
		
	});
	return false;
	

	
	
	
}

