package com.chess.pieces;

public class Rook extends Piece {

    public Rook(int color) {
        super(color, 2);
    }

    @Override
    public int getPIECE_TYPE() {
        return 2;
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}
