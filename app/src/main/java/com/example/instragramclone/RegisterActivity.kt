package com.example.instragramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.instragramclone.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = layoutInflater
        binding = ActivityRegisterBinding.inflate(inflater)
        setContentView(binding.root)

        binding.run {
            // tambahkan setonclicklistener pada tombol btnSignin
            btn_signin.setOnClickListener {
                // finish() digunakan untuk mengakhiri sebuah activity
                // sehingga activity sebelumnya akan terbuka
                finish()
            }

            // tambahkan setonclicklistener pada tombol register
            btn_register.setOnClickListener {
                // jalankan fungsi createAccount()
                // kalau merah maka buat fungsi createAccount()
                createAccount()
            }
        }
    }

    // buat fungsi bernama showToast()
    private fun showToast(pesan: String) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
    }

    // buat fungsi createAccount()
    private fun createAccount() {
        // gunakan binding.run karena kita akan akses view di layout
        binding.run {
            // ambil nilai yang dimasukkan ke dalam masing-masing editText
            val fullName = input_fullname.text.toString()
            val emailUser = input_email.text.toString()
            val userName = input_username.text.toString()
            val passWord = input_password.text.toString()

            // cek semua input, jika kosong tampilkan toast
            // return digunakan untuk mengakhiri jalannya fungsi
            if (fullName.isEmpty()) {
                showToast("Fullname Required")
                return
            }
            if (emailUser.isEmpty()) {
                showToast("Email Required")
                return
            }
            if (userName.isEmpty()) {
                showToast("Username Required")
                return
            }
            if (passWord.isEmpty()) {
                showToast("Password Required")
                return
            }
            if (!emailUser.isEmailValid()) {
                // tanda seru ! digunakan untuk hasil yang berlawanan
                // semisal hasil isEmailValid itu true
                // maka di if ini nilainya false
                showToast("No Valid Email, Please Check Your Email")
                return
            }
            if (passWord.count() < 8) { // Jika password ukurannya kurang dari 8 karakter
                showToast("Password Minimum 8 Length")
                return
            }
            // munculkan loading sebelum menyimpan data ke firebase
            dialog.startLoadingDialog()

            // sambungkan ke Firebase Auth
            val mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(emailUser, passWord)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // panggil fungsi saveUserInfo
                        // untuk menyimpan data user seperti fullname dan username
                        saveUserInfo(fullName, userName, emailUser)
                    } else {
                        // jika gagal membuat user maka tampilkan toast berisi errornya
                        val message = task.exception
                        showToast(message.toString())
                        mAuth.signOut()
                        // jika gagal, loading ditutup menggunakan dismissDialog()
                        dialog.dismissDialog()
                    }
                }
        }
    }

    // buat fungsi saveUserInfo()
    private fun saveUserInfo(fullName: String, userName: String, emailUser: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val userRef = FirebaseDatabase.getInstance().reference.child("users")
        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId
        userMap["fullname"] = fullName
        userMap["username"] = userName
        userMap["email"] = emailUser
        userMap["Bio"] = ""
        userMap["image"] = ""

        // fungsi di bawah ini untuk memasukkan data ke dalam database firebase
        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { // jika berhasil update firebase
                    // jika sukses tutup dialog
                    dialog.dismissDialog()
                    showToast("Account Made")
                    // buat intent yang menuju mainactivity
                    val intent = Intent(this, MainActivity::class.java)
                    // tambahkan flag activity clear task untuk nonaktifkan tombol back
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    // jika gagal tutup dialog
                    dialog.dismissDialog()
                    val message = task.exception.toString()
                    showToast(message)
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }
}