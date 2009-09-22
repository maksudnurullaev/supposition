package org.supposition.db.proxy;

public class AdsBean extends SimpleProxy {
	private String text;
	private String price;
	private String type;
		
	public void setText(String text) {
		this.text = text;
	}	
	public String getText() {
		return text;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPrice() {
		return price;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
}
