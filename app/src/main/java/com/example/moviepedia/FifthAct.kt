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
import com.example.moviepedia.Adapter.GenreAdapter
import com.example.moviepedia.Adapter.PeopleAdapter
import com.example.moviepedia.Adapter.TVAdapter
import com.example.moviepedia.Adapter.VideoAdapter
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.Rate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fifth.*
import kotlinx.android.synthetic.main.content_scrolling_3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FifthAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    var overview : String = ""
    var flag = 0
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
    lateinit var tv_id : String
    var chk : Int = 0
    var chkW : Int = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setSupportActionBar(toolbarTV)

        collapseToolBarTV.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()
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
                favTV.setImageResource(R.drawable.ic_favorite_border)
            } else {
                chk = 1
                favTV.setImageResource(R.drawable.ic_favorite_black)
            }

            val isWatchlist = db2.WatchDao().checkWatchlist(id.toString())
            if(isWatchlist == null) {
                chkW = 0
                btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_black_24dp)
            } else {
                chkW = 1
                btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
            }
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
                        .fit()
                        .into(iViewTV)
                    if(it.body()!!.poster_path == null) {
                        Picasso.with(this).load(R.drawable.baseline_broken_image_white_18dp).into(iViewTV)
                    }
                    if(it.body()!!.first_air_date == null) {
                        tVreleaseDateTV.isVisible = false
                    } else {
                        tVreleaseDateTV.text = "Release date : "
                        tVreleaseDateTV.text = tVreleaseDateTV.text.toString() + it.body()!!.first_air_date
                    }
                    if(it.body()!!.overview == null) {
                        tVoverviewTV.isVisible = false
                        expand_button_3.isVisible = false
                    } else {
                        overview = it.body()!!.overview
                        tVoverviewTV.text = it.body()!!.overview
                        if(tVoverviewTV.lineCount <= 4) {
                            expand_button_3.isVisible = false
                        } else {
                            expand_button_3.setImageResource(R.drawable.ic_expand_more)
                        }
                    }
                    if(it.body()!!.vote_average == null) {
                        tVvoteTV.isVisible = false
                    } else {
                        tVvoteTV.text = it.body()!!.vote_average + " / 10 "
                        tVvoteTV.setTextColor(Color.CYAN)
                    }
                    if(it.body()!!.genres ==  null) {
                        tVgenreTV.isVisible = false
                        rViewGenreTV.isVisible = false
                    } else {
                        tVgenreTV.text = "Genre"
                        rViewGenreTV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        rViewGenreTV.adapter = GenreAdapter(this, it.body()!!.genres)
                    }
                }
            }
        })
        val videoService = retrofit.create(API::class.java)
        videoService.getVideoTV(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.results.size == 0) {
                        rViewVideoTV.isVisible = false
                        tVvideoTV.isVisible = false
                    } else {
                        tVvideoTV.text = "Videos"
                        rViewVideoTV.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        rViewVideoTV.adapter = VideoAdapter(this, it.body()!!)
                    }
                }
            }
        })
        val similarService = retrofit.create(API::class.java)
        similarService.getTVSimilar(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.results.size == 0) {
                        tVsimilarTV.isVisible = false
                        rViewSimilarTV.isVisible = false
                    } else {
                        tVsimilarTV.text = "Similar TV Series"
                        rViewSimilarTV.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        rViewSimilarTV.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            }
        })
        val castService = retrofit.create(API::class.java)
        castService.getTVCast(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.cast.size == 0) {
                        tVcastTV.isVisible = false
                        rViewCastTV.isVisible = false
                    } else {
                        tVcastTV.text = "Cast"
                        rViewCastTV.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                        rViewCastTV.adapter = PeopleAdapter(this, it.body()!!.cast, false)
                    }
                }
            }
        })

        expand_button_3.setOnClickListener {
            if(flag == 0) {
                flag = 1
                expand_button_3.setImageResource(R.drawable.ic_expand_less)
                tVoverviewTV.isVisible = false
                tVoverviewTVF.isVisible = true
                tVoverviewTVF.setText(overview)
            } else {
                flag = 0
                expand_button_3.setImageResource(R.drawable.ic_expand_more)
                tVoverviewTV.isVisible = true
                tVoverviewTVF.isVisible = false
                tVoverviewTV.setText(overview)
            }
        }

        seeReviewsTV.setOnClickListener {
            val intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "TVReview")
            intent.putExtra("id", id.toString())
            startActivity(intent)
        }

        rateTVSeries.setOnClickListener {
            if(userPresent == null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val alreadyRated = db3.RatedDao().getRated(id.toString())
            if(alreadyRated != null) {
                Toast.makeText(this, "Already Rated!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rate = Rate(
                rateTV.rating * 2
            )

            val rated = Rated(
                media_id = id.toString()
            )
            val serviceRating = retrofit.create(API::class.java)
            serviceRating.putRatingTV(id.toString(), "application/json;charset=utf-8", rate, api_key, userPresent.session_id)
                .enqueue(retrofitCallback{ throwable, response ->
                    response?.let {
                        if(it.isSuccessful) {
                            db3.RatedDao().insertRow(rated)
                            Toast.makeText(this, "Thanks for rating", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
        favTV.setOnClickListener {
            if(userPresent == null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val fav = Favourites(
                movie_id = id.toString()
            )
            if(chk == 0) {
                val fab = com.example.moviepedia.DataClass.fav(
                    "tv",
                    tv_id.toInt(),
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
                                favTV.setImageResource(R.drawable.ic_favorite_black)
                            }
                        }
                    })
            } else  {
                Toast.makeText(this, "Already added!", Toast.LENGTH_SHORT).show()
            }
        }
        btnWatchlistTV.setOnClickListener {
            if(userPresent == null) {
                Toast.makeText(this, "Login required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val watch = Watchlist(
                movie_id = id.toString()
            )
            if(chkW == 0) {
                val watchL = com.example.moviepedia.DataClass.watchlist(
                    "tv",
                    tv_id.toInt(),
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
                                btnWatchlistTV.setImageResource(R.drawable.ic_playlist_add_check_black_24dp)
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
