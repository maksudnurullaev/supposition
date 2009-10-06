package org.supposition.db;

import org.apache.cayenne.validation.ValidationResult;
import org.supposition.db.auto._Group;
import org.supposition.db.interfaces.IDBOClass;

public class Group extends _Group implements IDBOClass {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ValidationResult getValidationResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNew() {
		return this.getPersistenceState() == org.apache.cayenne.PersistenceState.NEW;
	}

	@Override
	public void postValidationSave() {
		// TODO Auto-generated method stub
		
	}

}



