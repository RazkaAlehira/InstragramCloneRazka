package com.example.instragramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instragramclone.databinding.ActivityLoginBinding
import com.example.instragramclone.databinding.CustomBarBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = layoutInflater
        binding = ActivityLoginBinding.inflate(inflater)
        setContentView(binding.root)

        loginDialog = LoadingDialog(this)

        binding.run {
            // menambahkan click pada btnsignup
            btn_signup.setOnClickListener {
                //buat intent menuju ke Register Activity
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                // mulai activity intent itu
                startActivity(intent)
            }

            btn_login.setOnClickListener {
                // Jalankan Fungsi User
                loginUser()
            }
        }

    }

    // Buat Fungsi User
    private fun loginUser() {
        val email = binding.inputEmail.text_title.toString()
        val password = binding.inputPass.text_title.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(this, "Password Minimum 8 Length", Toast.LENGTH_SHORT).show()
            return
        }

        val mAuth = FirebaseAuth.getInstance()

        loginDialog.startLoadingDialog()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginDialog.dismissDialog()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    mAuth.signOut()
                    loginDialog.dismissDialog()
                }
            }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}