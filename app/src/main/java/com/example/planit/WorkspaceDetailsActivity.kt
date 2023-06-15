package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.planit.databinding.ActivityWorkspaceDetailsBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class WorkspaceDetailsActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityWorkspaceDetailsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var query: DatabaseReference
    private lateinit var users: DatabaseReference
    lateinit var workspaceData: Workspace
    private lateinit var postAdapterDB: PostAdapterDB
    val GALLERY_INTENT_CALLED = 1
    val GALLERY_KITKAT_INTENT_CALLED = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workspace_details)

        binding = ActivityWorkspaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        val getIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        workspaceData = Workspace(
            intent.getStringExtra("ID"),
            intent.getStringExtra("Name"),
            intent.getStringExtra("CreationDate"),
            intent.getStringExtra("CreatorID"),
            intent.getStringExtra("Type")
        )

        query = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
        getReference("Workspaces").child(workspaceData.id.toString()).child("posts")

        var sorted = query.orderByChild("date")

        users = FirebaseDatabase.getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/").
        getReference("Workspaces").child(workspaceData.id.toString()).child("users")

        readAllPosts()

        Log.i("WORKSPACEID", workspaceData.id.toString())

        val options: FirebaseRecyclerOptions<Post> = FirebaseRecyclerOptions.Builder<Post>().
        setQuery(sorted, Post::class.java).setLifecycleOwner(this).build()

        Log.i("ILE", options.snapshots.size.toString())

        binding.recyclerView.layoutManager = CustomLayoutManager(this, 1)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        postAdapterDB = PostAdapterDB(options, query, users, workspaceData.id.toString())
        binding.recyclerView.adapter = postAdapterDB

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
                    Toast.makeText(this, "Bląd podczas usuwania!!", Toast.LENGTH_SHORT).show()
                }
        })

        binding.btnWorkspaceTasks.setOnClickListener(View.OnClickListener {

        })

        binding.btnWorkspaceUsers.setOnClickListener(View.OnClickListener {
            showFragment(WorkspaceUsersFragment(), R.id.fragmentWorkspaceUsers)
            binding.recyclerView.visibility = View.GONE
        })

        binding.btnWorkspaceAddUser.setOnClickListener(View.OnClickListener {
            showFragment(AddUserFragment(), R.id.fragmentAddUser)
            binding.recyclerView.visibility = View.GONE
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

    fun showFragment(fragment: Fragment, id: Int)
    {
        val fragmentBundle = Bundle()
        fragmentBundle.putString("workspaceID", workspaceData.id)
        fragmentBundle.putString("creatorID", workspaceData.creatorId)
        fragment.arguments = fragmentBundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment)
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

    fun readAllPosts()
    {
        query.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for (i in snapshot.children)
                {
                    Log.i("NAME1", i.getValue(Post::class.java)?.id.toString())
                    Log.i("NAME2", i.getValue(Post::class.java)?.name.toString())
                }
            }
            override fun onCancelled(error: DatabaseError)
            {
                Log.e("EŁROR", error.message)
            }
        })
    }
    fun showRecyclerView()
    {
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onResume()
    {
        super.onResume()
        postAdapterDB.notifyDataSetChanged()
    }
}


