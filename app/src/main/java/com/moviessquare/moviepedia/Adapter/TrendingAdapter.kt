package com.moviessquare.moviepedia

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*

class TrendingAdapter(val context: Context,val nameList: ArrayList<Trending_results>) :
    RecyclerView.Adapter<TrendingAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w185/"

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
        Picasso.with(this.context).load(baseURL + target).fit().into(holder.itemView.iView)
        if (target == null) {
            Picasso.with(this.context).load(R.drawable.baseline_broken_image_black_18dp).into(holder.itemView.iView)
        }

        holder.itemView.parentLayout.setOnClickListener {
            if(nameList[position]!!.media_type == "movie") {
                var intent = Intent(context, ThirdAct::class.java)
                intent.putExtra("id", nameList[position].id)
                intent.putExtra("type", "Movie")
                startActivity(context, intent, null)
            } else {
                Log.d("myCHECK", "TV SECTION")
                var intent = Intent(context, FifthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                startActivity(context, intent, null)
            }

        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}