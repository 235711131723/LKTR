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
     * The last square the piece was placed on.
     */
    private Square previousSquare = null;

    /**
     * The side of the player, either white or black.
     */
    public final boolean white;
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

    public boolean hasBeenMoved() {
        return previousSquare != null;
    }

    public Square getPreviousSquare() {
        return previousSquare;
    }

    public int getRound() {
        return round;
    }

    public boolean isEnemy(Piece piece) {
        return (piece.white != white);
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }
    
    public Image getSprite() {
        return sprite;
    }
    
    /**
     * Move the piece by specifying a square.
     * @param ns The new square where to place the piece.
     * @return Whenever the move has been a success or not.
     */
    public void move(Board board, Square newSquare) {
        // Save the previous position
        previousSquare = this.currentSquare;

        // Displace the piece
        currentSquare.removePiece();
        newSquare.placePiece(this);
        currentSquare = newSquare;

        // Mark the n-th round
        // NOTE: the piece is moved before the move is saved
        // in the history
        round = board.getRound();
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
        return white ? shortname.toUpperCase() : shortname.toLowerCase();
    }
}