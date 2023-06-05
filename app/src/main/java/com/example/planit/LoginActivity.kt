package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.planit.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegisteract.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()
        })

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(View.OnClickListener
        {

            val email = binding.etEmail2.text.toString()
            val password = binding.etPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty())
            {
                login(email, password)
            }
            else
            {
                Toast.makeText(this, "Wypełnij wszystkie pola ośle!", Toast.LENGTH_SHORT).show();
            }
        })

        binding.btnForgottenPassword.setOnClickListener(View.OnClickListener {
            showFragment(PasswordResetFragment())
        })
    }

    fun login(email: String, password: String)
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(OnCompleteListener
        {
                task -> if (task.isSuccessful)
        {
            Log.d("TAG", "signInWithCustomToken:success")
            Toast.makeText(this, "Zalogowano pomyślnie.", Toast.LENGTH_SHORT).show();
            startActivity(Intent(applicationContext, HomepageActivity::class.java))
            finish()
        }
        else
        {
            Log.w("TAG", "signInWithCustomToken:failure", task.exception)
            Toast.makeText(this, "Niepoprawne hasło lub adres email!", Toast.LENGTH_SHORT).show();
        }
        })
    }

    fun showFragment(fragment: Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentDetails, fragment)
        fragmentTransaction.addToBackStack("theFragment")
        fragmentTransaction.commit()
    }
}