package com.example.moviepedia

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.ReqToken
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignupAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

//    val retrofit1 = Retrofit.Builder()
//        .baseUrl("https://www.themoviedb.org/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()


    lateinit var reqToken : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val serviceRT = retrofit.create(API::class.java)
        serviceRT.generateRequestToken(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    Log.d("CHECKID", "ID : ${it.body()!!.request_token}")
                    reqToken = it.body()!!.request_token
                }
            }
        })

//        val serviceAllow = retrofit1.create(API::class.java)
//        serviceAllow.allowToken(reqToken).enqueue(retrofitCallback{ throwable, response ->
//            response?.let {
//                if(it.isSuccessful) {
//                    Toast.makeText(this, "GRANTED", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

        btnsignup.setOnClickListener {
            val serviceRT = retrofit.create(API::class.java)
            val req_token = ReqToken(reqToken)
            serviceRT.create(req_token,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        Log.d("CHECKID", "ID : ${it.body()!!.session_id}")
                    } else {
                        Log.d("CHECKID", "ID : SOME ERROR")
                    }
                }
            })
        }

        btnAllow.setOnClickListener {
            var intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("https://www.themoviedb.org/authenticate/${reqToken}")
            startActivity(intent)
        }
    }
}
