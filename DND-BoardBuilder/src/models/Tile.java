package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tile implements Cloneable {
    public enum TileCategory {
        FLOORING, WALLS, TREES
    }

    public static final Tile GRASS = new Tile("Grass", TileCategory.FLOORING);
    public static final Tile ORANGE_GRASS = new Tile("Orange Grass", TileCategory.FLOORING);
    public static final Tile WATER = new Tile("Water", TileCategory.FLOORING);
    public static final Tile STONE_WALL = new Tile("Stone", TileCategory.FLOORING);
    public static final Tile BRICK_WALL = new Tile("Brick wall", TileCategory.WALLS);
    public static final Tile OAK_TREE = new Tile("Oak Tree", TileCategory.TREES);

    private static final List<Tile> ALL_TILES;

    static {
        List<Tile> tiles = new ArrayList<>();
        tiles.add(GRASS);
        tiles.add(ORANGE_GRASS);
        tiles.add(WATER);
        tiles.add(STONE_WALL);
        tiles.add(BRICK_WALL);
        tiles.add(OAK_TREE);
        ALL_TILES = Collections.unmodifiableList(tiles);
    }

    private String type;
    private int x;
    private int y;
    private double rotation;
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

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
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
                ", rotation=" + rotation +
                ", category=" + category +
                '}';
    }

    @Override
    public Tile clone() {
        try {
            return (Tile) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static Tile getTileByName(String selectedItem) {
        for (Tile tile : ALL_TILES) {
            if (tile.getType().equalsIgnoreCase(selectedItem)) {
                return tile;
            }
        }
        return null; // or throw an exception if preferred
    }

    public static List<Tile> getAllTiles() {
        return ALL_TILES;
    }

    public static Tile[] values() {
        return ALL_TILES.toArray(new Tile[0]);
    }
}
