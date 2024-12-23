package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import models.Tile;

public class TileManager {

    private static final Map<String, Image[]> tileImages = new HashMap<>();

    static {
        loadTileImages();
    }

    private static void loadTileImages() {
        String basePath = "/assets/tiles/";
        loadTileImage("Grass", basePath + "flooring/grass.png");
        loadTileImage("Orange Grass", basePath + "flooring/orange_grass.png");
        loadTileImage("Water", basePath + "flooring/water.png");
        loadTileImage("Stone", basePath + "flooring/stone.png");
        loadTileImage("Brick wall", basePath + "walls/brick.png");
        loadTileImage("Oak Tree", basePath + "trees/fornow.png");
    }

    private static void loadTileImage(String tileName, String imagePath) {
        Image originalImage = new Image(imagePath);
        Image[] rotations = new Image[4];
        rotations[0] = originalImage;
        for (int i = 1; i < 4; i++) {
            ImageView imageView = new ImageView(rotations[i - 1]);
            imageView.getTransforms().add(new Rotate(90, originalImage.getWidth() / 2, originalImage.getHeight() / 2));
            rotations[i] = imageView.snapshot(new SnapshotParameters(), null);
        }
        tileImages.put(tileName, rotations);
    }

    public static Image getTileImage(Tile tile) {
        Image[] rotations = tileImages.get(tile.getType());
        if (rotations == null) {
            return null;
        }
        int rotationIndex = (int) (tile.getRotation() / 90) % 4;
        return rotations[rotationIndex];
    }

    public static List<String> getFlooringTileNames() {
        return getTileNamesByCategory(Tile.TileCategory.FLOORING);
    }

    public static List<String> getWallsTileNames() {
        return getTileNamesByCategory(Tile.TileCategory.WALLS);
    }

    public static List<String> getTreesTileNames() {
        return getTileNamesByCategory(Tile.TileCategory.TREES);
    }

    private static List<String> getTileNamesByCategory(Tile.TileCategory category) {
        List<String> tileNames = new ArrayList<>();
        for (Tile tile : Tile.getAllTiles()) {
            if (tile.getCategory() == category) {
                tileNames.add(tile.getType());
            }
        }
        return tileNames;
    }
}
