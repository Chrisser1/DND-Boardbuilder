mod board;
mod grid;

use board::Board;
use grid::Tile;

use eframe::egui;
use egui::ImageSource;

fn main() -> Result<(), eframe::Error> {
    let options = eframe::NativeOptions::default();
    eframe::run_native("D&D Board Builder", options, Box::new(|cc| Ok(Box::new(App::new(cc)))))
}

pub struct App {
    board: Board,
    selected_tool: Tile, // The tile type the user is currently placing
}

impl App {
    fn new(cc: &eframe::CreationContext<'_>) -> Self {
        egui_extras::install_image_loaders(&cc.egui_ctx);
        Self {
            board: Board::new(10, 10),
            selected_tool: Tile::Wall, // default tool selected
        }
    }
}

impl eframe::App for App {
    fn update(&mut self, ctx: &egui::Context, frame: &mut eframe::Frame) {
        egui::CentralPanel::default().show(ctx, |ui| {
            ui.heading("D&D Board Builder");
            ui.separator();

            // A toolbar for selecting the currently placed tile type:
            ui.horizontal(|ui| {
                if ui.button("Place empty").clicked() {
                    self.selected_tool = Tile::Empty;
                }
                if ui.button("Place Wall").clicked() {
                    self.selected_tool = Tile::Wall;
                }
                if ui.button("Place floor").clicked() {
                    self.selected_tool = Tile::Floor;
                }
            });

            ui.separator();

            // Draw the grid
            egui::Grid::new("board_grid")
                .spacing(egui::Vec2::new(4.0, 4.0))
                .show(ui, |ui| {
                    for y in 0..self.board.height() {
                        for x in 0..self.board.width() {
                            let tile = self.board.get_tile(x, y);
                            let texture = match tile {
                                Tile::Empty => egui::include_image!("../../assets/unknown.png"),
                                Tile::Wall => egui::include_image!("../../assets/wall.png"),
                                Tile::Floor => egui::include_image!("../../assets/floor.png"),
                            };

                            // Use ImageButton to detect clicks
                            if ui.add(egui::ImageButton::new(texture)).clicked() {
                                // Place the currently selected tool's tile type
                                self.board.set_tile(x, y, self.selected_tool.clone());
                            }
                        }
                        ui.end_row();
                    }
                });
        });
    }
}
