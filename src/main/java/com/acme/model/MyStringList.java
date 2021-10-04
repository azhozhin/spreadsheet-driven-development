package com.acme.model;

import com.acme.exception.ParseException;

import java.util.Arrays;
import java.util.List;

public class MyStringList extends MyObject {

    private List<String> values;

    // Input expected 'A','B','C'
    public MyStringList(String value) {
        this.values = Arrays.stream(value.split(",")).map(v -> {
            var element = v.strip();
            if (element.startsWith("'") && element.endsWith("'")) {
                if (element.length() > 2) {
                    return element.substring(1, element.length() - 1);
                } else {
                    throw new ParseException("Empty list elements are not allowed");
                }
            } else {
                throw new ParseException("Every element in the string list should be enclosed in single quotes");
            }
        }).toList();
    }

    public List<String> getValues() {
        return values;
    }
}
