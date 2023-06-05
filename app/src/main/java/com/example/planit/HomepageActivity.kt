package com.example.planit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
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

//        binding.lol1.text = mAuth.currentUser?.email.toString()
//
//        if (!mAuth.currentUser?.isEmailVerified!!)
//        {
//            mAuth.currentUser?.sendEmailVerification()
//            binding.lol2.text = "Potwierdż adres email"
//        }
//        else
//        {
//            binding.lol2.text = "Adres email potwierdzony"
//        }
//
//        Log.i("TAG", mAuth.currentUser?.displayName.toString())


        binding.btnMenuLogout.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        })

        binding.btnMenuHome.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, HomepageActivity::class.java))
        })

        binding.btnMenuCalendar.setOnClickListener(View.OnClickListener {

        })

        binding.btnMenuClock.setOnClickListener(View.OnClickListener {

        })

        binding.btnMenuSettings.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, EditActivity::class.java))
        })
//
//        binding.btnEdit.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(applicationContext, EditActivity::class.java))
//        })
//
//        binding.calendar.setOnDateChangeListener { calendarView, y, m, d ->
//            Log.i("DATA", y.toString())
//            Log.i("DATA2", (m + 1).toString())
//            Log.i("DATA3", d.toString())
//            intent = Intent(applicationContext, CreatePostActivity::class.java)
//            intent.putExtra("YEAR", y)
//            intent.putExtra("MONTH", m)
//            intent.putExtra("DAY", d)
//            startActivity(intent)
//        }
    }
}