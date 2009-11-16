package org.supposition.db.proxy;

import java.util.Arrays;
import java.util.List;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.Cgroup;
import org.supposition.db.abstracts.ADBProxyObject;
import org.supposition.utils.DBUtils;
import org.supposition.utils.MessagesManager;
import org.supposition.utils.Utils;

public class CgroupProxy extends ADBProxyObject<Cgroup> {
	
	private static final long serialVersionUID = 1L;
	
	public CgroupProxy(){
		super();
		
		setEClass(Cgroup.class);
		setDataContext(DBUtils.getInstance().getDBContext());
	}

	public CgroupProxy(DataContext inDataContext) {
		super();
		setEClass(Cgroup.class);
		setDataContext(inDataContext);
	}	 	
	
	public String removeDBO(String inUuid){
		Cgroup cgroup = getDBObjectByUuid(inUuid);
		if(cgroup == null){
			return MessagesManager.getDefault("web.error.result.prefix")
			+ MessagesManager.getText("errors.data.not.found");
		}
		
		deleteObject(cgroup);
		commitChanges();

		return MessagesManager.getDefault("web.ok.result.prefix") 
		+ MessagesManager.getText("message.data.saved");			
	}
	
	public String addDBONew(CgroupBean inCgroupBean){
		_log.debug("addDBONew -> " + inCgroupBean.getName());

		// Check for valid name		
		if(!Utils.isValidString(inCgroupBean.getName())){
			_log.debug("addDBORole -> Constants.isValidString -> roleBean.getName() -> " + inCgroupBean.getName());			
			return MessagesManager.getDefault("web.error.result.prefix")
						+ MessagesManager.getText("errors.empty.value");
		}			
		
		// Create new DBO
		Cgroup cgroup = null;
		try {
			cgroup = createNew();
		} catch (Exception e) {
			return MessagesManager.getDefault("web.error.result.prefix") 
				+ MessagesManager.getText("errors.could.not.create.dbobject");
		}
		
		// Setup user bean
		cgroup.setCgroup(inCgroupBean);

		// Validate
		ValidationResult validationResult = cgroup.getValidationResult();

		if(validationResult.hasFailures()){
			// Delete Object before commit
			deleteObject(cgroup);
			return MessagesManager.getDefault("web.error.result.prefix")
						+ DBUtils.getFailuresAsString(validationResult);
		}
		
		// Link with parent element
		if(!inCgroupBean.getGuuid().equals(MessagesManager.getDefault("root.id.def"))){
			CgroupProxy cgroupProxy = new CgroupProxy(getDataContext());
			Cgroup pcgroup = cgroupProxy.getDBObjectByUuid(inCgroupBean.getGuuid());
			if(pcgroup != null)
				pcgroup.addToChilds(cgroup);
			else
				_log.debug("Could not find parent cgroup by uuid = " + inCgroupBean.getGuuid());
		}

		commitChanges();		
		
		return MessagesManager.getDefault("web.ok.result.prefix") 
			+ MessagesManager.getText("message.data.saved");		
		
	}
	

	@Override
	public List<String> getColumnNames() {
		_log.debug("-> getColumnNames");
		
		String[] result = { "#", "Name"};
		return Arrays.asList(result);
	}

	public Cgroup getRootElement() {
		clearExpressions();
		addExpression(ExpressionFactory.matchDbExp("pid", null));
		
		List<Cgroup> result =  getAll();
		
		if(result == null ||
				result.size() == 0){
			_log.debug("Cgroup not initilazed, initiate Root CGroup...");
			
			CgroupBean cgroupBean = new CgroupBean();
			
			cgroupBean.setName(MessagesManager.getText("text.root.group"));
			cgroupBean.setGuuid(MessagesManager.getDefault("root.id.def"));
			
			addDBONew(cgroupBean);
					
			
			return getAll().get(0);
		}
		
		return result.get(0);
	}
}
