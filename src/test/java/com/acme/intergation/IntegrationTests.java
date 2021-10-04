package com.acme.intergation;

import com.acme.SpreadSheetDrivenDevelopment;
import com.acme.dto.Trade;
import com.acme.dto.Underlying;
import com.acme.processor.RuleProcessor;
import com.acme.spreadsheet.SpreadsheetParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationTests {

    RuleProcessor ruleProcessor;

    @BeforeAll
    void setup() {
        var filename = "classification.csv";
        var stream = SpreadSheetDrivenDevelopment.class.getClassLoader().getResourceAsStream(filename);
        var spreadsheetParser = new SpreadsheetParser(stream, "targetType");
        var rules = spreadsheetParser.parse();
        ruleProcessor = new RuleProcessor(rules);
    }

    // Bonds
    @Test
    void tradeWithRateNotionalExpiration_shouldBeFixedRateBond() {
        var notionalValue = 100.0f;
        var trade = Trade.builder()
                .rate(0.01f)
                .notional(notionalValue)
                .expiration(new Date(2021, 1, 1))
                .build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("FixedRateBond")));
    }

    @Test
    void tradeWithNotionalIndexExpiration_shouldBeFloatingRateBond() {
        var notionalValue = 100.0f;
        var trade = Trade.builder()
                .notional(notionalValue)
                .index("LIBOR")
                .expiration(new Date(2021, 1, 1))
                .build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("FloatingRateBond")));
    }

    @Test
    void tradeWithNotionalExpiration_shouldBeZeroCouponeBond() {
        var notionalValue = 100.0f;
        var trade = Trade.builder()
                .notional(notionalValue)
                .expiration(new Date(2021, 1, 1))
                .build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("ZeroCouponeBond")));
    }

    @Test
    void tradeWithNotional_shouldBeFallbackBond() {
        var notionalValue = 100.0f;
        var trade = Trade.builder().notional(notionalValue).build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("FallbackBond")));
    }

    // Option
    @Test
    void tradeWithAsianUnderlying_shouldBeAsianOption() {
        Underlying underlying = Underlying.builder().type("Asian").build();
        var trade = Trade.builder().underlying(underlying).build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("AsianOption")));
    }

    @Test
    void tradeWithAmericanUnderlying_shouldBeAmericanOption() {
        Underlying underlying = Underlying.builder().type("American").build();
        var trade = Trade.builder().underlying(underlying).build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("AmericanOption")));
    }

    @Test
    void tradeWithEuropeanUnderlying_shouldBeEuropeanOption() {
        Underlying underlying = Underlying.builder().type("European").build();
        var trade = Trade.builder().underlying(underlying).build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("EuropeanOption")));
    }

    @Test
    void tradeWithOtherUnderlying_shouldBeFuture() {
        Underlying underlying = Underlying.builder().type("Other").build();
        var trade = Trade.builder().underlying(underlying).build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("Future")));
    }

    @Test
    void tradeWithCallPutWithoutUnderlying_shouldBeFallbackOption() {
        var trade = Trade.builder().callOrPut("Call").build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("FallbackOption")));
    }

    // Global fallback
    @Test
    void emptyTrade_shouldGloballyFallbackToStock() {
        var trade = Trade.builder().build();
        var target = ruleProcessor.process(trade);

        assertThat(target, is(equalTo("Stock")));
    }
}
