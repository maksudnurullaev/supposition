Namespace("LocalUserProxy");
//Updated
LocalUserProxy.updateUserData = function(){
	// Check fields
	if( !Main.isValidValue("mail")) { return false; }
	
	var User = {
			uuid :null,
			mail :null,
			additionals :null
		};	
	
	dwr.util.getValues(User);

	LocalUserProxy.updateDBOUser(User, function(result) {
		alert(result);
	});

	return false;	
};

LocalUserProxy.updateUserPassword = function(){
	// Check fields
	if( !Main.isValidValue("newpassword") ||
		!Main.isValidValue("newpassword2")) { return false; }
	
	var User = {
			uuid :null,
			newpassword :null,
			newpassword2 :null
		};	
	
	dwr.util.getValues(User);

	LocalUserProxy.updateDBOUserPassword(User, function(result) {
		alert(result);
		if(Main.isOK(result)){
			var User = {
				newpassword :null,
				newpassword2 :null
			};
			dwr.util.setValues(User);
		}
	});
	return false;
};

LocalUserProxy.addNewUser = function() {	
	// Check fields
	if( !Main.isValidValue("mail") ||
		!Main.isValidValue("newpassword") ||
		!Main.isValidValue("newpassword2") ||
		!Main.isValidValue("kaptcha")) { return false; }

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
		if (Main.isOK(result)) {
			Main.loadInitialPageContext();
		}

	});

	return false;
};

LocalUserProxy.enterUser = function() {
	// Check fields
	if( !Main.isValidValue("mail") ||
		!Main.isValidValue("password") ||
		!Main.isValidValue("kaptcha")) { return false; }

	var User = {
		mail :null,
		password :null,
		kaptcha : null		
	};

	dwr.util.getValues(User);
	
	LocalUserProxy.enterDBOUser(User, function(result) {
		alert(result);
		if (Main.isOK(result)) {
			Main.loadInitialPageContext();
		}
	});
	
	return false;
};

LocalUserProxy.userLogoff = function(){	
	Session.logOff(function(result){
		alert(result);
		if(Main.isOK(result)) { 
			Main.loadInitialPageContext();
		}
	});
	
	return false;
};

LocalUserProxy.getCabinetContext = function(){
	// Get form
	LocalUserProxy.getUserCabinet(dwr.util.byId('cabinet.select.actions').value, function(result){
		// Eval Part
		if(result.eval){
			eval(result.eval);
		}
		dwr.util.byId('personal.cabinet.div').innerHTML = result.text;
	});	
};