package com.example.planit

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.planit.databinding.ActivityEditBinding
import com.example.planit.databinding.ActivityMainBinding
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

class EditActivity : AppCompatActivity()
{
    private var PICK_IMAGE: Int = 200
    private lateinit var binding: ActivityEditBinding
    private lateinit var imageUrl: String
    private lateinit var name: String
    private lateinit var surname: String
    private var default: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner(binding.countrySpinner)

        if (default)
            imageUrl = "https://img1.ak.crunchyroll.com/i/spire2/0fdcdc55d10ccee7f745c348124c193f1655144936_large.jpg"

        binding.avatarView.loadImage(data = imageUrl)

        binding.avatarView.setOnClickListener(View.OnClickListener {
            Log.i("LOL", "lol")
            imageChooser()
        })

        name = binding.etName.text.toString()
        surname = binding.etSurname.text.toString()

        binding.btnSave.setOnClickListener(View.OnClickListener {

            val sharedPreference = getSharedPreferences("NAME", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString("LINK", imageUrl)
            editor.putString("NAME", name)
            editor.putString("SURNAME", surname)
            editor.apply()

            binding.etName.setText(sharedPreference.getString("NAME", "xd"))
            binding.etName.setText(sharedPreference.getString("SURNAME", "xd"))

            finish()
        })
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

    fun imageChooser()
    {
        val intent = Intent()

        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select picutre"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK)
        {
            if (requestCode == PICK_IMAGE)
            {
                if (data != null)
                {
                    imageUrl = data.data.toString()
                    Log.i("URL", imageUrl)
                    binding.avatarView.loadImage(data = imageUrl)
                    default = false
                }
            }
        }
    }
}


