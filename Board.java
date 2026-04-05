// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] rowWidth, columnHeight;
	private int maxHeight;
	private boolean[][] backupGrid;
	private int[] backupRowWidth;
	private int[] backupColumnHeight;
	private int backupMaxHeight;

	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		maxHeight = 0;
		rowWidth = new int[height];
		columnHeight = new int[width];
		backupGrid = new boolean[width][height];
		backupRowWidth = new int[height];
		backupColumnHeight = new int[width];
		backupMaxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	

	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] checkRowWidth = new int[height];
			int[] checkColumnHeight = new int[width];
			int checkMaxHeight = 0;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (grid[x][y]) {
						checkRowWidth[y]++;
						if (y + 1 > checkColumnHeight[x]) checkColumnHeight[x] = y + 1;
						if (y + 1 > checkMaxHeight) checkMaxHeight = y + 1;
					}
				}
			}
			if (checkMaxHeight != maxHeight) {
				throw new RuntimeException("maxHeight is wrong! " + checkMaxHeight + "instead of: " + maxHeight);
			}
			for (int x = 0; x < width; x++) {
				if (checkColumnHeight[x] != columnHeight[x]) {
					throw new RuntimeException("columnHeight is wrong! " + checkColumnHeight[x]  + "instead of: " + columnHeight[x]);
				}
			}
			for (int y = 0; y < height; y++) {
				if (checkRowWidth[y] != rowWidth[y]) {
					throw new RuntimeException("rowWidth is wrong! " + checkRowWidth[y]  + "instead of: " + rowWidth[y]);
				}
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int dropY = 0;
		for (int i = 0; i < piece.getWidth(); i++) {
			int boardX = i + x;
			if (boardX >= 0 && boardX < width) {
				int curY = getColumnHeight(boardX) - skirt[i];
				if (curY > dropY) dropY = curY;
			}
		}
		return dropY;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return columnHeight[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return rowWidth[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		if (!committed) throw new RuntimeException("place commit problem");
		System.arraycopy(rowWidth, 0, backupRowWidth, 0, height);
		System.arraycopy(columnHeight, 0, backupColumnHeight, 0, width);
		for (int i = 0; i < width; i++) {
			System.arraycopy(grid[i], 0, backupGrid[i], 0, height);
		}
		backupMaxHeight = maxHeight;
		committed = false;
		int result = PLACE_OK;
		if (x < 0 || x + piece.getWidth() > width || y < 0 || y + piece.getHeight() > height) return PLACE_OUT_BOUNDS;
		TPoint[] body = piece.getBody();
		for (int i = 0; i < body.length; i++) {
			int currX = x + body[i].x;
			int currY = y + body[i].y;
			if (grid[currX][currY]) return PLACE_BAD;
			grid[currX][currY] = true;
			rowWidth[currY]++;
			if (currY + 1 > columnHeight[currX]) {
				columnHeight[currX] = currY + 1;
				if (columnHeight[currX] > maxHeight) maxHeight = columnHeight[currX];
			}
			if (rowWidth[currY] == width) result = PLACE_ROW_FILLED;
		}
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		if (committed) {
			System.arraycopy(rowWidth, 0, backupRowWidth, 0, height);
			System.arraycopy(columnHeight, 0, backupColumnHeight, 0, width);
			for (int i = 0; i < width; i++) {
				System.arraycopy(grid[i], 0, backupGrid[i], 0, height);
			}
			backupMaxHeight = maxHeight;
			committed = false;
		}
		int toY = 0;
		for (int fromY = 0; fromY < maxHeight; fromY++) {
			if (rowWidth[fromY] == width) rowsCleared++;
			else {
				if (toY != fromY) {
					for (int i = 0; i < width; i++) {
						grid[i][toY] = grid[i][fromY];
					}
					rowWidth[toY] = rowWidth[fromY];
				}
				toY++;
			}
		}
		for (int y = toY; y < maxHeight; y++) {
			for (int x = 0; x < width; x++) {
				grid[x][y] = false;
			}
			rowWidth[y] = 0;
		}
		calculateMaxHeight();
		sanityCheck();
		return rowsCleared;
	}

	public void calculateMaxHeight() {
		maxHeight = 0;
		for (int x = 0; x < width; x++) {
			columnHeight[x] = 0;
			for (int j = height - 1; j >= 0; j--) {
				if (grid[x][j]) {
					columnHeight[x] = j + 1;
					if (columnHeight[x] > maxHeight) maxHeight = columnHeight[x];
					break;
				}
			}
		}
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) return;
		boolean[][] tempGrid = grid;
		grid = backupGrid;
		backupGrid = tempGrid;
		int[] tempWidths = rowWidth;
		rowWidth = backupRowWidth;
		backupRowWidth = tempWidths;
		int[] tempHeights = columnHeight;
		columnHeight = backupColumnHeight;
		backupColumnHeight = tempHeights;
		maxHeight = backupMaxHeight;
		commit();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


