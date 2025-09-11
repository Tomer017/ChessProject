package com.chess.board;

import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;

/*
 * this class is used to represent the chess board
 * the board is a 8x8 matrix of pieces
 * the board is initialized with the pieces in their starting positions
 * the board is used to store the pieces and their positions
 * the board is used to validate the moves of the pieces
 * the board is used to update the board after a move
 * the board is used to get the pieces from the board
 * the board is used to set the pieces on the board
 */
public class Board {
    
    // the board is a 8x8 matrix of pieces
    private Piece[][] BOARD;

    // use binary to represent the pieces and their colors
    private final int EMPTY = 0;

    public static final int ROWS = 8;
    public static final int COLS = 8;

    public Board() {
        this.BOARD = new Piece[ROWS][COLS];
    }

    public Piece getPiece(int x, int y) {
        return BOARD[x][y];
    }

    public void initializeBoard() {
        // load the starting position
        this.loadFenPosition(TranslateFen.getStartingPositionFen());
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        // Check if there's a piece at the start position
        if (BOARD[startX][startY] != null && BOARD[startX][startY].getColor() != EMPTY) {
            Piece movingPiece = BOARD[startX][startY];
            
            // If it's a pawn, mark it as no longer on first move
            if (movingPiece instanceof Pawn) {
                ((Pawn) movingPiece).setFirstMove(false);
            }
            
            // Move piece (capturing if there's an enemy piece at destination)
            BOARD[endX][endY] = movingPiece;
            BOARD[startX][startY] = new Piece(EMPTY, 0); // Set start position to empty
        }
    }

    public void setPiece(int x, int y, Piece piece) {
        BOARD[x][y] = piece;
    }

    public boolean isEmptySquare(int x, int y) {
        return BOARD[x][y] == null || BOARD[x][y].getColor() == EMPTY;
    }

    public void loadFenPosition(String fen) {
        this.BOARD = TranslateFen.translateFen(fen).BOARD;
    }
}
