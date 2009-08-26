package org.supposition.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author M.Nurullayev
 * 
 */
public final class Constants {
	// **** BEAN Constants
	public static final String _beans_main_input_pipe = "_main_input_pipe";
	public static final String _beans_main_message_collector = "_main_message_collector";
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
	public static final String _dispatchers_posfix = "Dispatcher";
	public static final String _dispatchers_back_path = "org.supposition.dispatchers.back.";
	public static final String _dispatchers_front_path = "org.supposition.dispatchers.front.";
	public static final String _db_objects_package = "org.supposition.db";
	public static final String _file_name_delimiter = "_";
	public static final String _beans_text_manager = "textManager";
	public static final String _db_map_name = "supposition";
	public static final int _min_password_length = 4;
	public static final String _password_salted = "SALTED";
	public static final String _web_error_result_prefix = "ERROR:";
	public static String _web_ok_result_prefix = "OK:";
	public static String _string_userId = "userId";
	public static int _default_id_for_new_dbo = -1;
	public static String _page_size_def = ".pageSize";
	public static String _current_page_def = ".currentPage";
	public static String _page_count_def =  ".pageCount";
	public static String _go2Page_def = ".go2Page(%s)";
	public static String _page_density_def = ".pageDencity";
	public static String _set_page_density_def = ".setPageDencity()";

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

	public static boolean isValidString(String str) {
		if ((str == null) || str.matches("^\\s*$")) {
			return false;
		} else {
			return true;
		}
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

	public static String getDefaultDispatcherName(String messageClassName) {
		return _dispatchers_back_path + messageClassName + _dispatchers_posfix;
	}

	public static String getDefaultFrontDispatcherName(String messageClassName) {
		return _dispatchers_front_path + messageClassName + _dispatchers_posfix;
	}

	public static String translate(String string) {
		return string;
	}

	public static int getIntFromStr(String text) {
		int result = -1;
		try {
			result = Integer.parseInt(text.trim());			
		} catch (NumberFormatException e) {
		}
		return result;
	}

}
