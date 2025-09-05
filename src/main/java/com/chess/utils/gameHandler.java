package com.chess.utils;

import com.chess.board.Board;
import com.chess.pieces.Piece;

public class gameHandler {
    private final Board board;
    private int currentTurn = 8; // 8 = white starts, 16 = black
    private MouseHandler mouseHandler;
    private Runnable renderCallback;
    
    // Constants for colors
    private static final int WHITE = 8;
    private static final int BLACK = 16;
    private static final int EMPTY = 0;

    public gameHandler(Board board) {
        this.board = board;
    }

    public void makeMove(int startCol, int startRow, int endCol, int endRow) {
        // Check bounds
        if (!isValidPosition(startCol, startRow) || !isValidPosition(endCol, endRow)) {
            System.out.println("Invalid position: out of bounds");
            return;
        }

        Piece startPiece = board.getPiece(startRow, startCol);
        
        // Check if there's a piece at start position
        if (startPiece == null || startPiece.getColor() == EMPTY) {
            System.out.println("No piece at start position");
            return;
        }

        // Check if it's the correct player's turn
        if (startPiece.getColor() != currentTurn) {
            System.out.println("Not your turn! Current turn: " + (currentTurn == WHITE ? "White" : "Black"));
            return;
        }

        // Check if trying to capture own piece
        Piece endPiece = board.getPiece(endRow, endCol);
        if (endPiece != null && endPiece.getColor() == currentTurn) {
            System.out.println("Cannot capture your own piece");
            return;
        }

        // Make the move
        System.out.println("Moving piece from (" + startCol + "," + startRow + ") to (" + endCol + "," + endRow + ")");
        
        // Check if capturing an enemy piece
        if (endPiece != null && endPiece.getColor() != EMPTY && endPiece.getColor() != currentTurn) {
            System.out.println("Capturing " + (endPiece.getColor() == WHITE ? "white" : "black") + " piece");
        }
        
        board.movePiece(startRow, startCol, endRow, endCol);
        
        // Switch turns
        currentTurn = (currentTurn == WHITE) ? BLACK : WHITE;
        System.out.println("Turn switched to: " + (currentTurn == WHITE ? "White" : "Black"));
        
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
        
        if (pressedPos == null || releasedPos == null) {
            System.out.println("Mouse positions not available");
            return;
        }

        // Convert mouse coordinates to board coordinates
        // MouseHandler returns [x, y] where x=column, y=row
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
}
