package com.shubhamgupta16.shopmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.shubhamgupta16.shopmanager.common.valueString
import com.shubhamgupta16.shopmanager.databinding.ActivityAddProductBinding
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                bottom = systemBars.bottom, left = systemBars.left, right = systemBars.right
            )
            insets
        }

        binding.toolbar1.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.publishButton.setOnClickListener {
            process()
        }

        if (intent.hasExtra("model")) {
            changeToUpdateUI()
        }
    }

    private fun changeToUpdateUI() {
        val productModel: ProductModel = intent.getSerializableExtra("model") as ProductModel
        binding.inputAmount.editText?.setText(productModel.amount.valueString)
        binding.inputGst.editText?.setText(productModel.gst.valueString)
        binding.inputName.editText?.setText(productModel.name)
        binding.publishButton.text = getString(R.string.update)
        binding.toolbarTitle.text = getString(R.string.update_product)
        binding.publishButton.setOnClickListener {
            process(productModel.id)
        }
    }

    private fun process(productId: Int = 0) {
        val name = binding.inputName.editText?.text.toString().trim()
        val amount = binding.inputAmount.editText?.text.toString().trim()
        val gst = binding.inputGst.editText?.text.toString().trim()

        val productModel = ProductModel(
            productId, name, "", amount.toFloat(), gst.toFloat(), false, 1
        )

        if (validate(name, amount, gst)) {

            CoroutineScope(Dispatchers.IO).launch {
                val message = if (productId == 0) {
                    db.productDao().insert(productModel)
                    "Product Added Successfully!"
                } else {
                    db.productDao().update(productModel)
                    "Product Updated Successfully!"
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddProductActivity, message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
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