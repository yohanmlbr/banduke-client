package iot.polytech.banduke.models

import java.util.*

data class Session(val id:Int, val userId:String, val name:String, val startTime: Date, val endTime:Date, val duration:Int, val content:SessionContent)