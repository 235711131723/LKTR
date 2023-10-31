package lktr.Pieces;

import java.util.ArrayList;
import lktr.Square;
import lktr.Board;

public class Pawn extends Piece {
    public Pawn(boolean white, Square initialSquare, String sprite) {
        super(white, initialSquare, sprite);
    }

    @Override
    public void move(Board board, Square newSquare) {
        // We give the final square where the pawn will be placed
        // not the square where the enemy pawn is
        if (canEnPassant(board, true) || canEnPassant(board, false)) {
            Square lowerSquare = getBackSquare(newSquare);
            // Eat the pawn
            lowerSquare.removePiece();
        }
        super.move(board, newSquare);
    }


    /**
     * If the pawn has not been moved once, then it can either move forward by 1 or 2 squares.
     * Otherwise, it will always move forward by 1 square.
     * In both cases, it is assumed that no piece stands in front of the pawn.
     */
    @Override
    public ArrayList<Square> getLegalMoves(Board board) {
        ArrayList<Square> moves = new ArrayList<Square>();

        Square frontSquare = getFrontSquare();
        if (!frontSquare.isOccupied())
            moves.add(frontSquare);
        
        if (canDoubleMove())
            moves.add(getFrontSquare(frontSquare));

        boolean xAxis[] = { true, false };
        for(boolean toRight: xAxis){
            Square frontDiagonal = getFrontDiagonalSquare(toRight);
            if (canEat(toRight))
                moves.add(frontDiagonal);

            if (canEnPassant(board, toRight))
                moves.add(frontDiagonal);
        }

        return moves;
    }

    private Square getFrontDiagonalSquare(boolean toRight) {
        return getFrontDiagonalSquare(getCurrentSquare(), toRight);
    }

    private Square getFrontDiagonalSquare(Square square, boolean toRight) {
        Square s = getFrontSquare(square);
        if (s == null)
            return null;
        s = s.getNextNeighborX(toRight);
        return s;
    }
    
    private Square getFrontSquare() {
        return getFrontSquare(getCurrentSquare());
    }
    
    private Square getFrontSquare(Square square) {
        if (square == null)
            return null;
        return square.getNextNeighborY(!white);
    }

    private Square getBackSquare() {
        return getFrontSquare(getCurrentSquare());
    }
    
    private Square getBackSquare(Square square) {
        if (square == null)
            return null;
        return square.getNextNeighborY(white);
    }

    private boolean canEat(boolean toRight) {
        Square frontDiagonal = getFrontDiagonalSquare(toRight);
        if (frontDiagonal == null || !frontDiagonal.isOccupied())
            return false;
        Piece piece = frontDiagonal.getOccupyingPiece();
        if (!isEnemy(piece))
            return false;

        return true;
    }

    /**
     * Check if the pawn can double move.
     */
    private boolean canDoubleMove() {
        if(hasBeenMoved()) return false;

        Square currentSquare = getCurrentSquare();
        for(int i = 0; i < 2; i++) {
            currentSquare = getFrontSquare(currentSquare);
            if(currentSquare.isOccupied()) return false;
        }

        return true;
    }

    /**
     * Detect if this pawn can en-passant.
     * The provided square is the one where the pawn will be at the end of the move.
     */
    private boolean canEnPassant(Board board, boolean toRight) {
        Square currentSquare = getCurrentSquare();
        // The square where the enemy pawn is
        // next to the current pawn
        Square lowerSquare = currentSquare.getNextNeighborX(toRight);
        if (lowerSquare == null || !lowerSquare.isOccupied())
            return false;

        Piece piece = lowerSquare.getOccupyingPiece();
        if (!piece.hasBeenMoved())
            return false;
        Square previousMovePiece = piece.getPreviousSquare();

        // Check if it is a pawn and
        // if it has been moved
        boolean previousMove = piece.getRound() == board.getRound() - 1;
        boolean hasDoubleMoved =  Math.abs(previousMovePiece.y - piece.getCurrentSquare().y) == 2;
        if(piece instanceof Pawn && isEnemy(piece) && hasBeenMoved() && previousMove && hasDoubleMoved) {
            return true;
        }

        return false;
    }

    public String toString() {
        return super.toString("P");
    }
}
