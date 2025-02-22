package logic;

import java.util.ArrayList;
import java.util.List;

public class Block {
  private List<List<Character>> originalShape;
  private List<List<Character>> currentShape;
  private Character symbol;
  private boolean isPlaced;

  public Block(List<List<Character>> shape) {
    this.originalShape = new ArrayList<>();
    for (List<Character> row : shape) {
      this.originalShape.add(new ArrayList<>(row));
    }
    this.currentShape = new ArrayList<>(originalShape);
    this.symbol = findSymbol(originalShape);
    this.isPlaced = false;
  }

  public boolean isPlaced() {
    return isPlaced;
  }

  public void setPlaced(boolean placed) {
    this.isPlaced = placed;
  }

  private Character findSymbol(List<List<Character>> shape) {
    for (List<Character> row : shape) {
      for (Character c : row) {
        if (c != '.') {
          return c;
        }
      }
    }
    return '.';
  }

  public void applyTransformation(int rotation, boolean flip) {
    List<List<Character>> transformed = new ArrayList<>(originalShape);
    
    for (int i = 0; i < rotation; i++) {
        transformed = rotate90(transformed);
    }
    
    if (flip) {
        transformed = flip(transformed);
    }
    
    this.currentShape = transformed;
}
  
  private List<List<Character>> flip(List<List<Character>> shape) {
    List<List<Character>> flippedShape = new ArrayList<>();

    for (int i = shape.size() - 1; i >= 0; i--) {
      flippedShape.add(new ArrayList<>(shape.get(i)));
    }
    return flippedShape;
  }


  private List<List<Character>> rotate90(List<List<Character>> shape) {
    int maxCol = shape.get(0).size();
    int rowCount = shape.size();
    List<List<Character>> rotated = new ArrayList<>();

    for (int i = 0; i < maxCol; i++) {
      List<Character> newRow = new ArrayList<>();
      for (int j = rowCount - 1; j >= 0; j--) {
        if (i < shape.get(j).size()) {
          newRow.add(shape.get(j).get(i));
        } else {
          newRow.add('.');
        }
      }
      rotated.add(newRow);
    }
    return rotated;
  }

  public List<List<Character>> getShape() {
    return currentShape;
  }

  public int getRow() {
    return currentShape.size();
  }

  public int getCol() {
    return currentShape.get(0).size();
  }

  public char getSymbol() {
    return symbol;
  }

}
