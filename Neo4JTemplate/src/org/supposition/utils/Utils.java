package org.supposition.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.util.WebUtils;
/**
 * 
 * @author M.Nurullayev
 * 
 */
public final class Utils {
	public static Log _log = LogFactory.getLog("org.supposition.utils.Utils");

	public static boolean isValidLink(String inLink){
		if(inLink == null 
				|| inLink.isEmpty() 
				|| inLink.equals("http://"))
			return false;
		return true;
	}
	
	public static final Pattern _pattern_to_cheack_email = 
		Pattern.compile(MessagesManager.getDefault("regex.pattern.to.cheack.email"));
	
	public static String GetCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(MessagesManager.getDefault("default.date.format"));
		return String.format(sdf.format(new Date()));
	};
	
	public static String GetFormatedDate(String format) {
		return GetFormatedDate(format, new Date());
	};	
	
	public static String GetFormatedDate(String format, Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(inDate);
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
		Matcher m =  _pattern_to_cheack_email.matcher(aEmailAddress);
		return m.matches();
	}

	public static boolean isValidString(String str) {
		if ((str == null) || str.matches("^\\s*$")) {
			return false;
		} else {
			return true;
		}
	}

	public static String currentDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(MessagesManager.getDefault("default.date.format"));
		return sdf.format(cal.getTime());

	}

	public static String getUniqueDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(cal.getTime());
	}

	protected static String getUniqueDateTime(Date inDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(inDate);
	}	
	
	public static String trimString(String inString) {
		return trimString(inString, 
				getIntFromStr(MessagesManager.getDefault("default.trim.length")),
				getIntFromStr(MessagesManager.getDefault("default.trim.length.gap")));
	}

	public static String trimString(String inString, int inMaxCount, int inGap) {
		// Initial check
		if (inMaxCount <= 0)
			inMaxCount = getIntFromStr(MessagesManager.getDefault("default.trim.length"));
		if (inGap <= 0)
			inGap = getIntFromStr(MessagesManager.getDefault("default.trim.length.gap"));
		if (inString.length() > (inMaxCount + inGap))
			return inString.substring(0, inMaxCount).trim() + "...";
		return inString;
	}

	public static int getIntFromStr(String text) {
		int result = -1;
		try {
			result = Integer.parseInt(text.trim());
		} catch (NumberFormatException e) {
			_log.error("Could not parse to int = " + text);
		}
		return result;
	}

	public static boolean isWebContext() {
		return MessagesManager.getDefault("web.context.loader.as.string.tomcat").equalsIgnoreCase(Thread
				.currentThread().getContextClassLoader().getClass().getName()) 
				||
				MessagesManager.getDefault("web.context.loader.as.string.jetty").equalsIgnoreCase(Thread
						.currentThread().getContextClassLoader().getClass().getName());
	}

	public static String normalizeText(String inText) {
		if(inText == null ||
				inText.trim().isEmpty())
			return "---";
		return inText.trim();
	}

	public static String getWebAppRootPath() {
		String result = null;
		if(isWebContext()){
			// send current application context root path
			try {
				result = WebUtils.getRealPath(ContextLoader.getCurrentWebApplicationContext().getServletContext(), "/");
			} catch (FileNotFoundException e) {
				result = System.getProperty("user.dir");
				_log.error(e.getMessage());
				_log.warn("Die error case, root application path setted up to: " + result);
			}
		}else{
			//otherwise, send current root directory
			result = System.getProperty("user.dir");
		}
		
		if(!result.endsWith("/") ||
				!result.endsWith("\\"))
			result += "/";
		
		result = result.replace('/', File.separatorChar);
		
		_log.debug("Root application path is: " + result);
		
		return result;
	}



}
