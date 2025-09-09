package com.chess.utils;

import com.chess.pieces.Piece;

public class RuleHandler {
    
    
    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow) {
        return piece.isValidMove(startCol, startRow, endCol, endRow);
    }

    public boolean isCaptureValid(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }
    
    public boolean isCheckValid(int startCol, int startRow, int endCol, int endRow) {
        return true;
    }
}
