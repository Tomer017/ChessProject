package com.chess.engine;

import com.chess.board.Board;
import com.chess.pieces.Piece;
import com.chess.utils.RuleHandler;

public class Engine {
    private final Board board;
    private final RuleHandler ruleHandler;
    
    private final int WHITE = 8;
    private final int BLACK = 16;
    private final int EMPTY = 0;
    private final int PAWN = 1;
    private final int KNIGHT = 2;
    private final int BISHOP = 3;
    private final int ROOK = 4;
    private final int QUEEN = 5;
    private final int KING = 6;

    private final int[][] PIECE_VALUES = {
        {100, 300, 300, 500, 900, 10000},
        {100, 300, 300, 500, 900, 10000}
    };
    
    private int currentTurn = WHITE; // 8 = white starts, 16 = black
    private double evaluation = 0;



    public Engine(Board board) {
        this.board = board;
        this.ruleHandler = new RuleHandler();
        this.evaluation = 0;
        this.currentTurn = 8;
    }

    public double evaluatePosition(Board board) {
        // evaluate the position
        for (int i = 0; i < Board.ROWS; i++) {
            for (int j = 0; j < Board.COLS; j++) {
                Piece piece = board.getPiece(i, j);
                if (piece != null) {
                    evaluation += PIECE_VALUES[piece.getColor()][piece.getPIECE_TYPE()];
                }
            }
        }
        return evaluation;
    }

}
