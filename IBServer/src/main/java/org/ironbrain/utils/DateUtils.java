package org.ironbrain.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils{

    public static String getNiceDate(long ms){
        Date date = new Date(ms);
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(date);
    }

    public static String getUniqueFileName(){
        return new SimpleDateFormat("yyyy_MM_dd__HH_mm_ss_SSS").format(new Date(System.currentTimeMillis()));
    }
}

