package com.tinkabell.roman;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A roman number is a natural number expressed as roman numerals.
 * This implementation is therefore wraps an Integer,
 * but limited to only positive values (not zero),
 * and the parse function will accept a string of roman numerals.
 *
 * From Wikipedia:
 * Roman numerals are a numeral system that originated in ancient Rome and remained
 * the usual way of writing numbers throughout Europe well into the Late Middle Ages.
 * Numbers in this system are represented by combinations of letters from the Latin alphabet.
 * Modern style uses seven symbols, each with a fixed integer value:
 *
 * Symbol	I	V	X	L	C	D	M
 * Value	1	5	10	50	100	500	1000
 *
 * Analysis:
 * Given that there is no concept of zero, or even negative numbers
 * (it was after all a "tally" system), the smallest number is 1 and the
 * largest should be 4000-1 (as there is no '5' of thousands), but this
 * implementation will allow up to 9 units ('I', 'X', 'C' & 'M') to be
 * valid (if not correct) so gives a maximum of 10k-1.
 */
public class RomanNumber {

    private static final int MAX_INT_VALUE = 4000 - 1;
    private static final int MIN_INT_VALUE = 1;

    // the valid characters used to get an index
    private final static String numerals = "IVXLCDM";
    // the values of those characters referenced by that index
    private final static int[] values = {1, 5, 10, 50, 100, 500, 1000};

    private static int arabicValue(int c) throws NumberFormatException {
        int value;
        try {
            value = values[numerals.indexOf(c)];
        } catch (ArrayIndexOutOfBoundsException e){
            throw new NumberFormatException("Invalid character: " + (char) c);
        }
        return value;
    }

    /*
    private RomanNumber(int i)
            throws NumberFormatException{
        value = i;
        if (value < MIN_INT_VALUE || value > MAX_INT_VALUE) throw new NumberFormatException("Value out of range");
    }
     */

    /**
     * Parse the string argument as an unsigned roman number.
     *
     * The characters in the string must all be roman numerals ("IVXLCDM"),
     * but for convenience will accept lowercase ascii characters as well.
     * The 'five' numerals ("VLD") can only be singletons.
     * Numerals will usually be in descending numerical value, but we will
     * support "subtractive notation" as well as "additive notation" for input
     * and "subtractive notation" for output.
     *
     * @param s - a String containing the int representation to be parsed
     * @return the integer value represented by the argument in decimal
     * @throws NumberFormatException – if the string does not contain a parsable integer
     */
    public static int parse(@NotNull String s )
            throws NumberFormatException{
        /*
        Back to the drawing board and programming 101:
        KISS!
         */
        int value;
        String validated = s.trim().toUpperCase();
        // validate input only has valid characters
        /*
        if (validated.chars().anyMatch(a -> numerals.indexOf(a) == -1))
            throw new NumberFormatException(validated + " contains characters that are not roman numerals");

         */
        value = validated.chars().map(RomanNumber::arabicValue).sum();
        return value;
    }

    public static String prompt(Scanner scanner, String s){
        System.out.println(s);
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        /* Wanted to use Console, but not supported under IDEA!
        Console console = System.console();
        System.console() returns as null!
         */
        Scanner scanner = new Scanner(System.in);
        // System.out.println("Welcome to the Roman Number Translator");
        System.out.println("Enter an empty line to exit");
        String input = prompt(scanner, "Please enter a Roman Numeral");
        int number = RomanNumber.parse(input.trim());
        System.out.println(number);
        /*
        while (input.trim().length() > 0){
            int number = RomanNumber.parse(input.trim());
            System.out.println(number);
            input = prompt(scanner,"Please enter another Roman Numeral");
        }
             */
    }
}
