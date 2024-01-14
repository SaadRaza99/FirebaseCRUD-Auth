package com.example.androidtaskapplication

import android.content.Context
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

class Validation {
    companion object{
        public fun emailValidator(context:Context, textInputLayout: TextInputLayout, emailAddress: String): Boolean {
            return if (emailAddress.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                true
            } else {
                textInputLayout.error = context.resources.getString(R.string.email_address_not_valid)
                false
            }
        }

        public fun passwordValidator(context:Context, textInputLayout: TextInputLayout, password: String): Boolean {
            return if (password.isNotEmpty()) {
                true
            } else {
                textInputLayout.error = context.resources.getString(R.string.password_not_valid)
                false
            }
        }

        public fun passwordCharacters(context:Context, textInputLayout: TextInputLayout, password: String): Boolean {
            return if (password.isNotEmpty() && password.chars().count()>=6) {
                true
            } else {
                textInputLayout.error = context.resources.getString(R.string.password_character_count_error)
                false
            }
        }
    }
}