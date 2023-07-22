import javafx.event.Event
import javafx.scene.input.MouseEvent
import scalafx.Includes.when
import scalafx.scene.image.{Image, ImageView}
import scalafx.application.JFXApp3
import scalafx.scene.{Node, Scene}
import scalafx.scene.paint.Color.*
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{Background, BackgroundFill, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize, HBox, VBox}
import scalafx.scene.paint.LinearGradient
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage

import scala.collection.mutable.ArrayBuffer


object App extends JFXApp3 {
  private val board: Board = new Board(9)
  private var state: AppState = AppState.Default
  private var selected_wall: Option[(Int,Int)] = None
  private var areas_available: ArrayBuffer[(Int,Int)] = new ArrayBuffer[(Int, Int)]()
  private var round = 0

  private val tile_color = DarkBlue
  private val highlighted_tile_color = DarkGreen
  private val wall_color = Black
  private val available_wall_color = Orange
  private val selected_wall_color = Red
  private val no_wall_color = LightBlue
  private def square(xr: Int, yr: Int): HBox ={
    val positions = board.players.map((p: Player) => p.position)
    val highlighted_area: Boolean = ((xr,yr)==board.players(board.round_color.ordinal).position && state==AppState.Moving)||areas_available.contains((xr,yr))
    if(xr % 2 == 0 && yr % 2 == 0 && positions.contains((xr,yr))){
      val color = Color.values(positions.indexOf((xr, yr)))
      pawn_view(color,highlighted_area)
    }
    else{
      tile_view(xr,yr,highlighted_area)
    }
  }
  private def tile_mouse_event(node: Node, new_state: AppState, position:(Int,Int)): Unit ={
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
        board.players(board.round_color.ordinal).move(position._1, position._2)
        board.change_round()
        update()
      }
      case (AppState.Moving,AppState.Default, false) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        update()
      }
      case (AppState.PuttingWall, AppState.Default, _) => node.onMouseClicked = (_: MouseEvent) => {
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

  private def wall_mouse_event(node: Node, new_state: AppState, position: (Int, Int), can_put_wall: Boolean): Unit = {
    (state, new_state, can_put_wall) match {
      case (AppState.Moving, AppState.Default, _) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        update()
      }
      case (AppState.Default, AppState.PuttingWall, _) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        selected_wall = Some(position)
        update()
      }
      case (AppState.PuttingWall, AppState.Default, false) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        update()
      }
      case (AppState.PuttingWall, AppState.Default, true) => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available.clear()
        board.put_wall(selected_wall.get, board.coords_to_dir(selected_wall.get, position))
        selected_wall = None
        board.players(board.round_color.ordinal).walls_available -= 1
        board.change_round()
        update()
      }
      case _ => node.onMouseClicked = (_: MouseEvent) => {
        state = new_state
        areas_available = board.available(position)
        update()
      }
    }
  }
  private def pawn_view(color:Color,highlighted: Boolean) = new HBox{
    val img: Image = color.get_image()
    val view = new ImageView(img)
    val position: (Int, Int) = board.players(color.ordinal).position
    view.fitWidth = 60
    view.fitHeight = 60
    if(highlighted)
      background = Background.fill(highlighted_tile_color)
    else
      background = Background.fill(tile_color)
    if(state == AppState.Moving){
        tile_mouse_event(view,AppState.Default,position)
    }
    else if(state == AppState.Default){
      if(color== board.round_color)
        tile_mouse_event(view,AppState.Moving,position)
    }
    else if (state == AppState.PuttingWall) {
      if (color == board.round_color)
        tile_mouse_event(view, AppState.Default, position)
    }
    children = Seq(view)
  }

  private def tile_view(xr:Int, yr: Int,highlighted: Boolean) = new HBox{
    val s: (Int, Int) = rec_size(xr % 2 == 0, yr % 2 == 0)
    val rec: Rectangle =new Rectangle {
      width = s._1
      height = s._2
    }
      if(s==(60,60)){
        tile_mouse_event(rec,AppState.Default,(xr,yr))
        if (highlighted)
          rec.fill = highlighted_tile_color
        else
          rec.fill = tile_color
      }
      else{
        wall_view(xr,yr,rec)
      }
    children = Seq(rec)
  }

  private def wall_view(xr: Int, yr: Int, rec:Rectangle): Unit={
      if (board.walls.contains((xr, yr)))
        rec.fill = wall_color
      else if(state==AppState.PuttingWall){
        if ((xr, yr) == selected_wall.get) {
          rec.fill = selected_wall_color
          wall_mouse_event(rec, AppState.Default, (xr, yr),false)
        }
        else if (board.can_put_wall(selected_wall.get, (xr, yr))) {
          rec.fill = available_wall_color
          wall_mouse_event(rec, AppState.Default, (xr, yr),true)
        }
        else
          rec.fill = no_wall_color
      }
      else{
        rec.fill = no_wall_color
        if(state==AppState.Moving)
          wall_mouse_event(rec, AppState.Default, (xr, yr),false)
        else if (state == AppState.Default && board.players(board.round_color.ordinal).walls_available > 0)
          wall_mouse_event(rec, AppState.PuttingWall, (xr, yr),false)
      }
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
    alignment = Pos.Center
  }

  private def vbox(xr: Int): VBox = new VBox {
    children = (0 to 16).map((yr: Int) => square(xr,yr))
  }
  def side_rec(color: Color): Rectangle = {
    var size:(Int,Int)=(0,0)
    color match {
      case Color.Black | Color.White => size = (620, 40)
      case Color.Blue | Color.Red => size = (40, 620)
    }
    new Rectangle{
      fill = color.get_paint()._1
      height = size._1
      width = size._2
    }
  }
  private def make_board()= {
    val hbox = new HBox{
      children = List(side_rec(Color.White)).++((0 to 16).map((x: Int) => vbox(x))).++(List(side_rec(Color.Black)))
      alignment = Pos.Center
    }
    new VBox{
      children = side_rec(Color.Red) :: hbox :: side_rec(Color.Blue) :: Nil
      alignment = Pos.Center
      padding = Insets(10,10,10,10)
    }

  }

  private def update(): Unit = {
    stage.getScene.setRoot(
    new VBox {
      background = Background.fill(Purple)
      alignment = Pos.Center
      children = Seq(make_board(), players_info())
    })
  }
  private def check(): Boolean = {
    round +=1
    if (round>=9){
      return true
    }
    false
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "The Corridor Game"
      fullScreen = true
      scene = new Scene {
        resizable = true
        minWidth = 1280
        minHeight = 960
        root = new VBox{
          background = Background.fill(Purple)
          alignment = Pos.Center
          children = Seq(make_board(),players_info())
        }
      }
    }
  }
}