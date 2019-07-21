package com.example.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moviepedia.AppDatabase
import com.example.moviepedia.DataClass.Common_results
import com.example.moviepedia.FavDatabase
import com.example.moviepedia.R
import com.example.moviepedia.ThirdAct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*

class LoadingAdapter(val context: Context) :
    RecyclerView.Adapter<LoadingAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_8, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: LoadingAdapter.NameViewHolder, position: Int) {
//            Picasso.with(this.context).load(R.drawable.loading).into(holder.itemView.iView)
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}