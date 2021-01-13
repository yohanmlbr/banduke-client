package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextPassword
import kotlinx.android.synthetic.main.activity_login.editTextUsername
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.LogInUser
import iot.polytech.banduke.models.TokenResponse
import iot.polytech.banduke.storage.LocalStorage

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textViewSignUp.setOnClickListener {
            startActivity(Intent(this@LogInActivity, SignUpActivity::class.java))
        }

        buttonLogin.setOnClickListener {

            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(username.isEmpty()){
                editTextUsername.error = "Email required"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            val liu = LogInUser(username = username, password = password)
            RetrofitClient.instance.logInUser(liu)
                    .enqueue(object: Callback<TokenResponse>{
                        override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                            if(response.body()?.token?.length!!>0){

                                LocalStorage.getInstance(applicationContext).saveToken(response.body()?.token!!)
                                LocalStorage.getInstance(applicationContext).saveUser(response.body()?.userProfile!!)

                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                //startActivity(Intent(this@LogInActivity, HomeActivity::class.java))


                            }else{
                                Toast.makeText(applicationContext, "Nom d'utilisateur ou mot de passe invalide", Toast.LENGTH_LONG).show()
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
