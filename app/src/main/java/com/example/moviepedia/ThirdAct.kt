package com.example.moviepedia

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.ReviewAdapter
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.Common_results
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setSupportActionBar(toolbar)

        collapseToolBar.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()
        val type = intent.getStringExtra("type")

        val service = retrofit.create(API::class.java)
        if(type=="Movie") {
            service.getMovie(id, api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        collapseToolBar.title = it.body()!!.original_title
                        Picasso
                            .with(this)
                            .load(baseURL + it.body()!!.poster_path)
                            .resize(450,600)
                            .into(iView)
                        tVTagline.text = it.body()!!.tagline
                        tVTagline.setTextColor(Color.CYAN)
                        tVreleaseDate.text = "Release date : "
                        tVreleaseDate.text = tVreleaseDate.text.toString() + it.body()!!.release_date
                        tVoverview.text = it.body()!!.overview
                        tVgenre.text = " "
                        for(i in it.body()!!.genres) {
                            tVgenre.setText(tVgenre.text.toString() + i.name + ", ")
                        }
                    }
                }
            })
            val similarService = retrofit.create(API::class.java)
            similarService.getSimilarMovie(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rViewSimilar.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                        rViewSimilar.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
            val reviewService = retrofit.create(API::class.java)
            reviewService.getReview(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    rViewreview.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewreview.adapter = ReviewAdapter(this, it.body()!!.results)
                }
            })
        } else {
            service.getPeope(id, api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        collapseToolBar.title = it.body()!!.name
                        Picasso
                            .with(this)
                            .load(baseURL + it.body()!!.profile_path)
                            .resize(450,600)
                            .into(iView)
                    }
                }
            })
        }



    }
}
