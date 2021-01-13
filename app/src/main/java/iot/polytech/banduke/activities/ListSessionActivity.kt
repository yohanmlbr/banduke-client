package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_friends.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.SessionIdData
import iot.polytech.banduke.storage.LocalStorage

class ListSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listsession)

    }


    override fun onStart() {
        super.onStart()
        var sessionList = emptyArray<String>();
        var sessionIdList = emptyArray<Int>()
        val id = LocalStorage.getInstance(this).user.id
        val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
        RetrofitClient.instance.getSessionsByUserId(id, token)
                .enqueue(object: Callback<List<SessionIdData>> {
                    override fun onFailure(call: Call<List<SessionIdData>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<SessionIdData>>, response: Response<List<SessionIdData>>) {
                        if(response.body()?.size!!>0){
                            response.body()!!.forEach {
                                sessionList=sessionList.plus(it.name)
                                sessionIdList=sessionIdList.plus(it.id)
                            }
                            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                                    applicationContext,R.layout.custom_list_item,sessionList
                            )
                            listViewSessions.adapter=arrayAdapter
                            listViewSessions.setOnItemClickListener { adapterView, view, i, id ->
                                val intent = Intent(applicationContext, SessionActivity::class.java)
                                intent.putExtra("idSession",sessionIdList[i])
                                startActivity(intent)
                                finish()
                            }
                        }else{
                            Toast.makeText(applicationContext, "Vous n'avez aucune session", Toast.LENGTH_LONG).show()
                        }
                    }

                })

    }
}
