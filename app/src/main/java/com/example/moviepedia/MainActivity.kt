package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Intent
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

        lastLayout.isVisible = false
        forward.setOnClickListener {
            track.text = (track.text.toString().toInt() + 1).toString()
            onOpen1()
        }
        backward.setOnClickListener {
            track.text = (track.text.toString().toInt() - 1).toString()
            onOpen1()
        }

        btn1.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Trending")
            startActivity(intent)
        }
        btn2.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Now Playing")
            startActivity(intent)
        }
        btn3.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Upcoming")
            startActivity(intent)
        }
        btn4.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Popular")
            startActivity(intent)
        }
        btn5.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Top Rated")
            startActivity(intent)
        }
    }

    fun onOpen() {
        val service = retrofit.create(API::class.java)

        service.getTrending(media_type, time_window, api_key, track.text.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView1.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView1.adapter = TrendingAdapter(this, it.body()!!.results)
                }
            }
        })

        service.getNowPlaying(api_key, track.text.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView2.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView2.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getUpcoming(api_key, track.text.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView3.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView3.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getPopular(api_key, track.text.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView4.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView4.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getTopRated(api_key, track.text.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView5.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView5.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })
    }

    @SuppressLint("WrongConstant")
    fun onOpen1() {
        val service = retrofit.create(API::class.java)

        service.getPopularPeople(api_key, track.text.toString().toInt()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                    rView1.adapter = PeopleAdapter(this, it.body()!!, false)
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
            R.id.nav_movie -> {
                lastLayout.isVisible = false
                btn5.isVisible = true
                btn4.isVisible = true
                btn3.isVisible = true
                btn2.isVisible = true
                rView5.isVisible = true
                rView4.isVisible = true
                rView3.isVisible = true
                rView2.isVisible = true
                btn1.text = "  Trending"
                onOpen()
            }
            R.id.nav_people -> {
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Popular People"
                onOpen1()
            }
            R.id.nav_tv -> {

            }
            R.id.nav_fav -> {

            }
            R.id.nav_watchList -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
