package com.example.moviepedia

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.ReviewAdapter
import com.example.moviepedia.Api.API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling_3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val db: FavDatabase by lazy {
        Room.databaseBuilder(
            this,
            FavDatabase::class.java,
            "Favs.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setSupportActionBar(toolbar)

        collapseToolBar.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()

        val type = intent.getStringExtra("type")
//        var checkFav = intent.getStringExtra("checkFav").toString().toInt()
//        if(checkFav == 0) {
//            favMovie.setImageResource(R.drawable.ic_favorite_border)
//        } else {
//            favMovie.setImageResource(R.drawable.ic_favorite_black)
//        }
        var chk : Int
        val isFav = db.FavDao().checkFavourite(id.toString())
        if(isFav == null) {
            chk = 0
            favMovie.setImageResource(R.drawable.ic_favorite_border)
        } else {
            chk = 1
            favMovie.setImageResource(R.drawable.ic_favorite_black)
        }

        val service = retrofit.create(API::class.java)
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
                        tVvote.text = it.body()!!.vote_average + " / 10 "
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

        browseVideo.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("id", id.toString())
            intent.putExtra("type", "Movie")
            startActivity(intent)
        }

        seeCast.setOnClickListener {
            val intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Cast")
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }

        favMovie.setOnClickListener {
            val fav = Favourites(
                movie_id = id.toString()
            )
            if(chk == 0) {
                chk = 1
                db.FavDao().insertRow(fav)
                Toast.makeText(this, "Added to favourite", Toast.LENGTH_SHORT).show()
                favMovie.setImageResource(R.drawable.ic_favorite_black)
            } else {
                chk = 0
                db.FavDao().delete(id.toString())
                Toast.makeText(this, "Removed from favourite", Toast.LENGTH_SHORT).show()
                favMovie.setImageResource(R.drawable.ic_favorite_border)
            }
        }
    }
}
