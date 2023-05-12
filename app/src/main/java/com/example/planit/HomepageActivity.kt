package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

        binding.lol1.text = mAuth.currentUser?.email.toString()

        if (!mAuth.currentUser?.isEmailVerified!!)
        {
            mAuth.currentUser?.sendEmailVerification()
            binding.lol2.text = "Potwierdż adres email"
        }
        else
        {
            binding.lol2.text = "Adres email potwierdzony"
        }

        Log.i("TAG", mAuth.currentUser?.displayName.toString())

        binding.btnSignOut.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        })

        binding.btnEdit.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, EditActivity::class.java))
        })
    }
}