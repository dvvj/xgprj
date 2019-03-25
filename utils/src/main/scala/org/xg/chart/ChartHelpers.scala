package org.xg.chart

import java.time.ZonedDateTime
import java.util.Date

import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.chart.{CategoryAxis, NumberAxis, StackedBarChart, XYChart}
import org.xg.dbModels.MOrder
import org.xg.gnl.DataUtils
import org.xg.uiModels.CustomerOrder

import scala.reflect.ClassTag

object ChartHelpers {

  import java.lang.{Double => JavaDouble}

  private def zdtFrom(year:Int, month:Int):ZonedDateTime = ZonedDateTime.of(
    year, month,
    1, 0, 0, 0, 0,
    DataUtils.UTC
  )

  private def checkIfNeedPadding(prevYearMonth:Option[(Int, Int)], currYearMonth:(Int, Int)):(Boolean, (Int, Int)) = {
    if (prevYearMonth.isEmpty) (false, currYearMonth)
    else {
      val (prevYear, prevMonth) = prevYearMonth.get
      val prevDt = zdtFrom(prevYear, prevMonth)
      //val dt = zdtFrom(yearMonth._1, yearMonth._2)
      val prevPlus1 = prevDt.plusMonths(1)
      val (year, month) = currYearMonth
      //curr = Option(prevPlus1.getYear -> prevPlus1.getMonthValue)
      //println(s"curr: $curr")
      if (year > prevPlus1.getYear || (year == prevPlus1.getYear && month > prevPlus1.getMonthValue))
        true -> (prevPlus1.getYear, prevPlus1.getMonthValue)
      else if (year != prevPlus1.getYear || month != prevPlus1.getMonthValue)
        throw new RuntimeException("not supposed to happend")
      else
        false -> currYearMonth
    }
  }

  def barChartFromOrder(orders:Array[CustomerOrder]): ObservableList[XYChart.Series[String, Number]] = {
    val groupedByYearMonth = orders.map { o =>
      val zdt = o.getOrder.getCreationTime
      val yearMonth = zdt.getYear -> zdt.getMonthValue
      yearMonth -> o
    }.groupBy(_._1)
      .mapValues { p =>
        val cos = p.map(_._2)
        val paidOrders = cos.filter(!_.getOrder.getNotPayed)
        val unpaidOrders = cos.filter(_.getOrder.getNotPayed)
        val rewardFromPaid = paidOrders.map(_.getReward.toDouble).sum
        val rewardFromUnpaid = unpaidOrders.map(_.getReward.toDouble).sum
        rewardFromPaid -> rewardFromUnpaid
      }.toArray
      .sortBy(_._1)

    val paid = new XYChart.Series[String, Number]
    val unpaid = new XYChart.Series[String, Number]

    val paddingStart =
      if (groupedByYearMonth.nonEmpty) groupedByYearMonth.last._1
      else 0 -> 0

    var prev:Option[(Int, Int)] = None
    groupedByYearMonth.foreach { p =>
      val (yearMonth, paidUnpaid) = p
      val (year, month) = yearMonth
      var (needPadding, prevPlus1) = checkIfNeedPadding(prev, yearMonth)
      while (needPadding) {
        val yearMonthStr = s"${prevPlus1._1}-${prevPlus1._2}"
        paid.getData.add(new XYChart.Data(yearMonthStr, 0.0))
        unpaid.getData.add(new XYChart.Data(yearMonthStr, 0.0))
        prev = Option(prevPlus1)
        println(prev)
        val t = checkIfNeedPadding(prev, yearMonth)
        needPadding = t._1
        prevPlus1 = t._2
      }
      val yearMonthStr = s"${yearMonth._1}-${yearMonth._2}"
      paid.getData.add(new XYChart.Data(yearMonthStr, paidUnpaid._1))
      unpaid.getData.add(new XYChart.Data(yearMonthStr, paidUnpaid._2))
      prev = Option(yearMonth)

    }

    val data: ObservableList[XYChart.Series[String, Number]] = FXCollections.observableArrayList()
    data.addAll(paid, unpaid)
    data
  }


