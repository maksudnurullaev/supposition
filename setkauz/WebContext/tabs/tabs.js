function Tabs() {}

Tabs.id_div_suffix = '.div';
Tabs.div_id_prefix = 'ID.';
Tabs.tablinks_id_prefix = 'tablinks.';
Tabs.div_stack = null;
Tabs.div_stack_id = 'stack';


Tabs.last_nav_path = "";

Tabs.onClik = function(elem) {
	// * Init Stack
	if(!Tabs.initStack()) return;	
	// Check ID Format 
	if(!Tabs.checkTabLink(elem)) return;
	// * Do nothing if it's old pathTab
	if(Tabs.last_nav_path == elem.id) return;
	// * Don nothing if is already selected tab
	if(elem.parentNode.className == 'Selected')return;
	
	// * Init values
	var indexOfLastDot  = elem.id.lastIndexOf('.');
	var uniqueID        = elem.id.slice(0, indexOfLastDot);
	var activeRootDivID = uniqueID + Tabs.id_div_suffix;
	var activeLinksID   = Tabs.tablinks_id_prefix + uniqueID;
	var activeDivID     = elem.id + Tabs.id_div_suffix;	
	
	// Init other divs
	var activeRootDiv = document.getElementById(activeRootDivID);
	if(!activeRootDiv){
		alert('ERROR: Could not find parent div by id!');
		return;
	}

	// * Save current inactuve div in stack
	var parentCurrentDivs = activeRootDiv.getElementsByTagName("DIV");
	for (var i = 0; i < parentCurrentDivs.length; i++) {
		Tabs.div_stack.appendChild(parentCurrentDivs[i])
	}	
	
	// * Get active Div from stack or server
	var oldStackedDiv = document.getElementById(Tabs.div_id_prefix + activeDivID)
	if(!oldStackedDiv){// ...from server
		Session.getTextByKeyAsDiv(activeDivID, function(data) {
			dwr.util.setValue(activeRootDivID, data, {
				escapeHtml :false
			});
			evaluateItByKey(activeDivID);
		});
	}else { // ...from local stack
		activeRootDiv.appendChild(oldStackedDiv);
	}
	
	// * Re-Arrange Tabs Activities
	Tabs.ReArrangeTabs(activeLinksID, elem.id);
	
	// * Finish & Save last tab path
	Tabs.last_nav_path = elem.id;
	
	return false;
}

Tabs.checkTabLink = function(elem){
	if((!elem.id) || (elem.id.lastIndexOf('.') == -1)){
		alert("Tab does not have neccessary ID or incorrect fotmat: " + elem.innerHTML);
		return false;
	}	
	return true;
}

Tabs.ReArrangeTabs = function(activeLinksID, navPath){
	// * Check all
	var activeLinks = document.getElementById(activeLinksID);
	if(!activeLinks){
		alert('ERROR: Could not find Tab Links (' + activeLinksID +')!');
		return;
	}
	// * Finish
	var lis = activeLinks.getElementsByTagName('A');
	for (var i = 0; i < lis.length; i++){
		lis[i].parentNode.className = (navPath.indexOf(lis[i].id) != -1)?"Selected":"";
	}
}

Tabs.initStack = function(){
	if(!Tabs.div_stack){
		Tabs.div_stack = document.getElementById(Tabs.div_stack_id);
		if(!Tabs.div_stack){
			alert('ERROR: Could not find invisible div stack elements!');
			return false;
		}
	}
	return true;
}

Tabs.clearStack = function(){
	if(Tabs.initStack()){
		dwr.util.setValue(Tabs.div_stack_id);
	}
}