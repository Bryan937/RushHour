package com.example.rushhour.utils;

public class Position {
    /**
     * The Position class represents a block position in a two-dimensional space.
     * It encapsulates information about the row number and column number of the position.
     */
    private final int columnNumber;
    private final int rowNumber;


    public Position(int rowNum, int columnNum) {
        this.rowNumber = rowNum;
        this.columnNumber = columnNum;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        Position objPosition = (Position) obj;
        return (objPosition.getRowNumber() == this.getRowNumber()) && (objPosition.getColumnNumber() == this.getColumnNumber());
    }
}

