package com.example.planit

import android.content.Intent
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
import java.text.SimpleDateFormat
import java.util.*

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
            getReference("Workspaces").child(it).child("posts") }!!

        setSpinner(binding.platformSpinner, R.array.platforms_array)
        setSpinner(binding.typeSpinner, R.array.types_array)

        binding.btnDatechoose.setOnClickListener(View.OnClickListener
        {
            showFragment(CalendarFragment(), R.id.fragmentCalendar)
            binding.conLayout.visibility = View.GONE
        })

        binding.btnPublish.setOnClickListener(View.OnClickListener
        {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val current = formatter.format(time)

            val newPost = Post(randomId(), binding.etName.text.toString(), binding.etDate.text.toString(), current,
                binding.platformSpinner.selectedItemPosition, binding.typeSpinner.selectedItemPosition,
                binding.etContent.text.toString())

            writeNewPost(newPost)

            //val intent = Intent("refresh_activity")
            //sendBroadcast(intent)

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

    fun writeNewPost(newPost: Post)
    {
        databaseReference.child(newPost.id.toString()).setValue(newPost).addOnSuccessListener {
            Log.i("LOGI", "SZMATA")
        }.addOnFailureListener {
            Log.i("DUPA", it.message.toString())
        }
    }
    fun showElements()
    {
        binding.conLayout.visibility = View.VISIBLE
    }

    fun randomId(): String
    {
        var id: String = ""

        for (i in 1..10)
        {
            var znak = ((97..122).random()).toChar()
            id += znak
        }

        return id
    }
}