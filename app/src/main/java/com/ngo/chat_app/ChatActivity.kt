package com.ngo.chat_app

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.truong.ngo.chat_app.databinding.ActivityChatBinding

class ChatActivity:AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapterChat: MessageAdapter
    private lateinit var listMessages: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference
    var receiverRoom: String? = null
    var senderRoom: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val receiveUid= intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiveUid + senderUid
        receiverRoom = senderRoom + receiveUid

        listMessages = ArrayList()
        supportActionBar?.title = name
        mDbRef = FirebaseDatabase.getInstance().getReference()

        adapterChat = MessageAdapter(this )
        binding.charRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = adapterChat
        }
        //show message
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listMessages.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        listMessages.add(message!!)
                    }
                    adapterChat.messageList = listMessages
                    adapterChat.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        binding.apply {
            txtSend.setOnClickListener {
                val message = messageBox.text.toString()
                val messageObj = Message(message, senderUid)
                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObj).addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObj)
                    }
                messageBox.setText("")
            }
        }
    }
}