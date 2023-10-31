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
        Square initialSquare = getCurrentSquare();

        // 4 directions
        boolean directions[] = { true, false };
        Square candidate;
        Square nextCandidateSquare;
        for(boolean toBottom: directions) {
            for (boolean toRight : directions) {
                // Add free squares
                candidate = initialSquare.getLastFreeSquare(toBottom, toRight, true, true);
                moves.add(candidate);

                // Check if it can eat
                nextCandidateSquare = candidate.getNextNeighbor(toBottom, toRight, true, true);
                if(nextCandidateSquare != null && nextCandidateSquare.isOccupied()) {
                    Piece piece = nextCandidateSquare.getOccupyingPiece();
                    if(isEnemy(piece)) moves.add(nextCandidateSquare);
                }
            }
        }

        return moves;
    }

    public String toString() {
        return super.toString("B");
    }
}
