package Client.Model;

import java.util.ArrayList;

public class Board {
    public static final int BOARD_SIZE = 8;
    private final Troop[][] board;
    public Board() {
        board = new Troop[BOARD_SIZE][BOARD_SIZE];
    }
    public void placePiece(Troop piece, Position position) {
        board[position.row()][position.column()] = piece;
    }
    public Troop getPieceAt(Position position) {
        return board[position.row()][position.column()];
    }
    public void removePieceAt(Position position) {
        board[position.row()][position.column()] = null;
    }
    public boolean isOffBoardPosition(Position position) {}
    public void makeMove(Move move) {}
    public Boolean hasPlayerWon(Player player, Player enemy){}
    public ArrayList<Position> getPositionsOfPlayersPieces(Player player){}
    public boolean isNextTo(Position position){}
    public boolean isPositionEmpty(Position position){
        return getPieceAt(position) == null;
    }


}
