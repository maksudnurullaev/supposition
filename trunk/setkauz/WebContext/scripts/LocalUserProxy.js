Namespace("LocalUserProxy");

LocalUserProxy.addNewUser = function() {
	// Check fields
	if(!isValidValue("mail")) return false;		
	if(!isValidValue("newpassword"))return false;	
	if(!isValidValue("newpassword2"))return false;	0

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
	// Check fields
	if(!isValidValue("mail")) return false;		
	if(!isValidValue("password"))return false;	

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