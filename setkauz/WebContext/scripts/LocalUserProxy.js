Namespace("LocalUserProxy");

LocalUserProxy.addNewUser = function() {
	// Check fields
	if( !isValidValue("mail") ||
		!isValidValue("newpassword") ||
		!isValidValue("newpassword2") ||
		!isValidValue("kaptcha"))return false;

	var User = {
		mail :null,
		additionals :null,
		newpassword :null,
		newpassword2 :null,
		kaptcha : null
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
	// Check fields
	if( !isValidValue("mail") ||
		!isValidValue("password") ||
		!isValidValue("kaptcha"))return false;

	var User = {
		mail :null,
		password :null,
		kaptcha : null		
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