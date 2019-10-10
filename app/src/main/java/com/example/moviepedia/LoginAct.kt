package com.example.moviepedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.ReqToken
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    lateinit var reqToken : String
    lateinit var session_id : String

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "Users.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    var flag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val trialCheck = intent.getStringExtra("trialCheck")

        val serviceRT = retrofit.create(API::class.java)
        serviceRT.generateRequestToken(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    reqToken = it.body()!!.request_token
                    Log.d("REQTOKEN", "$reqToken")
                }
            }
        })

        btnAllow.setOnClickListener {
            flag = 1
            val s1 = nameUser.editText!!.text.toString()
            val s2 = username.editText!!.text.toString()
            val s3 = password.editText!!.text.toString()
            if(s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
                if(s1.isEmpty())
                    nameUser.setError("Should not be empty")

                if(s2.isEmpty())
                    username.setError("Should not be empty")

                if(s3.isEmpty())
                    password.setError("Should not be empty")

                return@setOnClickListener
            }

            var intent = Intent(this, WebActivity::class.java)
            intent.putExtra("reqToken", "${reqToken}")
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if(flag == 0) {
                Snackbar.make(btnLogin,
                    "Please validate before login!",
                    Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            btnLogin.isVisible = false
            pBarLogin.isVisible = true
            flag = 2
            val serviceRT = retrofit.create(API::class.java)
            val req_token = ReqToken(reqToken)
            serviceRT.create(req_token,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        session_id = it.body()!!.session_id
                        val user = Users(
                            name = nameUser.editText!!.text.toString(),
                            username = username.editText!!.text.toString(),
                            password = password.editText!!.text.toString(),
                            session_id = session_id
                        )

                        db.UsersDao().insertRow(user)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("mode", "account")
                        startActivity(intent)
                        finish()
                    } else {
                        Snackbar.make(btnLogin, "Somethings wrong!", Snackbar.LENGTH_LONG).show()
                        pBarLogin.isVisible = false
                        btnLogin.isVisible = true
                    }
                }
            })
        }

        btnFT.setOnClickListener {
            if(trialCheck == "already") {
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("mode", "free")
                startActivity(intent)
                finish()
            }
        }

    }
}
