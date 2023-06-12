package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddUserFragment : Fragment() {
    private lateinit var viewOfLayout: View
    lateinit var buttonAdd: Button
    lateinit var buttonClose: Button
    lateinit var email: EditText
    private val utils = Utilities()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_add_user, container, false)

        buttonAdd = viewOfLayout.findViewById(R.id.btn_add_user)
        buttonClose = viewOfLayout.findViewById(R.id.btn_close)
        email = viewOfLayout.findViewById(R.id.et_email)
        mAuth = FirebaseAuth.getInstance()

        buttonAdd.setOnClickListener(
            View.OnClickListener
        {
            if (email.text.toString().isNotEmpty() && utils.validateEmail(email.text.toString()))
            {
                showUsersList(
                    viewOfLayout.findViewById<TextView>(R.id.et_email).text.toString(),
                    viewOfLayout.findViewById<Spinner>(R.id.sp_role).selectedItem.toString()
                )
            }
            else {
                Toast.makeText(activity, "Proszę podaj poprawny email użytkownika", Toast.LENGTH_SHORT).show()
            }
        })

        buttonClose.setOnClickListener(View.OnClickListener {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()

            (context as PostRemoval).showRecyclerView()
        })

        setSpinner(viewOfLayout.findViewById(R.id.sp_role))
        return viewOfLayout
    }

    fun setSpinner(spinner: Spinner)
    {
        ArrayAdapter.createFromResource(
            requireActivity().applicationContext,
            R.array.workspace_roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun showUsersList(email: String, role: String) {
        val workspaceID = arguments?.get("workspaceID").toString()

        userInWorkspace(email, role, workspaceID)
    }

    // Sprawdzenie czy user już nie jest w projekcie
    fun userInWorkspace(email: String, role: String, workspaceID: String) {
        var dataSnapshot: DataSnapshot
        var userInWorkspace: Boolean = false

        databaseReference
            .child(workspaceID)
            .child("users")
            .get()
            .addOnSuccessListener {
                dataSnapshot = it

                for (child in dataSnapshot.children) {
                    var test = child.getValue(WorkspaceUser::class.java)

                    if (test?.email.toString() == email) {
                        userInWorkspace = true;
                    }
                }

                if (!userInWorkspace) {
                    addUserToWorkspace(email, role, workspaceID)
                }
                else {
                    Toast.makeText(requireActivity().applicationContext, "Ten użytkownik jest już w projekcie!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    // Dodanie usera do projektu
    fun addUserToWorkspace(email: String, role: String, workspaceID: String) {
        val newWorkspaceUser = WorkspaceUser(
            email,
            role
        )

        databaseReference
            .child(workspaceID)
            .child("users")
            .push()
            .setValue(newWorkspaceUser)
            .addOnSuccessListener{
                Toast.makeText(requireActivity().applicationContext, "Dodano użytkownika ${email} do projektu!", Toast.LENGTH_SHORT).show()
                fragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
            .addOnFailureListener{
                Toast.makeText(requireActivity().applicationContext, "Jebać ruch!!", Toast.LENGTH_SHORT).show()
            }
    }
}