package com.chess.board;

import com.chess.pieces.Bishop;
import com.chess.pieces.King;
import com.chess.pieces.Knight;
import com.chess.pieces.Pawn;
import com.chess.pieces.Piece;
import com.chess.pieces.Queen;
import com.chess.pieces.Rook;

/**
 * Utility class for translating FEN (Forsyth-Edwards Notation) strings to Board objects
 * 
 * FEN Format: "pieces active_color castling en_passant halfmove fullmove"
 * Example starting position: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
 */
public class TranslateFen {
    
    private static final int BLACK = 16;
    private static final int WHITE = 8;
    private static final int EMPTY = 0;

    /**
     * Translates a FEN string into a Board object
     * @param fen The FEN string to translate
     * @return A Board object representing the position
     */
    public static Board translateFen(String fen) {
        if (fen == null || fen.isEmpty()) {
            throw new IllegalArgumentException("FEN string cannot be null or empty");
        }

        Board board = new Board();
        String[] parts = fen.split(" ");
        
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid FEN format");
        }

        // Parse the piece placement (first part of FEN)
        String piecePlacement = parts[0];
        String[] rows = piecePlacement.split("/");
        
        if (rows.length != 8) {
            throw new IllegalArgumentException("FEN must have exactly 8 rows");
        }

        // Process each row (rank)
        for (int row = 0; row < 8; row++) {
            String rankString = rows[row];
            int col = 0;
            
            // Process each character in the rank
            for (int i = 0; i < rankString.length(); i++) {
                char c = rankString.charAt(i);
                
                if (Character.isDigit(c)) {
                    // Numbers represent empty squares
                    int emptySquares = Character.getNumericValue(c);
                    for (int j = 0; j < emptySquares; j++) {
                        if (col < 8) {
                            board.setPiece(row, col, new Piece(EMPTY, 0));
                            col++;
                        }
                    }
                } else {
                    // Letters represent pieces
                    if (col < 8) {
                        Piece piece = createPieceFromChar(c);
                        board.setPiece(row, col, piece);
                        col++;
                    }
                }
            }
        }

        return board;
    }

    /**
     * Creates a piece object from a FEN character
     * @param c The character representing the piece
     * @return The corresponding Piece object
     */
    private static Piece createPieceFromChar(char c) {
        switch (c) {
            // Black pieces (lowercase)
            case 'p':
                return new Pawn(BLACK);
            case 'r':
                return new Rook(BLACK);
            case 'n':
                return new Knight(BLACK);
            case 'b':
                return new Bishop(BLACK);
            case 'q':
                return new Queen(BLACK);
            case 'k':
                return new King(BLACK);
            
            // White pieces (uppercase)
            case 'P':
                return new Pawn(WHITE);
            case 'R':
                return new Rook(WHITE);
            case 'N':
                return new Knight(WHITE);
            case 'B':
                return new Bishop(WHITE);
            case 'Q':
                return new Queen(WHITE);
            case 'K':
                return new King(WHITE);
            
            default:
                return new Piece(EMPTY, 0);
        }
    }

    /**
     * Translates a Board object back to a FEN string (piece placement only)
     * @param board The board to translate
     * @return The FEN piece placement string
     */
    public static String boardToFen(Board board) {
        StringBuilder fen = new StringBuilder();
        
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                
                if (piece == null || piece.getColor() == EMPTY) {
                    emptyCount++;
                } else {
                    // Add empty squares count if any
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    
                    // Add piece character
                    fen.append(getPieceChar(piece));
                }
            }
            
            // Add remaining empty squares at end of row
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            
            // Add row separator (except for last row)
            if (row < 7) {
                fen.append('/');
            }
        }
        
        return fen.toString();
    }

    /**
     * Gets the FEN character representation of a piece
     * @param piece The piece to convert
     * @return The FEN character for the piece
     */
    private static char getPieceChar(Piece piece) {
        char baseChar;
        switch (piece.getPIECE_TYPE()) {
            case 1:
                baseChar = 'p';  // Pawn
                break;
            case 2:
                baseChar = 'r';  // Rook
                break;
            case 3:
                baseChar = 'n';  // Knight
                break;
            case 4:
                baseChar = 'b';  // Bishop
                break;
            case 5:
                baseChar = 'q';  // Queen
                break;
            case 6:
                baseChar = 'k';  // King
                break;
            default:
                baseChar = ' ';
                break;
        }
        
        // Uppercase for white, lowercase for black
        return piece.getColor() == WHITE ? Character.toUpperCase(baseChar) : baseChar;
    }

    /**
     * Gets the standard starting position FEN string
     * @return The FEN string for the initial chess position
     */
    public static String getStartingPositionFen() {
        return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    }
}
