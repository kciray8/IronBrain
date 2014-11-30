package org.ironbrain.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getNiceDate(long ms){
        Date date = new Date(ms);
        String formattedDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
        return formattedDate;
    }
}

