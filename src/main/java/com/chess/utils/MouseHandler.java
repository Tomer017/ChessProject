package com.chess.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.chess.board.Board;
import com.chess.ui.DrawBoard;


public class MouseHandler implements MouseListener, MouseMotionListener {

    private final int squareSize;
    private int[] mousePressedPosition;
    private int[] mouseReleasedPosition;
    private int[] currentMousePosition;
    private boolean isDragging = false;
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
        currentMousePosition = new int[]{e.getX(), e.getY()}; // Store actual pixel coordinates
        isDragging = true;
        System.out.println("Mouse pressed at: " + mousePressedPosition[0] + ", " + mousePressedPosition[1]);
        
        // Notify gameHandler that dragging started
        if (gameHandler != null) {
            gameHandler.startDrag(mousePressedPosition[1], mousePressedPosition[0]); // row, col
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleasedPosition = translateMousePosition(e);
        isDragging = false;
        System.out.println("Mouse released at: " + mouseReleasedPosition[0] + ", " + mouseReleasedPosition[1]);
        
        // Notify gameHandler that dragging stopped
        if (gameHandler != null) {
            gameHandler.endDrag();
        }
        
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

    // MouseMotionListener methods
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            currentMousePosition = new int[]{e.getX(), e.getY()};
            // Update drag position in gameHandler
            if (gameHandler != null) {
                gameHandler.updateDragPosition(currentMousePosition[0], currentMousePosition[1]);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not utilized
    }

    // Getters for drag state
    public boolean isDragging() {
        return isDragging;
    }

    public int[] getCurrentMousePosition() {
        return currentMousePosition;
    }
}
