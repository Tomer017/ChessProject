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
 * 
 * Reference: https://www.chess.com/terms/fen-chess
 */
public class TranslateFen {
    
    private static final int BLACK = 16;
    private static final int WHITE = 8;
    private static final int EMPTY = 0;

    /**
     * Represents the complete state of a chess position from FEN
     */
    public static class FenData {
        private Board board;
        private int activeColor;           // WHITE or BLACK
        private boolean whiteCanCastleKingside;
        private boolean whiteCanCastleQueenside;
        private boolean blackCanCastleKingside;
        private boolean blackCanCastleQueenside;
        private String enPassantTarget;    // Square behind the pawn that moved two squares (e.g., "e3")
        private int halfmoveClock;         // Moves since last pawn move or capture (for 50-move rule)
        private int fullmoveNumber;        // Number of completed turns (incremented after Black moves)

        public FenData(Board board) {
            this.board = board;
            this.activeColor = WHITE; // Default to white
            this.whiteCanCastleKingside = true;
            this.whiteCanCastleQueenside = true;
            this.blackCanCastleKingside = true;
            this.blackCanCastleQueenside = true;
            this.enPassantTarget = "-";
            this.halfmoveClock = 0;
            this.fullmoveNumber = 1;
        }

        // Getters
        public Board getBoard() { return board; }
        public int getActiveColor() { return activeColor; }
        public boolean canWhiteCastleKingside() { return whiteCanCastleKingside; }
        public boolean canWhiteCastleQueenside() { return whiteCanCastleQueenside; }
        public boolean canBlackCastleKingside() { return blackCanCastleKingside; }
        public boolean canBlackCastleQueenside() { return blackCanCastleQueenside; }
        public String getEnPassantTarget() { return enPassantTarget; }
        public int getHalfmoveClock() { return halfmoveClock; }
        public int getFullmoveNumber() { return fullmoveNumber; }

        // Setters
        public void setActiveColor(int activeColor) { this.activeColor = activeColor; }
        public void setWhiteCastleKingside(boolean canCastle) { this.whiteCanCastleKingside = canCastle; }
        public void setWhiteCastleQueenside(boolean canCastle) { this.whiteCanCastleQueenside = canCastle; }
        public void setBlackCastleKingside(boolean canCastle) { this.blackCanCastleKingside = canCastle; }
        public void setBlackCastleQueenside(boolean canCastle) { this.blackCanCastleQueenside = canCastle; }
        public void setEnPassantTarget(String target) { this.enPassantTarget = target; }
        public void setHalfmoveClock(int clock) { this.halfmoveClock = clock; }
        public void setFullmoveNumber(int number) { this.fullmoveNumber = number; }
    }

    /**
     * Translates a FEN string into a new Board object (piece placement only)
     * @param fen The FEN string to translate
     * @return A new Board object representing the position
     */
    public static Board translateFen(String fen) {
        return translateFen(fen, new Board());
    }

    /**
     * Translates a FEN string into an existing Board object (piece placement only)
     * @param fen The FEN string to translate
     * @param board The existing board to populate
     * @return The populated Board object
     */
    public static Board translateFen(String fen, Board board) {
        FenData fenData = translateFenComplete(fen, board);
        return fenData.getBoard();
    }

    /**
     * Translates a complete FEN string into a FenData object containing all chess state
     * @param fen The complete FEN string to translate
     * @return A FenData object containing the board and all game state
     */
    public static FenData translateFenComplete(String fen) {
        return translateFenComplete(fen, new Board());
    }

    /**
     * Translates a complete FEN string into a FenData object with existing board
     * @param fen The complete FEN string to translate
     * @param board The existing board to populate
     * @return A FenData object containing the board and all game state
     */
    public static FenData translateFenComplete(String fen, Board board) {
        if (fen == null || fen.isEmpty()) {
            throw new IllegalArgumentException("FEN string cannot be null or empty");
        }

        String[] parts = fen.split(" ");
        
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid FEN format - need at least piece placement");
        }

        FenData fenData = new FenData(board);

        // 1. Parse piece placement (first field)
        parsePiecePlacement(parts[0], board);

        // 2. Parse active color (second field)
        if (parts.length > 1) {
            parseActiveColor(parts[1], fenData);
        }

        // 3. Parse castling rights (third field)
        if (parts.length > 2) {
            parseCastlingRights(parts[2], fenData);
        }

        // 4. Parse en passant target (fourth field)
        if (parts.length > 3) {
            parseEnPassantTarget(parts[3], fenData);
        }

        // 5. Parse halfmove clock (fifth field)
        if (parts.length > 4) {
            parseHalfmoveClock(parts[4], fenData);
        }

        // 6. Parse fullmove number (sixth field)
        if (parts.length > 5) {
            parseFullmoveNumber(parts[5], fenData);
        }

