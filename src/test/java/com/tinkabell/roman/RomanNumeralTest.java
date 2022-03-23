package com.tinkabell.roman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
            "I, 1",
            "II, 2",
            "iii, 3",
            "IiIi, 4",
            "iiiii, 5",
            "IIIIII, 6",
            "iiiiiii, 7",
            "IIIIIIII, 8",
            "iiiiiiiii, 9"
    })
    public void checkOnes(String roman, int expected){
        Assertions.assertEquals(expected, RomanNumeral.parse(roman));
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

    @ParameterizedTest
    @CsvSource({
            "I, 1",
            "II, 2",
            "iii, 3",
            "iv, 4",
            "V, 5",
            "VI, 6",
            "vii, 7",
            "vIII, 8",
            "iX, 9"
    })
    public void checkUnits(String roman, int expected){
        Assertions.assertEquals(expected, RomanNumeral.parse(roman));
    }

    @ParameterizedTest
    @CsvSource({
            "X, 10",
            "XX, 20",
            "xxx, 30",
            "XL, 40",
            "l, 50",
            "LX, 60",
            "lXx, 70",
            "LxXx, 80",
            "XC, 90"
    })
    public void checkTens(String roman, int expected){
        Assertions.assertEquals(expected, RomanNumeral.parse(roman));
    }

    @ParameterizedTest
    @CsvSource({
            "C, 100",
            "cC, 200",
            "CcC, 300",
            "cD, 400",
            "d, 500",
            "Dc, 600",
            "dCc, 700",
            "DcCc, 800",
            "CM, 900"
    })
    public void checkHundreds(String roman, int expected){
        Assertions.assertEquals(expected, RomanNumeral.parse(roman));
    }

    @ParameterizedTest
    @CsvSource({
            "CXIV, 114",
            "CDXLIV, 444",
            "CMXCIX, 999",
            "MMXXI, 2021",
            "MCMLIX, 1959"
    })
    public void checkValidParse(String roman, int expected){
        Assertions.assertEquals(expected, RomanNumeral.parse(roman));
    }

    @Test
    public void checkTheBigOne(){
        Assertions.assertEquals(9999, RomanNumeral.parse("MMMMMMMMMCMXCIX"));
    }

}
