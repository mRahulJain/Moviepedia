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
import com.example.moviepedia.*
import com.example.moviepedia.DataClass.Common_results
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*
import kotlinx.android.synthetic.main.item_genre.view.*

class GenreAdapter(val context: Context, val nameList: ArrayList<Genre>) : RecyclerView.Adapter<GenreAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_genre, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    override fun onBindViewHolder(holder: GenreAdapter.NameViewHolder, position: Int) {
        holder.itemView.tViewGenre.text = nameList[position].name
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}