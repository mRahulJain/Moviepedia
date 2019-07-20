package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moviepedia.Adapter.*
import com.example.moviepedia.Api.*
import com.example.moviepedia.DataClass.Common_results
import com.example.moviepedia.DataClass.TV_details
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_1.view.*
import kotlinx.android.synthetic.main.item_6.view.*
import kotlinx.android.synthetic.main.item_6.view.iView
import kotlinx.android.synthetic.main.item_6.view.parentLayout
import kotlinx.android.synthetic.main.item_6.view.tView
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
    var bool : Boolean = false
    var type : String = "Movie"

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "Users.db"
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
    var bool1 = false
    lateinit var name : String
    lateinit var userPresentA : Users
    lateinit var AccountID : String

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

        val userPresent = db.UsersDao().getUser()
        userPresentA = userPresent
        if(userPresent!=null) {
            name = userPresent.name
            Log.d("SESSION_ID", "${userPresent.session_id}")
            bool1 = true
//            usernamePreview.setText(userPresent.name)

            val hView = nav_view.getHeaderView(0)
            val textViewName = hView.findViewById(R.id.usernamePreview) as TextView
            textViewName.setText(userPresent.name)

            Log.d("SESSION_ID", "${userPresent.session_id}")
            val serviceAccount = retrofit.create(API::class.java)
            serviceAccount.getAccountDetail(api_key, "${userPresent.session_id}").enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                            AccountID = it.body()!!.id
                        Log.d("SESSION_ID", "${AccountID}")
                    }
                }
            })


        } else {
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
            finish()
        }

        layoutSearch.isVisible = false
        lastLayout.isVisible = false
        forward.setOnClickListener {
            track.text = (track.text.toString().toInt() + 1).toString()
            if(type=="People") {
                onOpen1()
            } else if(type == "Movies") {
                onOpen3()
            } else if(type =="TV") {
                onOpen4()
            } else if(type == "MovieWatch") {
                onOpen5()
            } else if(type == "TVWatch") {
                onOpen6()
            } else if(type == "Rated Movie") {
                onOpen7()
            }
        }
        backward.setOnClickListener {
            track.text = (track.text.toString().toInt() - 1).toString()
            if(type=="People") {
                onOpen1()
            } else if(type == "Movies") {
                onOpen3()
            } else if(type =="TV") {
                onOpen4()
            } else if(type == "MovieWatch") {
                onOpen5()
            } else if(type == "TVWatch") {
                onOpen6()
            } else if(type == "Rated Movie") {
                onOpen7()
            }
        }

        btn1.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Airing Today")
            } else {
                intent.putExtra("type", "Trending")
            }
            startActivity(intent)
        }
        btn2.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV On Air")
            } else {
                intent.putExtra("type", "Now Playing")
            }
            startActivity(intent)
        }
        btn3.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Popular")
            } else {
                intent.putExtra("type", "Upcoming")
            }
            startActivity(intent)
        }
        btn4.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Top Rated")
            } else {
                intent.putExtra("type", "Popular")
            }
            startActivity(intent)
        }
        btn5.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Top Rated")
            startActivity(intent)
        }

        backSearch.setOnClickListener {
            eTSearch.setText("")
            if(type == "Movie") {
                type = "Movie"
                layoutSearch.isVisible = false
                bool = false
                btn1.isClickable = true
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
            } else if(type == "People") {
                type = "People"
                layoutSearch.isVisible = false
                btn1.isClickable = false
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
            } else if(type == "TV") {
                type = "TV"
                layoutSearch.isVisible = false
                bool = true
                btn1.isClickable = true
                lastLayout.isVisible = false
                btn5.isVisible = false
                btn4.isVisible = true
                btn3.isVisible = true
                btn2.isVisible = true
                rView5.isVisible = false
                rView4.isVisible = true
                rView3.isVisible = true
                rView2.isVisible = true
                btn1.text = "  TV Airing Today"
                btn2.text = "  TV On Air"
                btn3.text = "  TV Popular"
                btn4.text = "  TV Top Rated"
                onOpen2()
            }
        }

        goSearch.setOnClickListener {
            val text = eTSearch.text.toString()
            if(text == "") {
                return@setOnClickListener
            }
            btn1.isVisible = false
            lastLayout.isVisible = false
            btn5.isVisible = false
            btn4.isVisible = false
            btn3.isVisible = false
            btn2.isVisible = false
            rView5.isVisible = false
            rView4.isVisible = false
            rView3.isVisible = false
            rView2.isVisible = false
            val serviceSearch = retrofit.create(API::class.java)
            serviceSearch.getSearch(api_key, text).enqueue(retrofitCallback{ throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = SearchAdapter(this, it.body()!!.results)
                    }
                }
            })
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
                    rView1.adapter = PeopleAdapter(this, it.body()!!.results, false)
                }
            }
        })

    }

    @SuppressLint("WrongConstant")
    fun onOpen3() {
        val serviceFav = retrofit.create(API::class.java)
        serviceFav.getFavouriteMovie(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen4() {
        val serviceFav = retrofit.create(API::class.java)
        serviceFav.getFavouriteTV(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen5() {
        val serviceWatchMovie = retrofit.create(API::class.java)
        serviceWatchMovie.getMovieWatchlist(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = CommonAdapter(this, it.body()!!.results, false)
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen6() {
        val serviceTVWatchlist = retrofit.create(API::class.java)
        serviceTVWatchlist.getTVWatchlist(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = TVAdapter(this, it.body()!!.results)
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen7() {
        val serviceRatedMovie = retrofit.create(API::class.java)
        serviceRatedMovie.getRatedMovie(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = RatedMovieAdapter(this, it.body()!!.results, false, userPresentA.session_id)
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen8() {
        val serviceRatedMovie = retrofit.create(API::class.java)
        serviceRatedMovie.getRatedTV(AccountID, api_key, userPresentA.session_id, track.text.toString().toInt())
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        rView1.layoutManager = GridLayoutManager(this, 2,GridLayoutManager.VERTICAL, false)
                        rView1.adapter = TVRatedAdapter(this, it.body()!!.results, userPresentA.session_id)
                    }
                }
            })
    }

    fun onOpen2() {
        val service = retrofit.create(API::class.java)

        service.getTVAiringToday(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView1.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView1.adapter = TVAdapter(this, it.body()!!.results)
                }
            }
        })

        service.getTVonAir(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView2.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView2.adapter = TVAdapter(this, it.body()!!.results)
                }
            }
        })

        service.getTVPopular(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView3.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView3.adapter = TVAdapter(this, it.body()!!.results)
                }
            }
        })

        service.getTVTopRated(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView4.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView4.adapter = TVAdapter(this, it.body()!!.results)
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
        when(item.itemId) {
            R.id.search -> {
                eTSearch.setText("")
                layoutSearch.isVisible = !layoutSearch.isVisible
            }
        }
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_movie -> {
                type = "Movie"
                layoutSearch.isVisible = false
                bool = false
                btn1.isClickable = true
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
                type = "People"
                layoutSearch.isVisible = false
                btn1.isClickable = false
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
                type = "TV"
                layoutSearch.isVisible = false
                bool = true
                btn1.isClickable = true
                lastLayout.isVisible = false
                btn5.isVisible = false
                btn4.isVisible = true
                btn3.isVisible = true
                btn2.isVisible = true
                rView5.isVisible = false
                rView4.isVisible = true
                rView3.isVisible = true
                rView2.isVisible = true
                btn1.text = "  TV Airing Today"
                btn2.text = "  TV On Air"
                btn3.text = "  TV Popular"
                btn4.text = "  TV Top Rated"
                onOpen2()
            }
            R.id.nav_fav_movies -> {
                type = "Favorite"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Favorite Movies"
                onOpen3()
            }
            R.id.nav_fav_TV -> {
                type = "TV"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Favorite TV Shows"
                onOpen4()
            }
            R.id.nav_watchList_Movies -> {
                type = "MovieWatch"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Movie Watchlist"
                onOpen5()
            }
            R.id.nav_watchList_TV -> {
                type = "TVWatch"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  TV Watchlist"
                onOpen6()
            }
            R.id.nav_account -> {
                val intent = Intent(this, AccountAct::class.java)
                intent.putExtra("name", "${userPresentA.name}")
                intent.putExtra("username", "${userPresentA.username}")
                intent.putExtra("password", "${userPresentA.password}")
                startActivity(intent)
            }
            R.id.nav_rated_movie -> {
                type = "Rated Movie"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Rated Movie"
                onOpen7()
            }
            R.id.nav_rated_tv -> {
                type = "Rated TV"
                layoutSearch.isVisible = false
                btn1.isClickable = false
                lastLayout.isVisible = true
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Rated TV"
                onOpen8()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    inner class RatedMovieAdapter(val context: Context, val nameList: ArrayList<Common_results>, val check : Boolean, val session_id : String) :
        RecyclerView.Adapter<RatedMovieAdapter.NameViewHolder>() {

        val baseURL = "https://image.tmdb.org/t/p/w342/"

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
            val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val itemView = li.inflate(R.layout.item_6, parent, false)
            return NameViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            if(check==false) {
                return nameList.size
            }
            return 0
        }

        override fun onBindViewHolder(holder: RatedMovieAdapter.NameViewHolder, position: Int) {
            if(check==false) {
                holder.itemView.rateView.text = nameList[position].rating + " / 10"
                holder.itemView.tView.text = nameList[position].original_title
                Log.d("CHECK", "${holder.itemView.tView.text}")
                val target = nameList[position].poster_path
                Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)

                holder.itemView.deletS.setOnClickListener {
                    db3.RatedDao().deletRated(nameList[position].id)
                    val serviceDeleteRatedMovie = retrofit.create(API::class.java)
                    serviceDeleteRatedMovie.deleteRated(nameList[position].id, "application/json;charset=utf-8", api_key, session_id)
                        .enqueue(retrofitCallback{ throwable, response ->
                            response?.let {
                                if(it.isSuccessful) {
                                    onOpen7()
                                }
                            }
                        })
                }

                holder.itemView.parentLayout.setOnClickListener {
                    var intent = Intent(context, ThirdAct::class.java)
                    intent.putExtra("id", nameList[position].id)
                    intent.putExtra("type", "Movie")
                    ContextCompat.startActivity(context, intent, null)
                }
            }
        }

        inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    inner class TVRatedAdapter(val context: Context, val nameList: ArrayList<TV_details>, val session_id : String) :
        RecyclerView.Adapter<TVRatedAdapter.NameViewHolder>() {

        val baseURL = "https://image.tmdb.org/t/p/w342/"

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
            val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val itemView = li.inflate(R.layout.item_6, parent, false)
            return NameViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return nameList.size
        }

        override fun onBindViewHolder(holder: TVRatedAdapter.NameViewHolder, position: Int) {
            holder.itemView.rateView.text = nameList[position].rating + " / 10"
            holder.itemView.tView.text = nameList[position].name
            Log.d("CHECK", "${holder.itemView.tView.text}")
            val target = nameList[position].poster_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)

            holder.itemView.deletS.setOnClickListener {
                db3.RatedDao().deletRated(nameList[position].id)
                val serviceDeleteRatedMovie = retrofit.create(API::class.java)
                serviceDeleteRatedMovie.deletRatedTV(nameList[position].id, "application/json;charset=utf-8", api_key, session_id)
                    .enqueue(retrofitCallback{ throwable, response ->
                        response?.let {
                            if(it.isSuccessful) {
                                onOpen8()
                            }
                        }
                    })
            }

            holder.itemView.parentLayout.setOnClickListener {
                var intent = Intent(context, FifthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            }
        }


        inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
