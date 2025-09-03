package com.chess.pieces;

public class Pawn extends Piece {

    private boolean isFirstMove = true;
    
    public Pawn(int color) {
        super(color, 1);
    }
    
    @Override
    public int getPIECE_TYPE() {
        return 1;
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}
