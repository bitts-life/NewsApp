package com.bittslife.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(val context: Context, val articles: List<Article>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = articles.get(position)
        holder.textviewTitle.text = article.title
        holder.textviewDescription.text = article.description
        Glide.with(context).load(article.urlToImage).into(holder.imageView)
        holder.itemView.setOnClickListener {
            Toast.makeText(context, article.title, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textviewTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        val textviewDescription = itemView.findViewById<TextView>(R.id.textViewDescription)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

}

