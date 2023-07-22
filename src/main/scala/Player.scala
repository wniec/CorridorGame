import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.effect.BlendMode.Blue
import scalafx.scene.layout.{Background, HBox, VBox}
import scalafx.scene.paint.Color.{Green, LightGreen, Purple, White}
import scalafx.scene.text.Font

import scala.collection.mutable.ArrayBuffer
class Player(var row: Int, var col: Int, var color:Color) {
  var walls_available = 5
  def position: (Int, Int) = (row,col)
  def move(row:Int,col:Int):Unit ={
    this.row = row
    this.col = col
  }
  def info_box(round_color: Color): VBox = {
    val palette = color.get_paint()
    var textColor = palette._2
    if(round_color==this.color) {
      textColor = Green
    }
    val nameLabel: Label = new Label("Player "+color.toString)
    val wallLabel: Label = new Label("available walls: "+walls_available)
    nameLabel.setTextFill(textColor)
    wallLabel.setTextFill(textColor)
    nameLabel.setFont(Font.font("Arial",20.0))
    wallLabel.setFont(Font.font("Arial",15.0))
    val vbox = new VBox {
      alignment = Pos.Center
      children = Seq(nameLabel,wallLabel)
      prefHeight = 154
      prefWidth = 154
      background = Background.fill(palette._1)
    }
    vbox
  }
}
