package iot.polytech.banduke.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.AccData
import iot.polytech.banduke.storage.LocalStorage
import iot.polytech.banduke.util.AccAxisValueFormatter
import iot.polytech.banduke.util.MiniSecAxisValueFormatter
import kotlinx.android.synthetic.main.activity_accdata.*
import kotlinx.android.synthetic.main.activity_accdata.textViewBvdDB
import kotlinx.android.synthetic.main.activity_accdata.textViewBvgDB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

class AccDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accdata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentAccDataById(idSession, token)
                .enqueue(object : Callback<List<AccData>> {
                    override fun onFailure(call: Call<List<AccData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<AccData>>, response: Response<List<AccData>>) {
                        if (response.body()?.size!!>0) {
                            val pointsX: ArrayList<Entry> = ArrayList()
                            val pointsY: ArrayList<Entry> = ArrayList()
                            val pointsZ: ArrayList<Entry> = ArrayList()
                            var bvg: Double = 0.0 //best virage gauche
                            var bvd: Double = 0.0 //best virage droite
                            var ba: Double = 0.0 //best acceleration
                            var bf: Double = 0.0 //best freinage
                            val startTime: Long = response.body()?.get(0)?.accTime?.time!!
                            response.body()!!.forEach {
                                pointsX.add(Entry((it.accTime.time-startTime).toFloat(), it.accX.toFloat()))
                                pointsY.add(Entry((it.accTime.time-startTime).toFloat(), it.accY.toFloat()))
                                pointsZ.add(Entry((it.accTime.time-startTime).toFloat(), it.accZ.toFloat()))
                                if(it.accY>bvg)
                                    bvg=it.accY
                                if(it.accY<bvd)
                                    bvd=it.accY
                                if(it.accX>ba)
                                    ba=it.accX
                                if(it.accX<bf)
                                    bf=it.accX
                            }
                            val dataSetX:LineDataSet = LineDataSet(pointsX,"Axe X")
                            val dataSetY:LineDataSet = LineDataSet(pointsY,"Axe Y")
                            val dataSetZ:LineDataSet = LineDataSet(pointsZ,"Axe Z")

                            dataSetX.color=resources.getColor(R.color.colorDanger)
                            dataSetX.setDrawValues(false)
                            dataSetX.setDrawCircles(false)

                            dataSetY.color=resources.getColor(R.color.colorSuccess)
                            dataSetY.setDrawValues(false)
                            dataSetY.setDrawCircles(false)

                            dataSetZ.color=resources.getColor(R.color.colorOk)
                            dataSetZ.setDrawValues(false)
                            dataSetZ.setDrawCircles(false)

                            val data:LineData = LineData(dataSetX,dataSetY,dataSetZ)

                            lineChartAcc.data=data
                            lineChartAcc.description.isEnabled=false
                            lineChartAcc.axisRight.setDrawLabels(false)
                            lineChartAcc.xAxis.valueFormatter = MiniSecAxisValueFormatter()
                            lineChartAcc.axisLeft.valueFormatter = AccAxisValueFormatter()
                            lineChartAcc.animateY(200)

                            textViewBvgDB.text=bvg.absoluteValue.toString()+"g"
                            textViewBvdDB.text=bvd.absoluteValue.toString()+"g"
                            textViewBaDB.text=ba.absoluteValue.toString()+"g"
                            textViewBfDB.text=bf.absoluteValue.toString()+"g"

                        } else {
                            Toast.makeText(applicationContext, "Pas de données", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}