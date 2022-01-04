package com.shubhamgupta16.shopmanager.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.shubhamgupta16.shopmanager.R

class AddCartButton @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {
    var count = 0
        set(value) {
            field = value
            updateUI()
        }

    var limit = -1
        set(value) {
            field = value
            updateUI()
        }
    var onCountChangeListener: ((count: Int) -> Boolean)? = null

    private var isDisabled = false
    private val positiveButton: ImageButton
    private val negativeButton: ImageButton
    private val addButton: View
    private val countText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_add_cart_button, this, true)
        positiveButton = findViewById(R.id.positiveButton)
        negativeButton = findViewById(R.id.negativeButton)
        countText = findViewById(R.id.countText)
        addButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            if (onCountChangeListener != null) {
                if (onCountChangeListener!!(count + 1))
                    count++
            } else
                count++
        }
        positiveButton.setOnClickListener {
            if (onCountChangeListener != null) {
                if (onCountChangeListener!!(count + 1))
                    count++
            } else
                count++
        }
        negativeButton.setOnClickListener {
            if (count <= 0) return@setOnClickListener
            if (onCountChangeListener != null) {
                if (onCountChangeListener!!(count - 1))
                    count--
            } else
                count--
        }
        updateUI()
    }

    override fun setEnabled(isEnabled: Boolean) {
        isDisabled = !isEnabled
    }

    private fun updateUI() {
        if (count > 0) {
            negativeButton.visibility = VISIBLE
            positiveButton.visibility = VISIBLE
            countText.visibility = VISIBLE
            addButton.visibility = GONE
            countText.text = count.toString()
            if (isDisabled) {
                negativeButton.isEnabled = true
                negativeButton.setImageResource(R.drawable.ic_sell_delete_20dp)
                positiveButton.isEnabled = false
                addButton.isEnabled = false
                countText.isEnabled = false
            } else {
                negativeButton.isEnabled = true
                positiveButton.isEnabled = true
                addButton.isEnabled = true
                countText.isEnabled = true
                if (count == 1) {
                    negativeButton.setImageResource(R.drawable.ic_sell_delete_20dp)
                } else {
                    negativeButton.setImageResource(R.drawable.ic_sell_remove_20dp)
                }
                positiveButton.isEnabled = count < limit || limit < 0
            }
        } else {
            negativeButton.visibility = GONE
            positiveButton.visibility = GONE
            countText.visibility = GONE
            addButton.visibility = VISIBLE
            addButton.isEnabled = !isDisabled
        }
    }
}