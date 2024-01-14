package com.example.androidtaskapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtaskapplication.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()
        binding.btnrecoverpassword.setOnClickListener {
            if (!Validation.emailValidator(
                    this,
                    binding.etLayoutEmail,
                    binding.editTextUsername.text.toString()
                )
            ) {
                return@setOnClickListener
            }
            val email: String = binding.editTextUsername.text.toString().trim()
            beginRecovery(email)
        }

    }

    private fun beginRecovery(email: String) {
        mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // if isSuccessful then done message will be shown
                // and you can change the password
                Toast.makeText(this@ForgetPasswordActivity, "Open Email and Rest Your Password", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this@ForgetPasswordActivity, "Error Occurred", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this@ForgetPasswordActivity, "Error Failed", Toast.LENGTH_LONG).show()
        }
    }
}