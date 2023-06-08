package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityWorkspaceDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkspaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkspaceDetailsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var workspaceData: Workspace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workspace_details)

        binding = ActivityWorkspaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        workspaceData = Workspace(
            intent.getStringExtra("ID"),
            intent.getStringExtra("Name"),
            intent.getStringExtra("CreationDate"),
            intent.getStringExtra("CreatorID"),
            intent.getStringExtra("Type")
        )

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

        binding.btnWorkspaceDelete.setOnClickListener(View.OnClickListener {
            databaseReference = FirebaseDatabase
                .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Workspaces")
                .child(workspaceData.id.toString())

            val removeTask = databaseReference
                .removeValue()
                .addOnCompleteListener{
                    Toast.makeText(this, "Workspace usunięto!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, WorkspacesActivity::class.java))
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Jebać ruch!!", Toast.LENGTH_SHORT).show()
                }
        })

        binding.btnWorkspaceEdit.setOnClickListener(View.OnClickListener {

        })

        binding.btnWorkspaceTasks.setOnClickListener(View.OnClickListener {

        })

        binding.btnWorkspaceUsers.setOnClickListener(View.OnClickListener {

        })
        binding.btnWorkspaceAddUser.setOnClickListener(View.OnClickListener {
            showFragment(AddUserFragment())
        })


        loadText()
    }

    fun loadText() {
        binding.tvWorkspaceName.text = workspaceData.name
        binding.tvWorkspaceType.text = workspaceData.type
    }

    fun showFragment(fragment: Fragment)
    {
        val fragmentBundle = Bundle()
        fragmentBundle.putString("workspaceID", workspaceData.id)
        fragment.arguments = fragmentBundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentAddUser, fragment)
        fragmentTransaction.commit()
    }
}