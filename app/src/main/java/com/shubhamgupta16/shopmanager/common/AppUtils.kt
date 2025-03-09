package com.shubhamgupta16.shopmanager.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.shubhamgupta16.shopmanager.databinding.LayoutEditAmountBinding
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.models.SellModel
import com.shubhamgupta16.shopmanager.room.AppDatabase.Companion.db
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun Context.showEditAmountDialog(
    model: SellModel,
    positiveBtnListener: (string: Double, dialog: DialogInterface, i: Int) -> Unit,
) {

    val binding: LayoutEditAmountBinding =
        LayoutEditAmountBinding.inflate(LayoutInflater.from(this))

    binding.inputAmount.editText?.setText(model.amount.valueString)
    val dialog = alertDialogBuilder("Edit Price", model.name, "DONE", null)
        .setView(binding.root).create()

    dialog.setOnShowListener {
        val positiveButton: Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            when (val input = binding.inputAmount.editText?.text.toString().toDoubleOrNull()) {
                null -> binding.inputAmount.error = "Amount Required"
                0.0 -> binding.inputAmount.error = "Amount cannot be 0"
                else -> {
                    positiveBtnListener(input, dialog, 0)
                    dialog.dismiss()
                }
            }
        }
    }
    dialog.show()

}

fun Context.showDeleteProductAlert(productModel: ProductModel, successListener: () -> Unit) {
    alertDialogBuilder("Delete Product", "Are you sure to delete this product",
        "YES", { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                db.productDao().delete(productModel)
                withContext(Dispatchers.Main) {
                    successListener()
                }
            }
        }).create().show()
}

