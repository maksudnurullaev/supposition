package org.supposition.db.auto;

import java.util.List;

/** Class _User was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _User extends org.apache.cayenne.CayenneDataObject {

    public static final String ADDITIONALS_PROPERTY = "additionals";
    public static final String CREATED_PROPERTY = "created";
    public static final String MAIL_PROPERTY = "mail";
    public static final String PASSWORD_PROPERTY = "password";
    public static final String SALT_PROPERTY = "salt";
    public static final String STATUS_PROPERTY = "status";
    public static final String STRING2VERIFY_PROPERTY = "string2verify";
    public static final String UPDATED_PROPERTY = "updated";
    public static final String UUID_PROPERTY = "uuid";
    public static final String ADS_PROPERTY = "ads";
    public static final String COMPANIES_PROPERTY = "companies";
    public static final String ROLES_PROPERTY = "roles";

    public static final String ID_PK_COLUMN = "id";

    public void setAdditionals(String additionals) {
        writeProperty("additionals", additionals);
    }
    public String getAdditionals() {
        return (String)readProperty("additionals");
    }
    
    
    public void setCreated(String created) {
        writeProperty("created", created);
    }
    public String getCreated() {
        return (String)readProperty("created");
    }
    
    
    public void setMail(String mail) {
        writeProperty("mail", mail);
    }
    public String getMail() {
        return (String)readProperty("mail");
    }
    
    
    public void setPassword(String password) {
        writeProperty("password", password);
    }
    public String getPassword() {
        return (String)readProperty("password");
    }
    
    
    public void setSalt(String salt) {
        writeProperty("salt", salt);
    }
    public String getSalt() {
        return (String)readProperty("salt");
    }
    
    
    public void setStatus(String status) {
        writeProperty("status", status);
    }
    public String getStatus() {
        return (String)readProperty("status");
    }
    
    
    public void setString2verify(String string2verify) {
        writeProperty("string2verify", string2verify);
    }
    public String getString2verify() {
        return (String)readProperty("string2verify");
    }
    
    
    public void setUpdated(String updated) {
        writeProperty("updated", updated);
    }
    public String getUpdated() {
        return (String)readProperty("updated");
    }
    
    
    public void setUuid(String uuid) {
        writeProperty("uuid", uuid);
    }
    public String getUuid() {
        return (String)readProperty("uuid");
    }
    
    
    public void addToAds(org.supposition.db.Ads obj) {
        addToManyTarget("ads", obj, true);
    }
    public void removeFromAds(org.supposition.db.Ads obj) {
        removeToManyTarget("ads", obj, true);
    }
    public List getAds() {
        return (List)readProperty("ads");
    }
    
    
    public void addToCompanies(org.supposition.db.Company obj) {
        addToManyTarget("companies", obj, true);
    }
    public void removeFromCompanies(org.supposition.db.Company obj) {
        removeToManyTarget("companies", obj, true);
    }
    public List getCompanies() {
        return (List)readProperty("companies");
    }
    
    
    public void addToRoles(org.supposition.db.Role obj) {
        addToManyTarget("roles", obj, true);
    }
    public void removeFromRoles(org.supposition.db.Role obj) {
        removeToManyTarget("roles", obj, true);
    }
    public List getRoles() {
        return (List)readProperty("roles");
    }
    
    
}
