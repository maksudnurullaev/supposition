Namespace("LocalUserProxy");

LocalUserProxy.addNewUser = function() {
	var User = {
		mail :null,
		additionals :null,
		newpassword :null,
		newpassword2 :null
	};
	
	dwr.util.getValues(User);

	LocalUserProxy.addDBOUser(User, function(result) {
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
	
	LocalUserProxy.enterDBOUser(User, function(result) {
		alert(result);
		if (isOK(result)) {
			showMainTabList();
			var User = {
					mail :null,
					massword :null
				};
			dwr.util.setValues(User);
		}
	});
	
	return false;
};