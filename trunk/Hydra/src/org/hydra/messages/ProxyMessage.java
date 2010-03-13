package org.hydra.messages;

public class ProxyMessage {
	private String _type = "html.context";
	private String _data = "";
	
	public void setType(String _type) {
		this._type = _type;
	}
	public String getType() {
		return _type;
	}
	public void setData(String _data) {
		this._data = _data;
	}
	public String getData() {
		return _data;
	}
	
	
}
