package com.rafalropel.ecommerceshop.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton(context:Context, attrs: AttributeSet):AppCompatButton(context, attrs) {

    init {
        applyButtonFont()
    }

    private fun applyButtonFont(){
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }

}