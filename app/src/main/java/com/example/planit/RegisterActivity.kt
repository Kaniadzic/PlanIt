package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.planit.databinding.ActivityMainBinding
import com.example.planit.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginact.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        })

        mAuth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener(View.OnClickListener
        {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() && password.isEmpty())
            {
                Toast.makeText(this, "Wypełnij wszystkie pola ośle!", Toast.LENGTH_SHORT).show();
            }
            else if (password.length < 8)
            {
                Toast.makeText(this, "Hasło powinno zawierać minimum 8 znaków", Toast.LENGTH_SHORT).show();
            }
            else
            {
                register(email, password)
            }
        })
    }

    fun register(email: String, password: String)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(OnCompleteListener
        {
                task -> if (task.isSuccessful)
        {
            Log.d("TAG", "signInWithCustomToken:success")
            Toast.makeText(this, "Zarejestrowano pomyślnie!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.w("TAG", "signInWithCustomToken:failure", task.exception)
            Toast.makeText(this, "Rejestracja nieudana!", Toast.LENGTH_SHORT).show();
        }
        })
    }
}