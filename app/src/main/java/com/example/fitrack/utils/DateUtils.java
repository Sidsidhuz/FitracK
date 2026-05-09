package com.example.fitrack.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final SimpleDateFormat STORAGE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private static final SimpleDateFormat HUMAN_FORMAT = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    private DateUtils() {
    }

    public static String today() {
        return STORAGE_FORMAT.format(new Date());
    }

    public static String humanDate(String storageDate) {
        try {
            Date date = STORAGE_FORMAT.parse(storageDate);
            return date == null ? storageDate : HUMAN_FORMAT.format(date);
        } catch (ParseException e) {
            return storageDate;
        }
    }

    public static long toTime(String storageDate) {
        try {
            Date date = STORAGE_FORMAT.parse(storageDate);
            return date == null ? 0L : date.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }
}

