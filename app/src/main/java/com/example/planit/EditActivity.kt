package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.getstream.avatarview.coil.loadImage

class EditActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityEditBinding
    private lateinit var imageUrlDef: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val SELECT_PICTURE = 200
    var ile = 1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        databaseReference = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner(binding.countrySpinner)
        setSpinner2(binding.roleSpinner)

        mAuth = FirebaseAuth.getInstance()
        read()
        imageUrlDef = "https://img1.ak.crunchyroll.com/i/spire2/0fdcdc55d10ccee7f745c348124c193f1655144936_large.jpg"

        binding.avatarView.setOnClickListener(View.OnClickListener
        {
            imageChooser()
        })

        binding.btnSave.setOnClickListener(View.OnClickListener
        {
            mAuth.currentUser?.let { it1 ->
                writeNewUser(it1.uid, binding.etName.text.toString(), binding.etSurname.text.toString(), imageUrlDef, binding.countrySpinner.selectedItemPosition,
                binding.roleSpinner.selectedItemPosition)
            }

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

    fun setSpinner2(spinner: Spinner)
    {
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.roles_array,
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
        binding.avatarView.visibility = View.GONE
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
        binding.avatarView.visibility = View.VISIBLE
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

    fun writeNewUser(uid: String, name: String, surname: String, photoUrl: String, countryCode: Int, countryRole: Int)
    {
        val user = User(name, surname, photoUrl, countryCode, countryRole)


        databaseReference.child(uid).setValue(user).addOnSuccessListener {
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
                    binding.avatarView.loadImage(imageUrlDef)
                }
            }
        }
    }
    fun read()
    {
        mAuth.currentUser?.let {
            databaseReference.child(it.uid).addChildEventListener(object: ChildEventListener
            {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
                {
                    val keys = snapshot.key.toString().split("\n")
                    val values = snapshot.value.toString().split("\n")

                    val map: Map<String, String> = keys.zip(values).toMap()
                    map["name"]?.let { it1 -> binding.etName.setText(it1)}
                    map["surname"]?.let { it1 -> binding.etSurname.setText(it1) }
                    map["countryCode"]?.let { it1 -> binding.countrySpinner.setSelection(Integer.parseInt(it1)) }
                    map["roleCode"]?.let { it1 -> binding.roleSpinner.setSelection(Integer.parseInt(it1)) }
                    map["photoUrl"]?.let { it1 -> binding.avatarView.loadImage(data = it1) }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?)
                {
                    Log.i("ZMIENIONO", "zmiana")
                }

                override fun onChildRemoved(snapshot: DataSnapshot)
                {
                    Log.i("DELETED", snapshot.value.toString())
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?)
                {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError)
                {
                    Log.i("FAILED", error.message)
                }

            })
        }
    }
}


