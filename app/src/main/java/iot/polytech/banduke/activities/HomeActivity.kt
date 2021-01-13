package iot.polytech.banduke.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import iot.polytech.banduke.R
import iot.polytech.banduke.storage.LocalStorage

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonLogout.setOnClickListener {
            LocalStorage.getInstance(this).clear()
            startActivity(Intent(this@HomeActivity, LogInActivity::class.java))
        }

        buttonProfile.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.putExtra("idProfile",LocalStorage.getInstance(this).user.id)
            startActivity(intent)
        }

        buttonFriends.setOnClickListener{
            val intent = Intent(applicationContext, FriendsActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onStart() {
        super.onStart()

        if(!LocalStorage.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        else{
            val firstname = LocalStorage.getInstance(this).user.firstname
            textViewHello.text = "Bonjour "+firstname+" !"
        }

    }
}
