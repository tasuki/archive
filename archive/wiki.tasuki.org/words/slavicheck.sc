// Slavíček, slávy check?
//
// Tested working with: Ammonite Repl 2.5.5, Scala 2.13
// From repo root, run: `amm words/slavicheck.sc`

import scala.io.Source

case class Line(num: Int, content: String)

def normalize(s: String): List[Char] =
  s.toList.filter(c => c == '-' || c == '.')

def verseRuleMatch(rule: List[Char], verse: List[Char]): Boolean = {
  (rule, verse) match {
    case (Nil, Nil) => true
    case ('-' :: r, '-' :: v) => verseRuleMatch(r, v)
    case ('.' :: '.' :: r, '-' :: v) => verseRuleMatch(r, v)
    case ('.' :: '.' :: r, '.' :: '.' :: v) => verseRuleMatch(r, v)
    case _ => false
  }
}

def areDifferent(rule: String, verse: String): Boolean = {
  val matches = verseRuleMatch(normalize(rule), normalize(verse))
  !matches
}

def findBad(pair: (Line, Line)): List[Int] = {
  List(
    (pair._1, areDifferent("-.. -.. -.. -.. -.. --", pair._1.content)),
    (pair._2, areDifferent("-.. -.. -   -.. -.. -", pair._2.content))
  ).filter(_._2).map(_._1.num)
}


val lines: List[String] = Source.fromFile("words/slavy-dcera.md").getLines.toList

val casomira = lines.zipWithIndex
.map(l => Line(l._2, l._1))
.filter(_.content.startsWith("-")).grouped(2).map {
  case List(a, b) => (a, b)
  case _ => throw new Exception(
    "I just want the compiler warning to go away. " +
    "This will never happen. Ha ha."
  )
}

val bads: List[Int] = casomira.flatMap(findBad).toList

if (bads.length > 0) {
  val lineArray: Array[String] = lines.toArray
  println("Lines with inconsistencies:")
  bads.foreach { bad =>
    println("")
    println(lineArray(bad - 1))
    println(lineArray(bad))
  }
} else {
  println("Everything looks fine!")
}
