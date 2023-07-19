
/*import scala.collection.mutable

object TerminalApp {
  val board: Board = new Board(9)
  @main
  def main(): Unit = {
    board.show()
    board.put_wall((7,4),Direction.Right)
    board.show()
    var dir = Direction.Up
    println(board.can_put_wall((5,8),Direction.Up))
    println(board.walls)
    board.black.move(6,8)
    board.show()
    println(board.available(board.black.position,Color.Black))
    println(board.can_go_to_end(board.white.position,Color.White,new mutable.HashSet[(Int, Int)]()))
    board.put_wall((7,0),Direction.Right)
    board.put_wall((7,8),Direction.Right)
    board.put_wall((7,12),Direction.Right)
    board.show()
    println(board.can_go_to_end(board.white.position,Color.White,new mutable.HashSet[(Int, Int)]()))
    println(board.can_go_to_end_if_added((8,15),Direction.Down))
    board.put_wall((8,15),Direction.Down)
    board.show()
    println(board.can_go_to_end_if_added((11,14),Direction.Right))
    board.put_wall((11, 14), Direction.Right)
    board.show()
  }
}
*/