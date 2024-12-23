package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
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
    private TextArea statusLabel;

    @FXML
    private Canvas boardCanvas;

    private final int TILE_SIZE = 64;
    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 8;

    private Board board;

    // Current selected tile type
    private Tile currentTile = Tile.GRASS;

    @FXML
    public void initialize() {
        // Init the board
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT, Tile.GRASS);

        // Initialize ChoiceBoxes with tile options
        initializeChoiceBoxes();
        drawBoard();
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

    private void drawBoard() {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());

        // Draw tiles
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Tile tile = board.getTile(x, y);
                Image tileImage = TileManager.getTileImage(tile);
                if (tileImage != null) {
                    gc.drawImage(tileImage, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
