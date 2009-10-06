package org.supposition.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.supposition.db.Ads;
/**
 * 
 * @author M.Nurullayev
 * 
 */
public final class Utils {
	public static Log _log = LogFactory.getLog("org.supposition.utils.Utils");

	public static final String ROOT_ID_DEF = "root";
	public static final String INPUT_BUTTON_TEMPLATE = "<input type=\"button\" onclick=\"%s\"  value=\"%s\"/>";
	public static final String LINK_TEMPLATE = "<a href=\"#\" id=\"%s\" onclick=\"javascript:void(%s)\">%s</a>";
	
	public static String getWwwBlankLink(String inLink){
		if(inLink == null 
				|| inLink.isEmpty() 
				|| inLink.equals("http://"))
			return "";		
		return String.format("<a href=\"%s\" target=\"_blank\">www</a>", inLink);
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

    public static final Comparator<Ads> ADS_DEFAULT_ORDER = new Comparator<Ads>() {
    	public int compare(Ads e1, Ads e2) {
    		
    		return getUniqueDateTime(e2.getCreated()).compareTo(getUniqueDateTime(e1.getCreated()));
    	}
};



	public static String getHTMLSelectCity4(String inPrefix) {
		String result = String.format(MessagesManager.getText("html.select.cities.header"), inPrefix);
		result += MessagesManager.getText("html.select.cities.body");
		result += MessagesManager.getText("html.select.cities.footer");
		return result;
	}



}
