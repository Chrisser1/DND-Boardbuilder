package models;

public class Board {
    private int width;
    private int height;
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
}
