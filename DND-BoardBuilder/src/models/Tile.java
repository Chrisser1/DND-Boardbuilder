package models;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    public enum TileCategory {
        FLOORING, WALLS, TREES
    }

    public static final Tile GRASS = new Tile("Grass", TileCategory.FLOORING);
    public static final Tile ORANGE_GRASS = new Tile("Orange Grass", TileCategory.FLOORING);
    public static final Tile WATER = new Tile("Water", TileCategory.FLOORING);
    public static final Tile STONE_WALL = new Tile("Stone", TileCategory.WALLS);
    public static final Tile OAK_TREE = new Tile("Oak Tree", TileCategory.TREES);

    private String type;
    private int x;
    private int y;
    private TileCategory category;

    private Tile(String type, TileCategory category) {
        this.type = type;
        this.category = category;
    }

    public Tile(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TileCategory getCategory() {
        return category;
    }

    public void setCategory(TileCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", category=" + category +
                '}';
    }

    public static Tile getTileByName(String selectedItem) {
        for (Tile tile : getAllTiles()) {
            if (tile.getType().equalsIgnoreCase(selectedItem)) {
                return tile;
            }
        }
        return null; // or throw an exception if preferred
    }

    private static List<Tile> getAllTiles() {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.GRASS);
        tiles.add(Tile.OAK_TREE);

        return tiles;
    }
}
