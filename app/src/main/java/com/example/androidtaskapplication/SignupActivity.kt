package com.example.androidtaskapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.androidtaskapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etLayoutEmail.error = ""
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etLayoutPassword.error = ""
            }
        })
        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etLayoutConfirmPassword.error = ""
            }
        })
        binding.buttonLogin.setOnClickListener{
            signup()
        }
    }

    private fun signup(){
        if(!Validation.emailValidator(this, binding.etLayoutEmail,binding.etEmail.text.toString())){
            return
        }
        if(!Validation.passwordValidator(this, binding.etLayoutPassword,binding.etPassword.text.toString())){
            return
        }
        if(!Validation.passwordCharacters(this, binding.etLayoutPassword,binding.etPassword.text.toString())){
            return
        }
        if(!Validation.passwordValidator(this, binding.etLayoutConfirmPassword,binding.etConfirmPassword.text.toString())){
            return
        }
        if(!Validation.passwordCharacters(this, binding.etLayoutConfirmPassword,binding.etConfirmPassword.text.toString())){
            return
        }
        if(binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
            Toast.makeText(
                baseContext,
                resources.getString(R.string.password_not_matched),
                Toast.LENGTH_SHORT,
            ).show()
           return
        }
        callSignup()
    }

    private fun callSignup() {
        //Progress start
        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    finish()
                    //Progress end
                    //Move forward
                } else {
                    // If sign in fails, display a message to the user.
                    //Progress end
                    Toast.makeText(
                        baseContext,
                        task.exception?.message.toString(),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}