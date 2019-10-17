package com.moviessquare.moviepedia

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.moviessquare.moviepedia.Adapter.GetWorkAdapter
import com.moviessquare.moviepedia.Adapter.PhotoAdapter
import com.moviessquare.moviepedia.Api.API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fourth.*
import kotlinx.android.synthetic.main.content_scrolling_2.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FourthAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/w500/"
    var biography : String = ""
    var flag = 0
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        MobileAds.initialize(this, "ca-app-pub-6222464247565126~5207993837")
        val adRequest = AdRequest.Builder().build()
        adView2.loadAd(adRequest)

        setSupportActionBar(toolbar)
        tVbio.movementMethod = ScrollingMovementMethod() as MovementMethod?

        collapseToolBar.title = "Loading..."

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val id = intent.getStringExtra("id").toInt()
        val service = retrofit.create(API::class.java)

        service.getPeople(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    collapseToolBar.title = it.body()!!.name
                    Picasso
                        .with(this)
                        .load(baseURL + it.body()!!.profile_path)
                        .fit()
                        .into(iView)
                    if(it.body()!!.profile_path == null) {
                        Picasso.with(this).load(R.drawable.baseline_broken_image_white_18dp).into(iView)
                    }
                    if(it.body()!!.biography == null) {
                        expand_button_2.isVisible = false
                        tVbio.isVisible = false
                    } else {
                        biography = it.body()!!.biography
                        tVbio.text = it.body()!!.biography
                        if(tVbio.lineCount <= 4) {
                            expand_button_2.isVisible = false
                        } else {
                            expand_button_2.setImageResource(R.drawable.ic_expand_more)
                        }
                    }
                    if(it.body()!!.also_known_as == null || it.body()!!.also_known_as.size == 0) {
                        just1.isVisible = false
                        tVaka.isVisible = false
                    } else {
                        just1.text = "Also known as"
                        tVaka.text = ""
                        for(i in it.body()!!.also_known_as) {
                            tVaka.text = tVaka.text.toString() + i + ", "
                        }
                        tVaka.text = tVaka.text.toString().substring(0, tVaka.text.toString().lastIndex-2)
                    }
                    if(it.body()!!.popularity == null) {
                        tVpopularity.isVisible = false
                    } else {
                        tVpopularity.setTextColor(Color.CYAN)
                        tVpopularity.text = it.body()!!.popularity
                    }
                    if(it.body()!!.known_for_department == null) {
                        just2.isVisible = false
                        tVkfd.isVisible = false
                    } else {
                        just2.text = "Known for department"
                        tVkfd.text = it.body()!!.known_for_department
                        tVkfd.setTextColor(Color.CYAN)
                    }
                }
            }
        })

        val photoService = retrofit.create(API::class.java)
        photoService.getPhoto(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.profiles.size == 0) {
                        rViewPhotos.isVisible = false
                        tVmp.isVisible = false
                    } else {
                        tVmp.text = "More Pictures"
                        rViewPhotos.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        rViewPhotos.adapter = PhotoAdapter(this, it.body()!!)
                    }
                }
            }
        })
        val getWorkService = retrofit.create(API::class.java)
        getWorkService.getPeopleWork(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    if(it.body()!!.cast.size == 0) {
                        just3.isVisible = false
                        rViewWork.isVisible = false
                    } else {
                        just3.text = "Work"
                        rViewWork.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                        rViewWork.adapter = GetWorkAdapter(this, it.body()!!)
                    }
                }
            }
        })

        expand_button_2.setOnClickListener {
            if(flag == 0) {
                flag = 1
                expand_button_2.setImageResource(R.drawable.ic_expand_less)
                tVbio.isVisible = false
                tVbioF.isVisible = true
                tVbioF.setText(biography)
            } else {
                flag = 0
                expand_button_2.setImageResource(R.drawable.ic_expand_more)
                tVbio.isVisible = true
                tVbioF.isVisible = false
                tVbio.setText(biography)
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
