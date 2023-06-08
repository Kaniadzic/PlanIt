package com.example.planit

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WorkspaceUsersFragment : Fragment() {
    private lateinit var viewOfLayout: View
    lateinit var buttonClose: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        showUsers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_workspace_users, container, false)

        buttonClose = viewOfLayout.findViewById(R.id.btn_close)

        buttonClose.setOnClickListener(View.OnClickListener {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        })

        return viewOfLayout
    }

    fun showUsers() {
        var dataSnapshot: DataSnapshot
        var dataSnapshotUsers: DataSnapshot
        val workspaceID = arguments?.get("workspaceID").toString()

//        databaseReference = FirebaseDatabase
//            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
//            .getReference("Users")
//
//        // userzy
//        databaseReference
//            .get()
//            .addOnSuccessListener {
//                dataSnapshotUsers = it
//
//                for (d in dataSnapshotUsers.children) {
//                    var dupa = d.getValue(User::class.java)?.email
//                    Log.e("u e m a i l", dupa.toString())
//                }
//            }

        databaseReference = FirebaseDatabase
            .getInstance("https://planit-79310-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Workspaces")

        val users: ArrayList<String> = ArrayList<String>()

        // maile w workspace
        databaseReference
            .child(workspaceID)
            .child("users")
            .get()
            .addOnSuccessListener {
                dataSnapshot = it

                for(d in dataSnapshot.children) {
                    var test = d.getValue(WorkspaceUser::class.java)?.email
                    users.add(test.toString())
                }
            }


    }
}