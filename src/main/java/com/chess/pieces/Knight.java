package com.chess.pieces;

public class Knight extends Piece {

    public Knight(int color) {
        super(color, 3);
    }

    @Override
    public int getPIECE_TYPE() {
        return 3;
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}