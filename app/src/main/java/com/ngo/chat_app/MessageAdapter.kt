package com.ngo.chat_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.truong.ngo.chat_app.databinding.LayoutReciveBinding
import com.truong.ngo.chat_app.databinding.LayoutSendBinding

class MessageAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messageList = ArrayList<Message>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class SendViewHolder(val binding: LayoutSendBinding ) : RecyclerView.ViewHolder(binding.root)

    class ReceiverViewHolder( val binding: LayoutReciveBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1001) {
            SendViewHolder(LayoutSendBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ReceiverViewHolder(LayoutReciveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = messageList[position]
        if (holder is SendViewHolder) {
            holder.binding.txtSend.text = item.message
        } else {
            holder as ReceiverViewHolder
            holder.binding.txtReceive.text = item.message
        }
    }



    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
      return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
          1001
      } else {
          1002
      }
    }

}
enum class MessageType {
    SEND, RECEIVE
}