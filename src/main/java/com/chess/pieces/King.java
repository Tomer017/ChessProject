package com.chess.pieces;

public class King extends Piece {
    public King(int color) {
        super(color, 6);
    }

    @Override
    public int getPIECE_TYPE() {
        return 6;
    }

    public boolean isInCheck(int startX, int startY, int endX, int endY) {
        return false;
    }

    @Override
    public boolean isLegalMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}
