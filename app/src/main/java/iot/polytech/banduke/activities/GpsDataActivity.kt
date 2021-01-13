package iot.polytech.banduke.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import iot.polytech.banduke.R
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.Session
import iot.polytech.banduke.models.SessionContent
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_gpsdata.*
import kotlinx.android.synthetic.main.activity_session.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.Map
import kotlin.collections.ArrayList

class GpsDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpsdata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentById(idSession, token)
                .enqueue(object : Callback<SessionContent> {
                    override fun onFailure(call: Call<SessionContent>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<SessionContent>, response: Response<SessionContent>) {
                        if (response.body()!=null) {
                            val points: ArrayList<Entry> = ArrayList()
                            response.body()?.gpsData!!.forEach {
                                points.add(Entry(it.gpsLat.toFloat()*10000, it.gpsLon.toFloat()*10000))
                            }
                            val dataSet:LineDataSet = LineDataSet(points,"Points GPS")
                            dataSet.color=resources.getColor(R.color.colorPrimary )
                            val data:LineData = LineData(dataSet)

                            lineChartGps.data=data
                            lineChartGps.description.isEnabled=false
                            lineChartGps.axisRight.setDrawLabels(false)
                            lineChartGps.animateY(200)

                        } else {
                            Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}