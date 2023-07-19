import scalafx.scene.image.Image
enum Color:
  case White, Black
  def value:Int={
    this match {
      case White => 1
      case Black => 0
    }
  }
  def get_image(): Image = {
    this match {
      case Black => new Image("file:src/main/resources/red.png")
      case White => new Image("file:src/main/resources/blue.png")
    }
  }