package org.hydra.web.services;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hydra.messages.Message;
import org.hydra.messages.ProxyMessage;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.pipes.exceptions.RichedMaxCapacityException;
import org.hydra.utils.Constants;
import org.hydra.utils.abstracts.ALogger;

public class MessageHandler extends ALogger {

	public List<ProxyMessage> sendMessage(ProxyMessage inMessage) throws RichedMaxCapacityException{
		// 1. Create result list of ProxyMessage
		List<ProxyMessage> _result = new ArrayList<ProxyMessage>();
		
		// 2. Try to create internal message from proxy
		IMessage message = createIMessageFromProxyMessage(inMessage);
		if(message == null){
			String errorMessage = "Could not parse proxy message type: " + inMessage.getType();
			getLog().error(errorMessage);
			_result.add(Constants.createProxyMessage("message.error", errorMessage));
			return _result;
			
		}else // 3. Send message to hydra
			Constants.getMainInputPipe().setMessage(createIMessageFromProxyMessage(inMessage));
		
		// 4. Waiting for response
		long startTime = System.currentTimeMillis();
		boolean gotResponse = true;
		while(!Constants.getMainMesssageCollector().hasNewMessages(Constants.getCurrentSessionID())){
			if(System.currentTimeMillis() - startTime > Constants._max_response_wating_time){
				gotResponse = false;				
				
				String errorMessage = "Waiting time for response is over...";				
				getLog().error(errorMessage);
				_result.add(Constants.createProxyMessage("message.error", errorMessage));				
				
				break;
			}
			Thread.yield();
		}
		// 5. If response messages exist
		if(gotResponse){
			IMessage imessage = null;
			while((imessage = Constants.getMainMesssageCollector().getMessage(Constants.getCurrentSessionID())) != null){
				if(imessage.getObject() instanceof ProxyMessage){
					_result.add((ProxyMessage) imessage.getObject());
				} else
					getLog().error(String.format("Response object(%s) is not ProxyMessage type",imessage.getObject().toString()));					
			}
		}

		return _result;
		
	}

	/**
	 * Try to create internal message from proxy messages
	 * @param inMessage(ProxyMessage)
	 * @return IMessage
	 */
	private IMessage createIMessageFromProxyMessage(ProxyMessage inMessage) {
		if(inMessage.getType().contentEquals("html.context")){
			return createHTMLContextMessage(inMessage);
		}
		return null;
	}

	/**
	 * Parse html message from proxy message
	 * @param inMessage
	 * @return IMessage
	 */
	private IMessage createHTMLContextMessage(ProxyMessage inMessage) {
		Message _result = new Message();
		_result.setGroupID(Constants.getCurrentSessionID());
		_result.setObject(inMessage);
		return _result;
	}
}
