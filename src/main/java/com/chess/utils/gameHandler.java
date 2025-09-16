package com.chess.utils;

import com.chess.board.Board;
import com.chess.board.TranslateFen;
import com.chess.pieces.Piece;

public class gameHandler {
    private final Board board;
    private int currentTurn = 8; // 8 = white starts, 16 = black
    private MouseHandler mouseHandler;
    private Runnable renderCallback;
    private RuleHandler ruleHandler;
    
    // Drag state
    private boolean isDragging = false;
    private int dragStartRow = -1;
    private int dragStartCol = -1;
    private int dragX = 0;
    private int dragY = 0;
    private Piece draggedPiece = null;
    
    // En passant state
    private String enPassantTarget = "-";
    
    // Game state
    private boolean gameOver = false;
    private String gameResult = "";
    
    // Constants for colors
    private static final int WHITE = 8;
    private static final int BLACK = 16;
    private static final int EMPTY = 0;

    public gameHandler(Board board) {
        this.board = board;
        this.ruleHandler = new RuleHandler();
    }

    /**
     * Initializes the game state after board setup
     * Call this after setting up the initial board position
     */
    public void initializeGame() {
        gameOver = false;
        gameResult = "";
        currentTurn = WHITE;
        enPassantTarget = "-";
        ruleHandler.setEnPassantTarget(enPassantTarget);
        
        // Check initial game state (shouldn't be checkmate/stalemate at start)
        checkGameState();
        System.out.println("Game initialized! White to move.");
    }

    public void makeMove(int startCol, int startRow, int endCol, int endRow) {
        // Check if game is already over
        if (gameOver) {
            System.out.println("Game is over! " + gameResult);
            return;
        }

        // Check bounds
        if (!isValidPosition(startCol, startRow) || !isValidPosition(endCol, endRow)) {
            System.out.println("Invalid position: out of bounds");
            return;
        }

        Piece startPiece = board.getPiece(startRow, startCol);
        
        // Check if there's a piece at start position
        if (startPiece == null || startPiece.getColor() == EMPTY) {
            return;
        }

        // Check if it's the correct player's turn
        if (startPiece.getColor() != currentTurn) {
            return;
        }

        // Check if the move is valid (including en passant and check validation)
        if (!ruleHandler.isMoveValid(startPiece, startCol, startRow, endCol, endRow, board, enPassantTarget)) {
            System.out.println("Invalid move for piece type");
            return;
        }

        // Check if trying to capture own piece
        Piece endPiece = board.getPiece(endRow, endCol);
        if (endPiece != null && endPiece.getColor() == currentTurn) {
            return;
        }

        // Make the move

        // Check for en passant capture before moving
        boolean isEnPassantCapture = false;
        if (startPiece instanceof com.chess.pieces.Pawn) {
            com.chess.pieces.Pawn pawn = (com.chess.pieces.Pawn) startPiece;
            if (pawn.isEnPassantCapture(startCol, startRow, endCol, endRow, enPassantTarget)) {
                isEnPassantCapture = true;
                // Remove the captured pawn
                int[] capturedPawnPos = com.chess.pieces.Pawn.getEnPassantCapturedPawnPosition(enPassantTarget);
                if (capturedPawnPos != null) {
                    board.setPiece(capturedPawnPos[0], capturedPawnPos[1], new Piece(EMPTY, 0));
                    System.out.println("En passant capture! Removed pawn at (" + capturedPawnPos[1] + "," + capturedPawnPos[0] + ")");
                }
            }
        }
        
        // Check if capturing a regular piece
        if (!isEnPassantCapture && endPiece != null && endPiece.getColor() != EMPTY && endPiece.getColor() != currentTurn) {
        }
        
        // Execute the move
        board.movePiece(startRow, startCol, endRow, endCol);
        
        // Update en passant target for next turn
        updateEnPassantTarget(startPiece, startCol, startRow, endCol, endRow);
        
        // Switch turns
        currentTurn = (currentTurn == WHITE) ? BLACK : WHITE;
        System.out.println("Turn switched to: " + (currentTurn == WHITE ? "White" : "Black"));
        System.out.println("En passant target: " + enPassantTarget);
        
        // Check game state after the move
        checkGameState();
        
        // Trigger re-render
        if (renderCallback != null) {
            renderCallback.run();
        }
    }

    public void handleMove() {
        if (mouseHandler == null) {
            System.out.println("MouseHandler not set");
            return;
        }

        int[] pressedPos = mouseHandler.getMousePressedPosition();
        int[] releasedPos = mouseHandler.getMouseReleasedPosition();

        // Convert mouse coordinates to board coordinates
        // MouseHandler returns [x, y] where x=column, y=row
        System.out.println(TranslateFen.boardToFENString(board));
        makeMove(pressedPos[0], pressedPos[1], releasedPos[0], releasedPos[1]);
    }

    private boolean isValidPosition(int col, int row) {
        return col >= 0 && col < 8 && row >= 0 && row < 8;
    }

    public void setMouseHandler(MouseHandler mouseHandler) {
        this.mouseHandler = mouseHandler;
    }

