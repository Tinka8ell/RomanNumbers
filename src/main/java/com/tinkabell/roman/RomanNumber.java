package com.tinkabell.roman;

import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

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

    public Integer value;
    private final boolean wasRoman = false;

    private static final int MAX_INT_VALUE = 4000 - 1;
    private static final int MIN_INT_VALUE = 1;

    public static RomanNumber MAX_VALUE = new RomanNumber(4000 - 1);
    public static RomanNumber MIN_VALUE = new RomanNumber(1);

    private static final String[][] numerals = {
            {"I", "V", "X"},
            {"X", "L", "C"},
            {"C", "D", "M"},
            {"M", "", ""},
    };

    /**
     */
    public RomanNumber(String s) throws NumberFormatException {
        value = Integer.parseInt(s);
    }

    private RomanNumber(int i)
            throws NumberFormatException{
        value = i;
        if (value < MIN_INT_VALUE || value > MAX_INT_VALUE) throw new NumberFormatException("Value out of range");
    }

    /**
     * Parses the string argument as a signed decimal integer.
     *
     * The characters in the string must all be decimal digits,
     * except that the first character may be an ASCII minus sign '-' ('\u002D')
     * to indicate a negative value or an ASCII plus sign '+' ('\u002B')
     * to indicate a positive value.
     * The resulting integer value is returned,
     * exactly as if the argument and the radix 10
     * were given as arguments to the parseInt(String, int) method.
     *
     * @param s - a String containing the int representation to be parsed
     * @return the integer value represented by the argument in decimal
     * @throws NumberFormatException – if the string does not contain a parsable integer
     */
    public static RomanNumber parseRoman(@NotNull String s )
            throws NumberFormatException{
        return new RomanNumber(Integer.parseInt(s));  // for now just accept arabic digits
    }

    public String asRoman () {
        int rest = value;
        int [] powers = new int[4];
        for (int i = 0; i < powers.length; i++) {
            powers[i] = rest % 10;
            rest /= 10;
        }
        StringBuilder result = new StringBuilder();
        for (int i = powers.length - 1; i >= 0 ; i--) {
            result.append(makeRoman(powers[i], numerals[i]));
        }
        return result.toString();
    }

    private static String makeRoman(int power, String[] numeral) {
        String result = "";
        switch (power){
            case 5:
                if (numeral[1].equals(""))
                    result = numeral[1].repeat(power);
                else
                    result = numeral[1];
                break;
            case 4:
                if (numeral[1].equals(""))
                    result = numeral[0].repeat(power);
                else
                    result = numeral[0] + numeral[1];
                break;
            case 9:
                if (numeral[2].equals(""))
                    result = numeral[0].repeat(power);
                else
                    result = numeral[0] + numeral[2];
                break;
            case 0:
                // nothing to add
                break;
            default:
                if (power < 5){
                    result = numeral[0].repeat(power);
                } else {
                    if (numeral[1].equals(""))
                        result = numeral[0].repeat(power);
                    else
                        result = numeral[1] + numeral[0].repeat(power - 5);
                }
                break;
        }
        return result;
    }

    public String asArabic(){
        return value.toString();
    }

    public String toString(){
        return wasRoman ?
                "Roman number " + asRoman() + " is " + asArabic() + " in arabic notation":
                "Arabic number " + asArabic() + " is " + asRoman() + " in Roman Numerals";
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
        System.out.println("Welcome to the Roman Number Translator");
        System.out.println("Enter an empty line to exit");
        String input = prompt(scanner, "Please enter a Roman Numeral");
        while (input.trim().length() > 0){
            RomanNumber number = RomanNumber.parseRoman(input.trim());
            System.out.println(number);
            input = prompt(scanner,"Please enter another Roman Numeral");
        }
    }
}
