package com.chess.pieces;

public class Piece {
    // use binary to represent the pieces and their colors
    private final int BLACK = 16;
    private final int WHITE = 8;
    private final int PAWN = 1;
    private final int ROOK = 2;
    private final int KNIGHT = 3;
    private final int BISHOP = 4;
    private final int QUEEN = 5;
    private final int KING = 6;

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
}

