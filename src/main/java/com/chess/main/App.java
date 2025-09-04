package com.chess.main;

import java.awt.Graphics;

import javax.swing.JFrame;

import com.chess.board.Board;
import com.chess.ui.DrawBoard;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);

        Board board = new Board();
        DrawBoard drawBoard = new DrawBoard();
        Graphics g = frame.getGraphics();

        // initialize the board
        board.initializeBoard();
        g.clearRect(0, 0, 1280, 720);
        drawBoard.drawBoard(board, g);

        board.printBoard();
    }
}