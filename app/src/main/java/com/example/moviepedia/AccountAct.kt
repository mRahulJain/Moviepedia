package com.example.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_account.*

class AccountAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        nameUserA.text = "NAME : " + name.toString()
        usernameA.text = "USERNAME : " + username.toString()
        passwordA.text = "PASSWORD : " + password.toString()
    }
}
