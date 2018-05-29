package com.example.services.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface DateHelper {
    static boolean isModifiedOnCurrentDate(final Date lastModified) {
        final Date currentDate = new Date();
        return isSameDay(lastModified, currentDate);
    }

    static boolean isSameDay(final Date lastModified, final Date currentDate) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        System.out.println("modifiedDate: " + sdf.format(lastModified));
//        System.out.println("currentDate: " + sdf.format(currentDate));
//        System.out.println(sdf.format(lastModified).compareTo(sdf.format(currentDate)) == 0);
        return sdf.format(lastModified).compareTo(sdf.format(currentDate)) == 0;
    }

    static boolean isModifiedInGivenMinutes(final Date lastModified, Long modifiedDeltaDuration) {
        final Date currentDate = new Date();
        final long dateDiff = currentDate.getTime() - lastModified.getTime();
        final long dateDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
        return dateDiffInMinutes <= modifiedDeltaDuration;
    }

    static String getCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(calendar.getTime());
    }
}
