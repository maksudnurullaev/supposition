package org.hydra.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hydra.collectors.MessagesCollector;
import org.hydra.collectors.interfaces.ICollector;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.pipes.Pipe;
import org.hydra.pipes.exceptions.RichedMaxCapacityException;
import org.hydra.text.TextManager;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public final class MessagesManager {
	private static Log _log = LogFactory.getLog("org.supposition.utils.WebMessageFactory");	
	private static WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	
	private static MessagesCollector _messageCollector = (MessagesCollector) wac.getBean(Constants._beans_main_message_collector);
	

	private static Pipe _mainPipe = (Pipe) wac.getBean(Constants._beans_main_input_pipe);
	private static TextManager _textManager = (TextManager) wac.getBean(Constants._beans_text_manager);
	
	
	// *** TEXT MANAGER part ****
	public static  String getText(String inKey){
		return _textManager.getTextByKey(inKey);
	}
	
	// *** MESSAGE SENDER part ****
	public static boolean hasMessageByKey(String inKey){
		return _textManager.hasKey(inKey);
	}
	
	public static void sendMessage(IMessage message) {
		try {
			_mainPipe.setMessage(message);
		} catch (RichedMaxCapacityException e) {
			e.printStackTrace();
		}
		_log.debug(String.format("Send Message(%s) to main pipe", message.toString()));
	}	
		
	// *** MESSAGE RECEIVER part **** 
	public static boolean isNewMessage(String sessionID){
		return getMessageCollector().hasNewMessages(sessionID);
	}
	
	public static ICollector getMessageCollector() {
		return _messageCollector;
	}

	public static void changeLocale(String inLocale) {
		_textManager.setLocale(inLocale);
	}

	public static String getLocale() {
		return _textManager.getLocale();
	}	
}
