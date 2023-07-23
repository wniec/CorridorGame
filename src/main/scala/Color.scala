import javafx.scene.paint.Stop
import scalafx.scene.image.Image
import scalafx.scene.paint.{CycleMethod, LinearGradient}
enum Color:
  case Black, Blue, White, Red
  def get_palette: (scalafx.scene.paint.LinearGradient,scalafx.scene.paint.Color) = {
    this match {
      case Black =>  (lin_grad(true),scalafx.scene.paint.Color.White)
      case White => (lin_grad(true),scalafx.scene.paint.Color.rgb(127,127,127))
      case Red => (lin_grad(true),scalafx.scene.paint.Color.White)
      case Blue => (lin_grad(true),scalafx.scene.paint.Color.White)
    }
  }

  def get_paint: scalafx.scene.paint.Color = {
    this match {
      case Black => scalafx.scene.paint.Color.Black
      case White => scalafx.scene.paint.Color.White
      case Red => scalafx.scene.paint.Color.Red
      case Blue => scalafx.scene.paint.Color.Blue
    }
  }
  private def get_stops: Array[Stop] = {
    this match {
      case Black => Array(new Stop(1,scalafx.scene.paint.Color.rgb(20,20,20)), new Stop(0,this.get_paint))
      case White => Array(new Stop(0,this.get_paint),new Stop(1,scalafx.scene.paint.Color.rgb(160,160,160)))
      case Red => Array(new Stop(0,this.get_paint),new Stop(1,scalafx.scene.paint.Color.Black))
      case Blue => Array(new Stop(0,this.get_paint),new Stop(1,scalafx.scene.paint.Color.Black))
    }
  }

  def lin_grad(horizontal: Boolean): LinearGradient = {
    val stops = get_stops
    if (horizontal)
      new LinearGradient(0,0,1,0,true,CycleMethod.NoCycle, stops)
    else
      new LinearGradient(0,0,0,1,true,CycleMethod.NoCycle, stops)
  }