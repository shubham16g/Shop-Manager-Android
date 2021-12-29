package com.shubhamgupta16.shopmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shubhamgupta16.shopmanager.databinding.ActivityAddProductBinding
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import com.shubhamgupta16.shopmanager.models.ProductModel

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.publishButton.setOnClickListener {
            addProduct()
        }
    }

    private fun addProduct() {
        val name = binding.inputName.editText?.text.toString().trim()
        val amount = binding.inputAmount.editText?.text.toString().trim()
        val gst = binding.inputGst.editText?.text.toString().trim()

        if (validate(name, amount, gst)) {
            db.productDao().insert(
                ProductModel(
                    0, name, "", amount.toFloat(),
                    gst.toFloat(), false, 1
                )
            )
            Toast.makeText(this, "Product Added Successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun validate(name: String, amount: String, gst: String): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            isValid = false
            binding.inputName.error = "Product Name Required"
        }
        if (amount.isEmpty()) {
            isValid = false
            binding.inputAmount.error = "Amount Required"
        } else if (amount.toFloatOrNull() == 0f) {
            isValid = false
            binding.inputAmount.error = "Amount cannot be 0"
        }
        if (gst.isEmpty()) {
            isValid = false
            binding.inputGst.error = "Enter 0 if not Required"
        }
        return isValid
    }
}