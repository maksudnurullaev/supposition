package org.supposition.db.auto;

import java.util.List;

/** Class _Company was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Company extends org.apache.cayenne.CayenneDataObject {

    public static final String ADDITIONALS_PROPERTY = "additionals";
    public static final String NAME_PROPERTY = "name";
    public static final String UUID_PROPERTY = "uuid";
    public static final String CGOUPS_PROPERTY = "Cgoups";

    public static final String ID_PK_COLUMN = "id";

    public void setAdditionals(String additionals) {
        writeProperty("additionals", additionals);
    }
    public String getAdditionals() {
        return (String)readProperty("additionals");
    }
    
    
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
    
    
    public void addToCgoups(org.supposition.db.Cgroup obj) {
        addToManyTarget("Cgoups", obj, true);
    }
    public void removeFromCgoups(org.supposition.db.Cgroup obj) {
        removeToManyTarget("Cgoups", obj, true);
    }
    public List getCgoups() {
        return (List)readProperty("Cgoups");
    }
    
    
}
