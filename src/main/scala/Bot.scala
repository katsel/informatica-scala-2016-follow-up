// Tutorial Bot Class

class ControlFunction(var movingDirection: (Int, Int)) {
  // The only door to the EXTERNAL world:
  //
  // Callback function, which is always called, when anything in the world around changes.
  def respond(input: String) = {
    val (opCode: String, paramMap: Map[String, String]) = parse(input)

    if (opCode=="React") /* e.g. React(generation=0,time=0,view=__W_W_W__,energy=100) */ {
      val energy = paramMap("energy")
      val view = paramMap("view")
      val (x,y) = findPath(view)
      s"Move(direction=$x:$y)|Status(text=$energy)"
    } else ""
  }

  private def findPath(view: String) = {
    val pathFinder = new PathFinder(new MyView(view))
    val nextMovement = pathFinder.findPath(movingDirection)
    this.movingDirection = nextMovement
    nextMovement
  }

  private def parse(input: String): (String, Map[String, String]) = {
    val tokens: Array[String] = input.split('(')
    val opCode: String = tokens(0)

    val params: Array[String] = tokens(1).dropRight(1).split(',')
    val paramMap: Map[String, String] =
      params
        .map(param => param.split('='))
        .map(kv => (kv(0), kv(1))).toMap

    (opCode, paramMap)
  }
}

// abstract class: field (empty, wall, entity)
abstract class Field
case class BotSelf(x: Int, y: Int) extends Field
case class Empty(x: Int, y: Int) extends Field
case class Wall(x: Int, y: Int) extends Field
case class Entity(kind: Kind, x: Int, y: Int) extends Field
case class Unknown(x: Int, y: Int) extends Field

abstract class Kind
// good things
case class Zugar() extends Kind
case class Fluppet() extends Kind
// evil things
case class Toxifera() extends Kind
case class Snorg() extends Kind


// NEW
// EXERCISE: Implement findPath, so that the Bot does not crash into a wall
class PathFinder (view: MyView) {
  def changeDirection(movingDirection: (Int, Int)): (Int, Int) = {
    val (x, y) = movingDirection
    return (-x, -y)
  }

  def findPath(movingDirection: (Int, Int)) = {

    var target = movingDirection  // default moving direction

    // what kinds of things are in the view?
    val viewContents: Seq[Field] = for {
      x <- -1 to 1
      y <- -1 to 1
      e = view.at(x,y) match {
        case '_' => Empty(x,y)
        case 'M' => BotSelf(x,y)
        case 'W' => Wall(x,y)
        case 'P' => Entity(Zugar(), x, y)
        case 'B' => Entity(Fluppet(), x, y)
        case 'p' => Entity(Toxifera(), x, y)
        case 'b' => Entity(Snorg(), x, y)
        case _ => Unknown(x,y)
      }
    } yield e

    // find out what's bad
    val blocked = viewContents.filter(f => {
      f match {
        case BotSelf(_,_) => true
        case Wall(_,_) => true
        case Entity(Toxifera(), _, _) => true
        case Entity(Snorg(), _, _) => true
        case _ => false
      }
    })

    // find out what's edible/good
    val edible = viewContents.filter(f => {
      f match {
        case Entity(Zugar(), _, _) => true
        case Entity(Fluppet(), _, _) => true
        case _ => false
      }
    })

    // move towards edible things
    if (edible.nonEmpty) {
      val targetField = edible.head
      target = targetField match {
        case Entity(Zugar(), x, y) => (x, y)
        case Entity(Fluppet(), x, y) => (x, y)
      }
    }

    // is moving direction blocked?
    val blockInDirection = blocked.find(f => {
      f match {
        case Wall(x,y) => (x,y) == target
        case Entity(Toxifera(),x,y) => (x,y) == target
        case Entity(Snorg(),x,y) => (x,y) == target
        case _ => false
      }
    })
    blockInDirection match {
      case None => {}
      case Some(f) => {
        f match {
          case Wall(x,y) => target = changeDirection((x, y))
          case Entity(Toxifera(),x,y) => target = changeDirection((x, y))
          case Entity(Snorg(),x,y) => target = changeDirection((x, y))
        }
      }
    }

    println (s"FREE CELL: ${target}, CHAR = ${view.at(target._1,target._2)}")
    target
  }

}

// NEW
// EXERCISE: Implement toIndex, so that MyViewTest08 is green
class MyView (val view: String){
  val size = scala.math.sqrt(view.length).round.toInt
  val n = size/2

  require(size*size==view.length, s"length of view is not quadratic: ${view.length} != $size*$size")


  def at(x: Int, y:Int) = view.charAt(toIndex(x,y))

  private def toIndex(x: Int, y: Int): Int = (n+y)*size + (n+x)

  override def toString: String = {
    def toLines(rest:String): String =
      if (rest.isEmpty) ""
      else {
        val (line, remainingLines) = rest.splitAt(size)
        line + "\n" +toLines(remainingLines)
      }
    toLines(view)
  }
}

// ----------------------------------------------------------------------------------
// INTERNALS (you don't need to touch this during the workshop!)
//
// Entry Point for the Server

class ControlFunctionFactory {
  def create = new ControlFunction((1, -1)).respond _
}

