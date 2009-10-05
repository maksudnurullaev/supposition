Namespace("AdsProxy");

AdsProxy.testRequest = function(){
	alert('test');
};

AdsProxy.mainDiv = "main.ads.table";

AdsProxy.showNewForm = function() {
	Main.getTextFromServerToDiv("main.ads.formNew",AdsProxy.mainDiv, false);
	return false;
};

AdsProxy.addNew = function(){
	// Check fields
	if(!Main.isValidValue("ads.text")){ return false; }
	
	Ads = {text:null, 
			kaptcha:null,
			price:null,
			weeks2keep:null,
			cuuid:null,
			guuid:null,
			type:null,
			city:null};

	Ads.text = dwr.util.getValue("ads.text"); 
	Ads.kaptcha = dwr.util.getValue("kaptcha");
	Ads.price = dwr.util.getValue("ads.price");
	Ads.weeks2keep = dwr.util.getValue("ads.weeks2keep");
	Ads.cuuid = dwr.util.getValue("ads.cuuid");
	Ads.guuid = dwr.util.getValue("ads.guuid");
	Ads.type = dwr.util.getValue("ads.type");
	Ads.city = dwr.util.getValue("ads.city");
	
//	alert("Ads.type = " + Ads.type);
//	alert("Ads.text = " + Ads.text);
//	alert("Ads.price = " + Ads.price);
//	alert("Ads.weeks2keep = " + Ads.weeks2keep);
//	alert("Ads.cuuid = " + Ads.cuuid);
//	alert("Ads.guuid = " + Ads.guuid);
//	alert("Ads.city = " + Ads.city);
	
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
	GCFilter = {city:null, guuid:null};
	
	GCFilter.city = dwr.util.getValue("ads.city");
	GCFilter.guuid = dwr.util.getValue("ads.guuid");
	
	AdsProxy.getPageAsHTMLTable(GCFilter, inPage, function(table) {
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
