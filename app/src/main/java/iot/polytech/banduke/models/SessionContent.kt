package iot.polytech.banduke.models

data class SessionContent(val gpsData:List<GpsData>, val accData:List<AccData>, val gyrData:List<GyrData>, val calcData:List<CalcData>)