package com.chess.pieces;

public class Knight extends Piece {

    public Knight(int color) {
        super(color, 3);
    }

    @Override
    public int getPIECE_TYPE() {
        return 3;
    }

    @Override
    public boolean isLegalMove(int startCol, int startRow, int endCol, int endRow) {
        // Knight moves in L-shape: 2 squares in one direction, 1 square in perpendicular direction
        int colDiff = Math.abs(endCol - startCol);
        int rowDiff = Math.abs(endRow - startRow);
        
        // Valid knight moves: (2,1) or (1,2) in any direction
        if ((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2)) {
            return true;
        }
        
        return false;
    }
}