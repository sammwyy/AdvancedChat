package com.sammwy.advancedchat.utils;

import java.util.List;

public class ListUtils {
    public static boolean containsIgnoreCase(String string, List<String> list) {
        return list.stream().anyMatch(value -> value.equalsIgnoreCase(string));
    }
}
