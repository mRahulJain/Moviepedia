package com.example.moviepedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.Adapter.CommonAdapter
import com.example.moviepedia.Adapter.PhotoAdapter
import com.example.moviepedia.Api.API
import com.example.moviepedia.DataClass.PeoplePhoto
import com.example.moviepedia.DataClass.PeopleWork
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.item_1.view.*
import kotlinx.android.synthetic.main.item_3.view.*
import kotlinx.android.synthetic.main.item_3.view.iViewType
import kotlinx.android.synthetic.main.item_4.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoAct : AppCompatActivity() {

    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        val id = intent.getStringExtra("id").toInt()
        Log.d("CHECKid", "$id")

        val photoService = retrofit.create(API::class.java)
        photoService.getPhoto(id,api_key).enqueue(retrofitCallback{ throwable, response ->
            response?.let {
                if(it.isSuccessful) {
                    rViewPhotos.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
                    rViewPhotos.adapter = PhotoAdapter(this, it.body()!!)
                }
            }
        })
    }
}
