package com.example.moviepedia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.PeoplePhoto
import com.example.moviepedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_4.view.*

class PhotoAdapter(val context: Context, val nameList: PeoplePhoto) : RecyclerView.Adapter<PhotoAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_4, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nameList.profiles.size
    }

    override fun onBindViewHolder(holder: PhotoAdapter.NameViewHolder, position: Int) {
        Picasso
            .with(context)
            .load(baseURL + nameList.profiles[position].file_path)
            .into(holder.itemView.iViewPhoto)
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}