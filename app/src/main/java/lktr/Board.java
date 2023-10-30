package lktr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.LinkedList;
import lktr.Pieces.Piece;
import lktr.Pieces.Pawn;

public class Board {
    // Sprite of each piece
    private static final String RESOURCES_WBISHOP_PNG = "/wbishop.png";
	private static final String RESOURCES_BBISHOP_PNG = "/bbishop.png";
	private static final String RESOURCES_WKNIGHT_PNG = "/wknight.png";
	private static final String RESOURCES_BKNIGHT_PNG = "/bknight.png";
	private static final String RESOURCES_WROOK_PNG = "/wrook.png";
	private static final String RESOURCES_BROOK_PNG = "/brook.png";
	private static final String RESOURCES_WKING_PNG = "/wking.png";
	private static final String RESOURCES_BKING_PNG = "/bking.png";
	private static final String RESOURCES_BQUEEN_PNG = "/bqueen.png";
	private static final String RESOURCES_WQUEEN_PNG = "/wqueen.png";
	private static final String RESOURCES_WPAWN_PNG = "/wpawn.png";
	private static final String RESOURCES_BPAWN_PNG = "/bpawn.png";
	
    private Square[][] board = new Square[8][8];
    public final LinkedList<Piece> blackPieces = new LinkedList<Piece>();
    public final LinkedList<Piece> whitePieces = new LinkedList<Piece>();
    private boolean whiteTurn = true;
    private ArrayList<Square[]> history = new ArrayList<Square[]>();

    /**
     * Place squares to reproduce the chess board (alternating between white and black).
     */
    private void placeSquares() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int xMod = x % 2;
                int yMod = y % 2;
                boolean whiteSquare = (xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1);
                board[y][x] = new Square(this, whiteSquare, x, y);
            }
        }
    }

    /**
     * Place pieces like bishops, pawns upon squares.
     */
    private void placePieces() {
        // Pawns
        for (int x = 0; x < 8; x++) {
            // Black pawns
            board[1][x].place(new Pawn(false, board[1][x], RESOURCES_BPAWN_PNG));
            // White pawns
            board[6][x].place(new Pawn(true, board[6][x], RESOURCES_WPAWN_PNG));
        }
    }

    public Board() {
        placeSquares();
        placePieces();
        
        // Add every respective pieces
        // after placing each and every one of them
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                blackPieces.add(board[y][x].getOccupyingPiece());
                whitePieces.add(board[8-(y+1)][x].getOccupyingPiece());
            }
        }
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void display() {
        String[][] pieces = new String[8][8];

        // Draw pieces
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Square s = board[y][x];
                Piece p = s.getOccupyingPiece();
                pieces[y][x] = (p == null) ? " " : p.toString();
            }
        }

        // Show coordinates
        for (int y = 0; y < 8; y++) {
            System.out.print(String.format("%d%c", 7 - y + 1, '|'));
            for (int x = 0; x < 8; x++) {
                System.out.print(String.format("%s|", pieces[y][x] == null ? " " : pieces[y][x]));
            }
            System.out.println();
        }
        System.out.print("  ");
        for (int x = 0; x < 8; x++) {
            System.out.print(String.format("%c ", (char)(65 + x)));
        }
        System.out.println();
    }

    public boolean checkCode(String c) {
        Pattern pattern = Pattern.compile("^[a-h][0-9]$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(c);
        return matcher.find();
    }

    public Square getSquareFromCode(String c) {
        if (!checkCode(c)) throw new RuntimeException("Code " + c + " is invalid.");
        c = c.toUpperCase();
        int x = c.charAt(0) - 'A';
        int y = 8 - Integer.parseInt(String.valueOf(c.charAt(1)));

        Square s = board[y][x];
        return s;
    }
    
    public void play(String c1, String c2) throws RuntimeException {
        Square s1 = getSquareFromCode(c1);
        Square s2 = getSquareFromCode(c2);

        if (!s1.isOccupied()) {
            throw new RuntimeException("There is no piece on " + s1 + ".");
        }

        Piece p = s1.getOccupyingPiece();
        if (p.isWhite() != whiteTurn) {
            throw new RuntimeException(String.format("%s is not in your side.", p));
        }

        ArrayList<Square> moves = p.getLegalMoves(this);
        if (!moves.contains(s2)) {
            throw new RuntimeException(String.format("%s => %s is an illegal move.", s1, s2));
        }

        Square[] move = { s1, s2 };
        p.move(this, s2);
        history.add(move);

        whiteTurn = !whiteTurn;
    }

    public int getRound() {
        return history.size() + 1;
    }

    public ArrayList<Square[]> getHistory() {
        return history;
    }
}