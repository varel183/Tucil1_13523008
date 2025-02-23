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

  // buat papan kosong rowsxcols
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

  // cek blok bisa ditaruh ke papan
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

  // taruh blok ke papan
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

  // hapus block dri papan
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

  public int getRows() {return rows;}

  public int getCols() {return cols;}

  public List<List<Character>> getGrid() {return grid;}
}
