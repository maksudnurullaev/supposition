package org.supposition.db.auto;

import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.supposition.db.User;

/**
 * Class _Role was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Role extends CayenneDataObject {

    public static final String ID_PROPERTY = "ID";
    public static final String NAME_PROPERTY = "Name";
    public static final String USERS_PROPERTY = "Users";

    public static final String ID_PK_COLUMN = "ID";

    public void setID(Integer ID) {
        writeProperty("ID", ID);
    }
    public Integer getID() {
        return (Integer)readProperty("ID");
    }

    public void setName(String Name) {
        writeProperty("Name", Name);
    }
    public String getName() {
        return (String)readProperty("Name");
    }

    public void addToUsers(User obj) {
        addToManyTarget("Users", obj, true);
    }
    public void removeFromUsers(User obj) {
        removeToManyTarget("Users", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return (List<User>)readProperty("Users");
    }


}
