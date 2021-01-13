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
import iot.polytech.banduke.models.User
import iot.polytech.banduke.storage.LocalStorage

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


    }


    override fun onStart() {
        super.onStart()
        var usernameList = emptyArray<String>();
        var userIdList = emptyArray<Int>()
        val myId = LocalStorage.getInstance(this).user.id
        val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
        RetrofitClient.instance.getAllUsers(token)
                .enqueue(object: Callback<List<User>> {
                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        if(response.body()?.size!!>0){
                            response.body()!!.forEach {
                                if(it.id!=myId){
                                    usernameList=usernameList.plus(it.username)
                                    userIdList=userIdList.plus(it.id)
                                }
                            }
                            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                                    applicationContext,R.layout.custom_list_item,usernameList
                            )
                            listViewFriends.adapter=arrayAdapter
                            listViewFriends.setOnItemClickListener { adapterView, view, i, id ->
                                val intent = Intent(applicationContext, ProfileActivity::class.java)
                                intent.putExtra("idProfile",userIdList[i])
                                startActivity(intent)
                                finish()
                            }
                        }else{
                            Toast.makeText(applicationContext, "Aucun autre utilisateur", Toast.LENGTH_LONG).show()
                        }
                    }

                })

    }
}
