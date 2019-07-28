package com.example.moviepedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        tool.setTitle("Moviepedia")

        val reqToken = intent.getStringExtra("reqToken")
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.themoviedb.org/authenticate/${reqToken}/allow")
    }

    override fun onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }

    }
}