    public void setRenderCallback(Runnable renderCallback) {
        this.renderCallback = renderCallback;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public String getCurrentTurnString() {
        return currentTurn == WHITE ? "White" : "Black";
    }

    public String getEnPassantTarget() {
        return enPassantTarget;
    }

    public void setEnPassantTarget(String enPassantTarget) {
        this.enPassantTarget = enPassantTarget != null ? enPassantTarget : "-";
        ruleHandler.setEnPassantTarget(this.enPassantTarget);
    }

    /**
     * Updates the en passant target square after a move
     */
    private void updateEnPassantTarget(Piece movedPiece, int startCol, int startRow, int endCol, int endRow) {
        // Reset en passant target by default
        enPassantTarget = "-";

        // Check if a pawn moved two squares
        if (movedPiece instanceof com.chess.pieces.Pawn) {
            int rowDiff = Math.abs(endRow - startRow);
            if (rowDiff == 2) {
                // Pawn moved two squares, set en passant target
                char file = (char) ('a' + endCol);
                int rank;
                
                if (movedPiece.getColor() == WHITE) {
                    // White pawn moved from rank 7 to rank 5, target is rank 6
                    rank = 8 - endRow - 1; // Convert board row to chess rank
                } else {
                    // Black pawn moved from rank 2 to rank 4, target is rank 3  
                    rank = 8 - endRow + 1; // Convert board row to chess rank
                }
                
                enPassantTarget = "" + file + rank;
                System.out.println("Pawn moved two squares, en passant target set to: " + enPassantTarget);
            }
        }

        // Update rule handler
        ruleHandler.setEnPassantTarget(enPassantTarget);
    }

    // Drag methods
    public void startDrag(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == currentTurn) {
            isDragging = true;
            dragStartRow = row;
            dragStartCol = col;
            draggedPiece = piece;
            
            // Trigger re-render to show drag state
            if (renderCallback != null) {
                renderCallback.run();
            }
        }
    }

    public void updateDragPosition(int x, int y) {
        if (isDragging) {
            dragX = x;
            dragY = y;
            
            // Trigger re-render to update drag position
            if (renderCallback != null) {
                renderCallback.run();
            }
        }
    }

    public void endDrag() {
        isDragging = false;
        draggedPiece = null;
        dragStartRow = -1;
        dragStartCol = -1;
        
        // Trigger re-render to clear drag state
        if (renderCallback != null) {
            renderCallback.run();
        }
    }

    // Getters for drag state
    public boolean isDragging() {
        return isDragging;
    }

    public int getDragStartRow() {
        return dragStartRow;
    }

    public int getDragStartCol() {
        return dragStartCol;
    }

    public int getDragX() {
        return dragX;
    }

    public int getDragY() {
        return dragY;
    }

    public Piece getDraggedPiece() {
        return draggedPiece;
    }

    /**
     * Checks if the current player is in check
     */
    public boolean isCurrentPlayerInCheck() {
        return ruleHandler.isKingInCheck(board, currentTurn);
    }

    /**
     * Checks if a specific color's king is in check
     */
    public boolean isKingInCheck(int color) {
        return ruleHandler.isKingInCheck(board, color);
    }

    /**
     * Gets the current board state for external access
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the rule handler for external access
     */
    public RuleHandler getRuleHandler() {
        return ruleHandler;
    }

    /**
     * Checks and updates the current game state
     */
    private void checkGameState() {
        String gameState = ruleHandler.getGameState(board, currentTurn);
        
        switch (gameState) {
            case "CHECKMATE":
                gameOver = true;
                String winner = (currentTurn == WHITE) ? "Black" : "White";
                gameResult = "CHECKMATE! " + winner + " wins!";
                System.out.println("=".repeat(50));
                System.out.println(gameResult);
                System.out.println("=".repeat(50));
                break;
                
            case "STALEMATE":
                gameOver = true;
                gameResult = "STALEMATE! Game is a draw!";
                System.out.println("=".repeat(50));
                System.out.println(gameResult);
                System.out.println("=".repeat(50));
                break;
                
            case "CHECK":
                System.out.println("CHECK! " + (currentTurn == WHITE ? "White" : "Black") + " king is in check!");
                break;
                
            case "NORMAL":
                // Game continues normally
                break;
        }
    }

    /**
     * Checks if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Gets the game result message
     */
    public String getGameResult() {
        return gameResult;
    }

    /**
     * Gets the current game state as a string
     */
    public String getCurrentGameState() {
        return ruleHandler.getGameState(board, currentTurn);
    }

    /**
     * Resets the game to initial state
     */
    public void resetGame() {
        gameOver = false;
        gameResult = "";
        currentTurn = WHITE;
        enPassantTarget = "-";
        ruleHandler.setEnPassantTarget(enPassantTarget);
        
        // Reset board (caller should reinitialize pieces)
        System.out.println("Game reset! White to move.");
    }

    /**
     * Checks if the current player has any legal moves
     */
    public boolean currentPlayerHasLegalMoves() {
        return ruleHandler.hasAnyLegalMoves(board, currentTurn);
    }

    /**
     * Gets detailed game information
     */
    public String getGameInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Current Turn: ").append(currentTurn == WHITE ? "White" : "Black").append("\n");
        info.append("Game State: ").append(getCurrentGameState()).append("\n");
        info.append("En Passant: ").append(enPassantTarget).append("\n");
        info.append("Game Over: ").append(gameOver).append("\n");
        if (gameOver) {
            info.append("Result: ").append(gameResult).append("\n");
        }
        return info.toString();
    }
}
