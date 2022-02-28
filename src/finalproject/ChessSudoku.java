
package finalproject;

import java.util.*;
import java.io.*;

public class ChessSudoku {
	/*
	 * SIZE is the size parameter of the Sudoku puzzle, and N is the square of the
	 * size. For a standard Sudoku puzzle, SIZE is 3 and N is 9.
	 */
	public int SIZE, N;

	/*
	 * The grid contains all the numbers in the Sudoku puzzle. Numbers which have
	 * not yet been revealed are stored as 0.
	 */
	public int grid[][];

	// makes all answerable cells true and pre-existing cells false
	public boolean valid_cell[][];

	/*
	 * Booleans indicating whether of not one or more of the chess rules should be
	 * applied to this Sudoku.
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;

	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();

	/*
	 * The solve() method should remove all the unknown characters ('x') in the grid
	 * and replace them with the numbers in the correct range that satisfy the
	 * constraints of the Sudoku puzzle. If true is provided as input, the method
	 * should find finds ALL possible solutions and store them in the field named
	 * solutions.
	 */
	public void solve(boolean allSolutions) {
		
		// create ArrayLists to store sets
		// create sets for storing numbers present in the respective row, col, and SIZE x SIZE grid
		ArrayList<HashSet<Integer>> gridAL = new ArrayList<HashSet<Integer>>();
		ArrayList<HashSet<Integer>> rowAL = new ArrayList<HashSet<Integer>>();
		ArrayList<HashSet<Integer>> colAL = new ArrayList<HashSet<Integer>>();

		// initialize and store the sets sequentially in their respective ArrayList
		for (int i = 0; i < N; i += 1) {
			gridAL.add(new HashSet<>());
			rowAL.add(new HashSet<>());
			colAL.add(new HashSet<>());
		}

		// in solutionCells, make every empty/solution cell TRUE, and every pre-existing cell FALSE
		// ALSO, add the pre-existing numbers in grid to the appropriate sets
		boolean[][] solutionCells = new boolean[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				if (grid[i][j] == 0)
					solutionCells[i][j] = true;
				else {
					solutionCells[i][j] = false;
					rowAL.get(i).add(grid[i][j]);
					colAL.get(j).add(grid[i][j]);
					gridAL.get(gridIndex(i, j)).add(grid[i][j]);
				}

			}
		}
		
