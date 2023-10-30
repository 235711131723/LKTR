package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Knight extends Piece {
    public Knight(boolean white, Square initialSquare, String sprite) {
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
        Square cs;
        for(boolean toRight: bools) {
            for(boolean toBottom: bools) {
                // Code for only the quarter
                // Loops to avoid writing too much code
                Square ss[] = new Square[2];
                
                cs = is.getNextNeighborY(toBottom);
                if(cs == null) continue;
                cs = cs.getNextNeighborY(toBottom);
                if(cs == null) continue;
                cs = cs.getNextNeighborX(toRight);
                if(cs == null) continue;
                ss[0] = cs;

                cs = cs.getNextNeighborX(toRight);
                if(cs == null) continue;
                cs = cs.getNextNeighborX(toRight);
                if(cs == null) continue;
                cs = is.getNextNeighborY(toBottom);
                if(cs == null) continue;
                ss[1] = cs;

                for (Square fs : ss) {
                    if(!fs.isOccupied()) {
                        moves.add(fs);
                    } else {
                        Piece p = cs.getOccupyingPiece();
                        if (isEnemy(p))
                            moves.add(fs);
                    }
                }
            }
        }

        return moves;
    }

    public String toString() {
        return super.toString("K");
    }
}