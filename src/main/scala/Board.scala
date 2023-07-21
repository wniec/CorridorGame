import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.compiletime.ops.boolean.&&

class Board(size:Int) {
  private val whole_size = size*2-1
  private val black: Player = new Player(0, whole_size / 2, Color.Black)
  private val white: Player = new Player(whole_size - 1, whole_size / 2, Color.White)
  private val blue: Player = new Player(whole_size / 2, 0, Color.Blue)
  private val red: Player = new Player(whole_size / 2, whole_size - 1, Color.Red)
  var round_color: Color = Color.Black
  val players: Seq[Player] =Seq(black,white,red,blue)
  var walls: mutable.HashSet[(Int,Int)] = new mutable.HashSet[(Int, Int)]()

  def change_round(): Unit = {
    round_color = Color.fromOrdinal((round_color.value + 1) % 4)
  }

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
    if(can_go_to_end_if_added(pos, direction)){
      return wall_path(pos, direction).map((x:(Int,Int)) => walls.contains(x)).reduceLeft((x: Boolean,y: Boolean) => x||y)
    }
    false
  }


  def available(position: (Int,Int)): ArrayBuffer[(Int, Int)] = {
    val result = new ArrayBuffer[(Int, Int)]()
    for (move <- Direction.values) {
      val vector: (Int, Int) = move.to_vec
      val new_row = position._1 + vector._1 * 2
      val new_col = position._2 + vector._2 * 2
      if (new_row > -1 && new_row < whole_size && new_col > -1 && new_col < whole_size) {
        val mid_row = position._1 + vector._1
        val mid_col = position._2 + vector._2
        val is_not_other_pawn = Color.values.map((x:Color) => players(x.value).position != (new_row,new_col)).reduceLeft((x:Boolean,y: Boolean) => x&&y)
        if(!walls.contains((mid_row,mid_col))&&is_not_other_pawn)
          result.addOne((new_row, new_col))
      }
    }
    result
  }

  def available(position: (Int, Int), added: mutable.ArrayBuffer[(Int,Int)]): ArrayBuffer[(Int, Int)] = {
    val result = new ArrayBuffer[(Int, Int)]()
    for (move <- Direction.values) {
      val vector: (Int, Int) = move.to_vec
      val new_row = position._1 + vector._1 * 2
      val new_col = position._2 + vector._2 * 2
      if (new_row > -1 && new_row < whole_size && new_col > -1 && new_col < whole_size) {
        val mid_row = position._1 + vector._1
        val mid_col = position._2 + vector._2
        val is_not_other_pawn = Color.values.map((x:Color) => players(x.value).position != (new_row,new_col)).reduceLeft((x:Boolean,y: Boolean) => x&&y)
        if (!walls.contains((mid_row, mid_col)) && !added.contains((mid_row, mid_col)) && is_not_other_pawn)
          result.addOne((new_row, new_col))
      }
    }
    result
  }
  def has_won(position: (Int, Int), color: Color): Boolean = {
    color match{
      case Color.Black => position._1 == whole_size - 1
      case Color.White => position._1 == 0
      case Color.Blue => position._2 == whole_size - 1
      case Color.Red => position._2 == 0
    }
  }
  def can_go_to_end(position: (Int, Int), color: Color, visited: mutable.HashSet[(Int,Int)]): Boolean = {
    if(has_won(position,color)){
      return true
    }
    visited.addOne(position)
    val moves = available(position).filter((x:(Int,Int)) => !visited.contains(x))
    if(moves.isEmpty)
      return false
    moves.map((move:(Int,Int))=>can_go_to_end(move,color,visited)).reduceLeft((x:Boolean,y:Boolean) => x||y)
  }

  def can_go_to_end_if_added(position: (Int, Int), color: Color, visited: mutable.HashSet[(Int, Int)], added: mutable.ArrayBuffer[(Int, Int)]): Boolean = {
    if (has_won(position,color)) {
      return true
    }
    visited.addOne(position)
    val moves = available(position, added).filter((x: (Int, Int)) => !visited.contains(x))
    if (moves.isEmpty)
      return false
    moves.map((move: (Int, Int)) => can_go_to_end_if_added(move, color, visited, added)).reduceLeft((x: Boolean, y: Boolean) => x || y)
  }

  def can_go_to_end(color: Color): Boolean = {
    val pawn = players(color.value)
    val visited = new mutable.HashSet[(Int,Int)]()
    can_go_to_end(pawn.position,color,visited)
  }

  private def can_go_to_end_if_added(pos:(Int,Int), direction: Direction): Boolean = {
    val w_path = wall_path(pos,direction)
    players.map((x:Player) => can_go_to_end_if_added(x.position,x.color,new mutable.HashSet[(Int, Int)](),w_path)).reduceLeft((y: Boolean, z: Boolean) => y&&z)
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
