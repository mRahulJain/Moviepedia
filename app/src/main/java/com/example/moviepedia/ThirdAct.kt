package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.GenreAdapter
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
    var overview : String = ""
    var flag = 0
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
    var chk : Int = 0
    var chkW : Int = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
                        tVoverviewF.isVisible = false
                        movieID = it.body()!!.id
                        collapseToolBar.title = it.body()!!.original_title
                        Picasso
                            .with(this)
                            .load(baseURL + it.body()!!.poster_path)
                            .fit()
                            .into(iView)
                        if(it.body()!!.poster_path == null) {
                            Picasso.with(this).load(R.drawable.baseline_broken_image_white_18dp).into(iView)
                        }
                        if(it.body()!!.tagline == null) {
                            tVTagline.isVisible = false
                        } else {
                            tVTagline.text = it.body()!!.tagline
                            tVTagline.setTextColor(Color.CYAN)
                        }
                        if(it.body()!!.release_date ==  null) {
                            tVreleaseDate.isVisible = false
                        } else {
                            tVreleaseDate.text = "Release date : "
                            tVreleaseDate.text = tVreleaseDate.text.toString() + it.body()!!.release_date
                        }
                        if(it.body()!!.overview == null) {
                            tVoverview.isVisible = false
                            expand_button.isVisible = false
                        } else {
                            overview = it.body()!!.overview
                            tVoverview.text = it.body()!!.overview
                            if(tVoverview.lineCount <= 4) {
                                expand_button.isVisible = false
                            } else {
                                expand_button.setImageResource(R.drawable.ic_expand_more)
                            }
                        }
                        if(it.body()!!.vote_average == null) {
                            tVvote.isVisible = false
                        } else {
                            tVvote.text = it.body()!!.vote_average + " / 10 "
                            tVvote.setTextColor(Color.CYAN)
                        }
                        if(it.body()!!.genres == null) {
                            tV.isVisible = false
                            rViewGenre.isVisible = false
                        } else {
                            tV.text = "Genre"
                            rViewGenre.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                            rViewGenre.adapter = GenreAdapter(this, it.body()!!.genres)
                        }
                    }
                }
            })
            val videoService = retrofit.create(API::class.java)
            videoService.getVideo(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            tVvideo.isVisible = false
                            rViewVideo.isVisible = false
                        } else {
                            tVvideo.text = "Videos"
                            rViewVideo.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                            rViewVideo.adapter = VideoAdapter(this, it.body()!!)
                        }
                    }
                 }
            })
            val similarService = retrofit.create(API::class.java)
            similarService.getSimilarMovie(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            rViewSimilar.isVisible = false
                            tVsimilar.isVisible = false
                        } else {
                            tVsimilar.text = "Similar Movies"
                            rViewSimilar.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                            rViewSimilar.adapter = CommonAdapter(this, it.body()!!.results, false)
                        }
                    }
                }
            })


            val castService = retrofit.create(API::class.java)
            castService.getCast(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.cast.size == 0) {
                        rViewCast.isVisible = false
                        tVcast.isVisible = false
                    } else {
                        tVcast.text = "Cast"
                        rViewCast.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                        rViewCast.adapter = PeopleAdapter(this, it.body()!!.cast, false)
                    }
                }
            }
        })

        expand_button.setOnClickListener {
            if(flag == 0) {
                flag = 1
                expand_button.setImageResource(R.drawable.ic_expand_less)
                tVoverview.isVisible = false
                tVoverviewF.isVisible = true
                tVoverviewF.setText(overview)
            } else {
                flag = 0
                expand_button.setImageResource(R.drawable.ic_expand_more)
                tVoverview.isVisible = true
                tVoverviewF.isVisible = false
                tVoverview.setText(overview)
            }
        }

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
            val serviceRating = retrofit.create(API::class.java)
            serviceRating.putRatingMovie(movieID, "application/json;charset=utf-8", rate, api_key, userPresent.session_id)
                .enqueue(retrofitCallback{ throwable, response ->
                    response?.let {
                        if(it.isSuccessful) {
                            db3.RatedDao().insertRow(rated)
                            Toast.makeText(this, "Thanks for rating", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
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
                val serviceFav = retrofit.create(API::class.java)
                serviceFav.putFavourite(AccountID, "application/json;charset=utf-8" ,fab, api_key, userPresent.session_id)
                    .enqueue(retrofitCallback { throwable, response ->
                        response?.let {
                            if(it.isSuccessful) {
                                chk = 1
                                db.FavDao().insertRow(fav)
                                Toast.makeText(this, "Added to favourite", Toast.LENGTH_SHORT).show()
                                favMovie.setImageResource(R.drawable.ic_favorite_black)
                            }
                        }
                    })

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
                val serviceFav = retrofit.create(API::class.java)
                serviceFav.putWatchlist(AccountID, "application/json;charset=utf-8" ,watchL, api_key, userPresent.session_id)
                    .enqueue(retrofitCallback { throwable, response ->
                        response?.let {
                            if(it.isSuccessful) {
                                chkW = 1
                                db2.WatchDao().insertRow(watch)
                                Toast.makeText(this, "Added to Watchlist", Toast.LENGTH_SHORT).show()
                                btnWatchlist.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
                            }
                        }
                    })
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
