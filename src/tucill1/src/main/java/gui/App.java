package gui;

import java.io.IOException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logic.Block;
import logic.Board;
import logic.Solver;
import utils.ColorManager;
import utils.InputHandler;
import utils.OutputHandler;

public class App extends Application {

    private Board board;
    private List<Block> blocks;
    private Solver solver;
    private GridPane boardGrid;
    private GridPane blocksPreview;
    private Label statusLabel;
    private Label iterationsLabel;
    private Label timeLabel;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        HBox topControls = createTopControls(primaryStage);
        root.setTop(topControls);
        
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(1);
        boardGrid.setVgap(1);
        boardGrid.setPadding(new Insets(20));
        
        blocksPreview = new GridPane();
        blocksPreview.setAlignment(Pos.CENTER);
        blocksPreview.setHgap(10);
        blocksPreview.setVgap(10);
        blocksPreview.setPadding(new Insets(20));
        
        HBox centerContent = new HBox(20, boardGrid, blocksPreview);
        centerContent.setAlignment(Pos.CENTER);
        root.setCenter(centerContent);
        
        VBox statusBox = createStatusBox();
        root.setBottom(statusBox);
        
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("IQ Puzzler Pro Solver");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private HBox createTopControls(Stage primaryStage) {
        TextField inputFileNameField = new TextField();
        inputFileNameField.setPromptText("Input file name");

        Button loadButton = new Button("Load Puzzle File");
        Button solveButton = new Button("Solve Puzzle");
        Button saveButton = new Button("Save Solution");
        Button saveImageButton = new Button("Save Solution to Image");
        
        TextField outputFileNameField = new TextField();
        outputFileNameField.setPromptText("Output file name");
        
        loadButton.setOnAction(e -> loadPuzzleFile(inputFileNameField.getText(), primaryStage));
        solveButton.setOnAction(e -> solvePuzzle());
        saveButton.setOnAction(e -> saveSolution(outputFileNameField.getText()));
        saveImageButton.setOnAction(e -> saveImageSolution(outputFileNameField.getText()));
        
        HBox controls = new HBox(10, inputFileNameField, loadButton, solveButton, saveButton, saveImageButton, outputFileNameField);
        controls.setPadding(new Insets(10));
        controls.setAlignment(Pos.CENTER);
        
        return controls;
    }
    
    private VBox createStatusBox() {
        statusLabel = new Label("Status: Ready");
        timeLabel = new Label("Solving time: -");
        iterationsLabel = new Label("Iterations: -");
        
        VBox statusBox = new VBox(5, statusLabel, timeLabel, iterationsLabel);
        statusBox.setPadding(new Insets(10));
        statusBox.setAlignment(Pos.CENTER_LEFT);
        
        return statusBox;
    }
    
    private void loadPuzzleFile(String fileName, Stage primaryStage) {
        if (fileName != null) {
            try {
                InputHandler inputHandler = new InputHandler(fileName + ".txt");
                board = new Board(inputHandler.getN(), inputHandler.getM());
                blocks = inputHandler.getBlocks();
                solver = new Solver(board, blocks);
                
                updateBoardDisplay();
                updateBlocksPreview();
                statusLabel.setText("Status: Puzzle loaded successfully");
                timeLabel.setText("Solving time: -");
                iterationsLabel.setText("Iterations: -");
                
            } catch (Exception ex) {
                showAlert("Error", "Failed to load puzzle file", ex.getMessage());
            }
        }
    }

    
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
    
    private void solvePuzzle() {
        if (board == null || blocks == null) {
            showAlert("Error", "No puzzle loaded", "Please load a puzzle file first.");
            return;
        }
        
        statusLabel.setText("Status: Solving puzzle...");
        
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
                } else {
                    statusLabel.setText("Status: No solution found");
                    timeLabel.setText(String.format("Solving time: %d ms", elapsedTime));
                    iterationsLabel.setText(String.format("Iterations: %d", solver.getIterationCount()));
                }
            });
        }).start();
    }
    
    private void saveSolution(String fileName) {
        if (board == null || !solver.isSolved()) {
            showAlert("Error", "No solution to save", "Please solve the puzzle first.");
            return;
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            showAlert("Error", "Invalid file name", "Please enter a valid file name.");
            return;
        }
        
        try {
            OutputHandler.saveToFile(board, fileName);
            showAlert("Success", "Solution saved", "Solution saved to " + fileName);
        } catch (IOException e) {
            showAlert("Error", "Failed to save solution", e.getMessage());
        }
    }

    private void saveImageSolution(String fileName) {
        if (board == null || !solver.isSolved()) {
            showAlert("Error", "No solution to save", "Please solve the puzzle first.");
            return;
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            showAlert("Error", "Invalid file name", "Please enter a valid file name.");
            return;
        }
        
        OutputHandler.saveToImage(board, fileName);
        showAlert("Success", "Solution saved", "Solution saved to " + fileName);
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}