package lktr;

import lktr.Pieces.Piece;

public class Square {
    /**
     * The color as it is shown on the board.
     * It is not linked at all to the color of the piece.
     */
    private final boolean white;
    private final Board board;
    
    private Piece occupyingPiece;
    
    public final int x;
    public final int y;
    
    /**
     * 
     * @param board The board to play on.
     * @param white The color as it will be displayed on the board.
     * @param x x-th square on the board, starting from left at 0.
     * @param y y-th square on the board, starting from top at 0.
     */
    public Square(Board board, boolean white, int x, int y) {
        this.board = board;
        this.white = white;
        this.x = x;
        this.y = y;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean isOccupied() {
        return (occupyingPiece != null);
    }

    /**
     * Get the square from the relative positions.
     * Returns null if the neighbor is out of the box.
     */
    public Square getNeighbor(int dx, int dy) {
        Square[][] squares = board.getBoard();
        if (x + dx < 0 || x + dx >= 8 || y + dy < 0 || y + dy >= 8)
            return null;
        return squares[y + dy][x + dx];
    }

    /**
     * Get the neighbor next to the current square based on booleans
     * indicating directions.
     * 
     * Directions can be either taken in account or ignored with parameters xAxis and yAxis.
     */
    public Square getNextNeighbor(boolean toBottom, boolean toRight, boolean xAxis, boolean yAxis) {
        int dx = xAxis ? (toRight ? 1 : -1) : 0;
        int dy = yAxis ? (toBottom ? 1 : -1) : 0;
        return getNeighbor(dx, dy);
    }

    public Square getNextNeighborY(boolean toBottom) {
        return getNextNeighbor(toBottom, true, false, true);
    }

    public Square getNextNeighborX(boolean toRight) {
        return getNextNeighbor(true, toRight, true, false);
    }

    public Square getDiagonalNeighbor(boolean toBottom, boolean toRight) {
        return getNextNeighbor(toBottom, toRight, true, true);
    }

    public Square getLastFreeSquare(boolean toBottom, boolean toRight, boolean xAxis, boolean yAxis) {
        Square currentSquare = this;
        Square newSquare = null;
        while (currentSquare != null) {
            newSquare = currentSquare.getNextNeighbor(toBottom, toRight, xAxis, yAxis);
            if(newSquare == null || newSquare.isOccupied()) return currentSquare; 
            currentSquare = newSquare;
        }

        return currentSquare;
    }

    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }
    
    /**
     * Replace the current piece by the new one.
     */
    public void placePiece(Piece piece) {
        this.occupyingPiece = piece;
    }

    /**
     * Set the occupying piece as null,
     * then return the removed piece.
     * @return The piece that was on the square.
     */
    public Piece removePiece() {
        Piece piece = this.occupyingPiece;
        this.occupyingPiece = null;
        return piece;
    }
    
    public void capture(Piece newPiece) {
        Piece currentPiece = getOccupyingPiece();
        if (currentPiece.white) board.blackPieces.remove(currentPiece);
        if (!currentPiece.white) board.whitePieces.remove(currentPiece);
        this.occupyingPiece = newPiece;
    }

    public String toString() {
        char[] letters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        return String.format("%c%d", letters[x], (8 - y));
    }
}