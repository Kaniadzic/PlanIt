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
import com.google.firebase.database.DataSnapshot
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

        binding.btnWorkspaceTasks.setOnClickListener(View.OnClickListener {

        })

        binding.btnWorkspaceUsers.setOnClickListener(View.OnClickListener {
            showFragment(WorkspaceUsersFragment())
        })

        binding.btnWorkspaceAddUser.setOnClickListener(View.OnClickListener {
            showFragment(AddUserFragment())
        })

        binding.btnWorkspaceLeave.setOnClickListener(View.OnClickListener {
            leaveWorkspace()
        })

        binding.btnPostAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, CreatePostActivity::class.java)
            intent.putExtra("ID", workspaceData.id)
            startActivity(intent)
        })

        loadText()
        checkUserPermissions()
        showWorkspaceCreator()
    }

    fun loadText() {
        binding.tvWorkspaceName.text = workspaceData.name
        binding.tvWorkspaceType.text = workspaceData.type
    }

    fun showFragment(fragment: Fragment)
    {
        val fragmentBundle = Bundle()
        fragmentBundle.putString("workspaceID", workspaceData.id)
        fragmentBundle.putString("creatorID", workspaceData.creatorId)
        fragment.arguments = fragmentBundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainter, fragment)
        fragmentTransaction.commit()
    }

    fun checkUserPermissions() {
        val userID = mAuth.currentUser?.uid
        Log.d("Current user: ", userID.toString())

        if (workspaceData.creatorId == userID) {
            binding.btnWorkspaceAddUser.visibility = View.VISIBLE
            binding.btnWorkspaceLeave.visibility = View.INVISIBLE
        }
    }

    fun leaveWorkspace() {
        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")
            .child(workspaceData.id.toString())
            .child("users")

        var dbRef: DatabaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")
            .child(workspaceData.id.toString())
            .child("users")

        var dataSnapshot: DataSnapshot

        databaseReference
            .get()
            .addOnSuccessListener {
                dataSnapshot = it

                for(d in dataSnapshot.children) {
                    var email = d.getValue(WorkspaceUser::class.java)?.email

                    if (email == mAuth.currentUser?.email) {
                        dbRef
                            .child(d.key.toString())
                            .removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Projekt opuszczono", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext, WorkspacesActivity::class.java))
                            }
                    }
                }
            }
    }

    fun showWorkspaceCreator() {
        var dataSnapshot: DataSnapshot

        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

        databaseReference
            .child(workspaceData.creatorId.toString())
            .get()
            .addOnSuccessListener {
                dataSnapshot = it

                var email = dataSnapshot.getValue(User::class.java)?.email

                binding.tvWorkspaceCreator.text = "Autor: ${email}"
            }


        workspaceData.creatorId
    }
}