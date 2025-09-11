package com.chess.utils;

import com.chess.board.Board;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;

public class RuleHandler {
    
    private String currentEnPassantTarget = "-";
    
    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow) {
        return piece.isLegalMove(startCol, startRow, endCol, endRow);
    }

    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board) {
        return isMoveValid(piece, startCol, startRow, endCol, endRow, board, currentEnPassantTarget);
    }

    public boolean isMoveValid(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board, String enPassantTarget) {
        // First check if the piece can legally make this type of move
        if (!piece.isLegalMove(startCol, startRow, endCol, endRow)) {
            return false;
        }

        // Special validation for pawns
        if (piece instanceof Pawn) {
            return isPawnMoveValid((Pawn) piece, startCol, startRow, endCol, endRow, board, enPassantTarget);
        }

        return true;
    }

    public void setEnPassantTarget(String enPassantTarget) {
        this.currentEnPassantTarget = enPassantTarget != null ? enPassantTarget : "-";
    }

    public String getEnPassantTarget() {
        return currentEnPassantTarget;
    }

    private boolean isPawnMoveValid(Pawn pawn, int startCol, int startRow, int endCol, int endRow, Board board) {
        return isPawnMoveValid(pawn, startCol, startRow, endCol, endRow, board, currentEnPassantTarget);
    }

    private boolean isPawnMoveValid(Pawn pawn, int startCol, int startRow, int endCol, int endRow, Board board, String enPassantTarget) {
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

        // Diagonal movement (capture or en passant)
        if (Math.abs(colDiff) == 1) {
            Piece targetPiece = board.getPiece(endRow, endCol);
            
            // Regular capture
            if (targetPiece != null && targetPiece.getColor() != 0 && targetPiece.getColor() != pawn.getColor()) {
                return true;
            }
            
            // En passant capture
            if (pawn.isEnPassantCapture(startCol, startRow, endCol, endRow, enPassantTarget)) {
                // Verify there's an enemy pawn in the correct position to capture
                int[] capturedPawnPos = Pawn.getEnPassantCapturedPawnPosition(enPassantTarget);
                if (capturedPawnPos != null) {
                    Piece capturedPawn = board.getPiece(capturedPawnPos[0], capturedPawnPos[1]);
                    if (capturedPawn instanceof Pawn && capturedPawn.getColor() != pawn.getColor()) {
                        return true;
                    }
                }
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
