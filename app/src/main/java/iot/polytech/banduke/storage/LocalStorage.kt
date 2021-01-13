package iot.polytech.banduke.storage

import android.content.Context
import iot.polytech.banduke.models.EditUser
import iot.polytech.banduke.models.User


class LocalStorage private constructor(private val mCtx: Context) {

    val bearerToken: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("bearerToken", "")
        }

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("bearerToken", "") != ""
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
            return User(
                    sharedPreferences.getInt("id", -1),
                    sharedPreferences.getString("username", null),
                    sharedPreferences.getString("firstname", null)
            )
        }


    fun saveUser(user: User) {

        val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("id", user.id)
        editor.putString("username", user.username)
        editor.putString("firstname", user.firstname)
        editor.apply()

    }

    fun saveProfile(editUser:EditUser){
        val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("firstname", editUser.firstname)
        editor.apply()
    }

    fun saveToken(bearerToken: String) {

        val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("bearerToken", bearerToken)
        editor.apply()
    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(LOCAL_STORAGE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val LOCAL_STORAGE_NAME = "my_local_storage"
        private var mInstance: LocalStorage? = null
        @Synchronized
        fun getInstance(mCtx: Context): LocalStorage {
            if (mInstance == null) {
                mInstance = LocalStorage(mCtx)
            }
            return mInstance as LocalStorage
        }
    }

}