package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Knight extends Piece {
    public Knight(boolean white, Square initialSquare, String sprite) {
        super(white, initialSquare, sprite);
    }

    /**
     * A method to get the L square from the current square.
     * The wideAxisX parameter tells the method to either jump two squares
     * from the X-axis first, or Y-axis.
     */
    public Square[] getLSquare(boolean toBottom, boolean toRight, boolean wideAxisX) {
        Square[] squares = { null, null };

        boolean xAxis;
        boolean yAxis;
        if (wideAxisX) {
            xAxis = true;
            yAxis = false;
        } else {
            xAxis = false;
            yAxis = true;
        }

        Square l = getCurrentSquare();
        for (int i = 0; i < 2; i++) {
            l = l.getNextNeighbor(toBottom, toRight, xAxis, yAxis);
            if (l == null)
                return squares;
        }
        squares[0] = l.getNextNeighbor(toBottom, toRight, yAxis, xAxis);
        squares[1] = l.getNextNeighbor(toBottom, toRight, yAxis, xAxis);

        return squares;
    }

    public ArrayList<Square> getAllLSquares() {
        ArrayList<Square> squares = new ArrayList<Square>();

        boolean directions[] = { true, false };
        Square candidates[];
        for (boolean toRight : directions) {
            for (boolean toBottom : directions) {
                for (boolean side : directions) {
                    candidates = getLSquare(toBottom, toRight, side);

                    for (Square candidate : candidates) {
                        if (candidate == null)
                            continue;
                        squares.add(candidate);
                    }
                }
            }
        }

        return squares;
    }

    /**
     * If the pawn has not been moved once, then it can either move forward by 1 or 2 squares.
     * Otherwise, it will always move forward by 1 square.
     * In both cases, it is assumed that no piece stands in front of the pawn.
     */
    @Override
    public ArrayList<Square> getLegalMoves(Board board) {
        ArrayList<Square> moves = new ArrayList<Square>();
        
        for (Square candidate : getAllLSquares()) {
            if (!candidate.isOccupied())
                moves.add(candidate);
            else {
                Piece piece = candidate.getOccupyingPiece();
                if (isEnemy(piece))
                    moves.add(candidate);
            }
        }

        return moves;
    }

    public String toString() {
        return super.toString("K");
    }
}