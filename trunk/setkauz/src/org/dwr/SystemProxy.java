package org.dwr;

import java.util.Enumeration;
import java.util.List;

import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.Default;
import org.supposition.db.proxy.Defaults;
import org.supposition.db.proxy.KeyValueBean;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class SystemProxy extends ADBProxyObject<Default>
{
	private Log _log = LogFactory.getLog(this.getClass());		

	public String getDefaultsAsHTMLTable(){
		String format = MessagesManager.getText("main.admin.defaults.table.tr");
		String result = "";		
		_log.debug("Get Session as HTML Table");
		
		Defaults defaults = new Defaults();
		List<Default> list = defaults.getAll();
		
		_log.debug("Found " + list.size() + " DBObject(s)");
		
		for (Default df:list){
			result += String.format(format, df.getKey(), df.getValue(), df.getID(), df.getID());
		}
		
		return MessagesManager.getText("main.admin.defaults.table.header")
		+ result
		+ MessagesManager.getText("main.admin.defaults.table.footer");	
    }
	
	public String deleteDBOKeyValue(int ID){
		Defaults defaults = new Defaults();
		_log.debug("Delete object ID = " + ID);
		defaults.deleteObject(defaults.getDBObjectByIntPk(ID));
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");
	}
	
	public String addDBOKeyValue(KeyValueBean inKeyValue){
		if(inKeyValue == null)
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.null.object");
		
		Defaults defaults = new Defaults();
		Default keyValue = null;
		
		_log.debug(String.format("Tring to create DBO Default with key(%s) and value(%s)",
				inKeyValue.getKey(),
				inKeyValue.getValue()));
		
		try {
			keyValue = defaults.createNew();
		} catch (Exception e) {
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		keyValue.setBean(inKeyValue);
		
		ValidationResult validationResult = new ValidationResult(); 		
		keyValue.validateForSave(validationResult);
		
		if(validationResult.hasFailures()){
			System.out.println("### Validation Failed ###");
			String failResult = MessagesManager.getText("message.data.NOT.saved") + ":\n";
			for(ValidationFailure fail: validationResult.getFailures()){
				System.out.println("Fails: " + fail.getDescription());
				failResult += "\t - " + MessagesManager.getText(fail.getDescription()) + "\n";;
			}			
			return Constants._web_error_result_prefix + failResult;
		}		
		
		defaults.commitChanges();
		_log.debug("Changes commited");		
		_log.debug(DBUtils.getState(keyValue.getPersistenceState()));		
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");		
		
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

	@Override
	public List<String> getColumnNames() {
		return null;
	}
	
	
}