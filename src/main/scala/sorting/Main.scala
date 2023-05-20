package sorting

import au.com.bytecode.opencsv.CSVWriter

import scala.io.BufferedSource
import java.io._
import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.util.Random
import au.com.bytecode.opencsv.CSVWriter
import hash.HashFuncs

import java.util.concurrent.TimeUnit
import scala.io.BufferedSource

object Main {

  var map_simple: Map[Long, Array[Data]] = Map.empty[Long, Array[Data]]
  var map_hard: Map[Long, Array[Data]] = Map.empty[Long, Array[Data]]

  /**
   * Главный метод программы, содержит вызовы всех вспомогательных функций и замер времени выполнения сортировок
   *
   * @param args
   * @return
   */
  def main(args: Array[String]): Unit = {


    var servicesListTMP: Array[String] = Array.empty[String]
    val services_source: BufferedSource = io.Source.fromFile("src/main/resources/origdata/services.csv")
    for (line <- services_source.getLines.drop(1)) {
      servicesListTMP = servicesListTMP :+ line
    }


    val selections: Array[String] = Array[String]("100", "500", "1000", "2500", "5000", "10000", "25000", "50000", "100000")

        utils.data.MakeCSV.generateData(selections)

    for (selection <- selections) {
      println(s"----STARTING SELECTION $selection----")
      map_simple = Map.empty[Long, Array[Data]]
      map_hard = Map.empty[Long, Array[Data]]
      var data_arr: Array[Data] = Array.empty[Data]
      val data_source: BufferedSource = io.Source.fromFile(s"src/main/resources/gendata/$selection.csv")
      for (line <- data_source.getLines.drop(1)) {
        val splittedLine: Array[String] = line.split(",")
        val DataObject: Data = new DataClass(splittedLine(0).drop(1).dropRight(1).toLong, splittedLine(1).drop(1).dropRight(1), splittedLine(2).drop(1).dropRight(1).toInt,
          splittedLine(3).drop(1).dropRight(1).toInt,
          splittedLine(4).drop(1).dropRight(1).toInt)
        data_arr = data_arr :+ DataObject
        HashFuncs.hashAdd(DataObject, "simple")
        HashFuncs.hashAdd(DataObject, "hard")
      }
      data_source.close

      println(s"---start simple $selection---")
      for (service <- servicesListTMP) {
        val t0 = System.nanoTime()
        HashFuncs.hashGet(service, "simple")
        val t1 = System.nanoTime()
        println(s"[simple]  Elapsed time: " + (t1 - t0) + "ns    " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS)
          + "ms    " + TimeUnit.SECONDS.convert(TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS), TimeUnit.MILLISECONDS) + "s")

      }
      println(s"---end simple $selection---")

      println(s"---start hard $selection---")
      for (service <- servicesListTMP) {
        val t0 = System.nanoTime()
        HashFuncs.hashGet(service, "hard")
        val t1 = System.nanoTime()
        println(s"[hard]  Elapsed time: " + (t1 - t0) + "ns    " + TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS)
          + "ms    " + TimeUnit.SECONDS.convert(TimeUnit.MILLISECONDS.convert((t1 - t0), TimeUnit.NANOSECONDS), TimeUnit.MILLISECONDS) + "s")

      }
      println(s"---end hard $selection---")

      println(s"simple: ${HashFuncs.collisions(map_simple)}")
      println(s"hard: ${HashFuncs.collisions(map_hard)}")

      println(s"----ENDING SELECTION $selection----")
    }
  }
}
