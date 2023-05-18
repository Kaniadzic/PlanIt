package com.example.planit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordChangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordChangeFragment : Fragment()
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    lateinit var buttonChange: Button
    lateinit var buttonClose: Button
    lateinit var oldPassword: EditText
    lateinit var newPassword: EditText
    lateinit var newPasswordRep: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_password_change, container, false)
        mAuth = FirebaseAuth.getInstance()

        oldPassword = viewOfLayout.findViewById(R.id.tv_oldpass)
        newPassword = viewOfLayout.findViewById(R.id.tv_newpass)
        newPasswordRep = viewOfLayout.findViewById(R.id.tv_newpassrepeat)
        buttonChange = viewOfLayout.findViewById(R.id.btn_change)
        buttonClose = viewOfLayout.findViewById(R.id.btn_close)

        buttonChange.setOnClickListener(View.OnClickListener
        {
            if (newPassword.text.toString().equals(newPasswordRep.text.toString()))
                mAuth.currentUser?.updatePassword(newPassword.text.toString())
        })

        buttonClose.setOnClickListener(View.OnClickListener
        {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            Log.i("DUPA", "dupa")
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
         * @return A new instance of fragment PasswordChangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordChangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}