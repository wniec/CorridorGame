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
    val palette = color.get_palette
    var text_color = palette._2
    if(round_color==this.color) {
      text_color = Green
    }
    val nameLabel: Label = new Label("PLAYER "+color.toString.toUpperCase())
    val wallLabel: Label = new Label("AVAILABLE WALLS: "+walls_available)
    nameLabel.setTextFill(text_color)
    wallLabel.setTextFill(text_color)
    nameLabel.setFont(Font.font("Impact",20.0))
    wallLabel.setFont(Font.font("Impact",15.0))
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
