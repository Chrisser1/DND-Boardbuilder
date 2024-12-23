package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;
import models.Tile;

public class TileManager {

    private static final Map<String, Image> tileImages = new HashMap<>();

    static {
        loadTileImages();
    }

    private static void loadTileImages() {
        String basePath = "/assets/tiles/";
        tileImages.put("Grass", new Image(basePath + "flooring/grass.png"));
        tileImages.put("Orange Grass", new Image(basePath + "flooring/orange_grass.png"));
        tileImages.put("Water", new Image(basePath + "flooring/water.png"));
        tileImages.put("Stone", new Image(basePath + "walls/stone_wall.png"));
        tileImages.put("Oak Tree", new Image(basePath + "trees/fornow.png"));
    }

    public static Image getTileImage(Tile tile) {
        return tileImages.get(tile.getType());
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
        for (Tile tile : getAllTiles()) {
            if (tile.getCategory() == category) {
                tileNames.add(tile.getType());
            }
        }
        return tileNames;
    }

    private static List<Tile> getAllTiles() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.GRASS);
        tiles.add(Tile.ORANGE_GRASS);
        tiles.add(Tile.WATER);
        tiles.add(Tile.STONE_WALL);
        tiles.add(Tile.OAK_TREE);
        return tiles;
    }
}
