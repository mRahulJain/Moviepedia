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
    val db1: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "Users.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    val db: FavDatabase by lazy {
        Room.databaseBuilder(
            this,
            FavDatabase::class.java,
            "Favs.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    val db2: WatchDatabase by lazy {
        Room.databaseBuilder(
            this,
            WatchDatabase::class.java,
            "Watch.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    lateinit var AccountID : String
    lateinit var movieID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setSupportActionBar(toolbar)

        collapseToolBar.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()

        val type = intent.getStringExtra("type")

        val userPresent = db1.UsersDao().getUser()
        val serviceAccount = retrofit.create(API::class.java)
        serviceAccount.getAccountDetail(api_key, "${userPresent.session_id}").enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    AccountID = it.body()!!.id
                    Log.d("SESSION_ID", "${AccountID}")
                }
            }
        })
        var chk : Int
        val isFav = db.FavDao().checkFavourite(id.toString())
        if(isFav == null) {
            chk = 0
            favMovie.setImageResource(R.drawable.ic_favorite_border)
        } else {
            chk = 1
            favMovie.setImageResource(R.drawable.ic_favorite_black)
        }

        var chkW : Int
        val isWatchlist = db2.WatchDao().checkWatchlist(id.toString())
        if(isWatchlist == null) {
            chkW = 0
            btnWatchlist.setImageResource(R.drawable.ic_playlist_add_black_24dp)
        } else {
            chkW = 1
            btnWatchlist.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
        }

        val service = retrofit.create(API::class.java)
            service.getMovie(id, api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        movieID = it.body()!!.id
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
                val fab = com.example.moviepedia.DataClass.fav(
                    "movie",
                    movieID.toInt(),
                    true
                )
                chk = 1
                db.FavDao().insertRow(fav)
                val serviceFav = retrofit.create(API::class.java)
                serviceFav.putFavourite(AccountID, "application/json;charset=utf-8" ,fab, api_key, userPresent.session_id)
                    .enqueue(retrofitCallback { throwable, response ->
                        response?.let {
                            if(it.isSuccessful) {
                                Log.d("ADDEDFAV", "IS SUCCESS")
                            }
                        }
                    })
                Toast.makeText(this, "Added to favourite", Toast.LENGTH_SHORT).show()
                favMovie.setImageResource(R.drawable.ic_favorite_black)
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }

        btnWatchlist.setOnClickListener {
            val watch = Watchlist(
                movie_id = id.toString()
            )
            if(chk == 0) {
                val watchL = com.example.moviepedia.DataClass.watchlist(
                    "movie",
                    movieID.toInt(),
                    true
                )
                chk = 1
                db2.WatchDao().insertRow(watch)
                val serviceFav = retrofit.create(API::class.java)
                serviceFav.putWatchlist(AccountID, "application/json;charset=utf-8" ,watchL, api_key, userPresent.session_id)
                    .enqueue(retrofitCallback { throwable, response ->
                        response?.let {
                            if(it.isSuccessful) {
                                Log.d("ADDEDFAV", "IS SUCCESS")
                            }
                        }
                    })
                Toast.makeText(this, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                btnWatchlist.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