  def createChart(data:ObservableList[XYChart.Series[String, Number]]): StackedBarChart[String, Number] = {
    val yAxis = new NumberAxis
    val sbc = new StackedBarChart[String, Number](new CategoryAxis, new NumberAxis)
    sbc.setData(data)
    sbc.setTitle("paid/unpaid")
    sbc
  }

  import java.lang.{Double => JavaDouble}

  trait TBarChartDataByYearMonth[T] {
    val rawData:Array[T]
    val getYearMonth:T => (Int, Int)
    def yearMonth2Str(yearMonth:(Int, Int)): String = {
      s"${yearMonth._1}-${yearMonth._2}"
    }
    type CategoryFilter = T => Boolean
    type ResultGetter = T => Double
    val resultGetter:ResultGetter
    def groupByCategory(categorizers:Map[String, CategoryFilter], data:IndexedSeq[T]):Map[String, Double] = {
      categorizers.map(p => p._1 -> data.filter(p._2).map(resultGetter).sum)
    }

    private def step(curr:(Int, Int)):(Int, Int) = {
      if (curr._2 < 12) (curr._1, curr._2+1)
      else (curr._1+1, 1)
    }

    val categorizers:List[(String, CategoryFilter)]

    def groupByCategoryAndYearMonth(
                                   forceStart:Option[(Int, Int)]
                                   ):ObservableList[XYChart.Series[String, Number]] = {
      val res: ObservableList[XYChart.Series[String, Number]] = FXCollections.observableArrayList()
      if (!rawData.isEmpty) {
        val categorizerMap = categorizers.toMap
        val catSeries = categorizerMap.keySet.map(k => k -> new XYChart.Series[String, Number]()).toMap

        val groupedByYearMonth = rawData.map {d =>
          getYearMonth(d) -> d
        }.groupBy(_._1).mapValues(_.map(_._2).toIndexedSeq)
        val start =
          if (forceStart.nonEmpty) forceStart.get
          else groupedByYearMonth.minBy(_._1)._1

        println(s"start: $start")

        val end = groupedByYearMonth.maxBy(_._1)._1
        println(s"end: $end")

        var curr = start
        while (curr != end) {
          val yearMonthStr = yearMonth2Str(curr)
          if (groupedByYearMonth.contains(curr)) {
            val currData = groupByCategory(categorizerMap, groupedByYearMonth(curr))
            println(s"adding curr: $curr, $currData")
            currData.foreach { cd =>
              val ds = catSeries(cd._1).getData
              val success = ds.add(new XYChart.Data[String, Number](yearMonthStr, new JavaDouble(cd._2)))
              println(success)
            }
          }
          else {
            println(s"adding padding: $curr")
            catSeries.foreach { cd =>
              cd._2.getData.add(new XYChart.Data[String, Number](yearMonthStr, new JavaDouble(0.0)))
            }
          }
          curr = step(curr)
        }

        // adding the last
        val yearMonthStr = yearMonth2Str(curr)
        val currData = groupByCategory(categorizerMap, groupedByYearMonth(curr))
        currData.foreach { cd =>
          catSeries(cd._1).getData.add(new XYChart.Data(yearMonthStr, cd._2))
        }

        categorizers.foreach { c =>
          res.add(catSeries(c._1))
        }

      }
      res
    }

    def groupByCategoryAndYearMonthJ:ObservableList[XYChart.Series[String, Number]] = groupByCategoryAndYearMonth(None)
  }

  private def customerOrderBarChartData(customerOrders:Array[CustomerOrder]):TBarChartDataByYearMonth[CustomerOrder] = new TBarChartDataByYearMonth[CustomerOrder] {
    override val getYearMonth: CustomerOrder => (Int, Int) = co => {
      co.getOrder.getCreationTime.getYear -> co.getOrder.getCreationTime.getMonthValue
    }

    override val rawData: Array[CustomerOrder] = customerOrders

    override val resultGetter: ResultGetter = co => co.getReward
    override val categorizers: List[(String, CategoryFilter)] = List(
      "paid" -> (co => !co.getOrder.getNotPayed),
      "unpaid" -> (co => co.getOrder.getNotPayed)
    )
  }

  def createChartFromCustomerOrders(customerOrders:Array[CustomerOrder]):StackedBarChart[String, Number] = {
    createChart(
      customerOrderBarChartData(customerOrders).groupByCategoryAndYearMonth(None)
    )
  }
}
