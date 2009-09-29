package org.supposition.db.auto;

/** Class _Ads was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Ads extends org.apache.cayenne.CayenneDataObject {

    public static final String CREATED_PROPERTY = "created";
    public static final String DELETE_AFTER_PROPERTY = "deleteAfter";
    public static final String PRICE_PROPERTY = "price";
    public static final String TEXT_PROPERTY = "text";
    public static final String TYPE_PROPERTY = "type";
    public static final String UUID_PROPERTY = "uuid";
    public static final String CGROUP_PROPERTY = "cgroup";
    public static final String COMPANY_PROPERTY = "company";
    public static final String USER_PROPERTY = "user";

    public static final String ID_PK_COLUMN = "id";

    public void setCreated(java.util.Date created) {
        writeProperty("created", created);
    }
    public java.util.Date getCreated() {
        return (java.util.Date)readProperty("created");
    }
    
    
    public void setDeleteAfter(java.util.Date deleteAfter) {
        writeProperty("deleteAfter", deleteAfter);
    }
    public java.util.Date getDeleteAfter() {
        return (java.util.Date)readProperty("deleteAfter");
    }
    
    
    public void setPrice(String price) {
        writeProperty("price", price);
    }
    public String getPrice() {
        return (String)readProperty("price");
    }
    
    
    public void setText(String text) {
        writeProperty("text", text);
    }
    public String getText() {
        return (String)readProperty("text");
    }
    
    
    public void setType(String type) {
        writeProperty("type", type);
    }
    public String getType() {
        return (String)readProperty("type");
    }
    
    
    public void setUuid(String uuid) {
        writeProperty("uuid", uuid);
    }
    public String getUuid() {
        return (String)readProperty("uuid");
    }
    
    
    public void setCgroup(org.supposition.db.Cgroup cgroup) {
        setToOneTarget("cgroup", cgroup, true);
    }

    public org.supposition.db.Cgroup getCgroup() {
        return (org.supposition.db.Cgroup)readProperty("cgroup");
    } 
    
    
    public void setCompany(org.supposition.db.Company company) {
        setToOneTarget("company", company, true);
    }

    public org.supposition.db.Company getCompany() {
        return (org.supposition.db.Company)readProperty("company");
    } 
    
    
    public void setUser(org.supposition.db.User user) {
        setToOneTarget("user", user, true);
    }

    public org.supposition.db.User getUser() {
        return (org.supposition.db.User)readProperty("user");
    } 
    
    
}
