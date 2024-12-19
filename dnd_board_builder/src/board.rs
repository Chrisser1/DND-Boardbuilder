use serde::{Serialize, Deserialize};
use std::fs;
use std::error::Error;

use crate::grid::{Grid, Tile};

#[derive(Serialize, Deserialize)]
pub struct Board {
    grid: Grid,
}

impl Board {
    pub fn new(width: usize, height: usize) -> Self {
        Board {
            grid: Grid::new(width, height),
        }
    }

    pub fn get_tile(&self, x: usize, y: usize) -> &Tile {
        self.grid.get_tile(x, y)
    }

    pub fn set_tile(&mut self, x: usize, y: usize, tile: Tile) {
        self.grid.set_tile(x, y, tile);
    }

    pub fn width(&self) -> usize {
        self.grid.width
    }

    pub fn height(&self) -> usize {
        self.grid.height
    }
}

pub fn save_board(board: &Board, path: &str) -> Result<(), Box<dyn Error>> {
    let json = serde_json::to_string(board)?;
    fs::write(path, json)?;
    Ok(())
}

pub fn load_board(path: &str) -> Result<Board, Box<dyn Error>> {
    let json = fs::read_to_string(path)?;
    let board: Board = serde_json::from_str(&json)?;
    Ok(board)
}
