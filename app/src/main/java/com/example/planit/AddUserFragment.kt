package com.example.planit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class AddUserFragment : Fragment() {
    private lateinit var viewOfLayout: View
    private lateinit var mAuth: FirebaseAuth
    lateinit var buttonAdd: Button
    lateinit var buttonClose: Button
    lateinit var email: EditText
    private val utils = Utilities()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
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
            if (email.text.toString().isNotEmpty() && !utils.validateEmail(email.text.toString()))
            {
                Toast.makeText(activity, "Dodanie usera do projektu", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(activity, "Proszę podaj email użytkownika", Toast.LENGTH_SHORT).show()
            }
        })

        buttonClose.setOnClickListener(View.OnClickListener {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
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
}