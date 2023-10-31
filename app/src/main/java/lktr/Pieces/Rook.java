package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Rook extends Piece {
    public Rook(boolean white, Square initialSquare, String sprite) {
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

        boolean directions[] = { true, false };
        boolean xyAxis[][] = { { true, false }, { false, true } };
        Square candidate;
        Square nextCandidateSquare;
        for(boolean toRight: directions) {
            for(boolean toBottom: directions) {
                for (boolean[] axis : xyAxis) {
                    boolean xAxis = axis[0];
                    boolean yAxis = axis[1];

                    candidate = initialSquare.getLastFreeSquare(toBottom, toRight, xAxis, yAxis);
                    moves.add(candidate);

                    // Check if it can eat
                    nextCandidateSquare = candidate.getNextNeighbor(toBottom, toRight, xAxis, yAxis);
                    if(nextCandidateSquare != null && nextCandidateSquare.isOccupied()) {
                        Piece piece = nextCandidateSquare.getOccupyingPiece();
                        if(isEnemy(piece)) moves.add(nextCandidateSquare);
                    }
                }
            }
        }

        return moves;
    }

    public String toString() {
        return super.toString("R");
    }
}
