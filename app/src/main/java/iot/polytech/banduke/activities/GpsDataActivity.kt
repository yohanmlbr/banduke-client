package iot.polytech.banduke.activities

import android.content.Intent
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
import iot.polytech.banduke.models.GpsData
import iot.polytech.banduke.models.Session
import iot.polytech.banduke.models.SessionContent
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_gpsdata.*
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

        buttonAltSpeed.setOnClickListener {
            val intent = Intent(applicationContext, AltSpeedDataActivity::class.java)
            intent.putExtra("idSession", idSession)
            startActivity(intent)
        }

        RetrofitClient.instance.getSessionContentGpsDataById(idSession, token)
                .enqueue(object : Callback<List<GpsData>> {
                    override fun onFailure(call: Call<List<GpsData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<GpsData>>, response: Response<List<GpsData>>) {
                        if (response.body()?.size!!>0) {
                            val points: ArrayList<Entry> = ArrayList()
                            response.body()!!.forEach {
                                points.add(Entry(it.gpsLat.toFloat()*10000, it.gpsLon.toFloat()*10000))
                            }
                            val dataSet:LineDataSet = LineDataSet(points,"Trajet GPS")

                            dataSet.color=resources.getColor(R.color.colorPrimary )
                            dataSet.setDrawValues(false)
                            dataSet.setDrawCircles(false)

                            val data:LineData = LineData(dataSet)

                            lineChartGps.data=data
                            lineChartGps.description.isEnabled=false
                            lineChartGps.axisRight.setDrawLabels(false)
                            lineChartGps.animateY(200)

                        } else {
                            Toast.makeText(applicationContext, "Pas de données", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}