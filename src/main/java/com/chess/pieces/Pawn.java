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
    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        if (isFirstMove && getColor() == WHITE) {
            if (startX == endX && (startY == endY + 1 || startY == endY + 2)) {
                return true;
            }
        } else if (startY == endY + 1 && getColor() == WHITE) {
            if (startX == endX && startY == endY + 1) {
                return true;
            }
        } else if (isFirstMove && getColor() == BLACK) {
            if (startX == endX && (startY == endY - 1 || startY == endY - 2)) {
                return true;
            }
        } else if (startY == endY - 1 && getColor() == BLACK) {
            if (startX == endX && startY == endY - 1) {
                return true;
            }
        }
          
        return false;
    }
}
