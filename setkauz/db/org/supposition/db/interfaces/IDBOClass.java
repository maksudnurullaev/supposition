package org.supposition.db.interfaces;

import org.apache.cayenne.validation.ValidationResult;

public interface IDBOClass {

	public abstract boolean isNew();

	public abstract void postValidationSave();

	public abstract ValidationResult getValidationResult();

}