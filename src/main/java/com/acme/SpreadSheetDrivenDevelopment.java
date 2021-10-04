package com.acme;

import com.acme.dto.Trade;
import com.acme.processor.RuleProcessor;
import com.acme.spreadsheet.SpreadsheetParser;

public class SpreadSheetDrivenDevelopment {

    public static void main(String[] args) {
        var filename = "classification.csv";
        var stream = SpreadSheetDrivenDevelopment.class.getClassLoader().getResourceAsStream(filename);
        var spreadsheetParser = new SpreadsheetParser(stream, "targetType");
        var rules = spreadsheetParser.parse();

        var ruleProcessor = new RuleProcessor(rules);

        var notionalValue = 100.0f;
        // Trade with not-null notional value should be classified as FallbackBond
        var trade = Trade.builder().notional(notionalValue).build();
        var target = ruleProcessor.process(trade);
        System.out.println("Trade with non-empty notional: '%s' should be 'FallbackBond'".formatted(target));

    }
}

