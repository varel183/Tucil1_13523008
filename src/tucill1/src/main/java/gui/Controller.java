package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import logic.Block;
import logic.Board;
import logic.Solver;
import utils.ColorManager;
import utils.InputHandler;
import utils.OutputHandler;

public class Controller implements Initializable {

    @FXML private Button loadButton;
    @FXML private Button solveButton;
    @FXML private Button saveButton;
    @FXML private Button saveImageButton;
    @FXML private TextField fileNameField;
    @FXML private GridPane boardGrid;
    @FXML private GridPane blocksPreview;
    @FXML private Label statusLabel;
    @FXML private Label timeLabel;
    @FXML private Label iterationsLabel;
    
    private Board board;
    private List<Block> blocks;
    private Solver solver;

    static {

    }
    
    // private final Map<Character, Color> colorMap = new HashMap<>();
    // private final Color[] blockColors = {
    //     Color.RED, Color.BLUE, Color.GREEN, Color.PURPLE, Color.ORANGE, 
    //     Color.CYAN, Color.MAGENTA, Color.BROWN, Color.DARKBLUE, Color.DARKGREEN,
    //     Color.DARKORANGE, Color.DARKRED, Color.LIGHTBLUE, Color.LIGHTGREEN, Color.LIGHTPINK,
    //     Color.LIGHTYELLOW, Color.DARKGRAY, Color.GOLD, Color.SILVER, Color.TEAL,
    //     Color.VIOLET, Color.TOMATO, Color.SALMON, Color.OLIVE, Color.INDIANRED, Color.CORNFLOWERBLUE
    // };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        solveButton.setDisable(true);
        saveButton.setDisable(true);
        saveImageButton.setDisable(true);
    }
    
    @FXML
    private void handleLoadPuzzle(String fileName) {
        // FileChooser fileChooser = new FileChooser();
        // fileChooser.setTitle("Open Puzzle File");
        // fileChooser.getExtensionFilters().add(
        //     new FileChooser.ExtensionFilter("Text Files", "*.txt")
        // );
        
        
        // File selectedFile = fileChooser.showOpenDialog(loadButton.getScene().getWindow());
        if (fileName != null) {
            try {
                InputHandler inputHandler = new InputHandler(fileName + ".txt");
                board = new Board(inputHandler.getN(), inputHandler.getM());
                blocks = inputHandler.getBlocks();
                solver = new Solver(board, blocks);
                
                // setupBlockColors();
                
                updateBoardDisplay();
                updateBlocksPreview();
                statusLabel.setText("Status: Puzzle loaded successfully");
                timeLabel.setText("Solving time: -");
                iterationsLabel.setText("Iterations: -");
                
                solveButton.setDisable(false);
                
            } catch (Exception ex) {
                showAlert("Error", "Failed to load puzzle file", ex.getMessage());
            }
        }
    }
    
    @FXML
    private void handleSolvePuzzle() {
        if (board == null || blocks == null) {
            showAlert("Error", "No puzzle loaded", "Please load a puzzle file first.");
            return;
        }
        
        statusLabel.setText("Status: Solving puzzle...");
        solveButton.setDisable(true);
        
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            boolean solved = solver.solve(0);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            
            Platform.runLater(() -> {
                if (solved) {
                    statusLabel.setText("Status: Solution found!");
                    timeLabel.setText(String.format("Solving time: %d ms", elapsedTime));
                    iterationsLabel.setText(String.format("Iterations: %d", solver.getIterationCount()));
                    updateBoardDisplay();
                    saveButton.setDisable(false);
                    saveImageButton.setDisable(false);
                } else {
                    statusLabel.setText("Status: No solution found");
                    timeLabel.setText(String.format("Solving time: %d ms", elapsedTime));
                    iterationsLabel.setText(String.format("Iterations: %d", solver.getIterationCount()));
                }
                solveButton.setDisable(false);
            });
        }).start();
    }
    
    @FXML
    private void handleSaveSolution() {
        if (board == null || !solver.isSolved()) {
            showAlert("Error", "No solution to save", "Please solve the puzzle first.");
            return;
        }
        
        String fileName = fileNameField.getText();
        if (fileName == null || fileName.trim().isEmpty()) {
            showAlert("Error", "Invalid file name", "Please enter a valid file name.");
            return;
        }
        
        try {
            OutputHandler.saveToFile(board, fileName);
            showAlert("Success", "Solution saved", "Solution saved to " + fileName);
        } catch (Exception e) {
            showAlert("Error", "Failed to save solution", e.getMessage());
        }
    }

    @FXML
    private void handleSaveImageSolution() {
        if (board == null || !solver.isSolved()) {
            showAlert("Error", "No solution to save", "Please solve the puzzle first.");
            return;
        }
        
        String fileName = fileNameField.getText();
        if (fileName == null || fileName.trim().isEmpty()) {
            showAlert("Error", "Invalid file name", "Please enter a valid file name.");
            return;
        }
        
        try {
            OutputHandler.saveToImage(board, fileName);
            showAlert("Success", "Solution saved", "Solution saved to " + fileName);
        } catch (Exception e) {
            showAlert("Error", "Failed to save solution", e.getMessage());
        }
    }
    
    // private void setupBlockColors() {
    //     colorMap.clear();
    //     int colorIndex = 0;
        
    //     for (Block block : blocks) {
    //         char symbol = block.getSymbol();
    //         colorMap.put(symbol, blockColors[colorIndex % blockColors.length]);
    //         colorIndex++;
    //     }
    // }
    
    private void updateBoardDisplay() {
        boardGrid.getChildren().clear();
        
        if (board == null) return;
        
        int rows = board.getRows();
        int cols = board.getCols();
        
        double cellSize = Math.min(400 / cols, 400 / rows);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Rectangle cell = new Rectangle(cellSize, cellSize);
                char cellValue = board.getGrid().get(i).get(j);
                
                if (cellValue == '.') {
                    cell.setFill(ColorManager.EMPTY_CELL);
                    cell.setStroke(ColorManager.EMPTY_CELL_BORDER);
                } else {
                    cell.setFill(ColorManager.getBlockColor(cellValue));
                    cell.setStroke(ColorManager.BLOCK_BORDER);
                }
                
                boardGrid.add(cell, j, i);
            }
        }
    }
    
    private void updateBlocksPreview() {
        blocksPreview.getChildren().clear();
        
        if (blocks == null) return;
        
        int blockIndex = 0;
        int maxBlocksPerRow = 3;
        
        for (Block block : blocks) {
            GridPane blockGrid = new GridPane();
            blockGrid.setHgap(1);
            blockGrid.setVgap(1);
            
            List<List<Character>> shape = block.getShape();
            char symbol = block.getSymbol();
            Color blockColor = ColorManager.getBlockColor(symbol);
            
            Label blockLabel = new Label("Block " + symbol);
            blockGrid.add(blockLabel, 0, 0, shape.get(0).size(), 1);
            
            for (int i = 0; i < shape.size(); i++) {
                for (int j = 0; j < shape.get(i).size(); j++) {
                    Rectangle cell = new Rectangle(15, 15);
                    
                    if (shape.get(i).get(j) != '.') {
                        cell.setFill(blockColor);
                        cell.setStroke(ColorManager.BLOCK_BORDER);
                    } else {
                        cell.setFill(ColorManager.TRANSPARENT);
                        cell.setStroke(ColorManager.TRANSPARENT);
                    }
                    
                    blockGrid.add(cell, j, i + 1);
                }
            }
            
            int row = blockIndex / maxBlocksPerRow;
            int col = blockIndex % maxBlocksPerRow;
            
            blocksPreview.add(blockGrid, col, row);
            blockIndex++;
        }
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}