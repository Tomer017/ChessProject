package com.chess.main;

import javax.swing.JFrame;

import com.chess.board.Board;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setVisible(true);

        Board board = new Board();
        board.initializeBoard();
    }
}