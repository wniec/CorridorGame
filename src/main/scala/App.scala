import javafx.event.Event
import javafx.scene.input.MouseEvent
import scalafx.Includes.when
import scalafx.scene.image.{Image, ImageView}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.shape.Rectangle
/*This is a skeleton for creating future GUI
*/
object App extends JFXApp3 {
  //private val w_width = 1920.0: Double
  //private val w_height = 1080.0: Double
  private var board: Board = new Board(9)
  //private var bot: Bot = new Bot(grid)
  //private var hasBot: Boolean = false
  private var side = 1
  private var round = 0
  def change_side(): Unit = {
    side = 3 - side
  }

  def get_image(color: Color): Image = {
    color.value match {
      case 0 => new Image("file:src/main/resources/red.png")
      case 1 => new Image("file:src/main/resources/blue.png")
    }
  }

  private def square(xr: Int, yr: Int): HBox = new HBox {
    padding = Insets(0, 0, 0, 0)
    val img: Image = get_image(Color.White)
    val view = new ImageView(img)
    val rec = new Rectangle {
      x = 25
      y = 40
      if(xr%2==0)
        width = (60).toInt
      else
        width = 10
      if (yr % 2 == 0)
        height = 60
      else
        height = 10
      if(xr%2==1 || yr%2==1)
        fill = LightBlue
      else
        fill = DarkBlue
    }
    //view.setFitWidth((w_width - 80) / 17)
    //view.setFitHeight((w_height - 100) / 17)
      /*view.onMouseClicked = (_: MouseEvent) => {
        update()
      }*/
    children = Seq(
      rec
    )

  }

  private def wall= new Rectangle {
      x = 25
      y = 40
      width = 130
      height = 10
      fill = Black
  }

  private def vbox(xr: Int): VBox = new VBox {
    children = (0 to 16).map((yr: Int) => square(xr,yr))
  }

  private def make_VBox_seq(): Seq[VBox] = {
    (0 to 16).map((x: Int) => vbox(x))
  }

  private def make_Scene(): Scene = new Scene {
    fill = LightGrey
    content = new HBox {
      children = make_VBox_seq()
    }
  }
  def check(): Boolean = {
    round +=1
    if (round>=9){
      return true
    }
    false
  }

  def update(): Unit ={
      stage.scene = make_Scene()
      change_side()
      check()
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Tic Tac Toe Game"
      scene = new Scene {
        resizable = true
        fill = LightGrey
        fullScreen = true
        root = new HBox {
          padding= Insets(10,10,10,10)
          fill = Red
          alignment = Pos.Center
          children = make_VBox_seq()
        }
      }
    }
  }
}