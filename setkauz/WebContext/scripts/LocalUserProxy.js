Namespace("LocalUserProxy");

LocalUserProxy.addNewUser = function() {
	var User = {
		mail :null,
		additionals :null,
		newpassword :null,
		newpassword2 :null
	};
	dwr.util.getValues(User);

	UserProxy.addUser(User, function(result) {
		alert(result);
		if (isOK(result)) {
			showMainTabList();
			var User = {
				mail :null,
				additionals :null,
				newpassword :null,
				newpassword2 :null
			};
			dwr.util.setValues(User);
		}

	});
	return false;
};

LocalUserProxy.enterUser = function() {
	var User = {
		mail :null,
		password :null
	};
	dwr.util.getValues(User);

	UserProxy.enterUser(User, function(result) {
		alert(result);
		if (isOK(result)) {
			showMainTabList();
			var User = {
				mail :null,
				password :null
			};
			dwr.util.setValues(User);
		}

	});
	return false;
};