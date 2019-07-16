package com.example.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.People
import com.example.moviepedia.DataClass.PeopleWork
import com.example.moviepedia.FourthAct
import com.example.moviepedia.R
import com.example.moviepedia.ThirdAct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*
import kotlinx.android.synthetic.main.item_1.view.iView
import kotlinx.android.synthetic.main.item_1.view.parentLayout
import kotlinx.android.synthetic.main.item_1.view.tView
import kotlinx.android.synthetic.main.item_3.view.*

class GetWorkAdapter(val context: Context, val nameList: PeopleWork) : RecyclerView.Adapter<GetWorkAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_3, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
            return nameList.cast.size
    }

    override fun onBindViewHolder(holder: GetWorkAdapter.NameViewHolder, position: Int) {
            holder.itemView.tView.text = nameList.cast[position].original_title

            val target = nameList.cast[position].poster_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)
        if(nameList.cast[position].media_type=="movie") {
            holder.itemView.iViewType.setImageResource(R.drawable.ic_movie_white)
        } else {
            holder.itemView.iViewType.setImageResource(R.drawable.ic_tv_white)
        }

            holder.itemView.parentLayout.setOnClickListener {
                var intent = Intent(context, ThirdAct::class.java)
                intent.putExtra("id", nameList.cast[position].id)
                ContextCompat.startActivity(context, intent, null)
        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}