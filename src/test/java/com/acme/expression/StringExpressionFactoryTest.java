package com.acme.expression;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class StringExpressionFactoryTest {

    private StringExpressionFactory expressionFactory = new StringExpressionFactory();

    @Test
    void test_simpleObject_numericProperty_equalTo() {
        var notionalValue = 100.0f;
        var exp = expressionFactory.equalTo("notional", notionalValue);

        assertThat(exp, is(equalTo("notional == 100.0")));
    }

    @Test
    void test_simpleObject_numericProperty_notEqualTo() {
        var notionalValue = 100.0f;
        var exp = expressionFactory.notEqualTo("notional", notionalValue);

        assertThat(exp, is(equalTo("notional != 100.0")));
    }

    @Test
    void test_nestedObject_stringProperty_equalTo() {
        var underlyingType = "Asian";
        var exp = expressionFactory.equalTo("underlying.type", underlyingType);

        assertThat(exp, is(equalTo("underlying.type == 'Asian'")));
    }

    @Test
    void test_nestedObject_stringProperty_notEqualTo() {
        var underlyingType = "Asian";
        var exp = expressionFactory.notEqualTo("underlying.type", underlyingType);

        assertThat(exp, is(equalTo("underlying.type != 'Asian'")));
    }

    @Test
    void test_simpleObject_nullProperty_isNull() {
        var exp = expressionFactory.isNull("notional");

        assertThat(exp, is(equalTo("notional eq null")));
    }

    @Test
    void test_simpleObject_notNullProperty_isNotNull() {
        var exp = expressionFactory.isNotNull("underlying");

        assertThat(exp, is(equalTo("underlying ne null")));
    }

    @Test
    void test_simpleObject_numericProperty_inList() {
        var exp = expressionFactory.inNumericList("notional", List.of("1", "2"));

        assertThat(exp, is(equalTo("{1, 2}.contains(notional)")));
    }

    @Test
    void test_simpleObject_numericProperty_notInList() {
        var exp = expressionFactory.notInNumericList("notional", List.of("1", "2"));

        assertThat(exp, is(equalTo("!{1, 2}.contains(notional)")));
    }

    @Test
    void test_simpleObject_stringProperty_inList() {
        var exp = expressionFactory.inStringList("type", List.of("Bond", "Stock"));

        assertThat(exp, is(equalTo("{'Bond', 'Stock'}.contains(type)")));
    }

    @Test
    void test_simpleObject_stringProperty_notInList() {
        var exp = expressionFactory.notInStringList("type", List.of("Option", "Future"));

        assertThat(exp, is(equalTo("!{'Option', 'Future'}.contains(type)")));
    }

}
