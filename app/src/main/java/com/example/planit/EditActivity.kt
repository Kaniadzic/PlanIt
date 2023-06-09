package com.example.planit

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.planit.databinding.ActivityEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityEditBinding
    private lateinit var imageUrlDef: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val SELECT_PICTURE = 200
    lateinit var userDetails: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        mAuth = FirebaseAuth.getInstance()

        userDetails = applicationContext.getSharedPreferences(mAuth.uid, MODE_PRIVATE)

        databaseReference = mAuth.uid?.let { it -> FirebaseDatabase.
        getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
        getReference("Users").child(it) }!!

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner(binding.countrySpinner, R.array.countries_array)
        setSpinner(binding.roleSpinner, R.array.roles_array)

        read2()
        imageUrlDef = userDetails.getString(mAuth.uid, "https://i.pinimg.com/originals/f1/0f/f7/f10ff70a7155e5ab666bcdd1b45b726d.jpg").toString()

        binding.imageView.setOnClickListener(View.OnClickListener
        {
            imageChooser()
        })

        binding.btnSave.setOnClickListener(View.OnClickListener
        {
            writeNewUser(
                binding.etName.text.toString(),
                binding.etSurname.text.toString(),
                mAuth.currentUser?.email,
                imageUrlDef,
                binding.countrySpinner.selectedItemPosition,
                binding.roleSpinner.selectedItemPosition
            )

            Log.i("DZIALA", "DJHHFKJFU")
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

        binding.btnMenuLogout.setOnClickListener(View.OnClickListener {
            mAuth.signOut()
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        })

        binding.btnMenuHome.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, HomepageActivity::class.java))
        })

        binding.btnMenuCalendar.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, WorkspacesActivity::class.java))
        })

        binding.btnMenuSettings.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, EditActivity::class.java))
        })
    }
    private fun imageChooser()
    {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "SelectPicture"), SELECT_PICTURE)
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
        binding.imageView.visibility = View.GONE
        binding.tvName.visibility = View.GONE
        binding.etName.visibility = View.GONE
        binding.tvSurname.visibility = View.GONE
        binding.etSurname.visibility = View.GONE
        binding.tvKraj.visibility = View.GONE
        binding.countrySpinner.visibility = View.GONE
        binding.tvRola.visibility = View.GONE
        binding.roleSpinner.visibility = View.GONE
        binding.btnPasswordchange.visibility = View.GONE
        binding.btnEmailchange.visibility = View.GONE
        binding.btnSave.visibility = View.GONE
    }

    fun show()
    {
        binding.imageView.visibility = View.VISIBLE
        binding.tvName.visibility = View.VISIBLE
        binding.etName.visibility = View.VISIBLE
        binding.tvSurname.visibility = View.VISIBLE
        binding.etSurname.visibility = View.VISIBLE
        binding.tvKraj.visibility = View.VISIBLE
        binding.countrySpinner.visibility = View.VISIBLE
        binding.tvRola.visibility = View.VISIBLE
        binding.roleSpinner.visibility = View.VISIBLE
        binding.btnPasswordchange.visibility = View.VISIBLE
        binding.btnEmailchange.visibility = View.VISIBLE
        binding.btnSave.visibility = View.VISIBLE
    }

    fun writeNewUser(name: String, surname: String, email: String?, photoUrl: String, countryCode: Int, countryRole: Int)
    {
        val user = User(
            name,
            surname,
            photoUrl,
            countryCode,
            countryRole,
            email
        )

        databaseReference.setValue(user).addOnSuccessListener {
            Log.i("LOGI", "SZMATA")
        }.addOnFailureListener {
            Log.i("DUPA", it.message.toString())
        }
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
                    imageUrlDef = selectedImageUri.toString()
                    Log.i("URL", imageUrlDef)
                    Glide.with(this).load(imageUrlDef).circleCrop().into(binding.imageView)
                }
            }
        }
    }
    fun read2()
    {
        mAuth.uid?.let { databaseReference.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                var user = snapshot.getValue(User::class.java)

                if (user != null)
                {
                    binding.etName.setText(user.name)
                    binding.etSurname.setText(user.surname)
                    user.countryCode?.let { it1 -> binding.countrySpinner.setSelection(it1) }
                    user.roleCode?.let { it1 -> binding.roleSpinner.setSelection(it1) }
                    user.photoUrl?.let { it1 -> Log.i("PHOTS", it1) }

                    if (user.photoUrl?.isNotEmpty() == true)
                    {
                        Log.i("NIEPUSTE", "NIEPUSTE")
                        var edit = userDetails.edit()
                        edit.putString(mAuth.uid, user.photoUrl!!.trim())
                        edit.apply()
                    }
                    else
                    {
                        Log.i("PUSTE", "PUSTE")
                    }

                    Glide.with(applicationContext).load(user.photoUrl).circleCrop().into(binding.imageView)
                }
            }

            override fun onCancelled(error: DatabaseError)
            {
                Log.e("ERROR", error.message)
            }
        })}
    }
}


