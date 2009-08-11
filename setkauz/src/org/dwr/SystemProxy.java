package org.dwr;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SystemProxy
{
	private Log _log = LogFactory.getLog(this.getClass());		

	@SuppressWarnings("unchecked")
	public String getDefaultsAsHTMLTable(){
		String format = MessagesManager.getText("main.admin.defaults.table.tr");
		String result = "";		
		_log.debug("Get Session as HTML Table");
		
		Enumeration<String> attributeNames = SessionManager.getHttpSession().getAttributeNames();
		
//		for (Enumeration<String> e =attributeNames; e.hasMoreElements();){
//			String name  = e.nextElement();
//			result += String.format(format, name, SessionManager.getHttpSession().getAttribute(name));
//		}
		
		return MessagesManager.getText("main.admin.defaults.table.header")
		+ result
		+ MessagesManager.getText("main.admin.defaults.table.footer");	
    }
	
	@SuppressWarnings("unchecked")
	public String getSessionAsHTMLTable(){
		String format = MessagesManager.getText("main.admin.session.table.tr");
		String result = "";		
		_log.debug("Get Defaults as HTML Table");
		
		Enumeration<String> attributeNames = SessionManager.getHttpSession().getAttributeNames();
		
		for (Enumeration<String> e =attributeNames; e.hasMoreElements();){
			String name  = e.nextElement();
			result += String.format(format, name, SessionManager.getHttpSession().getAttribute(name));
		}
		
		return MessagesManager.getText("main.admin.session.table.header")
		+ result
		+ MessagesManager.getText("main.admin.session.table.footer");	
    }	
}