		try {
			// solve the empty cells using backtracking algorithm
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
	
					// false if respective rule is followed, true if broken
					boolean knight = false;
					boolean king = false;
					boolean queen = false;
	
					// only work on solution cells
					if (solutionCells[i][j]) {
						int num = grid[i][j] + 1;
						
						// validNum becomes true if it is temporarily acceptable in the grid
						boolean validNum = false;
	
						while (true) {
							// check if num = N+1 which triggers backtrack; validNum remains false
							if (num == N + 1)
								break;
							// check if num is temporarily valid for that cell
							if (!gridAL.get(gridIndex(i, j)).contains(num) && !rowAL.get(i).contains(num)
									&& !colAL.get(j).contains(num)) {
								
								// applies any of the chess rules as determined from the main method
								if (knightRule) {
									knight = Apply_Knight_Rule(i, j, num);
								}
	
								if (kingRule) {
									king = Apply_King_Rule(i, j, num);
								}
	
								if (queenRule && num == N) {
									queen = Apply_Queen_Rule(i, j, num);
								}
	
								if (!knight && !king && !queen) {
									validNum = true;
									break;
								}
	
							}
							// keep increasing num until a valid num OR an invalid num = SIZE+1 is found
							num += 1;
						}
	
						// if validNum true, place num in grid and the respective grid, row, col sets
						if (validNum) {
	
							// remove the previous, invalid number from grid and all sets
							// eg:- num = 8, and previous value on grid[i][j] was 7. Remove 7 from the sets, and add 8
							int old_num = grid[i][j];
							gridAL.get(gridIndex(i, j)).remove(old_num);
							rowAL.get(i).remove(old_num);
							colAL.get(j).remove(old_num);
	
							// update grid with num and add it to all sets
							grid[i][j] = num;
							gridAL.get(gridIndex(i, j)).add(num);
							rowAL.get(i).add(num);
							colAL.get(j).add(num);
	
						}
	
						// if validNum false, initiate backtracking
						else {
	
							// remove the previous, invalid number from grid and all sets
							int invalidNum = grid[i][j];
							gridAL.get(gridIndex(i, j)).remove(invalidNum);
							rowAL.get(i).remove(invalidNum);
							colAL.get(j).remove(invalidNum);
							// reset grid cell to 0
							grid[i][j] = 0;
	
							// backtrack by getting indices of previous solution cell 
							
							int indices[] = prevIndices(solutionCells, i, j);
							i = indices[0];
							j = indices[1];
		
							// since the for loop increments i, j decrement them once more to offset it
							if (j == 0) {
								i -= 1;
								j = N - 1;
							} else
								j -= 1;
	
	
						}
	
					}
					
					// find all solutions if required
					if (i == N-1 && j == N-1 && allSolutions) {
						
						// create obj
						ChessSudoku s = new ChessSudoku(SIZE);
						// duplicate grid
						s.grid = copyGrid(this.grid);
						// add solution to solutions set
						solutions.add(s);
						
						// while loop to reach the last solution cell in the puzzle
						while (!solutionCells[i][j]) {
							if (j == 0) {
								i -= 1;
								j = N - 1;
							} else
								j -= 1;
						}
						
						// now backtrack once on from the last solution cell on this grid
						// remove the previous, invalid number from grid and all sets
						int invalidNum = grid[i][j];
						gridAL.get(gridIndex(i, j)).remove(invalidNum);
						rowAL.get(i).remove(invalidNum);
						colAL.get(j).remove(invalidNum);
						// reset grid cell to 0
						grid[i][j] = 0;

						// backtrack again by getting indices of teh second-last solution cell
						
						int indices[] = prevIndices(solutionCells, i, j);
						i = indices[0];
						j = indices[1];
	
						// since the for loop increments i, j decrement them once more to offset it
						if (j == 0) {
							i -= 1;
							j = N - 1;
						} else
							j -= 1;
						
					}
	
				}// for j
			}// for i
		}// try
		
		catch (ArrayIndexOutOfBoundsException e) {
			if (!allSolutions) {
				System.out.printf("No Solution.\n\n");
			}
			else if(allSolutions && solutions.size() == 0) {
				System.out.printf("No Solution.\n\n");
			}
			else if(allSolutions && solutions.size() > 0) {
				System.out.printf("All solutions have been added to solutions set.\n\n");
			}
		}
		

	}

	// ADDITIONAL METHODS

	// kingRule
	// (m,n) = position of a cell
	// val is the number that is being checked to see if it is valid
	public boolean Apply_King_Rule(int m, int n, int val) { 
		
		// king true if val is invalid due to king rule, else false
		boolean king = false;
		int ki1 = 0;
		int ki2 = 0;
		int ki3 = 0;
		int ki4 = 0;

		// check top-left diagonal
		if (m - 1 < 0 || n - 1 < 0) { 

		} else if (grid[m - 1][n - 1] == val) {
			ki1 = 1;
		}

		// check top-right diagonal
		if (m - 1 < 0 || n + 1 >= N) {

		} else if (grid[m - 1][n + 1] == val) {
			ki2 = 1;
		}

		// check bottom-left diagonal
		if (m + 1 >= N || n - 1 < 0) {

		} else if (grid[m + 1][n - 1] == val) {
			ki3 = 1;
		}
		
		// check bottom-right diagonal
		if (m + 1 >= N || n + 1 >= N) {

		} else if (grid[m + 1][n + 1] == val) {
			ki4 = 1;
		}
		
		// if any of the above conditions are met, king true meaning king rule is broken therefore val is invalid
		if (ki1 == 1 || ki2 == 1 || ki3 == 1 || ki4 == 1) {
			king = true;
		}

		return king;

	}

