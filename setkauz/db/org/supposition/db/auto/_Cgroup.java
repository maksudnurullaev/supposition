package org.supposition.db.auto;

import java.util.List;

/** Class _Cgroup was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Cgroup extends org.apache.cayenne.CayenneDataObject {

    public static final String NAME_PROPERTY = "name";
    public static final String UUID_PROPERTY = "uuid";
    public static final String ADS_PROPERTY = "ads";
    public static final String CHILDS_PROPERTY = "childs";
    public static final String COMPANIES_PROPERTY = "companies";
    public static final String GROUPS_PROPERTY = "groups";
    public static final String PARENT_PROPERTY = "parent";

    public static final String ID_PK_COLUMN = "id";

    public void setName(String name) {
        writeProperty("name", name);
    }
    public String getName() {
        return (String)readProperty("name");
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
    
    
    public void addToChilds(org.supposition.db.Cgroup obj) {
        addToManyTarget("childs", obj, true);
    }
    public void removeFromChilds(org.supposition.db.Cgroup obj) {
        removeToManyTarget("childs", obj, true);
    }
    public List getChilds() {
        return (List)readProperty("childs");
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
    
    
    public void addToGroups(org.supposition.db.Group obj) {
        addToManyTarget("groups", obj, true);
    }
    public void removeFromGroups(org.supposition.db.Group obj) {
        removeToManyTarget("groups", obj, true);
    }
    public List getGroups() {
        return (List)readProperty("groups");
    }
    
    
    public void setParent(org.supposition.db.Cgroup parent) {
        setToOneTarget("parent", parent, true);
    }

    public org.supposition.db.Cgroup getParent() {
        return (org.supposition.db.Cgroup)readProperty("parent");
    } 
    
    
}
