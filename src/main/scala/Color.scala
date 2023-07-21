import scalafx.scene.image.Image
enum Color:
  case Black, White, Red, Blue
  def value:Int={
    this match {
      case Black => 0
      case White => 1
      case Red => 2
      case Blue => 3
    }
  }
  def get_image(): Image = {
    this match {
      case Black => new Image("file:src/main/resources/black.png")
      case White => new Image("file:src/main/resources/white.png")
      case Red => new Image("file:src/main/resources/red.png")
      case Blue => new Image("file:src/main/resources/blue.png")
    }
  }

  def get_paint(): (scalafx.scene.paint.Color,scalafx.scene.paint.Color) = {
    this match {
      case Black =>  (scalafx.scene.paint.Color.Black,scalafx.scene.paint.Color.White)
      case White => (scalafx.scene.paint.Color.White,scalafx.scene.paint.Color.DarkGray)
      case Red => (scalafx.scene.paint.Color.Red,scalafx.scene.paint.Color.White)
      case Blue => (scalafx.scene.paint.Color.Blue,scalafx.scene.paint.Color.White)
    }
  }