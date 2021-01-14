package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.ApiResponse
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_editsession.*

class EditSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editsession)

        buttonValidate.setOnClickListener {

            val idSession = intent.getIntExtra("idSession",0)
            val name = editTextName.text.toString().trim()

            if(name.isEmpty()){
                editTextName.error = "Nom requis"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
            RetrofitClient.instance.renameSessionById(idSession,name,token)
                    .enqueue(object: Callback<ApiResponse>{
                        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Erreur de connexion : "+t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                            if(response.body()?.success==true){

                                val intent = Intent(applicationContext, ListSessionActivity::class.java)
                                startActivity(intent)
                                finish()

                            }else{
                                Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                            }
                        }

                    })

        }
    }

    override fun onStart() {
        super.onStart()

        editTextName.setText(intent.getStringExtra("name"))
    }
}
