package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.planit.databinding.ActivityWorkspacesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkspacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkspacesBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val workspacesList = mutableListOf<Workspace?>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workspaces)

        binding = ActivityWorkspacesBinding.inflate(layoutInflater)
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

        binding.btnAddWorkspace.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, AddWorkspaceActivity::class.java))
        })

        getWorkspaces()
    }

    fun getWorkspaces() {
        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")

        val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var dataSnapshot: DataSnapshot

        databaseReference
            .get()
            .addOnSuccessListener{
                dataSnapshot = it

                dataSnapshot.children.forEach{
                    workspacesList.add(it.getValue(Workspace::class.java))
                }

                var workspacesNames = arrayOf<String?>()
                var workspacesTypes = arrayOf<String?>()
                var workspacesIDs = arrayOf<String?>()

                workspacesList.forEach{
                    workspacesNames = workspacesNames.plus(it?.name)
                    workspacesTypes = workspacesTypes.plus(it?.type)
                    workspacesIDs = workspacesIDs.plus(it?.id)
                }

                val workspacesListAdapter = WorkspacesListAdapter(this, workspacesNames, workspacesTypes, workspacesIDs)
                binding.lvWorkspaces.adapter = workspacesListAdapter

                binding.lvWorkspaces.setOnItemClickListener(){adapterView, view, position, id ->
                    val itemAtPos = adapterView.getItemAtPosition(position)
                    Toast.makeText(this, "$itemAtPos", Toast.LENGTH_LONG).show()
                }

                Toast.makeText(this, "Sukces!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Jebać ruch!!", Toast.LENGTH_SHORT).show()
            }
    }
}
















