package com.capstone.PhotoExam

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.capstone.PhotoExam.R
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    lateinit var textFullname: TextView
    lateinit var textEmail: TextView
    lateinit var btn_Logout: TextView

    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textFullname = findViewById(R.id.full_name)
        textEmail = findViewById(R.id.email)
        btn_Logout = findViewById(R.id.btn_Logout)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null){
            textFullname.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        btn_Logout.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}