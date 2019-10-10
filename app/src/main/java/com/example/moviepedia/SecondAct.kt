package com.example.moviepedia

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.ReviewAdapter
import com.example.moviepedia.Adapter.TVAdapter
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.Common_results
import com.example.moviepedia.DataClass.TV_details
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
    var isScrolling : Boolean = false
    var currentItems : Int = 0
    var totalItems : Int = 0
    var scrolledOutItems : Int = 0
    var currentPage : Int = 1
    lateinit var layoutManager: RecyclerView.LayoutManager
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var list : ArrayList<Trending_results>
    lateinit var commonList : ArrayList<Common_results>
    lateinit var TVList : ArrayList<TV_details>
    lateinit var ReviewList : ArrayList<reviews>
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        type = intent.getStringExtra("type")
        textDest.text = type

        pBar.isVisible = true

        toBeCalled()

        rView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = rView.layoutManager!!
                currentItems = layoutManager!!.childCount
                totalItems = layoutManager!!.itemCount
                when(layoutManager) {
                    is GridLayoutManager -> gridLayoutManager = layoutManager as GridLayoutManager
                }
                scrolledOutItems = gridLayoutManager!!.findFirstVisibleItemPosition()

                if((scrolledOutItems + currentItems == totalItems) && isScrolling) {
                    currentPage++
                    isScrolling = false
                    toBeCalled()
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })
    }

    @SuppressLint("WrongConstant")
    fun toBeCalled() {
        val service = retrofit.create(API::class.java)
        if(type=="Trending") {
            service.getTrending(media_type, time_window, api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            list = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = TrendingAdapter(this, list)
                        } else {
                            list.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "Now Playing") {
            service.getNowPlaying(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            commonList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = CommonAdapter(this, commonList, false)
                        } else {
                            commonList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "Upcoming") {
            service.getUpcoming(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            commonList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = CommonAdapter(this, commonList, false)
                        } else {
                            commonList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "Popular") {
            service.getPopular(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            commonList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = CommonAdapter(this, commonList, false)
                        } else {
                            commonList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "Top Rated") {
            service.getTopRated(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            commonList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = CommonAdapter(this, commonList, false)
                        } else {
                            commonList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "TV Airing Today") {
            service.getTVAiringTodayF(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            TVList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = TVAdapter(this, TVList)
                        } else {
                            TVList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "TV On Air") {
            service.getTVonAirF(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            TVList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = TVAdapter(this, TVList)
                        } else {
                            TVList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "TV Popular") {
            service.getTVPopularF(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            TVList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = TVAdapter(this, TVList)
                        } else {
                            TVList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "TV Top Rated") {
            service.getTVTopRatedF(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            TVList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                            rView.adapter = TVAdapter(this, TVList)
                        } else {
                            TVList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        } else if(type == "TVReview") {
            val id = intent.getStringExtra("id").toInt()
            service.getReviewTV(id,api_key, currentPage).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(i==0) {
                        pBar.isVisible = false
                        ReviewList = it.body()!!.results
                        rView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                        rView.adapter = ReviewAdapter(this, ReviewList)
                    } else {
                        ReviewList.addAll(it.body()!!.results)
                        rView.adapter!!.notifyDataSetChanged()
                    }
                    i++
                }
            })
        } else if(type == "Review") {
            val id = intent.getStringExtra("id").toInt()
            service.getReview(id,api_key, currentPage).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBar.isVisible = false
                        if(i==0) {
                            ReviewList = it.body()!!.results
                            rView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                            rView.adapter = ReviewAdapter(this, ReviewList)
                        } else {
                            ReviewList.addAll(it.body()!!.results)
                            rView.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
        }
    }
}
