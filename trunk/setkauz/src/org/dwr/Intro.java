package org.dwr;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.supposition.utils.Constants;
import org.supposition.utils.MessagesManager;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Intro
{
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
    
    public String getTitle(){
   		return MessagesManager.getText("mainTitle");		
    }
    
    public String getTabList(){
   		return MessagesManager.getText("mainTabList");		
    }          

	public int hasMessageByKey(String inKey){
		return MessagesManager.hasMessageByKey(inKey)?Constants.TRUE:Constants.FALSE;
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
}