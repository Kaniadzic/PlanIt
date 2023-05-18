package com.example.planit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityEditBinding
import com.example.planit.databinding.ActivityMainBinding
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

class EditActivity : AppCompatActivity()
{
    private var PICK_IMAGE: Int = 200
    private lateinit var binding: ActivityEditBinding
    private lateinit var imageUrlDef: String
    private var elementy: ArrayList<Int> = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner(binding.countrySpinner)

        imageUrlDef = "https://img1.ak.crunchyroll.com/i/spire2/0fdcdc55d10ccee7f745c348124c193f1655144936_large.jpg"

        binding.avatarView.setOnClickListener(View.OnClickListener {
            Log.i("LOL", "lol")
            imageChooser()
        })

        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this.baseContext)
        Log.i("XD", sharedPreference.getString("name", "DEFAULT").toString())
        Log.i("XD2", sharedPreference.getString("surname", "DEFAULT").toString())

        binding.etName.setText(sharedPreference.getString("name", "Imię").toString())
        binding.etSurname.setText(sharedPreference.getString("surname", "Nazwisko").toString())
        binding.avatarView.loadImage(data = sharedPreference.getString("link", imageUrlDef))

        binding.btnSave.setOnClickListener(View.OnClickListener
        {

            val editor = sharedPreference.edit()
            editor.putString("name", binding.etName.text.toString())
            editor.putString("surname", binding.etSurname.text.toString())
            editor.putString("link", imageUrlDef)
            editor.commit()
            finish()
        })

        binding.btnPasswordchange.setOnClickListener(View.OnClickListener
        {
            showFragment(PasswordChangeFragment(), R.id.fragmentPassChange)
            hide()
        })

        binding.btnEmailchange.setOnClickListener(View.OnClickListener
        {
            showFragment(EmailChangeFragment(), R.id.fragmentMailChange)
            hide()
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
                    imageUrlDef = data.data.toString()
                    Log.i("URL", imageUrlDef)
                    binding.avatarView.loadImage(data = imageUrlDef)
                }
            }
        }
    }

    fun showFragment(fragment: Fragment, id: Int)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment)
        fragmentTransaction.addToBackStack("theFragment")
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        fragmentTransaction.show(fragment)
        fragmentTransaction.commit()
    }

    //sry nie miałem lepszego rozwiazania :(
    fun hide()
    {
        binding.avatarView.visibility = View.GONE
        binding.tvName.visibility = View.GONE
        binding.etName.visibility = View.GONE
        binding.tvSurname.visibility = View.GONE
        binding.etSurname.visibility = View.GONE
        binding.tvKraj.visibility = View.GONE
        binding.countrySpinner.visibility = View.GONE
        binding.btnPasswordchange.visibility = View.GONE
        binding.btnEmailchange.visibility = View.GONE
        binding.btnSave.visibility = View.GONE
    }

    fun show()
    {
        binding.avatarView.visibility = View.VISIBLE
        binding.tvName.visibility = View.VISIBLE
        binding.etName.visibility = View.VISIBLE
        binding.tvSurname.visibility = View.VISIBLE
        binding.etSurname.visibility = View.VISIBLE
        binding.tvKraj.visibility = View.VISIBLE
        binding.countrySpinner.visibility = View.VISIBLE
        binding.btnPasswordchange.visibility = View.VISIBLE
        binding.btnEmailchange.visibility = View.VISIBLE
        binding.btnSave.visibility = View.VISIBLE
    }
}


