package com.rafalropel.ecommerceshop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText(context: Context, attrs: AttributeSet ):AppCompatEditText(context, attrs) {

    init {
        applyEditTextFont()
    }

    private fun applyEditTextFont(){
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}