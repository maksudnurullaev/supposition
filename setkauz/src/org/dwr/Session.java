package org.dwr;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Session
{
	private static final int FALSE = 0;
	private static final int TRUE = 1;
	
	public Log _log = LogFactory.getLog(this.getClass());		
    /**
     * A simple test that the DWr is working. Used by the front page.
     * @return The text of the insert.html page
     * @throws IOException From {@link WebContext#forwardToString(String)}
     * @throws ServletException From {@link WebContext#forwardToString(String)}
     */
    public String getHTMLTextFromFile(String pathToHtmlFile) throws ServletException, IOException
    {
        return WebContextFactory.get().forwardToString(pathToHtmlFile);
    }
    
    public void setLocale(String lang){
		MessagesManager.changeLocale(lang);
    }

	public int hasMessageByKey(String inKey){
		return MessagesManager.hasMessageByKey(inKey)?TRUE:FALSE;
	}    
    
    public String getTextByKey(String inKey){
    	return MessagesManager.getText(inKey);
    } 
    
	public String getTextByKeyAsDiv(String inKey){
		return String.format("<div id=\"ID.%s\">%s</div>", inKey, MessagesManager.getText(inKey));
	}    
    
    public String getSessionID(){
    	return WebContextFactory.get().getScriptSession().getId();
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
	
	public String logOff(){
		SessionManager.logoffUser();
		return MessagesManager.getDefault("web.ok.result.prefix")
			+ MessagesManager.getText("message.bye");
	}
}