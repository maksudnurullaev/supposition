package org.hydra.utils;

import groovy.util.Proxy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.guice.spring.WebApplicationContextLoader;
import org.hydra.collectors.MessagesCollector;
import org.hydra.collectors.StatisticsCollector;
import org.hydra.messages.ProxyMessage;
import org.hydra.messages.interfaces.IMessage;
import org.hydra.pipes.Pipe;
import org.hydra.pipes.interfaces.IPipe;
import org.hydra.spring.AppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author M.Nurullayev
 * 
 */
public final class Constants {
	private static Log _log = LogFactory.getLog("org.hydra.utils.Constants");		
	// **** BEAN Constants
	public static final String _beans_main_input_pipe = "_main_input_pipe_";
	public static final String _beans_main_message_collector = "_main_message_collector";
	public static final String _beans_main_statistics_collector = "_statisticsCollector";
	// **** END - BEAN Constants
	public static final String _project_name = "Supposition";
	public static final String _project_version = "0.1";
	public static final String _conf_dir_location = "conf";
	public static final String _logs_dir_location = "logs";
	public static final String _date_time_id_format = "yyyy.MM.dd HH:mm:ss";
	// **** Common constants ****

	public static final Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

	public static final int _unlimited = -1;

	public static final String UnknownString = "Unknown";
	
	// **** Deafult waiting times (in milliseconds)
	public static final long _default_sleep_time = 100;
	public static final long _default_wait_message_time = 2000; 
	
	// **** For dispatcher
	public static final String _message_handler_posfix = "Handler";
	public static final String _message_handler_class_path = "org.hydra.messages.handlers.";
	public static final String _file_name_delimiter = "_";
	public static final String _beans_text_manager = "_text_manager";
	public static final int _min_password_length = 4;
	public static final String _password_salted = "SALTED";
	public static final String _web_error_result_prefix = "ERROR:";
	public static String _web_ok_result_prefix = "OK:";
	public static String _string_userId = "userId";
	public static String _path2ApplicationContext_xml = "WebContext/WEB-INF/applicationContext.xml";
	public static long _max_response_wating_time = 5000; // 5 seconds

	public static String GetCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(_date_time_id_format);
		return String.format(sdf.format(new Date()));
	};

	public static String GetCurrentDateTime(String inFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(inFormat);
		return String.format(sdf.format(new Date()));
	};	
	
	public static String GetUUID() {
		return java.util.UUID.randomUUID().toString();

	}

	/**
	 * Validate the form of an email address.
	 * 
	 * @param aEmailAddress
	 *            that will be compiled against:
	 *            <p>
	 *            {@code Pattern.compile(".+@.+\\.[a-z]+")}
	 *            </p>
	 * @return (boolean)true or false
	 * 
	 */
	public static boolean isValidEmailAddress(String aEmailAddress) {
		Matcher m = p.matcher(aEmailAddress);
		return m.matches();
	}	

	public static String defaultSign() {
		return UnknownString;
	}
	
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	private static final int DefaultStringTrimLength = 50;
	private static final int DefaultStringTrimLengthGap = 10;
	// For JavaSctipt interchange
	public static final int FALSE = 0;
	public static final int TRUE = 1;

	public static String currentDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());

	}
	
	public static String trimString(String inString) {
		return trimString(inString, DefaultStringTrimLength,
				DefaultStringTrimLengthGap);
	}

	public static String trimString(String inString, int inMaxCount, int inGap) {
		// Initial check
		if (inMaxCount <= 0)
			inMaxCount = DefaultStringTrimLength;
		if (inGap <= 0)
			inGap = DefaultStringTrimLengthGap;
		if (inString.length() > (inMaxCount + inGap))
			return inString.substring(0, inMaxCount).trim() + "...";
		return inString;
	}

	public static String getCurrentSessionID() {
		return WebContextFactory.get().getScriptSession().getId();
	}
	
	public static String getDefaultDispatcherName(String messageClassName) {
		return _message_handler_class_path 
				+ messageClassName
				+ _message_handler_posfix;
	}

	public static String translate(String string) {
		return string;
	}
	/**
	 * Get current web session id
	 * @return UniqueSessionID(String)
	 */
	public static String getWebSessionID(){
		return WebContextFactory.get().getSession().getId();
	}
	
	/**
	 * Returns main input pipe
	 * @return Pipe
	 */
	public static Pipe getMainInputPipe(){
		Object _result = AppContext.getApplicationContext().getBean(Constants._beans_main_input_pipe);
		if(_result instanceof Pipe)
			return (Pipe)_result;
		else
			_log.fatal("Could not initialize " + Constants._beans_main_input_pipe + " object");
		
		return null;	
	}
	
	/**
	 * Returns main statistics collector
	 * @return StatisticsCollector
	 */
	public static StatisticsCollector getMainStatisticsCollector() {
		Object _result = AppContext.getApplicationContext().getBean(Constants._beans_main_statistics_collector);
		if(_result instanceof StatisticsCollector)
			return (StatisticsCollector)_result;
		else
			_log.fatal("Could not initialize " + Constants._beans_main_statistics_collector + " object");
		
		return null;	
	}
	
	/**
	 * Returns main messages collector
	 * @return MessagesCollector
	 */
	public static MessagesCollector getMainMesssageCollector() {
		Object _result = AppContext.getApplicationContext().getBean(Constants._beans_main_message_collector);
		if(_result instanceof MessagesCollector)
			return (MessagesCollector)_result;
		else
			_log.fatal("Could not initialize " + Constants._beans_main_message_collector + " object");
		
		return null;	
	}
	
	/**
	 * Create proxy message with type(inType) and data(inData)
	 * @param inType
	 * @param inData
	 * @return ProxyMessage
	 */
	public static ProxyMessage createProxyMessage(String inType, String inData) {
		ProxyMessage _result = new ProxyMessage();
		
		_result.setType(inType);
		_result.setData(inData);
		
		return _result;
	}
		
}
