package com.psi.shoppingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.psi.shoppingapp.R

class LoginRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, LoginRegisterFragment.newInstance())
//                .commitNow()
//        }
    }
}