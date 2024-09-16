package com.example.server.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    @Test
    void whenCalculate_returnsCorrectValue() {
        // Given
        // When
        var result = Calculator.calculate(1, 2);
        // Then
        assertEquals(result, 3);
    }
}
