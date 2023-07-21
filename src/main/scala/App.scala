import javafx.event.Event
import javafx.scene.input.MouseEvent
import scalafx.Includes.when
import scalafx.scene.image.{Image, ImageView}
import scalafx.application.JFXApp3
import scalafx.scene.{Node, Scene}
import scalafx.scene.paint.Color.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{Background, HBox, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage

import scala.collection.mutable.ArrayBuffer


object App extends JFXApp3 {
  private val board: Board = new Board(9)
  private var state: AppState = AppState.Default
  private var wall_chosen: Option[(Int,Int)] = None
  private var areas_available: ArrayBuffer[(Int,Int)] = new ArrayBuffer[(Int, Int)]()
  private var round = 0

  private def square(xr: Int, yr: Int): HBox ={
    val positions = board.players.map((p: Player) => p.position)
    val highlighted_area: Boolean = ((xr,yr)==board.players(board.round_color.value).position && state==AppState.Moving)||areas_available.contains((xr,yr))
    if(xr % 2 == 0 && yr % 2 == 0 && positions.contains((xr,yr))){
      val color = Color.values(positions.indexOf((xr, yr)))
      pawn_view(color,highlighted_area)
    }
    else{
      field_view(xr,yr,highlighted_area)
    }
    //view.setFitWidth((w_width - 80) / 17)
    //view.setFitHeight((w_height - 100) / 17)
      /*view.onMouseClicked = (_: MouseEvent) => {
        update()
      }*/

  }
  private def add_mouse_event(node: Node, new_state: AppState, position:(Int,Int)): Unit ={
    val can_go_to = areas_available.contains(position)
    (state,new_state, can_go_to) match{
      case (AppState.Default,AppState.Moving, _) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available=board.available(position)
        update()
      }
      case (AppState.Moving, AppState.Default, true) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        board.players(board.round_color.value).move(position._1, position._2)
        println(board.players(board.round_color.value).position)
        board.change_round()
        update()
      }
      case (AppState.Moving,AppState.Default, false) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        update()
      }
      case _ => node.onMouseClicked =(_: MouseEvent) => {
        state = new_state
        areas_available = board.available(position)
        update()
      }
    }
  }
  private def pawn_view(color:Color,highlighted: Boolean) = new HBox{
    val img: Image = color.get_image()
    val view = new ImageView(img)
    val position: (Int, Int) = board.players(color.value).position
    view.fitWidth = 60
    view.fitHeight = 60
    if(highlighted)
      background = Background.fill(DarkGreen)
    else
      background = Background.fill(DarkBlue)
    if(state == AppState.Moving){
        add_mouse_event(view,AppState.Default,position)
    }
    else if(state == AppState.Default){
      if(color== board.round_color)
        add_mouse_event(view,AppState.Moving,position)
    }
    else if (state == AppState.PuttingWall) {
      if (color == board.round_color)
        add_mouse_event(view, AppState.Default, position)
    }
    children = Seq(view)
  }

  private def field_view(xr:Int, yr: Int,highlighted: Boolean) = new HBox{
    val s: (Int, Int) = rec_size(xr % 2 == 0, yr % 2 == 0)
    val rec: Rectangle =new Rectangle {
      width = s._1
      height = s._2
      if (highlighted)
        fill = DarkGreen
      else
        fill = DarkBlue
    }
      if(s==(60,60)){
        if(state == AppState.Moving) {
          add_mouse_event(rec,AppState.Default,(xr,yr))
        }
        else{
          add_mouse_event(rec,AppState.Default,(xr,yr))
        }
      }
      else{
        if (board.walls.contains((xr, yr))) {
          rec.fill = Black
        }
        else {
          rec.fill = LightBlue
        }
        if(s._1!=s._2){

        }
        else{

        }
      }
    children = Seq(rec)
  }
  private def rec_size(a:Boolean, b:Boolean)= {
    (a, b) match {
      case (true,true) =>(60,60)
      case (true,false) =>(60,10)
      case (false,false) =>(10,10)
      case (false,true) =>(10,60)
    }
  }



  private def players_info() = new HBox {
    minWidth = 60
    maxWidth = 620
    prefHeight = 154
    children = board.players.map((x:Player) => x.info_box(board.round_color))
    background = Background.fill(Purple)
    alignment = Pos.Center
  }

  private def vbox(xr: Int): VBox = new VBox {
    children = (0 to 16).map((yr: Int) => square(xr,yr))
  }

  private def make_HBox_seq()= new HBox{
    children = (0 to 16).map((x: Int) => vbox(x))
    alignment = Pos.Center
    padding = Insets(10,10,10,10)
  }

  private def update(): Unit = {
    stage.getScene.setRoot(
    new VBox {
      padding = Insets(10, 10, 10, 10)
      alignment = Pos.Center
      children = Seq(make_HBox_seq(), players_info())
    }
    )
  }
  private def check(): Boolean = {
    round +=1
    if (round>=9){
      return true
    }
    false
  }

  override def start(): Unit = {
    //board.put_wall((7,8),Direction.Right)
    //println(board.walls)
    //println(board.walls.contains((7,8)))
    stage = new JFXApp3.PrimaryStage {
      title.value = "The Corridor Game"
      fullScreen = true
      scene = new Scene {
        resizable = true
        minWidth = 1280
        minHeight = 960
        fill = LightGrey
        root = new VBox{
          padding= Insets(10,10,10,10)
          fill = Red
          alignment = Pos.Center
          children = Seq(make_HBox_seq(),players_info())
        }
      }
    }
  }
}