package com.example.planit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference

class PostAdapter(data: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>()
{
    var postsList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_posts, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int
    {
        return postsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.name.text = postsList[position].name
        holder.postCreated.text = postsList[position].postCreated
        holder.postPublish.text = postsList[position].date
        holder.platform.text = postsList[position].platformCode.toString()
        holder.type.text = postsList[position].typeCode.toString()
        holder.content.text = postsList[position].content
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val name: TextView = itemView.findViewById(R.id.tv_post_name)
        val postCreated: TextView = itemView.findViewById(R.id.tv_date_created)
        val postPublish: TextView = itemView.findViewById(R.id.tv_date_publish)
        val platform: TextView = itemView.findViewById(R.id.tv_post_platform)
        val type: TextView = itemView.findViewById(R.id.tv_post_type)
        val content: TextView = itemView.findViewById(R.id.tv_post_content)
    }
}


