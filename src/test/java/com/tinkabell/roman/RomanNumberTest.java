package com.tinkabell.roman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.NumberFormatException;

class RomanNumberTest {

    @Test
    public void checkCase1(){
        Assertions.assertEquals(1, RomanNumber.parse("I"));
        Assertions.assertEquals(1, RomanNumber.parse(" i "));
    }

    @Test
    public void checkCase2(){
        Assertions.assertEquals(2, RomanNumber.parse("II"));
        Assertions.assertEquals(2, RomanNumber.parse(" iI "));
    }

    @Test
    public void checkInvalid(){
        NumberFormatException thrown = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumber.parse("z"),
                "NumberFormatException was expected");
    }


    @Test
    @Disabled
    public void checkOnes(){
        // Arrange
        String [] ones = {
                "I",
                "II",
                "III",
                "IIII",
                "iiiii",
                "IiIiIi",
                "iiIIiiIIi",
                "IIiiIIiiII",
                "iIiIiIiIiIi"
        };
        int [] expected = {
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9
        };

        // Act
        int[] results = new int[ones.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumber.parse(ones[i]);
        }

        // Assert
        for (int i = 0; i < ones.length; i++) {
            Assertions.assertEquals(expected[i], results[i]);
        }
    }

    @Test
    @Disabled
    public void checkAsArabic() {
        // Arrange
        String [] roman = {
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
        int [] expected = {
                1,
                3,
                4,
                5,
                8,
                9,
                10,
                2021,
                1959
        };

        // Act
        int[] results = new int[roman.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumber.parse(roman[i]);
        }

        // Assert
        for (int i = 0; i < roman.length; i++) {
            Assertions.assertEquals(expected[i], results[i]);
        }
    }
}