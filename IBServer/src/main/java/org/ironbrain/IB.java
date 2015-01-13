package org.ironbrain;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;

@Component
public class IB {
    //For testing purpose
    private static long msOffset = 0;

    public static Random rand() {
        return random;
    }

    public static void setRandom(Random random) {
        IB.random = random;
    }

    private static Random random = new Random();

    public static void setMsOffset(long msOffset) {
        IB.msOffset = msOffset;
    }

    public static long getNowMs() {
        return System.currentTimeMillis() + msOffset;
    }

    public static Calendar getNowCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getNowMs());

        return calendar;
    }
}
