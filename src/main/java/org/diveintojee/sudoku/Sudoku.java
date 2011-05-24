package org.diveintojee.sudoku;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author louis.gueye@gmail.com
 */
public class Sudoku {

    private static final int[] POSSIBLE_VALUES = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    /**
     * Given a string {source}, split every {step} characters<br/>
     * This methods returns the last term index
     * 
     * @param source
     * @param step
     * @return
     */
    protected static int getLastTermIndex(final String source, final int step) {

        if (StringUtils.isEmpty(source))
            throw new IllegalArgumentException();

        if (step < 1)
            throw new IllegalArgumentException();

        int index = source.length() - source.length() % step;

        if (index == source.length()) {
            index = source.length() - step;
        }
        return index;
    }

    public static void main(final String[] args) {
        final StringBuilder gridBuilder = new StringBuilder();
        gridBuilder.append("900100005");
        gridBuilder.append("005090201");
        gridBuilder.append("800040000");
        gridBuilder.append("000080000");
        gridBuilder.append("000700000");
        gridBuilder.append("000026009");
        gridBuilder.append("200300006");
        gridBuilder.append("000200900");
        gridBuilder.append("001904570");
        final Sudoku underTest = new Sudoku(gridBuilder.toString());

        System.out.println("Before solving\n");
        Sudoku.printGrid(underTest.getGrid());

        final long start = Calendar.getInstance().getTimeInMillis();
        underTest.cellSolved(0);
        final long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("\nAfter solving\n");
        Sudoku.printGrid(underTest.getGrid());
        System.out.println("\nTook " + underTest.getIterations() + " iterations");
        System.out.println("\nTook " + (end - start) + " ms");
    }

    public static void printGrid(final int[][] grid) {
        for (int i = 0; i < 9; i++) {

            if (i % 3 == 0) {
                System.out.println("-------------");
            }

            for (int j = 0; j < 9; j++) {

                if (j == 0) {
                    System.out.print("|" + grid[i][j]);
                } else if ((j + 1) % 3 == 0) {
                    System.out.print(grid[i][j] + "|");
                } else {
                    System.out.print(grid[i][j]);
                }

                if (j % 9 == 8) {
                    System.out.println("");
                }

            }
        }

        System.out.println("-------------");
    }

