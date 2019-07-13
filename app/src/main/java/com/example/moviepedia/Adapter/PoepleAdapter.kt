package com.example.moviepedia.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.People
import com.example.moviepedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*

class PeopleAdapter(val context: Context, val nameList: People, val check : Boolean) : RecyclerView.Adapter<PeopleAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_1, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if(check==false) {
            return nameList.results.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: PeopleAdapter.NameViewHolder, position: Int) {
        if(check==false) {
            holder.itemView.tView.text = nameList.results[position].name

            val target = nameList.results[position].profile_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)
        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}