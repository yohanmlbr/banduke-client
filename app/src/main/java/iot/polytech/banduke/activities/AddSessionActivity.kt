package iot.polytech.banduke.activities

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import iot.polytech.banduke.R
import iot.polytech.banduke.api.RetrofitClient
import iot.polytech.banduke.models.Session
import iot.polytech.banduke.storage.LocalStorage
import kotlinx.android.synthetic.main.activity_addsession.*
import kotlinx.android.synthetic.main.activity_addsession.buttonValidate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader

class AddSessionActivity : AppCompatActivity() {

    lateinit var filepath : Uri
    lateinit var rawData : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addsession)

        editTextFile.setOnClickListener {
            startFileChooser()
        }

        buttonValidate.setOnClickListener {
            val filename = editTextFile.text.toString().trim()
            if(filename.isEmpty()){
                editTextFile.error = "Fichier requis"
                return@setOnClickListener
            }
            uploadFile()
        }
    }

    private fun startFileChooser(){
        var i = Intent()
        i.type = "text/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Sélectionnez le fichier"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode==Activity.RESULT_OK && data!=null){
            filepath = data.data!!

            var filename: String = ""
            val cursor: Cursor = contentResolver.query(filepath, null, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
            val cut: Int = filename.lastIndexOf('/')
            if (cut != -1) {
                filename = filename.substring(cut + 1)
            }
            editTextFile.setText(filename)
            rawData = contentResolver.openInputStream(filepath).bufferedReader().use(BufferedReader::readText)
        }
    }

    private fun uploadFile(){
        val myId = LocalStorage.getInstance(this).user.id
        val token = "Bearer "+ LocalStorage.getInstance(this).bearerToken
        RetrofitClient.instance.addSessionByUserId(myId, rawData, token)
                .enqueue(object : Callback<Session> {
                    override fun onFailure(call: Call<Session>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erreur de connexion : " + t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<Session>, response: Response<Session>) {
                        if (response.body() != null) {
                            val intent = Intent(applicationContext, ListSessionActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            Toast.makeText(applicationContext, "Erreur de connexion", Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }
}