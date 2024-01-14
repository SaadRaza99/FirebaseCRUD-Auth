package com.example.androidtaskapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidtaskapplication.databinding.ActivityDashboardBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage


class DashboardActivity : AppCompatActivity(),
    ProductRVAdapter.ProductClickInterface {
    private lateinit var binding: ActivityDashboardBinding
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    private lateinit var productRVModalArrayList: ArrayList<ProdectRVModal>
    private lateinit var auth: FirebaseAuth
    var user:FirebaseUser?=null
    private var productRVAdapter: ProductRVAdapter? = null
    var senderToken=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        user=auth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance()

        // on below line creating our database reference.
        databaseReference = firebaseDatabase?.getReference("Products")
        productRVModalArrayList = ArrayList()

        productRVAdapter = ProductRVAdapter(productRVModalArrayList, this, this)

        binding.idRVProducts.setLayoutManager(LinearLayoutManager(this))
        // setting adapter to recycler view on below line.
        binding.idRVProducts.setAdapter(productRVAdapter)
        binding.buttonAdd.setOnClickListener {
            showAddDialog()
        }

        if(user==null)
            binding.buttonLogout.visibility = View.GONE
        binding.buttonLogout.setOnClickListener {
            showLogoutDialog()
        }
        getSenderToken()
    }

    private fun getSenderToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (task.isSuccessful) {
                    senderToken = task.result.toString()
                    // Now, you can send this token to your server and associate it with the user account.
                } else {
                    // Handle the error
                }
            }
    }

    override fun onResume() {
        super.onResume()
        getProducts()
    }

    private fun getProducts() {
        // on below line clearing our list.
        productRVModalArrayList.clear()
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                // on below line we are hiding our progress bar.
                // adding snapshot to our array list on below line.
                for(ds in snapshot.children) {
                    productRVModalArrayList.add(ds.getValue(ProdectRVModal::class.java)!!)
                }
                // notifying our adapter that data has changed.
                productRVAdapter?.notifyDataSetChanged()
                databaseReference?.removeEventListener(this)
            }

            override fun onCancelled(@NonNull error: DatabaseError) {
                databaseReference?.removeEventListener(this)
            }
        })
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextPrice = dialogView.findViewById<EditText>(R.id.editTextPrice)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Enter Item Details")
            .setPositiveButton("Submit") { dialog, _ ->
                // Handle the submission here
                val name = editTextName.text.toString()
                val price =editTextPrice.text.toString()

                // Perform actions with input values (you can modify this part)
               if (name.isEmpty() || price.isEmpty()){
                   showToast("Please enter data in both fileds")
               }
                else{
                   val productRVModal = ProdectRVModal(
                       name,
                       name,
                       price
                   )
                   databaseReference!!.addValueEventListener(object : ValueEventListener {
                       override fun onDataChange(@NonNull snapshot: DataSnapshot) {
                           // on below line we are setting data in our firebase database.
                           databaseReference!!.child(name).setValue(productRVModal)
                           // displaying a toast message.
                           Toast.makeText(
                               this@DashboardActivity,
                               "Product Added..",
                               Toast.LENGTH_SHORT
                           ).show()
                           databaseReference?.removeEventListener(this)
                           getProducts()
                           // starting a main activity.
                           val recipientTokens: MutableList<String> =
                               getRecipientTokens(senderToken) // Implement this method to get tokens excluding the sender



// Create notification payload
                           val data: MutableMap<String, String> = HashMap()
                           data["title"] = "Your notification title"
                           data["body"] = "Notification message"


// Exclude sender's token
                           recipientTokens.remove(senderToken)


// Create FCM message
                           for (recipientToken in recipientTokens) {
                               FirebaseMessaging.getInstance().send(
                                   RemoteMessage.Builder(recipientToken!!)
                                       .setData(data)
                                       .build()
                               )
                           }    
                       }

                       override fun onCancelled(@NonNull error: DatabaseError) {
                           // displaying a failure message on below line.
                           Toast.makeText(
                               this@DashboardActivity,
                               "Fail to add Product..",
                               Toast.LENGTH_SHORT
                           ).show()
                           databaseReference?.removeEventListener(this)
                       }
                   })
                   dialog.dismiss()
                }


            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }



    private fun getRecipientTokens(senderUserId: String): MutableList<String> {
        val recipientTokens: MutableList<String> = ArrayList()
        val usersRef = FirebaseDatabase.getInstance().reference.child("users")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val userId = userSnapshot.key

                    // Exclude the sender's token
                    if (userId != senderUserId) {
                        val fcmToken = userSnapshot.child("fcmToken").getValue(
                            String::class.java
                        )
                        if (fcmToken != null) {
                            recipientTokens.add(fcmToken)
                        }
                    }
                }

                // Now you can use recipientTokens to send notifications
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                // Handle the error
            }
        })
        return recipientTokens
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onProductViewClick(position: Int) {
        if (user!=null){
            val i = Intent(this@DashboardActivity, ViewProductActicity::class.java)
            i.putExtra("product",productRVModalArrayList.get(position))
            startActivity(i)
        }
        else{
            Toast.makeText(this, getString(R.string.you_are_unauthorised),Toast.LENGTH_SHORT).show()
        }

    }

    override fun onProductEditClick(position: Int) {
        if (user!=null){
            val i = Intent(this@DashboardActivity, EditProductActivity::class.java)
            i.putExtra("product",productRVModalArrayList.get(position))
            startActivity(i)
        }
        else{
            Toast.makeText(this, getString(R.string.you_are_unauthorised),Toast.LENGTH_SHORT).show()
        }

    }

    override fun onProductDeleteClick(position: Int) {
        if (user!=null){
            showdeletedialog(position)
        }
        else{
            Toast.makeText(this, getString(R.string.you_are_unauthorised),Toast.LENGTH_SHORT).show()
        }
    }

    private fun showdeletedialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete this item?")

        // Positive Button (Yes)

        // Positive Button (Yes)
        builder.setPositiveButton("Yes") { dialog, which -> // Handle the deletion logic here
            deleteItem(position)
        }

        // Negative Button (No)

        // Negative Button (No)
        builder.setNegativeButton("No") { dialog, which -> // Do nothing or handle cancel logic
            Toast.makeText(applicationContext, "Deletion canceled", Toast.LENGTH_SHORT).show()
        }

        // Create and show the AlertDialog

        // Create and show the AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteItem(position: Int) {
       val databaseReferencefordelete = firebaseDatabase?.getReference("Products")?.child(productRVModalArrayList.get(position).productId);
        databaseReferencefordelete?.removeValue()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Item removed from Firebase, now update RecyclerView
                productRVModalArrayList.removeAt(position)
                productRVAdapter?.notifyItemRemoved(position)
                Toast.makeText(this, "Product Deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")

        // Positive Button (Yes)

        // Positive Button (Yes)
        builder.setPositiveButton("Yes") { dialog, which -> // Handle the deletion logic here
            logout()
        }

        // Negative Button (No)

        // Negative Button (No)
        builder.setNegativeButton("No") { dialog, which -> // Do nothing or handle cancel logic
        }

        // Create and show the AlertDialog

        // Create and show the AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun logout(){
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}