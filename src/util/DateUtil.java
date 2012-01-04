package util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static String DATE_FORMAT="dd/MM/yyyy";
	
	public static Date addDaysToDate(Date d, int days){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, days);
		
		return c.getTime();
	}
	
	public static int getDateDayDifference(Date d1, Date d2){
		Calendar date = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		date.setTime(d1);
		endDate.setTime(d2);
		int daysBetween = 0;  
		while (date.before(endDate)) {  
			date.add(Calendar.DATE, 1);  
		    daysBetween++;  
		}  
		return daysBetween;  
	}
}
