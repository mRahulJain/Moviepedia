package com.example.moviepedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.VideoAdapter
import com.example.moviepedia.Api.API
import kotlinx.android.synthetic.main.activity_video.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VideoActivity : AppCompatActivity() {

    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val id = intent.getStringExtra("id").toInt()
        val type = intent.getStringExtra("type")

        val videoService = retrofit.create(API::class.java)

        if(type=="Movie") {
            videoService.getVideo(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rViewVideo.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                        rViewVideo.adapter = VideoAdapter(this, it.body()!!)
                    }
                }
            })
        } else {
            videoService.getVideoTV(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rViewVideo.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                        rViewVideo.adapter = VideoAdapter(this, it.body()!!)
                    }
                }
            })
        }
    }
}
