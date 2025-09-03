package com.chess.pieces;

public class Queen extends Piece {
    public Queen(int color) {
        super(color, 5);
    }

    @Override
    public int getPIECE_TYPE() {
        return 5;
    }

    public boolean isValidMove(int startX, int startY, int endX, int endY) {
        return false;
    }
}
