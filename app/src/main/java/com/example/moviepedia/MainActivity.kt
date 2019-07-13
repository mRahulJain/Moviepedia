package com.example.moviepedia

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.PeopleAdapter
import com.example.moviepedia.Api.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val media_type = "all"
    val time_window = "week"
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        onOpen()
    }

    fun onOpen() {
        val serviceTrending = retrofit.create(TrendingAPI::class.java)

        serviceTrending.getTrending(media_type, time_window, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewT.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewT.adapter = TrendingAdapter(this, it.body()!!.results)
                }
            }
        })

        val serviceNowPlaying = retrofit.create(NowPlayingAPI::class.java)

        serviceNowPlaying.getNowPlaying(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewNP.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewNP.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        val serviceUpcoming = retrofit.create(UpcomingAPI::class.java)

        serviceUpcoming.getUpcoming(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewU.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewU.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        val servicePopular = retrofit.create(PopularAPI::class.java)

        servicePopular.getPopular(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewP.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewP.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        val serviceTopRated = retrofit.create(TopRatedAPI::class.java)

        serviceTopRated.getTopRated(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewTR.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rViewTR.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })
    }

    @SuppressLint("WrongConstant")
    fun onOpen1() {
        val servicePeople = retrofit.create(PopularPeopleAPI::class.java)

        servicePeople.getPopularPeople(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewT.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                    rViewT.adapter = PeopleAdapter(this, it.body()!!.results, false)
                }
            }
        })

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                lLayout5.isVisible = true
                lLayout4.isVisible = true
                lLayout3.isVisible = true
                lLayout2.isVisible = true
                rViewTR.isVisible = true
                rViewP.isVisible = true
                rViewU.isVisible = true
                rViewNP.isVisible = true
                view1.text = "  Trending"
                onOpen()
            }
            R.id.nav_people -> {
                lLayout5.isVisible = false
                lLayout4.isVisible = false
                lLayout3.isVisible = false
                lLayout2.isVisible = false
                rViewTR.isVisible = false
                rViewP.isVisible = false
                rViewU.isVisible = false
                rViewNP.isVisible = false
                view1.text = "  Popular People"
                onOpen1()
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
