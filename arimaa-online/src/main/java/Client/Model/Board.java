package Client.Model;

public class Board {
    private final BoardTile[][] board;
    private final int dimension;

    // Constructor will create a squared board with "dimension" attribute and set all the tiles to BoardTile.EMPTY
    public Board(int dimension) {
        this.dimension = dimension;
        board = new BoardTile[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = BoardTile.EMPTY;
            }
        }
    }

    public int dimension() {
        return dimension;
    }

    // Return tile with position
    public BoardTile at(TilePos pos) {
        return board[pos.i()][pos.j()];
    }

    // Create a new a board after move
    public Board withTiles(TileAt... ats) {
        //TODO
        return new Board(8);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}
