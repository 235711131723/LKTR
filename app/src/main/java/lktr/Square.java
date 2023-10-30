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
    
    private int x;
    private int y;
    
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

    /**
     * Get the square from the relative dy-position.
     * Returns null if the neighbor is out of the box.
     */
    public Square neighborY(int dy) {
        Square[][] squares = board.getBoard();
        if (y + dy < 0 || y + dy >= 8)
            return null;
        return squares[y + dy][x];
    }

    /**
     * Get the square from the relative dx-position.
     * Returns null if the neighbor is out of the box.
     */
    public Square neighborX(int dx) {
        Square[][] squares = board.getBoard();
        if (x + dx < 0 || x + dx >= 8)
            return null;
        return squares[y][x + dx];
    }

    public Square getNextNeighborX(boolean toRight) {
        int dx = toRight ? 1 : -1;
        return neighborX(dx);
    }

    public Square getNextNeighborY(boolean toBottom) {
        int dy = toBottom ? 1 : -1;
        return neighborY(dy);
    }

    public Square getDiagonalNeighbor(boolean toBottom, boolean toRight) {
        Square ny = getNextNeighborY(toBottom);
        if (ny == null)
            return null;
        return ny.getNextNeighborX(toRight);
    }

    public boolean isWhite() {
        return white;
    }
    
    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }
    
    public boolean isOccupied() {
        return (occupyingPiece != null);
    }

    /**
     * Check if there is a piece between this square and another
     * starting from the current square to the given square.
     * on the Y-axis.
     */
    public boolean isYLinearlyOccupied(Square s) {
        // Swap squares if the current one is below the given one
        // as we want to check from top to bot for more simplicity
        boolean toBottom = getY() <= s.getY();
        Square currentSquare = this;
        while (currentSquare != s) {
            currentSquare = currentSquare.getNextNeighborY(toBottom);
            if (currentSquare == null)
                return false;
            if (currentSquare.isOccupied())
                return true;
        }

        return false;
    }

    /**
     * Check if there is a piece between this square and another
     * starting from the current square to the given square.
     * on the Y-axis.
     */
    public boolean isXLinearlyOccupied(Square s) {
        // Swap squares if the current one is below the given one
        // as we want to check from left to right for more simplicity
        boolean toRight = getX() <= s.getX();
        Square currentSquare = this;
        while (currentSquare != s) {
            currentSquare = currentSquare.getNextNeighborX(toRight);
            if (currentSquare == null)
                return false;
            if (currentSquare.isOccupied())
                return true;
        }

        return false;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void place(Piece piece) {
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
    
    public void capture(Piece p) {
        Piece k = getOccupyingPiece();
        if (k.isWhite()) board.blackPieces.remove(k);
        if (!k.isWhite()) board.whitePieces.remove(k);
        this.occupyingPiece = p;
    }

    public String toString() {
        char[] letters = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };
        return String.format("%c%d", letters[getX()], (7 - getY() + 1));
    }
}