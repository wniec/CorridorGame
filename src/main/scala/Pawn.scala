import scala.collection.mutable.ArrayBuffer
class Pawn(var row: Int,var col: Int,var color:Color) {

  def position: (Int, Int) = (row,col)
  def move(row:Int,col:Int):Unit ={
    this.row = row
    this.col = col
  }
}
