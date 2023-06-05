package com.example.planit

import android.util.Patterns

class Utilities {

    // Walidacja adresu email
    fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true
        }

        return false
    }
}