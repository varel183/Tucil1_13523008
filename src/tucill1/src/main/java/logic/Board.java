package logic;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private List<List<Character>> grid;
  private final int rows, cols;

  public Board(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    initializeGrid();
  }

  private void initializeGrid() {
    grid = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      List<Character> row = new ArrayList<>();
      for (int j = 0; j < cols; j++) {
        row.add('.');
      }
      grid.add(row);
    }
  }

  public boolean canPlaceBlock(Block block, int row, int col) {
    List<List<Character>> shape = block.getShape();
    for (int i = 0; i < shape.size(); i++) {
      for (int j = 0; j < shape.get(i).size(); j++) {
        if (shape.get(i).get(j) != '.' && (row + i >= rows || col + j >= cols || grid.get(row + i).get(col + j) != '.')) {
          return false;
        }
      }
    }
    return true;
  }

  public void placeBlock(Block block, int row, int col, char symbol) {
    List<List<Character>> shape = block.getShape();
    for (int i = 0; i < shape.size(); i++) {
      for (int j = 0; j < shape.get(i).size(); j++) {
        if (shape.get(i).get(j) != '.') {
          grid.get(row + i).set(col + j, symbol);
        }
      }
    }
  }

  public void removeBlock(Block block, int row, int col) {
    List<List<Character>> shape = block.getShape();
    for (int i = 0; i < shape.size(); i++) {
      for (int j = 0; j < shape.get(i).size(); j++) {
        if (shape.get(i).get(j) != '.') {
          grid.get(row + i).set(col + j, '.');
        }
      }
    }
  }

  // public void printBoard() {
  //   String[] colors = {
  //     "\u001B[30m",  "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[37m",
  //     "\u001B[90m",  "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m", "\u001B[95m", "\u001B[96m",  "\u001B[97m", 
  //     "\u001B[38;5;208m", "\u001B[38;5;99m",  "\u001B[38;5;46m", "\u001B[38;5;200m", "\u001B[38;5;202m", "\u001B[38;5;130m", 
  //     "\u001B[38;5;214m", "\u001B[38;5;21m", "\u001B[38;5;129m", "\u001B[38;5;40m"  
  // };

  //   String resetColor = "\u001B[0m";
    
  //   for (List<Character> row : grid) {
  //       for (Character cell : row) {
  //           int colorIndex = cell % colors.length;
  //           System.out.print(colors[colorIndex] + cell + resetColor);
  //       }
  //       System.out.println();
  //   }
  // }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public List<List<Character>> getGrid() {
    return grid;
  }
}
