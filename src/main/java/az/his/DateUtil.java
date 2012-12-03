package az.his;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {
    public static Calendar createCalDate1() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 1);
        calToMidnight(calendar);
        return calendar;
    }

    public static Calendar convertInDateParam(Long rawFrom) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Baku"));  // TODO Set TZ somewhere else

        Calendar calendar = new GregorianCalendar();
        if (rawFrom == null || rawFrom == 0) {
            return null;
        }
        calendar.setTimeInMillis(rawFrom);
        calToMidnight(calendar);
        return calendar;
    }

    public static void calToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
}
