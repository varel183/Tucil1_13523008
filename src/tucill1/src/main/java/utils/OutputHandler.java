package utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import logic.Board;

public class OutputHandler {
  public static void saveToFile(Board board, String filePath) throws IOException {
    FileWriter fileWriter = new FileWriter("test/output/" + filePath);
    List<List<Character>> grid = board.getGrid();
    for (List<Character> row : grid) {
      for (Character cell : row) {
          fileWriter.write(cell);
      }
      fileWriter.write("\n");
  }
    fileWriter.close();
  }

  public static void saveToImage(Board board, String filePath) {
    int cellSize = 50;
    List<List<Character>> grid = board.getGrid();
    int rows = grid.size();
    int cols = grid.get(0).size();
    
    BufferedImage image = new BufferedImage(cols * cellSize, rows * cellSize, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            char cell = grid.get(i).get(j);
            if (cell == '.') {
              g.setColor(Color.WHITE);
            } else {
              g.setColor(Color.decode(ColorManager.getHexColor(cell)));
            }
            
            g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);

            g.setColor(Color.BLACK);
            g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(cell), j * cellSize + 20, i * cellSize + 30);
        }
    }
    g.dispose();
    try {
        ImageIO.write(image, "png", new File("test/output/" + filePath + ".png"));
    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
  }
}
