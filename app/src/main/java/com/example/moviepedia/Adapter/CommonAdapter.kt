package com.example.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviepedia.DataClass.Common_results
import com.example.moviepedia.R
import com.example.moviepedia.ThirdAct
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_1.view.*

class CommonAdapter(val context: Context, val nameList: ArrayList<Common_results>, val check : Boolean) : RecyclerView.Adapter<CommonAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w342/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_1, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if(check==false) {
            return nameList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: CommonAdapter.NameViewHolder, position: Int) {
        if(check==false) {
            holder.itemView.tView.text = nameList[position].original_title

            val target = nameList[position].poster_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)

            holder.itemView.parentLayout.setOnClickListener {
                var intent = Intent(context, ThirdAct::class.java)
                intent.putExtra("id", nameList[position].id)
                intent.putExtra("type", "Movie")
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}