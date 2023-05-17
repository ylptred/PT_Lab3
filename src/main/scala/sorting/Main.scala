package sorting

import au.com.bytecode.opencsv.CSVWriter

import scala.io.BufferedSource
import java.io._
import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._
import scala.util.Random
import au.com.bytecode.opencsv.CSVWriter
import search.SearchFuncs
import utils.Timer._
import scala.io.BufferedSource

object Main {

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
      var dataMap: Map[String, Int] = Map.empty[String, Int]
      var data_arr: Array[Data] = Array.empty[Data]
      val data_source: BufferedSource = io.Source.fromFile(s"src/main/resources/gendata/$selection.csv")
      for (line <- data_source.getLines.drop(1)) {
        val splittedLine: Array[String] = line.split(",")
        val DataObject: Data = new DataClass(splittedLine(0).drop(1).dropRight(1).toInt, splittedLine(1), splittedLine(2).drop(1).dropRight(1).toInt,
          splittedLine(3).drop(1).dropRight(1).toInt,
          splittedLine(4).drop(1).dropRight(1).toInt)
        data_arr = data_arr :+ DataObject
        dataMap += (DataObject.serviceName -> DataObject.price)
      }
      data_source.close

      val outputFile: BufferedWriter = new BufferedWriter(new FileWriter(s"src/main/resources/sorteddata/shaker_$selection.csv"))
      val csvWriter: CSVWriter = new CSVWriter(outputFile)


      println(s"----ENDING SELECTION $selection----")
    }
  }
}
