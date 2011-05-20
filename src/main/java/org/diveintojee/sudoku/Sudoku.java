package org.diveintojee.sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author lgueye
 */
public class Sudoku {

    /**
     * Splits a string every n characters
     * 
     * @param source
     * @param n
     * @return
     */
    public static String[] split(final String source, final int n) {

        if (StringUtils.isEmpty(source))
            throw new IllegalArgumentException();

        if (n < 1)
            throw new IllegalArgumentException();

        final List<String> results = new ArrayList<String>();

        int start = 0;

        // First single term
        if (source.length() <= n)
            return new String[] { source };

        int end = n;

        while (end < source.length()) {
            results.add(source.substring(start, end));
            start = end;
            end += n;
        }

        // Last term index
        start = source.length() - source.length() % n;

        if (start == source.length()) {
            start = source.length() - n;
        }

        // Last term
        final String lastTerm = source.substring(start);

        if (StringUtils.isNotEmpty(lastTerm)) {
            results.add(lastTerm);
        }

        return results.toArray(new String[results.size()]);
    }

    /**
     * Parses every single char on a string and tries to convert it to
     * 
     * @param string
     * @return
     */
    protected static int[] stringToIntArray(final String string) {

        if (StringUtils.isEmpty(string))
            throw new IllegalArgumentException();

        final int[] result = new int[string.length()];

        for (int i = 0; i < string.length(); i++) {
            result[i] = Integer.parseInt(String.valueOf(string.charAt(i)));
        }

        return result;
    }

    private String gridAsString;

    private int squareSideLength;

    private static final String VALID_INPUT = "\\d{" + (int) Math.pow(3, 4) + "}";

    public Sudoku(final String gridAsString) {
        parse(gridAsString);
    }

    public String getGridAsString() {
        return gridAsString;
    }

    public int getGridSideLength() {
        return (int) Math.pow(squareSideLength, 2);
    }

    public int getSquareSideLength() {
        return squareSideLength;
    }

    protected int[][] parse(final String gridAsString) {

        if (StringUtils.isEmpty(gridAsString))
            throw new IllegalArgumentException();

        if (!Pattern.matches(Sudoku.VALID_INPUT, gridAsString))
            throw new IllegalArgumentException();

        this.gridAsString = gridAsString;

        squareSideLength = (int) Math.sqrt(Math.sqrt(this.gridAsString.length()));

        final String[] rows = Sudoku.split(gridAsString, getGridSideLength());

        final int[][] result = new int[getGridSideLength()][getGridSideLength()];

        for (int i = 0; i < rows.length; i++) {
            final String row = rows[i];
            result[i] = stringToIntArray(row);
        }

        return result;
    }

    public void setGridAsString(final String gridAsString) {
        this.gridAsString = gridAsString;
    }

}
