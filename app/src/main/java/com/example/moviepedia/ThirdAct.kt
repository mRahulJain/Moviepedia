package com.example.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Api.API
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/w185/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        collapseToolBar.title = "RAHUL"
        val id = intent.getStringExtra("id").toInt()

        val service = retrofit.create(API::class.java)
        service.getMovie(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    collapseToolBar.title = it.body()!!.original_title
                    Picasso.with(this).load(baseURL + it.body()!!.poster_path).into(iView)
                }
            }
        })


    }
}