        return fenData;
    }

    public static String boardToFENString(Board board) {
        return boardToPiecePlacement(board);
    }

    /**
     * Parses the piece placement part of FEN string
     */
    private static void parsePiecePlacement(String piecePlacement, Board board) {
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
    }

    /**
     * Parses the active color field (w or b)
     */
    private static void parseActiveColor(String activeColorStr, FenData fenData) {
        if ("w".equals(activeColorStr)) {
            fenData.setActiveColor(WHITE);
        } else if ("b".equals(activeColorStr)) {
            fenData.setActiveColor(BLACK);
        } else {
            throw new IllegalArgumentException("Invalid active color: " + activeColorStr + " (must be 'w' or 'b')");
        }
    }

    /**
     * Parses the castling rights field (KQkq or combinations, or -)
     */
    private static void parseCastlingRights(String castlingStr, FenData fenData) {
        // Reset all castling rights to false
        fenData.setWhiteCastleKingside(false);
        fenData.setWhiteCastleQueenside(false);
        fenData.setBlackCastleKingside(false);
        fenData.setBlackCastleQueenside(false);

        if (!"-".equals(castlingStr)) {
            for (char c : castlingStr.toCharArray()) {
                switch (c) {
                    case 'K':
                        fenData.setWhiteCastleKingside(true);
                        break;
                    case 'Q':
                        fenData.setWhiteCastleQueenside(true);
                        break;
                    case 'k':
                        fenData.setBlackCastleKingside(true);
                        break;
                    case 'q':
                        fenData.setBlackCastleQueenside(true);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid castling character: " + c);
                }
            }
        }
    }

    /**
     * Parses the en passant target field (algebraic notation or -)
     */
    private static void parseEnPassantTarget(String enPassantStr, FenData fenData) {
        if ("-".equals(enPassantStr)) {
            fenData.setEnPassantTarget("-");
        } else {
            // Validate algebraic notation (e.g., "e3", "d6")
            if (enPassantStr.length() == 2 && 
                enPassantStr.charAt(0) >= 'a' && enPassantStr.charAt(0) <= 'h' &&
                enPassantStr.charAt(1) >= '1' && enPassantStr.charAt(1) <= '8') {
                fenData.setEnPassantTarget(enPassantStr);
            } else {
                throw new IllegalArgumentException("Invalid en passant target: " + enPassantStr);
            }
        }
    }

    /**
     * Parses the halfmove clock field
     */
    private static void parseHalfmoveClock(String halfmoveStr, FenData fenData) {
        try {
            int halfmove = Integer.parseInt(halfmoveStr);
            if (halfmove < 0) {
                throw new IllegalArgumentException("Halfmove clock cannot be negative: " + halfmove);
            }
            fenData.setHalfmoveClock(halfmove);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid halfmove clock: " + halfmoveStr);
        }
    }

    /**
     * Parses the fullmove number field
     */
    private static void parseFullmoveNumber(String fullmoveStr, FenData fenData) {
        try {
            int fullmove = Integer.parseInt(fullmoveStr);
            if (fullmove < 1) {
                throw new IllegalArgumentException("Fullmove number must be at least 1: " + fullmove);
            }
            fenData.setFullmoveNumber(fullmove);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid fullmove number: " + fullmoveStr);
        }
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
        return boardToPiecePlacement(board);
    }

    /**
     * Converts a FenData object to a complete FEN string
     * @param fenData The FenData object containing all game state
     * @return The complete FEN string
     */
    public static String fenDataToString(FenData fenData) {
        StringBuilder fen = new StringBuilder();
        
        // 1. Piece placement
        fen.append(boardToPiecePlacement(fenData.getBoard()));
        fen.append(' ');
        
        // 2. Active color
        fen.append(fenData.getActiveColor() == WHITE ? 'w' : 'b');
        fen.append(' ');
        
        // 3. Castling rights
        StringBuilder castling = new StringBuilder();
        if (fenData.canWhiteCastleKingside()) castling.append('K');
        if (fenData.canWhiteCastleQueenside()) castling.append('Q');
        if (fenData.canBlackCastleKingside()) castling.append('k');
        if (fenData.canBlackCastleQueenside()) castling.append('q');
        fen.append(castling.length() > 0 ? castling.toString() : "-");
        fen.append(' ');
        
        // 4. En passant target
        fen.append(fenData.getEnPassantTarget());
        fen.append(' ');
        
        // 5. Halfmove clock
        fen.append(fenData.getHalfmoveClock());
        fen.append(' ');
        
        // 6. Fullmove number
        fen.append(fenData.getFullmoveNumber());
        
        return fen.toString();
    }

    /**
     * Converts board piece placement to FEN piece placement string
     * @param board The board to convert
     * @return The piece placement part of FEN
     */
    private static String boardToPiecePlacement(Board board) {
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
