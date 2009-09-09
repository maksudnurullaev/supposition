Namespace("Gup");

Gup.testURL = function(name, url) {
	if (!url) url = window.location.href;
	name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");	
	var results = new RegExp("[\\?&]"+name+"=([^&#]*)").exec(url);
	if( results == null ) return null;	
	else 
		return decodeURIComponent(results[1].replace(/\+/g," ")); 
};
