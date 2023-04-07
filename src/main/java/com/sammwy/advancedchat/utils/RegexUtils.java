package com.sammwy.advancedchat.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private static String URL_EXP = "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$";
    private static Pattern URL_REGEX = Pattern.compile(URL_EXP);

    public static boolean matchURL(String text, List<String> whitelist) {
        Matcher matcher = URL_REGEX.matcher(text);
        return matcher.results().anyMatch((result) -> {
            return ListUtils.containsIgnoreCase(result.group(), whitelist);
        });
    }
}
