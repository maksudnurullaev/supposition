package org.supposition.db.auto;

import java.util.List;

/** Class _Role was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Role extends org.apache.cayenne.CayenneDataObject {

    public static final String NAME_PROPERTY = "name";
    public static final String UUID_PROPERTY = "uuid";
    public static final String USERS_PROPERTY = "users";

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
    
    
    public void addToUsers(org.supposition.db.User obj) {
        addToManyTarget("users", obj, true);
    }
    public void removeFromUsers(org.supposition.db.User obj) {
        removeToManyTarget("users", obj, true);
    }
    public List getUsers() {
        return (List)readProperty("users");
    }
    
    
}
