package iot.polytech.banduke.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.CalcData
import iot.polytech.banduke.storage.LocalStorage
import iot.polytech.banduke.util.MetreAxisValueFormatter
import iot.polytech.banduke.util.MiniSecAxisValueFormatter
import kotlinx.android.synthetic.main.activity_calcdata.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.absoluteValue


class CalcDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcdata)

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken

        RetrofitClient.instance.getSessionContentCalcDataById(idSession, token)
                .enqueue(object : Callback<List<CalcData>> {
                    override fun onFailure(call: Call<List<CalcData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<CalcData>>, response: Response<List<CalcData>>) {
                        if (response.body()?.size!! > 0) {
                            val denivNeg: ArrayList<Entry> = ArrayList()
                            val denivPos: ArrayList<Entry> = ArrayList()
                            val distTot: ArrayList<Entry> = ArrayList()
                            var dt: Float = 0.0f
                            var dn: Float = 0.0f
                            var dp: Float = 0.0f
                            val startTime: Long = response.body()?.get(0)?.calcTime?.time!!
                            response.body()!!.forEach {
                                denivNeg.add(Entry((it.calcTime.time - startTime).toFloat(), it.denivNeg.toFloat()))
                                denivPos.add(Entry((it.calcTime.time - startTime).toFloat(), it.denivPos.toFloat()))
                                distTot.add(Entry((it.calcTime.time - startTime).toFloat(), it.distTot.toFloat()))
                            }

                            dt=distTot[distTot.size-1].y
                            dn=denivNeg[denivNeg.size-1].y
                            dp=denivPos[denivPos.size-1].y

                            val dataSetDn: LineDataSet = LineDataSet(denivNeg, "Dénivelé négatif")
                            val dataSetDp: LineDataSet = LineDataSet(denivPos, "Dénivelé positif")
                            val dataSetDt: LineDataSet = LineDataSet(distTot, "Distance parcourue")

                            dataSetDn.color = resources.getColor(R.color.colorDanger)
                            dataSetDn.setDrawValues(false)
                            dataSetDn.setDrawCircles(false)

                            dataSetDp.color = resources.getColor(R.color.colorSuccess)
                            dataSetDp.setDrawValues(false)
                            dataSetDp.setDrawCircles(false)

                            dataSetDt.color = resources.getColor(R.color.colorPrimary)
                            dataSetDt.setDrawValues(false)
                            dataSetDt.setDrawCircles(false)


                            val dataDeniv: LineData = LineData(dataSetDn, dataSetDp)
                            val dataDist: LineData = LineData(dataSetDt)

                            lineChartDeniv.data = dataDeniv
                            lineChartDeniv.description.isEnabled = false
                            lineChartDeniv.axisRight.setDrawLabels(false)
                            lineChartDeniv.xAxis.valueFormatter = MiniSecAxisValueFormatter()
                            lineChartDeniv.axisLeft.valueFormatter = MetreAxisValueFormatter()
                            lineChartDeniv.animateY(200)

                            lineChartDist.data = dataDist
                            lineChartDist.description.isEnabled = false
                            lineChartDist.axisRight.setDrawLabels(false)
                            lineChartDist.xAxis.valueFormatter = MiniSecAxisValueFormatter()
                            lineChartDist.axisLeft.valueFormatter = MetreAxisValueFormatter()
                            lineChartDist.animateY(200)

                            textViewDistDB.text="Total : "+dt.absoluteValue.toString()+"m"
                            textViewDenivDB.text="+ : "+dp.absoluteValue.toString()+"m / - : "+dn.absoluteValue.toString()+"m"

                        } else {
                            Toast.makeText(applicationContext, "Pas de données", Toast.LENGTH_LONG).show()
                        }
                    }

                })
    }
}