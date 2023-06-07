package com.example.planit

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class WorkspacesListAdapter (
    private val context: Activity,
    private val name: Array<String?>,
    private val type: Array<String?>,
    private val workspaceIDs: Array<String?>
    )
    : ArrayAdapter<String>(context, R.layout.list_workspaces, workspaceIDs) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_workspaces, null, true)

        val titleText = rowView.findViewById(R.id.name) as TextView
        val subtitleText = rowView.findViewById(R.id.type) as TextView
//        val id = rowView.findViewById(R.id.id) as TextView

        titleText.text = name[position]
        subtitleText.text = type[position]
//        id.text = workspaceIDs[position]

        return rowView
    }
}