package iot.polytech.banduke.util

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter


class MetreAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"m"
    }
}

class LatAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return (value/10000).toString()+"N"
    }
}

class LonAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return (value/10000).toString()+"E"
    }
}

class AngleAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"°"
    }
}

class AccAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"g"
    }
}

class MiniSecAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"ms"
    }
}

class KmHAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"km/h"
    }
}