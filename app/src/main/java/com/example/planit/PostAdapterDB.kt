package com.example.planit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class PostAdapterDB(options: FirebaseRecyclerOptions<Post>, query: DatabaseReference) : FirebaseRecyclerAdapter<Post, PostAdapterDB.PostViewHolder>(options)
{
    var db = query

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder
    {
        Log.i("DZIALAVIEW", "VIEW")
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_posts, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post)
    {
        model.id?.let { Log.i("ID", it) }
        model.name?.let { Log.i("NAME", it) }

        holder.tv_name.text = model.name
        holder.tv_datecreated.text = model.postCreated
        holder.tv_datepublish.text = model.date
        holder.tv_platform.text = model.platformCode.toString()
        holder.tv_type.text = model.typeCode.toString()
        holder.tv_content.text = model.content

        holder.removePost.setOnClickListener(View.OnClickListener
        {
            db.child(model.id.toString()).removeValue()
        })
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tv_name = itemView.findViewById<TextView>(R.id.tv_post_name)
        var tv_datecreated = itemView.findViewById<TextView>(R.id.tv_date_created)
        var tv_datepublish = itemView.findViewById<TextView>(R.id.tv_date_publish)
        var tv_platform = itemView.findViewById<TextView>(R.id.tv_post_platform)
        var tv_type = itemView.findViewById<TextView>(R.id.tv_post_type)
        var tv_content = itemView.findViewById<TextView>(R.id.tv_post_content)
        var removePost =itemView.findViewById<Button>(R.id.btn_remove_post)
    }
}
