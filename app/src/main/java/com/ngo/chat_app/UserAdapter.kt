package com.ngo.chat_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.truong.ngo.chat_app.databinding.LayoutUserBinding

class UserAdapter(val context: Context): RecyclerView.Adapter<UserViewHolder>() {
    var data = listOf<User>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = LayoutUserBinding.inflate(inflate, parent, false)
        return  UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.binding.txtName.text = item.name
        holder.binding.txtName
            .setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", item.name)
            intent.putExtra("uid", FirebaseAuth.getInstance().currentUser?.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = data.size
}

class UserViewHolder(var binding: LayoutUserBinding): RecyclerView.ViewHolder(binding.root)