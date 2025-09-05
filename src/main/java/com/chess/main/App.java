package com.chess.main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.chess.board.Board;
import com.chess.ui.DrawBoard;
import com.chess.utils.MouseHandler;
import com.chess.utils.Render;
import com.chess.utils.gameHandler;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        Board board = new Board();
        DrawBoard drawBoard = new DrawBoard();
        Render render = new Render(board, drawBoard);
        gameHandler gameHandler = new gameHandler(board);

        // Create a custom JPanel for rendering
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                render.renderBoard(g);
            }
        };

        frame.add(gamePanel);
        frame.setVisible(true);

        // Initialize the board
        board.initializeBoard();
        board.printBoard();

        // Set up mouse handler
        MouseHandler mouseHandler = new MouseHandler(board, drawBoard, drawBoard.getSquareSize(), drawBoard.getBoardSize());
        gameHandler.setMouseHandler(mouseHandler);
        mouseHandler.setGameHandler(gameHandler);
        gamePanel.addMouseListener(mouseHandler);

        // Set up render callback to repaint the panel when a move is made
        gameHandler.setRenderCallback(() -> {
            gamePanel.repaint();
            System.out.println("Board re-rendered!");
        });

        // Initial render
        gamePanel.repaint();
    }
}