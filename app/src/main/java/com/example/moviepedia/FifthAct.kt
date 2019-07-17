package com.example.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.ReviewAdapter
import com.example.moviepedia.Adapter.TVAdapter
import com.example.moviepedia.Api.API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fifth.*
import kotlinx.android.synthetic.main.activity_third.collapseToolBar
import kotlinx.android.synthetic.main.activity_third.toolbar
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling_3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FifthAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        setSupportActionBar(toolbarTV)

        collapseToolBarTV.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()

        val service = retrofit.create(API::class.java)
        service.getTV(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    collapseToolBarTV.title = it.body()!!.name
                    Picasso
                        .with(this)
                        .load(baseURL + it.body()!!.poster_path)
                        .resize(450,600)
                        .into(iViewTV)
                    tVreleaseDateTV.text = "Release date : "
                    tVreleaseDateTV.text = tVreleaseDateTV.text.toString() + it.body()!!.first_air_date
                    tVoverviewTV.text = it.body()!!.overview
                    tVvoteTV.text = it.body()!!.vote_average + " / 10 "
                    tVgenreTV.text = " "
                    for(i in it.body()!!.genres) {
                        tVgenreTV.setText(tVgenreTV.text.toString() + i.name + ", ")
                    }
                }
            }
        })
        val similarService = retrofit.create(API::class.java)
        similarService.getTVSimilar(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewSimilarTV.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    rViewSimilarTV.adapter = TVAdapter(this, it.body()!!.results)
                }
            }
        })
        val reviewService = retrofit.create(API::class.java)
        reviewService.getReviewTV(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                rViewreviewTV.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                rViewreviewTV.adapter = ReviewAdapter(this, it.body()!!.results)
            }
        })

        browseVideoTV.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("type", "TV")
            startActivity(intent)
        }

        seeCastTV.setOnClickListener {
            val intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "TVCast")
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }
    }
}
