package com.moviessquare.moviepedia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviessquare.moviepedia.R

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