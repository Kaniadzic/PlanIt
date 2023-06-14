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
import android.widget.TimePicker
import androidx.constraintlayout.widget.ConstraintLayout
import coil.imageLoader
import com.bumptech.glide.Glide
import com.example.planit.databinding.ActivityCreatePostBinding
import com.example.planit.databinding.ActivityUpdatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class UpdatePostActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    lateinit var binding: ActivityUpdatePostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: DatabaseReference
    private val SELECT_PICTURE = 200
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
    var photoUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        binding = ActivityUpdatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
            getReference("Workspaces").child(intent.getStringExtra("WORKSPACEID").toString()).child("posts")

        user = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
        getReference("Workspaces").child(intent.getStringExtra("WORKSPACEID").toString()).child("users")

        Log.i("WORKSPACEID", intent.getStringExtra("WORKSPACEID").toString())

        val post = intent.getSerializableExtra("POST") as? Post

        usersFun()

        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.platforms_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.platformSpinner.adapter = adapter

        var position = adapter.getPosition(post?.platformCode)

        var adapter2: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this, R.array.types_array, android.R.layout.simple_spinner_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.adapter = adapter2

        var position2 = adapter2.getPosition(post?.typeCode)

        if (post != null)
        {
            binding.etName.setText(post.name)
            binding.etDate.setText(post.date)
            binding.platformSpinner.setSelection(position)
            binding.typeSpinner.setSelection(position2)
            binding.etContent.setText(post.content)

            if (post.photoUrl?.isNotEmpty() == true)
            {
                Glide.with(this).load(post.photoUrl).into(binding.imageView2)
                binding.tvAdd.visibility = View.VISIBLE
                binding.imageView2.visibility = View.VISIBLE

                val params = binding.btnUpdate.layoutParams as ConstraintLayout.LayoutParams
                params.bottomToBottom = binding.imageView2.id
            }
            else
            {
                binding.tvAdd.visibility = View.GONE
                binding.imageView2.visibility = View.GONE

                val params2 = binding.btnUpdate.layoutParams as ConstraintLayout.LayoutParams
                params2.bottomToBottom = binding.etContent.id
            }
        }

        binding.btnDatechoose.setOnClickListener(View.OnClickListener
        {
            DatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        binding.tvAdd.setOnClickListener(View.OnClickListener {
            imageChooser()
        })

        binding.btnUpdate.setOnClickListener(View.OnClickListener {
            val dateToPost = formatter.format(calendar.timeInMillis)
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
            val current = formatter.format(time)

            var photo = photoUrl
            var date = ""

            if (photoUrl.isEmpty())
            {
                if (post != null)
                {
                    photo = post.photoUrl.toString()
                }
            }

            if (date.isEmpty())
            {
                if (post != null)
                {
                    date = post.date.toString()
                }
            }
            else
            {
                date = dateToPost
            }

            val newPost = Post(post?.id, binding.etName.text.toString(), date, current, binding.platformSpinner.selectedItem.toString(),
                binding.typeSpinner.selectedItem.toString(), binding.etContent.text.toString(), photo)

            writeNewPost(newPost)

            finish()
        })
    }

    fun writeNewPost(newPost: Post)
    {
        databaseReference.child(newPost.id.toString()).setValue(newPost).addOnSuccessListener {
            Log.i("LOGI", "SZMATA")
        }.addOnFailureListener {
            Log.i("DUPA", it.message.toString())
        }
    }

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
        val intent = Intent()
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
    fun usersFun()
    {
        user.get().addOnSuccessListener {
            for (i in it.children)
            {
                if (i.getValue(WorkspaceUser::class.java)?.email == FirebaseAuth.getInstance().currentUser?.email)
                {
                    if (i.getValue(WorkspaceUser::class.java)?.role == "Użytkownik")
                    {
                        binding.etContent.isFocusable = false
                        binding.etContent.hint = "Edytowanie treści dostępne jest tylko dla redaktorów i twórców"
                        binding.tvAdd.visibility = View.GONE
                        binding.imageView2.visibility = View.GONE

                        val params = binding.btnUpdate.layoutParams as ConstraintLayout.LayoutParams
                        params.bottomToBottom = binding.etContent.id
                    }
                }
            }
        }
    }
}