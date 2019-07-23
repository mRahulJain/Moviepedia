package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.PeopleAdapter
import com.example.moviepedia.Adapter.VideoAdapter
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.Rate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_third.*
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ThirdAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "<api_key>"
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
    val db3: RatedDatabase by lazy {
        Room.databaseBuilder(
            this,
            RatedDatabase::class.java,
            "Rate.db"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    lateinit var AccountID : String
    lateinit var movieID : String
    var chk : Int = 3
    var chkW : Int = 3

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setSupportActionBar(toolbar)

        collapseToolBar.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()

        val type = intent.getStringExtra("type")

        val userPresent = db1.UsersDao().getUser()
        val serviceAccount = retrofit.create(API::class.java)
        if(userPresent!=null) {
            serviceAccount.getAccountDetail(api_key, "${userPresent.session_id}").enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        AccountID = it.body()!!.id
                        Log.d("SESSION_ID", "${AccountID}")
                    }
                }
            })
            val isFav = db.FavDao().checkFavourite(id.toString())
            if(isFav == null) {
                chk = 0
                favMovie.setImageResource(R.drawable.ic_favorite_border)
            } else {
                chk = 1
                favMovie.setImageResource(R.drawable.ic_favorite_black)
            }

            val isWatchlist = db2.WatchDao().checkWatchlist(id.toString())
            if(isWatchlist == null) {
                chkW = 0
                btnWatchlist.setImageResource(R.drawable.ic_playlist_add_black_24dp)
            } else {
                chkW = 1
                btnWatchlist.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
            }
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
                        if(it.body()!!.poster_path == null) {
                            Picasso.with(this).load(R.drawable.baseline_broken_image_white_18dp).into(iView)
                        }
                        tVTagline.text = it.body()!!.tagline
                        tVTagline.setTextColor(Color.CYAN)
                        tVreleaseDate.text = "Release date : "
                        tVreleaseDate.text = tVreleaseDate.text.toString() + it.body()!!.release_date
                        tVoverview.text = it.body()!!.overview
                        tVvote.text = it.body()!!.vote_average + " / 10 "
                        tVvote.setTextColor(Color.CYAN)
                        tVgenre.text = " "
                        for(i in it.body()!!.genres) {
                            tVgenre.setText(tVgenre.text.toString() + i.name + ", ")
                        }
                    }
                }
            })
            val videoService = retrofit.create(API::class.java)
            videoService.getVideo(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rViewVideo.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        rViewVideo.adapter = VideoAdapter(this, it.body()!!)
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


            val castService = retrofit.create(API::class.java)
            castService.getCast(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewCast.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewCast.adapter = PeopleAdapter(this, it.body()!!.cast, false)
                }
            }
        })

        seeReview.setOnClickListener {
            val intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Review")
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }

        rateM.setOnClickListener {
            if(userPresent== null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val alreadyRated = db3.RatedDao().getRated(movieID)
            if(alreadyRated != null) {
                Toast.makeText(this, "Already Rated!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rate = Rate(
                rateMovie.rating * 2
            )

            val rated = Rated(
                media_id = movieID
            )
            db3.RatedDao().insertRow(rated)
            val serviceRating = retrofit.create(API::class.java)
            serviceRating.putRatingMovie(movieID, "application/json;charset=utf-8", rate, api_key, userPresent.session_id)
                .enqueue(retrofitCallback{ throwable, response ->
                    Log.d("SUCCESS", "IS SUCCESS, ${AccountID}, ${userPresent.session_id}")
                })
            Toast.makeText(this, "Thanks for rating", Toast.LENGTH_SHORT).show()
        }

        favMovie.setOnClickListener {
            if(userPresent== null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
            if(userPresent== null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val watch = Watchlist(
                movie_id = id.toString()
            )
            if(chkW == 0) {
                val watchL = com.example.moviepedia.DataClass.watchlist(
                    "movie",
                    movieID.toInt(),
                    true
                )
                chkW = 1
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
