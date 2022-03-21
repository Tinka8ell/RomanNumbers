package com.tinkabell.roman;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A roman numeral object represents a sequence of roman numerals.
 * That sequence can be viewed as:
 *    String goodNumerals - sequence of a valid roman number
 *    char badNumeral - if non-zero is the first "numeral" found in error
 *    String errorMessage - if an error then this describes it in relation to the good and bad numerals
 *    int[4] digits - values from 0-9 for each place value (big endian)
 *    int currentOrder - index of first non-zero digit
 *    int nextOrder - index of digit being constructed
 *    enum lookingFor - type of digit being looked for as part of that construction
 * The constructor takes a numeral character and populates the state fo that one numeral.
 * The combiner takes two consecutive (ordered) numerals and makes one.
 * The
 *
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
 *
 * Logically each decimal digit (1000's, 100's, 10', 1') must follow
 * in this order and for each "digit" consists of one of these 4 combinations:
 *    one, ten => 9
 *    one, five => 4
 *    five, optional series of one's => 5 to 9
 *    one, optional series of one's => 1 to 9
 * Any following digits must be less than "one" at this level.
 */
public class RomanNumeral {

    /*
    To process sequential numerals previous numerals will be summarised
    by the following state:
     */
    private int[] digits;  // value of each digit - ls digit first
    private int ofOrder;  // highest order of this number
    private int addOrder; // the order at which we can add
    private NumeralType canJoinTo; // what we can accept next
    private StringBuffer goodNumerals; // what has worked so far
    private char badNumeral; // first bad numberal found
    private String errorMessage; // explain the error
    private boolean areOnes; // are all "one" numerals

    private enum NumeralType {
        NOTHING, // nothing more at this order
        ONE, // one's
        ONE_FIVE, // one or five
        ONE_FIVE_TEN // one or five or ten
    }

    /*
    magic numbers
     */
    private static final int minOrder=0;  // from units
    private static final int maxOrder=3;  // to thousands
    private static final int one=1;
    private static final int five=5;
    private static final int ten=10;

    private static final int MAX_INT_VALUE = (int) Math.pow(ten, maxOrder + 1) - one;
    private static final int MIN_INT_VALUE = one;

    // the valid characters used to get an index
    private static final String numerals = "IVXLCDM";

    // "properties"
    private boolean isTen(int atOrder) {
        return (atOrder < maxOrder) && // ten possible at theis order
                (atOrder == ofOrder - 1) && // most significant digit could be ten
                (digits[atOrder] == 0) && // nothing already at this order
                (digits[ofOrder] == one);  // only if most significant digit is one
    }
    private boolean isFive(int atOrder) {
        return (atOrder == ofOrder) &&
                (digits[ofOrder] == five);  // only if most significant digit is five
    }
    private boolean isOne(int atOrder) {
        return (atOrder == ofOrder) &&
                (digits[ofOrder] == one);  // only if most significant digit is one
    }
    private boolean areOnes(int atOrder) {
        return (atOrder == ofOrder) && areOnes; // only ones in the most significant digit
    }

    private boolean isSmaller(int atOrder) {
        return atOrder > ofOrder; // at wholly lower order of magnitude
    }

    /**
     * Create a "unity" Roman Numeral
     */
    public RomanNumeral(){
        digits = new int[maxOrder + 1]; // four digits
        addOrder = digits.length - 1; // most significant digit first
        badNumeral = 0; // there isn't one yet
        goodNumerals = new StringBuffer();  // none yet
        ofOrder = -1; // actually invalid but zero is invalid!
        errorMessage = "Can't have an empty Roman Numeral";
        canJoinTo = NumeralType.ONE_FIVE; // starting a new digit means looking for a one or five
    }

