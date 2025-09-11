package com.chess.pieces;

public class Bishop extends Piece {
    public Bishop(int color) {
        super(color, 4);
    }

    @Override
    public int getPIECE_TYPE() {
        return 4;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        // Bishop moves diagonally only
        int colDiff = Math.abs(endCol - startCol);
        int rowDiff = Math.abs(endRow - startRow);
        
        // Must move diagonally (same distance in both row and column)
        if (colDiff > 0 && rowDiff > 0 && colDiff == rowDiff) {
            return true;
        }
        
        return false;
    }
}
