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
 * Use the [EmailChangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailChangeFragment : Fragment()
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View
    lateinit var buttonChange: Button
    lateinit var buttonClose: Button
    lateinit var oldEmail: EditText
    lateinit var newEmail: EditText
    lateinit var newEmailRep: EditText
    private lateinit var mAuth: FirebaseAuth
    var test: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_email_change, container, false)
        mAuth = FirebaseAuth.getInstance()

        oldEmail = viewOfLayout.findViewById(R.id.tv_oldmail)
        newEmail = viewOfLayout.findViewById(R.id.tv_newmail)
        newEmailRep = viewOfLayout.findViewById(R.id.tv_newmailrepeat)
        buttonChange = viewOfLayout.findViewById(R.id.btn_change)
        buttonClose = viewOfLayout.findViewById(R.id.btn_close)

        buttonChange.setOnClickListener(View.OnClickListener
        {
            var oldMail: String = oldEmail.text.toString()
            var newMail: String = newEmail.text.toString()
            var newMailRep: String = newEmailRep.text.toString()

            if (oldMail.isEmpty() && newMail.isEmpty() && newMailRep.isEmpty())
            {
                Toast.makeText(context, "Wypełnij wszystkie pola!", Toast.LENGTH_LONG).show()
            }
            else if (newMail != newMailRep)
            {
                Toast.makeText(context, "Maile się różnią", Toast.LENGTH_LONG).show()
            }
            else
            {
                mAuth.currentUser?.verifyBeforeUpdateEmail(newMail)
                mAuth.currentUser?.updateEmail(newMail)
                fragmentManager?.beginTransaction()?.remove(this)?.commit()
                (activity as EditActivity).show()
                Toast.makeText(context, "Mail został pomyślnie zmienione", Toast.LENGTH_LONG).show()
            }
        })

        buttonClose.setOnClickListener(View.OnClickListener
        {
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
            (activity as EditActivity).show()
            Log.i("STAN FRAGMENTU", "ZAMKNIETY")
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
         * @return A new instance of fragment EmailChangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmailChangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}