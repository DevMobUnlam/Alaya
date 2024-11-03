package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorSecondary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun SingleLineChartWithGridLines(pointsData: List<Point>, modifier: Modifier) {
    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .topPadding(105.dp)
        .axisLabelColor(ColorText)
        .axisLineColor(ColorText)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i].description }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .axisLabelColor(ColorText)
        .axisLineColor(ColorText)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }.build()
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(colorFilter = ColorFilter.tint(ColorPrimary)),
                    IntersectionPoint(color = ColorText, radius = 2.dp),
                    SelectionHighlightPoint(color = ColorText),
                    ShadowUnderLine(color = ColorSecondary),
                    SelectionHighlightPopUp(popUpLabel = { _, y -> "Cant de crisis : ${y.toInt()}" })
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            color = ColorQuaternary, colorFilter = ColorFilter.tint(
                ColorQuaternary
            )
        )
    )
    LineChart(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = data
    )
}