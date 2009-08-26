Namespace("UserProxy");
// Update table
UserProxy.updateTable = function() {
	if($("UserProxy.currentPage")){
		if(parseInt(dwr.util.getValue("UserProxy.currentPage")) > 0){
			UserProxy.go2Page(parseInt(dwr.util.getValue("UserProxy.currentPage")));
		}
	}else
		UserProxy.go2Page(1);
};

UserProxy.go2Page = function(inPage){
	UserProxy.getPageAsHTMLTable(inPage, function(table) {
		dwr.util.setValue('main.admin.users.table', table, {
			escapeHtml :false
		});
	});
	return false;	
}

// Setup page
UserProxy.setPageDencity = function(){
	if(parseInt(dwr.util.getValue("UserProxy.pageDencity")) > 0){
		UserProxy.setPageSize(parseInt(dwr.util.getValue("UserProxy.pageDencity")), function(){
			UserProxy.go2Page(1);
		});
	}	
	return false;
}

// Filter
UserProxy.showFilterForm = function(){
	getTextFromServerToDiv("main.admin.users.filterForm", "main.admin.users.table", false);	
	return false;	
}

UserProxy.checkFilter = function(){
	var str4Filter = trim(dwr.util.getValue("mail"));
	if(str4Filter.length == 0)alert(';-)');
	else{
		UserProxy.findItemsByFilter(str4Filter, function(result){
			alert(result);
		});
	}
	return false;
}


// Edit & Update 
UserProxy.editUser = function(id) {
	UserProxy.getFormUpdate(id, function(form) {
		dwr.util.setValue('main.admin.users.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

UserProxy.updateUserData = function() {
	var User = {
		id :null,
		mail :null,
		additionals :null
	};
	dwr.util.getValues(User);
	UserProxy.updateDBOUser(User, function(result) {
		alert(result);
	});
	return false;
};

UserProxy.updateUserPassword = function() {
	var User = {
		id :null,
		password :null,
		newpassword :null,
		newpassword2 :null
	};
	dwr.util.getValues(User);
	UserProxy.updateDBOUserPassword(User, function(result) {
		alert(result);
		var User = {
			password :null,
			newpassword :null,
			newpassword2 :null
		};
		dwr.util.setValues(User);
	});
	return false;
};