package org.diveintojee.sudoku;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author louis.gueye@gmail.com
 */
public class SudokuTest {

    private Sudoku underTest;

    @Test
    public void allowedOnColumn() {

        // When
        boolean result = true;

        // On column 0 the above values are not allowed
        result = underTest.allowedOnColumn(9, 0);
        Assert.assertFalse(result);

        result = underTest.allowedOnColumn(8, 0);
        Assert.assertFalse(result);

        result = underTest.allowedOnColumn(2, 0);
        Assert.assertFalse(result);
    }

    @Test
    public void allowedOnRow() {
        // // Given
        // final int[][] expected = { { 9, 0, 0, 1, 0, 0, 0, 0, 5 }, { 0, 0, 5, 0, 9, 0, 2, 0, 1 },
        // { 8, 0, 0, 0, 4, 0, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0, 0 }, { 0, 0, 0, 7, 0, 0, 0, 0, 0 },
        // { 0, 0, 0, 0, 2, 6, 0, 0, 9 }, { 2, 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 0, 0, 2, 0, 0, 9, 0, 0 },
        // { 0, 0, 1, 9, 0, 4, 5, 7, 0 } };
        // final int[][] result = underTest.parse(underTest.getGridAsString());

        // When
        boolean result = true;

        // On row 0 the above values are not allowed
        result = underTest.allowedOnRow(9, 0);
        Assert.assertFalse(result);

        result = underTest.allowedOnRow(1, 0);
        Assert.assertFalse(result);

        result = underTest.allowedOnRow(5, 0);
        Assert.assertFalse(result);

        // On row 1 the above values are not allowed
        result = underTest.allowedOnRow(5, 1);
        Assert.assertFalse(result);

        result = underTest.allowedOnRow(9, 1);
        Assert.assertFalse(result);

        result = underTest.allowedOnRow(2, 1);
        Assert.assertFalse(result);

        result = underTest.allowedOnRow(1, 1);
        Assert.assertFalse(result);

        // etc.
    }

    @Test
    public void allowedOnSquare() {

        // When
        boolean result = true;

        // On column 0 the above values are not allowed
        result = underTest.allowedOnSquare(9, 0, 1);
        Assert.assertFalse(result);

        result = underTest.allowedOnSquare(8, 0, 1);
        Assert.assertFalse(result);

        result = underTest.allowedOnSquare(5, 0, 0);
        Assert.assertFalse(result);
    }

    @Before
    public void before() {
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
        underTest = new Sudoku(gridBuilder.toString());
    }

    @Test
    public void cellSolvedIfLastCellIsReached() {
        final int cellNumber = (int) Math.pow(underTest.getGridSideLength(), 2);
        Assert.assertTrue(underTest.cellSolved(cellNumber));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getLastTermIndexShouldThrowIllegalArgumentExceptionWithEmptyString() {
        Sudoku.getLastTermIndex(StringUtils.EMPTY, 3);
    }

    @Test
    public void getLastTermIndexShouldThrowIllegalArgumentExceptionWithNonStrictlyPositiveStep() {
        try {
            Sudoku.getLastTermIndex("12345", 0);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {

        }
        try {
            Sudoku.getLastTermIndex("12345", -1);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {

        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void getLastTermIndexShouldThrowIllegalArgumentExceptionWithNullString() {
        Sudoku.getLastTermIndex(null, 3);
    }

    @Test
    public void gridSideLength() {
        Assert.assertEquals((int) Math.pow(underTest.getSquareSideLength(), 2), underTest.getGridSideLength());
    }

    @Test
    public void parseShouldReturnAnArray() {
        final int[][] expected = { { 9, 0, 0, 1, 0, 0, 0, 0, 5 }, { 0, 0, 5, 0, 9, 0, 2, 0, 1 },
                { 8, 0, 0, 0, 4, 0, 0, 0, 0 }, { 0, 0, 0, 0, 8, 0, 0, 0, 0 }, { 0, 0, 0, 7, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 2, 6, 0, 0, 9 }, { 2, 0, 0, 3, 0, 0, 0, 0, 6 }, { 0, 0, 0, 2, 0, 0, 9, 0, 0 },
                { 0, 0, 1, 9, 0, 4, 5, 7, 0 } };

        underTest.parse(underTest.getGridAsString());

        final int[][] result = underTest.getGrid();

        Assert.assertNotNull(result);

        for (int i = 0; i < result.length; i++) {

            final int[] actualArray = result[i];
            // printIntArray("actual = ", actualArray);

            final int[] expectedArray = expected[i];
            // printIntArray("expect = ", expectedArray);

            Assert.assertArrayEquals(expectedArray, actualArray);

        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldThrowIllegalArgumentExceptionWithEmptyGrid() {
        underTest.parse(StringUtils.EMPTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldThrowIllegalArgumentExceptionWithIncorrectCellCount() {
        final String faultyString = RandomStringUtils.random((9 * 9 + 1));
        underTest.parse(faultyString);
    }

    @Test
    public void parseShouldThrowIllegalArgumentExceptionWithNonDigitChars() {
        String faultyString = null;

        faultyString = RandomStringUtils.random((9 * 9), true, false);
        try {
            underTest.parse(faultyString);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {

        }

        faultyString = RandomStringUtils.random(40, true, false) + RandomStringUtils.random(41, false, true);
        try {
            underTest.parse(faultyString);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {}
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseShouldThrowIllegalArgumentExceptionWithNullGrid() {
        underTest.parse(null);
    }

    @Test
    public void splitShouldReturnArrayWithOneElementIfStringLengthIsLessThanOneStep() {
        // Given
        final String string = "900";
        final int step = 10;

        // When
        final String[] result = Sudoku.split(string, step);

        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(string, result[0]);
    }

    @Test
    public void splitShouldReturnArrayWithTwoElementsIfStringLengthEqualsTwoSteps() {
        // Given
        final String string = "123456";
        final int step = 3;

        // When
        final String[] result = Sudoku.split(string, step);

        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("123", result[0]);
        Assert.assertEquals("456", result[1]);
    }

    @Test
    public void splitShouldReturnArrayWithTwoElementsIfStringLengthIsLessThanTwoSteps() {
        // Given
        final String string = "12345";
        final int step = 3;

        // When
        final String[] result = Sudoku.split(string, step);

        // Then
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.length);
        Assert.assertEquals("123", result[0]);
        Assert.assertEquals("45", result[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void splitShouldThrowIllegalArgumentExceptionWithEmptyString() {
        Sudoku.split(StringUtils.EMPTY, 3);
    }

    @Test
    public void splitShouldThrowIllegalArgumentExceptionWithNonStrictlyPositiveStep() {
        try {
            Sudoku.split("12345", 0);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {

        }
        try {
            Sudoku.split("12345", -1);
            Assert.fail(IllegalArgumentException.class.getSimpleName() + " expected");
        } catch (final IllegalArgumentException e) {

        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void splitShouldThrowIllegalArgumentExceptionWithNullString() {
        Sudoku.split(null, 3);
    }

    @Test
    public void squareSideLength() {
        Assert.assertEquals(3, underTest.getSquareSideLength());
    }
}
