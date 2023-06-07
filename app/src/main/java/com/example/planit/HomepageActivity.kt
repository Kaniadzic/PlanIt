package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityHomepageBinding
import com.example.planit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomepageActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityHomepageBinding
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()


        binding.btnMenuLogout.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        })

        binding.btnMenuHome.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, HomepageActivity::class.java))
        })

        binding.btnMenuCalendar.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, WorkspacesActivity::class.java))
        })

        binding.btnMenuSettings.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, EditActivity::class.java))
        })
    }

}