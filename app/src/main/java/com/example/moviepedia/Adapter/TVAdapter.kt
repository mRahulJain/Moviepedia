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
import com.example.moviepedia.DataClass.TV
import com.example.moviepedia.DataClass.TV_details
import com.example.moviepedia.FifthAct
import com.example.moviepedia.R
import com.example.moviepedia.ThirdAct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*

class TVAdapter(val context: Context, val nameList: ArrayList<TV_details>) :
    RecyclerView.Adapter<TVAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_1, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
            return nameList.size
    }

    override fun onBindViewHolder(holder: TVAdapter.NameViewHolder, position: Int) {
            holder.itemView.tView.text = nameList[position].name
            Log.d("CHECK", "${holder.itemView.tView.text}")
            val target = nameList[position].poster_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)

            holder.itemView.parentLayout.setOnClickListener {
                var intent = Intent(context, FifthAct::class.java)
                intent.putExtra("id", nameList[position].id)
                ContextCompat.startActivity(context, intent, null)
            }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}