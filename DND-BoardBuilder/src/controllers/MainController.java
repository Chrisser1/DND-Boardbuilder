package controllers;

import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import models.Board;
import models.Tile;
import utils.TileManager;

public class MainController {

    @FXML
    private ChoiceBox<String> flooringChoiceBox;

    @FXML
    private ChoiceBox<String> wallsChoiceBox;

    @FXML
    private ChoiceBox<String> treesChoiceBox;

    @FXML
    private Text statusLabel;

    @FXML
    private Canvas boardCanvas;

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 10;

    private Board board;
    private Stack<Board> undoStack = new Stack<>();
    private Stack<Board> redoStack = new Stack<>();

    // Current selected tile type
    private Tile currentTile = Tile.GRASS;

    @FXML
    public void initialize() {
        // Init the board
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT, Tile.GRASS);

        // Initialize ChoiceBoxes with tile options
        initializeChoiceBoxes();
        drawBoard();

        // Add mouse event handlers to the canvas
        boardCanvas.setOnMouseClicked(event -> handleMouseEvent(event.getX(), event.getY()));
        boardCanvas.setOnMouseDragged(event -> handleMouseEvent(event.getX(), event.getY()));

        // Bind canvas size to AnchorPane size
        boardCanvas.widthProperty().bind(((AnchorPane) boardCanvas.getParent()).widthProperty());
        boardCanvas.heightProperty().bind(((AnchorPane) boardCanvas.getParent()).heightProperty().subtract(37)); // Subtract toolbar height

        // Redraw the board when the canvas size changes
        boardCanvas.widthProperty().addListener(evt -> drawBoard());
        boardCanvas.heightProperty().addListener(evt -> drawBoard());

        // Add key event handler for undo and redo after the scene is set
        boardCanvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyEvent);
            }
        });
    }

    private void initializeChoiceBoxes() {
        // Flooring ChoiceBox
        flooringChoiceBox.setItems(FXCollections.observableArrayList(TileManager.getFlooringTileNames()));
        flooringChoiceBox.getSelectionModel().selectFirst();
        currentTile = Tile.getTileByName(flooringChoiceBox.getSelectionModel().getSelectedItem());

        flooringChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentTile = Tile.getTileByName(newValue);
            statusLabel.setText("Selected Flooring: " + newValue);
            drawBoard();
        });

        // Walls ChoiceBox
        wallsChoiceBox.setItems(FXCollections.observableArrayList(TileManager.getWallsTileNames()));
        wallsChoiceBox.getSelectionModel().selectFirst();

        wallsChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentTile = Tile.getTileByName(newValue);
            statusLabel.setText("Selected Walls: " + newValue);
            drawBoard();
        });

        // Trees ChoiceBox
        treesChoiceBox.setItems(FXCollections.observableArrayList(TileManager.getTreesTileNames()));
        treesChoiceBox.getSelectionModel().selectFirst();

        treesChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentTile = Tile.getTileByName(newValue);
            statusLabel.setText("Selected Trees: " + newValue);
            drawBoard();
        });
    }

    private void handleMouseEvent(double x, double y) {
        double tileWidth = boardCanvas.getWidth() / BOARD_WIDTH;
        double tileHeight = boardCanvas.getHeight() / BOARD_HEIGHT;

        int tileX = (int) (x / tileWidth);
        int tileY = (int) (y / tileHeight);

        if (tileX >= 0 && tileX < BOARD_WIDTH && tileY >= 0 && tileY < BOARD_HEIGHT && board.getTile(tileX, tileY).getType() != currentTile.getType()) {
            saveStateToUndoStack();
            board.setTile(tileX, tileY, currentTile);
            drawBoard();
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            undo();
        } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
            redo();
        }
    }

    private void saveStateToUndoStack() {
        undoStack.push(board.clone());
        redoStack.clear();
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(board.clone());
            board = undoStack.pop();
            drawBoard();
        }
    }

    private void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(board.clone());
            board = redoStack.pop();
            drawBoard();
        }
    }

    private void drawBoard() {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

        double canvasWidth = boardCanvas.getWidth();
        double canvasHeight = boardCanvas.getHeight();
        double tileWidth = canvasWidth / BOARD_WIDTH;
        double tileHeight = canvasHeight / BOARD_HEIGHT;

        // Draw tiles
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                Tile tile = board.getTile(x, y);
                Image tileImage = TileManager.getTileImage(tile);
                if (tileImage != null) {
                    gc.drawImage(tileImage, x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
    }
}
