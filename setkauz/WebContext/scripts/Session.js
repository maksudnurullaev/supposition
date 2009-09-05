Namespace("Session");

Session.updateSessionTable = function() {
	Session.getSessionAsHTMLTable( function(result) {
		dwr.util.setValue("main.admin.session.table", result, {
			escapeHtml :false
		});
	});
	return false;
};
