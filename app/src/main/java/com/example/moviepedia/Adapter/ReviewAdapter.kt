package com.example.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.Common_results
import com.example.moviepedia.R
import com.example.moviepedia.ThirdAct
import com.example.moviepedia.reviews
import com.squareup.picasso.Picasso
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