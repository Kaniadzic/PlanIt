package com.example.planit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
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
    private val SELECT_PICTURE = 200
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
    var photoUrl: String = ""
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
                binding.etContent.text.toString(), photoUrl)

            writeNewPost(newPost)
            finish()
        })

        binding.etDate.setText(intent.getStringExtra("DATA"))
        usersFun()

        binding.typeSpinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                Log.i("WYBOR", binding.typeSpinner.selectedItemPosition.toString())

                if (binding.typeSpinner.selectedItemPosition == 1)  //1 czyli "Grafika"
                {
                    binding.tvAdd.visibility = View.VISIBLE
                    binding.imageView2.visibility = View.VISIBLE

                    val params = binding.btnPublish.layoutParams as ConstraintLayout.LayoutParams
                    params.bottomToTop = binding.imageView2.id
                }
                else
                {
                    binding.tvAdd.visibility = View.GONE
                    binding.imageView2.visibility = View.GONE

                    val params2 = binding.btnPublish.layoutParams as ConstraintLayout.LayoutParams
                    params2.bottomToTop = binding.etContent.id
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?)
            {
                TODO("Not yet implemented")
            }

        }

        binding.tvAdd.setOnClickListener(View.OnClickListener {
            imageChooser()
        })
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
            Log.i("LOG1", "DZIALA")
        }.addOnFailureListener {
            Log.e("LOG2", it.message.toString())
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
                    binding.tvAdd.visibility = View.GONE
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

    private fun imageChooser()
    {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "SelectPicture"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK)
        {
            if (requestCode == SELECT_PICTURE)
            {
                val selectedImageUri = data?.data

                if (selectedImageUri != null)
                {
                    photoUrl = selectedImageUri.toString()
                    Log.i("URL", selectedImageUri.toString())
                    Glide.with(this).load(selectedImageUri.toString()).into(binding.imageView2)
                }
            }
        }
    }
}