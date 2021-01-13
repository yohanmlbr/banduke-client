package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_signup.buttonValidate
import kotlinx.android.synthetic.main.activity_signup.editTextFirstName
import kotlinx.android.synthetic.main.activity_signup.editTextLastName
import kotlinx.android.synthetic.main.activity_signup.editTextMotorcycle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.EditUser
import iot.polytech.banduke.storage.LocalStorage

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        buttonValidate.setOnClickListener {

            val id = LocalStorage.getInstance(applicationContext).user.id
            val lastname = editTextLastName.text.toString().trim()
            val firstname = editTextFirstName.text.toString().trim()
            val motorcycle = editTextMotorcycle.text.toString().trim()

            if(lastname.isEmpty()){
                editTextLastName.error = "Nom requis"
                editTextLastName.requestFocus()
                return@setOnClickListener
            }

            if(firstname.isEmpty()){
                editTextFirstName.error = "Prénom requis"
                editTextFirstName.requestFocus()
                return@setOnClickListener
            }

            if(firstname.isEmpty()){
                editTextMotorcycle.error = "Moto requise"
                editTextMotorcycle.requestFocus()
                return@setOnClickListener
            }

            val token = "Bearer "+LocalStorage.getInstance(this).bearerToken
            val eu = EditUser(id = id, lastname = lastname, firstname = firstname, motorcycle = motorcycle)
            RetrofitClient.instance.editUser(eu,token)
                    .enqueue(object: Callback<EditUser>{
                        override fun onFailure(call: Call<EditUser>, t: Throwable) {
                            Toast.makeText(applicationContext, "Erreur de connexion : "+t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<EditUser>, response: Response<EditUser>) {
                            if(response.body()!=null){

                                LocalStorage.getInstance(applicationContext).saveProfile(response.body()!!)

                                val intent = Intent(applicationContext, ProfileActivity::class.java)
                                intent.putExtra("idProfile",LocalStorage.getInstance(applicationContext).user.id)
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

        editTextLastName.setText(intent.getStringExtra("lastname"))
        editTextFirstName.setText(intent.getStringExtra("firstname"))
        editTextMotorcycle.setText(intent.getStringExtra("motorcycle"))

    }
}
