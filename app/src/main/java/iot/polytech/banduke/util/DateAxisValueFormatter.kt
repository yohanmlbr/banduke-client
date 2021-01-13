package iot.polytech.banduke.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat


internal class DateAxisValueFormatter(private val mValues: Array<String>) : IAxisValueFormatter {
    var sdf = SimpleDateFormat("yyyy.MM.dd.hh")
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[value.toInt()]
    }
}