    /**
     * Splits a string every n characters
     * 
     * @param source
     * @param step
     * @return
     */
    public static String[] split(final String source, final int step) {

        if (StringUtils.isEmpty(source))
            throw new IllegalArgumentException();

        if (step < 1)
            throw new IllegalArgumentException();

        final List<String> results = new ArrayList<String>();

        int start = 0;

        // First single term
        if (source.length() <= step)
            return new String[] { source };

        int end = step;

        while (end < source.length()) {
            final String term = source.substring(start, end);
            if (term != null) {
                results.add(term);
            }
            start = end;
            end += step;
        }

        // Last term index
        start = getLastTermIndex(source, step);

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

    private int[][] grid;

    private int iterations;

    private static final String VALID_INPUT = "\\d{" + (int) Math.pow(3, 4) + "}";

    public Sudoku(final String gridAsString) {
        parse(gridAsString);
    }

    /**
     * Tests if a candidate value is allowed on a colum<br/>
     * 
     * @param columnIndex
     * @param candidateValue
     * @return
     */
    public boolean allowedOnColumn(final int candidateValue, final int columnIndex) {

        for (int rowIndex = 0; rowIndex < getGridSideLength(); rowIndex++) {

            final int value = getGrid()[rowIndex][columnIndex];

            if (candidateValue == value)
                return false;

        }

        return true;

    }

    /**
     * Tests if a candidate value is allowed on a row<br/>
     * 
     * @param rowIndex
     * @param candidateValue
     * @return
     */
    public boolean allowedOnRow(final int candidateValue, final int rowIndex) {

        for (int columnIndex = 0; columnIndex < getGridSideLength(); columnIndex++) {

            final int value = getGrid()[rowIndex][columnIndex];

            if (candidateValue == value)
                return false;

        }

        return true;

    }

    /**
     * Tests if a candidate value is allowed in a square<br/>
     * The square is delimited by :<br/>
     * - a min row index, a max row index<br/>
     * - a min col index, a max col index<br/>
     * Given an element {rowIndex, columnIndex} in the grid the method can determine the square it belongs to<br>
     * The candidateValue can then be tested against the square values<br/>
     * 
     * @param candidateValue
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    public boolean allowedOnSquare(final int candidateValue, final int rowIndex, final int columnIndex) {

        final int minRowIndex = rowIndex - rowIndex % getSquareSideLength();

        final int maxRowIndex = minRowIndex + getSquareSideLength() - 1;

        final int minColumnIndex = columnIndex - columnIndex % getSquareSideLength();

        final int maxColumnIndex = minColumnIndex + getSquareSideLength() - 1;

        for (int i = minRowIndex; i <= maxRowIndex; i++) {

            for (int j = minColumnIndex; j <= maxColumnIndex; j++) {

                final int value = getGrid()[i][j];

                if (value == candidateValue)
                    return false;

            }

        }

        return true;

    }

    /**
     * @param cellNumber
     * @return
     */
    public boolean cellSolved(final int cellNumber) {

        iterations++;

        if (cellNumber == (int) Math.pow(getGridSideLength(), 2))
            return true;

        final int rowIndex = cellNumber / getGridSideLength();

        final int columnIndex = cellNumber % getGridSideLength();

        final int cellValue = getGrid()[rowIndex][columnIndex];

        final int nextCellNumber = cellNumber + 1;

        if (cellValue != 0)
            return cellSolved(nextCellNumber);

        for (final int candidateValue : Sudoku.POSSIBLE_VALUES) {

            if (allowedOnRow(candidateValue, rowIndex) && allowedOnColumn(candidateValue, columnIndex)
                && allowedOnSquare(candidateValue, rowIndex, columnIndex)) {

                setValueAt(candidateValue, rowIndex, columnIndex);

                if (cellSolved(nextCellNumber))
                    return true;

            }

        }

        setValueAt(0, rowIndex, columnIndex);

        return false;

    }

    /**
     * @return the grid
     */
    public int[][] getGrid() {
        return grid;
    }

    /**
     * @return the input grid as string
     */
    public String getGridAsString() {
        return gridAsString;
    }

    /**
     * @return
     */
    public int getGridSideLength() {
        return (int) Math.pow(squareSideLength, 2);
    }

    public int getIterations() {
        return iterations;
    }

    /**
     * @return the miminal inner square length
     */
    public int getSquareSideLength() {
        return squareSideLength;
    }

    /**
     * Parses a grid<br/>
     * Guesses the square side length<br/>
     * Splits the grid in rows<br/>
     * 
     * @param gridAsString
     */
    protected void parse(final String gridAsString) {

        if (StringUtils.isEmpty(gridAsString))
            throw new IllegalArgumentException();

        if (!Pattern.matches(Sudoku.VALID_INPUT, gridAsString))
            throw new IllegalArgumentException();

        setGridAsString(gridAsString);

        setSquareSideLength((int) Math.sqrt(Math.sqrt(getGridAsString().length())));

        final String[] rows = Sudoku.split(gridAsString, getGridSideLength());

        final int[][] result = new int[getGridSideLength()][getGridSideLength()];

        for (int i = 0; i < rows.length; i++) {
            final String row = rows[i];
            result[i] = stringToIntArray(row);
        }

        setGrid(result);

    }

    private void setGrid(final int[][] grid) {
        this.grid = grid;
    }

    private void setGridAsString(final String gridAsString) {
        this.gridAsString = gridAsString;
    }

    private void setSquareSideLength(final int squareSideLength) {
        this.squareSideLength = squareSideLength;
    }

    private void setValueAt(final int value, final int rowIndex, final int columnIndex) {
        getGrid()[rowIndex][columnIndex] = value;
    }

}
