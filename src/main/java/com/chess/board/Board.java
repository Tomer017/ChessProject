package com.chess.board;

import com.chess.pieces.Piece;

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

    public Board() {
        this.BOARD = new Piece[8][8];
    }

    public void initializeBoard() {
        // initialize the board with the pieces
    }
}
