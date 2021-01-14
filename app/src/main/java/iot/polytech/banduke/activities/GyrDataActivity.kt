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
import iot.polytech.banduke.models.GyrData
import iot.polytech.banduke.models.SessionContent
import iot.polytech.banduke.storage.LocalStorage
import iot.polytech.banduke.util.AccAxisValueFormatter
import iot.polytech.banduke.util.AngleAxisValueFormatter
import iot.polytech.banduke.util.MiniSecAxisValueFormatter
import kotlinx.android.synthetic.main.activity_gyrdata.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue

class GyrDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyrdata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentGyrDataById(idSession, token)
                .enqueue(object : Callback<List<GyrData>> {
                    override fun onFailure(call: Call<List<GyrData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<GyrData>>, response: Response<List<GyrData>>) {
                        if (response.body()?.size!!>0) {
                            val pointsX: ArrayList<Entry> = ArrayList()
                            val pointsY: ArrayList<Entry> = ArrayList()
                            var bvg: Double = 0.0 //best virage gauche
                            var bvd: Double = 0.0 //best virage droite
                            var bw: Double = 0.0 //best wheeling
                            var bs: Double = 0.0 //best stoppie
                            var startTime: Long = response.body()?.get(0)?.gyrTime?.time!!
                            response.body()!!.forEach {
                                pointsX.add(Entry((it.gyrTime.time-startTime).toFloat(), it.gyrX.toFloat()))
                                pointsY.add(Entry((it.gyrTime.time-startTime).toFloat(), it.gyrY.toFloat()))
                                if(it.gyrX>bvg)
                                    bvg=it.gyrX
                                if(it.gyrX<bvd)
                                    bvd=it.gyrX
                                if(it.gyrY>bw)
                                    bw=it.gyrY
                                if(it.gyrY<bs)
                                    bs=it.gyrY
                            }
                            val dataSetX:LineDataSet = LineDataSet(pointsX,"Axe X")
                            val dataSetY:LineDataSet = LineDataSet(pointsY,"Axe Y")

                            dataSetX.color=resources.getColor(R.color.colorDanger)
                            dataSetX.setDrawValues(false)
                            dataSetX.setDrawCircles(false)

                            dataSetY.color=resources.getColor(R.color.colorSuccess)
                            dataSetY.setDrawValues(false)
                            dataSetY.setDrawCircles(false)

                            val data:LineData = LineData(dataSetX,dataSetY)

                            lineChartGyr.data=data
                            lineChartGyr.description.isEnabled=false
                            lineChartGyr.axisRight.setDrawLabels(false)
                            lineChartGyr.xAxis.valueFormatter = MiniSecAxisValueFormatter()
                            lineChartGyr.axisLeft.valueFormatter = AngleAxisValueFormatter()
                            lineChartGyr.animateY(200)

                            textViewBvgDB.text=bvg.absoluteValue.toString()+"°"
                            textViewBvdDB.text=bvd.absoluteValue.toString()+"°"
                            textViewBwDB.text=bw.absoluteValue.toString()+"°"
                            textViewBsDB.text=bs.absoluteValue.toString()+"°"

                        } else {
                            Toast.makeText(applicationContext, "Pas de données", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}