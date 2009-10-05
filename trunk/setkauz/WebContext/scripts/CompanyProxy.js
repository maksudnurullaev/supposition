Namespace("CompanyProxy");

CompanyProxy.mainDiv = "main.company.table";

CompanyProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.company.formNew",CompanyProxy.mainDiv, false);
	return false;
};

CompanyProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("company.name")){ return false; }
	
	Company = {name:null, additionals:null, www:null, guuid:null, city:null};

	Company.name = dwr.util.getValue("company.name");
	Company.additionals = dwr.util.getValue("company.additionals");
	Company.www = dwr.util.getValue("company.www");
	Company.city = dwr.util.getValue("company.city");
	Company.guuid = dwr.util.getValue("company.guuid");

//	alert("ACompany.name = " + Company.name);
//	alert("Company.additional = " + Company.additionals);
//	alert("Company.www = " + Company.www);
//	alert("Company.city = " + Company.city);
//	alert("Company.guuid = " + Company.guuid);
	
	CompanyProxy.addDBONew(Company, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CompanyProxy.updateTable();
		}			
	});
	
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
	CFilter = {city:null, guuid:null, owner:null};
	
	CFilter.city = dwr.util.getValue("company.city");
	CFilter.guuid = dwr.util.getValue("company.guuid");
	CFilter.owner = dwr.util.getValue("company.owner");
	
	CompanyProxy.getPageAsHTMLTable(CFilter, inPage, function(table) {
		dwr.util.setValue(CompanyProxy.mainDiv, table, {
			escapeHtml :false
		});
	});
	
	return false;
};

//  Set page size
CompanyProxy.setPageDencity = function() {
	alert(dwr.util.getValue("CompanyProxy.pageDencity"));
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

CompanyProxy.remove = function(uuid) {
	if(!confirm("Are you sure!? Вы уверены!?")) return false;
	
	CompanyProxy.removeDBO(uuid, function(result){
		alert(result);
		if (Main.isOK(result)) {
			CompanyProxy.updateTable();
		}			
	});
	
	return false;
};

CompanyProxy.edit = function(uuid) {
	alert(uuid);
	return false;
};
