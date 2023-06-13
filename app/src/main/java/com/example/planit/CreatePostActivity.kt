package com.example.planit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class CreatePostActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    lateinit var binding: ActivityCreatePostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var users: DatabaseReference

    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = intent.getStringExtra("ID")?.let {
            FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
            getReference("Workspaces").child(it).child("posts") }!!

        users = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
        getReference("Workspaces").child(intent.getStringExtra("ID")!!).child("users")

        setSpinner(binding.platformSpinner, R.array.platforms_array)
        setSpinner(binding.typeSpinner, R.array.types_array)

        binding.btnDatechoose.setOnClickListener(View.OnClickListener
        {
            DatePickerDialog(this, this, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        binding.btnPublish.setOnClickListener(View.OnClickListener
        {
            val dateToPost = formatter.format(calendar.timeInMillis)

            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val current = formatter.format(time)
            binding.platformSpinner.setSelection(binding.platformSpinner.selectedItemPosition)

            val newPost = Post(randomId(), binding.etName.text.toString(), dateToPost, current,
                binding.platformSpinner.selectedItem.toString(), binding.typeSpinner.selectedItem.toString(),
                binding.etContent.text.toString())

            writeNewPost(newPost)
            finish()
        })

        binding.etDate.setText(intent.getStringExtra("DATA"))
        usersFun()
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

    fun usersFun()
    {
        users.get().addOnSuccessListener {
        for (i in it.children)
        {
            if (i.getValue(WorkspaceUser::class.java)?.email == FirebaseAuth.getInstance().currentUser?.email)
            {
                if (i.getValue(WorkspaceUser::class.java)?.role == "Użytkownik")
                {
                    binding.etContent.isFocusable = false
                    binding.etContent.hint = "Edytowanie treści dostępne jest tylko dla redaktorów i twórców"
                }
            }
        }
    }}
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int)
    {
        calendar.set(year, month, day)
        binding.etDate.setText(formatter.format(calendar.timeInMillis))
        TimePickerDialog(this, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    override fun onTimeSet(view: TimePicker?, hours: Int, minute: Int)
    {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minute)
        }

        binding.etDate.setText(formatter.format(calendar.timeInMillis))
    }
}