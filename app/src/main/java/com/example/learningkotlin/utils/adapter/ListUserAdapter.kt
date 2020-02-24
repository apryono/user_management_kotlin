package com.example.learningkotlin.utils.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningkotlin.DetailActivity
import com.example.learningkotlin.R
import com.example.learningkotlin.model.SetterGetter.ListUser

class ListUserAdapter(val context: Context, var users : ArrayList<ListUser>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    override fun onBindViewHolder(view: ViewHolder, position: Int) {
        view.username.text = users[position].username
        view.email.text = users[position].email

        view.cardView.setOnClickListener {
            var intent = Intent(context, DetailActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("user_id", users[position].user_id)
            intent.putExtra("username", users[position].username)
            intent.putExtra("email",users[position].email)

            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(view.context).inflate(R.layout.item_list_user, view, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return users.size
    }

  class ViewHolder(v:View) : RecyclerView.ViewHolder(v) {
      var cardView = v.findViewById<CardView>(R.id.cardView)
      var email:TextView = v.findViewById(R.id.emailUser)
      var username:TextView = v.findViewById(R.id.nameUser)

  }
}