package rangegenerator

import collection.mutable.Seq
import scala.collection.mutable.Buffer
import protos.network.Range

class keyRangeGenerator(
    allSamples: scala.collection.Seq[String],
    numWorkers: Int
) {

  var temp = convertTomutable(allSamples)
  var sortedSamples: Seq[String] = temp.sortWith((s1, s2) => comparator(s1, s2))

  def generateKeyrange(): Seq[Range] = {
    val numPoints = numWorkers - 1
    val term = sortedSamples.length / numWorkers
    val points = for (i <- term to sortedSamples.length - 1 by term) yield i;
    var ranges: Buffer[Range] = Buffer()

    for (i <- 0 to points.length) {
      if (i == 0) {
        var el = Range("!!!!!!!!!!", sortedSamples(points.head))
        ranges = ranges :+ el
      } else if (i == points.length) {
        var el = Range(sortedSamples(points.last), "~~~~~~~~~~")
        ranges = ranges :+ el
      } else {
        var el = Range(sortedSamples(i), sortedSamples(i + 1))
        ranges = ranges :+ el
      }
    }
    ranges
  }
  def convertToimmutable[T](sq: Seq[T]): scala.collection.Seq[T] =
    scala.collection.Seq[T](sq: _*)
  def convertTomutable[T](sq: scala.collection.Seq[T]): Seq[T] = Seq[T](sq: _*)
  def comparator(s1: String, s2: String): Boolean = {
    s1.slice(0, 10) < s2.slice(0, 10)
  }
}