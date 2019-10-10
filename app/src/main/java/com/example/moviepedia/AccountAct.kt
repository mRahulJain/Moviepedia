package com.example.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_account.*

class AccountAct : AppCompatActivity() {

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "Users.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val name = intent.getStringExtra("name")
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        nameUserA.text = "NAME : " + name.toString()
        usernameA.text = "USERNAME : " + username.toString()
        passwordA.text = "PASSWORD : " + password.toString()


        btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure ?")
                .setTitle("Logout")
                .setPositiveButton("Yes"){dialogInterface, which ->
                    val alertDialog = builder.create()
                    alertDialog.show()
                    db.UsersDao().deleteUser()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel"){dialogInterface, which ->
                    null
                }
            val alertDialog = builder.create()
            alertDialog.show()
        }



    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
