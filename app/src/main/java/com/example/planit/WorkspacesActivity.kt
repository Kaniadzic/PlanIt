package com.example.planit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.planit.databinding.ActivityHomepageBinding
import com.example.planit.databinding.ActivityWorkspacesBinding
import com.google.firebase.auth.FirebaseAuth

class WorkspacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkspacesBinding
    private lateinit var mAuth: FirebaseAuth

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
    }
}