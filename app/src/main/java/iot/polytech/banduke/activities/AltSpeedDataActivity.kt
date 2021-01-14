package iot.polytech.banduke.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.GpsData
import iot.polytech.banduke.models.SessionContent
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_altspeeddata.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AltSpeedDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_altspeeddata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentGpsDataById(idSession, token)
                .enqueue(object : Callback<List<GpsData>> {
                    override fun onFailure(call: Call<List<GpsData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<GpsData>>, response: Response<List<GpsData>>) {
                        if (response.body()?.size!!>0) {
                            val vitesses: ArrayList<Entry> = ArrayList()
                            val altitudes: ArrayList<Entry> = ArrayList()
                            val startTime: Long = response.body()?.get(0)?.gpsTime?.time!!
                            response.body()!!.forEach {
                                vitesses.add(Entry((it.gpsTime.time-startTime).toFloat(), it.gpsSpeed.toFloat()))
                                altitudes.add(Entry((it.gpsTime.time-startTime).toFloat(), it.gpsAlt.toFloat()))
                            }
                            val dataSetV: LineDataSet = LineDataSet(vitesses,"Vitesse")
                            val dataSetA: LineDataSet = LineDataSet(altitudes,"Altitude")

                            dataSetV.color=resources.getColor(R.color.colorPrimary)
                            dataSetV.setDrawValues(false)
                            dataSetV.setDrawCircles(false)

                            dataSetA.color=resources.getColor(R.color.colorPrimary)
                            dataSetA.setDrawValues(false)
                            dataSetA.setDrawCircles(false)


                            val dataV: LineData = LineData(dataSetV)
                            val dataA: LineData = LineData(dataSetA)

                            lineChartSpeed.data=dataV
                            lineChartSpeed.description.isEnabled=false
                            lineChartSpeed.axisRight.setDrawLabels(false)
                            lineChartSpeed.animateY(200)

                            lineChartAlt.data=dataA
                            lineChartAlt.description.isEnabled=false
                            lineChartAlt.axisRight.setDrawLabels(false)
                            lineChartAlt.animateY(200)


                        } else {
                            Toast.makeText(applicationContext, "Pas de données", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}