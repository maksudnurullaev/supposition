Namespace("CompanyProxy");

CompanyProxy.mainDiv = "main.company.table";

CompanyProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.company.formNew",CompanyProxy.mainDiv, false);
	return false;
};

CompanyProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("company.name")){ return false; }
	
	Company = {name:null, additional:null, www:null, city:null};

	Company.name = dwr.util.getValue("company.name");
	Company.additional = dwr.util.getValue("company. additional");
	Company.www = dwr.util.getValue("company.www");
	Company.city = dwr.util.getValue("company.city");
	
	alert("ACompany.name = " + Company.name);
	alert("Company.additional = " + Company.additional);
	alert("Company.www = " + Company.www);
	alert("Company.city = " + Company.city);
	
//	CompanyProxy.addDBONew(Ads, function(result){
//		alert(result);
//		if (Main.isOK(result)) {
//			CompanyProxy.updateTable();
//		}			
//	});
	
	return false;	
};

CompanyProxy.updateTable = function() {
	if (Main.byId("CompanyProxy.currentPage")) {
		if (parseInt(dwr.util.getValue("CompanyProxy.currentPage"),10) > 0) {
			CompanyProxy.go2Page(parseInt(dwr.util.getValue("CompanyProxy.currentPage"),10));
		}
	} else {
		CompanyProxy.go2Page(1);
	}
	return false;
};

CompanyProxy.go2Page = function(inPage) {
	CompanyProxy.getPageAsHTMLTable(dwr.util.getValue("guuid"), inPage, function(table) {
		dwr.util.setValue(CompanyProxy.mainDiv, table, {
			escapeHtml :false
		});
	});
	return false;
};

//Set page size
CompanyProxy.setPageDencity = function() {
	if (parseInt(dwr.util.getValue("CompanyProxy.pageDencity"), 10) > 0) {
		CompanyProxy.setPageSize(parseInt(dwr.util
				.getValue("CompanyProxy.pageDencity"), 10), function() {
			CompanyProxy.go2Page(1);
		});
	}
	return false;
};

CompanyProxy.remove = function(uuid) {
	CompanyProxy.removeDBO(uuid, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CompanyProxy.updateTable();
		}			
	});
	
	return false;
};
