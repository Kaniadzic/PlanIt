package com.example.planit

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UsersListAdapter (
        private val context: Activity,
        private val emails: ArrayList<String?>,
    )
    : ArrayAdapter<String>(context, R.layout.list_users, emails) {

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            val inflater = context.layoutInflater
            val rowView = inflater.inflate(R.layout.list_users, null, true)

            val emailText = rowView.findViewById(R.id.tv_email_user) as TextView

            emailText.text = emails[position]

            return rowView
        }
}