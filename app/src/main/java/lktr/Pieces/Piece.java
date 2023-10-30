package lktr.Pieces;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import lktr.Square;
import lktr.Board;

public abstract class Piece {
    /**
     * Set n-th move.
     */
    private int round;

    /**
     * The piece has been moved at least once.
     */
    private boolean wasMoved = false;

    /**
     * The last square the piece was placed on.
     */
    private Square previousSquare = null;

    /**
     * The side of the player, either white or black.
     */
    private final boolean white;
    private BufferedImage sprite;
    private Square currentSquare;
    
    /**
     * Prototype constructor to create a piece.
     * @param white Tells if the piece belongs to the white side or not.
     * @param initialSquare The square where to place the piece at first.
     * @param sprite The associated sprite to the piece.
     */
    public Piece(boolean white, Square initialSquare, String sprite) {
        this.white = white;
        this.currentSquare = initialSquare;
        
        try {
            if (this.sprite == null) {
                this.sprite = ImageIO.read(getClass().getResource(sprite));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
    
    /**
     * Move the piece by specifying a square.
     * @param ns The new square where to place the piece.
     * @return Whenever the move has been a success or not.
     */
    public boolean move(Board board, Square ns) {
        // Save the previous position
        this.previousSquare = this.currentSquare;

        // Displace the piece
        this.currentSquare.removePiece();
        ns.place(this);
        this.currentSquare = ns;

        // Mark the piece as moved
        this.wasMoved = true;

        // Mark the n-th round
        // NOTE: the piece is moved before the move is saved
        // in the history
        this.round = board.getRound();
        return true;
    }

    public Square getSquare() {
        return currentSquare;
    }
    
    /**
     * Returns a boolean telling if the piece belongs to the white side or not.
     */
    public boolean isWhite() {
        return white;
    }
    
    public Image getSprite() {
        return sprite;
    }

    public boolean isEnemy(Piece p) {
        return (p.isWhite() != isWhite());
    }
    
    /**
     * Prototype method to return every square where the piece
     * can be placed on while respecting rules.
     * @param board The current board.
     * @return A list of squares.
     */
    public abstract ArrayList<Square> getLegalMoves(Board board);

    /**
     * A shortname will be shown.
     * Uppercase if white, lowercase otherwise.
     */
    public String toString(String shortname) {
        return isWhite() ? shortname.toUpperCase() : shortname.toLowerCase();
    }

    public boolean hasBeenMoved() {
        return wasMoved;
    }

    public Square getPreviousSquare() {
        return previousSquare;
    }

    public int getRound() {
        return round;
    }
}