    /**
     * Create an initial Roman Numeral.
     * Validate the character supplied.
     * @param numeral - provided first character (expected to be uppercase)
     */
    public RomanNumeral(int numeral){
        this();
        // assume bad until proved otherwise
        badNumeral = (char) numeral;
        errorMessage = " is an invalid character for a Roman Numeral";
        int index = numerals.indexOf(badNumeral);
        if (index >= 0) {
            // ok we are actually valid
            goodNumerals.append(badNumeral);
            badNumeral = 0;
            errorMessage = "";
            /* analise this numeral:
               order = index / 2, isFive = index % 2
               numeral index order isFive
                  I      0     0      0
                  V      1     0      1
                  X      2     1      0
                  L      3     1      1
                  C      4     2      0
                  D      5     2      1
                  M      6     3      0
             */
            ofOrder = index / 2; // order of this numeral (power of 10)
            addOrder = ofOrder; // start wanting to add to the same order
            boolean isOne = (index % 2) == 0; // even ones are one's and odd ones are 'five's
            areOnes = isOne; // one 'one' is all 'one's
            digits[ofOrder] = isOne? one: five;
            canJoinTo = isOne?
                    NumeralType.ONE_FIVE_TEN: // one can be followed by one, five or ten
                    NumeralType.ONE; // only a one can follow a five
        }
    }

    @Override
    public String toString() {
        return "RomanNumeral{" +
                Arrays.toString(digits) +
                ", '" + goodNumerals +
                "', '" + badNumeral +
                "', " + ofOrder +
                ", " + addOrder +
                ", " + canJoinTo +
                ", e='" + errorMessage + "'" +
                '}';

    }

