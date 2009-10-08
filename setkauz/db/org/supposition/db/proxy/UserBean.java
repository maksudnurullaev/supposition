package org.supposition.db.proxy;

public class UserBean extends SimpleBean {
	private String roleuuid;
	private String mail;
	private String additionals;
	
	private String password;
	private String newpassword;
	private String newpassword2;
	
	private String kaptcha;
		
	public void setMail(String mail) {
		this.mail = mail;
	}	
	public String getMail() {
		return mail;
	}
	public void setAdditionals(String additionals) {
		this.additionals = additionals;
	}	
	public String getAdditionals() {
		return additionals;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}
	public String getNewpassword() {
		return newpassword;
	}
	public void setNewpassword2(String newpassword2) {
		this.newpassword2 = newpassword2;
	}
	public String getNewpassword2() {
		return newpassword2;
	}
	public void setRoleuuid(String roleUuid) {
		this.roleuuid = roleUuid;
	}
	public String getRoleuuid() {
		return roleuuid;
	}
	public void setKaptcha(String kaptcha) {
		this.kaptcha = kaptcha;
	}
	public String getKaptcha() {
		return kaptcha;
	}
}
