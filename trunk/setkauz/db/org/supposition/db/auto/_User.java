package org.supposition.db.auto;

import java.util.List;

import org.apache.cayenne.CayenneDataObject;
import org.supposition.db.Role;

/**
 * Class _User was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _User extends CayenneDataObject {

    public static final String ADDITIONALS_PROPERTY = "Additionals";
    public static final String CREATED_PROPERTY = "Created";
    public static final String ID_PROPERTY = "ID";
    public static final String MAIL_PROPERTY = "Mail";
    public static final String PASSWORD_PROPERTY = "Password";
    public static final String SALT_PROPERTY = "Salt";
    public static final String STATUS_PROPERTY = "Status";
    public static final String UPDATED_PROPERTY = "Updated";
    public static final String VERIFY_FOR_PROPERTY = "VerifyFor";
    public static final String VERIFY_STRING_PROPERTY = "VerifyString";
    public static final String ROLES_PROPERTY = "Roles";

    public static final String ID_PK_COLUMN = "ID";

    public void setAdditionals(String Additionals) {
        writeProperty("Additionals", Additionals);
    }
    public String getAdditionals() {
        return (String)readProperty("Additionals");
    }

    public void setCreated(String Created) {
        writeProperty("Created", Created);
    }
    public String getCreated() {
        return (String)readProperty("Created");
    }

    public void setID(Integer ID) {
        writeProperty("ID", ID);
    }
    public Integer getID() {
        return (Integer)readProperty("ID");
    }

    public void setMail(String Mail) {
        writeProperty("Mail", Mail);
    }
    public String getMail() {
        return (String)readProperty("Mail");
    }

    public void setPassword(String Password) {
        writeProperty("Password", Password);
    }
    public String getPassword() {
        return (String)readProperty("Password");
    }

    public void setSalt(String Salt) {
        writeProperty("Salt", Salt);
    }
    public String getSalt() {
        return (String)readProperty("Salt");
    }

    public void setStatus(String Status) {
        writeProperty("Status", Status);
    }
    public String getStatus() {
        return (String)readProperty("Status");
    }

    public void setUpdated(String Updated) {
        writeProperty("Updated", Updated);
    }
    public String getUpdated() {
        return (String)readProperty("Updated");
    }

    public void setVerifyFor(String VerifyFor) {
        writeProperty("VerifyFor", VerifyFor);
    }
    public String getVerifyFor() {
        return (String)readProperty("VerifyFor");
    }

    public void setVerifyString(String VerifyString) {
        writeProperty("VerifyString", VerifyString);
    }
    public String getVerifyString() {
        return (String)readProperty("VerifyString");
    }

    public void addToRoles(Role obj) {
        addToManyTarget("Roles", obj, true);
    }
    public void removeFromRoles(Role obj) {
        removeToManyTarget("Roles", obj, true);
    }
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        return (List<Role>)readProperty("Roles");
    }


}
