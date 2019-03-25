package org.xg.chart

import java.time.ZonedDateTime
import java.util.Date

import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.chart.{CategoryAxis, NumberAxis, StackedBarChart, XYChart}
import org.xg.dbModels.MOrder
import org.xg.gnl.DataUtils
import org.xg.uiModels.CustomerOrder

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

  def createChart(orders:Array[CustomerOrder]): StackedBarChart[String, Number] = {
    val yAxis = new NumberAxis
    val sbc = new StackedBarChart[String, Number](new CategoryAxis, new NumberAxis)
    sbc.setData(
      barChartFromOrder(orders)
    )
    sbc.setTitle("paid/unpaid")
    sbc
  }
}
