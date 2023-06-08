package com.example.planit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.planit.databinding.ActivityCreatePostBinding

class CreatePostActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityCreatePostBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val year = intent.getIntExtra("YEAR", 2000)
        val month = intent.getIntExtra("MONTH", 1)
        val day = intent.getIntExtra("DAY", 1)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.data.text = String.format("%d/%d/%d", day, month, year)
    }
}