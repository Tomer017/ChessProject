package com.chess.pieces;

public class Piece {
    private int color;
    private final int PIECE_TYPE;

    public Piece(int color, int pieceType) {
        this.color = color;
        this.PIECE_TYPE = pieceType;
    }

    public int getColor() {
        return color;
    }

    public int getPIECE_TYPE() {
        return PIECE_TYPE;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isLegalMove(int startX, int startY, int endX, int endY) {
        return false;
    }

    public void move(int startX, int startY, int endX, int endY) {
        // move the piece to the new position
    }

    public void getPosition() {
        // get the position of the piece
    }
}

