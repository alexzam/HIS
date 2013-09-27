package az.his;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {
    public static Calendar createCalDate1() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 1);
        moveToMidnight(calendar);
        return calendar;
    }

    public static Calendar convertInDateParam(Long rawFrom) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Baku"));  // TODO Set TZ somewhere else

        Calendar calendar = new GregorianCalendar();
        if (rawFrom == null || rawFrom == 0) {
            return null;
        }
        calendar.setTimeInMillis(rawFrom);
        moveToMidnight(calendar);
        return calendar;
    }

    public static void moveToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void moveToMonday(Calendar cal){
        int offset = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
        if (offset < 0) offset += 7;
        cal.add(Calendar.DATE, -offset);
    }

    public static void moveToFirstOfMonth(Calendar cal){
        moveToMidnight(cal);
        cal.set(Calendar.DAY_OF_MONTH, 1);
    }
}
