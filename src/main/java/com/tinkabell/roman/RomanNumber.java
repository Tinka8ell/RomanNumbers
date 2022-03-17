package com.tinkabell.roman;

import org.jetbrains.annotations.NotNull;

/**
 * A roman number is a natural number expressed as roman numerals.
 * This implementation is therefore wraps an Integer,
 * but limited to only positive values (not zero),
 * and the parse function will accept a string of roman numerals.
 */
public class RomanNumber {

    public Integer value;

    /**
     */
    private RomanNumber(String s) throws NumberFormatException {
        value = Integer.parseInt(s);
    }

    private RomanNumber(int i){
        value = i;
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

    public static void main(String[] args) {
        System.out.println("Welcome to the Roman Number Translator");
    }
}