    /**
     * Append a roman numeral to this one.
     *
     * @param nextNumeral to append
     * @return the merged roman numeral
     */
    private RomanNumeral append(RomanNumeral nextNumeral) {
        RomanNumeral returnNumeral = this;  // by default, we return this one updated
        if (ofOrder < 0) // we are unity, so just return appended numeral
            returnNumeral = nextNumeral;
        else if (badNumeral == 0) { // we haven't got an error yet
            // what can we joint to?
            if (nextNumeral.isSmaller(addOrder)){
                // can always add a smaller number
                overlay(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE
                    && nextNumeral.areOnes(addOrder)) {
                // can add what we are expecting
                addOn(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE_FIVE
                    && nextNumeral.isFive(addOrder)) {
                // can add what we are expecting
                canJoinTo = NumeralType.ONE; // after a five want ones
                overlay(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE_FIVE
                    && nextNumeral.areOnes(addOrder)) {
                // can add what we are expecting
                canJoinTo = NumeralType.ONE; // after a one want ones
                addOn(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE_FIVE_TEN
                    && nextNumeral.isTen(addOrder)) {
                // can add what we are expecting
                canJoinTo = NumeralType.NOTHING; // after a nine can't join at this order
                nextNumeral.makeTen();
                // our one and next numeral's ten, will make a nine:
                digits[addOrder] = - digits[addOrder]; // make our one a minus one
                addOn(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE_FIVE_TEN
                    && nextNumeral.isFive(addOrder)) {
                // can add what we are expecting
                canJoinTo = NumeralType.NOTHING; // after a four can't join at this order
                // our one and next numeral's five, will make a four:
                digits[addOrder] = - digits[addOrder]; // make our one a minus one
                addOn(nextNumeral);
            } else if (canJoinTo == NumeralType.ONE_FIVE_TEN
                    && nextNumeral.areOnes(addOrder)) {
                // can add what we are expecting
                canJoinTo = NumeralType.ONE; // after a one want ones
                addOn(nextNumeral);
            } else {
                // next numeral is out of sequence
                // we can't add it so must be an error
                markWithError(nextNumeral, " is not valid at this point in the roman numeral");
            }
        } // else we already have error so ignore appended numeral
        return returnNumeral;
    }

    /**
     * Make this roman numeral which has been identified as a "ten"
     * into one by moving the one at the most significant digit to
     * become a tem at the next digit.
     */
    private void makeTen() {
        digits[ofOrder] = 0;
        ofOrder --;
        digits[ofOrder] = 10;
    }

    /**
     * Mark this roman numeral now has error from the
     * appended next roman numeral.
     * Use it's first good numeral as our bad one.
     * @param nextNumeral to use for bad numeral
     */
    @Contract(mutates = "this")
    private void markWithError(@NotNull RomanNumeral nextNumeral, String message) {
        badNumeral = nextNumeral.goodNumerals.charAt(0); // their first is our bad
        errorMessage = message;
    }

    /**
     * Add on most significant digit and
     * copy all the leftover data into this roman numeral.
     * @param nextNumeral containing data to add
     */
    private void addOn(@NotNull RomanNumeral nextNumeral) {
        if (addOrder == ofOrder) {
            // adding to most significant digit
            areOnes = areOnes && nextNumeral.areOnes(addOrder);
        }
        digits[addOrder] += nextNumeral.removeDigit(addOrder);
        if (digits[addOrder] >= ten){
            markWithError(nextNumeral, " repeats too many times");
        } else {
            overlay(nextNumeral);
        }
    }

    /**
     * Remove the digit at the atOrder level from this numeral
     * and return it.
     * As we are expecting to remove only the digit at "ofOrder",
     * we have removed the most significant digit so move "ofOrder".
     * @param atOrder - at which to remove
     * @return digit at that order
     */
    private int removeDigit(int atOrder) {
        int value = digits[atOrder];
        digits[atOrder] = 0;
        // adjust ofOrder
        while (ofOrder >= minOrder && digits[ofOrder] == 0)
            ofOrder--; // skip and zero most significant digits
        return value;
    }

    /**
     * Copy all the relevant data into this roman numeral.
     * @param nextNumeral containing data to add
     */
    private void overlay(@NotNull RomanNumeral nextNumeral) {
        // copy extra digits
        if (nextNumeral.ofOrder + 1 - nextNumeral.addOrder >= 0)
            System.arraycopy(
                    nextNumeral.digits, nextNumeral.addOrder, 
                    digits, nextNumeral.addOrder,
                    nextNumeral.ofOrder + 1 - nextNumeral.addOrder);
        goodNumerals.append(nextNumeral.goodNumerals);
        badNumeral = nextNumeral.badNumeral;
        errorMessage = nextNumeral.errorMessage;
        if (addOrder > nextNumeral.addOrder){
            addOrder = nextNumeral.addOrder;
            canJoinTo = nextNumeral.canJoinTo;
        }
    }

    /**
     * Return this roman numeral as an int.
     * @return the integer value represented by this roman number
     * @throws NumberFormatException – if roman numeral has an error
     */
    public int getValue() throws NumberFormatException{
        if (errorMessage.length() > 0)
            if (goodNumerals.length() > 0)
                throw new NumberFormatException(badNumeral + errorMessage + " after " + goodNumerals);
            else
                throw new NumberFormatException(badNumeral + errorMessage);
        int value = 0;
        for (int index = digits.length - 1; index >= 0; index--) {
            value *= ten; // move to next order
            value += digits[index]; // add next digit
        }
        return value;
    }

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
        int value;
        String validated = s.trim().toUpperCase();
        Optional<RomanNumeral> romanNumeral = s.trim()
                .toUpperCase()
                .chars()
                .mapToObj(RomanNumeral::new)
                .reduce(RomanNumeral::append);
        if (romanNumeral.isEmpty())
            value = new RomanNumeral().getValue();  // should cause an error
        else
            value = romanNumeral.get().getValue();
        // don't think the processing can give a value > MAX_INT_VALUE, but could be 0!
        if (value < MIN_INT_VALUE || value > MAX_INT_VALUE)
            throw new NumberFormatException("Value of '" + validated + "' is " + value + " but this is out of range");
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
            System.out.println(s + " is " + RomanNumeral.parse(s));
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
                    .map(RomanNumeral::parseAndPrint)
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
