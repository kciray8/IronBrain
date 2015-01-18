package org.ironbrain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

@Component
public class IB {
    @Autowired
    private ServletContext context;

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

    public static Calendar getNowCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getNowMs());

        return calendar;
    }

    public String getVersionStr() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null) {//Tomcat bug fix
            Properties prop = new Properties();
            try {
                prop.load(context.getResourceAsStream("/META-INF/MANIFEST.MF"));
                version = prop.getProperty("Implementation-Version");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return version;
    }
}
