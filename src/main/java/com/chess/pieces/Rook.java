package com.chess.pieces;

public class Rook extends Piece {
    private boolean hasMovedBefore = false;

    public Rook(int color) {
        super(color, 2);
    }

    @Override
    public int getPIECE_TYPE() {
        return 2;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        // Rook moves horizontally or vertically only
        int colDiff = Math.abs(endCol - startCol);
        int rowDiff = Math.abs(endRow - startRow);
        
        // Must move in straight line (either same row or same column)
        if (colDiff == 0 && rowDiff > 0) {
            return true; // Vertical movement
        }
        
        if (rowDiff == 0 && colDiff > 0) {
            return true; // Horizontal movement
        }
        
        return false;
    }

    public boolean hasMoved() {
        return hasMovedBefore;
    }

    public void setMoved(boolean moved) {
        this.hasMovedBefore = moved;
    }
}
