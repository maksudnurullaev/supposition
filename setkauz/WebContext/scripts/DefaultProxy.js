Namespace("DefaultProxy");

DefaultProxy.updateSessionTable = function() {
	DefaultProxy.getSessionAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.session.table", result, {
			escapeHtml :false
		});
	});
	return false;
};

DefaultProxy.updateDefaultsTable = function() {
	DefaultProxy.getDefaultsAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.defaults.table", result, {
			escapeHtml :false
		});
	});
	return false;
};

DefaultProxy.showNewDefaultForm = function() {
	KeyValue = {key:null, value:null, id:null}
	dwr.util.getValues(KeyValue);
	
	DefaultProxy.getDefaultForm(KeyValue, function(result){
		dwr.util.setValue("main.admin.defaults.table", 
				result, 
				{escapeHtml :false});
	});
	return false;
}

DefaultProxy.showEditDefaultForm = function(ID) {
	KeyValue = {key:null, value:null, id:ID}
	dwr.util.getValues(KeyValue);
	
	alert(KeyValue.id)
	
	DefaultProxy.getDefaultForm(KeyValue, function(result){
		dwr.util.setValue("main.admin.defaults.table", 
				result, 
				{escapeHtml :false});
	});
	return false;
}

DefaultProxy.updateDefault = function(){
	KeyValue = {key:null, value:null, id:null};
	dwr.util.getValues(KeyValue);

	// alert(KeyValue.id);
	DefaultProxy.updateDBODefault(KeyValue, function(result) {
		alert(result);
		if (isOK(result)) {
			DefaultProxy.updateDefaultsTable();
		}

	});
	return false;
}

DefaultProxy.deleteDefault = function(id){
	Intro.getTextByKey("message.are.you.sure", function(result){
		if (confirm(result)) {
			DefaultProxy.deleteDBOKeyValue(id, function(result) {
				alert(result);
				if (isOK(result)) {
					DefaultProxy.updateDefaultsTable();
				}						
				
			});
		}
		
	});
	return false;
}

