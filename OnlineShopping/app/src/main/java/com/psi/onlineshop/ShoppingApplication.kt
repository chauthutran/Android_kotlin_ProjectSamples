package com.psi.onlineshop

import android.app.Application
import android.content.Context
import android.content.Intent
import com.psi.onlineshop.activities.LoginActivity
import com.psi.onlineshop.data.User
import com.psi.onlineshop.httpRequest.HttpRequestUtil
import org.json.JSONObject

class ShoppingApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val userData = getUserData(this)

        if (userData == null) {
            // User is not logged in, redirect to login screen
            startActivity(Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // the "Login" activity will be launched independently of the "Shopping Activity" stack of the current task
            })
        }
    }

    companion object {

        const val PREF_NAME = "MyPrefs"
        const val KEY_LOGGED_IN = "isLoggedIn"

        fun saveUserData( context: Context, user: User ) {
            val sharedPrefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putString("userData", HttpRequestUtil.convertObjToJson(user).toString())
            editor.apply()
        }

        fun getUserData(context: Context) : User? {
            val sharedPrefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
            val jsonDataStr = sharedPrefs.getString("userData", null)
            jsonDataStr?.let {
                val jsonData = JSONObject(jsonDataStr)
                return HttpRequestUtil.convertJsonToObj<User>( jsonData )
            }

            return null
        }
    }
}