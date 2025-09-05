package com.chess.utils;

import java.awt.Graphics;

import com.chess.board.Board;
import com.chess.ui.DrawBoard;

public class Render {
    private final Board board;
    private final DrawBoard drawBoard;
    private gameHandler gameHandler;

    public Render(Board board, DrawBoard drawBoard) {
        this.board = board;
        this.drawBoard = drawBoard;
    }

    public void setGameHandler(gameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void renderBoard(Graphics g) {
        drawBoard.drawBoard(board, g, gameHandler);
    }
}
