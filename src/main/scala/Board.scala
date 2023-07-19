import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Board(size:Int) {
  private val whole_size = size*2-1
  var black: Pawn = new Pawn(0,whole_size/2,Color.Black)
  var white: Pawn = new Pawn(whole_size-1,whole_size/2,Color.White)
  private val pawns=Seq(black,white)
  var walls: mutable.HashSet[(Int,Int)] = new mutable.HashSet[(Int, Int)]()
  def put_wall(begin: (Int,Int),end: (Int,Int)):Unit={
    if((begin._1 == end._1)==(begin._2 == end._2)){
      throw new IllegalArgumentException
      //Sprawdzam,czy dokładnie jedna współrzędna się różni
    }
    if(begin._1==end._1){
      val diff = end._2-begin._2
      val range = (0 to diff).map((i: Int) => (begin._1,begin._2+i))
      walls.addAll(range)
    }
    else{
      val diff = end._1 - begin._1
      val range = (0 to diff).map((i: Int) => (begin._1+i, begin._2))
      walls.addAll(range)
    }
  }
  def can_put_wall(pos:(Int,Int),direction: Direction): Boolean = {
    val vec = direction.to_vec
    val vx = vec._1*2
    val vy = vec._2*2
    val x = pos._1
    val y = pos._2
    if(pos._1%2==0 || pos._2%2==0){
      return false
    }
    if(walls.contains((x,y))||walls.contains((x+vx,y+vy))||walls.contains(x+2*vx,y+2*vy))
        return false
    true
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
  def can_go_to_end(position: (Int, Int),from: (Int, Int), color: Color): Boolean = {
    if((color==Color.Black && position._1==whole_size-1) || (color==Color.White && position._1==0)){
      return true
    }
    val moves = available(position,color).filter((x:(Int,Int)) =>x!=from)
    moves.map((m:(Int,Int))=>can_go_to_end(m,position,color)).reduceLeft((x:Boolean,y:Boolean) => x||y)
  }
  def show(): Unit = {
    //os.system('cls')
    print("\u001b[2J")
    print("  ")
    for (col <- 0 until whole_size) {
      print(col + " ")
    }
    println()
    for (row <- 0 until whole_size) {
      print(row + " ")
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
