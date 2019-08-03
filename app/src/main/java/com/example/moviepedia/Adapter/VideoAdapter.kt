package com.example.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.Video
import com.example.moviepedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_5.view.*

class VideoAdapter(val context: Context, val nameList: Video) : RecyclerView.Adapter<VideoAdapter.NameViewHolder>() {

    var baseURL = "https://www.youtube.com/watch?v="

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_5, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nameList.results.size
    }

    override fun onBindViewHolder(holder: VideoAdapter.NameViewHolder, position: Int) {
        var url = "https://img.youtube.com/vi/${nameList.results[position].key}/0.jpg"
        holder.itemView.tVvideoName.text = nameList.results[position].name
        Picasso.with(context).load(url).fit().into(holder.itemView.iViewVid)
        holder.itemView.pL.setOnClickListener {
            var i = Intent()
            i.action = Intent.ACTION_VIEW
            i.data = Uri.parse(baseURL + nameList.results[position].key)
            startActivity(context, i, null)
        }
    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}