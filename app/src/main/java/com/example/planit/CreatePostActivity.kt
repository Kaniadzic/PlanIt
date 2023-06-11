package com.example.planit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityCreatePostBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreatePostActivity : AppCompatActivity()
{
    lateinit var binding: ActivityCreatePostBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = intent.getStringExtra("ID")?.let {
            FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
            getReference("Workspaces").child(it) }!!

        setSpinner(binding.platformSpinner, R.array.platforms_array)
        setSpinner(binding.typeSpinner, R.array.types_array)

        binding.btnDatechoose.setOnClickListener(View.OnClickListener
        {
            showFragment(CalendarFragment(), R.id.fragmentCalendar)
            binding.conLayout.visibility = View.GONE
        })

        binding.btnPublish.setOnClickListener(View.OnClickListener
        {
            writeNewPost(binding.etName.text.toString(), binding.etDate.text.toString(),
            binding.platformSpinner.selectedItemPosition, binding.typeSpinner.selectedItemPosition,
            binding.etContent.text.toString())

            finish()
        })

        binding.etDate.setText(intent.getStringExtra("DATA"))
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
    fun setSpinner(spinner: Spinner, array: Int)
    {
        ArrayAdapter.createFromResource(
            applicationContext,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    fun writeNewPost(name: String, date: String, platform: Int, type: Int, content: String)
    {
        val post = Post(name, date, platform, type, content)

        databaseReference.child("bonoyassine").setValue(post).addOnSuccessListener {
            Log.i("LOGI", "SZMATA")
        }.addOnFailureListener {
            Log.i("DUPA", it.message.toString())
        }
    }
    fun showElements()
    {
        binding.conLayout.visibility = View.VISIBLE
    }
}