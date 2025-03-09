package com.shubhamgupta16.shopmanager.common

import java.text.FieldPosition

data class RecyclerItem(val case:Int, val position: Int){
    companion object{
        const val CASE_INSERTED = 2
        const val CASE_REMOVED = 3
        const val CASE_CHANGED = 4
    }
}