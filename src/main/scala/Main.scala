import scalafx.scene.image.Image

object Main {
  def main(args: Array[String]): Unit = {
    //val a = new Image("file:" + "src/main/resources/" + piece.colorName + piece.name + ".png")
    //val a = new Image("file:src/main/resources/red.png")
    val game:Game=new Game()
    game.play_with_player()
  }
}