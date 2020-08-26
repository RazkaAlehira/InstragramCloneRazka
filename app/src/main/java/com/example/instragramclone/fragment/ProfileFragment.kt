package com.example.instragramclone.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instragramclone.AccountSettingActivity
import com.example.instragramclone.R
import com.example.instragramclone.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    // ViewBinding Fragment untuk fragment_profile.xml
    private lateinit var binding: FragmentProfileBinding

    // onCreateView dipakai untuk menginisialisasi view yang ada pada layout fragment_profile
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inisialisasi viewbinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated untuk memberi fungsi pada view di dalam layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // beri fungsi setonclicklistener pada tombol edit profil
        binding.btnEditProfile.setOnClickListener {
            // buat intent menuju Akun Setting
            val intent = Intent(view.context, AccountSettingActivity::class.java)
            startActivity(intent)
        }
    }
}