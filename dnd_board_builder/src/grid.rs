use std::vec;
use serde::{Deserialize, Serialize};


#[derive(Serialize, Deserialize)]
pub struct Grid {
    pub width: usize,
    pub height: usize,
    pub tiles: Vec<Tile>,
}

impl Grid {
    pub fn new(width: usize, height: usize) -> Self {
        let tiles = vec![Tile::Empty; width * height];
        Grid {
            width,
            height,
            tiles,
        }
    }

    pub fn set_tile(&mut self, x: usize, y: usize, tile: Tile) {
        if x < self.width && y < self.height {
            self.tiles[y * self.width + x] = tile;
        }
    }

    pub fn get_tile(&self, x: usize, y: usize) -> &Tile {
        &self.tiles[y * self.width + x]
    }
}

#[derive(Clone, PartialEq, Serialize, Deserialize)]
pub enum Tile {
    Empty,
    Wall,
    Floor,
}

impl Default for Tile {
    fn default() -> Self {
        Tile::Empty
    }
}
