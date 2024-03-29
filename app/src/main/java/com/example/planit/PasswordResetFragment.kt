package com.example.planit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordReset.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordResetFragment: Fragment()
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var buttonSend: Button
    lateinit var buttonClose: Button
    lateinit var email: EditText
    private lateinit var viewOfLayout: View
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_password_reset, container, false)

        buttonSend = viewOfLayout.findViewById(R.id.btn_reset)
        buttonClose = viewOfLayout.findViewById(R.id.btn_close)
        email = viewOfLayout.findViewById(R.id.tv_emailreset)
        mAuth = FirebaseAuth.getInstance()

        buttonSend.setOnClickListener(View.OnClickListener
        {
            if (email.text.toString().isNotEmpty())
            {
                mAuth.sendPasswordResetEmail(email.text.toString())
                Toast.makeText(activity, "Wysłano link resetujący na podany adres email!", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(activity, "Proszę wpisac email!", Toast.LENGTH_SHORT).show()
        })

        buttonClose.setOnClickListener(View.OnClickListener {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            Log.i("ZAMKNIETO", "FRAGMENT")
        })

        return viewOfLayout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordResetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}