//Knight Rule		 
	public boolean Apply_Knight_Rule(int m, int n, int val) { 
		boolean knight = false;
		int k1 = 0;
		int k2 = 0;
		int k3 = 0;
		int k4 = 0;
		int k5 = 0;
		int k6 = 0;
		int k7 = 0;
		int k8 = 0;

		if (m + 2 >= N || n - 1 < 0) {

		} else if (grid[m + 2][n - 1] == val) {
			k1 = 1;

		}

		if (m + 2 >= N || n + 1 >= N) {

		} else if (grid[m + 2][n + 1] == val) {
			k2 = 1;
		}

		if (m - 2 < 0 || n - 1 < 0) {

		} else if (grid[m - 2][n - 1] == val) {
			k3 = 1;
		}
		if (m - 2 < 0 || n + 1 >= N) {

		} else if (grid[m - 2][n + 1] == val) {
			k4 = 1;
		}

		if (m - 1 < 0 || n - 2 < 0) {

		} else if (grid[m - 1][n - 2] == val) {
			k5 = 1;
		}

		if (m + 1 >= N || n - 2 < 0) {

		} else if (grid[m + 1][n - 2] == val) {
			k6 = 1;
		}

		if (m - 1 < 0 || n + 2 >= N) {

		} else if (grid[m - 1][n + 2] == val) {
			k7 = 1;
		}
		if (m + 1 >= N || n + 2 >= N) {

		} else if (grid[m + 1][n + 2] == val) {
			k8 = 1;
		}

		if (k1 == 1 || k2 == 1 || k3 == 1 || k4 == 1 || k5 == 1 || k6 == 1 || k7 == 1 || k8 == 1) {
			knight = true;
		}

		return knight;

	}

