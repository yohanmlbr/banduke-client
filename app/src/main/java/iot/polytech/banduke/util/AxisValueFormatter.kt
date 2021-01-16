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

class SecAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return (value/1000).toString()+"s"
    }
}

class KmHAxisValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String? {
        return value.toString()+"km/h"
    }
}