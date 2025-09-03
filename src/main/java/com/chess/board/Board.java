package com.chess.board;

import com.chess.pieces.Bishop;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;

// this class is used to represent the chess board
// the board is a 8x8 matrix of pieces
// the board is initialized with the pieces in their starting positions
// the board is used to store the pieces and their positions
// the board is used to validate the moves of the pieces
// the board is used to update the board after a move
// the board is used to get the pieces from the board
// the board is used to set the pieces on the board
public class Board {
    private final Piece[][] BOARD;
    private final int BLACK = 16;
    private final int WHITE = 8;
    private final int EMPTY = 0;

    public static final int ROWS = 8;
    public static final int COLS = 8;

    public Board() {
        this.BOARD = new Piece[ROWS][COLS];
    }

    public void initializeBoard() {
        // initalize black pieces
        BOARD[0][0] = new Rook(BLACK);
        BOARD[0][1] = new Knight(BLACK);
        BOARD[0][2] = new Bishop(BLACK);
        BOARD[0][3] = new Queen(BLACK);
        BOARD[0][4] = new King(BLACK);
        BOARD[0][5] = new Bishop(BLACK);
        BOARD[0][6] = new Knight(BLACK);
        BOARD[0][7] = new Rook(BLACK);

        // initalize black pawns
        for (int i = 0; i < COLS; i++) {
            BOARD[1][i] = new Pawn(BLACK);
        }

        // initalize white pieces
        BOARD[7][0] = new Rook(WHITE);
        BOARD[7][1] = new Knight(WHITE);
        BOARD[7][2] = new Bishop(WHITE);
        BOARD[7][3] = new Queen(WHITE);
        BOARD[7][4] = new King(WHITE);
        BOARD[7][5] = new Bishop(WHITE);
        BOARD[7][6] = new Knight(WHITE);
        BOARD[7][7] = new Rook(WHITE);

        // initalize white pawns
        for (int i = 0; i < COLS; i++) {
            BOARD[6][i] = new Pawn(WHITE);
        }

        // initalize empty squares
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < COLS; j++) {
                BOARD[i][j] = new Piece(EMPTY, 0);
            }
        }
    }
}
