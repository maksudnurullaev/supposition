package org.supposition.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author M.Nurullayev
 * 
 */
public final class Utils {
	public static Log _log = LogFactory.getLog("org.supposition.utils.Utils");
	
	
	public static final Pattern _pattern_to_cheack_email = 
		Pattern.compile(MessagesManager.getDefault("pattern.to.cheack.email.pattern"));
	
	public static String GetCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(MessagesManager.getDefault("DATE.FORMAT.NOW"));
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
		SimpleDateFormat sdf = new SimpleDateFormat(MessagesManager.getDefault("DATE.FORMAT.NOW"));
		return sdf.format(cal.getTime());

	}

	public static String getUniqueDateTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(cal.getTime());
	}

	public static String trimString(String inString) {
		return trimString(inString, 
				getIntFromStr(MessagesManager.getDefault("DefaultStringTrimLength")),
				getIntFromStr(MessagesManager.getDefault("DefaultStringTrimLengthGap")));
	}

	public static String trimString(String inString, int inMaxCount, int inGap) {
		// Initial check
		if (inMaxCount <= 0)
			inMaxCount = getIntFromStr(MessagesManager.getDefault("DefaultStringTrimLength"));
		if (inGap <= 0)
			inGap = getIntFromStr(MessagesManager.getDefault("DefaultStringTrimLengthGap"));
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

	public static boolean isTomcatContext() {
		return MessagesManager.getDefault("web.context.loader.as.string").equalsIgnoreCase(Thread
				.currentThread().getContextClassLoader().getClass().getName());
	}
}
