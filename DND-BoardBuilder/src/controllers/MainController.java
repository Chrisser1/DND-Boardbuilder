package controllers;

import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import models.Board;
import models.Tile;
import utils.TileManager;

public class MainController {

    @FXML
    private ListView<Tile> tileListView;

    @FXML
    private Text statusLabel;

    @FXML
    private Canvas boardCanvas;

    private final int BOARD_WIDTH = 80;
    private final int BOARD_HEIGHT = 64;

    private Board board;
    private Stack<Board> undoStack = new Stack<>();
    private Stack<Board> redoStack = new Stack<>();

    // Current selected tile type
    private Tile currentTile = Tile.GRASS;
    private byte rotationAngle = 0;

    @FXML
    public void initialize() {
        // Init the board
        board = new Board(BOARD_WIDTH, BOARD_HEIGHT, Tile.GRASS);

        // Initialize ListView with tile options
        initializeTileListView();
        drawBoard();

        // Add mouse event handlers to the canvas
        boardCanvas.setOnMouseClicked(event -> handleMouseEvent(event.getX(), event.getY()));
        boardCanvas.setOnMouseDragged(event -> handleMouseEvent(event.getX(), event.getY()));

        // Bind canvas size to AnchorPane size
        boardCanvas.widthProperty().bind(((AnchorPane) boardCanvas.getParent()).widthProperty().subtract(174));
        boardCanvas.heightProperty().bind(((AnchorPane) boardCanvas.getParent()).heightProperty());

        // Redraw the board when the canvas size changes
        boardCanvas.widthProperty().addListener(evt -> drawBoard());
        boardCanvas.heightProperty().addListener(evt -> drawBoard());

        // Add key event handler for undo, redo, and rotate after the scene is set
        boardCanvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyEvent);
            }
        });
    }

    private void initializeTileListView() {
        tileListView.setItems(FXCollections.observableArrayList(Tile.values()));
        tileListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Tile> call(ListView<Tile> param) {
                return new ListCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Tile item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            imageView.setImage(TileManager.getTileImage(item));
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            setGraphic(imageView);
                            setText(item.getType());
                        }
                    }
                };
            }
        });

        tileListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentTile = newValue;
                statusLabel.setText("Selected Tile: " + newValue.getType());
                rotationAngle = 0; // Reset rotation angle when a new tile is selected
                drawBoard();
            }
        });

        tileListView.getSelectionModel().selectFirst();
    }

    private void handleMouseEvent(double x, double y) {
        double tileWidth = boardCanvas.getWidth() / BOARD_WIDTH;
        double tileHeight = boardCanvas.getHeight() / BOARD_HEIGHT;

        int tileX = (int) (x / tileWidth);
        int tileY = (int) (y / tileHeight);

        Tile boardTile = board.getTile(tileX, tileY);

        if (tileX >= 0 && tileX < BOARD_WIDTH && tileY >= 0 && tileY < BOARD_HEIGHT &&
            (!boardTile.getType().equals(currentTile.getType()) ||
            boardTile.getRotation() != currentTile.getRotation())) {
            saveStateToUndoStack();
            board.setTile(tileX, tileY, currentTile.clone());
            drawBoard();
        }
    }

    private void handleKeyEvent(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.Z) {
            undo();
        } else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
            redo();
        } else if (event.getCode() == KeyCode.R) {
            rotateTile();
        }
    }

    private void rotateTile() {
        rotationAngle = (byte) ((rotationAngle + 1) % 4);
        currentTile.setRotation(rotationAngle * 90);
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
        if (boardCanvas.getGraphicsContext2D() == null) {
            return;
        }

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
                    gc.save();
                    gc.translate((x + 0.5) * tileWidth, (y + 0.5) * tileHeight);
                    gc.drawImage(tileImage, -tileWidth / 2, -tileHeight / 2, tileWidth, tileHeight);
                    gc.restore();
                }
            }
        }
    }
}
