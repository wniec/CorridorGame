object App {
  val board: Board = new Board(9)
  @main
  def main(): Unit = {
    board.show()
    board.white.move(2,3)
    board.put_wall((7,7),(7,11))
    board.show()
    var dir = Direction.Up
    println(board.can_put_wall((5,9),Direction.Up))
    println(board.walls)
    board.black.move(6,8)
    board.show()
    println(board.available(Color.Black))
  }
}
