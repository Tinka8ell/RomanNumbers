package com.tinkabell.roman;

import org.junit.jupiter.api.Test;

import static com.tinkabell.roman.RomanNumber.parseRoman;
import static org.junit.jupiter.api.Assertions.*;

class RomanNumberTest {

    @Test
    public void checkOnes(){
        // Arrange
        String [] ones = {
                "I",
                "II",
                "III",
                "IIII"
        };
        String [] expected = {
                "1",
                "2",
                "3",
                "4,"
        };

        // Act
        String[] results = new String[ones.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = parseRoman(ones[i]).asArabic();
        }

        // Assert
        for (int i = 0; i < ones.length; i++) {
            assertEquals(expected[i], results[i]);
        }
    }

    @Test
    public void checkParseRoman() {
        // Arrange
        RomanNumber number = parseRoman("1");

        // Act
        int result = 1;

        // Assert
        assertEquals(result, number.value);
    }

    @Test
    public void checkAsRoman() {
        // Arrange
        String [] numbers = {
                "1",
                "3",
                "4",
                "5",
                "8",
                "9",
                "10",
                "2021",
                "1959"
        };
        String [] expected = {
                "I",
                "III",
                "IV",
                "V",
                "VIII",
                "IX",
                "X",
                "MMXXI",
                "MCMLIX"
        };

        // Act
        String[] results = new String[numbers.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = parseRoman(numbers[i]).asRoman();
        }

        // Assert
        for (int i = 0; i < numbers.length; i++) {
            assertEquals(expected[i], results[i]);
        }
    }

    @Test
    public void checkAsArabic() {
        // Arrange
        String [] numbers = {
                "I",
                "III",
                "IV",
                "V",
                "VIII",
                "IX",
                "X",
                "MMXXI",
                "MCMLIX"
        };
        String [] expected = {
                "1",
                "3",
                "4",
                "5",
                "8",
                "9",
                "10",
                "2021",
                "1959"
        };

        // Act
        String[] results = new String[numbers.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = parseRoman(numbers[i]).asArabic();
        }

        // Assert
        for (int i = 0; i < numbers.length; i++) {
            assertEquals(expected[i], results[i]);
        }
    }
}