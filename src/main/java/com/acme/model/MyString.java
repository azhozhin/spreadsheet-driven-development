package com.acme.model;

import com.acme.exception.ParseException;

public class MyString extends MyObject {

    private String value;

    public MyString(String value) {
        // we need to strip single quotes
        if (value.startsWith("'")){
            if (value.endsWith("'")){
                this.value = value.substring(1, value.length()-1);
            } else {
                throw new ParseException("String value does not have matching quote");
            }
        } else {
            throw new ParseException("String value should be surrounded by single quotes");
        }
    }

    public String getValue() {
        return value;
    }
}
