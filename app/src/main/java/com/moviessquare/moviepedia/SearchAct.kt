package com.moviessquare.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.moviessquare.moviepedia.Adapter.SearchAdapter
import com.moviessquare.moviepedia.Api.API
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchAct : AppCompatActivity() {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api_key: String = "40c1d09ce2457ccd5cabde67ee04c652"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbarSearch)
        supportActionBar!!.title = ""

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        MobileAds.initialize(this, "ca-app-pub-6222464247565126~5207993837")
        val adRequest = AdRequest.Builder().build()
        adView3.loadAd(adRequest)

        textSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val serviceSearch = retrofit.create(API::class.java)
                serviceSearch.getSearch(api_key, p0.toString()).enqueue(retrofitCallback{ throwable, response ->
                    response?.let {
                        if(it.isSuccessful) {
                            noSearchList.isVisible = it.body()!!.results.size == 0

                            rViewSearch.layoutManager = GridLayoutManager(this@SearchAct, 2,
                                GridLayoutManager.VERTICAL, false)
                            rViewSearch.adapter = SearchAdapter(this@SearchAct, it.body()!!.results)
                        }
                    }
                })
            }

        })

        textSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_SEARCH) {
                val serviceSearch = retrofit.create(API::class.java)
                serviceSearch.getSearch(api_key, textSearch.text.toString()).enqueue(retrofitCallback{ throwable, response ->
                    response?.let {
                        if(it.isSuccessful) {
                            noSearchList.isVisible = it.body()!!.results.size == 0

                            rViewSearch.layoutManager = GridLayoutManager(this@SearchAct, 2,
                                GridLayoutManager.VERTICAL, false)
                            rViewSearch.adapter = SearchAdapter(this@SearchAct, it.body()!!.results)
                        }
                    }
                })
            }
            true
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
