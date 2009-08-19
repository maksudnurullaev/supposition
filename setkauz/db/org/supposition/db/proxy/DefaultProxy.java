package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.cayenne.validation.ValidationFailure;
import org.apache.cayenne.validation.ValidationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supposition.db.Default;
import org.supposition.db.proxy.abstracts.ADBProxyObject;
import org.supposition.utils.Constants;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.SessionManager;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class DefaultProxy extends ADBProxyObject<Default>
{
	private Log _log = LogFactory.getLog(this.getClass());		

	public DefaultProxy(){
		super();
		setEClass(Default.class);
	}
	
	@Override
	public List<String> getColumnNames() {
		String[] result = { "Key", "Value" };
		return Arrays.asList(result);
	}	
	
	@Override
	public int getCount() {
		return getAll().size();
	}	
	
	public String getDefaultsAsHTMLTable(){
		String format = MessagesManager.getText("main.admin.defaults.table.tr");
		String result = "";		
		_log.debug("Get Session as HTML Table");
		
		List<Default> list = getAll();
		
		_log.debug("Found " + list.size() + " DBObject(s)");
		
		for (Default df:list){
			result += String.format(format, df.getKey(), df.getValue(), df.getID(), df.getID());
		}
		
		return MessagesManager.getText("main.admin.defaults.table.header")
		+ result
		+ MessagesManager.getText("main.admin.defaults.table.footer");	
    }
	
	public String deleteDBOKeyValue(int ID){
		_log.debug("Delete object ID = " + ID);
		deleteObject(getDBObjectByIntPk(ID));
		return Constants._web_ok_result_prefix + MessagesManager.getText("message.data.saved");
	}
	
	public String updateDBODefault(KeyValueBean inKeyValue){
		if(inKeyValue == null)
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.null.object");
		
		Default keyValue = null;
		
		_log.debug(String.format("Tring to create DBO Default with key(%s) and value(%s)",
				inKeyValue.getKey(),
				inKeyValue.getValue()));
		
		try {
			if(inKeyValue.isNew())
				keyValue = createNew();
			else
				keyValue = getDBObjectByIntPk(inKeyValue.getId());
		} catch (Exception e) {
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		if(keyValue == null)
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.null.object");
		
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
		
		commitChanges();
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

	public String getDefaultForm(KeyValueBean inKeyValue){	
		if(inKeyValue == null)
			return Constants._web_error_result_prefix + MessagesManager.getText("errors.null.object");
		// New
		_log.debug("KeyValue.ID = " + inKeyValue.getId());
		if(inKeyValue.isNew()){
			return MessagesManager.getText("main.admin.system.defaults.formHeaderNew")
				+ String.format(MessagesManager.getText("main.admin.system.defaults.form")
						, ""
						, ""
						, -1);
		}
		
		// Update
		Default keyValue = getDBObjectByIntPk(inKeyValue.getId());
		if(keyValue == null){
			return MessagesManager.getText("errors.object.not.found");
		}
		
		return MessagesManager.getText("main.admin.system.defaults.formHeaderUpdate")
		+ String.format(MessagesManager.getText("main.admin.system.defaults.form")
				, keyValue.getKey()
				, keyValue.getValue()
				, keyValue.getID());
	}
	
}