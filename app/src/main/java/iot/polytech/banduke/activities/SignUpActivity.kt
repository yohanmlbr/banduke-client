package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.SignUpUser
import iot.polytech.banduke.models.TokenResponse
import iot.polytech.banduke.storage.LocalStorage

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        textViewLogIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LogInActivity::class.java))
        }


        buttonValidate.setOnClickListener {

            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val lastname = editTextName.text.toString().trim()
            val firstname = editTextFirstName.text.toString().trim()
            val motorcycle = editTextMotorcycle.text.toString().trim()


            if(username.isEmpty()){
                editTextUsername.error = "Nom d'utilisateur requis"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                editTextPassword.error = "Mot de passe requis"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if(lastname.isEmpty()){
                editTextName.error = "Nom requis"
                editTextName.requestFocus()
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

            val suu = SignUpUser(username = username, password = password, lastname = lastname, firstname = firstname, motorcycle = motorcycle)
            RetrofitClient.instance.signUpUser(suu)
                    .enqueue(object: Callback<TokenResponse>{
                        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Erreur de connexion : "+t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                            if(response.body()?.token?.length!!>0){

                                LocalStorage.getInstance(applicationContext).saveToken(response.body()?.token!!)
                                LocalStorage.getInstance(applicationContext).saveUser(response.body()?.userProfile!!)

                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)


                            }else{
                                Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                            }
                        }

                    })

        }
    }

    override fun onStart() {
        super.onStart()

        if(LocalStorage.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}
