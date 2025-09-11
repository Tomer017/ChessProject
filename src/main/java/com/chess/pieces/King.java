package com.chess.pieces;

public class King extends Piece {
    private boolean hasMovedBefore = false;

    public King(int color) {
        super(color, 6);
    }

    @Override
    public int getPIECE_TYPE() {
        return 6;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        // King moves one square in any direction (horizontal, vertical, or diagonal)
        int colDiff = Math.abs(endCol - startCol);
        int rowDiff = Math.abs(endRow - startRow);
        
        // King can move one square in any direction
        if (colDiff <= 1 && rowDiff <= 1 && (colDiff > 0 || rowDiff > 0)) {
            return true;
        }
        
        // TODO: Add castling logic here later
        // Castling would be: colDiff == 2 && rowDiff == 0 && !hasMovedBefore
        
        return false;
    }

    public boolean isInCheck(int kingCol, int kingRow) {
        // This method is now handled by RuleHandler.isKingInCheck()
        // Keep for compatibility but should use RuleHandler instead
        return false;
    }

    public boolean hasMoved() {
        return hasMovedBefore;
    }

    public void setMoved(boolean moved) {
        this.hasMovedBefore = moved;
    }

    /**
     * Checks if castling is possible (basic validation, without check detection)
     * @param startCol King's starting column
     * @param startRow King's starting row  
     * @param endCol King's ending column
     * @param endRow King's ending row
     * @return true if this could be a castling move
     */
    public boolean isCastlingMove(int startCol, int startRow, int endCol, int endRow) {
        // Castling: King moves 2 squares horizontally
        int colDiff = endCol - startCol;
        int rowDiff = endRow - startRow;
        
        // Must be exactly 2 squares horizontally, no vertical movement
        if (Math.abs(colDiff) == 2 && rowDiff == 0 && !hasMovedBefore) {
            return true;
        }
        
        return false;
    }
}
