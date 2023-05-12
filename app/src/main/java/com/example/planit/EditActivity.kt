package com.example.planit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

class EditActivity : AppCompatActivity()
{
    private lateinit var avatarView: AvatarView
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        spinner= findViewById(R.id.country_spinner)
        setSpinner(spinner)

        avatarView = findViewById(R.id.avatar_view)
        avatarView.loadImage(data = "https://img1.ak.crunchyroll.com/i/spire2/0fdcdc55d10ccee7f745c348124c193f1655144936_large.jpg")
    }

    fun setSpinner(spinner: Spinner)
    {
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.countries_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
}


