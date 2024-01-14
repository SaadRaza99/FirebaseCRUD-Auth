package com.example.androidtaskapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtaskapplication.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"
    private var usersRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        usersRef = FirebaseDatabase.getInstance().getReference().child("users")
        binding.buttonLogin.setOnClickListener {
            if(!Validation.emailValidator(this, binding.etLayoutEmail,binding.editTextUsername.text.toString())){
                return@setOnClickListener
            }
            if(!Validation.passwordValidator(this, binding.etLayoutPassword,binding.editTextPassword.text.toString())){
                return@setOnClickListener
            }
            if(!Validation.passwordCharacters(this, binding.etLayoutPassword,binding.editTextPassword.text.toString())){
                return@setOnClickListener
            }


            signIn(binding.editTextUsername.text.toString(),binding.editTextPassword.text.toString())
        }
        binding.forgetpassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.signup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        binding.dashboardguest.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    saveFcmTokenToFirebase(user)
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)

                  //  updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                 //   updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun saveFcmTokenToFirebase(user: FirebaseUser?) {

        if (user != null) {
            // Obtain the FCM token
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task: Task<String?> ->
                    if (task.isSuccessful) {
                        val fcmToken = task.result

                        // Save the FCM token to Firebase
                        if (fcmToken != null) {
                            usersRef!!.child(user.uid).child("fcmToken").setValue(fcmToken)
                        }
                    } else {
                        // Handle the error
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user!=null){
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}