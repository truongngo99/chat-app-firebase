package com.ngo.chat_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.truong.ngo.chat_app.databinding.ActivitySignupBinding

class SignUpActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fDatabase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.apply {
            btnSignup.setOnClickListener {
                val email = edtEmail.text.toString()
                val pass = edtPass.text.toString()
                signUp(email,pass)
            }
        }
    }

    private fun signUp( email: String, pass: String) {
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserDatabase(binding.edtName.text.toString(), email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun addUserDatabase(name: String, email: String, uid: String) {
    fDatabase = FirebaseDatabase.getInstance().getReference()
        fDatabase.child("user").child(uid).setValue(User(email, name, uid))
    }
}