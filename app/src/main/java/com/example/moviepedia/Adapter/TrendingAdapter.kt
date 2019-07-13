package com.example.moviepedia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*
import java.security.AccessController.getContext

class TrendingAdapter(val context: Context,val nameList: ArrayList<Trending_results>) : RecyclerView.Adapter<TrendingAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_1, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    override fun onBindViewHolder(holder: TrendingAdapter.NameViewHolder, position: Int) {
        if(nameList[position].original_name!=null) {
            holder.itemView.tView.text = nameList[position].original_name
        } else {
            holder.itemView.tView.text = nameList[position].original_title
        }
        val target = nameList[position].poster_path
        Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}