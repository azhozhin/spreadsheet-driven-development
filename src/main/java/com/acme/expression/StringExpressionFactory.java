package com.acme.expression;

import com.acme.util.MyUtil;

import java.util.List;

public class StringExpressionFactory {

    public StringExpressionFactory() {
    }

    private String escapeSingleValue(Object value) {
        String stringValue;
        if (MyUtil.isNumericType(value)) {
            stringValue = value.toString();
        } else if (MyUtil.isStringType(value)) {
            stringValue = "'%s'".formatted(value);
        } else {
            throw new IllegalArgumentException("value should be primitive or string type");
        }
        return stringValue;
    }

    private List<String> escapeValueList(List<String> list) {
        return list.stream()
                .map(this::escapeSingleValue)
                .toList();
    }

    public String equalTo(String accessor, Object value) {
        var stringValue = escapeSingleValue(value);
        return "%s == %s".formatted(accessor, stringValue);
    }

    public String notEqualTo(String accessor, Object value) {
        var stringValue = escapeSingleValue(value);
        return "%s != %s".formatted(accessor, stringValue);
    }

    public String isNull(String accessor) {
        return "%s eq null".formatted(accessor);
    }

    public String isNotNull(String accessor) {
        return "%s ne null".formatted(accessor);
    }

    public String inStringList(String accessor, List<String> list) {
        var escapedValueListStr = String.join(", ", escapeValueList(list));
        return "{%s}.contains(%s)".formatted(escapedValueListStr, accessor);
    }

    public String inNumericList(String accessor, List<String> list) {
        var escapedValueListStr = String.join(", ", list);
        return "{%s}.contains(%s)".formatted(escapedValueListStr, accessor);
    }

    public String notInStringList(String accessor, List<String> list) {
        var escapedValueListStr = String.join(", ", escapeValueList(list));
        return "!{%s}.contains(%s)".formatted(escapedValueListStr, accessor);
    }

    public String notInNumericList(String accessor, List<String> list) {
        var escapedValueListStr = String.join(", ", list);
        return "!{%s}.contains(%s)".formatted(escapedValueListStr, accessor);
    }
}
