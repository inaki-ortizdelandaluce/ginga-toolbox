package org.ginga.tools.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final SimpleDateFormat DATE_FORMAT_DATABASE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_LACDUMP = new SimpleDateFormat("yyMMdd HH:mm:s");
	public static final SimpleDateFormat DATE_FORMAT_FILE = new SimpleDateFormat("yyMMddHHmmss");
	 
	public static String convertDatabaseToFileFormat(String date) throws ParseException {
		Date d = DATE_FORMAT_DATABASE.parse(date);
		return DATE_FORMAT_FILE.format(d);
	} 
	
	public static Date parseDatabaseFormat(String date) throws ParseException {
		return DATE_FORMAT_DATABASE.parse(date);
	}
}
