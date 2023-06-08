package com.example.planit

import android.content.Intent
import android.os.Bundle
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
    private val workspacesListAsUser = mutableListOf<Workspace?>()

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
        getWorkspacesAsUser()
    }

    // wyświetlenie projektów w których user jest założycielem
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
                    if (it.getValue(Workspace::class.java)?.creatorId.toString() == userID) {
                        workspacesList.add(it.getValue(Workspace::class.java))
                    }
                }

                var workspacesNames = arrayOf<String?>()
                var workspacesTypes = arrayOf<String?>()
                var workspacesIDs = arrayOf<String?>()

                workspacesList.forEach{
                    workspacesNames = workspacesNames.plus(it?.name)
                    workspacesTypes = workspacesTypes.plus(it?.type)
                    workspacesIDs = workspacesIDs.plus(it?.id)
                }

                /////////////////////////////////
                val workspacesListAdapter = WorkspacesListAdapter(this, workspacesNames, workspacesTypes, workspacesIDs)
                binding.lvWorkspaces.adapter = workspacesListAdapter

                binding.lvWorkspaces.setOnItemClickListener(){adapterView, view, position, id ->
                    val itemAtPos = adapterView.getItemAtPosition(position)
                    val ws = getWorkspaceByID(workspacesList, itemAtPos.toString())
                    val intention = Intent(applicationContext, WorkspaceDetailsActivity::class.java)

                    // tak jest prościej xD
                    intention.putExtra("ID", itemAtPos.toString())
                    intention.putExtra("Name", ws?.name)
                    intention.putExtra("CreationDate", ws?.creationDate)
                    intention.putExtra("CreatorID", ws?.creatorId)
                    intention.putExtra("Type", ws?.type)

                    startActivity(intention)
                }

                Toast.makeText(this, "Sukces!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Jebać ruch!!", Toast.LENGTH_SHORT).show()
            }
    }

    fun getWorkspaceByID(list: List<Workspace?>, id: String): Workspace? {
        val ws = list.find {
            workspace ->  workspace?.id.equals(id)
        }

        return ws
    }

    // wyświetlenie projektów w których user nie ma specjalnych uprawnień
    fun getWorkspacesAsUser() {
        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")

        var dataSnapshot: DataSnapshot
        val email = mAuth.currentUser?.email

        databaseReference
            .get()
            .addOnSuccessListener {
                dataSnapshot = it

                dataSnapshot.children.forEach{
                    var dupa = it.getValue(WorkspaceWithUsers::class.java)

                    if (dupa?.users?.values != null) {
                        val test = ArrayList(dupa?.users?.values)

                        for(user in test) {
                            if (user.email.toString() == email.toString()) {
                                val ws = Workspace(
                                    dupa.id,
                                    dupa.name,
                                    dupa.creationDate,
                                    dupa.creatorId,
                                    dupa.type
                                )

                                workspacesListAsUser.add(ws)
                            }
                        }
                    }
                }

                var workspacesNames = arrayOf<String?>()
                var workspacesTypes = arrayOf<String?>()
                var workspacesIDs = arrayOf<String?>()

                workspacesListAsUser.forEach{
                    workspacesNames = workspacesNames.plus(it?.name)
                    workspacesTypes = workspacesTypes.plus(it?.type)
                    workspacesIDs = workspacesIDs.plus(it?.id)
                }

                val workspacesListAdapter = WorkspacesListAdapter(this, workspacesNames, workspacesTypes, workspacesIDs)
                binding.lvWorkspacesUser.adapter = workspacesListAdapter

                binding.lvWorkspacesUser.setOnItemClickListener(){adapterView, view, position, id ->
                    val itemAtPos = adapterView.getItemAtPosition(position)
                    val ws = getWorkspaceByID(workspacesListAsUser, itemAtPos.toString())
                    val intention = Intent(applicationContext, WorkspaceDetailsActivity::class.java)

                    // tak jest prościej xD
                    intention.putExtra("ID", itemAtPos.toString())
                    intention.putExtra("Name", ws?.name)
                    intention.putExtra("CreationDate", ws?.creationDate)
                    intention.putExtra("CreatorID", ws?.creatorId)
                    intention.putExtra("Type", ws?.type)

                    startActivity(intention)
                }
            }
    }
}
