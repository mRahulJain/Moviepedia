package com.example.moviepedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.ReqToken
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnAllow
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

        val serviceRT = retrofit.create(API::class.java)
        serviceRT.generateRequestToken(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    reqToken = it.body()!!.request_token
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

            var intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://www.themoviedb.org/authenticate/${reqToken}")
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            flag = 2
            val serviceRT = retrofit.create(API::class.java)
            val req_token = ReqToken(reqToken)
            serviceRT.create(req_token,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        session_id = it.body()!!.session_id
                        Snackbar.make(btnLogin, "You have successfully logged in. Now, click on BACK and enjoy this app", Snackbar.LENGTH_INDEFINITE).show()
                    }
                }
            })
        }

        btnBack.setOnClickListener {
            val s1 = nameUser.editText!!.text.toString()
            val s2 = username.editText!!.text.toString()
            val s3 = password.editText!!.text.toString()
            if(!s1.isEmpty() && !s2.isEmpty() && !s3.isEmpty() && flag==2) {
                val user = Users(
                    1,
                    nameUser.editText!!.text.toString(),
                    username.editText!!.text.toString(),
                    password.editText!!.text.toString(),
                    session_id,
                    1
                )

                db.UsersDao().insertRow(user)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
