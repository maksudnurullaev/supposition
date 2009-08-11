Namespace("db.proxy.users");
db.proxy.users.updateTable = function() {
	Users.getPageAsHTMLTable(0, function(table) {
		dwr.util.setValue('main.admin.users.table', table, {
			escapeHtml :false
		});
	});
	return false;
};

db.proxy.users.editUser = function(id) {
	Users.getFormUpdate(id, function(form) {
		dwr.util.setValue('main.admin.users.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

db.proxy.users.updateUserData = function() {
	var User = {
		id :null,
		mail :null,
		additionals :null
	};
	dwr.util.getValues(User);
	Users.updateUserData(User, function(result) {
		alert(result);
	});
	return false;
};

db.proxy.users.updateUserPassword = function() {
	var User = {
		id :null,
		password :null,
		newpassword :null,
		newpassword2 :null
	};
	dwr.util.getValues(User);
	Users.updateUserPassword(User, function(result) {
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