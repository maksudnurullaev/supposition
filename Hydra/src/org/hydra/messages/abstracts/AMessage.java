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
	private TYPE _type = TYPE.PUBLIC;

	public void setType(TYPE inType) {
		_type = inType;
	}

	public TYPE getType() {
		return _type;
	}

	public boolean isSigned() {
		return (_groupID != Constants.defaultSign());
	};

	public boolean isObjectAttached() {
		return (_object != null);
	}

	public void setGroupID(String inSign) {
		_groupID = inSign;
		setProxy(false);
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

	public boolean isProxy() {
		return (getType() == TYPE.PUBLIC);
	}

	public void setProxy(boolean inProxy) {
		this._type = inProxy ? TYPE.PUBLIC : TYPE.PRIVATE;
	}

}
