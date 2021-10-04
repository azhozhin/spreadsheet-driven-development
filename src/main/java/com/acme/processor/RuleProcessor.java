package com.acme.processor;

import com.acme.exception.RuleProcessorException;
import org.springframework.expression.Expression;

import java.util.LinkedHashMap;

public class RuleProcessor {

    private LinkedHashMap<Expression, String> rules;

    public RuleProcessor(LinkedHashMap<Expression, String> rules) {
        this.rules = rules;
    }

    public String process(Object obj) {
        for (var entry : this.rules.entrySet()) {
            var expression = entry.getKey();
            var result = (boolean) expression.getValue(obj);
            if (result) {
                return entry.getValue();
            }
        }
        throw new RuleProcessorException("No rules matched input");
    }
}
