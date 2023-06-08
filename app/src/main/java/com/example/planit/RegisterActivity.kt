package com.example.planit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.planit.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private val utils = Utilities()

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
            val passwordRepeat = binding.etPasswordRepeat.text.toString()

            if (email.isEmpty() && password.isEmpty())
            {
                Toast.makeText(this, R.string.missing_inputs, Toast.LENGTH_SHORT).show();
            }
            else if (password.length < 8)
            {
                Toast.makeText(this, R.string.password_too_short, Toast.LENGTH_SHORT).show();
            }
            else if (password != passwordRepeat)
            {
                Toast.makeText(this, R.string.passwords_not_matching, Toast.LENGTH_SHORT).show()
            }
            else if (!utils.validateEmail(email))
            {
                Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.w("TAG", "signInWithCustomToken:failure", task.exception)
            Toast.makeText(this, R.string.register_failure, Toast.LENGTH_SHORT).show();
        }
        })
    }
}