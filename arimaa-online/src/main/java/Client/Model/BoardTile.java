package Client.Model;

public interface BoardTile extends Tile{
    BoardTile EMPTY = new BoardTile() {
        @Override
        public boolean canStepOn() {
            return true;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }
    };

    BoardTile TRAP = new BoardTile() {
        @Override
        public boolean canStepOn() {
            return false;
        }

        @Override
        public boolean hasTroop() {
            return false;
        }
    };

}
