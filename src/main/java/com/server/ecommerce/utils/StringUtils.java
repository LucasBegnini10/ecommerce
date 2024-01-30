package com.server.ecommerce.utils;

public class StringUtils {

    public static boolean isNullOrBlank(String text){
        return (text == null) || (text.isBlank());
    }

    public static boolean isNotNullAndNotBlank(String text){
        return !isNullOrBlank(text);
    }
}
