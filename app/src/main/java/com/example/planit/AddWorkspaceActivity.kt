package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planit.databinding.ActivityAddWorkspaceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class AddWorkspaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddWorkspaceBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workspace)

        binding = ActivityAddWorkspaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

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

        binding.btnCreateWorkspace.setOnClickListener(View.OnClickListener {
            createWorkspace()
        })

        setSpinner(binding.typeSpinner, R.array.workspace_types)
    }

    fun createWorkspace()
    {
        if (binding.etName.text.isEmpty()) {
            Toast.makeText(this, R.string.missing_inputs, Toast.LENGTH_SHORT).show()
        }
        else {
            val sdf = SimpleDateFormat("dd-MM-yyyy")
            val currentDate = sdf.format(Date())

            databaseReference = FirebaseDatabase
                .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Workspaces")

            val workspaceID = databaseReference.push().key!!

            val newWorkspace = Workspace(
                workspaceID,
                binding.etName.text.toString(),
                currentDate,
                FirebaseAuth.getInstance().currentUser?.uid.toString(),
                binding.typeSpinner.selectedItem.toString(),
                "",
                ""
            )

            databaseReference
                .child(workspaceID)
                .setValue(newWorkspace)
                .addOnSuccessListener{
                    Toast.makeText(this, "Sukces!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, WorkspacesActivity::class.java))
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Jebać ruch!!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun setSpinner(spinner: Spinner, arr: Int)
    {
        ArrayAdapter.createFromResource(
            applicationContext,
            arr,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}