package com.example.androidtaskapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtaskapplication.databinding.ActivityEditProductBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditProductActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditProductBinding
    private var productID: String? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var productRVModal: ProdectRVModal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firebaseDatabase = FirebaseDatabase.getInstance()
        productRVModal = getIntent().getParcelableExtra("product")
        if (productRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            binding.idEdtProductName.setText(productRVModal?.productName)
            binding.idEdtProductPrice.setText(productRVModal?.productPrice)
            productID = productRVModal?.productId
        }
        databaseReference = firebaseDatabase?.getReference("Products")?.child(productID!!)
        binding.btnupdate.setOnClickListener {
            if (binding.idEdtProductName.text!!.isEmpty()||binding.idEdtProductPrice.text!!.isEmpty()){
                Toast.makeText(this,"Product Name and Price cant be empty",Toast.LENGTH_SHORT).show()
            }
            else{
                val map: MutableMap<String, Any> = HashMap()
                map["productName"] = binding.idEdtProductName.text.toString()
                map["productPrice"] = binding.idEdtProductPrice.text.toString()
                map["productId"] = binding.idEdtProductName.text.toString()
                databaseReference!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull snapshot: DataSnapshot) {

                        // adding a map to our database.
                        databaseReference!!.updateChildren(map)
                        // on below line we are displaying a toast message.
                        Toast.makeText(
                            this@EditProductActivity,
                            "Product Updated..",
                            Toast.LENGTH_SHORT
                        ).show()
                        databaseReference?.removeEventListener(this)
                        finish()
                    }

                    override fun onCancelled(@NonNull error: DatabaseError) {
                        // displaying a failure message on toast.
                        Toast.makeText(
                            this@EditProductActivity,
                            "Fail to update Product..",
                            Toast.LENGTH_SHORT
                        ).show()
                        databaseReference?.removeEventListener(this)
                    }
                })
            }
        }
    }
}