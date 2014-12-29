package org.ginga.toolbox.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final SimpleDateFormat DATE_FORMAT_DATABASE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_LACDUMP = new SimpleDateFormat("yyMMdd HH:mm:s");
	public static final SimpleDateFormat DATE_FORMAT_FILE = new SimpleDateFormat("yyMMddHHmmss");
	public static final SimpleDateFormat DATE_FORMAT_INPUT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	 
	public static String format(SimpleDateFormat format, Date date) throws ParseException {
		return format(format.toPattern(), date);
	} 
	
	public static String format(String pattern, Date date) throws ParseException {
		return new SimpleDateFormat(pattern).format(date);
	} 
	
	public static Date parse(String pattern, String date) throws ParseException {
		return new SimpleDateFormat(pattern).parse(date);
	}
	
	public static String convertDatabaseToFileFormat(String date) throws ParseException {
		Date d = DATE_FORMAT_DATABASE.parse(date);
		return DATE_FORMAT_FILE.format(d);
	} 
	
	public static String formatInputDate(Date date) throws ParseException {
		return format(DATE_FORMAT_INPUT, date);
	}
	
	public static Date parseInputFormat(String date) throws ParseException {
		return DATE_FORMAT_INPUT.parse(date);
	}

	public static Date parseDatabaseFormat(String date) throws ParseException {
		return DATE_FORMAT_DATABASE.parse(date);
	}
	
	public static Date parseLacdumpFormat(String date) throws ParseException {
		return DATE_FORMAT_LACDUMP.parse(date);
	}
}
