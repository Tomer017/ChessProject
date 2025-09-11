package com.chess.utils;

import com.chess.board.Board;
import com.chess.pieces.King;
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

        // Path validation for sliding pieces (Rook, Bishop, Queen)
        if (piece instanceof com.chess.pieces.Rook || 
            piece instanceof com.chess.pieces.Bishop || 
            piece instanceof com.chess.pieces.Queen) {
            return isSlidingPiecePathClear(piece, startCol, startRow, endCol, endRow, board);
        }

        // Knight and King don't need path validation (they jump/move one square)
        
        // Final check: ensure this move doesn't leave the player's king in check
        if (doesMoveLeaveKingInCheck(piece, startCol, startRow, endCol, endRow, board)) {
            return false; // Move would leave king in check
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

    /**
     * Checks if a king is currently in check
     * @param board The current board state
     * @param kingColor The color of the king to check (WHITE or BLACK)
     * @return true if the king is in check
     */
    public boolean isKingInCheck(Board board, int kingColor) {
        // Find the king's position
        int[] kingPos = findKingPosition(board, kingColor);
        if (kingPos == null) {
            return false; // King not found (shouldn't happen in normal game)
        }

        return isSquareUnderAttack(board, kingPos[1], kingPos[0], kingColor);
    }

    /**
     * Checks if a specific square is under attack by the enemy
     * @param board The current board state
     * @param col Column of the square to check
     * @param row Row of the square to check  
     * @param friendlyColor The color we're checking for (enemy attacks against this color)
     * @return true if the square is under attack by enemy pieces
     */
    public boolean isSquareUnderAttack(Board board, int col, int row, int friendlyColor) {
        int enemyColor = (friendlyColor == 8) ? 16 : 8; // WHITE = 8, BLACK = 16

        // Check all squares on the board for enemy pieces that can attack this square
        for (int checkRow = 0; checkRow < 8; checkRow++) {
            for (int checkCol = 0; checkCol < 8; checkCol++) {
                Piece piece = board.getPiece(checkRow, checkCol);
                
                // Skip empty squares and friendly pieces
                if (piece == null || piece.getColor() == 0 || piece.getColor() == friendlyColor) {
                    continue;
                }

                // Check if this enemy piece can attack the target square
                if (canPieceAttackSquare(piece, checkCol, checkRow, col, row, board)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if a piece can attack a specific square
     * @param piece The attacking piece
     * @param pieceCol The piece's column
     * @param pieceRow The piece's row
     * @param targetCol Target square column
     * @param targetRow Target square row
     * @param board The current board state
     * @return true if the piece can attack the target square
     */
    private boolean canPieceAttackSquare(Piece piece, int pieceCol, int pieceRow, int targetCol, int targetRow, Board board) {
        // Check if the piece can legally move to the target square
        if (!piece.isLegalMove(pieceCol, pieceRow, targetCol, targetRow)) {
            return false;
        }

        // For sliding pieces, check if path is clear
        if (piece instanceof com.chess.pieces.Rook || 
            piece instanceof com.chess.pieces.Bishop || 
            piece instanceof com.chess.pieces.Queen) {
            return isSlidingPiecePathClear(piece, pieceCol, pieceRow, targetCol, targetRow, board, true);
        }

        // Pawns have special attack patterns
        if (piece instanceof Pawn) {
            return isPawnAttackingSquare((Pawn) piece, pieceCol, pieceRow, targetCol, targetRow);
        }

        // Knights and Kings don't need path validation for attacks
        return true;
    }

    /**
     * Checks if a pawn is attacking a specific square
     */
    private boolean isPawnAttackingSquare(Pawn pawn, int pawnCol, int pawnRow, int targetCol, int targetRow) {
        int colDiff = targetCol - pawnCol;
        int rowDiff = targetRow - pawnRow;

        // Pawns attack diagonally
        if (Math.abs(colDiff) == 1) {
            if (pawn.getColor() == 8 && rowDiff == -1) { // White pawn attacks up
                return true;
            } else if (pawn.getColor() == 16 && rowDiff == 1) { // Black pawn attacks down
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the position of a king on the board
     * @param board The current board state
     * @param kingColor The color of the king to find
     * @return int array [row, col] of the king's position, or null if not found
     */
    private int[] findKingPosition(Board board, int kingColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.getColor() == kingColor) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    /**
     * Validates that a move doesn't leave the player's own king in check
     * @param piece The piece being moved
     * @param startCol Starting column
     * @param startRow Starting row
     * @param endCol Ending column
     * @param endRow Ending row
     * @param board The current board state
     * @return true if the move is legal (doesn't leave king in check)
     */
    public boolean doesMoveLeaveKingInCheck(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board) {
        // Make a temporary copy of the board to test the move
        Board tempBoard = copyBoard(board);
        
        // Execute the move on the temporary board
        Piece tempPiece = tempBoard.getPiece(startRow, startCol);
        tempBoard.setPiece(endRow, endCol, tempPiece);
        tempBoard.setPiece(startRow, startCol, new Piece(0, 0)); // Empty square

        // Check if this move leaves the king in check
        return isKingInCheck(tempBoard, piece.getColor());
    }

    /**
     * Creates a deep copy of the board for move testing
     */
    private Board copyBoard(Board originalBoard) {
        Board copy = new Board();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece originalPiece = originalBoard.getPiece(row, col);
                if (originalPiece != null) {
                    // Create a new piece of the same type and color
                    Piece newPiece = createPieceCopy(originalPiece);
                    copy.setPiece(row, col, newPiece);
                }
            }
        }
        
        return copy;
    }

    /**
     * Creates a copy of a piece
     */
    private Piece createPieceCopy(Piece original) {
        switch (original.getPIECE_TYPE()) {
            case 1: return new Pawn(original.getColor());
            case 2: return new com.chess.pieces.Rook(original.getColor());
            case 3: return new com.chess.pieces.Knight(original.getColor());
            case 4: return new com.chess.pieces.Bishop(original.getColor());
            case 5: return new com.chess.pieces.Queen(original.getColor());
            case 6: return new King(original.getColor());
            default: return new Piece(original.getColor(), original.getPIECE_TYPE());
        }
    }

    /**
     * Validates that the path is clear for sliding pieces (Rook, Bishop, Queen)
     */
    private boolean isSlidingPiecePathClear(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board) {
        return isSlidingPiecePathClear(piece, startCol, startRow, endCol, endRow, board, false);
    }

    /**
     * Validates that the path is clear for sliding pieces (Rook, Bishop, Queen)
     * @param isAttackCheck true if we're checking for attacks (ignore destination piece color)
     */
    private boolean isSlidingPiecePathClear(Piece piece, int startCol, int startRow, int endCol, int endRow, Board board, boolean isAttackCheck) {
        int colDiff = endCol - startCol;
        int rowDiff = endRow - startRow;
        
        // Determine direction of movement
        int colStep = Integer.compare(colDiff, 0); // -1, 0, or 1
        int rowStep = Integer.compare(rowDiff, 0); // -1, 0, or 1
        
        // Check each square along the path (excluding start and end squares)
        int currentCol = startCol + colStep;
        int currentRow = startRow + rowStep;
        
        while (currentCol != endCol || currentRow != endRow) {
            // Check if there's a piece blocking the path
            Piece blockingPiece = board.getPiece(currentRow, currentCol);
            if (blockingPiece != null && blockingPiece.getColor() != 0) {
                return false; // Path is blocked
            }
            
            // Move to next square in the path
            currentCol += colStep;
            currentRow += rowStep;
        }
        
        // For attack checking, we don't care about the destination piece color
        if (isAttackCheck) {
            return true;
        }
        
        // Check destination square - can be empty or contain enemy piece
        Piece destinationPiece = board.getPiece(endRow, endCol);
        if (destinationPiece != null && destinationPiece.getColor() == piece.getColor()) {
            return false; // Can't capture own piece
        }
        
        return true; // Path is clear
    }

    /**
     * Checks if a player is in checkmate
     * @param board The current board state
     * @param playerColor The color of the player to check
     * @return true if the player is in checkmate
     */
    public boolean isCheckmate(Board board, int playerColor) {
        // First, the king must be in check
        if (!isKingInCheck(board, playerColor)) {
            return false;
        }

        // If king is in check, see if there are any legal moves to escape
        return !hasAnyLegalMoves(board, playerColor);
    }

    /**
     * Checks if a player is in stalemate (no legal moves but not in check)
     * @param board The current board state
     * @param playerColor The color of the player to check
     * @return true if the player is in stalemate
     */
    public boolean isStalemate(Board board, int playerColor) {
        // Must NOT be in check
        if (isKingInCheck(board, playerColor)) {
            return false;
        }

        // Must have no legal moves
        return !hasAnyLegalMoves(board, playerColor);
    }

    /**
     * Checks if a player has any legal moves available
     * @param board The current board state
     * @param playerColor The color of the player to check
     * @return true if the player has at least one legal move
     */
    public boolean hasAnyLegalMoves(Board board, int playerColor) {
        // Check every piece of the player's color
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                
                // Skip empty squares and enemy pieces
                if (piece == null || piece.getColor() != playerColor) {
                    continue;
                }

                // Check all possible destination squares for this piece
                if (pieceHasLegalMove(piece, col, row, board)) {
                    return true; // Found at least one legal move
                }
            }
        }

        return false; // No legal moves found
    }

    /**
     * Checks if a specific piece has any legal moves from its current position
     * @param piece The piece to check
     * @param pieceCol The piece's current column
     * @param pieceRow The piece's current row
     * @param board The current board state
     * @return true if the piece has at least one legal move
     */
    private boolean pieceHasLegalMove(Piece piece, int pieceCol, int pieceRow, Board board) {
        // Check all possible destination squares
        for (int targetRow = 0; targetRow < 8; targetRow++) {
            for (int targetCol = 0; targetCol < 8; targetCol++) {
                // Skip the piece's current position
                if (targetRow == pieceRow && targetCol == pieceCol) {
                    continue;
                }

                // Check if this move would be legal
                if (isMoveValid(piece, pieceCol, pieceRow, targetCol, targetRow, board, currentEnPassantTarget)) {
                    return true; // Found a legal move
                }
            }
        }

        return false; // No legal moves for this piece
    }

    /**
     * Checks the current game state (normal, check, checkmate, stalemate)
     * @param board The current board state
     * @param playerColor The color of the current player
     * @return String describing the game state
     */
    public String getGameState(Board board, int playerColor) {
        boolean inCheck = isKingInCheck(board, playerColor);
        boolean hasLegalMoves = hasAnyLegalMoves(board, playerColor);

        if (inCheck && !hasLegalMoves) {
            return "CHECKMATE";
        } else if (!inCheck && !hasLegalMoves) {
            return "STALEMATE";
        } else if (inCheck) {
            return "CHECK";
        } else {
            return "NORMAL";
        }
    }

    /**
     * Checks if the game has ended (checkmate or stalemate)
     * @param board The current board state
     * @param playerColor The color of the current player
     * @return true if the game has ended
     */
    public boolean isGameOver(Board board, int playerColor) {
        return isCheckmate(board, playerColor) || isStalemate(board, playerColor);
    }
}
