// Append next week to a structured markdown
// Doesn't deal with year borders. Deal with it.
//
// Tested working with: Ammonite Repl 2.5.5, Scala 2.13
// From repo root, run something like (make sure the file exists):
// `amm fit/fitcal.sc --file fit/bodyweight-2020.md`

import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.time.temporal.WeekFields
import java.time.temporal.TemporalAdjusters.firstInMonth
import java.util.Locale
import scala.util.matching.Regex

val weekPattern: Regex = """^(\d+)\. week.*""".r
val yearPattern: Regex = """^## (\d+)""".r

val locale = Locale.UK
val now = LocalDate.now
val weekField = WeekFields.of(locale).weekOfYear()
val dowdomText = DateTimeFormatter.ofPattern("E dd")

def startOfFirstWeek(year: Int): LocalDate = {
  val firstMonday = LocalDate.of(year, 1, 1).`with`(firstInMonth(DayOfWeek.MONDAY))
  if (firstMonday.get(weekField) == 1) firstMonday
  else firstMonday.minusDays(7)
}

def appendWeek(year: Int, week: Int): String = {
  val monday = startOfFirstWeek(year).plusDays(7 * (week - 1))
  val month = monday.plusDays(3).getMonth.toString.toLowerCase.capitalize

  val days: Array[String] = Range(0, 7).map { d =>
    val day = monday.plusDays(d)
    val dowdom = dowdomText.format(day)
    s"- ${dowdom}:"
  }.toArray
  val lines: Array[String] = Array("", f"${week}. week, ${month}", "") ++ days ++ Array("")

  lines.mkString("\n")
}

def getYearWeek(lines: IndexedSeq[String]): (Int, Int) = {
  def findFirst(lines: IndexedSeq[String], pattern: Regex, default: Int): Int =
    lines.find(pattern.matches).map {
      case pattern(matched) => matched.toInt
    }.getOrElse(default)

  val weekMatch = findFirst(lines, weekPattern, now.get(weekField))
  val yearMatch = findFirst(lines, yearPattern, now.getYear)

  (yearMatch, weekMatch)
}

@main
def main(file: String) = {
  val path: os.Path = os.pwd/os.RelPath(file)
  val lines: IndexedSeq[String] = os.read.lines(path)
  val (year, week) = getYearWeek(lines.reverse)
  val toAdd = appendWeek(year, week + 1)
  os.write.append(path, toAdd)
  println("======== Adding the following:")
  println(toAdd)
  println("========")
}
