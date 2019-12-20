import java.util.*;

public class Sudoku {

	public int[][] grid;

	public Sudoku(int[][] arr) {
		grid = arr;
	}

	public boolean validNum(int i, int j, int num) { //Checks whether an entry is valid given index
		if (this.grid[i][j] != 0) { //Number already filled
			return false;
		}
		for (int k = 0; k < grid.length; k++) {
			if ((this.grid[i][k] == num) || (this.grid[k][j] == num)) { //Number already in row/column
				return false;
			}
		}

		int row = (i < 3) ? 0 : (i < 6) ? 3 : 6;
		int col = (j < 3) ? 0 : (j < 6) ? 3 : 6;
		for (int a = row; a < row + 3; a++) {
			for (int b = col; b < col + 3; b++) {
				if (this.grid[a][b] == num) { //Number is already in sub-grid
					return false;
				}
			}
		}
		return true;
	}

	public void printGrid() {
		for (int i = 0; i < this.grid.length - 3; i += 3) {
			for (int j = i; j < i + 3; j++) {
				for(int k=0; k<this.grid.length-3; k+=3) {
					System.out.print(grid[j][k] + " ");
					System.out.print(grid[j][k+1] + " ");
					System.out.print(grid[j][k+2] + " | ");
				}
				System.out.println(grid[j][6] + " " + grid[j][7] + " " + grid[j][8]);
			}
			System.out.println("- - -   - - -   - - -");
		}
		for (int j = 6; j < 9; j++) {
			for(int k=0; k<this.grid.length-3; k+=3) {
				System.out.print(grid[j][k] + " ");
				System.out.print(grid[j][k+1] + " ");
				System.out.print(grid[j][k+2] + " | ");
			}
			System.out.println(grid[j][6] + " " + grid[j][7] + " " + grid[j][8]);
		}
		System.out.println("\n");
	}

	public boolean isComplete() {
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid.length; j++) {
				if (grid[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void preTraverse() { //Check if it is possible to solve instantly
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid.length; j++) {
				List<Integer> validNums = new ArrayList<Integer>();
				for (int n = 1; n <= 9; n++) {
					if (this.validNum(i, j, n)) {
						validNums.add(n);
					}
				}
				if (validNums.size() == 1) {
					this.grid[i][j] = validNums.get(0);
					this.preTraverse();
					return;
				}
			}
		}
	}

	public boolean solve() { //Backtracks to find single valid solution
		for (int i = 0; i < this.grid.length; i++) {
			for (int j = 0; j < this.grid.length; j++) {
				if (this.grid[i][j] != 0) {
					continue;
				}
				for (int n = 1; n <= this.grid.length; n++) {
					if (this.validNum(i, j, n)) {
						this.grid[i][j] = n;
						if (this.solve()) {
							return true;
						} else {
							this.grid[i][j] = 0;
						}
					}
				}
				return false; // no valid number for an empty slot, must backtrack & try again
			}
		}
		return true;
	}

	public boolean verify() { //Verifies that grid follows Sudoku properties
		for (int i = 0; i < grid.length; i++) {
			HashSet<Integer> x = new HashSet<Integer>();
			HashSet<Integer> y = new HashSet<Integer>();

			for (int j = 0; j < grid.length; j++) {
				if (!x.add(grid[i][j]) || !y.add(grid[j][i])) {
					return false;
				}
			}
			if (x.size() != 9 || y.size() != 9) {
				return false;
			}
		}
		for (int i = 0; i < grid.length; i += 3) {
			for (int j = 0; j < grid.length; j += 3) {
				List<Integer> griddy = new ArrayList<Integer>();
				griddy.addAll(
						Arrays.asList(grid[i][j], grid[i + 1][j], grid[i + 2][j], grid[i][j + 1], grid[i + 1][j + 1],
								grid[i + 2][j + 1], grid[i][j + 2], grid[i + 1][j + 2], grid[i + 2][j + 2]));
				if (griddy.size() != 9) {
					return false;
				}
			}
		}
		return true;
	}

}
