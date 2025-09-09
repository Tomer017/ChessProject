package com.chess.utils;

import com.chess.board.Board;
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
    
    // Constants for colors
    private static final int WHITE = 8;
    private static final int BLACK = 16;
    private static final int EMPTY = 0;

    public gameHandler(Board board) {
        this.board = board;
        this.ruleHandler = new RuleHandler();
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

        // Check if the move is valid
        if (!ruleHandler.isMoveValid(startPiece, startCol, startRow, endCol, endRow)) {
            return;
        }

        // Check if trying to capture own piece
        Piece endPiece = board.getPiece(endRow, endCol);
        if (endPiece != null && endPiece.getColor() == currentTurn) {
            return;
        }

        // Make the move
        System.out.println("Moving piece from (" + startCol + "," + startRow + ") to (" + endCol + "," + endRow + ")");
        
        // Check if capturing an enemy piece
        if (endPiece != null && endPiece.getColor() != EMPTY && endPiece.getColor() != currentTurn) {
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

    // Drag methods
    public void startDrag(int row, int col) {
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == currentTurn) {
            isDragging = true;
            dragStartRow = row;
            dragStartCol = col;
            draggedPiece = piece;
            System.out.println("Started dragging piece at (" + col + "," + row + ")");
            
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
}
