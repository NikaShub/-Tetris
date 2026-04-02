

import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	private Piece stick1, stick2, stick3;
	private Piece l11, l12, l13, l14;
	private Piece l21, l22, l23, l24;
	private Piece s11, s12, s13, s14;
	private Piece s21, s22, s23, s24;
	private Piece sq, sqRotated;

	protected void setUp() throws Exception {
		super.setUp();
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		setUpSquare();
		setUpStick();
		setUpL1();
		setUpL2();
		setUpS1();
		setUpS2();
	}

	private void setUpSquare() {
		sq = new Piece(Piece.SQUARE_STR);
		sqRotated = sq.computeNextRotation();
	}

	private void setUpS2() {
		s21 = new Piece(Piece.S2_STR);
		s22 = s21.computeNextRotation();
		s23 = s22.computeNextRotation();
		s24 = s23.computeNextRotation();
	}

	private void setUpS1() {
		s11 = new Piece(Piece.S1_STR);
		s12 = s11.computeNextRotation();
		s13 = s12.computeNextRotation();
		s14 = s13.computeNextRotation();
	}

	private void setUpL2() {
		l21 = new Piece(Piece.L2_STR);
		l22 = l21.computeNextRotation();
		l23 = l22.computeNextRotation();
		l24 = l23.computeNextRotation();
	}

	private void setUpL1() {
		l11 = new Piece(Piece.L1_STR);
		l12 = l11.computeNextRotation();
		l13 = l12.computeNextRotation();
		l14 = l13.computeNextRotation();
	}

	private void setUpStick() {
		stick1 = new Piece(Piece.STICK_STR);
		stick2 = stick1.computeNextRotation();
		stick3 = stick2.computeNextRotation();
	}

	public void testSampleSize() {
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	public void testWidth() {
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr3.getWidth());
		assertEquals(2, pyr4.getWidth());

		assertEquals(1, stick1.getWidth());
		assertEquals(4, stick2.getWidth());
		assertEquals(1, stick3.getWidth());

		assertEquals(2, l11.getWidth());
		assertEquals(3, l12.getWidth());
		assertEquals(2, l13.getWidth());
		assertEquals(3, l14.getWidth());

		assertEquals(2, l21.getWidth());
		assertEquals(3, l22.getWidth());
		assertEquals(2, l23.getWidth());
		assertEquals(3, l24.getWidth());

		assertEquals(3, s11.getWidth());
		assertEquals(2, s12.getWidth());
		assertEquals(3, s13.getWidth());
		assertEquals(2, s14.getWidth());

		assertEquals(3, s21.getWidth());
		assertEquals(2, s22.getWidth());
		assertEquals(3, s23.getWidth());
		assertEquals(2, s24.getWidth());

		assertEquals(2, sq.getWidth());
		assertEquals(2, sqRotated.getWidth());
	}

	public void testHeight() {
		assertEquals(2, pyr1.getHeight());
		assertEquals(3, pyr2.getHeight());
		assertEquals(2, pyr3.getHeight());
		assertEquals(3, pyr4.getHeight());

		assertEquals(4, stick1.getHeight());
		assertEquals(1, stick2.getHeight());
		assertEquals(4, stick3.getHeight());

		assertEquals(3, l11.getHeight());
		assertEquals(2, l12.getHeight());
		assertEquals(3, l13.getHeight());
		assertEquals(2, l14.getHeight());

		assertEquals(3, l21.getHeight());
		assertEquals(2, l22.getHeight());
		assertEquals(3, l23.getHeight());
		assertEquals(2, l24.getHeight());

		assertEquals(2, s11.getHeight());
		assertEquals(3, s12.getHeight());
		assertEquals(2, s13.getHeight());
		assertEquals(3, s14.getHeight());

		assertEquals(2, s21.getHeight());
		assertEquals(3, s22.getHeight());
		assertEquals(2, s23.getHeight());
		assertEquals(3, s24.getHeight());

		assertEquals(2, sq.getHeight());
		assertEquals(2, sqRotated.getHeight());
	}
	public void testSampleSkirt() {
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	public void testSkirt() {
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[]{1, 0}, pyr2.getSkirt()));
		assertTrue(Arrays.equals(new int[]{1, 0, 1}, pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[]{0, 1}, pyr4.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0}, stick1.getSkirt()));
		assertTrue(Arrays.equals(new int[]{0, 0, 0, 0}, stick2.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0, 0, 1}, s11.getSkirt()));
		assertTrue(Arrays.equals(new int[]{1, 0}, s12.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0, 0}, l11.getSkirt()));
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, l12.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0, 0}, sq.getSkirt()));
		assertTrue(Arrays.equals(new int[]{0, 0}, sqRotated.getSkirt()));

		Piece p = new Piece("0 1  0 0");
		assertTrue(Arrays.equals(new int[]{0}, p.getSkirt()));
	}

	public void testEquals() {
		assertTrue(pyr1.equals(pyr1));

		assertFalse(pyr1.equals("Some String"));
		assertFalse(stick1.equals(sq));

		assertTrue(sq.equals(sqRotated));

		assertTrue(stick1.equals(stick3));
		assertFalse(stick1.equals(stick2));

		assertTrue(s11.equals(s13));
		assertTrue(s21.equals(s23));
		assertFalse(s11.equals(s12));

		assertFalse(pyr1.equals(pyr2));
		assertFalse(pyr1.equals(pyr3));
		assertFalse(pyr1.equals(pyr4));

		assertFalse(l11.equals(l13));
	}


	public void testEqualsEdgeCases() {
		Piece p1 = new Piece("0 0 0 1");
		Piece p2 = new Piece("0 0 0 1 0 2");
		assertFalse(p1.equals(p2));

		Piece p3 = new Piece("0 0 1 1");
		Piece p4 = new Piece("0 0 0 1 1 0");
		assertFalse(p3.equals(p4));

		Piece p5 = new Piece("0 0 1 1");
		Piece p6 = new Piece("0 1 1 0");
		assertFalse(p5.equals(p6));
	}

	public void testFastRotation() {
		Piece[] pieces = Piece.getPieces();
		Piece square = pieces[Piece.SQUARE];
		assertTrue(square.equals(square.fastRotation()));

		Piece stick = pieces[Piece.STICK];
		Piece stickRotated = stick.fastRotation();
		assertFalse(stick.equals(stickRotated));
		assertTrue(stick.equals(stickRotated.fastRotation()));

		Piece pyr = pieces[Piece.PYRAMID];
		Piece pyr2 = pyr.fastRotation();
		Piece pyr3 = pyr2.fastRotation();
		Piece pyr4 = pyr3.fastRotation();
		Piece pyr5 = pyr4.fastRotation();
		assertFalse(pyr.equals(pyr2));
		assertFalse(pyr.equals(pyr3));
		assertFalse(pyr.equals(pyr4));
		assertTrue(pyr.equals(pyr5));

		Piece s1 = pieces[Piece.S1];
		assertTrue(s1.equals(s1.fastRotation().fastRotation()));

		Piece s2 = pieces[Piece.S2];
		assertTrue(s2.equals(s2.fastRotation().fastRotation()));

		Piece l1 = pieces[Piece.L1];
		assertTrue(l1.equals(l1.fastRotation().fastRotation().fastRotation().fastRotation()));

		Piece l2 = pieces[Piece.L2];
		assertTrue(l2.equals(l2.fastRotation().fastRotation().fastRotation().fastRotation()));
	}

	public void testParsePointsException() {
		boolean thrown = false;
		try {
			new Piece("0 0 1 oops");
		} catch (RuntimeException e) {
			thrown = true;
			assertTrue(e.getMessage().contains("Could not parse x,y string"));
		}
		assertTrue(thrown);
	}

	public void testGetBody() {
		Piece p = new Piece("0 0  1 0  2 0");
		TPoint[] body = p.getBody();
		assertEquals(3, body.length);
		assertEquals(0, body[0].x);
		assertEquals(0, body[0].y);
		assertEquals(1, body[1].x);
		assertEquals(0, body[1].y);
		assertEquals(2, body[2].x);
		assertEquals(0, body[2].y);
		TPoint[] sqBody = sq.getBody();
		assertEquals(4, sqBody.length);
		assertEquals(1, sqBody[3].x);
		assertEquals(1, sqBody[3].y);
	}

	public void testgetPieces() {
		Piece[] pieces1 = Piece.getPieces();
		assertNotNull(pieces1);
		assertEquals(7, pieces1.length);
		for (int i = 0; i < pieces1.length; i++) {
			assertNotNull(pieces1[i]);
		}
		Piece[] pieces2 = Piece.getPieces();
		assertSame(pieces1, pieces2);
	}
}
