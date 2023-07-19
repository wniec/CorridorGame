enum Color:
  case White, Black
  def value:Int={
    this match {
      case White => 1
      case Black => 0
    }
    }