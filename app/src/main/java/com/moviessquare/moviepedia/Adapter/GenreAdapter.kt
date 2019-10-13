package com.moviessquare.moviepedia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviessquare.moviepedia.*
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