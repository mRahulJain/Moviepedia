package com.moviessquare.moviepedia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moviessquare.moviepedia.R
import com.moviessquare.moviepedia.reviews
import kotlinx.android.synthetic.main.item_1.view.*

class ReviewAdapter(val context: Context, val nameList: ArrayList<reviews>) :
    RecyclerView.Adapter<ReviewAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_2, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    override fun onBindViewHolder(holder: ReviewAdapter.NameViewHolder, position: Int) {
        holder.itemView.tView.text = nameList[position].author
        holder.itemView.tView.text = holder.itemView.tView.text.toString() + " : "+ nameList[position].content
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}