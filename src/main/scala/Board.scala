import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Board(size:Int) {
  private val whole_size = size*2-1
  var black: Pawn = new Pawn(0,whole_size/2,Color.Black)
  var white: Pawn = new Pawn(whole_size-1,whole_size/2,Color.White)
  private var round_color = Color.White
  private val pawns=Seq(black,white)
  var walls: mutable.HashSet[(Int,Int)] = new mutable.HashSet[(Int, Int)]()

  def put_wall(pos: (Int, Int), direction: Direction): Unit = walls.addAll(wall_path(pos, direction))

  def wall_path(pos:(Int,Int),direction: Direction):ArrayBuffer[(Int,Int)]={
    val vec = direction.to_vec
    val vx = vec._1
    val vy = vec._2
    val x = pos._1
    val y = pos._2
    ArrayBuffer((x,y),(x+vx,y+vy),(x+2*vx,y+2*vy))
  }
  def can_put_wall(pos:(Int,Int),direction: Direction): Boolean = {
    if ((pos._1 % 2 == 0) == (pos._2 % 2 == 0)) {
      return false
    }
    if((pos._1%2==0 && direction.horizontal) || (pos._2%2==0 && !direction.horizontal)){
      return false
    }
    wall_path(pos, direction).map((x:(Int,Int)) => walls.contains(x)).reduceLeft((x: Boolean,y: Boolean) => x||y)
  }


  def available(position: (Int,Int),color: Color): ArrayBuffer[(Int, Int)] = {
    val result = new ArrayBuffer[(Int, Int)]()
    for (move <- Direction.values) {
      val vector: (Int, Int) = move.to_vec
      val new_row = position._1 + vector._1 * 2
      val new_col = position._2 + vector._2 * 2
      if (new_row > -1 && new_row < whole_size && new_col > -1 && new_col < whole_size) {
        val mid_row = position._1 + vector._1
        val mid_col = position._2 + vector._2
        if(!walls.contains((mid_row,mid_col))&&pawns(1-color.value).position!=(new_row,new_col))
          result.addOne((new_row, new_col))
      }
    }
    result
  }

  def available(position: (Int, Int), color: Color, added: mutable.ArrayBuffer[(Int,Int)]): ArrayBuffer[(Int, Int)] = {
    val result = new ArrayBuffer[(Int, Int)]()
    for (move <- Direction.values) {
      val vector: (Int, Int) = move.to_vec
      val new_row = position._1 + vector._1 * 2
      val new_col = position._2 + vector._2 * 2
      if (new_row > -1 && new_row < whole_size && new_col > -1 && new_col < whole_size) {
        val mid_row = position._1 + vector._1
        val mid_col = position._2 + vector._2
        if (!walls.contains((mid_row, mid_col)) && !added.contains((mid_row, mid_col)) && pawns(1 - color.value).position != (new_row, new_col))
          result.addOne((new_row, new_col))
      }
    }
    result
  }
  def can_go_to_end(position: (Int, Int), color: Color, visited: mutable.HashSet[(Int,Int)]): Boolean = {
    if((color==Color.Black && position._1==whole_size-1) || (color==Color.White && position._1==0)){
      return true
    }
    visited.addOne(position)
    val moves = available(position,color).filter((x:(Int,Int)) => !visited.contains(x))
    if(moves.isEmpty)
      return false
    moves.map((move:(Int,Int))=>can_go_to_end(move,color,visited)).reduceLeft((x:Boolean,y:Boolean) => x||y)
  }

  def can_go_to_end(position: (Int, Int), color: Color, visited: mutable.HashSet[(Int, Int)], added: mutable.ArrayBuffer[(Int, Int)]): Boolean = {
    if ((color == Color.Black && position._1 == whole_size - 1) || (color == Color.White && position._1 == 0)) {
      return true
    }
    visited.addOne(position)
    val moves = available(position, color, added).filter((x: (Int, Int)) => !visited.contains(x))
    if (moves.isEmpty)
      return false
    moves.map((move: (Int, Int)) => can_go_to_end(move, color, visited, added)).reduceLeft((x: Boolean, y: Boolean) => x || y)
  }

  def can_go_to_end(color: Color): Boolean = {
    val pawn = pawns(color.value)
    val visited = new mutable.HashSet[(Int,Int)]()
    can_go_to_end(pawn.position,color,visited)
  }

  def can_go_to_end_if_added(pos:(Int,Int),direction: Direction): Boolean = {
    val w_path = wall_path(pos,direction)
    var visited = new mutable.HashSet[(Int, Int)]()
    if(!can_go_to_end(white.position, Color.White, visited,w_path)){
      return false
    }
    visited = new mutable.HashSet[(Int, Int)]()
    can_go_to_end(black.position, Color.Black, visited,w_path)
  }
  def show(): Unit = {
    //os.system('cls')
    print("\u001b[2J")
    print("  ")
    for (col <- 0 until whole_size) {
      print(Integer.toString(col,16) + " ")
    }
    println()
    for (row <- 0 until whole_size) {
      print(Integer.toString(row,16) + " ")
      for (col <- 0 until whole_size) {
        if((row,col)==black.position)
          print(Console.RED + "X " + Console.WHITE) 
        else if((row,col)==white.position){
          print("X ")
          }
        else if (walls.contains(row, col)) {
          print("■ ")
        }
        else
          print("  ")
      }
      println()
    }
    print("\n")
  }
}
