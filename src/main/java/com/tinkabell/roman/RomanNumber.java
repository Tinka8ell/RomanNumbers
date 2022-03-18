package com.tinkabell.roman;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final int MAX_INT_VALUE = 10000 - 1;
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
        List<Integer> translatedNumerals = validated.chars()
                .map(RomanNumber::arabicValue) // turn numerals into ints
                .boxed()// turn ints into integers
                .collect(Collectors.toList()); // create a mutable list
        value = swallowNextDigit(translatedNumerals, 1000);
        value += swallowNextDigit(translatedNumerals, 100);
        value += swallowNextDigit(translatedNumerals, 10);
        value += swallowNextDigit(translatedNumerals, 1);
        if (translatedNumerals.size() > 0)
            throw new NumberFormatException("'" + validated + "' contains invalid characters or format");
        if (value < MIN_INT_VALUE || value > MAX_INT_VALUE)
            throw new NumberFormatException("Value of '" + validated + "' is " + value + " but this is out of range");
        return value;
    }


    /**
     * Process roman numerals as the next decimal digit.
     * Acceptable digits:
     *    one, ten => 9
     *    one, five => 4
     *    five, one* => 5 + ones (0 to 4)
     *    one, one& => 1 + ones (0 to 8)
     * Anything else with one, five or ten is an error.
     * Any none one, five or ten is assumed to be part of the next digit and processed there.
     *
     * @param translatedNumerals - list of numerals as their Integer values
     * @param order - value of a unit (1, 10, 100 or 1000)
     * @return the value of this decimal digit in this place position (1, 10, 100 or 1000)
     * @throws NumberFormatException - with description if errors detected
     */
    private static int swallowNextDigit(List<Integer> translatedNumerals, int order)
            throws NumberFormatException{
        // computationally 'one' and 'order' are equivalent, but here for the readability of the logic
        @SuppressWarnings({"Warning:(115, 13) Local variable 'one' is redundant"})
        int one = order;
        int five = 5 * order;
        int ten =  10 * order;
        int value = 0;  // return 0 if no numerals in this order
        int nine = ten - one;
        // process most exclusive to most generic ...
        if (translatedNumerals.size() > 1 && translatedNumerals.get(0) == one && translatedNumerals.get(1) == ten)
            // a valid 9
            value = translatedNumerals.remove(1) - translatedNumerals.remove(0); // remove the ten and the one
        else if (translatedNumerals.size() > 1 && translatedNumerals.get(0) == one && translatedNumerals.get(1) == five)
            // a valid 4
            value = translatedNumerals.remove(1) - translatedNumerals.remove(0); // remove the five and the one
        else if (translatedNumerals.size() > 0 && translatedNumerals.get(0) == five) {
            // a possible 5+
            value = translatedNumerals.remove(0); // remove the five
            while (value < nine && translatedNumerals.size() > 0 && translatedNumerals.get(0) == one)
                value += translatedNumerals.remove(0); // remove the one
        } else if (translatedNumerals.size() > 0 && translatedNumerals.get(0) == one) {
            value = translatedNumerals.remove(0); // remove the one
            // strictly no more than 2 (or 3) following ones, but I am feeling lenient!
            while (value < nine && translatedNumerals.size() > 0 && translatedNumerals.get(0) == one)
                value += translatedNumerals.remove(0); // remove the one
        } else {
            // check for invalid format
            if (translatedNumerals.size() > 0) {
                int possibleInvalid = translatedNumerals.get(0);
                if ( //(possibleInvalid == one) ||
                        (possibleInvalid == five) ||
                        (possibleInvalid == ten) )
                    throw new NumberFormatException("Invalid order of roman numerals");
            }
        }
        return value;
    }

    /**
     * Output the prompt string to the standard output
     * and wait for a line of input and return it
     *
     * @param scanner - to read from stdin
     * @param prompt - string to propmt user
     * @return - string entered by user
     */
    public static String prompt(@NotNull Scanner scanner, String prompt){
        System.out.println(prompt);
        return scanner.nextLine();
    }

    /**
     * Parse the given string as roman numerals.
     * Print the input string and it's value
     * unless a NumberFormatError when we output the error message.
     *
     * @param s - String of roman numerals
     * @return 1 if an error or 0 if ok
     */
    public static int parseAndPrint(String s){
        int error = 0;
        try {
            System.out.println(s + " is " + RomanNumber.parse(s));
        } catch (NumberFormatException e){
            error = 1;
            System.out.println(s + " returned NumberFormatException: " + e.getMessage());
        }
        return error;
    }

    public static void main(String[] args) {
        /* Wanted to use Console, but not supported under IDEA!
        Console console = System.console();
        System.console() returns as null!
         */
        if (args.length > 0) {
            // command line input, just process args:
            int errors = Arrays.stream(args)
                    .map(s -> (s.split("/b")))
                    .flatMap(Arrays::stream)
                    .map(RomanNumber::parseAndPrint)
                    .reduce(0, Integer::sum);
            if (errors > 0) {
                if (errors > 1)
                    System.out.println(errors + " errors detected!");
                else
                    System.out.println(errors + " error detected!");
            }
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Roman Number Translator");
            System.out.println("Enter an empty line to exit");
            String input = prompt(scanner, "Please enter a Roman Numeral");
            while (input.trim().length() > 0) {
                parseAndPrint(input);
                input = prompt(scanner, "Please enter another Roman Numeral");
            }
        }
    }
}
