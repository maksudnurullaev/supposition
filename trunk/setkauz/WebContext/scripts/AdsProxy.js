Namespace("AdsProxy");

AdsProxy.mainDiv = "main.ads.table";

AdsProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.ads.formNew",AdsProxy.mainDiv, false);
	return false;
};

AdsProxy.addNew = function(){
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
	
	AdsProxy.addDBONew(Ads, function(result){
		alert(result);
		if (Main.isOK(result)) {
			AdsProxy.updateTable();
		}			
	});
	
	return false;	
};

AdsProxy.updateTable = function() {
	if (Main.byId("AdsProxy.currentPage")) {
		if (parseInt(dwr.util.getValue("AdsProxy.currentPage"),10) > 0) {
			AdsProxy.go2Page(parseInt(dwr.util.getValue("AdsProxy.currentPage"),10));
		}
	} else {
		AdsProxy.go2Page(1);
	}
	return false;
};

AdsProxy.go2Page = function(inPage) {
	AdsProxy.getPageAsHTMLTable(dwr.util.getValue("guuid"), inPage, function(table) {
		dwr.util.setValue(AdsProxy.mainDiv, table, {
			escapeHtml :false
		});
	});
	return false;
};

//Set page size
AdsProxy.setPageDencity = function() {
	if (parseInt(dwr.util.getValue("AdsProxy.pageDencity"), 10) > 0) {
		AdsProxy.setPageSize(parseInt(dwr.util
				.getValue("AdsProxy.pageDencity"), 10), function() {
			AdsProxy.go2Page(1);
		});
	}
	return false;
};

AdsProxy.remove = function(uuid) {
	AdsProxy.removeDBO(uuid, function(result){
		alert(result);
		if (Main.isOK(result)) {
			AdsProxy.updateTable();
		}			
	});
	
	return false;
};
