package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Pawn extends Piece {
    public Pawn(boolean white, Square initialSquare, String sprite) {
        super(white, initialSquare, sprite);
    }

    @Override
    public boolean move(Board board, Square ns) {
        // We give the final square where the pawn will be placed
        // not the square where the enemy pawn is
        Square bs = getBackwardSquare(ns);
        if (canEnPassant(board, bs)) {
            bs.removePiece();
        }
        boolean flag = super.move(board, ns);
        return flag;
    }


    /**
     * If the pawn has not been moved once, then it can either move forward by 1 or 2 squares.
     * Otherwise, it will always move forward by 1 square.
     * In both cases, it is assumed that no piece stands in front of the pawn.
     */
    @Override
    public ArrayList<Square> getLegalMoves(Board board) {
        ArrayList<Square> moves = new ArrayList<Square>();
        
        Square cs = getSquare();
        // NOTE: pointless to think of null value when
        // dealing with forward squares
        Square fs = getForwardSquare();
        Square ffs = getDoubleForwardSquare();

        // Double move
        if(!hasBeenMoved() && !cs.isYLinearlyOccupied(ffs)) moves.add(ffs);
        if (!fs.isOccupied()) moves.add(fs);

        // Eat
        Square[] diagonals = {fs.getLeftNeighbor(), fs.getRightNeighbor()};
        for(Square s: diagonals) {
            if (s == null || !s.isOccupied()) continue;
            Piece p = s.getOccupyingPiece();
            if(isEnemy(p)) moves.add(s);
        }

        // En-passant
        Square[] sides = {cs.getLeftNeighbor(), cs.getRightNeighbor()};
        for(Square s: sides) {
            if (canEnPassant(board, s)) moves.add(getForwardSquare(s));
        }

        return moves;
    }

    /**
     * Detect if this pawn can en-passant.
     * The square where the pawn has done a double move.
     */
    private boolean canEnPassant(Board board, Square ns) {
        Square s = getSquare();
        if(ns == null || !ns.isOccupied() || !(s.getLeftNeighbor() == ns || s.getRightNeighbor() == ns)) return false;

        Piece p = ns.getOccupyingPiece();
        Square psp = p.getPreviousSquare();

        // Check if it is a pawn and
        // if it has been moved
        if(p instanceof Pawn && psp != null && p.getRound() == board.getRound() - 1) {
            boolean hasDoubleMoved = Math.abs(psp.getY() - p.getSquare().getY()) == 2;
            if (isEnemy(p) && hasDoubleMoved)
                return true;
        }

        return false;
    }

    /**
     * Get the forward square in front of the given square.
     * It may be null as well when hitting the border.
     */
    public Square getForwardSquare(Square s) {
        int dy = isWhite() ? -1 : 1;
        return s.neighborY(dy);
    }

    public Square getForwardSquare() {
        return getForwardSquare(getSquare());
    }

    /**
     * Get the backward square in front of the given square.
     * It may be null as well when hitting the border.
     */
    public Square getBackwardSquare(Square s) {
        int dy = isWhite() ? -1 : 1;
        return s.neighborY(-dy);
    }

    public Square getBackwardSquare() {
        return getBackwardSquare(getSquare());
    }

    /**
     * Get the double forward square in front of the given square.
     * It may be null as well when hitting the border.
     */
    public Square getDoubleForwardSquare(Square s) {
        Square fs = getForwardSquare(s);
        if(fs == null) return null;
        Square ffs = getForwardSquare(fs);
        return ffs;
    }

    public Square getDoubleForwardSquare() {
        return getDoubleForwardSquare(getSquare());
    }
    
    public String toString() {
        return super.toString("P");
    }
}
