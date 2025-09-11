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
        
        // Check for diagonal capture (including en passant)
        if (Math.abs(colDiff) == 1) {
            if (getColor() == WHITE && rowDiff == -1) {
                return true; // White diagonal capture or en passant
            } else if (getColor() == BLACK && rowDiff == 1) {
                return true; // Black diagonal capture or en passant
            }
        }
        
        return false;
    }

    /**
     * Checks if a move is a valid en passant capture
     * @param startCol Starting column
     * @param startRow Starting row  
     * @param endCol Ending column
     * @param endRow Ending row
     * @param enPassantTarget The en passant target square (e.g., "e3")
     * @return true if this is a valid en passant capture
     */
    public boolean isEnPassantCapture(int startCol, int startRow, int endCol, int endRow, String enPassantTarget) {
        if (enPassantTarget == null || "-".equals(enPassantTarget)) {
            return false;
        }

        // Convert en passant target to board coordinates
        int targetCol = enPassantTarget.charAt(0) - 'a'; // a=0, b=1, etc.
        int targetRow = 8 - (enPassantTarget.charAt(1) - '0'); // 8=0, 7=1, etc.

        // Check if the destination matches the en passant target square
        if (endCol != targetCol || endRow != targetRow) {
            return false;
        }

        // Check if this is a diagonal move
        int colDiff = endCol - startCol;
        int rowDiff = endRow - startRow;
        
        if (Math.abs(colDiff) != 1) {
            return false;
        }

        // Check direction based on color
        if (getColor() == WHITE && rowDiff == -1) {
            return true; // White en passant (moving up)
        } else if (getColor() == BLACK && rowDiff == 1) {
            return true; // Black en passant (moving down)
        }

        return false;
    }

    /**
     * Gets the position of the pawn that would be captured in an en passant move
     * @param enPassantTarget The en passant target square
     * @return int array [row, col] of the pawn to be captured, or null if invalid
     */
    public static int[] getEnPassantCapturedPawnPosition(String enPassantTarget) {
        if (enPassantTarget == null || "-".equals(enPassantTarget)) {
            return null;
        }

        int targetCol = enPassantTarget.charAt(0) - 'a';
        int targetRow = 8 - (enPassantTarget.charAt(1) - '0');

        // The captured pawn is on the same column as the target, but on a different row
        // If target is on rank 3 (row 5), captured pawn is on rank 4 (row 4)
        // If target is on rank 6 (row 2), captured pawn is on rank 5 (row 3)
        int capturedPawnRow;
        if (targetRow == 5) { // rank 3 (white en passant)
            capturedPawnRow = 4; // rank 4
        } else if (targetRow == 2) { // rank 6 (black en passant)
            capturedPawnRow = 3; // rank 5
        } else {
            return null; // Invalid en passant target
        }

        return new int[]{capturedPawnRow, targetCol};
    }

    public void setFirstMove(boolean firstMove) {
        this.isFirstMove = firstMove;
    }

    public boolean isFirstMove() {
        return isFirstMove;
    }
}
