package iot.polytech.banduke.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.SessionContent
import iot.polytech.banduke.storage.LocalStorage
import iot.polytech.banduke.util.DateAxisValueFormatter
import kotlinx.android.synthetic.main.activity_accdata.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AccDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accdata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentById(idSession, token)
                .enqueue(object : Callback<SessionContent> {
                    override fun onFailure(call: Call<SessionContent>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<SessionContent>, response: Response<SessionContent>) {
                        if (response.body()!=null) {
                            val pointsX: ArrayList<Entry> = ArrayList()
                            val pointsY: ArrayList<Entry> = ArrayList()
                            val pointsZ: ArrayList<Entry> = ArrayList()
                            val startTime: Long = response.body()?.accData?.get(0)?.accTime?.time!!
                            response.body()?.accData!!.forEach {
                                pointsX.add(Entry((it.accTime.time-startTime).toFloat(), it.accX.toFloat()))
                                pointsY.add(Entry((it.accTime.time-startTime).toFloat(), it.accY.toFloat()))
                                pointsZ.add(Entry((it.accTime.time-startTime).toFloat(), it.accZ.toFloat()))
                            }
                            val dataSetX:LineDataSet = LineDataSet(pointsX,"Points X")
                            val dataSetY:LineDataSet = LineDataSet(pointsY,"Points Y")
                            val dataSetZ:LineDataSet = LineDataSet(pointsZ,"Points Z")

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
                            lineChartAcc.animateY(200)


                        } else {
                            Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}