//Queen Rule		  
	// searches 
	public boolean Apply_Queen_Rule(int m, int n, int val) {
		
		// queen true if queen rule broken else false
		boolean queen = false;
		
		// 4 booleans for 4 continuous diagonals top-left, top-right, bottom-left, bottom-right
		// respective boolean becomes true if the number N is found in the respective diagonal
		boolean po = true;
		boolean qo = true;
		boolean ro = true;
		boolean so = true;
		int i = 1;
		
		// method functions by checking every step in all 4 diagonals from the reference cell
		// eg:- i=1 (check immediate diagonal from cell in 4 directions) i=2 (check second diagonal position from cell in 4 directions) 
		// i=3 (check third diagonal position from cell in 4 directions) and so on
		while (po == true || qo == true || ro == true || so == true) {
			
			// top-left check
			if (m - i < 0 || n - i < 0) { 
				po = false;

			} else if (grid[m - i][n - i] == val) {
				queen = true;
				break;
			}
			
			// top-right check
			if (m - i < 0 || n + i >= N) {
				qo = false;
			} else if (grid[m - i][n + i] == val) {
				queen = true;
				break;
			}
			
			// bottom-left check
			if (m + i >= N || n - i < 0) {
				ro = false;
			} else if (grid[m + i][n - i] == val) {
				queen = true;
				break;
			}
			
			// bottom-right check
			if (m + i >= N || n + i >= N) {
				so = false;
			} else if (grid[m + i][n + i] == val) {
				queen = true;
				break;
			}
			
			i++;

		}
		return queen;
	}
	
	
	
	// returns index of grid using row and col index of the given cell
	private int gridIndex(int row, int col) {
		return SIZE * (row / SIZE) + col / SIZE;
	}

	// returns the previous empty cell THAT IS A SOLUTION CELL i.e. ignore previous pre-existent cells
	private int[] prevIndices(boolean[][] solutionCells, int row, int col) throws ArrayIndexOutOfBoundsException
	{
		
		// get prev cell
		if (col == 0) {
			row -= 1;
			col = N - 1;
		} else
			col -= 1;

		// make sure the cell is a solution cell
		while (!solutionCells[row][col]) {
			if (col == 0) {
				row -= 1;
				col = N - 1;
			} else
				col -= 1;
		}

		int[] indices = { row, col };
		return indices;

	}
	
	// copies content of a grid and returns a newly created grid
	private int[][] copyGrid(int[][] matrix){
		
		int[][] grid = new int[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				
				grid[i][j] = matrix[i][j];
			}
		}
		
		return grid;
	}
	
	// for debugging
	private void printsolutionCells(boolean[][] solutionCells) {

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.printf("%b ", solutionCells[i][j]);
			}
			System.out.println();
		}

	}
	 
	private void printAL(ArrayList<HashSet<Integer>> al) {
		for (HashSet<Integer> hs : al) {
			Iterator<Integer> itr = hs.iterator();
			while (itr.hasNext()) {
				System.out.printf("%d ", itr.next());
			}
			System.out.println();
		}
	}
	
	private void printSolutions() {
		
		int counter = 1;
		for(ChessSudoku cs: solutions) {
			System.out.printf("Solution #%d\n", counter++);
			cs.print();
			System.out.println();
		}
		
	}
	

	/*****************************************************************************/
	/* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
	/*****************************************************************************/

	/*
	 * Default constructor. This will initialize all positions to the default 0
	 * value. Use the read() function to load the Sudoku puzzle from a file or the
	 * standard input.
	 */
	public ChessSudoku(int size) {
		SIZE = size;
		N = size * size;

		grid = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				grid[i][j] = 0;

	}

	/*
	 * readInteger is a helper function for the reading of the input file. It reads
	 * words until it finds one that represents an integer. For convenience, it will
	 * also recognize the string "x" as equivalent to "0".
	 */
	static int readInteger(InputStream in) throws Exception {
		int result = 0;
		boolean success = false;

		while (!success) {
			String word = readWord(in);

			try {
				result = Integer.parseInt(word);
				success = true;
			} catch (Exception e) {
				// Convert 'x' words into 0's
				if (word.compareTo("x") == 0) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}

	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord(InputStream in) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while (whiteSpace.indexOf(currentChar) > -1) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while (whiteSpace.indexOf(currentChar) == -1) {
			result.append((char) currentChar);
			currentChar = in.read();
		}
		return result.toString();
	}

	/*
	 * This function reads a Sudoku puzzle from the input stream in. The Sudoku grid
	 * is filled in one row at at time, from left to right. All non-valid characters
	 * are ignored by this function and may be used in the Sudoku file to increase
	 * its legibility.
	 */
	public void read(InputStream in) throws Exception {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				grid[i][j] = readInteger(in);
			}
		}
	}

	/*
	 * Helper function for the printing of Sudoku puzzle. This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint
	 * out takes at least width characters.
	 */
	void printFixedWidth(String text, int width) {
		for (int i = 0; i < width - text.length(); i++)
			System.out.print(" ");
		System.out.print(text);
	}

	/*
	 * The print() function outputs the Sudoku grid to the standard output, using a
	 * bit of extra formatting to make the result clearly readable.
	 */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku
		// puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for (int lineInit = 0; lineInit < lineLength; lineInit++)
			line.append('-');

		// Go through the grid, printing out its values separated by spaces
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				printFixedWidth(String.valueOf(grid[i][j]), digits);
				// Print the vertical lines between boxes
				if ((j < N - 1) && ((j + 1) % SIZE == 0))
					System.out.print(" |");
				System.out.print(" ");
			}
			System.out.println();

			// Print the horizontal line between boxes
			if ((i < N - 1) && ((i + 1) % SIZE == 0))
				System.out.println(line.toString());
		}
	}

	/*
	 * The main function reads in a Sudoku puzzle from the standard input, unless a
	 * file name is provided as a run-time argument, in which case the Sudoku puzzle
	 * is loaded from that file. It then solves the puzzle, and outputs the
	 * completed puzzle to the standard output.
	 */
	public static void main(String args[]) throws Exception {
		InputStream in = new FileInputStream("G:\\Projects\\SudokuChess\\ChessProject\\puzzles\\GitPuzzles\\kingQueen3x3.txt");

		// The first number in all Sudoku files must represent the size of the puzzle.
		// See the example files for the file format.
		int puzzleSize = readInteger(in);
		if (puzzleSize > 100 || puzzleSize < 1) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		ChessSudoku s = new ChessSudoku(puzzleSize);

		// You can modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = true;

		// read the rest of the Sudoku puzzle
		s.read(in);

		System.out.println("Before the solve:");
		s.print();
		System.out.println();

		// Solve the puzzle by finding one solution.
		s.solve(false);

		// Print out the (hopefully completed!) puzzle
		System.out.println("After the solve:");
		s.print();
		
		System.out.println();
		s.printSolutions();
	}
}
