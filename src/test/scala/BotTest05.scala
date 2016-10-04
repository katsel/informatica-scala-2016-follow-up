import org.scalatest.FunSuite
import org.scalatest.Matchers._

// about ScalaTest, FunSuite see
// - http://www.scalatest.org/getting_started_with_fun_suite
// - http://www.scalatest.org/at_a_glance/FunSuite
class BotTest05 extends FunSuite {
  val bot = new ControlFunction((1,-1))

  test("[ON React] The bot should not step on its own shoes") {
    val serverInput: String = "React(generation=0,time=0,view=____M____,energy=100)"

    val reply: String = bot.respond(serverInput)
    reply.shouldNot(be("Move(direction=0:0)|Status(text=100)"))
  }

  test("[ON React] The bot should move up and have its current energy state as status-message, if there are no impediments.") {
    val serverInput: String = "React(generation=0,time=0,view=_________,energy=100)"

    val reply: String = bot.respond(serverInput)
    reply.should(be("Move(direction=1:-1)|Status(text=100)"))
  }

  test("""[ON Goodbye] The bot should reply with "", if the Operation Code is 'GoodBye' and not 'React'""") {
    val serverInput: String = "Goodbye(energy=int)"

    val reply: String = bot.respond(serverInput)
    reply should be("")
  }

  test("""[ON Welcome] The bot should reply with "", if the Operation Code is 'Welcome' and not 'React'""") {
    val serverInput: String = "Welcome(name=UserBot,apocalypse=50,round=60,maxslaves=0)"

    val reply: String = bot.respond(serverInput)
    reply shouldBe empty
  }

  test("""[ON React] The bot should move towards a Zugar when it sees them""") {
    val serverInput: String = "React(generation=0,time=0,view=________P,energy=100)"

    val reply: String = bot.respond(serverInput)
    reply.should(be("Move(direction=1:1)|Status(text=100)"))
  }

  test("""[ON React] The bot should move towards a Fluppet when it sees them""") {
    val serverInput: String = "React(generation=0,time=0,view=_______B_,energy=100)"

    val reply: String = bot.respond(serverInput)
    reply.should(be("Move(direction=0:1)|Status(text=100)"))
  }

  test("""[ON React] The bot should avoid walls when it sees them""") {
    // walls everywhere but on the left and right
    val serverInput: String = "React(generation=0,time=0,view=WWW_M_WWW,energy=100)"

    val reply: String = bot.respond(serverInput)
    // bot should move to the left or to the right
    reply.should(be("Move(direction=-1:0)|Status(text=100)") or be("Move(direction=1:0)|Status(text=100)"))
  }

  test("""[ON React] The bot should avoid Toxiferae when it sees them""") {
    // Toxiferae everywhere but on top
    val serverInput: String = "React(generation=0,time=0,view=p_ppMpppp,energy=100)"

    val reply: String = bot.respond(serverInput)
    // bot should move to the top
    reply.should(be("Move(direction=0:-1)|Status(text=100)"))
  }

  test("""[ON React] The bot should avoid Snorgs when it sees them""") {
    // Snorgs everywhere but on the bottom left
    val serverInput: String = "React(generation=0,time=0,view=bbbbMb_bb,energy=100)"

    val reply: String = bot.respond(serverInput)
    // bot should move to the bottom left
    reply.should(be("Move(direction=-1:1)|Status(text=100)"))
  }

}
