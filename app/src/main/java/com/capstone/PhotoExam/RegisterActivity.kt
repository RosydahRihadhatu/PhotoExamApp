package com.capstone.PhotoExam

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editEmail: EditText
    lateinit var editPassword: EditText
    lateinit var editPasswordConf: EditText
    lateinit var btnRegister: Button
    lateinit var btnLogin: TextView
    lateinit var progressDialog: ProgressDialog
    lateinit var textInputPassword: TextInputLayout
    lateinit var textInputPasswordConf: TextInputLayout
    lateinit var textInputEmail: TextInputLayout

    var firebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.drawable.bg_regis)
        setContentView(R.layout.activity_register)
        editName = findViewById(R.id.username)
        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        editPasswordConf = findViewById(R.id.passwordConf)
        btnRegister = findViewById(R.id.btn_register)
        btnLogin = findViewById(R.id.btn_login)
        textInputPassword = findViewById(R.id.layoutPassword)
        textInputPasswordConf = findViewById(R.id.layoutPasswordConf)
        textInputEmail = findViewById(R.id.layoutEmail)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu")

        btnLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        btnRegister.setOnClickListener{
            if(editName.text.isNotEmpty() && editEmail.text.isNotEmpty() &&editPassword.text.isNotEmpty()){
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.text).matches()) {
                    if (editPassword.text.toString() == editPasswordConf.text.toString()) {
                        // Pengecekan panjang password
                        if (editPassword.text.length >= 6) {
                            processRegister()
                        } else {
                            Toast.makeText(this, "Password harus memiliki setidaknya 6 karakter", LENGTH_SHORT).show()
                            textInputPassword.boxStrokeColor = Color.RED
                            textInputPasswordConf.boxStrokeColor = Color.RED
                        }
                    } else {
                        Toast.makeText(this, "Kata sandi harus sama", LENGTH_SHORT).show()
                        textInputPasswordConf.boxStrokeColor = Color.RED
                    }
                } else {
                    Toast.makeText(this, "Format email tidak valid", LENGTH_SHORT).show()
                    textInputEmail.boxStrokeColor = Color.RED
                }
            } else {
                Toast.makeText(this, "Silahkan isi semua data", LENGTH_SHORT).show()
            }
        }
    }

    private fun processRegister(){
        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()

        if (name.length > 15) {
            Toast.makeText(this, "Username tidak boleh lebih dari 15 karakter", LENGTH_SHORT).show()
            return
        }

        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = name
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        .addOnFailureListener{ error2 ->
                            Toast.makeText( this, error2.localizedMessage, LENGTH_SHORT).show()
                        }
                }else{
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener{ error ->
                Toast.makeText(this, error.localizedMessage, LENGTH_SHORT).show()
            }
    }
}