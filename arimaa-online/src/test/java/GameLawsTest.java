import Client.Model.*;
import org.junit.jupiter.api.*;

import java.util.HashSet;

/**
 * GameLawsTest: Provides 5 JUnit tests for checking if rules where implemented correctly.
 */
public class GameLawsTest {

    private Board board;
    private Player player1;
    private Player player2;

    /**
     * testEnvironment: Initializes testing environment.
     */
    @BeforeEach
    public void testEnvironment(){
        player1 = new Player(1,"Player1", false);
        player2 = new Player(2,"Player2", false);
        board = new Board();
    }

    /**
     * stepTest: Tests the troops a player can step with based on the board state.
     */
    @Test
    public void stepTest(){
        String[][] stepBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "D", "", "", "", ""},
                {"", "", "r", "C", "r", "", "", ""},
                {"", "", "", "r", "", "", "", ""},
                {"", "", "", "C", "", "", "", ""},
                {"", "", "", "", "", "", "", ""}
        };
        board.populateBoardFrom2DString(stepBoard, player1, player2);

        HashSet<Offset2D> expectedPositions = new HashSet<>();
        expectedPositions.add(new Offset2D(3,3));
        expectedPositions.add(new Offset2D(6,3));

        Assertions.assertEquals(expectedPositions, new HashSet<>(board.canStepPositions(player1)));
    }

    /**
     * winTest: Tests various win conditions based on the board state.
     */
    @Test
    public void winTest(){

        // Test case 1: Golden player wins by reaching the end of the board
        String[][] winBoard = new String[][]{
                {"", "", "", "R", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertTrue(board.isWinner(player1, player2));

        // Test case 2: Golden player loses as enemy rabbit reaches beginning of the board
        winBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "r", "", "", ""},
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertFalse(board.isWinner(player1, player2));

        // Test case 3: Golden player wins when no rabbits are left on silver side
        winBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "d", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "R", "", "", ""}
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertTrue(board.isWinner(player1, player2));

        // Test case 4: Golden player loses when no rabbits are left on his side
        winBoard = new String[][]{
                {"", "", "", "", "r", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "D", "", "", ""},
                {"", "", "", "", "", "", "", ""}
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertFalse(board.isWinner(player1, player2));

        // Test case 5: Golden player wins when all silver troops are frozen
        winBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "r", "D", "", "", ""},
                {"", "R", "", "", "", "", "", ""}
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertTrue(board.isWinner(player1, player2));

        // Test case 6: Golden player loses when all his troops are frozen
        winBoard = new String[][]{
                {"", "", "", "", "", "r", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "C", "d", "", "R", "c"},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""}
        };
        board.populateBoardFrom2DString(winBoard, player1, player2);
        Assertions.assertFalse(board.isWinner(player1, player2));
    }

    /**
     * complexMoveTest: Tests that only viable pieces are selected for push and pull moves.
     */
    @Test
    public void complexMoveTest(){
        String[][] testBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "D", "c", "", "", "", ""},
                {"", "", "", "D", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "M", "h"},
                {"", "", "", "", "", "", "", "E"}
        };
        board.populateBoardFrom2DString(testBoard, player1, player2);
        HashSet<Offset2D> expectedPositions = new HashSet<>();

        // Test case 1: Valid positions for pieces that can be pulled
        expectedPositions.add(new Offset2D(3,3));
        expectedPositions.add(new Offset2D(6,7));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.canPullPositions(player2)));
        expectedPositions.clear();

        //Test case 2: Testing where can silver cat be pulled or positions of pieces that can make a push on it
        expectedPositions.add(new Offset2D(3,2));
        expectedPositions.add(new Offset2D(4,3));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePullingPieces(new Offset2D(3,3))));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePushingPieces(new Offset2D(3,3))));
        expectedPositions.clear();

        //Test case 3: Testing where can silver horse be pulled or positions of pieces that can make a push on it
        expectedPositions.add(new Offset2D(7,7));
        expectedPositions.add(new Offset2D(6,6));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePullingPieces(new Offset2D(6,7))));
        Assertions.assertEquals(expectedPositions, new HashSet<>(board.getPositionsOfPossiblePushingPieces(new Offset2D(6,7))));
    }

    /**
     * freezeTest: Tests if troops are correctly identified as frozen based on the board state.
     */
    @Test
    public void freezeTest(){

        // Test case 1: Rabbit is frozen near a stronger enemy troop
        String[][] freezeBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "R", "d", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(freezeBoard, player1, player2);
        Assertions.assertTrue(board.isTroopFrozen(new Offset2D(6,3)));

        // Test case 2: Rabbit is no longer frozen when near a friendly troop
        freezeBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "E", "R", "d", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(freezeBoard, player1, player2);
        Assertions.assertFalse(board.isTroopFrozen(new Offset2D(6,3)));

        // Test case 3: Rabbit is not frozen near a same-strength enemy troop
        freezeBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "R", "r", "", "", ""},
                {"", "", "", "", "", "", "", ""},
        };
        board.populateBoardFrom2DString(freezeBoard, player1, player2);
        Assertions.assertFalse(board.isTroopFrozen(new Offset2D(6,3)));
    }

    /**
     * directionTest: Tests if troops have the correct valid movement directions based on the board state.
     */
    @Test
    public void directionsTest(){

        String[][] directionsBoard = new String[][]{
                {"", "", "", "", "", "", "", ""},
                {"", "r", "", "d", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "", "", "", ""},
                {"", "", "", "", "D", "", "R", ""},
                {"", "", "", "", "", "", "", ""},
        };

        board.populateBoardFrom2DString(directionsBoard, player1, player2);

        HashSet<Directions> expectedDirections = new HashSet<>();
        expectedDirections.add(Directions.NORTH);
        expectedDirections.add(Directions.LEFT);
        expectedDirections.add(Directions.SOUTH);
        expectedDirections.add(Directions.RIGHT);

        // Test case 1: Golden dog valid directions
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getTileAt(new Offset2D(1,3)).troopValidDirections()));
        // Test case 2: Silver dog valid directions
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getTileAt(new Offset2D(6,4)).troopValidDirections()));

        // Test case 3: Golden rabbit valid directions (excluding South)
        expectedDirections.remove(Directions.SOUTH);
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getTileAt(new Offset2D(6,6)).troopValidDirections()));
        // Test case 4: Silver rabbit valid directions (excluding North)
        expectedDirections.add(Directions.SOUTH);
        expectedDirections.remove(Directions.NORTH);
        Assertions.assertEquals(expectedDirections, new HashSet<>(board.getTileAt(new Offset2D(1,1)).troopValidDirections()));
    }
}
