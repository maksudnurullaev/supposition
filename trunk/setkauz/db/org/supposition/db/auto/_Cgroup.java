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
    public static final String CHILDS_PROPERTY = "Childs";
    public static final String COMPANIES_PROPERTY = "Companies";
    public static final String PARENT_PROPERTY = "Parent";

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
    
    
    public void addToChilds(org.supposition.db.Cgroup obj) {
        addToManyTarget("Childs", obj, true);
    }
    public void removeFromChilds(org.supposition.db.Cgroup obj) {
        removeToManyTarget("Childs", obj, true);
    }
    public List getChilds() {
        return (List)readProperty("Childs");
    }
    
    
    public void addToCompanies(org.supposition.db.Company obj) {
        addToManyTarget("Companies", obj, true);
    }
    public void removeFromCompanies(org.supposition.db.Company obj) {
        removeToManyTarget("Companies", obj, true);
    }
    public List getCompanies() {
        return (List)readProperty("Companies");
    }
    
    
    public void setParent(org.supposition.db.Cgroup Parent) {
        setToOneTarget("Parent", Parent, true);
    }

    public org.supposition.db.Cgroup getParent() {
        return (org.supposition.db.Cgroup)readProperty("Parent");
    } 
    
    
}
