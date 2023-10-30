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
        Square is = getSquare();

        boolean bools[] = { true, false };
        Square cs = is;
        for(boolean toRight: bools) {
            for(boolean toBottom: bools) {
                // X-axis
                cs = is;
                while((cs = cs.getNextNeighborX(toRight)) != null) {
                    if (!cs.isOccupied())
                        moves.add(cs);
                    else {
                        Piece p = cs.getOccupyingPiece();
                        if (isEnemy(p))
                            moves.add(cs);
                        else {
                            break;
                        }
                    }
                }

                // Y-axis
                cs = is;
                while((cs = cs.getNextNeighborY(toBottom)) != null) {
                    if (!cs.isOccupied())
                        moves.add(cs);
                    else {
                        Piece p = cs.getOccupyingPiece();
                        if (isEnemy(p))
                            moves.add(cs);
                        else {
                            break;
                        }
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
