package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviepedia.Adapter.GetWorkAdapter
import com.example.moviepedia.Adapter.PhotoAdapter
import com.example.moviepedia.Api.API
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fourth.*
import kotlinx.android.synthetic.main.content_scrolling_2.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FourthAct : AppCompatActivity() {

    val baseURL = "https://image.tmdb.org/t/p/original/"
    val api_key: String = "<api_key>"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        setSupportActionBar(toolbar)
        tVbio.movementMethod = ScrollingMovementMethod() as MovementMethod?

        collapseToolBar.title = "Loading..."
        val id = intent.getStringExtra("id").toInt()
        val service = retrofit.create(API::class.java)

        service.getPeople(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    just1.text = "Also known as :"
                    just2.text = "Known for department :"
                    just3.text = "Work :"
                    collapseToolBar.title = it.body()!!.name
                    Picasso
                        .with(this)
                        .load(baseURL + it.body()!!.profile_path)
                        .resize(450,600)
                        .into(iView)
                    tVbio.text = it.body()!!.biography
                    tVaka.text = ""
                    for(i in it.body()!!.also_known_as) {
                        tVaka.text = tVaka.text.toString() + i + ", "
                    }
                    tVpopularity.setTextColor(Color.CYAN)
                    tVpopularity.text = it.body()!!.popularity + " / 100"
                    tVkfd.text = it.body()!!.known_for_department
                    tVkfd.setTextColor(Color.CYAN)
                }
            }
        })

        val photoService = retrofit.create(API::class.java)
        photoService.getPhoto(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewPhotos.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    rViewPhotos.adapter = PhotoAdapter(this, it.body()!!)
                }
            }
        })
        val getWorkService = retrofit.create(API::class.java)
        getWorkService.getPeopleWork(id, api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewWork.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    rViewWork.adapter = GetWorkAdapter(this, it.body()!!)
                }
            }
        })

    }
}
