package org.hydra.messages.interfaces;

public interface IMessage {
	/**
	 * Message directions:
	 * <li>(PUBLIC) - message should be handled by ALL processors
	 * <li>(PRIVATE) - message should be handled by ONE processors
	 */	
	public static enum TYPE{PUBLIC, PRIVATE};
	
	/**
	 * Set direction of message
	 */	
	public void setType(TYPE inDirection);
	
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
	 * Check sign of message
	 * @return boolean
	 */
	public boolean isSigned();
	
	/**
	 * Check that message has attached object
	 * @return boolean
	 */
	public boolean isObjectAttached();
	
	/**
	 * Set direction of messages
	 */	
	public TYPE getType();
	
	/**
	 * Check is massage on proxy mode
	 * @return boolean
	 */
	public boolean isProxy();
	
	/**
	 * Changes proxy mode
	 * @param inDirection
	 */	
	public void setProxy(boolean inProxy);
}