package com.example.moviepedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.moviepedia.Adapter.ReviewAdapter
import com.example.moviepedia.Adapter.TVAdapter
import com.example.moviepedia.Api.API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fifth.*
import kotlinx.android.synthetic.main.activity_third.*
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
    lateinit var tv_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        setSupportActionBar(toolbarTV)

        collapseToolBarTV.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()
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
            favTV.setImageResource(R.drawable.ic_favorite_border)
        } else {
            chk = 1
            favTV.setImageResource(R.drawable.ic_favorite_black)
        }

        var chkW : Int
        val isWatchlist = db2.WatchDao().checkWatchlist(id.toString())
        if(isWatchlist == null) {
            chkW = 0
            btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_black_24dp)
        } else {
            chkW = 1
            btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
        }

        val service = retrofit.create(API::class.java)
        service.getTV(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    tv_id = it.body()!!.id
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

        favTV.setOnClickListener {
            val fav = Favourites(
                movie_id = id.toString()
            )
            if(chk == 0) {
                val fab = com.example.moviepedia.DataClass.fav(
                    "tv",
                    tv_id.toInt(),
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
                favTV.setImageResource(R.drawable.ic_favorite_black)
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }
        btnWatchlistTV.setOnClickListener {
            val watch = Watchlist(
                movie_id = id.toString()
            )
            if(chk == 0) {
                val watchL = com.example.moviepedia.DataClass.watchlist(
                    "tv",
                    tv_id.toInt(),
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
                btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
