package com.moviessquare.moviepedia.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.moviessquare.moviepedia.DataClass.People_results
import com.moviessquare.moviepedia.FourthAct
import com.moviessquare.moviepedia.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_7.view.*

class PeopleAdapter(val context: Context, val nameList: ArrayList<People_results>, val check : Boolean) : RecyclerView.Adapter<PeopleAdapter.NameViewHolder>() {

    val baseURL = "https://image.tmdb.org/t/p/w185/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.item_7, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        if(check==false) {
            return nameList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: PeopleAdapter.NameViewHolder, position: Int) {
        if(check==false) {
            holder.itemView.tView.text = nameList[position].name
            holder.itemView.tViewAs.text = nameList[position].character
            val target = nameList[position].profile_path
            Picasso.with(this.context).load(baseURL + target).into(holder.itemView.iView)
            if(target == null) {
                Picasso.with(this.context).load(R.drawable.baseline_broken_image_black_18dp).into(holder.itemView.iView)
            }
        }

        holder.itemView.parentLayout.setOnClickListener {
            var intent = Intent(context, FourthAct::class.java)
            intent.putExtra("id", nameList[position].id)
            ContextCompat.startActivity(context, intent, null)
        }
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}