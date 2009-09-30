Namespace("CompanyProxy");

CompanyProxy.mainDiv = "main.company.table";

CompanyProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.company.formNew",CompanyProxy.mainDiv, false);
	return false;
};

CompanyProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("text")){ return false; }
	
	Ads = {text:null, 
			kaptcha:null,
			price:null,
			weeks2keep:null,
			cuuid:null,
			guuid:null,
			type:null};

	dwr.util.getValues(Ads);
	
//	alert("Ads.type = " + Ads.type);
//	alert("Ads.text = " + Ads.text);
//	alert("Ads.price = " + Ads.price);
//	alert("Ads.weeks2keep = " + Ads.weeks2keep);
//	alert("Ads.cuuid = " + Ads.cuuid);
//	alert("Ads.guuid = " + Ads.guuid);
	
	CompanyProxy.addDBONew(Ads, function(result){
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
