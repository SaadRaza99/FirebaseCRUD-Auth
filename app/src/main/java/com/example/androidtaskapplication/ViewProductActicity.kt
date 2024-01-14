package com.example.androidtaskapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidtaskapplication.databinding.ActivityViewProductActicityBinding

class ViewProductActicity : AppCompatActivity() {
    var productRVModal: ProdectRVModal? = null
    private lateinit var binding: ActivityViewProductActicityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductActicityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        productRVModal = getIntent().getParcelableExtra("product")
        if (productRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            binding.idTVProductName.text = "Product Name:${productRVModal?.productName}"
            binding.idTVProductPrice.setText("Product Price: Rs${productRVModal?.productPrice}")
        }
    }
}