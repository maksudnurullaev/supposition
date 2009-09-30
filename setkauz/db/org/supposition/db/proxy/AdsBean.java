package org.supposition.db.proxy;

public class AdsBean extends SimpleProxy {
	private String text;
	private String price;
	private String kaptcha;	
	private int weeks2keep;	
	// Seller or Buyer
	private String type;
	// Cgroup uuid;
	private String guuid;
	// Company uuid;
	private String cuuid;
	// Company city;
	private String city;
		
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
	public void setCuuid(String cuuid) {
		this.cuuid = cuuid;
	}
	public String getCuuid() {
		return cuuid;
	}
	public void setGuuid(String guuid) {
		this.guuid = guuid;
	}
	public String getGuuid() {
		return guuid;
	}
	public void setKaptcha(String kaptcha) {
		this.kaptcha = kaptcha;
	}
	public String getKaptcha() {
		return kaptcha;
	}
	public void setWeeks2keep(int weeks2keep) {
		this.weeks2keep = weeks2keep;
	}
	public int getWeeks2keep() {
		return weeks2keep;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity() {
		return city;
	}
}
