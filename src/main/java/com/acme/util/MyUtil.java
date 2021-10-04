package com.acme.util;

public class MyUtil {

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumericType(Object x) {
        var type = x.getClass();
        return (type == Double.class ||
                type == Float.class ||
                type == Long.class ||
                type == Integer.class ||
                type == Short.class ||
                type == Byte.class ||
                type == Boolean.class);
    }

    public static boolean isStringType(Object x) {
        var type = x.getClass();
        return (type == String.class || type == Character.class);
    }
}
