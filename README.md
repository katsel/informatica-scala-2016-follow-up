scala-Workshop: Follow Up
=========================

## 1. Starting Point

The bot implementation of the
[Scala Basic Workshop](https://github.com/plipp/informatica-scala-2016)
had some drawbacks:

- it only knew about 'W' (Walls) and was not aware of other entities (such as
  good and bad plants, good and bad beasts)
- it got trapped easily by certain wall constellations and could not escape

## 2. Follow up Exercise

### 2.1 Energy Saving Bot

Our aim is to implement an energy saving bot.
It should become aware of the other entities in its surrounding and try to eat
good things to gain energy, while avoiding bad things that draw energy.

The different kinds of plants and beasts, aka *entities*, are documented in the
Scalatron [Game Rules](docs/scalatron/Scalatron%20Game%20Rules.md)
and [Protocol](docs/scalatron/Scalatron%20Protocol.md).

The implementation uses abstract classes and
[case classes]((http://docs.scala-lang.org/tutorials/tour/case-classes)) to
categorize the kinds of fields a bot can encounter in its surrounding.
Combined with matchers, these provide the opportunity to decide on a moving
strategy.

### 2.2 Optional Tasks/Ideas

Optional tasks were:

* to find out how to escape from room-like wall-fragments ✅
* use Mini-Bots ❎
* try out strategies ❎ (requires Mini-Bots)
* change Scalatron server parameters ❎

## 3. Outcome

* [Unit tests](src/test/scala/) for the implementation ✅
* [Bot code](src/main/scala/) ✅
* A short README.md (this file) ✅

## 4. How to run the bot (see also: [bot development README](docs/bot-development/readme.md))

Given that you've already setup your JVM, Scala and sbt installation, these are
the commands required to run the bot.

In the project's root directory, run `sbt test` to run all tests.
Then run `sbt deploy` to compile the bot code.

The Scalatron game server can be started with

```
./server/bin/startScalatronServer.sh
```

Enjoy!

## 5. Explanation of the strategy

* The bot keeps a global state with its moving direction.
  Its moving direction can be changed in order to react properly to entities
  on the game board.
* When finding a Zugar or Fluppet in its surrounding, the bot changes its moving
  direction to collect it.
* The bot tries to avoid walls, Toxiferae and Snorgs. When finding one (or more)
  of these in its current moving direction, it randomly chooses a free cell to
  move into, resulting in a random change in moving direction.
* Because this moving direction change is done at random, the bot can usually
  escape room-like wall fragments in just a few steps.

# References

- [Scalatron Game Rules](docs/scalatron/Scalatron%20Game%20Rules.md)
- [Scalatron Game Protocol](docs/scalatron/Scalatron%20Protocol.md)
- [Scalatron Server Setup](https://github.com/plipp/scalatron/blob/master/Scalatron/doc/markdown/Scalatron%20Server%20Setup.md#botwar-game-options)
