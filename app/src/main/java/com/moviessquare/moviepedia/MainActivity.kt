package com.moviessquare.moviepedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.moviessquare.moviepedia.Adapter.*
import com.moviessquare.moviepedia.Api.*
import com.moviessquare.moviepedia.DataClass.Common_results
import com.moviessquare.moviepedia.DataClass.People_results
import com.moviessquare.moviepedia.DataClass.TV_details
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
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
    var trial : Int = 0
    val KEY_APP_OPEN = "app_open"
    var isScrolling : Boolean = false
    var currentItems : Int = 0
    var totalItems : Int = 0
    var scrolledOutItems : Int = 0
    var currentPage : Int = 1
    lateinit var layoutManager: RecyclerView.LayoutManager
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var PeopleList : ArrayList<People_results>
    lateinit var FavMovieList : ArrayList<Common_results>
    lateinit var FavTVList : ArrayList<TV_details>
    lateinit var WatchMovieList : ArrayList<Common_results>
    lateinit var WatchTVList : ArrayList<TV_details>

    var i = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if(!isConnected(this@MainActivity)) {
            buildDialog(this@MainActivity).show()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        onOpen()

        val prefs = getPreferences(Context.MODE_PRIVATE)
        trial = prefs.getInt(KEY_APP_OPEN, 0)

        val userPresent = db.UsersDao().getUser()
        userPresentA = userPresent
        if(userPresent!=null) {
            trial = 0
            prefs.edit {
                putInt(KEY_APP_OPEN, trial)
            }
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
            val hView = nav_view.getHeaderView(0)
            val textViewName = hView.findViewById(R.id.usernamePreview) as TextView
            val mode = intent.getStringExtra("mode")
            if(mode!=null) {
                trial = 1
                prefs.edit {
                    putInt(KEY_APP_OPEN, trial)
                }
                textViewName.setText("FREE TRIAL VERSION")
            } else {
                if(trial == 1) {
                    textViewName.setText("FREE TRIAL VERSION")
                } else {
                    val intent = Intent(this, LoginAct::class.java)
                    intent.putExtra("trialCheck", "not still")
                    startActivity(intent)
                    finish()
                }
            }
        }

        rView1.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
        rView1.adapter = LoadingAdapter(this)
        rView2.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
        rView2.adapter = LoadingAdapter(this)
        rView3.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
        rView3.adapter = LoadingAdapter(this)
        rView4.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
        rView4.adapter = LoadingAdapter(this)
        rView5.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
        rView5.adapter = LoadingAdapter(this)

        rView1.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = rView1.layoutManager!!
                currentItems = layoutManager!!.childCount
                totalItems = layoutManager!!.itemCount
                when(layoutManager) {
                    is GridLayoutManager -> gridLayoutManager = layoutManager as GridLayoutManager
                }
                scrolledOutItems = gridLayoutManager!!.findFirstVisibleItemPosition()

                if((scrolledOutItems + currentItems == totalItems) && isScrolling) {
                    currentPage++
                    isScrolling = false
                    if(type=="People") {
                        onOpen1()
                    } else if(type == "Movies") {
                        onOpen3()
                    } else if(type =="FavTV") {
                        onOpen4()
                    } else if(type == "MovieWatch") {
                        onOpen5()
                    } else if(type == "TVWatch") {
                        onOpen6()
                    } else if(type == "Rated Movie") {
                        onOpen7()
                    } else if(type == "Rated TV") {
                        onOpen8()
                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        click1.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Airing Today")
            } else {
                intent.putExtra("type", "Trending")
            }
            startActivity(intent)
        }
        click2.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV On Air")
            } else {
                intent.putExtra("type", "Now Playing")
            }
            startActivity(intent)
        }
        click3.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Popular")
            } else {
                intent.putExtra("type", "Upcoming")
            }
            startActivity(intent)
        }
        click4.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            if(bool == true) {
                intent.putExtra("type", "TV Top Rated")
            } else {
                intent.putExtra("type", "Popular")
            }
            startActivity(intent)
        }
        click5.setOnClickListener {
            var intent = Intent(this, SecondAct::class.java)
            intent.putExtra("type", "Top Rated")
            startActivity(intent)
        }
    }

    fun onOpen() {
        val service = retrofit.create(API::class.java)

        service.getTrending(media_type, time_window, api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    pBarMain.isVisible = false
                    rView1.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView1.adapter = TrendingAdapter(this, it.body()!!.results)
                }
            }
        })

        service.getNowPlaying(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView2.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView2.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getUpcoming(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView3.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView3.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getPopular(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rView4.layoutManager = GridLayoutManager(this, 1,GridLayoutManager.HORIZONTAL, false)
                    rView4.adapter = CommonAdapter(this, it.body()!!.results, false)
                }
            }
        })

        service.getTopRated(api_key, currentPage.toString()).enqueue(retrofitCallback{ throwable, response ->
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

        service.getPopularPeople(api_key, currentPage).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    pBarMain.isVisible = false
                    if(i==0) {
                        PeopleList = it.body()!!.results
                        rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                        rView1.adapter = PeopleAdapter2(this, PeopleList, false)
                    } else {
                        PeopleList.addAll(it.body()!!.results)
                        rView1.adapter!!.notifyDataSetChanged()
                    }
                    i++
                }
            }
        })

    }

    @SuppressLint("WrongConstant")
    fun onOpen3() {
        val serviceFav = retrofit.create(API::class.java)
        serviceFav.getFavouriteMovie(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        pBarMain.isVisible = false
                        if(i==0) {
                            if(it.body()!!.results.size == 0) {
                                emptyList.isVisible = true
                            }
                            FavMovieList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = CommonAdapter2(this, FavMovieList, false)
                        } else {
                            FavMovieList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen4() {
        val serviceFav = retrofit.create(API::class.java)
        serviceFav.getFavouriteTV(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            emptyList.isVisible = true
                        }
                        pBarMain.isVisible = false
                        if(i==0) {
                            FavTVList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = TVAdapter2(this, FavTVList)
                        } else {
                            FavTVList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen5() {
        val serviceWatchMovie = retrofit.create(API::class.java)
        serviceWatchMovie.getMovieWatchlist(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            emptyList.isVisible = true
                        }
                        pBarMain.isVisible = false
                        if(i==0) {
                            FavMovieList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = CommonAdapter2(this, FavMovieList, false)
                        } else {
                            FavMovieList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen6() {
        val serviceTVWatchlist = retrofit.create(API::class.java)
        serviceTVWatchlist.getTVWatchlist(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            emptyList.isVisible = true
                        }
                        pBarMain.isVisible = false
                        if(i==0) {
                            FavTVList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = TVAdapter2(this, FavTVList)
                        } else {
                            FavTVList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen7() {
        val serviceRatedMovie = retrofit.create(API::class.java)
        serviceRatedMovie.getRatedMovie(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            emptyList.isVisible = true
                        }
                        pBarMain.isVisible = false
                        if(i==0) {
                            WatchMovieList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = RatedMovieAdapter(this, WatchMovieList, false, userPresentA.session_id)
                        } else {
                            WatchMovieList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    @SuppressLint("WrongConstant")
    fun onOpen8() {
        val serviceRatedMovie = retrofit.create(API::class.java)
        serviceRatedMovie.getRatedTV(AccountID, api_key, userPresentA.session_id, currentPage)
            .enqueue(retrofitCallback { throwable, response ->
                response?.let {
                    if(it.isSuccessful) {
                        if(it.body()!!.results.size == 0) {
                            emptyList.isVisible = true
                        }
                        pBarMain.isVisible = false
                        if(i==0) {
                            WatchTVList = it.body()!!.results
                            rView1.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
                            rView1.adapter = TVRatedAdapter(this, WatchTVList, userPresentA.session_id)
                        } else {
                            WatchTVList.addAll(it.body()!!.results)
                            rView1.adapter!!.notifyDataSetChanged()
                        }
                        i++
                    }
                }
            })
    }

    fun onOpen2() {
        val service = retrofit.create(API::class.java)

        service.getTVAiringToday(api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    pBarMain.isVisible = false
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
        } else if(type == "Movie") {
            super.onBackPressed()
        } else {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            navView.setCheckedItem(R.id.nav_movie)
            navView.setNavigationItemSelectedListener(this)
            pBarMain.isVisible = true
            emptyList.isVisible = false
            type = "Movie"
            bool = false
            btn5.isVisible = true
            btn4.isVisible = true
            btn3.isVisible = true
            btn2.isVisible = true
            click1.isVisible = true
            click2.isVisible = true
            click3.isVisible = true
            click4.isVisible = true
            click5.isVisible = true
            rView5.isVisible = true
            rView4.isVisible = true
            rView3.isVisible = true
            rView2.isVisible = true
            btn1.text = "  Trending"
            btn2.text = "  Now Playing"
            btn3.text = "  Upcoming"
            btn4.text = "  Popular"
            btn5.text = "  Top Rated"
            onOpen()
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
//                eTSearch.setText("")
//                layoutSearch.isVisible = !layoutSearch.isVisible
                val intent = Intent(this, SearchAct::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    fun isConnected(context: Context) : Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo

        if(netInfo != null && netInfo.isConnectedOrConnecting) {
            val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return (mobile != null && mobile.isConnectedOrConnecting) || (wifi != null && wifi.isConnectedOrConnecting)
        } else {
            return false
        }
    }

    fun buildDialog(context: Context) : AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("No internet connectivity")
        builder.setMessage("You need to be connected with mobile data or wifi to access information in this app." +
                "Press ok to Exit!")
        builder.setPositiveButton("Ok") { dialogInterface, which ->
            finish()
        }
        return builder
    }

    @SuppressLint("WrongConstant")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_movie -> {
                emptyList.isVisible = false
                pBarMain.isVisible = true
                type = "Movie"
                bool = false
                btn5.isVisible = true
                btn4.isVisible = true
                btn3.isVisible = true
                btn2.isVisible = true
                click1.isVisible = true
                click2.isVisible = true
                click3.isVisible = true
                click4.isVisible = true
                click5.isVisible = true
                rView5.isVisible = true
                rView4.isVisible = true
                rView3.isVisible = true
                rView2.isVisible = true
                btn1.text = "  Trending"
                btn2.text = "  Now Playing"
                btn3.text = "  Upcoming"
                btn4.text = "  Popular"
                btn5.text = "  Top Rated"
                onOpen()
            }
            R.id.nav_people -> {
                emptyList.isVisible = false
                pBarMain.isVisible = true
                i = 0
                currentPage = 1
                type = "People"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Popular People"
                onOpen1()
            }
            R.id.nav_tv -> {
                emptyList.isVisible = false
                pBarMain.isVisible = true
                i = 0
                currentPage = 1
                type = "TV"
                bool = true
                btn5.isVisible = false
                click1.isVisible = true
                click2.isVisible = true
                click3.isVisible = true
                click4.isVisible = true
                click5.isVisible = false
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
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "Favorite"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Favorite Movies"
                onOpen3()
            }
            R.id.nav_fav_TV -> {
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "FavTV"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Favorite TV Shows"
                onOpen4()
            }
            R.id.nav_watchList_Movies -> {
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "MovieWatch"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Movie Watchlist"
                onOpen5()
            }
            R.id.nav_watchList_TV -> {
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "TVWatch"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  TV Watchlist"
                onOpen6()
            }
            R.id.nav_account -> {
                if(trial == 1) {
                    val intent = Intent(this, LoginAct::class.java)
                    intent.putExtra("trialCheck", "already")
                    startActivity(intent)
                } else {
                    val intent = Intent(this, AccountAct::class.java)
                    intent.putExtra("name", "${userPresentA.name}")
                    intent.putExtra("username", "${userPresentA.username}")
                    intent.putExtra("password", "${userPresentA.password}")
                    startActivity(intent)
                    finish()
                }
            }
            R.id.nav_rated_movie -> {
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "Rated Movie"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Rated Movie"
                onOpen7()
            }
            R.id.nav_rated_tv -> {
                emptyList.isVisible = false
                i = 0
                currentPage = 1
                if(trial == 1) {
                    Snackbar.make(nav_view,
                        "Login required",
                        Snackbar.LENGTH_LONG)
                        .show()
                    return true
                }
                pBarMain.isVisible = true
                type = "Rated TV"
                btn5.isVisible = false
                btn4.isVisible = false
                btn3.isVisible = false
                btn2.isVisible = false
                click1.isVisible = false
                click2.isVisible = false
                click3.isVisible = false
                click4.isVisible = false
                click5.isVisible = false
                rView5.isVisible = false
                rView4.isVisible = false
                rView3.isVisible = false
                rView2.isVisible = false
                btn1.text = "  Rated TV"
                onOpen8()
            }

            R.id.nav_rateApp -> {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.moviessquare.moviepedia")
                startActivity(intent)
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
                Picasso.with(this.context).load(baseURL + target).fit().into(holder.itemView.iView)

                holder.itemView.deletS.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Are you sure ?")
                        .setTitle("${nameList[position].original_title}")
                        .setPositiveButton("Delete"){dialogInterface, which ->
                            db3.RatedDao().deletRated(nameList[position].id)
                            val serviceDeleteRatedMovie = retrofit.create(API::class.java)
                            serviceDeleteRatedMovie.deleteRated(nameList[position].id, "application/json;charset=utf-8", api_key, session_id)
                                .enqueue(retrofitCallback{ throwable, response ->
                                    response?.let {
                                        if(it.isSuccessful) {
                                            i = 0
                                            onOpen7()
                                        }
                                    }
                                })
                        }
                        .setNegativeButton("Cancel"){dialogInterface, which ->
                            null
                        }
                    val alertDialog = builder.create()
                    alertDialog.show()
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
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure ?")
                    .setTitle("${nameList[position].name}")
                    .setPositiveButton("Delete"){dialogInterface, which ->
                        db3.RatedDao().deletRated(nameList[position].id)
                        val serviceDeleteRatedMovie = retrofit.create(API::class.java)
                        serviceDeleteRatedMovie.deletRatedTV(nameList[position].id, "application/json;charset=utf-8", api_key, session_id)
                            .enqueue(retrofitCallback{ throwable, response ->
                                response?.let {
                                    if(it.isSuccessful) {
                                        i = 0
                                        onOpen8()
                                    }
                                }
                            })
                    }
                    .setNegativeButton("Cancel"){dialogInterface, which ->
                        null
                    }
                val alertDialog = builder.create()
                alertDialog.show()
            }

            holder.itemView.iView.setOnClickListener {
                var intent = Intent(context, FifthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            }
        }


        inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
