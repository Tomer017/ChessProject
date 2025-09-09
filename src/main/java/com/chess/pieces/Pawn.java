package com.chess.pieces;

public class Pawn extends Piece {

    private boolean isFirstMove = true;

    private final int WHITE = 8;
    private final int BLACK = 16;
    
    public Pawn(int color) {
        super(color, 1);
    }
    
    @Override
    public int getPIECE_TYPE() {
        return 1;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        int colDiff = endCol - startCol;
        int rowDiff = endRow - startRow;
        
        // Check for forward movement (no capture)
        if (colDiff == 0) {
            // White moves up (decreasing row), Black moves down (increasing row)
            if (getColor() == WHITE) {
                // White pawn: can move 1 square forward, or 2 on first move
                if (rowDiff == -1 || (isFirstMove && rowDiff == -2)) {
                    return true;
                }
            } else if (getColor() == BLACK) {
                // Black pawn: can move 1 square forward, or 2 on first move
                if (rowDiff == 1 || (isFirstMove && rowDiff == 2)) {
                    return true;
                }
            }
        }
        
        // Check for diagonal capture
        if (Math.abs(colDiff) == 1) {
            if (getColor() == WHITE && rowDiff == -1) {
                return true; // White diagonal capture (one row forward)
            } else if (getColor() == BLACK && rowDiff == 1) {
                return true; // Black diagonal capture (one row forward)
            }
        }

        // Check for en passant
        
        
        return false;
    }

    public void setFirstMove(boolean firstMove) {
        this.isFirstMove = firstMove;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }
}
