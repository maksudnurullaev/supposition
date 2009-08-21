Namespace("UserProxy");
UserProxy.updateTable = function() {
	UserProxy.getPageAsHTMLTable(0, function(table) {
		dwr.util.setValue('main.admin.users.table', table, {
			escapeHtml :false
		});
	});
	return false;
};

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