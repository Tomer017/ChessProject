package com.chess.pieces;

import com.chess.board.Board;

public class Rook extends Piece {
    private final int WHITE = 8;
    private final int BLACK = 16;
    private final Board board;
    private boolean hasMovedBefore = false;

    public Rook(int color) {
        super(color, 2);
        this.board = new Board();
    }

    @Override
    public int getPIECE_TYPE() {
        return 2;
    }

    @Override
    public boolean isLegalMove(int startX, int startY, int endX, int endY) {
        // Rook can only move horizontally or vertically
        if (startX == endX) {
            return true;
        }
        
        if (startY == endY) {
            return true;
        }

        // Rook can only move to an empty square
        if (board.getPiece(endX, endY) != null) {
            return isLegalCapture(startX, startY, endX, endY);
        }
        

        return false;
    }

    public boolean isLegalCapture(int startX, int startY, int endX, int endY) {
        return false;
    }
}
