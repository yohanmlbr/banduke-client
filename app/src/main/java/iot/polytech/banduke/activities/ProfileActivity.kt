package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.UserDetails
import iot.polytech.banduke.storage.LocalStorage

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val id = intent.getIntExtra("idProfile",0)
        val myId = LocalStorage.getInstance(this).user.id
        //Modifier profil
        if(id==myId){
            buttonActionProfile.text="Modifier"
            buttonActionProfile.setOnClickListener{
                val intent = Intent(applicationContext, EditProfileActivity::class.java)
                intent.putExtra("lastname",textViewLastnameDB.text)
                intent.putExtra("firstname",textViewFirstnameDB.text)
                intent.putExtra("motorcycle",textViewMotoDB.text)
                startActivity(intent)
                finish()
            }
        }
        else{
            val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
            RetrofitClient.instance.isFriend(myId, id, token)
                    .enqueue(object: Callback<Boolean> {
                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            if(response.body()==true){
                                //Est ami => supprimer ami
                                buttonActionProfile.text="Supprimer"
                                buttonActionProfile.setBackgroundColor(resources.getColor(R.color.colorDanger));
                                buttonActionProfile.setOnClickListener{
                                    RetrofitClient.instance.deleteFriend(myId, id, token)
                                            .enqueue(object: Callback<Boolean> {
                                                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                                                }
                                                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                                    if(response.body()==true){
                                                        val intent = Intent(applicationContext, FriendsActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }else{
                                                        Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            })
                                }
                            }else{
                                //Pas ami => ajouter ami
                                buttonActionProfile.text="Ajouter"
                                buttonActionProfile.setBackgroundColor(resources.getColor(R.color.colorSuccess));
                                buttonActionProfile.setOnClickListener{
                                    RetrofitClient.instance.addFriend(myId, id, token)
                                            .enqueue(object: Callback<Boolean> {
                                                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                                                }
                                                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                                    if(response.body()==true){
                                                        val intent = Intent(applicationContext, FriendsActivity::class.java)
                                                        startActivity(intent)
                                                        finish()
                                                    }else{
                                                        Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            })
                                }
                            }
                        }
                    })
        }

    }


    override fun onStart() {
        super.onStart()

        val id = intent.getIntExtra("idProfile",0)
        val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
        RetrofitClient.instance.getUserDetailsById(id, token)
                .enqueue(object: Callback<UserDetails> {
                    override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<UserDetails>, response: Response<UserDetails>) {
                        if(response.body()?.id!! > -1){

                            textViewUsernameDB.text = response.body()?.username
                            textViewLastnameDB.text = response.body()?.lastname
                            textViewFirstnameDB.text = response.body()?.firstname
                            textViewMotoDB.text = response.body()?.motorcycle
                            textViewSessionsDB.text = response.body()?.nbsessions.toString()
                            textViewFriendsDB.text = response.body()?.nbfriends.toString()

                        }else{
                            Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                        }
                    }

                })

    }
}
