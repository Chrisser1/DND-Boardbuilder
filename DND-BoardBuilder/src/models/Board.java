package models;

import java.io.*;

public class Board implements Cloneable, Serializable {
    private final int width;
    private final int height;
    private Tile[][] tiles;

    public Board(int width, int height, Tile defaultTile) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(defaultTile.getType(), x, y);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return tiles[x][y];
        }
        return null;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[x][y] = tile;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public Board clone() {
        try {
            Board cloned = (Board) super.clone();
            cloned.tiles = new Tile[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    cloned.tiles[x][y] = this.tiles[x][y].clone();
                }
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void saveToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public static Board loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Board) ois.readObject();
        }
    }
}
