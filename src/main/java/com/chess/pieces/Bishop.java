package com.chess.pieces;

public class Bishop extends Piece {
    public Bishop(int color) {
        super(color, 4);
    }

    @Override
    public int getPIECE_TYPE() {
        return 4;
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}
