package com.acme.spreadsheet;

import com.acme.exception.ParseException;
import com.acme.exception.SpreadsheetParseException;
import com.acme.model.*;
import com.acme.util.MyUtil;

public class ValueParser {

    private MyObject processList(String s) {
        if (s.endsWith("]")) {
            var rest = s.substring(1, s.length() - 1);
            if (rest.startsWith("'")) {
                return new MyStringList(rest);
            } else {
                return new MyNumberList(rest);
            }
        } else {
            throw new ParseException("Missing closing square bracket for list definition");
        }
    }

    private MyObject processValue(String value) {
        if (MyUtil.isNumeric(value)) {
            return new MyNumber(value);
        } else if (value.startsWith("'")) {
            return new MyString(value);
        } else if (value.startsWith("[")) {
            return processList(value);
        } else if (value.equals("null")) {
            return new MyNull();
        }
        throw new SpreadsheetParseException("Unknown value detected:%s".formatted(value));
    }

    public MyObject covertInputString(String s) {
        if (s.startsWith("not ")) {
            var rest = s.substring("not ".length());
            var value = processValue(rest);
            value.setNegate(true);
            return value;
        } else {
            var value = processValue(s);
            return value;
        }
    }
}
