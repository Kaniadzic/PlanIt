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
import com.google.firebase.auth.EmailAuthProvider
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
            var oldPass: String = oldPassword.text.toString()
            var newPass: String = newPassword.text.toString()
            var newPassRep: String = newPasswordRep.text.toString()
            var credential = mAuth.currentUser?.email?.let { it1 -> EmailAuthProvider.getCredential(it1, oldPass) }

            if (oldPass.isEmpty() && newPass.isEmpty() && newPassRep.isEmpty())
            {
                Toast.makeText(context, "Wypełnij wszystkie pola!", Toast.LENGTH_LONG).show()
            }
            else if (newPass != newPassRep)
            {
                Log.i("HASLO1", newPass)
                Log.i("HASLO2", newPassRep)
                Toast.makeText(context, "Hasła się różnią", Toast.LENGTH_LONG).show()
            }
            else
            {
                if (credential != null)
                {
                    mAuth.currentUser?.reauthenticate(credential)?.addOnCompleteListener{ task ->
                        if (task.isSuccessful)
                        {
                            mAuth.currentUser!!.updatePassword(newPass).addOnCompleteListener { task ->

                                if (task.isSuccessful)
                                {
                                    Toast.makeText(context, "Hasło zostało zmienione!", Toast.LENGTH_SHORT).show()
                                    fragmentManager?.beginTransaction()?.remove(this)?.commit()
                                    (activity as EditActivity).show()
                                }
                                else
                                {
                                    Toast.makeText(context, "Zle haslo!", Toast.LENGTH_SHORT).show()
                                    Log.w("ERROR", task.exception)
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(context, "FAIL!", Toast.LENGTH_SHORT).show()
                            Log.w("Zle haslo!", task.exception)
                        }
                    }
                }
            }
        })

        buttonClose.setOnClickListener(View.OnClickListener
        {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            (activity as EditActivity).show()
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