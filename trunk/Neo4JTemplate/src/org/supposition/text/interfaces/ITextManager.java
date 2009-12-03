package org.supposition.text.interfaces;

import java.util.Properties;

public interface ITextManager {

	public abstract void setBasename(String name);

	public abstract String getBasename();

	public abstract String getDefaultByKey(String inKey);

	public abstract void setDefaultByKey(String inKey, String inValue);

	public abstract String getTextByKey(String inKey, String inLocale);

	public abstract boolean hasLocale(String inLocale);

	public abstract boolean hasKey(String inKey, String inLocale);

	public abstract boolean hasDefaultKey(String inKey);

	public abstract void tryToLoadPropertyFile(String inLocale);

	public abstract Properties getDefaults();

}