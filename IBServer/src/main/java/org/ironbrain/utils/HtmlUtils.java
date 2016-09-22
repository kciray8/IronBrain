package org.ironbrain.utils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

public class HtmlUtils {
    public static String getShortText(String html, int countChar) {
        String text = Jsoup.parse(html).text();

        //StringUtils - save for "out of range"
        return StringUtils.substring(text, 0, countChar - 1) + "...";
    }

    public static String getEscapedShortText(String html, int countChar) {
        return StringEscapeUtils.escapeHtml4(getShortText(html, countChar));
    }
}
