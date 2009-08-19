package org.supposition.db.proxy;

import org.supposition.utils.Constants;

public class KeyValueBean {
	private String key;
	private String value;
	private int id = Constants._default_id_for_new_dbo;
	
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
		return id == Constants._default_id_for_new_dbo;
	}
	
}
