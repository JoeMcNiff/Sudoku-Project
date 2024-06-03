// The Sudoku class
public class Sudoku {
  // The game board
  private int[][] game;

  // The instance method
  public Sudoku(int[][] game) {
    this.game = game;
  }

  // Solves the Sudoku
  public void solve() {
    Sudoku temp = new Sudoku(getGameValues());
    int[][] starting = givenBlanks();
    while (!temp.isSolved()) {
      temp.resetGameValues(getGameValues());
      int row; int col;
      for (int i = 0; i < starting.length; i++) {
        row = starting[i][0];
        col = starting[i][1];

        int rand = ((int) (Math.random() * 9)) + 1;
        rand = temp.betterRand(row, col, rand);
        temp.setGameValue(row, col, rand); 
        if (temp.isSolved()) {
          break;
        }
      }  
    }
    resetGameValues(temp.getGameValues());
  }

  // **OLD VERSION** Solves the Sudoku less efficiently than new way
  public void solve2() {
    Sudoku temp = new Sudoku(getGameValues());
    while (!temp.isSolved()) {
      temp.resetGameValues(getGameValues());
      for (int row = 0; row < game.length; row++) {
        for (int col = 0; col < game.length; col++) {
          if (notGiven(row, col)) {
            int rand = ((int) (Math.random() * 9)) + 1;
            rand = temp.betterRand(row, col, rand);
            temp.setGameValue(row, col, rand);
          }
          if (temp.isSolved()) {
            break;
          }
        }
      }
    }
    resetGameValues(temp.getGameValues());
  }

  // The printed form of the suduko
  public String toString() {
    String theSudoku = "";
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        if (game[row][col] != 0) {
          theSudoku += String.valueOf(game[row][col]);
        } else {
          theSudoku += "_";
        }
        theSudoku += " ";
      }
      theSudoku += "\n";
    }
    return theSudoku;
  }

  
  // Set a value for a particular coordinate
  public void setGameValue(int row, int col, int value) {
    game[row][col] = value;
  }

  // Returns the values of the game in in[][] form
  public int[][] getGameValues() {
    int[][] gameValues = new int[9][9];
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        gameValues[row][col] = game[row][col];
      }
    }
    return gameValues;
  }

  // Set the values of the game to a new int[][]
  public void resetGameValues(int[][] newValues) {
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        game[row][col] = newValues[row][col];
      }
    }
  }

  // Checks if the sudoku is solved
  public boolean isSolved() {
    int[][] boxes = makeBoxes();
    for (int row = 0; row < game.length; row++) {
      // Checks each row
      if (!allNine(game[row])) {
        return false;
      }
      // Checks each box
      if (!allNine(boxes[row])) {
        return false;
      }
      // Creates a column list
      int[] columns = new int[9];
      for (int col = 0; col < game[0].length; col++) {
        columns[col] = game[col][row];
      }
      // Checks each column
      if (!allNine(columns)) {
        return false;
      }
    }
    return true;
  }

  // Checks if numbers 0ne through nine in given list.
  private boolean allNine(int[] list) {
    int[] has = new int[9];
    int nextIndex = 0;
    for (int num = 0; num < 9; num++) {
      for (int i = 0; i < has.length; i++) {
        if (list[num] == has[i]) {
          return false;
        }
      }
      has[nextIndex] = list[num];
      nextIndex++;
    }
    return true;
  }

  // Makes the different boxes for the sudoku as lists in a 2D array
  private int[][] makeBoxes() {
    int[][] boxes = new int[9][9];
    int index = 0;
    for (int b = 0; b <= 6; b += 3) {
      for (int c = 0; c <= 6; c += 3) {
        int index2 = 0;
        for (int row = b; row < (b + 3); row++) {
          for (int col = c; col < (c + 3); col++) {
            boxes[index][index2] = game[row][col];
            index2++;
          }
        }
        index++;
      }
    }

    return boxes;
  }

  // Checks if a given list has a number in it
  private Boolean inList(int[] list, int num) {
    for (int i = 0; i < 9; i++) {
      if (list[i] == num) {
        return true;
      }
    }
    return false;
  }

  // Checks if a given column has a number in it
  private Boolean inRow(int row, int num) {
    for (int i = 0; i < 9; i++) {
      if (game[row][i] == num) {
        return true;
      }
    }
    return false;
  }

  // Checks if a given row has a number in it
  private Boolean inCol(int col, int num) {
    for (int i = 0; i < 9; i++) {
      if (game[i][col] == num) {
        return true;
      }
    }
    return false;
  }

  // Checks if a given box has a number in it STILL NEEDS TO BE FIXED
  private Boolean inBox(int row, int col, int num) {
    int[][] boxes = makeBoxes();
    int boxIndex = findBox(row, col);
    return inList(boxes[boxIndex], num);
  }

  // Finds the box that a given coordinate is in
  private int findBox(int row, int col) {
    int boxRow;
    if (row < 3) {
      boxRow = 0;
    }
    if (row < 6) {
      boxRow = 3;
    } else {
      boxRow = 6;
    }
    int index = boxRow + (int) (col / 3);
    return index;
  }

  // Checks if a coordinate was empty to start
  private Boolean notGiven(int row, int col) {
    int[][] starting = givenNumbers();
    Boolean notGiven = true;
    for (int i = 0; i < starting.length; i++) {
      if (row == starting[i][0] && col == starting[i][1]) {
        notGiven = false;
      }
    }
    return notGiven;
  }

  // Finds a random number that is more likely to be the solution for a given
  // coordinate
  private int betterRand(int row, int col, int num) {
    int rand = num;
    int test = 0;
    while (inRow(row, rand) || inCol(col, rand)) {
      rand = ((int) (Math.random() * 9)) + 1;
      // Makes sure the loop isn't infinite
      if (test == 30) {
        return num;
      }
      test++;
    }
    return rand;
  }
  
  // Finds the coordinates of the starting numbers
  private int[][] givenNumbers() {
    // Calculates the number of given numbers
    int size = 0;
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        if (game[row][col] != 0) {
          size += 1;
        }
      }
    }
    // Makes a 2-D array of the coordinates
    int[][] coordinates = new int[size][2];
    int pointInList = 0;
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        if (game[row][col] != 0) {
          coordinates[pointInList][0] = row;
          coordinates[pointInList][1] = col;
          pointInList += 1;
        }
      }
    }
    return coordinates;
  }

  // Finds the blanks to started with
  private int[][] givenBlanks() {
    // Calculates the number of blanks
    int size = 0;
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        if (game[row][col] == 0) {
          size += 1;
        }
      }
    }
    // Makes a 2-D array of the coordinates
    int[][] coordinates = new int[size][2];
    int pointInList = 0;
    for (int row = 0; row < game.length; row++) {
      for (int col = 0; col < game[0].length; col++) {
        if (game[row][col] == 0) {
          coordinates[pointInList][0] = row;
          coordinates[pointInList][1] = col;
          pointInList += 1;
        }
      }
    }
    return coordinates;
    }
}