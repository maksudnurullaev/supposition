package org.hydra.messages.abstracts;

import org.hydra.messages.interfaces.IMessage;
import org.hydra.utils.Constants;

/**
 * @author M.Nurullayev
 *
 */
public abstract class AMessage implements IMessage {
	protected Object _object = null;
	protected String _groupID = Constants.defaultSign();

	public boolean isSigned() {
		return (_groupID != Constants.defaultSign());
	};

	public boolean isObjectAttached() {
		return (_object != null);
	}

	public void setGroupID(String inSign) {
		_groupID = inSign;
	}

	public String getGroupID() {
		return _groupID;
	}

	public Object getObject() {
		return _object;
	}

	public void setObject(Object inObject) {
		_object = inObject;
	}
}
