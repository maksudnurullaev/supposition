Namespace("UserProxy");

//Update table
UserProxy.updateTable = function() {
	if (Main.byId("UserProxy.currentPage")) {
		if (parseInt(dwr.util.getValue("UserProxy.currentPage"),10) > 0) {
			UserProxy.go2Page(parseInt(dwr.util.getValue("UserProxy.currentPage"),10));
		}
	} else {
		UserProxy.go2Page(1);
	}
	return false;
};

//Filter
UserProxy.showFilterForm = function() {
	Main.getTextFromServerToDiv("main.admin.users.filterForm",
			"main.admin.users.table", false);
	return false;
};

UserProxy.setFilter = function() {
	// Check fields
	if (!Main.isValidValue("mail")) {
		return false;
	}

	// Find & Show Items
	var User = {
		mail :null
	};
	dwr.util.getValues(User);
	UserProxy.setSessionFilterAndGetPageAsHTMLTable(User, function(result) {
		dwr.util.setValue('main.admin.users.table', result, {
			escapeHtml :false
		});
	});

	return false;
};

UserProxy.go2Page = function(inPage) {
	UserProxy.getPageAsHTMLTable(inPage, function(table) {
		dwr.util.setValue('main.admin.users.table', table, {
			escapeHtml :false
		});
	});
	return false;
};

// Setup page
UserProxy.setPageDensity = function() {
	if (parseInt(dwr.util.getValue("UserProxy.pageDensity"), 10) > 0) {
		UserProxy.setPageSize(parseInt(dwr.util
				.getValue("UserProxy.pageDensity"), 10), function() {
			UserProxy.go2Page(1);
		});
	}

	return false;
};



UserProxy.checkItemsByFilter = function() {
	// Check fields
	if (!Main.isValidValue("mail")) {
		return false;
	}

	// Find Items
	var User = {
		mail :null
	};
	dwr.util.getValues(User);
	UserProxy.findItemsByFilter(User, function(result) {
		alert(result);
	});

	return false;
};

UserProxy.RemoveFilter = function() {
	UserProxy.removeSessionFilterAndGetPageAsHTMLTable( function(result) {
		dwr.util.setValue('main.admin.users.table', result, {
			escapeHtml :false
		});
	});
};

// Edit & Update
UserProxy.editUser = function(uuid) {
	UserProxy.getFormUpdate(uuid, function(form) {
		dwr.util.setValue('main.admin.users.table', form, {
			escapeHtml :false
		});
	});
	return false;
};

UserProxy.updateUserData = function() {
	// Check fields
	if (!Main.isValidValue("mail")) {
		return false;
	}

	var User = {
		uuid :null,
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
	// Check fields
	if (!Main.isValidValue("newpassword") || !Main.isValidValue("newpassword2")) {
		return false;
	}

	var User = {
		uuid :null,
		newpassword :null,
		newpassword2 :null
	};

	dwr.util.getValues(User);
	UserProxy.updateDBOUserPassword(User, function(result) {
		alert(result);
		if (Main.isOK(result)) {
			var User = {
				newpassword :null,
				newpassword2 :null
			};
			dwr.util.setValues(User);
		}
	});
	return false;
};

UserProxy.addRole = function(ID) {
	var User = {
		uuid :null,
		roleuuid :ID
	};

	dwr.util.getValues(User);

	UserProxy.addDBORole(User, function(result) {
		alert(result);
		if (Main.isOK(result)) {
			UserProxy.updateRoles(User.uuid);
		}
	});

	return false;
};

UserProxy.updateRoles = function(userID) {
	UserProxy.getCurrentRolesAsHTML(userID, function(result) {
		dwr.util.setValue("current_roles", result, {
			escapeHtml :false
		});
	});

	UserProxy.getAvailableRolesAsHTML(userID, function(result) {
		dwr.util.setValue("available_roles", result, {
			escapeHtml :false
		});
	});
};

UserProxy.removeRole = function(ID) {
	var User = {
		uuid :null,
		roleuuid :ID
	};

	dwr.util.getValues(User);

	UserProxy.removeDBORole(User, function(result) {
		alert(result);
		if (Main.isOK(result)) {
			UserProxy.updateRoles(User.uuid);
		}
	});

	return false;
};