package com.acme.spreadsheet;

import com.acme.exception.SpreadsheetParseException;
import com.acme.expression.StringExpressionFactory;
import com.acme.model.*;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpreadsheetParser {

    private final ValueParser valueParser;
    private final InputStream inputStream;
    private final String targetColumn;

    public SpreadsheetParser(InputStream inputStream, String targetColumn) {
        this.inputStream = inputStream;
        this.targetColumn = targetColumn;
        this.valueParser = new ValueParser();
    }

    // Order of rules is important
    public LinkedHashMap<Expression, String> parse() {
        var reader = new SpreadsheetReader(inputStream);
        var factory = new StringExpressionFactory();

        var rowsList = reader.read();
        var expressionTable = new LinkedHashMap<Expression, String>();
        var expressionParser = new SpelExpressionParser();
        for (var row : rowsList) {
            var rowResult = parseRow(row);
            var exps = convertPartsToExpressionList(factory, rowResult.parts);
            String fullExpression = String.join(" and ", exps);
            // if we have nothing as final expression it should be last effective row - always return true
            var expression = !fullExpression.equals("")
                    ? expressionParser.parseExpression(fullExpression)
                    : expressionParser.parseExpression("true");
            expressionTable.put(expression, rowResult.target);
        }
        return expressionTable;
    }

    private RowResult parseRow(Map<String, String> row) {
        var parts = new LinkedHashMap<String, MyObject>();
        String target = null;
        for (Map.Entry<String, String> entry : row.entrySet()) {
            if (entry.getKey().equals(this.targetColumn)) {
                target = entry.getValue();
            } else {
                var value = entry.getValue();
                // empty cells are not added to result
                if (value != null && !value.equals("")) {
                    var cell = this.valueParser.covertInputString(value);
                    parts.put(entry.getKey(), cell);
                }
            }
        }
        if (target == null) {
            throw new SpreadsheetParseException("Could not find targetColumn:%s in row".formatted(this.targetColumn));
        }
        return new RowResult(target, parts);
    }

    private List<String> convertPartsToExpressionList(StringExpressionFactory factory, Map<String, MyObject> parts) {
        var exps = new ArrayList<String>();
        for (var part : parts.entrySet()) {
            var accessor = part.getKey();
            var myObj = part.getValue();

            if (myObj instanceof MyNull) {
                exps.add(myObj.isNegate()
                        ? factory.isNotNull(accessor)
                        : factory.isNull(accessor));
            } else if (myObj instanceof MyNumber) {
                var value = ((MyNumber) myObj).getValue();
                exps.add(myObj.isNegate()
                        ? factory.notEqualTo(accessor, value)
                        : factory.equalTo(accessor, value));
            } else if (myObj instanceof MyString) {
                var value = ((MyString) myObj).getValue();
                exps.add(myObj.isNegate()
                        ? factory.notEqualTo(accessor, value)
                        : factory.equalTo(accessor, value));
            } else if (myObj instanceof MyNumberList) {
                var values = ((MyNumberList) myObj).getValues();
                exps.add(myObj.isNegate()
                        ? factory.notInNumericList(accessor, values)
                        : factory.inNumericList(accessor, values));
            } else if (myObj instanceof MyStringList) {
                var values = ((MyStringList) myObj).getValues();
                exps.add(myObj.isNegate()
                        ? factory.notInStringList(accessor, values)
                        : factory.inStringList(accessor, values));
            } else {
                throw new SpreadsheetParseException("Unknown object type detected: %s".formatted(myObj.getClass()));
            }
        }
        return exps;
    }

    record RowResult(String target, Map<String, MyObject> parts) {
    }
}
