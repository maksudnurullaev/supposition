package org.hydra.messages.interfaces;

public interface IMessage {

	/**
	 * Set sign to message
	 * @param inSign
	 */
	public void setGroupID(String inSign);
	
	/**
	 * Get sign of message
	 * @return String
	 */		
	public String getGroupID();
	
	/**
	 * Get attached object
	 * @return Object
	 */		
	public Object getObject();
	
	/**
	 * Attach object to message
	 * @param inObject
	 */
	public void setObject(Object inObject);
	
	/**
	 * Check that message has attached object
	 * @return boolean
	 */
	public boolean isObjectAttached();
	
}