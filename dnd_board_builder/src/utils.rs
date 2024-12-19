use image::ImageReader;

pub fn load_image(path: &str) -> Result<image::DynamicImage, Box<dyn std::error::Error>> {
    let img = ImageReader::open(path)?.decode()?;
    Ok(img)
}
