package com.tinkabell.roman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RomanNumeralTest {

    @Test
    public void checkCase1(){
        Assertions.assertEquals(1, RomanNumeral.parse("I"));
        Assertions.assertEquals(1, RomanNumeral.parse(" i "));
    }

    @Test
    public void checkCase2(){
        Assertions.assertEquals(2, RomanNumeral.parse("II"));
        Assertions.assertEquals(2, RomanNumeral.parse(" iI "));
    }

    @Test
    public void checkInvalid(){
        NumberFormatException thrown = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumeral.parse("z"),
                "NumberFormatException was expected");
    }


    @Test
    public void checkOnes(){
        // Arrange
        String [] ones = {
                "I",
                "I".repeat(2),
                "i".repeat(3),
                "Ii".repeat(2),
                "i".repeat(5),
                "I".repeat(6),
                "i".repeat(7),
                "I".repeat(8),
                "i".repeat(9),
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
            results[i] = RomanNumeral.parse(ones[i]);
        }

        // Assert
        for (int i = 0; i < ones.length; i++) {
            Assertions.assertEquals(expected[i], results[i]);
        }
    }

    @Test
    public void checkBadOnes(){
        NumberFormatException thrown2 = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumeral.parse(" "),
                "NumberFormatException was expected as nothing is an error!");

        NumberFormatException thrown = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumeral.parse("I".repeat(10)),
                "NumberFormatException was expected as 10 'I's should not be valid!");
    }

    @Test
    public void checkBadFiveAndTens(){
        NumberFormatException thrown2 = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumeral.parse("XXL"),
                "NumberFormatException was expected as can't precede a four!");

        NumberFormatException thrown = Assertions.assertThrows(NumberFormatException.class,
                () -> RomanNumeral.parse("CCM"),
                "NumberFormatException was expected as can't precede a nine!");
    }

    @Test
    public void checkUnits() {
        // Arrange
        String [] roman = {
                "I",
                "II",
                "III",
                "IV",
                "V",
                "VI",
                "VII",
                "VIII",
                "IX"
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
        int[] results = new int[roman.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumeral.parse(roman[i]);
        }

        // Assert
        for (int i = 0; i < roman.length; i++) {
            Assertions.assertEquals(expected[i], results[i], "For: " + roman[i]);
        }
    }

    @Test
    public void checkTens() {
        // Arrange
        String [] roman = {
                "X",
                "XX",
                "xxx",
                "XL",
                "l",
                "LX",
                "lXx",
                "LxXx",
                "XC"
        };
        int [] expected = {
                10,
                20,
                30,
                40,
                50,
                60,
                70,
                80,
                90
        };

        // Act
        int[] results = new int[roman.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumeral.parse(roman[i]);
        }

        // Assert
        for (int i = 0; i < roman.length; i++) {
            Assertions.assertEquals(expected[i], results[i], "For: " + roman[i]);
        }
    }

    @Test
    public void checkHundreds() {
        // Arrange
        String [] roman = {
                "C",
                "cC",
                "CcC",
                "cD",
                "d",
                "Dc",
                "dCc",
                "DcCc",
                "CM"
        };
        int [] expected = {
                100,
                200,
                300,
                400,
                500,
                600,
                700,
                800,
                900
        };

        // Act
        int[] results = new int[roman.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumeral.parse(roman[i]);
        }

        // Assert
        for (int i = 0; i < roman.length; i++) {
            Assertions.assertEquals(expected[i], results[i], "For: " + roman[i]);
        }
    }

    @Test
    public void checkValidParse() {
        // Arrange
        String [] roman = {
                "CXIV",
                "CDXLIV",
                "CMXCIX",
                "MMXXI",
                "MCMLIX"
        };
        int [] expected = {
                114,
                444,
                999,
                2021,
                1959
        };

        // Act
        int[] results = new int[roman.length];
        for (int i = 0; i < results.length; i++) {
            results[i] = RomanNumeral.parse(roman[i]);
        }

        // Assert
        for (int i = 0; i < roman.length; i++) {
            Assertions.assertEquals(expected[i], results[i], "For: " + roman[i]);
        }
    }

    @Test
    public void checkTheBigOne(){
        Assertions.assertEquals(9999, RomanNumeral.parse("MMMMMMMMMCMXCIX"));
    }

}
