package com.chess.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.chess.board.Board;
import com.chess.utils.gameHandler;

public class DrawBoard {

    private final int BOARD_SIZE = 800;
    private final int SQUARE_SIZE = BOARD_SIZE / 8;
    private final BufferedImage boardImage;
    private final BufferedImage blackPieceImages[];
    private final BufferedImage whitePieceImages[];

    public DrawBoard() {
        this.boardImage = ImageLoader.loadImageFromResources("/images/brown_board.png");
        this.blackPieceImages = new BufferedImage[6];
        this.whitePieceImages = new BufferedImage[6];

        // Load piece images: 1=pawn, 2=rook, 3=knight, 4=bishop, 5=queen, 6=king
        for (int i = 0; i < 6; i++) {
            int pieceNumber = i + 1; // Convert 0-based index to 1-based piece type
            this.blackPieceImages[i] = ImageLoader.loadImageFromResources("/images/pieces/black/" + pieceNumber + ".png");
            this.whitePieceImages[i] = ImageLoader.loadImageFromResources("/images/pieces/white/" + pieceNumber + ".png");
        }
    }

    public void drawBoard(Board board, Graphics g) {
        drawBoard(board, g, null);
    }

    public void drawBoard(Board board, Graphics g, gameHandler gameHandler) {
        // Draw the board image
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, BOARD_SIZE, BOARD_SIZE, null);
        }

        // Draw all pieces in a single loop
        // i = row (0=top, 7=bottom), j = column (0=left, 7=right)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var piece = board.getPiece(i, j);
                
                // Skip drawing the piece if it's currently being dragged
                boolean isBeingDragged = gameHandler != null && gameHandler.isDragging() && 
                                       gameHandler.getDragStartRow() == i && gameHandler.getDragStartCol() == j;
                
                // Only draw if piece exists, is not empty, and is not being dragged
                if (piece != null && piece.getColor() != 0 && !isBeingDragged) {
                    BufferedImage pieceImage = getPieceImage(piece);
                    if (pieceImage != null) {
                        // Draw at (column * SQUARE_SIZE, row * SQUARE_SIZE)
                        g.drawImage(pieceImage, j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, null);
                    }
                }
            }
        }

        // Draw the dragged piece at cursor position
        if (gameHandler != null && gameHandler.isDragging() && gameHandler.getDraggedPiece() != null) {
            BufferedImage draggedPieceImage = getPieceImage(gameHandler.getDraggedPiece());
            if (draggedPieceImage != null) {
                // Draw centered on cursor
                int dragX = gameHandler.getDragX() - SQUARE_SIZE / 2;
                int dragY = gameHandler.getDragY() - SQUARE_SIZE / 2;
                g.drawImage(draggedPieceImage, dragX, dragY, SQUARE_SIZE, SQUARE_SIZE, null);
            }
        }
    }

    private BufferedImage getPieceImage(com.chess.pieces.Piece piece) {
        int pieceType = piece.getPIECE_TYPE();
        int color = piece.getColor();
        
        // Ensure piece type is within valid range (1-6: pawn, rook, knight, bishop, queen, king)
        if (pieceType < 1 || pieceType > 6) {
            return null;
        }
        
        // Convert piece type (1-6) to array index (0-5)
        int arrayIndex = pieceType - 1;
        
        // color 8 = white, color 16 = black
        switch (color) {
            case 8:
                return whitePieceImages[arrayIndex];
            case 16:
                return blackPieceImages[arrayIndex];
            default:
                return null; // Unknown color
        }
    }

    public int getSquareSize() {
        return SQUARE_SIZE;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }
}
