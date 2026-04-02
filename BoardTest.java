import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
	Board b;
	Board board1x1;
	Board board2x20;
	Board board10x2;
	Board board10x24;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, stick1, stick2, l11, s11, s21, sq;

	@BeforeEach
	protected void setUp() throws Exception {
		init();
		placePieces();
	}

	protected void init() {
		b = new Board(3, 6);
		board1x1 = new Board(1, 1);
		board2x20 = new Board(2, 20);
		board10x2 = new Board(10, 2);
		board10x24 = new Board(10, 24);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		sq = new Piece(Piece.SQUARE_STR);
		setUpStick();
		l11 = new Piece(Piece.L1_STR);
		s11 = new Piece(Piece.S1_STR);
		s21 = new Piece(Piece.S2_STR);
	}

	protected void placePieces() {
		b.place(pyr1, 0, 0);
		b.commit();
		board2x20.place(sq, 0, 0);
		board2x20.commit();
		board10x2.place(stick2, 0, 0);
		board10x2.commit();
		board10x2.place(sq, 5, 0);
		board10x2.commit();
		board10x24.place(l11, 0, 0);
		board10x24.commit();
		board10x24.place(s11, 2, 0);
		board10x24.commit();
	}

	public void setUpStick() {
		stick1 = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();
	}

	@Test
	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	@Test
	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	@Test
	public void testBoardBConstructor() {
		assertEquals(3, b.getWidth());
		assertEquals(6, b.getHeight());
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertTrue(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 0));
		assertTrue(b.getGrid(2, 0));
		assertTrue(b.getGrid(1, 1));
		assertFalse(b.getGrid(0, 1));
	}

	@Test
	public void testBoard1x1Constructor() {
		assertEquals(1, board1x1.getWidth());
		assertEquals(1, board1x1.getHeight());
		assertEquals(0, board1x1.getMaxHeight());
		assertEquals(0, board1x1.getRowWidth(0));
		assertFalse(board1x1.getGrid(0, 0));
	}

	@Test
	public void testBoard2x20Constructor() {
		assertEquals(2, board2x20.getWidth());
		assertEquals(20, board2x20.getHeight());
		assertEquals(2, board2x20.getMaxHeight());
		assertEquals(2, board2x20.getRowWidth(0));
		assertEquals(2, board2x20.getRowWidth(1));
		assertEquals(0, board2x20.getRowWidth(2));
		assertTrue(board2x20.getGrid(0, 0));
		assertTrue(board2x20.getGrid(1, 0));
		assertTrue(board2x20.getGrid(0, 1));
		assertTrue(board2x20.getGrid(1, 1));
		assertFalse(board2x20.getGrid(0, 2));
	}

	@Test
	public void testBoard10x2Constructor() {
		assertEquals(10, board10x2.getWidth());
		assertEquals(2, board10x2.getHeight());
		assertEquals(2, board10x2.getMaxHeight());
		assertEquals(6, board10x2.getRowWidth(0));
		assertEquals(2, board10x2.getRowWidth(1));
		assertTrue(board10x2.getGrid(0, 0));
		assertTrue(board10x2.getGrid(3, 0));
		assertFalse(board10x2.getGrid(0, 1));
		assertFalse(board10x2.getGrid(4, 0));
		assertTrue(board10x2.getGrid(5, 0));
		assertTrue(board10x2.getGrid(6, 1));
	}

	@Test
	public void testBoard10x24Constructor() {
		assertEquals(10, board10x24.getWidth());
		assertEquals(24, board10x24.getHeight());
		assertEquals(3, board10x24.getMaxHeight());
		assertEquals(4, board10x24.getRowWidth(0));
		assertEquals(3, board10x24.getRowWidth(1));
		assertEquals(1, board10x24.getRowWidth(2));
		assertTrue(board10x24.getGrid(0, 2));
		assertFalse(board10x24.getGrid(1, 2));
		assertFalse(board10x24.getGrid(2, 1));
		assertTrue(board10x24.getGrid(2, 0));
		assertTrue(board10x24.getGrid(3, 1));
	}

	@Test
	public void testDropHeight10x2() {
		assertEquals(2, board10x2.dropHeight(stick1, 5));
		assertEquals(1, board10x2.dropHeight(s11, 1));
		assertEquals(0, board10x2.dropHeight(pyr1, 8));
	}

	@Test
	public void testDropHeight2x20() {
		assertEquals(2, board2x20.dropHeight(sq, 0));
		assertEquals(2, board2x20.dropHeight(stick1, 1));
	}

	@Test
	public void testDropHeight10x24() {
		assertEquals(2, b.dropHeight(pyr3, 0));
		assertEquals(2, b.dropHeight(pyr1, 0));
		board10x24.place(sq, 2, 2);
		board10x24.commit();
		board10x24.place(sq, 6, 0);
		board10x24.commit();
		assertEquals(2, board10x24.dropHeight(stick1, 4));
		assertEquals(4, board10x24.dropHeight(stick2, 3));
	}

	@Test
	public void testDropHeight10x24ComplexSkirtFit() {
		assertEquals(0, board10x24.dropHeight(s21, 7));
		board10x24.place(stick2, 5, 0); board10x24.commit();
		board10x24.place(sq, 5, 1); board10x24.commit();
		assertEquals(2, board10x24.dropHeight(s21, 6));
	}

	@Test
	public void testClearRowsBBasic() {
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		int cleared = b.clearRows();
		assertEquals(1, cleared);
		assertEquals(1, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(0, b.getRowWidth(1));
		assertFalse(b.getGrid(0, 0));
		assertTrue(b.getGrid(1, 0));
		assertFalse(b.getGrid(2, 0));
	}

	@Test
	public void testClearRows2x20MultipleRows() {
		assertEquals(2, board2x20.getMaxHeight());
		assertEquals(2, board2x20.getRowWidth(0));
		assertEquals(2, board2x20.getRowWidth(1));
		int cleared = board2x20.clearRows();
		assertEquals(2, cleared);
		assertEquals(0, board2x20.getMaxHeight());
		assertEquals(0, board2x20.getRowWidth(0));
		assertEquals(0, board2x20.getColumnHeight(0));
		assertEquals(0, board2x20.getColumnHeight(1));
		assertFalse(board2x20.getGrid(0, 0));
	}

	@Test
	public void testClearRows10x24NoRowsToClear() {
		int initialMaxHeight = board10x24.getMaxHeight();
		int cleared = board10x24.clearRows();
		assertEquals(0, cleared);
		assertEquals(initialMaxHeight, board10x24.getMaxHeight());
	}

	@Test
	public void testClearRowsBComplex() {
		b.place(pyr1, 0, 0);
		b.commit();
		b.place(pyr1, 0, 2);
		b.commit();
		int cleared = b.clearRows();
		assertEquals(2, cleared);
		assertEquals(2, b.getMaxHeight());
		assertEquals(1, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertTrue(b.getGrid(1, 0));
		assertTrue(b.getGrid(1, 1));
		assertFalse(b.getGrid(0, 0));
		assertFalse(b.getGrid(2, 0));
	}

	@Test
	public void testPlace10x24BasicEmptyArea() {
		assertEquals(0, board10x24.getColumnHeight(7));
		int result = board10x24.place(sq, 7, 0);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(2, board10x24.getColumnHeight(7));
		assertEquals(2, board10x24.getColumnHeight(8));
		assertEquals(6, board10x24.getRowWidth(0));
		assertEquals(5, board10x24.getRowWidth(1));
		assertTrue(board10x24.getGrid(7, 0));
		assertTrue(board10x24.getGrid(8, 0));
		assertTrue(board10x24.getGrid(7, 1));
		assertTrue(board10x24.getGrid(8, 1));
		assertFalse(board10x24.getGrid(7, 2));
		assertFalse(board10x24.getGrid(6, 0));
	}

	@Test
	public void testPlaceBStackingOnExistingPiece() {
		assertEquals(1, b.getColumnHeight(2));
		assertEquals(2, b.getMaxHeight());
		int result = b.place(stick1, 2, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(5, b.getColumnHeight(2));
		assertEquals(5, b.getMaxHeight());
		assertTrue(b.getGrid(2, 1));
		assertTrue(b.getGrid(2, 2));
		assertTrue(b.getGrid(2, 3));
		assertTrue(b.getGrid(2, 4));
		assertFalse(b.getGrid(2, 5));
	}

	@Test
	public void testPlace10x24ComplexShapeAndUndo() {
		int initialMaxHeight = board10x24.getMaxHeight();
		board10x24.place(pyr1, 6, 0);
		assertTrue(board10x24.getGrid(6, 0));
		assertTrue(board10x24.getGrid(7, 0));
		assertTrue(board10x24.getGrid(8, 0));
		assertTrue(board10x24.getGrid(7, 1));
		assertEquals(1, board10x24.getColumnHeight(6));
		assertEquals(2, board10x24.getColumnHeight(7));
		assertEquals(1, board10x24.getColumnHeight(8));
		board10x24.undo();
		assertEquals(initialMaxHeight, board10x24.getMaxHeight());
		assertEquals(0, board10x24.getColumnHeight(6));
		assertEquals(0, board10x24.getColumnHeight(7));
		assertEquals(0, board10x24.getColumnHeight(8));
		assertFalse(board10x24.getGrid(7, 1));
		assertFalse(board10x24.getGrid(7, 0));
	}

	@Test
	public void testPlace10x24OverlapIsBad() {
		int result = board10x24.place(sq, 0, 0);
		assertEquals(Board.PLACE_BAD, result);
		board10x24.undo();
		assertEquals(3, board10x24.getMaxHeight());
	}

	@Test
	public void testPlace2x20RowFilled() {
		int result = board2x20.place(sq, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, result);
		board2x20.commit();
		assertEquals(4, board2x20.getMaxHeight());
		assertEquals(2, board2x20.getRowWidth(2));
		assertEquals(2, board2x20.getRowWidth(3));
	}

	@Test
	public void testPlaceOutOfBoundsX() {
		assertEquals(Board.PLACE_OUT_BOUNDS, board10x24.place(sq, -1, 0));
		board10x24.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, board10x24.place(sq, 9, 0));
		board10x24.undo();
	}

	@Test
	public void testPlaceOutOfBoundsY() {
		assertEquals(Board.PLACE_OUT_BOUNDS, board10x24.place(sq, 0, -1));
		board10x24.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, board10x24.place(sq, 0, 23));
		board10x24.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, board10x2.place(stick1, 8, 0));
		board10x2.undo();
	}

	@Test
	public void testUndoBasic() {
		int initialHeight = board10x24.getMaxHeight();
		board10x24.place(sq, 7, 0);
		assertEquals(2, board10x24.getColumnHeight(7));
		assertTrue(board10x24.getGrid(7, 0));
		board10x24.undo();
		assertEquals(initialHeight, board10x24.getMaxHeight());
		assertEquals(0, board10x24.getColumnHeight(7));
		assertFalse(board10x24.getGrid(7, 0));
	}

	@Test
	public void testUndoAfterCommit() {
		board10x24.place(sq, 8, 0);
		board10x24.commit();
		board10x24.undo();
		assertEquals(2, board10x24.getColumnHeight(8));
		assertTrue(board10x24.getGrid(8, 0));
	}

	@Test
	public void testUndoAfterClearRows() {
		assertEquals(2, board2x20.getMaxHeight());
		board2x20.clearRows();
		assertEquals(0, board2x20.getMaxHeight());
		board2x20.undo();
		assertEquals(2, board2x20.getMaxHeight());
		assertEquals(2, board2x20.getRowWidth(0));
		assertTrue(board2x20.getGrid(0, 0));
	}

	@Test
	public void testUndoAfterFailedPlace() {
		assertEquals(3, board10x24.getColumnHeight(0));
		int result = board10x24.place(sq, 0, 1);
		assertEquals(Board.PLACE_BAD, result);
		board10x24.undo();
		assertEquals(3, board10x24.getColumnHeight(0));
		assertFalse(board10x24.getGrid(1, 1));
	}

	@Test
	public void testToString() {
		Board testBoard = new Board(3, 3);
		String expectedEmpty =
				"|   |\n" +
						"|   |\n" +
						"|   |\n" +
						"-----";
		assertEquals(expectedEmpty, testBoard.toString());
		testBoard.place(pyr1, 0, 0);
		testBoard.commit();
		String expectedWithPiece =
				"|   |\n" +
						"| + |\n" +
						"|+++|\n" +
						"-----";
		assertEquals(expectedWithPiece, testBoard.toString());
	}

	@Test
	public void testSanityCheck() {
		Board testBoard = new Board(5, 10);
		testBoard.sanityCheck();
		testBoard.place(pyr1, 0, 0);
		testBoard.sanityCheck();
		testBoard.commit();
		testBoard.sanityCheck();
		testBoard.place(stick2, 3, 0);
		testBoard.commit();
		testBoard.clearRows();
		testBoard.sanityCheck();
		assertTrue(true);
	}
}
