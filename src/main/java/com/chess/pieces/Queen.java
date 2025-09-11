package com.chess.pieces;

public class Queen extends Piece {
    public Queen(int color) {
        super(color, 5);
    }

    @Override
    public int getPIECE_TYPE() {
        return 5;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        // Queen combines rook and bishop movement (horizontal, vertical, or diagonal)
        int colDiff = Math.abs(endCol - startCol);
        int rowDiff = Math.abs(endRow - startRow);
        
        // Horizontal movement (like rook)
        if (rowDiff == 0 && colDiff > 0) {
            return true;
        }
        
        // Vertical movement (like rook)
        if (colDiff == 0 && rowDiff > 0) {
            return true;
        }
        
        // Diagonal movement (like bishop)
        if (colDiff > 0 && rowDiff > 0 && colDiff == rowDiff) {
            return true;
        }
        
        return false;
    }
}
