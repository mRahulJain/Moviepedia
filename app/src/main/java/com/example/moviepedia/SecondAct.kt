package com.example.moviepedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.PeopleAdapter
import com.example.moviepedia.Adapter.TVAdapter
import com.example.moviepedia.Api.API
import kotlinx.android.synthetic.main.activity_second.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondAct : AppCompatActivity() {

    val media_type = "all"
    val time_window = "week"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    lateinit var type : String
    var maxLimit : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        lastLayout.isVisible = true

        type = intent.getStringExtra("type")
        textDest.text = type
        if(type=="Trending") {
            maxLimit = 1000
        } else if(type == "Now Playing") {
            maxLimit = 47
        } else if(type == "Upcoming") {
            maxLimit = 11
        } else if(type == "Popular") {
            maxLimit = 996
        } else if(type == "Top Rated") {
            maxLimit = 371
        } else if(type == "Case") {
            maxLimit = 1
        }

        toBeCalled()

        forwardN.setOnClickListener {
            if(trackN.text.toString().toInt() == maxLimit) {
                return@setOnClickListener
            }
            trackN.text = ((trackN.text.toString().toInt()) + 1).toString()
            toBeCalled()
        }
        backwardN.setOnClickListener {
            if(trackN.text.toString().toInt()==1) {
                return@setOnClickListener
            }
            trackN.text = ((trackN.text.toString().toInt()) - 1).toString()
            toBeCalled()
        }
    }

    @SuppressLint("WrongConstant")
    fun toBeCalled() {
        val service = retrofit.create(API::class.java)
        if(type=="Trending") {
            service.getTrending(media_type, time_window, api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                        rView.adapter = TrendingAdapter(this, it.body()!!.results)
                    }
                }
            })
        } else if(type == "Now Playing") {
            service.getNowPlaying(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
        } else if(type == "Upcoming") {
            service.getUpcoming(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
        } else if(type == "Popular") {
            service.getPopular(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
        } else if(type == "Top Rated") {
            service.getTopRated(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
        } else if(type == "TV Airing Today") {
            service.getTVAiringTodayF(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
        } else if(type == "TV On Air") {
            service.getTVonAirF(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
        } else if(type == "TV Popular") {
            service.getTVPopularF(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
        } else if(type == "TV Top Rated") {
            service.getTVTopRatedF(api_key, trackN.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
        } else if(type == "TVCast") {
            lastLayout.isVisible = false
            val id = intent.getStringExtra("id").toInt()
            service.getTVCast(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = PeopleAdapter(this, it.body()!!.cast, false)
                    }
                }
            })
        } else if(type == "Cast") {
            lastLayout.isVisible = false
            val id = intent.getStringExtra("id").toInt()
            service.getCast(id,api_key).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView.adapter = PeopleAdapter(this, it.body()!!.cast, false)
                    }
                }
            })
        }
    }
}
