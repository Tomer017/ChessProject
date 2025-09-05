package com.chess.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.chess.board.Board;
import com.chess.ui.DrawBoard;


public class MouseHandler implements MouseListener {

    private final int squareSize;
    private int[] mousePressedPosition;
    private int[] mouseReleasedPosition;
    private gameHandler gameHandler;

    public MouseHandler(Board board, DrawBoard drawBoard, int squareSize, int boardSize) {
        this.squareSize = drawBoard.getSquareSize();
    }

    public int[] translateMousePosition(MouseEvent e){
        int[] position = new int[2];
        position[0] = e.getX() / squareSize;
        position[1] = e.getY() / squareSize;
        return position;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not utilized
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressedPosition = translateMousePosition(e);
        System.out.println("Mouse pressed at: " + mousePressedPosition[0] + ", " + mousePressedPosition[1]);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleasedPosition = translateMousePosition(e);
        System.out.println("Mouse released at: " + mouseReleasedPosition[0] + ", " + mouseReleasedPosition[1]);
        
        // Trigger move handling when mouse is released
        if (gameHandler != null && mousePressedPosition != null && mouseReleasedPosition != null) {
            gameHandler.handleMove();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Not utilized
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not utilized
    }

    public int[] getMousePressedPosition() {
        return mousePressedPosition;
    }

    public int[] getMouseReleasedPosition() {
        return mouseReleasedPosition;
    }

    public void setGameHandler(gameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
