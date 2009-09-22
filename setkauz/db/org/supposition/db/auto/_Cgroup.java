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
    public static final String CHILDS_PROPERTY = "childs";
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
    
    
    public void addToChilds(org.supposition.db.Cgroup obj) {
        addToManyTarget("childs", obj, true);
    }
    public void removeFromChilds(org.supposition.db.Cgroup obj) {
        removeToManyTarget("childs", obj, true);
    }
    public List getChilds() {
        return (List)readProperty("childs");
    }
    
    
    public void setParent(org.supposition.db.Cgroup parent) {
        setToOneTarget("parent", parent, true);
    }

    public org.supposition.db.Cgroup getParent() {
        return (org.supposition.db.Cgroup)readProperty("parent");
    } 
    
    
}