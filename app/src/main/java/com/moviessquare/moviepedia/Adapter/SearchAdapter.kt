package com.moviessquare.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.moviessquare.moviepedia.*
import com.moviessquare.moviepedia.DataClass.Search_result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_9.view.*

class SearchAdapter(val context: Context, val nameList: ArrayList<Search_result>) :
    RecyclerView.Adapter<SearchAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_9, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
            return nameList.size
    }

    override fun onBindViewHolder(holder: SearchAdapter.NameViewHolder, position: Int) {
        if(nameList[position].original_title!=null) {
            holder.itemView.tView.text = nameList[position].original_title
        } else {
            holder.itemView.tView.text = nameList[position].name
        }
        var target : String
        if(nameList[position].poster_path!=null) {
            target = nameList[position].poster_path
            Picasso.with(this.context).load(baseURL + target).fit().into(holder.itemView.iView)
        } else if(nameList[position].profile_path != null){
            target = nameList[position].profile_path
            Picasso.with(this.context).load(baseURL + target).fit().into(holder.itemView.iView)
        } else {
            Picasso.with(this.context).load(R.drawable.baseline_broken_image_black_18dp).into(holder.itemView.iView)
        }
        holder.itemView.parentLayout.setOnClickListener {
            if(nameList[position].media_type == "movie") {
                var intent = Intent(context, ThirdAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            } else if(nameList[position].media_type == "person") {
                var intent = Intent(context, FourthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            } else if(nameList[position].media_type == "tv") {
                var intent = Intent(context, FifthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}