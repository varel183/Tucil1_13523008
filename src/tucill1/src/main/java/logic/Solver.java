package logic;

import java.util.List;

public class Solver {
  private Board board;
  private List<Block> blocks;
  private long iterationCount = 0;

  public Solver(Board board, List<Block> blocks) {
    this.board = board;
    this.blocks = blocks;
  }

  public boolean solve(int blockIndex) {
    if (blockIndex == blocks.size()) {
      return isBoardFullyFilled() && allBlocksPlaced();
    }

    Block block = blocks.get(blockIndex);
    
    for (int rotation = 0; rotation < 4; rotation++) {
      for (boolean flip : new boolean[]{false, true}) {
          block.applyTransformation(rotation, flip);
          
          int blockRows = block.getRow();
          int blockCols = block.getCol();
          
          int maxRow = board.getRows() - blockRows;
          int maxCol = board.getCols() - blockCols;

          if (maxRow < 0 || maxCol < 0) continue;

          for (int i = 0; i <= maxRow; i++) {
              for (int j = 0; j <= maxCol; j++) {
                  iterationCount++;
                  if (board.canPlaceBlock(block, i, j)) {
                      board.placeBlock(block, i, j, block.getSymbol());
                      block.setPlaced(true); 
                      if (solve(blockIndex + 1)) {
                          return true;
                      }
                      board.removeBlock(block, i, j);
                      block.setPlaced(false);
                  }
              }
          }
      }
    }
    return false;
  }

  private boolean isBoardFullyFilled() {
    for (int i = 0; i < board.getRows(); i++) {
        for (int j = 0; j < board.getCols(); j++) {
            if (board.getGrid().get(i).get(j) == '.') {
                return false;
            }
        }
    }
    return true;
  }

  private boolean allBlocksPlaced() {
    for (Block block : blocks) {
        if (!block.isPlaced()) {
            return false;
        }
    }
    return true;
  }

  public Board getBoard() {
    return board;
  }

  public long getIterationCount() {
    return iterationCount;
  }
}
