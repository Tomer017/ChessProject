package com.chess.utils;

import com.chess.board.Board;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;

public class RuleHandler {
    
    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow) {
        return piece.isLegalMove(startCol, startRow, endCol, endRow);
    }

    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board) {
        // First check if the piece can legally make this type of move
        if (!piece.isLegalMove(startCol, startRow, endCol, endRow)) {
            return false;
        }

        // Special validation for pawns
        if (piece instanceof Pawn) {
            return isPawnMoveValid((Pawn) piece, startCol, startRow, endCol, endRow, board);
        }

        return true;
    }

    private boolean isPawnMoveValid(Pawn pawn, int startCol, int startRow, int endCol, int endRow, Board board) {
        int colDiff = endCol - startCol;
        int rowDiff = endRow - startRow;

        // Forward movement (no capture)
        if (colDiff == 0) {
            // Check if destination is empty
            if (board.getPiece(endRow, endCol) == null || board.getPiece(endRow, endCol).getColor() == 0) {
                // For 2-square moves, also check the square in between
                if (Math.abs(rowDiff) == 2) {
                    int middleRow = startRow + (rowDiff / 2);
                    if (board.getPiece(middleRow, endCol) != null && board.getPiece(middleRow, endCol).getColor() != 0) {
                        return false; // Path is blocked
                    }
                }
                return true;
            }
            return false; // Destination is occupied
        }

        // Diagonal movement (capture)
        if (Math.abs(colDiff) == 1) {
            Piece targetPiece = board.getPiece(endRow, endCol);
            // Must have an enemy piece to capture
            if (targetPiece != null && targetPiece.getColor() != 0 && targetPiece.getColor() != pawn.getColor()) {
                return true;
            }
            return false; // No piece to capture or friendly piece
        }

        return false;
    }

    public boolean isCaptureValid(Piece piece, int startCol, int startRow, int endCol, int endRow) {
        return true;
    }
    
    public boolean isCheckValid(Piece piece, int startCol, int startRow, int endCol, int endRow) {
        return true;
    }
}
