package org.supposition.db.proxy;

import org.supposition.utils.Utils;
import org.supposition.utils.MessagesManager;

public class KeyValueBean {
	private String key;
	private String value;
	private int id = Utils.getIntFromStr(MessagesManager.getDefault("default.id.for.new.dbo"));
	
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public boolean isNew() {
		return id == Utils.getIntFromStr(MessagesManager.getDefault("default.id.for.new.dbo"));
	}
	
}
