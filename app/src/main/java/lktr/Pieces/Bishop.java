package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Bishop extends Piece {
    public Bishop(boolean white, Square initialSquare, String sprite) {
        super(white, initialSquare, sprite);
    }

    /**
     * If the pawn has not been moved once, then it can either move forward by 1 or 2 squares.
     * Otherwise, it will always move forward by 1 square.
     * In both cases, it is assumed that no piece stands in front of the pawn.
     */
    @Override
    public ArrayList<Square> getLegalMoves(Board board) {
        ArrayList<Square> moves = new ArrayList<Square>();
        Square is = getSquare();

        // 4 directions
        // respectively to bottom, and to right
        boolean directions[] = { true, false };
        Square cs = is;
        for(boolean toBottom: directions) {
            for (boolean toRight : directions) {
                // Looping until border is hit
                // or another piece detected
                while ((cs = cs.getDiagonalNeighbor(toBottom, toRight)) != null) {
                    // Free square
                    if (!cs.isOccupied()) moves.add(cs);
                    // Enemy so we can fucking eat it
                    if (cs.isOccupied()) {
                        Piece p = cs.getOccupyingPiece();
                        if (isEnemy(p))
                            moves.add(cs);
                        else
                            break;
                    }
                }
                cs = is;
            }
        }

        return moves;
    }

    public String toString() {
        return super.toString("B");
    }
}
