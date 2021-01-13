package iot.polytech.banduke.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.Session
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_session.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class SessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        val idSession = intent.getIntExtra("idSession", 0)

        //Modifier session
        buttonModify.setOnClickListener{
            val intent = Intent(applicationContext, EditSessionActivity::class.java)
            intent.putExtra("name", textViewNameDB.text)
            intent.putExtra("idSession", idSession)
            startActivity(intent)
            finish()
        }

        buttonGpsData.setOnClickListener {
            val intent = Intent(applicationContext, GpsDataActivity::class.java)
            intent.putExtra("idSession", idSession)
            startActivity(intent)
        }

        buttonAccData.setOnClickListener {
            val intent = Intent(applicationContext, AccDataActivity::class.java)
            intent.putExtra("idSession", idSession)
            startActivity(intent)
        }

        buttonGyrData.setOnClickListener {
            val intent = Intent(applicationContext, GyrDataActivity::class.java)
            intent.putExtra("idSession", idSession)
            startActivity(intent)
        }

    }


    override fun onStart() {
        super.onStart()

        val idSession = intent.getIntExtra("idSession", 0)
        val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
        RetrofitClient.instance.getSessionById(idSession, token)
                .enqueue(object : Callback<Session> {
                    override fun onFailure(call: Call<Session>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Session>, response: Response<Session>) {
                        if (response.body()?.id!! > -1) {

                            val df: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
                            df.setTimeZone(TimeZone.getTimeZone("UTC"));
                            textViewNameDB.text = response.body()?.name
                            textViewDurationDB.text = response.body()?.duration.toString()+" min"
                            textViewStartTimeDB.text = df.format(response.body()?.startTime)
                            textViewEndTimeDB.text = df.format(response.body()?.endTime)

                        } else {
                            Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                        }
                    }

                })

    }
}
