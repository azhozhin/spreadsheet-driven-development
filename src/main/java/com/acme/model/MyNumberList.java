package com.acme.model;

import com.acme.exception.ParseException;
import com.acme.util.MyUtil;

import java.util.Arrays;
import java.util.List;

public class MyNumberList extends MyObject {

    private List<String> values;

    // Input expected 1, 2, 3
    public MyNumberList(String value) {
        this.values = Arrays.stream(value.split(",")).map(v -> {
            var element = v.strip();
            if (!MyUtil.isNumeric(element)){
                throw new ParseException("Number list should not have only numeric elements");
            }
            return element.substring(1, element.length() - 1);
        }).toList();
    }

    public List<String> getValues() {
        return values;
    }
}
