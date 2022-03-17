package com.tinkabell.roman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RomanNumberTest {

    @Test
    public void checkParseRoman(){
        // Arrange
        RomanNumber number = RomanNumber.parseRoman("1");

        // Act
        int result = 1;

        // Assert
        assertEquals(result, number.value);
    }

}